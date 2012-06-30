package name.nanek.greenerpedal.activity.support;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.view.View;

public class BitmapUtil {

	private static final String LOG_TAG = BitmapUtil.class.getSimpleName();

	private static final boolean PRETEND_NO_SD = false;

	private static final String IMAGE_OUTPUT_FILE_NAME = "greener_pedal.jpg";

	
	/**
	 * Draws view to a bitmap image.
	 */
	public static Bitmap drawToBitmap(final View viewToDraw) {
		
		final Bitmap bitmap = Bitmap.createBitmap(viewToDraw.getWidth(),
				viewToDraw.getHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);
		viewToDraw.draw(canvas);
		return bitmap;
	}
	
	public static String saveImageAndRecycleBitmap(final Context context, Bitmap bitmap) {
		OutputStream outputStream = null;

		try {
			final File targetFile;
			final File externalStorageDirectory = Environment
					.getExternalStorageDirectory();
			boolean useSdCard = !PRETEND_NO_SD
					&& externalStorageDirectory.exists();
			if (useSdCard) {
				targetFile = new File(externalStorageDirectory,
						IMAGE_OUTPUT_FILE_NAME);
				if (targetFile.exists()) {
					targetFile.delete();
				}
				outputStream = new FileOutputStream(targetFile);

			} else {
				targetFile = context.getFileStreamPath(IMAGE_OUTPUT_FILE_NAME);
				outputStream = context.openFileOutput(IMAGE_OUTPUT_FILE_NAME, Context.MODE_WORLD_READABLE);
			}

			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
			bitmap.recycle();
			bitmap = null;
			System.gc();
			System.gc();

			outputStream.flush();
			outputStream.close();

			// MediaStore.Images.Media.insertImage(getContentResolver(),
			// targetFile.getAbsolutePath(), targetFile.getName()
			// ,targetFile.getName());

			return targetFile.getAbsolutePath();

		} catch (final Exception e) {
			Log.e(LOG_TAG, "Error saving image.", e);
		} finally {
			if (null != bitmap) {
				bitmap.recycle();
				System.gc();
				System.gc();
			}
			if (null != outputStream) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// Do nothing.
				}
			}
		}
		
		return null;
	}
	
}
