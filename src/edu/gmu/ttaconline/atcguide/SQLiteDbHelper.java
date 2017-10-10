package edu.gmu.ttaconline.atcguide;

import edu.gmu.ttaconline.atcguide.SQLiteContract.ATStore;
import edu.gmu.ttaconline.atcguide.SQLiteContract.AreaStore;
import edu.gmu.ttaconline.atcguide.SQLiteContract.FeedEntry;
import edu.gmu.ttaconline.atcguide.SQLiteContract.IntentStore;
import edu.gmu.ttaconline.atcguide.SQLiteContract.SelectedArea;
import edu.gmu.ttaconline.atcguide.SQLiteContract.StrategyStore;
import edu.gmu.ttaconline.atcguide.SQLiteContract.TaskStore;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Modification of Android Developers example
 * 
 * @author Animesh Jain
 */
public class SQLiteDbHelper extends SQLiteOpenHelper {
	// If you change the database schema, you must increment the database
	// version.
	public static final int DATABASE_VERSION = 26;
	public static final String DATABASE_NAME = "Student.db";
	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ FeedEntry.STUDENT + " (" + FeedEntry.STUDENT_ID
			+ " TEXT PRIMARY KEY," + FeedEntry.STUDENT_GRADE + " TEXT,"
			+ FeedEntry.STUDENT_SCHOOL + " TEXT,"
			+ FeedEntry.STUDENT_PARTICIPANTS + " TEXT,"
			+ FeedEntry.STUDENT_DATE + " TEXT," + ""
			+ FeedEntry.STUDENT_IEPGOAL + " TEXT)";
	private static final String SQL_CREATE_CURRENT_ID = "CREATE TABLE CURRENT_DATA (studentid TEXT PRIMARY KEY)";
	private static final String SQL_CREATE_INTENT_ENTRIES = "CREATE TABLE "
			+ IntentStore.TABLE_NAME + " (" + IntentStore.COLUMN_NAME_ID
			+ " TEXT PRIMARY KEY," + IntentStore.COLUMN_NAME_INTENT + " TEXT"
			+ ")";

	private static final String SQL_CREATE_AREALIST = "CREATE TABLE "
			+ SelectedArea.TABLE_NAME + "(" + SelectedArea.COL_ID + " TEXT,"
			+ SelectedArea.COL_AREA + " TEXT)";
	
	private static final String SQL_CREATE_TASK = "CREATE TABLE "
			+ TaskStore.TABLE_NAME + "(" + TaskStore.COL_STUDENT_ID + " TEXT,"
			+ TaskStore.COL_TASK_ID + " TEXT," + "" + TaskStore.COL_TASK_NAME
			+ " TEXT, " + TaskStore.COL_AREA_NAME + "," + TaskStore.COL_AREA_ID
			+ " TEXT," + TaskStore.COL_SOLUTION + " TEXT)";

	public static final String SQL_CREATE_STRATEGY = "CREATE TABLE "
			+ StrategyStore.TABLE_NAME + "(" + StrategyStore.COL_STUDENT_ID
			+ " TEXT," + StrategyStore.COL_STRATEGY_ID + " TEXT,"
			+ StrategyStore.COL_STRATEGY_TEXT + " TEXT,"
			+ StrategyStore.COL_TASKID + " TEXT)";

	public static final String SQL_CREATE_AREA = "CREATE TABLE "
			+ AreaStore.TABLE_NAME + "(" + AreaStore.COL_AREA_ID + " TEXT,"
			+ AreaStore.COL_STUDENT_ID + " TEXT," + AreaStore.COL_AREA_NAME
			+ " TEXT)";

	private static final String SQL_CREATE_ATTABLE = "CREATE TABLE "
			+ ATStore.TABLE_NAME + "(" + ATStore.COL_STUDENT_ID + " TEXT,"
			+ ATStore.COL_AREA_ID + " TEXT,"
			+ ATStore.COL_TASK_ID + " TEXT,"
			+ ATStore.COL_AT_ID + " TEXT," 
			+ ATStore.COL_ATNAME + " TEXT,"
			+ ATStore.COL_PARTICIPANTS + " TEXT,"
			+ ATStore.COL_1stTrialDate + " TEXT,"
			+ ATStore.COL_1stTrialWorking + " TEXT,"
			+ ATStore.COL_AREA_NAME + " TEXT,"
			+ ATStore.COL_TASK_NAME + " TEXT,"
			+ ATStore.COL_SOLUTION + " TEXT"
			+ ")";

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ FeedEntry.STUDENT;

	private static final String SQL_DELETE_ENTRIES_INTENT = "DROP TABLE IF EXISTS "
			+ IntentStore.TABLE_NAME;

	private static final String SQL_DELETE_ID = "DROP TABLE IF EXISTS CURRENT_DATA";

	private static final String SQL_DELETE_AREALIST = "DROP TABLE IF EXISTS "
			+ SelectedArea.TABLE_NAME;

	private static final String SQL_DELETE_TASK = "DROP TABLE IF EXISTS "
			+ TaskStore.TABLE_NAME;

	private static final String SQL_DELETE_STRATEGY = "DROP TABLE IF EXISTS "
			+ StrategyStore.TABLE_NAME;

	private static final String SQL_DELETE_AREA = "DROP TABLE IF EXISTS "
			+ AreaStore.TABLE_NAME;
	private static final String SQL_DELETE_AT = "DROP TABLE IF EXISTS "
			+ ATStore.TABLE_NAME;

	public SQLiteDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
		db.execSQL(SQL_CREATE_INTENT_ENTRIES);
		db.execSQL(SQL_CREATE_CURRENT_ID);
		db.execSQL(SQL_CREATE_AREALIST);
		db.execSQL(SQL_CREATE_AREA);
		db.execSQL(SQL_CREATE_TASK);
		db.execSQL(SQL_CREATE_STRATEGY);
		db.execSQL(SQL_CREATE_ATTABLE);
		
	}

	/**
	 * Run this when database version is upgraded (Discard all previous data)
	 * */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy
		// is
		// to simply to discard the data and start over
		db.execSQL(SQL_DELETE_ENTRIES);
		db.execSQL(SQL_DELETE_ENTRIES_INTENT);
		db.execSQL(SQL_DELETE_ID);
		db.execSQL(SQL_DELETE_AREALIST);
		db.execSQL(SQL_DELETE_TASK);
		db.execSQL(SQL_DELETE_AREA);
		db.execSQL(SQL_DELETE_STRATEGY);
		db.execSQL(SQL_DELETE_AT);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

}