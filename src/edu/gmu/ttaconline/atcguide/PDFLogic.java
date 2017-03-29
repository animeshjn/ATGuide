package edu.gmu.ttaconline.atcguide;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

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

public class PDFLogic {
	
	static ArrayList<Paragraph> plist = new ArrayList<Paragraph>();
	static PdfPTable table = new PdfPTable(1);
	static Font headfont;
	static ArrayList<PdfPCell> celllist = new ArrayList<PdfPCell>();
	static String studentid;
	static Context context;
	static Document document;
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
public static void generatePDF(Context context){
	PDFLogic.context=context;
	try {
		// HAVE A PRINT WRITER HANDY
		 PdfWriter writer = null;
		// SETUP PATH
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/VA_AT_Consideration_Guide";
		// TAKEUP DIRECTORY
		File dir = new File(path);
		// MAKE DIRECTORY NATIVE COMMAND
		if (!dir.exists())
			dir.mkdirs();
		File f = new File(dir, "VA_AT_Consideration_Guide.pdf");
		f.delete();
		// THE ACTUAL STRATEGIES PDF FILE
		File file = new File(dir, "VA_AT_Consideration_Guide.pdf");
		//boolean result = java.io.file.Files.deleteIfExists(file.getPath());
		//Rectangle pageSize=new Rectangle(PageSize.A4.getWidth(), PageSize.A4.getHeight()-80);
        PdfReader reader = new PdfReader(context.getAssets().open("VAATGUIDE_FORM.pdf"));
        PdfStamper stamp1 = new PdfStamper(reader, new FileOutputStream(path+"/FILLED_FORM.pdf"));
        AcroFields form1 = stamp1.getAcroFields();
        form1.setField("student", "Bruno Lowagie");
		stamp1.setFormFlattening(true);
		stamp1.close();
		reader.close();
		
		
//		File pdfFile = new File(Environment.getExternalStorageDirectory()
//				.getAbsolutePath() + "/VA_AT_Consideration_Guide/FILLED_FORM.pdf");
//		Uri readpath = Uri.fromFile(pdfFile);
//		Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//		pdfIntent.setDataAndType(readpath, "application/pdf");
//		pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		context.startActivity(pdfIntent);
		
			
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
}
