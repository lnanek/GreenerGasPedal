package name.nanek.greenerpedal.activity.support;

public class TriggersResult {

	public String url;
	
	public int priority;
	
	public float scoreChange;

	public TriggersResult(String url, int priority, float scoreChange) {
		this.url = url;
		this.priority = priority;
		this.scoreChange = scoreChange;
	}
	
}
