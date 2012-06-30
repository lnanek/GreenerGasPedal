package name.nanek.greenerpedal.activity;

import name.nanek.greenerpedal.R;
import name.nanek.greenerpedal.activity.support.BitmapUtil;
import name.nanek.greenerpedal.activity.support.LogLine;
import name.nanek.greenerpedal.activity.support.LowPassFilter;
import name.nanek.greenerpedal.activity.support.Prefs;
import name.nanek.greenerpedal.activity.support.ScreenLock;
import name.nanek.greenerpedal.activity.support.Sensors;
import name.nanek.greenerpedal.activity.support.ShareUtil;
import name.nanek.greenerpedal.activity.support.Trigger;
import name.nanek.greenerpedal.activity.support.TriggersResult;
import name.nanek.greenerpedal.activity.support.Views;
import name.nanek.greenerpedal.db.model.Reading;
import name.nanek.greenerpedal.db.model.dao.DataHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

public class DisplayReadingsActivity extends Activity implements SensorEventListener {
	
	//TODO more accessibility
	
	//TODO voice overs so don't have to look so closely

	private static final int TIME_BETWEEN_DATABASE_INSERTS_MILLIS = 1000;

	private static final String LOG_TAG = "DisplayActivity";
	
	private Views views;
	
	private Sensors sensors;

	private Prefs prefs;
	
	private ScreenLock lock;
		
	private float currentScore;
	
	private Float lastOrientationSensorTilt;
	
	private long lastUptimeMillisWhenShowedNonIdleImage;

	private boolean calibrationRequested;
	
	private LowPassFilter filter;
	
	private LogLine logLine;
	
	private DataHelper data;
	
	private int driveNumber;
	
	private float lastScoreRate;
	
	private long lastUpdateDisplayTimeMillis;
	
	private String currentlyShownUrl;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		data = new DataHelper(this);				
		if ( null == savedInstanceState ) {
			driveNumber = data.readings.startNewDrive();
		} else {
			final Reading reading = data.readings.getLastReading();
			driveNumber = reading.getDriveNumber();
			currentScore = reading.getMoneySaved();
		}
		
		prefs = new Prefs(this);
		lock = new ScreenLock(this, "GreenerPedal " + getClass().getSimpleName());		
		views = new Views(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		//Keep screen on even if we're paused.
		lock.update(prefs);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		prefs = new Prefs(this);
		lock.update(prefs);
		views.mainRawValuesArea.setVisibility(prefs.showRawValues ? View.VISIBLE : View.GONE);
		views.mainScore.setVisibility(prefs.longTermRating ? View.VISIBLE : View.GONE);
		filter = new LowPassFilter(prefs);
		lastOrientationSensorTilt = null;		
		sensors = new Sensors(this, prefs);
	}
	
	@Override
	protected void onPause() {
		super.onPause();

		sensors.unregister(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		lock.release();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_menu, menu);
        return true;
    } 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

	    switch (item.getItemId()) {
	        case R.id.menuItemSettings:
	        	final Intent prefsIntent = new Intent(this, EditPrefsActivity.class);
	        	startActivity(prefsIntent);
	        	return true;

	        case R.id.menuItemGraphAndShare:
	        	final Intent graphIntent = new Intent(this, GraphAndShareActivity.class);
	        	startActivity(graphIntent);
	        	return true;

	        case R.id.menuItemCalibrate:
	        	calibrationRequested = true;
	        	return true;
	        	
	        case R.id.menuItemShare:
	        	ShareUtil.share(this, BitmapUtil.drawToBitmap(findViewById(R.id.content)), 0);
	        	return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}	
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
	
	//Long lastAccelOnSensorChanged;

    public void onSensorChanged(SensorEvent event) {
    	
       if ( Sensor.TYPE_ORIENTATION == event.sensor.getType() ) {

        	//Log.i(LOG_TAG, "azimuth, pitch, roll: " + event.values[0] + ", " + event.values[1] + ", " + event.values[2]);
        	// 45, -90, 0 is straight up and down, portrait mode.
        	// 45, -45, 0 is tilted back so screen half faces the sky

    		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int orientation = display.getOrientation();
            if ( Surface.ROTATION_90 == orientation ) {
            	lastOrientationSensorTilt = (90 - event.values[2]);
           } else if ( Surface.ROTATION_270 == orientation ) {
           		lastOrientationSensorTilt = (90 + event.values[2]);
           } else {
            	lastOrientationSensorTilt = (90 + event.values[1]);            	
            }

        	
        	//Log.i(LOG_TAG, "orientation sensor tiltDegrees: " + lastOrientationSensorTilt);
        	
            return;
        }
        
        if ( Sensor.TYPE_ACCELEROMETER != event.sensor.getType() ) {
        	return;
        }
        
        /*
        long now = SystemClock.uptimeMillis();
        if ( null != lastAccelOnSensorChanged ) {
        	long timeElapsed = now - lastAccelOnSensorChanged;
        	Log.i(LOG_TAG, "time elapsed: " + timeElapsed);
        }
        lastAccelOnSensorChanged = now;
        */

    	float[] roatatedAccelValues = event.values.clone();;        
        views.updateRawXYZ(roatatedAccelValues);

        logLine = new LogLine();        
        logLine.updateRawXYZ(roatatedAccelValues);
        
        // Adjust for landscape orientations
    	adjustForOrientation(roatatedAccelValues);
    	
        if ( calibrationRequested ) {
        	prefs.updateCalibration(getCalibration(roatatedAccelValues[2]));
        	calibrationRequested = false;
        }
        
        // Correct for tilt.
        float tiltDegrees;
        if ( prefs.automaticTiltCorrection ) {
        	if ( null == lastOrientationSensorTilt ) {
        		return;
        	}
        	// TODO maybe allow calibration while using auto too? would be added/subtracted to sensor value
        	tiltDegrees = lastOrientationSensorTilt;
        } else {
        	tiltDegrees = prefs.manualTiltCorrection;        	
        }
                
        //Adjust for tilt
        //Log.i(LOG_TAG, "tiltDegrees: " + tiltDegrees);
        
        tiltDegrees = tiltDegrees < 0 ? 0 : tiltDegrees > 89 ? 89 : tiltDegrees;	        
        float measuredForce = roatatedAccelValues[2];
    	float gravityOnMeasuredAxis = getGravityOnMeasuredAxis(tiltDegrees);
        float tiltRadians = toRadians(tiltDegrees);
    	float measuredAccelerationForceToRealForce = (float) Math.cos(tiltRadians);	    		    	
    	float measuredAcceleration = measuredForce - gravityOnMeasuredAxis;
    	float actualAcceleration = measuredAcceleration / measuredAccelerationForceToRealForce;

    	roatatedAccelValues[2] = actualAcceleration;

        	
        // Convert X, Y, Z to acceleration, breaking, and cornering
    	float acceleration = -roatatedAccelValues[2];
    	float breaking = roatatedAccelValues[2];
    	float cornering = Math.abs(roatatedAccelValues[0]);
    	// roatatedValues[1] is gravity when the phone isn't tilted, so isn't used.
    	
    	if ( prefs.swapAccelBreak ) {
    		acceleration = -acceleration;
    		breaking = - breaking;
    	}

    	acceleration = Math.max(acceleration, 0);
   		breaking = Math.max(breaking, 0);
    	
    	filter.updateRawMeasurements(acceleration, breaking, cornering);
    	views.updateRawMeasurements(acceleration, breaking, cornering, tiltDegrees);
        logLine.updateRawMeasurements(acceleration, breaking, cornering, tiltDegrees);
        		
        
              
		long currentUpTimeMillis = SystemClock.uptimeMillis();
		long timeSinceLastShowedNonIdleMillis = currentUpTimeMillis - lastUptimeMillisWhenShowedNonIdleImage;
		
		// Ignore if we just showed something, give the user time to see it.		
		if ( timeSinceLastShowedNonIdleMillis < prefs.updateRateMillis ) {
			logLine.log();
			logLine = null;
			return;
		}
		    	
    	int priority = Integer.MAX_VALUE;
    	TriggersResult result = new TriggersResult(prefs.idleImageUrl, priority, 1.5f);
		for ( Trigger trigger : prefs.triggers ) {
			if ( trigger.updateResult(result, filter.getAcceleration(), filter.getBreaking(), filter.getCornering()) ) {
				lastUptimeMillisWhenShowedNonIdleImage = SystemClock.uptimeMillis();
			}
		}    	

		long timeSinceLastUpdateDisplayFullyRanMillis = currentUpTimeMillis - lastUpdateDisplayTimeMillis;
		currentScore += timeSinceLastUpdateDisplayFullyRanMillis * lastScoreRate / ( 1000 * 60 * 60 );				
		lastScoreRate = result.scoreChange;
    	views.showScore(currentScore);

		if ( null == currentlyShownUrl || !currentlyShownUrl.equals(result.url) ) {
			showImage(result.url);
    		currentlyShownUrl = result.url;
		}
		
    	logLine.updateCalculatedMeasurements(filter.getAcceleration(), filter.getBreaking(), filter.getCornering());
    	logLine.updateScreen(result.url);
		logLine.log();
		logLine = null;		
		
		if ( timeSinceLastUpdateDisplayFullyRanMillis > TIME_BETWEEN_DATABASE_INSERTS_MILLIS ) {
			float breakUse = Math.max(cornering, breaking);        
       		final Reading reading = new Reading(null, acceleration, breakUse, currentScore, driveNumber, currentUpTimeMillis);
       		data.readings.insert(reading);
		}
       	
		lastUpdateDisplayTimeMillis = currentUpTimeMillis;
    }
    
	private void adjustForOrientation(float[] roatatedAccelValues) {
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getOrientation();
        if ( Surface.ROTATION_90 == orientation ) {
        	float swapTemp = roatatedAccelValues[0];
        	roatatedAccelValues[0] = roatatedAccelValues[1];// Correct
        	roatatedAccelValues[1] = swapTemp;
        	roatatedAccelValues[2] = roatatedAccelValues[2];        	
        } else if ( Surface.ROTATION_270 == orientation ) {
        	float swapTemp = roatatedAccelValues[0];
        	//Two stays 2.
        	roatatedAccelValues[0] = roatatedAccelValues[1];
        	roatatedAccelValues[1] = swapTemp;
        }
	}
    
    private float getGravityOnMeasuredAxis(float tiltDegrees) {
        float tiltRadians = toRadians(tiltDegrees);
        float measuredGravityForceToRealForce = (float) Math.sin(tiltRadians);	        
    	float gravityOnMeasuredAxis = SensorManager.GRAVITY_EARTH * measuredGravityForceToRealForce;
    	return gravityOnMeasuredAxis;
    }

	private float toRadians(float tiltDegrees) {
		return (float) (tiltDegrees * Math.PI / 180.0f);
	}

	private Float getCalibration(float measured) {
		Float closestMatch = null;
		Float absoluteDifference = null;
		for( int tiltDegrees = 0; tiltDegrees < 90; tiltDegrees++) {

	    	float gravityOnMeasuredAxis = getGravityOnMeasuredAxis(tiltDegrees);
	    	
	    	float newDifference = Math.abs(gravityOnMeasuredAxis - measured);
	    	if ( null == closestMatch || newDifference < absoluteDifference ) {
	    		closestMatch = (float) tiltDegrees;
	    		absoluteDifference = newDifference;
	    	}
		}
		return closestMatch;
	}
	
	private void showImage(String imageUrl) {
		String summary = "<html><link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\" /><body><img src=\"" + imageUrl + "\"  /></body></html>";
		//Log.i("DisplayActivity", summary);
		views.webView.loadDataWithBaseURL("file:///android_asset/", summary, "text/html", "utf-8", null);		
	}
		
}