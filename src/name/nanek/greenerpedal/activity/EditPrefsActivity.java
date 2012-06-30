/**
 * 
 */
package name.nanek.greenerpedal.activity;

import name.nanek.greenerpedal.R;
import name.nanek.greenerpedal.activity.support.APISafeKeyguardHider;
import name.nanek.greenerpedal.activity.support.Prefs;
import name.nanek.greenerpedal.activity.support.ScreenLock;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

/**
 * @author Lance Nanek
 *
 */
public class EditPrefsActivity extends PreferenceActivity {

	private Preference manualTiltCorrection;
	
	private ScreenLock lock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lock = new ScreenLock(this, "GreenerPedal " + getClass().getSimpleName());
		APISafeKeyguardHider.hideKeyguard(this);
		
		addPreferencesFromResource(R.xml.preferences);
		
		String preferenceKeyManualTiltCorrection = getString(R.string.preferenceKeyManualTiltCorrection);		
		manualTiltCorrection = getPreferenceManager().findPreference(preferenceKeyManualTiltCorrection);

		// Disable manual tilt correction when automatic is checked.
		String preferenceKeyAutomaticTiltCorrection = getString(R.string.preferenceKeyAutomaticTiltCorrection);
		Preference automaticTiltCorrection = getPreferenceManager().findPreference(preferenceKeyAutomaticTiltCorrection);
		automaticTiltCorrection.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {

				Boolean automaticTiltCorrectionValue = (Boolean) newValue;
				manualTiltCorrection.setEnabled(!automaticTiltCorrectionValue);	
				
				return true;
			}
		});		

		// Update screen lock when keep screen on setting changed.
		String preferenceKeyKeepScreenOn = getString(R.string.preferenceKeyKeepScreenOn);
		Preference keepScreenOn = getPreferenceManager().findPreference(preferenceKeyKeepScreenOn);
		keepScreenOn.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				Boolean keepScreenOnValue = (Boolean) newValue;
				lock.update(keepScreenOnValue);
				
				return true;
			}
		});				

		// Constrain sensitivity values.
		String preferenceKeySensitivty = getString(R.string.preferenceKeySensitivity);
		Preference sensitivity = getPreferenceManager().findPreference(preferenceKeySensitivty);
		sensitivity.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				float newSensitivity = Float.parseFloat((String) newValue);

				if ( newSensitivity < 0 ) {
					Prefs prefs = new Prefs(EditPrefsActivity.this);
					prefs.updateSensitivty(0);
					return false;
				}

				if ( newSensitivity > 1.0 ) {
					Prefs prefs = new Prefs(EditPrefsActivity.this);
					prefs.updateSensitivty(1.0f);
					return false;
				}

				return true;
			}
		});	
	}

	private void setOnClickListener(final int keyId, final OnPreferenceClickListener finishClickListener) {
		String preferenceKeyReturnToApp = getString(keyId);	
		Preference returnToApp = getPreferenceManager().findPreference(preferenceKeyReturnToApp);
		returnToApp.setOnPreferenceClickListener(finishClickListener);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		//Keep screen on even if we're paused.
		Prefs prefs = new Prefs(EditPrefsActivity.this);
		lock.update(prefs);
	}

	@Override
	protected void onStop() {
		super.onStop();
		lock.release();
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		Prefs prefs = new Prefs(this);
		manualTiltCorrection.setEnabled(!prefs.automaticTiltCorrection);	
	}
	
}
