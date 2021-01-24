package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

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
        System.out.println("************");

        if(duration<0.5){
            ticket.setPrice(Fare.RATE_BELOW_HALF_AN_HOUR);
        }
        else{
            switch (ticket.getParkingSpot().getParkingType()){
                case CAR: {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default: throw new IllegalArgumentException("Unkown Parking Type");
            }
        }

    }
}