/**
 * 
 */
package name.nanek.greenerpedal;

import android.app.Application;
import android.preference.PreferenceManager;

/**
 * @author Lance Nanek
 *
 */
public class GreenerPedalApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
				
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	}

}
