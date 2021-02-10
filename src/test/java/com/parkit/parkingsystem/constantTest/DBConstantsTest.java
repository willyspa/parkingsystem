package com.parkit.parkingsystem.constantTest;

public class DBConstantsTest {
    public static final String COUNT_DB = "SELECT COUNT(*) as totalRecords FROM test.ticket";
    public static final String AVAILABILITY_IS_UPDATED = "SELECT AVAILABLE as isFree FROM test.parking WHERE PARKING_NUMBER = 1;";
}
