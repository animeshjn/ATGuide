package edu.gmu.ttaconline.atcguide;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;

import edu.gmu.ttaconline.atcguide.FeedReaderContract.FeedEntry;
import edu.gmu.ttaconline.atcguide.FeedReaderContract.IntentStore;
import edu.gmu.ttaconline.atcguide.FeedReaderContract.SelectedArea;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Java bean to persist data to the SQLite DB. Storing details of current intent
 * and retrieving all previous values utilizing the helper and contract classes
 * This class is the part of the ATGUIDE application built for KIHD, CEHD, GMU.
 * Spring 2017
 * 
 * @author Animesh Jain
 * */
public class PersistenceBean {

	// Static Methods

	/**
	 * Method to input data from the first form of the screen in creating new
	 * student record.
	 * */
	public static Intent persistInputFormData(ViewGroup v, Context context) {
		// Requires: view group containing the required fields and current
		// application context
		// Modifies: SQL Database, Intent
		// Effect: stores the details in this context and view group in the
		// intent and the Database

		boolean result = false;
		Log.d("ATGUIDE", "Data Persistence Started");
		String studentid = ((EditText) (v.findViewById(R.id.studentid)))
				.getText().toString();
		String studentgrade = ((Spinner) (v.findViewById(R.id.studentgrade)))
				.getSelectedItem().toString();
		String studentschool = ((EditText) (v.findViewById(R.id.studentschool)))
				.getText().toString();
		String studentparticipant = ((EditText) (v
				.findViewById(R.id.participants))).getText().toString();
		DatePicker datePicker = (DatePicker) v.findViewById(R.id.date);
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth() + 1;
		int year = datePicker.getYear();
		System.out.println(" " + " " + studentid + "  " + studentgrade
				+ studentschool + studentparticipant + day + month + year);
		// SQLiteDatabase
		// db=context.openOrCreateDatabase("student",context.MODE_PRIVATE,null);

		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
		@SuppressWarnings("deprecation")
		java.sql.Date date = new java.sql.Date(year, month, day);

		// Gets the data repository in write mode
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		mDbHelper.onUpgrade(db, 2, 3);
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.STUDENT_ID, studentid);
		values.put(FeedEntry.STUDENT_GRADE, studentgrade);
		values.put(FeedEntry.STUDENT_SCHOOL, studentschool);
		values.put(FeedEntry.STUDENT_PARTICIPANTS, studentparticipant);
		values.put(FeedEntry.STUDENT_DATE, date.toString());

		// values.put(FeedEntry.STUDENT_IEPGOALS, studentgrade);

		// Insert the new row, returning the primary key value of the new row
		try {
			db.insert(FeedEntry.STUDENT, null, values);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			Log.e("ATGUIDE", "Exception: " + e.getMessage());
		} finally {
			db.close();
		}

		Log.d("ATGUIDE", "Data Persistence ended with : " + result);
		Intent intent = new Intent();
		intent.putExtra("studentid", studentid);
		intent.putExtra("studentgrade", studentgrade);
		intent.putExtra("studentparticipant", studentparticipant);
		intent.putExtra("studentschool", studentschool);
		intent.putExtra("date", date.toString());

		return intent;
	}

	/**
	 * Method to persist data from IEP Goals Activity
	 * */
	public static void persistIEGoals(String studentid, String iepGoal,
			Context context) {
		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
		ContentValues values = new ContentValues();
		int rows = 0;
		values.put(FeedEntry.STUDENT_IEPGOAL, iepGoal);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		try {
			rows = db.update(FeedEntry.STUDENT, values, ""
					+ FeedEntry.STUDENT_ID + "='" + studentid + "'", null);

		} catch (SQLException e) {
			Log.e("ATGUIDE", "SQLException: " + e.getMessage());
		} catch (Exception e) {
			Log.e("ATGUIDE", "Other Exception: " + e.getMessage());
		}
		db.close();
		if (rows > 0) {
			Toast.makeText(context, "" + rows + " record(s) affected",
					Toast.LENGTH_SHORT).show();
		}
		Log.d("ATGUIDE", "IEP Goals persisted, number of row(s): " + rows);
	}

	/**
	 * Method to persist data from instructional areas activity
	 * */
	public static void persistInstructionalAreas(String studentId,
			ArrayList<String> selectedInstructionalAreas, Context context)
	{
		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		for (String area : selectedInstructionalAreas) {
			ContentValues values = new ContentValues();
			values.put(SelectedArea.COL_ID, studentId);
			values.put(SelectedArea.COL_AREA, area);
			db.insert(SelectedArea.TABLE_NAME, null, values);
		}
		db.close();
	}

	/**
	 * Method to persist current intent data
	 * */
	public static boolean persistIntent(String studentId, Intent intent,
			Context context) {

		boolean result = true;
		try {
			String intentDescription = null;
			FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			intentDescription = intent.toUri(0);
			ContentValues values = new ContentValues();
			values.put(IntentStore.COLUMN_NAME_ID, studentId);
			values.put(IntentStore.COLUMN_NAME_INTENT, intentDescription);
			long rows = 0;
			rows = db.insertWithOnConflict(IntentStore.TABLE_NAME, null,
					values, SQLiteDatabase.CONFLICT_REPLACE);
			db.close();
			if (rows > 0) {
				Toast.makeText(context, "" + rows + " inserted",
						Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			Log.e("ATGUIDE", "Error in persisting intent " + e.getStackTrace());
			result = false;
		}

		return result;
	}

	/**
	 * Method to retrieve persisted area list for this student
	 * */
	public static ArrayList<CharSequence> getPersistedAreaList(
			String studentid, Context context) {
		ArrayList<CharSequence> persistedAreas = new ArrayList<CharSequence>();
		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor cursor;
		try {
			Log.d("ATGUIDE", "Retrieving cursor from DB");

			cursor = db.query(SelectedArea.TABLE_NAME,
					new String[] { SelectedArea.COL_AREA }, SelectedArea.COL_ID
							+ " = " + "'" + studentid + "'", null, null, null,
					null);
			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					persistedAreas.add(cursor.getString(0));
					cursor.moveToNext();
				}

			}

		} catch (Exception e) {
			Log.e("ATUGUIDE",
					"Error in retrieving persisted area list " + e.getMessage());
			return null;
		}
		return persistedAreas;
	}

	/**
	 * Method to retrieve persisted intent from the database
	 * */
	public static Intent getExistingIntent(String studentId, Context context) {
		Intent requiredIntent = null;
		Log.e("ATGUIDE", "Searching for persisted intent with student id :"
				+ studentId);
		int records = 0;
		Log.d("ATGUIDE", "before Db Helper");

		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
		Log.d("ATGUIDE", "before getting database");
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		// ContentValues values = new ContentValues();
		String intentDescription = null;
		Cursor cursor;

		try {
			Log.d("ATGUIDE", "Trying to get cursor");
			cursor = db.query(IntentStore.TABLE_NAME,
					new String[] { IntentStore.COLUMN_NAME_INTENT },
					IntentStore.COLUMN_NAME_ID + " = " + "'" + studentId + "'",
					null, null, null, null);

		} catch (Exception e) {
			Log.e("ATUGUIDE", "Error GETTING CURSOR " + e.getMessage());
			return null;
		}
		if (cursor == null) {
			Log.e("ATGUIDE", "Cursor is null ");
		}
		int i = 0;
		Log.d("ATGUIDE", "before cursor while ");
		cursor.moveToFirst();

		Log.d("ATGUIDE", "While started: " + (i++));
		intentDescription = cursor.getString(0);
		Log.d("ATGUIDE", "INTENT DESCRIPTION " + intentDescription);
		records++;
		try {
			requiredIntent = Intent.parseUri(intentDescription, 0);
		} catch (URISyntaxException e) {
			Log.e("ATGUIDE", "Error parsing intent: " + e.getStackTrace());
		}

		Toast.makeText(context, "Retrieved: " + records + " records",
				Toast.LENGTH_SHORT).show();
		if (requiredIntent == null) {
			Log.e("ATGUIDE", "Required intent is null ");
		}

		return requiredIntent;
	}

	/**
	 * Method to check if given student id is already present in the database
	 * */
	public static boolean isExistingRecord(String studentId, Context context) {
		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(
				context.getApplicationContext());
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor cursor = db.query(IntentStore.TABLE_NAME,
				new String[] { IntentStore.COLUMN_NAME_INTENT },
				IntentStore.COLUMN_NAME_ID + " like " + "'" + studentId + "'",
				null, null, null, null);
		if (cursor.getCount() > 0)
			return true;
		else
			return false;
	}

	/**
	 * Persists current student id
	 * */
	public static void persistCurrentId(String studentId, Context context) {

		try{
		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(SelectedArea.COL_ID, studentId);
			db.insert("CURRENT_DATA", null, values);
		db.close();
		}
		catch(Exception e){
			Log.e("ATGUIDE", "Exception while persisting current student id");
		}
	}

	/**
	 * Retrieves current running student id from the database
	 * */
	public static String getCurrentId(Context context){
		//Requires: current application context
		//Effects: Returns the current running id of this student
		
		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
		String currentId=null;
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor cursor;
		try {
			Log.d("ATGUIDE", "Trying to get current id cursor");
			cursor = db.rawQuery("select * from CURRENT_DATA", null);
			cursor.moveToFirst();
			currentId=cursor.getString(0);
	}catch(Exception e ){
		Log.e("ATGUIDE", "Error retrieving the current id");
	}
		return currentId;
	}
	
	public static void persistTaskObject(Task task,Context context){
		int taskid= task.taskid;
		String name= task.getTaskname();
		String areaName=task.getAreaname();
		Set<String> StrategyKeys=task.strategies.keySet();
		//studentid, taskid
		try{
			FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
			//	values.put(SelectedArea.COL_ID, studentId);
				db.insert("CURRENT_DATA", null, values);
				db.close();
			}
			catch(Exception e){
				Log.e("ATGUIDE", "Exception while persisting current student id");
			}
		
		
	}
}
