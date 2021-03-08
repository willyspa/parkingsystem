package com.parkit.parkingsystem.model;

import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getInTime() {
        return inTime = inTime == null ? null : (Date) inTime.clone();
    }
    // deep copy of outTime to avoid any accidental access to the value - for example the object used to set the time can be modified unintentionally (reference issues)
    public void setInTime(Date inTime) {
        this.inTime = inTime == null ? null : (Date) inTime.clone();
    }

    public Date getOutTime() {
        return outTime = outTime == null ? null : (Date) outTime.clone();
    }
    // deep copy of outTime to avoid any accidental access to the value - for example the object used to set the time can be modified unintentionally (reference issues)
    public void setOutTime(Date outTime) {
        this.outTime = outTime == null ? null : (Date) outTime.clone();
    }

}
