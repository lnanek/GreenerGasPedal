package name.nanek.greenerpedal.activity.support;

import java.util.List;

import name.nanek.greenerpedal.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class Prefs {

	private static final String LOG_TAG = "Prefs"; 
	
	public int updateRateMillis;
	
	public boolean automaticTiltCorrection;
	
	public float manualTiltCorrection;
	
	public String idleImageUrl;
	
	public boolean showRawValues;
	
	public boolean keepScreenOn;

	public boolean swapAccelBreak;
	
	public boolean longTermRating;
	
	public float sensitivity;
	
	public int logFileSize;

	public List<Trigger> triggers;

	private Activity activity;

	private SharedPreferences prefs;
	
	public Prefs(Activity activity) {
		this.activity = activity;
		prefs = PreferenceManager.getDefaultSharedPreferences(activity);

		swapAccelBreak = prefs.getBoolean(activity.getString(R.string.preferenceKeySwapAccelBreak), false);
		keepScreenOn = prefs.getBoolean(activity.getString(R.string.preferenceKeyKeepScreenOn), true);
    	idleImageUrl = prefs.getString(activity.getString(R.string.preferenceKeyIdleImage), "");
		showRawValues = prefs.getBoolean(activity.getString(R.string.preferenceKeyShowRawValues), false);		
		automaticTiltCorrection = prefs.getBoolean(activity.getString(R.string.preferenceKeyAutomaticTiltCorrection), false);	
		manualTiltCorrection = Float.parseFloat(prefs.getString(activity.getString(R.string.preferenceKeyManualTiltCorrection), "45"));	
		updateRateMillis = (int) (Float.parseFloat(prefs.getString(activity.getString(R.string.preferenceKeySecondsBeforeIdle), "1")) * 1000);
		longTermRating = prefs.getBoolean(activity.getString(R.string.preferenceKeyLongTermRating), true);
		sensitivity = Float.parseFloat(prefs.getString(activity.getString(R.string.preferenceKeySensitivity), "0.05"));
				
		triggers = Trigger.createTriggers(activity);
		for ( Trigger trigger : triggers ) {
			trigger.updatePrefs(prefs);
		}
	}

	public void updateCalibration(float calibration) {
    	Log.i(LOG_TAG, "Calibration found tiltDegrees: " + calibration);

		this.manualTiltCorrection = calibration;

		Editor edit = prefs.edit();
		edit.putString(activity.getString(R.string.preferenceKeyManualTiltCorrection), "" + calibration);
		edit.commit();
	}

	public void updateSensitivty(float newSensitivity) {

		sensitivity = newSensitivity;
		
		Editor edit = prefs.edit();
		edit.putString(activity.getString(R.string.preferenceKeySensitivity), "" + newSensitivity);
		edit.commit();
	}
		
}
