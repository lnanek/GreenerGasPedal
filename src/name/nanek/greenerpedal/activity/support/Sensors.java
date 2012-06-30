package name.nanek.greenerpedal.activity.support;

import name.nanek.greenerpedal.activity.DisplayReadingsActivity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class Sensors {

	public SensorManager sensorManager;
	
	public Sensor accelerometer;
	
	public Sensor orientationSensor;

	public Sensors(DisplayReadingsActivity displayReadingsActivity, Prefs prefs) {
        sensorManager = (SensorManager) displayReadingsActivity.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

		sensorManager.registerListener(displayReadingsActivity, accelerometer, SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(displayReadingsActivity, orientationSensor, SensorManager.SENSOR_DELAY_UI);
	}

	public void unregister(DisplayReadingsActivity displayReadingsActivity) {
		sensorManager.unregisterListener(displayReadingsActivity);
	}

}
