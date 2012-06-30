package name.nanek.greenerpedal.activity.support;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

public class APISafeKeyguardHider {
	
	//Perform SDK5+ only fields.
	private static final class SDK5 {
		
		public static void hideKeyguard(Activity activity) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		}
		
	}	
	
	public static void hideKeyguard(Activity activity) {
		if ( Integer.parseInt(Build.VERSION.SDK) < 5 ) {
			return;
		}
		
		SDK5.hideKeyguard(activity);
	}
	
}
