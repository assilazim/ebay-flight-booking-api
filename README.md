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
### Overbooking check - Expected 409:
```bash
curl -i -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "DL300",
    "passengerName": "Too Many Seats",
    "seats": 999
  }'
```
### Validation check - Expected 400:
```bash
curl -i -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "",
    "passengerName": "",
    "seats": 0
  }'
```
### Missing flight check - Expected 404:
```bash
curl -i -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "NOPE999",
    "passengerName": "Assil Azim",
    "seats": 1
  }'
```

## Full Prompts Used:

### Prompt 1:

Create a minimal Spring Boot Java REST API for a flight booking system.
Constraints:
- Use Spring Boot and Java
- In-memory storage only using HashMap, no database
- No authentication or authorization
- No flight search
- Assume the client already knows the flight number
- Ensure flights cannot be overbooked
- Only booking APIs are required
- Keep the scope small enough for a 60-minute take-home assignment

Implement:

- POST /bookings to create a booking
- DELETE /bookings/{bookingId} to cancel a booking

Use a few preloaded flights in memory on startup.
Requirements:

- Use clean package structure: controller, service, model, dto, exception
- Use proper HTTP methods and status codes
- Add basic validation
- Include simple global exception handling
- Keep the code clean, simple, and not overengineered

### Prompt 2:

Improve the existing Spring Boot flight booking API while keeping it lightweight and within the original take-home scope.

Requirements:

- Keep in-memory storage only
- Keep only booking APIs (no retrieval endpoints)
- Improve concurrency safety for a single-instance application
- Replace HashMap with a more concurrency-safe in-memory structure where appropriate
- Trim and normalize input values before processing
- Keep overbooking prevention correct
- Ensure cancellation does not create invalid seat counts
- Keep validation, exception handling, and HTTP status codes clean and simple
- Do not add a database, authentication, search, or extra features
- Keep the code easy to read and not overengineered

### Prompt 3:

Add basic integration tests for the Spring Boot flight booking API.
Requirements:

- Use Spring Boot test support
- Keep tests simple and focused
- Test the core flows only:
    - successful booking returns 201
    - overbooking returns 409
    - cancel booking returns 204
- Use the existing in-memory setup
- Do not add unnecessary mocking or complexity

### Prompt 4:

Fix Spring Boot project configuration to ensure test support works correctly.

- Replace non-standard dependencies with spring-boot-starter-web and spring-boot-starter-test
- Align project with Spring Boot 3.x conventions
- Ensure MockMvc and SpringBootTest annotations resolve properly
- Keep the project simple and aligned with the take-home scope

## With more time:

- With more time may seperate storage concerns within a repo / store layer and inject within service class. 
- Add retrieval endpoints (e.g., fetch booking or check flight availability) if the API scope were expanded
- Improve test coverage, especially around edge cases and concurrency scenarios
- Introduce idempotency support for booking and cancellation requests
- Add more robust concurrency handling and stress testing beyond the current single-instance assumptions
- Replace in-memory storage with a persistent database if durability and multi-instance scaling were required

