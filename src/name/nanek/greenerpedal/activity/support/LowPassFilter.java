package name.nanek.greenerpedal.activity.support;


public class LowPassFilter {
	
	private float sensitivity;
	
	private Float acceleration;
	
	private Float breaking;
	
	private Float cornering;

	public LowPassFilter(Prefs prefs) {
		sensitivity = prefs.sensitivity;
	}

	public void updateRawMeasurements(float newAcceleration, float newBreaking, float newCornering) {
		acceleration = filter(acceleration, newAcceleration);
		breaking = filter(breaking, newBreaking);
		cornering = filter(cornering, newCornering);		
	}

	private Float filter(Float calculated, float input) {
		if ( null == calculated ) {
			return input;
		}
		
		float delta = input - calculated;
		float appliedDelta = sensitivity * delta;
		return calculated + appliedDelta;
	}

	public Float getAcceleration() {
		return acceleration;
	}

	public Float getBreaking() {
		return breaking;
	}

	public Float getCornering() {
		return cornering;
	}

}
