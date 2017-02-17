package edu.gmu.ttaconline.atcguide;

import java.util.ArrayList;



public class CheckBoxBean {
	static int checkId=1000;
	//Map<Boolean,Integer>= new HashMap<Boolean,Integer>; 
	static ArrayList<Integer> checked= new ArrayList<Integer>();
	static ArrayList<Integer> unChecked= new ArrayList<Integer>();
	public CheckBoxBean(){super();}
	
	public CheckBoxBean(int startId){
		super();
		checkId=startId;
		
	}
	
	public int getNextCheckID()
	{
		return ++checkId;
	}
	
	public int getCurrentCheckId(){
		return checkId;
	}
	
	public void addChecked(int checkboxId)
	{
		checked.add(checkboxId);
	}
	public void removeChecked(int checkboxId)
	{
		checked.remove(checkboxId);
	}
	public ArrayList<Integer> getAllCheckedIds(){
		return checked;
	}
	
}
