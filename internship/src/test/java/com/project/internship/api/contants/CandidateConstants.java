package com.project.internship.api.contants;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CandidateConstants {

    //CANDIDATE CONTROLLER TEST

    //Candidate
    public static final Integer CANDIDATE_ID = 1;
    public static final String NAME = "John Doe";
    public static final String CONTACT_NUMBER = "060000000";
    public static final Date DATE_OF_BIRTH = new GregorianCalendar(2000, Calendar.FEBRUARY, 25).getTime();
    public static final String EMAIL = "john.doe@email.com";

    //New andidate
    public static final Integer NEW_CANDIDATE_ID = 2;
    public static final String NEW_NAME = "Johnny Doe";
    public static final String NEW_CONTACT_NUMBER = "060000002";
    public static final Date NEW_DATE_OF_BIRTH = new GregorianCalendar(2002, Calendar.FEBRUARY, 25).getTime();
    public static final String NEW_EMAIL = "johnny.doe@email.com";

    //Update values
    public static final String UPDATE_NAME = "Jane Doe";
    public static final String UPDATE_CONTACT_NUMBER = "060000001";
    public static final Date UPDATE_DATE_OF_BIRTH = new GregorianCalendar(2001, Calendar.FEBRUARY, 25).getTime();
    public static final String UPDATE_EMAIL = "jane.doe@email.com";

    //False values
    public static final Integer FALSE_CANDIDATE_ID = -1;

    //Unique error values
    public static final String FALSE_UNIQUE_EMAIL = "johnny.doe@email.com";
    public static final String FALSE_UNIQUE_CONTACT_NUMBER = "060000002";

    //SKILL CONTROLLER TEST

    //Candidate1
    public static final Integer CANDIDATE1_ID = 1;
    public static final String NAME1 = "John Doe";
    public static final String CONTACT_NUMBER1 = "060000000";
    public static final Date DATE_OF_BIRTH1 = new GregorianCalendar(2000, Calendar.FEBRUARY, 25).getTime();
    public static final String EMAIL1 = "john.doe@email.com";

    //Candidate2
    public static final Integer CANDIDATE2_ID = 2;
    public static final String NAME2 = "Jane Doe";
    public static final String CONTACT_NUMBER2 = "060000001";
    public static final Date DATE_OF_BIRTH2 = new GregorianCalendar(2002, Calendar.FEBRUARY, 25).getTime();
    public static final String EMAIL2 = "jane.doe@email.com";

}
