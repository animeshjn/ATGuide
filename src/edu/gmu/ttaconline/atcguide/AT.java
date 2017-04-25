package edu.gmu.ttaconline.atcguide;

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
	public String participants = "";
	public String firstTrialDate = "";
	public boolean solutionWorking = false;
	
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

}
