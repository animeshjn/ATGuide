package edu.gmu.ttaconline.atcguide;



public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}
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
}