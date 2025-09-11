package SpectraSystems.Nexus.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FlightPurchaseRequestModelTest {

    @Test
    void defaultCtor_setters_getters_work() {
        FlightPurchaseRequest req = new FlightPurchaseRequest();

        Date dep = new Date(1735689600000L);     // arbitrary fixed dates
        Date ret = new Date(1736294400000L);
        Date pur = new Date(1733779200000L);

        req.setUser_id(7L);
        req.setUserId(8L);
        req.setFlightId(123L);
        req.setState("active");
        req.setType("ECONOMY");
        req.setDepartureDate(dep);
        req.setDepartureLocation("GUA");
        req.setArrivalLocation("LAX");
        req.setReturnDate(ret);
        req.setPurchaseDate(pur);
        req.setPrice(199.99);
        req.setRating(5L);
        req.setBundle("B-123");

        assertEquals(7L, req.getUser_id());
        assertEquals(8L, req.getUserId());
        assertEquals(123L, req.getFlightId());
        assertEquals("active", req.getState());
        assertEquals("ECONOMY", req.getType());
        assertEquals(dep, req.getDepartureDate());
        assertEquals("GUA", req.getDepartureLocation());
        assertEquals("LAX", req.getArrivalLocation());
        assertEquals(ret, req.getReturnDate());
        assertEquals(pur, req.getPurchaseDate());
        assertEquals(199.99, req.getPrice(), 0.0001);
        assertEquals(5L, req.getRating());
        assertEquals("B-123", req.getBundle());
    }

    @Test
    void allArgsCtor_maps_fields_correctly() {
        Date dep = new Date(1704067200000L);
        Date ret = new Date(1704672000000L);
        Date pur = new Date(1701302400000L);

        FlightPurchaseRequest req = new FlightPurchaseRequest(
                9L,             // userId
                456L,           // flightId
                "active",       // state
                "BUSINESS",     // type
                dep,            // departureDate
                "SFO",          // departureLocation
                "JFK",          // arrivalLocation
                ret,            // returnDate
                4L,             // rating
                pur,            // purchaseDate
                "B-999",        // bundle
                999.50          // price
        );

        assertEquals(9L, req.getUserId());
        assertEquals(456L, req.getFlightId());
        assertEquals("active", req.getState());
        assertEquals("BUSINESS", req.getType());
        assertEquals(dep, req.getDepartureDate());
        assertEquals("SFO", req.getDepartureLocation());
        assertEquals("JFK", req.getArrivalLocation());
        assertEquals(ret, req.getReturnDate());
        assertEquals(4L, req.getRating());
        assertEquals(pur, req.getPurchaseDate());
        assertEquals("B-999", req.getBundle());
        assertEquals(999.50, req.getPrice(), 0.0001);
    }

    @Test
    void jackson_serialization_roundTrip_includes_user_id_and_userId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        FlightPurchaseRequest req = new FlightPurchaseRequest();
        req.setUser_id(1L);
        req.setUserId(2L);
        req.setFlightId(777L);
        req.setType("ECONOMY");

        String json = mapper.writeValueAsString(req);

        // both properties should be present as distinct keys
        assertTrue(json.contains("\"user_id\":1"));
        assertTrue(json.contains("\"userId\":2"));
        assertTrue(json.contains("\"flightId\":777"));
        assertTrue(json.contains("\"type\":\"ECONOMY\""));

        // round-trip
        FlightPurchaseRequest back = mapper.readValue(json, FlightPurchaseRequest.class);
        assertEquals(1L, back.getUser_id());
        assertEquals(2L, back.getUserId());
        assertEquals(777L, back.getFlightId());
        assertEquals("ECONOMY", back.getType());
    }
}