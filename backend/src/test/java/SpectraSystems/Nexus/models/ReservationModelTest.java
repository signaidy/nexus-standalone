package SpectraSystems.Nexus.models;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ReservationModelTest {

    @Test
    void builder_sets_fields_that_ctor_assigns_and_ignores_the_rest() {
        Date d1 = new Date(1_000_000L);
        Date d2 = new Date(2_000_000L);

        // NOTE: @Builder delegates to the explicit ctor, which ignores id/rating/bundle/providerId
        Reservation r = Reservation.builder()
                .id(1L)                    // will be IGNORED by ctor
                .hotelId("H-1")
                .userid(7L)                // maps to ctor param 'user'
                .hotel("Hotel A")
                .dateStart(d1)
                .dateEnd(d2)
                .roomType("KING")
                .reservationNumber("R-123")
                .location("NYC")
                .rating(5L)                // will be IGNORED by ctor
                .bedSize("Queen")
                .bedAmount(2)
                .price(199.99)
                .totalDays(3)
                .totalPrice(599.97)
                .guests(2)
                .state("whatever")         // ctor forces "active" anyway
                .bundle("B-1")             // will be IGNORED by ctor
                .providerId(55L)           // will be IGNORED by ctor
                .build();

        // ctor-assigned fields
        assertEquals("H-1", r.getHotelId());
        assertEquals(7L, r.getUser());
        assertEquals("Hotel A", r.getHotel());
        assertEquals(d1, r.getDateStart());
        assertEquals(d2, r.getDateEnd());
        assertEquals("KING", r.getRoomType());
        assertEquals("R-123", r.getReservationNumber());
        assertEquals("NYC", r.getLocation());
        assertEquals("Queen", r.getBedSize());
        assertEquals(2, r.getBedAmount());
        assertEquals(199.99, r.getPrice());
        assertEquals(3, r.getTotalDays());
        assertEquals(599.97, r.getTotalPrice());
        assertEquals(2, r.getGuests());

        // ctor behavior: force state + ignore some inputs
        assertEquals("active", r.getState(), "ctor forces state to 'active'");
        assertNull(r.getId(), "ctor ignores id");
        assertNull(r.getRating(), "ctor ignores rating");
        assertNull(r.getBundle(), "ctor ignores bundle");
        assertNull(r.getProviderId(), "ctor ignores providerId");
    }

    @Test
    void explicit_constructor_sets_subset_and_forces_active() {
        Date d1 = new Date(10L);
        Date d2 = new Date(20L);

        Reservation r = new Reservation(
                999L, "H-2", 77L, "Hotel B", d1, d2, "DOUBLE",
                "RSV-1", "GUA", 4L, "Full", 1, 80.0, 1, 80.0, 1,
                "ignored-state", "B-2", 123L
        );

        // ctor behavior
        assertNull(r.getId(), "ctor does not assign id");
        assertEquals("H-2", r.getHotelId());
        assertEquals(77L, r.getUser());
        assertEquals("Hotel B", r.getHotel());
        assertEquals(d1, r.getDateStart());
        assertEquals(d2, r.getDateEnd());
        assertEquals("DOUBLE", r.getRoomType());
        assertEquals("RSV-1", r.getReservationNumber());
        assertEquals("GUA", r.getLocation());
        assertEquals("Full", r.getBedSize());
        assertEquals(1, r.getBedAmount());
        assertEquals(80.0, r.getPrice());
        assertEquals(1, r.getTotalDays());
        assertEquals(80.0, r.getTotalPrice());
        assertEquals(1, r.getGuests());

        assertEquals("active", r.getState(), "ctor forces state to 'active'");
        assertNull(r.getRating(), "ctor ignores rating");
        assertNull(r.getBundle(), "ctor ignores bundle");
        assertNull(r.getProviderId(), "ctor ignores providerId");
    }

    @Test
    void setters_and_getters_cover_all_fields() {
        Date d1 = new Date(111L);
        Date d2 = new Date(222L);

        Reservation r = new Reservation();
        r.setId(5L);
        r.setHotelId("H-5");
        r.setUser(9L);
        r.setHotel("Hotel Z");
        r.setDateStart(d1);
        r.setDateEnd(d2);
        r.setRoomType("SUITE");
        r.setReservationNumber("R-999");
        r.setLocation("LAX");
        r.setRating(3L);
        r.setBedSize("King");
        r.setBedAmount(3);
        r.setPrice(123.45);
        r.setTotalDays(4);
        r.setTotalPrice(493.80);
        r.setGuests(4);
        r.setState("cancelled");
        r.setBundle("B-9");
        r.setProviderId(777L);

        assertEquals(5L, r.getId());
        assertEquals("H-5", r.getHotelId());
        assertEquals(9L, r.getUser());
        assertEquals("Hotel Z", r.getHotel());
        assertEquals(d1, r.getDateStart());
        assertEquals(d2, r.getDateEnd());
        assertEquals("SUITE", r.getRoomType());
        assertEquals("R-999", r.getReservationNumber());
        assertEquals("LAX", r.getLocation());
        assertEquals(3L, r.getRating());
        assertEquals("King", r.getBedSize());
        assertEquals(3, r.getBedAmount());
        assertEquals(123.45, r.getPrice());
        assertEquals(4, r.getTotalDays());
        assertEquals(493.80, r.getTotalPrice());
        assertEquals(4, r.getGuests());
        assertEquals("cancelled", r.getState());
        assertEquals("B-9", r.getBundle());
        assertEquals(777L, r.getProviderId());
    }
}