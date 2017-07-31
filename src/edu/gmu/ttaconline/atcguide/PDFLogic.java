package edu.gmu.ttaconline.atcguide;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * Provides key functionalities for fetching data and output it to user in the
 * PDF format.
 * 
 * @author Animesh Jain
 */
public class PDFLogic extends Service {

	/** Font */
	static Font headfont;

	/** StudentId */
	static String studentid;

	/** IEPGoal */
	static String iepgoal = "";

	/** IEPReading */
	static String iepreading = "";

	/** IEPTest */
	static String ieptest = "";

	/** Participants */
	static String participants = "";

	/** IEPAlt */
	static String iepalt = "";

	/** Context */
	static Context context;

	/** School */
	static String school = "";

	/** Grade */
	static String grade = "";

	/** Date */
	static String date = "";

	/** areaList */
	static ArrayList<CharSequence> areaList = null;

	static ArrayList<Area> trial1List = null;

	/**
	 * Binds the intent
	 */
	private final IBinder mBinder = new MyBinder();

	/**
	 * LOG_TAG
	 */
	public static final String LOG_TAG = "ATGUIDE";

	/**
	 * activity
	 */
	public static Activity activity;
	public static boolean trial1=false;

	/**
	 * init fonts
	 */
	public void initFonts() {
		Font f = new Font(FontFamily.HELVETICA, 14, Font.NORMAL, BaseColor.RED);
		headfont = f;
	}

	/**
	 * Generates PDF document from given context.
	 * 
	 * @param activity
	 *            - current intent stack.
	 */
	public static void generatePDF() {
		PDFLogic.context = activity.getApplicationContext();
		try {
			setData();
			/** Setup Path */
			Log.d(LOG_TAG, "Data set");

			/** Setup Destination */
			String dest = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + File.separator + "ATGUIDE_RESULTS";

			Log.d(LOG_TAG, "creating dest");

			File filepath = new File(dest);

			Log.d(LOG_TAG, "creating dest directory");

			if (!filepath.exists())
				filepath.mkdirs();
			Log.d(LOG_TAG, "creating filled form");
			File file = new File(filepath + File.separator
					+ "ATFilled_Form.pdf");
			file.getParentFile().mkdirs();
			file.delete();
			file.createNewFile();
			file.deleteOnExit();
			Log.d(LOG_TAG, "before reader");
			PdfReader reader = new PdfReader(context.getAssets().open(
					"VAATGUIDE_FORM2.pdf"));
			PdfStamper stamp1 = new PdfStamper(reader, new FileOutputStream(
					file));

			AcroFields form1 = stamp1.getAcroFields();
			// SET FIELDS HERE
			form1.setField("student", studentid);
			form1.setField("grade",
					activity.getIntent().getStringExtra("studentgrade"));
			form1.setField("date", activity.getIntent().getStringExtra("date"));
			form1.setField("school", school);
			form1.setField("participants1", participants);
			// Area task combination: O(n^2)
			final ArrayList<Area> persistedAreaList = PersistenceBean
					.getPersistedAreaObjects(studentid, context);
			int i = 1;

			for (Area area : persistedAreaList) {
				// Complexity O(n) {n Area + constant tasks (Max=7)* constant
				// strategy (Max=6)}
				final ArrayList<Task> persistedTasks = PersistenceBean
						.getPersistedTasks(area, studentid, context);
				for (Task task : persistedTasks) {
					form1.setField("task" + i,
							task.getAreaname() + ": " + task.getTaskname());
					Log.d(LOG_TAG,
							"" + task.getAreaname() + ": " + task.getTaskname());
					String strategy = "";
					for (String strategyKey : task.strategies.keySet()) {

						strategy += "" + task.strategies.get(strategyKey) + ";";
					}
					form1.setField("strategy" + i, strategy);
					i++;
				}
			}
			for (CharSequence area : areaList) {

				String areaname = area.toString().toLowerCase();
				form1.setField(areaname, "On");
				if (areaname.contains("study"))
					form1.setField("study", "On");

				else if (areaname.contains("oral"))
					form1.setField("oral", "On");

				else if (areaname.contains("activities"))
					form1.setField("activities", "On");

				else if (areaname.contains("recreation"))
					form1.setField("recreation", "On");

				else if (areaname.contains("Positioning"))
					form1.setField("positioning", "On");
				else if (areaname.contains("computer"))
					form1.setField("computer", "On");

				else if (areaname.toLowerCase().contains("environment"))
					form1.setField("environment", "On");
				else {
					form1.setField("other", "On");
					form1.setField("otherareaname", areaname);
				}
			}
			if (iepgoal.toLowerCase().contains("yes"))
				form1.setField("idyes", "On");
			else if (iepgoal.toLowerCase().contains("no"))
				form1.setField("idno", "On");

			if (iepreading.toLowerCase().contains("yes"))
				form1.setField("iepreadingyes", "On");
			else if (iepreading.toLowerCase().contains("no"))
				form1.setField("iepreadingno", "On");
			if (iepalt.toLowerCase().contains("yes"))
				form1.setField("iepaltyes", "On");
			else if (iepalt.toLowerCase().contains("no"))
				form1.setField("iepaltno", "On");
			if (trial1) {
				form1.setField("iep0no", "On");
				int c = 1;
				if(trial1List!=null)
					Log.d(LOG_TAG, "Trail1 no null in gen");
				for (Area ar : trial1List) {
					for (Task task : ar.tasks){
						if(ar.tasks!=null)
							{Log.d(LOG_TAG, "tasks not null"+c);}
						for (AT ats : task.ats) {
							if(task.ats!=null)
							{Log.d(LOG_TAG, "tasks not null"+c);}
							Log.d(LOG_TAG, "ATS:"+c);
							String fieldName = ats.instructionalArea+": "+""+ats.ATName;
							form1.setField("trial1at"+c, fieldName);
							form1.setField("trial1person" + c, ats.participants);
							form1.setField("trial1date" + c, ats.firstTrialDate);
							c++;
						}
				}
					}
			} else
				form1.setField("iep0complete", "On");
			stamp1.setFormFlattening(true);
			stamp1.close();
			reader.close();
			File pdfFile = new File(dest + File.separator + "ATFilled_Form.pdf");
			Uri readpath = Uri.fromFile(pdfFile);
			pdfFile.deleteOnExit();
			Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
			pdfIntent.setDataAndType(readpath, "application/pdf");
			pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			pdfIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			activity.startActivity(pdfIntent);
			// open the form from assets
			// get stamper for a new pdf
			// get acrofields from the stamper
			// fill out all the fields
			// compare sizes , set font sizes
			// isFit("given data", "some field")
			// fill out the data
			// flatten the form

		} catch (Exception e) {
			Log.e("ATGUIDE", "Error in PDF LOGIC :" + e);

		}

	}

	/**
	 * Set the data from persisted intent to this
	 */
	public static void setData() {
		studentid = PersistenceBean.getCurrentId(context);
		Intent intent = PersistenceBean.getExistingIntent(studentid, context);
		grade = intent.getStringExtra("studentgrade");
		school = intent.getStringExtra("studentschool");
		participants = intent.getStringExtra("studentparticipant");
		date = intent.getStringExtra("date");
		iepgoal = intent.getStringExtra("iepgoal");
		iepreading = intent.getStringExtra("iepreading");
		iepalt = intent.getStringExtra("iepalt");
		areaList = PersistenceBean.getPersistedAreaList(studentid, context);
		trial1 = intent.getBooleanExtra("trial1", false);
		if (trial1)
			trial1List = PersistenceBean.getPersistedAreaObjects("trial1"
					+ studentid, context);
			if(!(trial1List==null)){
				Toast.makeText(context, "TRIAL!NOT NULL", Toast.LENGTH_SHORT).show();
			}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		try{PDFLogic.generatePDF();}
		catch(Exception e){
			Log.e(LOG_TAG, ""+e);
			
		}
		return Service.START_NOT_STICKY;
	}

	/**
	 * @author ajain13
	 * 
	 */
	public class MyBinder extends Binder {
		PDFLogic getService() {
			return PDFLogic.this;
		}
	}
}
