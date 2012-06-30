
package name.nanek.greenerpedal.db.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelper {

    private static final String DATABASE_NAME = "greener_pedal.db";

    private static final int DATABASE_VERSION = 1;

    public final ReadingDao readings;

    private SQLiteDatabase mDB;

    public DataHelper(final Context context) {

        readings = new ReadingDao();
        OpenHelper openHelper = new OpenHelper(context);
        mDB = openHelper.getWritableDatabase();
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
           	readings.onCreate(db);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
           	readings.onOpen(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           	readings.onUpgrade(db, oldVersion, newVersion);
        }
    }

    public void close() {
        mDB.close();
        mDB = null;
    }
}
