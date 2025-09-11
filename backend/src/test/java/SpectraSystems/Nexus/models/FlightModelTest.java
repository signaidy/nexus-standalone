package SpectraSystems.Nexus.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlightModelTest {

    @Test
    void partialConstructor_setsFields_andDefaults() {
        Date dep = new Date(1_700_000_000_000L);
        Date ret = new Date(1_701_000_000_000L);

        LocalDate before = LocalDate.now();
        Flight f = new Flight(
                10L,            // id
                20L,            // user
                "AA100",        // flightNumber
                dep,            // departureDate
                "GUA",          // departureLocation
                "LAX",          // arrivalLocation
                ret,            // returnDate
                "ECONOMY",      // type
                199.99,         // price
                "B-1"           // bundle
        );
        LocalDate after = LocalDate.now();

        assertEquals(10L, f.getId());
        assertEquals(20L, f.getUser()); // field name in entity is usually 'userid'
        assertEquals("AA100", f.getFlightNumber());
        assertEquals(dep, f.getDepartureDate());
        assertEquals("GUA", f.getDepartureLocation());
        assertEquals("LAX", f.getArrivalLocation());
        assertEquals(ret, f.getReturnDate());
        assertEquals("ECONOMY", f.getType());
        assertEquals(199.99, f.getPrice());
        assertEquals("B-1", f.getBundle());

        // defaults enforced by ctor
        assertEquals("active", f.getState());
        assertNotNull(f.getPurchaseDate());
        // be tolerant around the "now" boundary
        LocalDate pd = f.getPurchaseDate();
        assertTrue(!pd.isBefore(before.minusDays(1)) && !pd.isAfter(after.plusDays(1)));
    }

    @Test
    void specificSetters_updateFields() {
        Flight f = new Flight();

        Date newDep = new Date(1_702_000_000_000L);
        f.setDepartureDate(newDep);
        assertEquals(newDep, f.getDepartureDate());

        f.setType("BUSINESS");
        assertEquals("BUSINESS", f.getType());

        LocalDate customPurchase = LocalDate.of(2025, 1, 15);
        f.setPurchaseDate(customPurchase);
        assertEquals(customPurchase, f.getPurchaseDate());

        f.setProviderId(77L);
        assertEquals(77L, f.getProviderId());

        List<TicketPurchase> tickets = new ArrayList<>();
        tickets.add(new TicketPurchase());
        tickets.add(new TicketPurchase());
        f.setTickets(tickets);

        assertNotNull(f.getTickets());
        assertEquals(2, f.getTickets().size());
        // ensure it's the same list reference we set (no defensive copy)
        assertSame(tickets, f.getTickets());
    }

    @Test
    void builder_with_subset_of_fields_sets_only_those() {
        Flight f = Flight.builder()
                .id(5L)
                .userid(9L)
                .flightNumber("UA200")
                .price(450.0)
                .build();

        assertEquals(5L, f.getId());
        assertEquals(9L, f.getUser());
        assertEquals("UA200", f.getFlightNumber());
        assertEquals(450.0, f.getPrice());

        // fields we didn't set via builder should be null
        assertNull(f.getDepartureDate());
        assertNull(f.getArrivalLocation());
        assertNull(f.getType());
        assertNull(f.getReturnDate());
        assertNull(f.getProviderId());
        assertNull(f.getBundle());
    }
}