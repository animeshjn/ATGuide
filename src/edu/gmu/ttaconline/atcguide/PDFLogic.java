package edu.gmu.ttaconline.atcguide;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFLogic  {
	
	static ArrayList<Paragraph> plist = new ArrayList<Paragraph>();
	static PdfPTable table = new PdfPTable(1);
	static Font headfont;
	static ArrayList<PdfPCell> celllist = new ArrayList<PdfPCell>();
	static String studentid;
	static String iepgoal="";
	static String iepreading="";
	static String participants="";
	static String iepalt="";
	static Context context;
	static Document document;
	static ArrayList<CharSequence> areaList=null;
	public static final String LOG_TAG="ATGUIDE";
	Date date;
	String sdate;
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public void initFonts() {
		Font f = new Font(FontFamily.HELVETICA, 14, Font.NORMAL, BaseColor.RED);
		headfont = f;

	}
public static void generatePDF(Activity activity){
	PDFLogic.context=activity.getApplicationContext();
	try {setData();
		// HAVE A PRINT WRITER HANDY
		// SETUP PATH
		String dest=Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/ATGUIDE_RESULTS";
		// TAKEUP DIRECTORY
		
//		File f = new File(dir, "VA_AT_Consideration_Guide.pdf");
//		f.delete();
		// THE ACTUAL STRATEGIES PDF FILE
		File filepath = new File(dest);
		if (!filepath.exists())
			filepath.mkdirs();
		File file = new File(filepath,"ATFilled_Form.pdf");
        file.getParentFile().mkdirs();
        file.deleteOnExit();
        file.delete();
		file.createNewFile();
		//boolean result = java.io.file.Files.deleteIfExists(file.getPath());
		//Rectangle pageSize=new Rectangle(PageSize.A4.getWidth(), PageSize.A4.getHeight()-80);
		PdfReader reader = new PdfReader(context.getAssets().open("VAATGUIDE_FORM2.pdf"));
		PdfStamper stamp1 = new PdfStamper(reader, new FileOutputStream(file));
        AcroFields form1 = stamp1.getAcroFields();
        
        //SET FIELDS HERE
        form1.setField("student", activity.getIntent().getStringExtra("studentid"));
        form1.setField("grade", activity.getIntent().getStringExtra("studentgrade"));
        form1.setField("date", activity.getIntent().getStringExtra("date"));
        for (CharSequence area : areaList){
        	String areaname=area.toString().toLowerCase();
        	form1.setField(areaname, "On");
        	if(areaname.contains("study"))
        		form1.setField("study", "On");
        	else if(areaname.contains("oral"))
        		form1.setField("oral", "On");
        	else if(areaname.contains("activities"))
        		form1.setField("activities", "On");
        	else if(areaname.contains("recreation"))
        		form1.setField("recreation", "On");
        	else if(areaname.contains("positioning"))
        		form1.setField("positioning", "On");
        	else if(areaname.contains("computer"))
        		form1.setField("computer", "On");
        	else if(areaname.contains("environmental"))
        		form1.setField("environment", "On");
        	else if(areaname.contains("other"))
        		form1.setField("other", "On");
		}
        
        if(iepgoal.toLowerCase().contains("yes"))
        	form1.setField("idyes","On");
        else if(iepgoal.toLowerCase().contains("no"))
        	form1.setField("idno","On");
        
        if(iepreading.toLowerCase().contains("yes"))
        	form1.setField("readgroup","readyes");
        if(iepreading.toLowerCase().contains("no"))
        	form1.setField("readgroup","readno");
        
        //
		stamp1.setFormFlattening(true);
		stamp1.close();
		reader.close();
		
		File pdfFile = new File(dest+"/ATFilled_Form.pdf");
		Uri readpath = Uri.fromFile(pdfFile);
		Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
		pdfIntent.setDataAndType(readpath, "application/pdf");
		pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(pdfIntent);
		pdfFile.deleteOnExit();
		/* START CREATING FIRST ROW TO BE ADDED TO THE DOC */
		//open the form from assets
		// get stamper for a new pdf
		// get acrofields from the stamper
		// fill out all the fields 
		// compare sizes , set font sizes
		// isFit("given data", "some field")
		//
		// fill out the data 
		// flatten the form
		
		
		
		
}catch(Exception e){
	Log.e("ATGUIDE", "Error in PDF LOGIC :"+e);
	
}
	
}

public static void setData()
{
	String studentId=PersistenceBean.getCurrentId(context);
	Intent intent= PersistenceBean.getExistingIntent(studentId, context);
			intent.getStringExtra("studentgrade");
			intent.getStringExtra("studentschool");
	participants=intent.getStringExtra("studentparticipants");
			intent.getStringExtra("date");
	iepgoal=intent.getStringExtra("iepgoal");
	iepreading=intent.getStringExtra("iepreading");
	iepalt=intent.getStringExtra("iepalt");
	 areaList=PersistenceBean.getPersistedAreaList(studentId, context);
	
	}


}
