package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

import java.sql.*;
import java.util.Date;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        Date inHour = ticket.getInTime();
        Date outHour = ticket.getOutTime();
        float duration = (float)(outHour.getTime() - inHour.getTime())/(1000*60*60);

        boolean client = TicketDAO.isClient(ticket.getVehicleRegNumber());
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



}