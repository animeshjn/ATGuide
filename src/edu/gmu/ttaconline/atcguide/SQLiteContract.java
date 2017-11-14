package edu.gmu.ttaconline.atcguide;



public final class SQLiteContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SQLiteContract() {}
    public static int id=1000;
    
    /* Inner class that defines the table contents */
    public static class FeedEntry  {
        public static final String STUDENT = "student";
        public static final String STUDENT_ID = "studentid";
        public static final String STUDENT_GRADE = "grade";
        public static final String STUDENT_SCHOOL = "school";
        public static final String STUDENT_PARTICIPANTS = "participants";
        public static final String STUDENT_DATE="date";
        public static final String STUDENT_IEPGOAL="iepgoal";
        
        
    }
    public static class InstructionalAreas  {
        public static final String TABLE_NAME = "instructionalareas";
        public static final String STUDENT = "";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
    public static class IntentStore{
    	public static final String TABLE_NAME= "intentstore";
    	public static final String COLUMN_NAME_ID= "studentid";
    	public static final String COLUMN_NAME_INTENT= "intentstring";
    	
    }
    public static class SelectedArea{
    	public static final String TABLE_NAME= "SELECTED_AREA";
    	public static final String COL_ID= "studentid";
    	public static final String COL_AREA= "area";
    }
    
    public static class TaskStore{
    	public static final String TABLE_NAME= "TasksAndStrategy";
    	public static final String COL_STUDENT_ID= "studentid";
    	public static final String COL_AREA_ID="areaid";
    	public static final String COL_TASK_ID= "taskid";
    	public static final String COL_AREA_NAME= "areaname";
    	public static final String COL_TASK_NAME="taskname";
    	public static final String COL_SOLUTION="solution";
    }
    public static class ATStore{
    	public static final String TABLE_NAME= "ATS";
    	public static final String COL_STUDENT_ID= "studentid";
    	public static final String COL_AREA_ID="areaid";
    	public static final String COL_TASK_ID= "taskid";
    	public static final String COL_AT_ID= "atid";
    	public static final String COL_ATNAME= "atname";
    	public static final String COL_PARTICIPANTS= "participants";
    	public static final String COL_1stTrialDate= "firsttrialdate";
    	public static final String COL_1stTrialWorking= "firsttrialworking";
    	public static final String COL_AREA_NAME= "areaname";
    	public static final String COL_TASK_NAME="taskname";
    	public static final String COL_SOLUTION="solution";
    	public static final String COL_TRIAL1_COMPLETION="trial1_completion_date";
    	public static final String COL_TRIAL1_Action="trial1_action";
    	public static final String COL_TRIAL1_Persons="trial1_action_person";


    }
    public static class StrategyStore{
    	
    	public static final String TABLE_NAME="strategymapping";
    	public static final String COL_STRATEGY_ID="strategyid";
    	public static final String COL_STRATEGY_TEXT="strategytext";
    	public static final String COL_TASKID="taskid";
    	public static final String COL_STUDENT_ID="studentid";
    
    }
   public static class AreaStore{
	   
	   public static final String TABLE_NAME="areastore";
	   public static final String COL_STUDENT_ID="studentid";
	   public static final String COL_AREA_ID="areaid";
	   public static final String COL_AREA_NAME="areaname";
   
   }
   
   
}