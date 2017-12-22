package edu.gmu.ttaconline.atcguide;

import android.util.Log;

/**
 * Bean to store Assistive Technology
 * 
 * @author Animesh Jain
 * 
 */
public class AT {

	public String instructionalArea = "";
	public int id;
	public String ATName = "";
	public String task = "";
	public String participants = ""; //part1
	public String firstTrialDate = "";//part1
	
	
	public boolean firstWorking = false;//part2
	/*For part two*/
	public boolean solutionWorking = true;//part3
	
	
	public String trial1CompletionDate = ""; //part2
	public String trial1Action="";//part2
	public String trial1Persons = "";//part2
	/*For part four (Trial2)*/
	/*TODO: Add Trial2 fields*/
	public String secondTrialDate = "";//part3
	public String trial2Participants = "";
	
	public boolean trial2solutionWorking = true; //part4
	public String trial2CompletionDate = "";//part4
	public String trial2Action="";//part4
	public String trial2Persons = "";//part4
	/**
	 * @return instructionalArea
	 */
	public String getInstructionalArea() {
		return instructionalArea;
	}

	/**
	 * @param instructionalArea
	 */
	public void setInstructionalArea(String instructionalArea) {
		this.instructionalArea = instructionalArea;
	}

	/**
	 * @return
	 */
	public String getATName() {
		return ATName;
	}

	/**
	 * @param aTName
	 */
	public void setATName(String aTName) {
		ATName = aTName;
	}

	/**
	 * @return
	 */
	public String getParticipants() {
		return participants;
	}

	/**
	 * @param participants
	 */
	public void setParticipants(String participants) {
		this.participants = participants;
	}

	/**
	 * @return
	 */
	public String getFirstTrialDate() {
		return firstTrialDate;
	}

	/**
	 * @param firstTrialDate
	 */
	public void setFirstTrialDate(String firstTrialDate) {
		this.firstTrialDate = firstTrialDate;
	}

	/**
	 * @return
	 */
	public boolean isSolutionWorking() {
		return solutionWorking;
	}

	/**
	 * @param solutionWorking
	 */
	public void setSolutionWorking(boolean solutionWorking) {
		this.solutionWorking = solutionWorking;
	}

	public void setSecondTrialDate(String string) {
		// TODO Auto-generated method stub
		this.secondTrialDate=string;
	}
	public void log(){
		Log.d("ATGUIDE","Logging AT \n"
				+ "AT NAME "+ATName+"\n"
				+ "First Trial Date"+firstTrialDate+"\n"
				+ "Participants "+participants+"\n ..."
				
				
				);
		
	}

}
