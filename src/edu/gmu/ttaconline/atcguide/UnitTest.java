package edu.gmu.ttaconline.atcguide;



public class UnitTest {
	public String trimATName(String atName){
		if(atName.contains("(e.g.")||atName.contains("( e.g.")){
		atName.replaceFirst("(e.g.*)", "");
		}
		return atName;
	}
	
}
