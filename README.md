# Flight Booking API

A minimal Spring Boot REST API for booking flight tickets using in-memory storage.

## Scope and assumptions

This solution keeps the scope intentionally small to match the take-home requirements and time constraint.

Assumptions made:
- The client already knows the `flightNumber`
- Flights are preloaded in memory on startup
- The application runs as a single instance
- No authentication, authorization, or rate limiting is required
- No flight search or destination lookup is included
- Only booking APIs are implemented
- Flights must not be overbooked
- Cancelling a booking releases seats back to the flight

## Design choices

- Used in-memory storage instead of a database to stay aligned with the assignment requirements
- Used simple request/response DTOs to keep the API contract separate from internal models
- Used per-flight synchronization in the booking flow to prevent overbooking in a single-instance application
- Added a few integration tests for the main flows:
  - successful booking
  - overbooking rejection
  - cancellation

## How to run

Make sure Java 17 is installed.

Run the service with:

```bash
./mvnw spring-boot:run
```
## Example Requests

### Create a Booking - Expected 201:

```bash
curl -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "AC101",
    "passengerName": "Assil Azim",
    "seats": 2
  }'
```
### Cancel a booking - Expected 204:

```bash
curl -X DELETE http://localhost:8080/bookings/{bookingId}
```
## Overbooking check - Expected 409:
```bash
curl -i -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "DL300",
    "passengerName": "Too Many Seats",
    "seats": 999
  }'
```
## Validation check - Expected 400:
```bash
curl -i -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "",
    "passengerName": "",
    "seats": 0
  }'
```
## Missing flight check - Expected 404:
```bash
curl -i -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "NOPE999",
    "passengerName": "Assil Azim",
    "seats": 1
  }'
```
########

With more time may seperate storage concerns within a class and inject within service class. 