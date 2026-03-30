package com.assil.flightbooking.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createBooking_shouldReturn201() throws Exception {
        String requestBody = """
                {
                  "flightNumber": "AC101",
                  "passengerName": "Assil Azim",
                  "seats": 2
                }
                """;

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    void createBooking_shouldReturn409WhenOverbooked() throws Exception {
        String requestBody = """
                {
                  "flightNumber": "DL300",
                  "passengerName": "Too Many Seats",
                  "seats": 999
                }
                """;

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    void cancelBooking_shouldReturn204() throws Exception {
        String createRequestBody = """
                {
                  "flightNumber": "WS201",
                  "passengerName": "Cancel Me",
                  "seats": 1
                }
                """;

        MvcResult createResult = mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestBody))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String bookingId = extractBookingId(responseBody);

        mockMvc.perform(delete("/bookings/{bookingId}", bookingId))
                .andExpect(status().isNoContent());
    }

    private String extractBookingId(String responseBody) {
        String marker = "\"bookingId\":\"";
        int start = responseBody.indexOf(marker);

        if (start == -1) {
            throw new IllegalStateException("bookingId not found in response");
        }

        start += marker.length();
        int end = responseBody.indexOf("\"", start);

        if (end == -1) {
            throw new IllegalStateException("bookingId value not terminated properly");
        }

        return responseBody.substring(start, end);
    }
}