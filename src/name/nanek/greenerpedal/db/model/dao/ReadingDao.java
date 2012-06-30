package name.nanek.greenerpedal.db.model.dao;


import java.util.ArrayList;
import java.util.List;

import name.nanek.greenerpedal.db.model.Reading;
import name.nanek.greenerpedal.db.model.dao.support.DbUtil;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class ReadingDao {
	
	static final String TABLE_NAME = "readings";
	
	private static final String ORDER = "id asc";

	private static final String[] FIELDS = new String[] { 
		"id", "gas_use", "break_use", "money_saved", "drive_number", "time"
	};

	private SQLiteStatement mInsertReading;

	private static final String INSERT_READING =
			"insert into " + TABLE_NAME + "(" +
					"gas_use, break_use, money_saved, drive_number, time" +
					") values (?, ?, ?, ?, ?)";

	private SQLiteDatabase mDB;

	public void init(final SQLiteDatabase db) {
	    mDB = db;
		mInsertReading = mDB.compileStatement(INSERT_READING);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " +
						TABLE_NAME + "(" +
						"id INTEGER PRIMARY KEY, " +
						"gas_use REAL, " +
						"break_use REAL, " +
						"money_saved REAL, " +
						"drive_number INTEGER, " +
						"time INTEGER" +						
						")");
	      init(db);
	}
	
    public void onPostCreate(SQLiteDatabase db) {
    }

    public void onOpen(SQLiteDatabase db) {
        init(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        init(db);
    }

    public void insert(Reading reading) {
        bindNonId(mInsertReading, reading);

        final long rowId = mInsertReading.executeInsert();
        reading.setRowId(rowId);
    }

	private void bindNonId(SQLiteStatement statement, Reading reading) {
		DbUtil.bindNullOrFloat(statement, 1, reading.getGasUse());
		DbUtil.bindNullOrFloat(statement, 2, reading.getBreakUse());
		DbUtil.bindNullOrFloat(statement, 3, reading.getMoneySaved());
		statement.bindLong(4, reading.getDriveNumber());
		statement.bindLong(5, reading.getTime());
	}
	
	public Reading get(long rowId) {
		Cursor cursor = mDB.query(TABLE_NAME, FIELDS, "id = ?", new String[] {Long.toString(rowId)}, null, null, ORDER);

		Reading reading = null;
		while (cursor.moveToNext()) {
			reading = fromCursor(cursor);
		}
		cursor.close();

		return reading;
	}

	public List<Reading> list(long oldestTime) {
		
		//final Cursor cursor = mDB.query(TABLE_NAME, FIELDS, null, null, null, null, ORDER);
		
		Cursor cursor = mDB.query(TABLE_NAME, FIELDS, "time >= ?", 
				new String[] {Long.toString(oldestTime)}, null, null, ORDER);

		List<Reading> list = new ArrayList<Reading>(cursor.getCount());

		while (cursor.moveToNext()) {
			final Reading reading = fromCursor(cursor);
			list.add(reading);
		}
		cursor.close();

		return list;
	}
	
	public Reading getLastReading() {
		
		final Cursor cursor = mDB.query(TABLE_NAME, FIELDS, null, null, null, null, ORDER);

		try {
			if (cursor.moveToNext()) {
				final Reading reading = fromCursor(cursor);
				return reading;
			}
		} finally {
			cursor.close();
		}

		return null;
	}
	
	public Integer getCurrentDriveNumber() {
		
		final Reading reading = getLastReading();
		if ( null != reading ) {
			return reading.getDriveNumber();
		}
		
		return null;
	}
	
	public int startNewDrive() {
		
		final Integer currentDriveNumber = getCurrentDriveNumber();
		if ( null != currentDriveNumber ) {
			//mDB.delete(TABLE_NAME, "drive_number <= ?", new String[] {Long.toString(currentDriveNumber)});			
			mDB.delete(TABLE_NAME, null, null);		
			return currentDriveNumber + 1;
		}
		
		return 1;
	}
	
	private Reading fromCursor(Cursor cursor) {
		final long id = cursor.getLong(0);
		
		final Float gasUse = getFloatOrNull(cursor, 1);
		final Float breakUse = getFloatOrNull(cursor, 2);
		final Float moneySaved = getFloatOrNull(cursor, 3);
		
		final int driveNumber = cursor.getInt(4);
		final Integer time = cursor.getInt(5);

		final Reading reading = new Reading(id, gasUse, breakUse, moneySaved, driveNumber, time);
		
		return reading;
	}
	
	private Float getFloatOrNull(Cursor cursor, int index) {
		return cursor.isNull(index) ? null : cursor.getFloat(index);
	}

}
