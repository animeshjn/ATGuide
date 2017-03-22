package edu.gmu.ttaconline.atcguide;


import edu.gmu.ttaconline.atcguide.FeedReaderContract.FeedEntry;
import edu.gmu.ttaconline.atcguide.FeedReaderContract.IntentStore;
import edu.gmu.ttaconline.atcguide.FeedReaderContract.SelectedArea;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 12;
    public static final String DATABASE_NAME = "Student.db";
    private static final String SQL_CREATE_ENTRIES =
    	    "CREATE TABLE " + FeedEntry.STUDENT+ " (" +
    	    FeedEntry.STUDENT_ID+ " TEXT PRIMARY KEY," +
    	    FeedEntry.STUDENT_GRADE+ " TEXT," +
    	    FeedEntry.STUDENT_SCHOOL + " TEXT,"+
    	    FeedEntry.STUDENT_PARTICIPANTS+" TEXT,"+
    	    FeedEntry.STUDENT_DATE+" TEXT,"
    	    +""+FeedEntry.STUDENT_IEPGOAL+" TEXT)";
    private static final String SQL_CREATE_CURRENT_ID="CREATE TABLE CURRENT_DATA (studentid TEXT PRIMARY KEY)";
    private static final String SQL_CREATE_INTENT_ENTRIES =
    	    "CREATE TABLE " + IntentStore.TABLE_NAME+ " (" +
    	    IntentStore.COLUMN_NAME_ID+ " TEXT PRIMARY KEY," +
    	    IntentStore.COLUMN_NAME_INTENT+ " TEXT"+")";
    	private static final String SQL_CREATE_AREALIST="CREATE TABLE "+SelectedArea.TABLE_NAME+"("+SelectedArea.COL_ID+" TEXT,"+SelectedArea.COL_AREA+" TEXT)";
    	
    	private static final String SQL_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + FeedEntry.STUDENT;
    	private static final String SQL_DELETE_ENTRIES_INTENT =
        	    "DROP TABLE IF EXISTS " +IntentStore.TABLE_NAME;
    	private static final String SQL_DELETE_ID="DROP TABLE IF EXISTS CURRENT_DATA";	
    	private static final String SQL_DELETE_AREALIST="DROP TABLE IF EXISTS "+SelectedArea.TABLE_NAME;
    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_INTENT_ENTRIES);
        db.execSQL(SQL_CREATE_CURRENT_ID);
        db.execSQL(SQL_CREATE_AREALIST);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES_INTENT);
        db.execSQL(SQL_DELETE_ID);
        db.execSQL(SQL_DELETE_AREALIST);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

	

	
}