package name.nanek.greenerpedal.activity.support;

import name.nanek.greenerpedal.R;
import name.nanek.greenerpedal.activity.DisplayReadingsActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

public class Views {

	public TextView accelerationRawValue;
	
	public TextView breakingRawValue;
	
	public TextView corneringRawValue;
	
	public TextView tiltRawValue;
	
	public TextView xRawValue;	
	
	public TextView yRawValue;
	
	public TextView zRawValue;
	
	public View mainRawValuesArea;
	
	public WebView webView;
	
	public TextView mainScore;
	
	public Views(DisplayReadingsActivity displayReadingsActivity) {
		
		//displayActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		//displayActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        //                       WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		APISafeKeyguardHider.hideKeyguard(displayReadingsActivity);
		
		displayReadingsActivity.setContentView(R.layout.display);
		
		mainRawValuesArea = displayReadingsActivity.findViewById(R.id.mainRawValuesArea);
		accelerationRawValue = (TextView) displayReadingsActivity.findViewById(R.id.displayAccelerationValue);
		breakingRawValue = (TextView) displayReadingsActivity.findViewById(R.id.displayBreakingValue);
		corneringRawValue = (TextView) displayReadingsActivity.findViewById(R.id.displayCorneringValue);
		tiltRawValue = (TextView) displayReadingsActivity.findViewById(R.id.displayTiltValue);
		mainScore = (TextView) displayReadingsActivity.findViewById(R.id.mainScore);
		
		xRawValue = (TextView) displayReadingsActivity.findViewById(R.id.displayXRawValue);
		yRawValue = (TextView) displayReadingsActivity.findViewById(R.id.displayYRawValue);
		zRawValue = (TextView) displayReadingsActivity.findViewById(R.id.displayZRawValue);		
		
		webView = (WebView) displayReadingsActivity.findViewById(R.id.mainRatingImage);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setVerticalScrollBarEnabled(false);
	}

	public void updateRawXYZ(float[] accelerometervalues) {
		xRawValue.setText(String.format("%.2f", accelerometervalues[0]));
		yRawValue.setText(String.format("%.2f", accelerometervalues[1]));
		zRawValue.setText(String.format("%.2f", accelerometervalues[2]));
	}

	public void updateRawMeasurements(float acceleration, float breaking, float cornering, float tiltDegrees) {
		accelerationRawValue.setText(String.format("%.2f", acceleration));
		breakingRawValue.setText(String.format("%.2f", breaking));
		corneringRawValue.setText(String.format("%.2f", cornering));
		tiltRawValue.setText(String.format("%.2f", tiltDegrees));
	}

	public void showScore(float currentScore) {
		mainScore.setText("$" + String.format("%.2f", currentScore));
	}

}
