package name.nanek.greenerpedal.activity.support;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore.Images;

public class ShareUtil {
	
	private static final String SHARE_TYPE = "image/jpeg";

	private static final String FILE_URI_PREFIX = "file://";
	
	public static void share(final Activity activity, final String path, final int requestCode) {
		final Intent share = new Intent();

		share.putExtra(Intent.EXTRA_STREAM,	Uri.parse(FILE_URI_PREFIX + path));

		share(activity, share, requestCode);
	}
	
	public static void share(final Activity activity, final Bitmap screenshot, final int requestCode) {
		final Intent share = new Intent();

		String path = Images.Media.insertImage(activity.getContentResolver(), screenshot, "Greener Pedal", null);
		Uri screenshotUri = Uri.parse(path);
		share.putExtra(Intent.EXTRA_STREAM, screenshotUri);

		share(activity, share, requestCode);
	}
	
	private static void share(final Activity activity, final Intent share, final int requestCode) {
		
		share.setAction(Intent.ACTION_SEND);
		share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		share.setType(SHARE_TYPE);

		share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

		String story = "My driving with Greener Pedal";

		share.putExtra(Intent.EXTRA_TEXT, story);
		share.putExtra("sms_body", story);
		share.putExtra(Intent.EXTRA_TITLE, story);
		share.putExtra(Intent.EXTRA_SUBJECT, story);
		activity.startActivityForResult(Intent.createChooser(share, "Choose a way to share:"), requestCode);
	}

}
