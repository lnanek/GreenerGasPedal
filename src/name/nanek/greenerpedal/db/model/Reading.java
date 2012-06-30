package name.nanek.greenerpedal.db.model;



public class Reading {
	
	private Long mRowId;
	
	private float mGasUse;
	
	private float mBreakUse;
	
	private float mMoneySaved;
	
	private int mDriveNumber;
	
	private long mTime;

	public Reading(Long rowId, float gasUse, float breakUse, float moneySaved, int driveNumber, long time) {
		mRowId = rowId;
		mGasUse = gasUse;
		mBreakUse = breakUse;
		mMoneySaved = moneySaved;
		mDriveNumber = driveNumber;
		mTime = time;
	}

	public Reading() {
	}

	public Long getRowId() {
		return mRowId;
	}

	public void setRowId(Long rowId) {
		mRowId = rowId;
	}
	
	public int getDriveNumber() {
		return mDriveNumber;
	}

	public void setDriveNumber(int driveNumber) {
		mDriveNumber = driveNumber;
	}

	public long getTime() {
		return mTime;
	}

	public void setTime(long time) {
		mTime = time;
	}

	public float getGasUse() {
		return mGasUse;
	}

	public void setGasUse(float gasUse) {
		mGasUse = gasUse;
	}

	public float getBreakUse() {
		return mBreakUse;
	}

	public void setBreakUse(float breakUse) {
		mBreakUse = breakUse;
	}

	public float getMoneySaved() {
		return mMoneySaved;
	}

	public void setMoneySaved(float moneySaved) {
		mMoneySaved = moneySaved;
	}

	
	
}
