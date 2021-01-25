package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.sql.*;
import java.util.Date;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        //int inHour = ticket.getInTime().getHours();
        //int outHour = ticket.getOutTime().getHours();
        Date inHour = ticket.getInTime();
        Date outHour = ticket.getOutTime();
        float duration = (float)(outHour.getTime() - inHour.getTime())/(1000*60*60);
        //TODO: Some tests are failing here. Need to check if this logic is correct
        //Date duration = outHour - inHour;

        System.out.println("************");
        System.out.println("inHour"+inHour);
        System.out.println("outHour"+outHour);
        System.out.println(duration);
        System.out.println("ticket reg number: "+ticket.getVehicleRegNumber());
        System.out.println("initial price: "+duration * Fare.CAR_RATE_PER_HOUR );
        System.out.println("************");

        boolean client = isClient(ticket.getVehicleRegNumber());
        System.out.println(client);

        if(duration<0.5){
            ticket.setPrice(Fare.RATE_BELOW_HALF_AN_HOUR);
        }
        else{
            double reduction;

            if(client)
                reduction = 0.95;
            else
                reduction = 1;

            switch (ticket.getParkingSpot().getParkingType()){

                case CAR: {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * reduction);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * reduction);
                    break;
                }
                default: throw new IllegalArgumentException("Unknown Parking Type");
            }
        }

    }
    public boolean isClient(String vehicleReg){
        DataBaseConfig dataBaseConfig = new DataBaseConfig();
        Connection con = null;
        int result = 0;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.COUNT_REG_VEHICLE);
            ps.setString(1,vehicleReg);
            ResultSet rs = ps.executeQuery();
            rs.next();
            result = rs.getInt("total");

        }catch (Exception e){
            System.err.println("Got an exception! "+e);
        }

        if(result>1){
            return true;
        }
        else{
            return false;
        }

    }


}