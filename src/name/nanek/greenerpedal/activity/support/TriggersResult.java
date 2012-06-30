package name.nanek.greenerpedal.activity.support;

public class TriggersResult {

	public String url;
	
	public int priority;
	
	public float scoreChange;
	
	public String contentDescription;

	public TriggersResult(String url, int priority, float scoreChange, String contentDescription) {
		this.url = url;
		this.priority = priority;
		this.scoreChange = scoreChange;
		this.contentDescription = contentDescription;
	}
	
}
