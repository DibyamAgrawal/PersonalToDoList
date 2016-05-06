package in.ac.nitrkl.personalto_dolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;


public class DBAdapter {

	private static final String TAG = "DBAdapter";
	
	// DB Fields
	public static final String KEY_ROWID = "_id";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_TIME = "time";
	public static final String KEY_LOCATION = "location";

    public static final int COL_ROWID = 0;
	public static final int COL_MESSAGE = 1;
	public static final int COL_TIME = 2;
	public static final int COL_LOCATION = 3;
	
	public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_MESSAGE, KEY_TIME, KEY_LOCATION};

	public static final String DATABASE_NAME = "ToDo";
	public static final String DATABASE_TABLE = "data";
	public static final int DATABASE_VERSION = 1;

    private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

	private static final String DATABASE_CREATE_SQL = 
			"create table " + DATABASE_TABLE 
			+ " (" + KEY_ROWID + " integer primary key autoincrement, "
			+ KEY_MESSAGE + " string not null, "
			+ KEY_TIME + " string not null, "
			+ KEY_LOCATION + " string not null"
			+ ");";


	public DBAdapter(Context ctx) {
		this.context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}

	public DBAdapter open() {
		db = myDBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		myDBHelper.close();
	}

	public long insertRow(String message, Date time, String location) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_MESSAGE, message);
		initialValues.put(KEY_TIME, time+"");
		initialValues.put(KEY_LOCATION, location);

		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		return db.delete(DATABASE_TABLE, where, null) != 0;
	}
	
	public void deleteAll() {
		Cursor c = getAllRows();
		long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
		if (c.moveToFirst()) {
			do {
				deleteRow(c.getLong((int) rowId));				
			} while (c.moveToNext());
		}
		c.close();
	}

	public Cursor getAllRows() {
		String where = null;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
							where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public Cursor getRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
						where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	

	public boolean updateRow(long rowId, String message, Date time, String location) {
		String where = KEY_ROWID + "=" + rowId;

		ContentValues newValues = new ContentValues();
		newValues.put(KEY_MESSAGE, message);
		newValues.put(KEY_TIME, time+"");
		newValues.put(KEY_LOCATION, location);

		return db.update(DATABASE_TABLE, newValues, where, null) != 0;
	}
	



	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE_SQL);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading application's database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");
			
			// Destroy old database:
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			
			// Recreate new database:
			onCreate(_db);
		}
	}
}
