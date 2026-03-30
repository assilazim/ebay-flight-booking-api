package com.assil.flightbooking.model;

public class Flight {
    private String flightNumber;
    private int capacity;
    private int bookedSeats;

    public Flight() {
    }

    public Flight(String flightNumber, int capacity, int bookedSeats) {
        this.flightNumber = flightNumber;
        this.capacity = capacity;
        this.bookedSeats = bookedSeats;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(int bookedSeats) {
        this.bookedSeats = bookedSeats;
    }
}