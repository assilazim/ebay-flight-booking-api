package com.assil.flightbooking.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.assil.flightbooking.dto.BookingResponse;
import com.assil.flightbooking.dto.CreateBookingRequest;
import com.assil.flightbooking.exception.BookingNotFoundException;
import com.assil.flightbooking.exception.FlightNotFoundException;
import com.assil.flightbooking.exception.OverbookingException;
import com.assil.flightbooking.model.Booking;
import com.assil.flightbooking.model.Flight;

import jakarta.annotation.PostConstruct;

@Service
public class BookingService {

    private final Map<String, Flight> flights = new HashMap<>();
    private final Map<String, Booking> bookings = new HashMap<>();

    @PostConstruct
    public void loadFlights() {
        flights.put("AC101", new Flight("AC101", 100, 0));
        flights.put("WS201", new Flight("WS201", 80, 0));
        flights.put("DL300", new Flight("DL300", 50, 0));
    }

    public BookingResponse createBooking(CreateBookingRequest request) {
        Flight flight = flights.get(request.getFlightNumber());

        if (flight == null) {
            throw new FlightNotFoundException("Flight not found: " + request.getFlightNumber());
        }

        synchronized (flight) {
            int remainingSeats = flight.getCapacity() - flight.getBookedSeats();

            if (request.getSeats() > remainingSeats) {
                throw new OverbookingException(
                        "Not enough seats available for flight " + flight.getFlightNumber()
                );
            }

            flight.setBookedSeats(flight.getBookedSeats() + request.getSeats());

            String bookingId = UUID.randomUUID().toString();
            Booking booking = new Booking(
                    bookingId,
                    request.getFlightNumber(),
                    request.getPassengerName(),
                    request.getSeats(),
                    "CONFIRMED"
            );

            bookings.put(bookingId, booking);

            return toResponse(booking);
        }
    }

    public void cancelBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);

        if (booking == null) {
            throw new BookingNotFoundException("Booking not found: " + bookingId);
        }

        Flight flight = flights.get(booking.getFlightNumber());

        if (flight == null) {
            throw new FlightNotFoundException("Flight not found: " + booking.getFlightNumber());
        }

        synchronized (flight) {
            flight.setBookedSeats(flight.getBookedSeats() - booking.getSeats());
            bookings.remove(bookingId);
        }
    }

    private BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getBookingId(),
                booking.getFlightNumber(),
                booking.getPassengerName(),
                booking.getSeats(),
                booking.getStatus()
        );
    }
}