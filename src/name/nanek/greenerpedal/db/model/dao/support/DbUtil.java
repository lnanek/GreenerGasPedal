
package name.nanek.greenerpedal.db.model.dao.support;

import android.database.sqlite.SQLiteStatement;

import java.util.Date;

public class DbUtil {

    public static void bindNullableString(SQLiteStatement statement, String value, int position) {
        if (null == value) {
            statement.bindNull(position);
        } else {
            statement.bindString(position, value);
        }
    }
    
    public static void bindNullOrInteger(SQLiteStatement statement, final int index, final Integer value) {
        if (null == value) {
            statement.bindNull(index);
        } else {
            statement.bindLong(index, value);
        }
    }

    public static void bindNullOrLong(SQLiteStatement statement, final int index, final Date value) {
        if (null == value) {
            statement.bindNull(index);
        } else {
            statement.bindLong(index, value.getTime());
        }
    }

    public static void bindNullOrLong(SQLiteStatement statement, final int index, final Long value) {
        if (null == value) {
            statement.bindNull(index);
        } else {
            statement.bindLong(index, value);
        }
    }

    public static void bindNullOrFloat(SQLiteStatement statement, final int index, final Float value) {
        if ( null == value ) {
            statement.bindNull(index);
        } else {
            statement.bindDouble(index, value);
        }
    }

}
