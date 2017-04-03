package edu.gmu.ttaconline.atcguide;

import android.util.Log;

/**
 * Logger class for this application, for debugging purposes
 * @author Animesh Jain
 *
 */
public class ATGuideLogger {
	
	public static final String LOGTAG="ATGUIDE";
	/**
	 * Logs the given area object
	 * @param area to be logged 
	 */
	public static void LogIt(Area area){
		Log.d(LOGTAG,""+area.getAreaName());
		Log.d(LOGTAG,""+area.getParentId());
		Log.d(LOGTAG,"log area complete");
	}
}
