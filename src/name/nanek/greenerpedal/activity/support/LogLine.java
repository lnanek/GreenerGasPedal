package name.nanek.greenerpedal.activity.support;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class LogLine {

	private static final String LOG_TAG = "GreenerPedal";
	
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm:ss:SSS");

	private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);

	//private static final DateFormat TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT);

	private Date date;

	private float[] accelerometervalues;

	float tiltDegrees;

	float acceleration;
	
	float breaking;
	
	float cornering; 
	
	String screen;
	
	Float calculatedAcceleration;
	
	Float calculatedBreaking;
	
	Float calculatedCornering; 
	
	//date time x y z tilt accel deaccel corner screen
	public LogLine() {
		date = new Date();		
	}
	
	public void updateRawXYZ(float[] accelerometervalues) {
		this.accelerometervalues = accelerometervalues.clone();
	}

	public void updateRawMeasurements(float acceleration, float breaking, float cornering, float tiltDegrees) {
		this.acceleration = acceleration;
		this.breaking = breaking;
		this.cornering = cornering;
		this.tiltDegrees = tiltDegrees;
	}
	
	public void updateScreen(String screen) {
		this.screen = screen;
	}
	
	public void updateCalculatedMeasurements(Float acceleration, Float breaking, Float cornering) {
		this.calculatedAcceleration = acceleration;
		this.calculatedBreaking = breaking;
		this.calculatedCornering = cornering;
	}
	
	public void log() {
		
		StringBuilder logStringBuilder = new StringBuilder();
		
		logStringBuilder.append(DATE_FORMAT.format(date)).append(", ");
		logStringBuilder.append(TIME_FORMAT.format(date)).append(", ");
		//logStringBuilder.append(SystemClock.uptimeMillis()).append(", ");

		logStringBuilder.append(formatFloat(accelerometervalues[0])).append(", ");
		logStringBuilder.append(formatFloat(accelerometervalues[1])).append(", ");
		logStringBuilder.append(formatFloat(accelerometervalues[2])).append(", ");

		logStringBuilder.append(tiltDegrees).append(", ");

		logStringBuilder.append(formatFloat(acceleration)).append(", ");
		logStringBuilder.append(formatFloat(breaking)).append(", ");
		logStringBuilder.append(formatFloat(cornering)).append(", ");

		logStringBuilder.append(screen).append(", ");

		logStringBuilder.append(formatFloat(calculatedAcceleration)).append(", ");
		logStringBuilder.append(formatFloat(calculatedBreaking)).append(", ");
		logStringBuilder.append(formatFloat(calculatedCornering));

		final String logString = logStringBuilder.toString();
		Log.i(LOG_TAG, logString);
		
	}

	private String formatFloat(Float f) {
		if ( null == f ) {
			return "null";
		}
		return String.format("%.2f", f);
	}
	
}
