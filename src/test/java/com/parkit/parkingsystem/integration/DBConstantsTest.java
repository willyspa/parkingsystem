package com.parkit.parkingsystem.integration;

public class DBConstantsTest {
    public static final String COUNT_REG_VEHICLE_TEST = "SELECT count(VEHICLE_REG_NUMBER) AS total FROM prod.ticket WHERE VEHICLE_REG_NUMBER = ?";
}
