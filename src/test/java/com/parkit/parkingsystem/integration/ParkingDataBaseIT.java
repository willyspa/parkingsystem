package com.parkit.parkingsystem.integration;


import com.parkit.parkingsystem.constantTest.DBConstantsTest;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static String vehicleRegNumber = "ABCDEF";

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {

        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
        dataBasePrepareService.clearDataBaseEntries();

    }

    @AfterAll
    private static void tearDown(){
   //     dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testParkingACar(){

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        Connection con = null;
        int numberOfTicketRecords = 0;
        boolean parkingSlotIsAvailable = true;
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 60 * 60 * 1000) );

        try {
            con = dataBaseTestConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstantsTest.UPDATE_IN_TIME);

            ps.setTimestamp(1, new Timestamp(inTime.getTime()));
            ps.execute();

        }catch (Exception e){
            System.err.println("Got an exception! "+e);
        }

        /*Testing if the record is saved*/
        try {
            con = dataBaseTestConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstantsTest.COUNT_DB);
            ResultSet rs = ps.executeQuery();
            rs.next();
            numberOfTicketRecords = rs.getInt("totalRecords");

        }catch (Exception e){
            System.err.println("Got an exception! "+e);
        }
        /*Testing if the parking table is updated*/
        try {
            con = dataBaseTestConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstantsTest.AVAILABILITY_IS_UPDATED);
            ResultSet rs = ps.executeQuery();
            rs.next();
            parkingSlotIsAvailable = rs.getBoolean("isFree");

        }catch (Exception e){
            System.err.println("Got an exception! "+e);
        }
        assertEquals(1,numberOfTicketRecords);
        assertEquals(false,parkingSlotIsAvailable);

    }

    @Test
    public void testParkingLotExit(){
        testParkingACar();

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        Ticket ticketOut = ticketDAO.getTicket(vehicleRegNumber);

        assertEquals(Fare.CAR_RATE_PER_HOUR,round(ticketOut.getPrice()));
        assertTrue(new Date().getTime()-ticketOut.getOutTime().getTime()<1000);
    }

    public static double round(double value) {
        double scale = Math.pow(10,2);
        return Math.round(value * scale) / scale;
    }

}