package edu.gmu.ttaconline.atcguide;

public class StudentBean {
	/*Overview: Non static bean to store a student record*/
	//Instances
	String studentid;
	String grade;
	String participants;
	String school;
	String date;
	
	//Methods
	public String getStudentid() {
		//Effects: returns student id
		
		return studentid;
	}
	public void setStudentid(String studentid) {
		//Modifies: this
		//Effects: set student id
		this.studentid = studentid;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getParticipants() {
		return participants;
	}
	public void setParticipants(String participants) {
		this.participants = participants;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	} 
	
	
	
	
}
