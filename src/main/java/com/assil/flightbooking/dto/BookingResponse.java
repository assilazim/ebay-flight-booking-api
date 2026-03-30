package com.assil.flightbooking.dto;

public class BookingResponse {
    private String bookingId;
    private String flightNumber;
    private String passengerName;
    private int seats;
    private String status;

    public BookingResponse() {
    }

    public BookingResponse(String bookingId, String flightNumber, String passengerName, int seats, String status) {
        this.bookingId = bookingId;
        this.flightNumber = flightNumber;
        this.passengerName = passengerName;
        this.seats = seats;
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}