package name.nanek.greenerpedal.activity.support;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class ScreenLock {

	private WakeLock wakeLock;
	
	private PowerManager pm;
	
	private String tag;

	public ScreenLock(Context context, String tag) {
		pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		this.tag = tag;
	}

	public void update(Prefs settings) {
		update( settings.keepScreenOn );
	}

	public void update(boolean keepScreenOn) {
		if ( keepScreenOn ) {
			if ( null == wakeLock || !wakeLock.isHeld() ) {				
				wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, tag);
				wakeLock.acquire();
			}
		} else {
			release();			
		}
	}

	public void release() {
		if ( null != wakeLock && wakeLock.isHeld() ) {
			wakeLock.release();
			wakeLock = null;
		}
	}


}
