package name.nanek.greenerpedal.activity.support;

import java.util.LinkedList;
import java.util.List;

import name.nanek.greenerpedal.R;
import android.content.Context;
import android.content.SharedPreferences;

public class Trigger {
	
	public enum Category{ ACCELERATION, BREAKING, CORNERING };
	
	private int priority;
	
	private float lowThreshold;
	
	private float highThreshold;
	
	private float score;
	
	private String imageUrl;
	
	private String priorityKey;
	
	private String lowThresholdKey;
	
	private String highThresholdKey;
	
	private String scoreKey;
	
	private String imageUrlKey;

	private Category category;
	
	private String contentDescription;
	
	public Trigger(Context context, int priorityKeyId, int lowThresholdKeyId, int highThresholdKeyId, int scoreKeyId, 
			int imageUrlKeyId, Category category, String contentDescription) {
		priorityKey = context.getString(priorityKeyId);
		lowThresholdKey = context.getString(lowThresholdKeyId);
		highThresholdKey = context.getString(highThresholdKeyId);
		scoreKey = context.getString(scoreKeyId);
		imageUrlKey = context.getString(imageUrlKeyId);
		this.category = category;
		this.contentDescription = contentDescription;
	}
	
	public void updatePrefs(SharedPreferences prefs) {
		priority = Integer.parseInt(prefs.getString(priorityKey, "" + Integer.MAX_VALUE));
		lowThreshold = Float.parseFloat(prefs.getString(lowThresholdKey, "" + Float.MAX_VALUE));
		highThreshold = Float.parseFloat(prefs.getString(highThresholdKey, "" + Float.MAX_VALUE));
		score = Float.parseFloat(prefs.getString(scoreKey, "0"));
		imageUrl = prefs.getString(imageUrlKey, "");
	}
	
	public boolean updateResult(TriggersResult result, Float acceleration, Float breaking, Float cornering) {
		if ( priority > result.priority ) {
			return false;
		}
		
		Float measurement = Category.ACCELERATION == category ? acceleration : Category.BREAKING == category ? breaking : cornering;
		if ( null == measurement ) {
			return false;
		}
		
		if ( measurement > lowThreshold && measurement <= highThreshold ) {
			result.priority = priority;
			result.url = imageUrl;
			result.scoreChange = score;
			result.contentDescription = contentDescription;
			return true;
		}
		
		return false;
	}
	
	public static List<Trigger> createTriggers(Context context) {
		List<Trigger> triggers = new LinkedList<Trigger>();
		
		triggers.add(new Trigger(context, 
				R.string.preferenceKeyAccelerationGoodPriority, 
				R.string.preferenceKeyAccelerationGoodLowThreshold, 
				R.string.preferenceKeyAccelerationGoodHighThreshold, 
				R.string.preferenceKeyAccelerationGoodScore, 
				R.string.preferenceKeyAccelerationGoodImage,
				Category.ACCELERATION, "Good Acceleration"));
		triggers.add(new Trigger(context, 
				R.string.preferenceKeyAccelerationMediumPriority, 
				R.string.preferenceKeyAccelerationMediumLowThreshold, 
				R.string.preferenceKeyAccelerationMediumHighThreshold, 
				R.string.preferenceKeyAccelerationMediumScore, 
				R.string.preferenceKeyAccelerationMediumImage,
				Category.ACCELERATION, "OK Acceleration"));
		triggers.add(new Trigger(context, 
				R.string.preferenceKeyAccelerationBadPriority, 
				R.string.preferenceKeyAccelerationBadLowThreshold, 
				R.string.preferenceKeyAccelerationBadHighThreshold, 
				R.string.preferenceKeyAccelerationBadScore, 
				R.string.preferenceKeyAccelerationBadImage,
				Category.ACCELERATION, "Bad Acceleration"));

		triggers.add(new Trigger(context, 
				R.string.preferenceKeyBreakingGoodPriority, 
				R.string.preferenceKeyBreakingGoodLowThreshold, 
				R.string.preferenceKeyBreakingGoodHighThreshold, 
				R.string.preferenceKeyBreakingGoodScore, 
				R.string.preferenceKeyBreakingGoodImage,
				Category.BREAKING, "Good Breaking"));
		triggers.add(new Trigger(context, 
				R.string.preferenceKeyBreakingMediumPriority, 
				R.string.preferenceKeyBreakingMediumLowThreshold, 
				R.string.preferenceKeyBreakingMediumHighThreshold, 
				R.string.preferenceKeyBreakingMediumScore, 
				R.string.preferenceKeyBreakingMediumImage,
				Category.BREAKING, "Medium Breaking"));
		triggers.add(new Trigger(context, 
				R.string.preferenceKeyBreakingBadPriority, 
				R.string.preferenceKeyBreakingBadLowThreshold, 
				R.string.preferenceKeyBreakingBadHighThreshold, 
				R.string.preferenceKeyBreakingBadScore, 
				R.string.preferenceKeyBreakingBadImage,
				Category.BREAKING, "Bad Breaking"));

		triggers.add(new Trigger(context, 
				R.string.preferenceKeyCorneringGoodPriority, 
				R.string.preferenceKeyCorneringGoodLowThreshold, 
				R.string.preferenceKeyCorneringGoodHighThreshold, 
				R.string.preferenceKeyCorneringGoodScore, 
				R.string.preferenceKeyCorneringGoodImage,
				Category.CORNERING, "Good Cornering"));
		triggers.add(new Trigger(context, 
				R.string.preferenceKeyCorneringMediumPriority, 
				R.string.preferenceKeyCorneringMediumLowThreshold, 
				R.string.preferenceKeyCorneringMediumHighThreshold, 
				R.string.preferenceKeyCorneringMediumScore, 
				R.string.preferenceKeyCorneringMediumImage,
				Category.CORNERING, "Medium Cornering"));
		triggers.add(new Trigger(context, 
				R.string.preferenceKeyCorneringBadPriority, 
				R.string.preferenceKeyCorneringBadLowThreshold, 
				R.string.preferenceKeyCorneringBadHighThreshold, 
				R.string.preferenceKeyCorneringBadScore, 
				R.string.preferenceKeyCorneringBadImage,
				Category.CORNERING, "Bad Cornering"));
		
		return triggers;
	}
	
}
