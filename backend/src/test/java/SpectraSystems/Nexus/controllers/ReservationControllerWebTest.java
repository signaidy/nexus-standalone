package SpectraSystems.Nexus.controllers;

import SpectraSystems.Nexus.models.Reservation;
import SpectraSystems.Nexus.services.ReservationService;
import SpectraSystems.Nexus.services.UserService;
import SpectraSystems.Nexus.repositories.ProviderRepository;
import SpectraSystems.Nexus.testsupport.TestSecurityConfig;

import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReservationController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class ReservationControllerWebTest {

    @Autowired MockMvc mvc;
    
    @MockBean
    private SpectraSystems.Nexus.services.JwtService jwtService;

    @MockBean ReservationService reservationService;
    @MockBean ProviderRepository providerRepository;
    @MockBean(answer = Answers.RETURNS_DEEP_STUBS) UserService userService;
    @MockBean UserDetailsService userDetailsService;
    @MockBean org.springframework.web.client.RestTemplate restTemplate;
    @MockBean JavaMailSender emailSender;

    // ---------- GET collections ----------
    @Test
    void getAllReservations_ok() throws Exception {
        when(reservationService.getAllReservations()).thenReturn(List.of());

        mvc.perform(get("/reservations"))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));

        verify(reservationService).getAllReservations();
    }

    @Test
    void getAllReservationsByUserId_ok() throws Exception {
        when(reservationService.getAllReservationsByUserId(7L)).thenReturn(List.of());

        mvc.perform(get("/reservations/user/{userId}", 7))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));

        verify(reservationService).getAllReservationsByUserId(7L);
    }

    // ---------- GET by id (200 / 404) ----------
    @Test
    void getReservationById_found_and_notFound() throws Exception {
        when(reservationService.getReservationById(42L)).thenReturn(Optional.of(new Reservation()));
        when(reservationService.getReservationById(99L)).thenReturn(Optional.empty());

        mvc.perform(get("/reservations/{id}", 42))
           .andExpect(status().isOk());

        mvc.perform(get("/reservations/{id}", 99))
           .andExpect(status().isNotFound());
    }

    // ---------- POST create (currently returns 400 in controller) ----------
    @Test
    void createReservation_badRequest_whenProviderMissing() throws Exception {
        // providerId is required=true; omitting it should 400 at the framework level
        mvc.perform(post("/reservations")
                .contentType(APPLICATION_JSON)
                .content("{}"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void createReservation_badRequest_evenWithProviderId_dueToStubbedController() throws Exception {
        // Current controller returns badRequest() regardless
        mvc.perform(post("/reservations")
                .param("providerId", "123")
                .contentType(APPLICATION_JSON)
                .content("{}"))
           .andExpect(status().isBadRequest());
    }

    // ---------- PUT update (secured) ----------
    @Test
    @WithMockUser(roles = "USER")
    void updateReservation_ok() throws Exception {
        when(reservationService.updateReservation(eq(5L), any(Reservation.class)))
                .thenReturn(new Reservation());

        mvc.perform(put("/reservations/{id}", 5)
                .contentType(APPLICATION_JSON)
                .content("{\"roomType\":\"DELUXE\"}"))
           .andExpect(status().isOk());

        verify(reservationService).updateReservation(eq(5L), any(Reservation.class));
    }

    // ---------- DELETE (secured) ----------
    @Test
    @WithMockUser(roles = "USER")
    void deleteReservation_noContent() throws Exception {
        doNothing().when(reservationService).deleteReservation(9L);

        mvc.perform(delete("/reservations/{id}", 9))
           .andExpect(status().isNoContent());

        verify(reservationService).deleteReservation(9L);
    }

    // ---------- Cancel endpoints ----------
    @Test
    void cancelReservationsByHotelId_ok() throws Exception {
        mvc.perform(put("/reservations/cancelHotel/{hotelId}", "HOTEL-123"))
           .andExpect(status().isOk());

        verify(reservationService).cancelReservationsByHotelId("HOTEL-123");
    }

    @Test
    void cancelReservationById_ok() throws Exception {
        mvc.perform(put("/reservations/cancel/{id}", "RES-1"))
           .andExpect(status().isOk());

        verify(reservationService).cancelReservationsById("RES-1");
    }

    // ---------- External hotel search stubs ----------
    @Test
    void hotelsearch_ok_emptyList() throws Exception {
        mvc.perform(get("/reservations/hotelsearch")
                .param("city", "GUA")
                .param("check-in", "2025-10-01")
                .param("check-out", "2025-10-03")
                .param("guests", "2"))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
    }

    @Test
    void roomsearch_badRequest_cases() throws Exception {
        // Missing providerId -> Spring will 400 for missing required param
        mvc.perform(get("/reservations/roomsearch")
                .param("id", "H-1")
                .param("city", "GUA"))
           .andExpect(status().isBadRequest());

        // With providerId -> controller currently returns badRequest() as well
        mvc.perform(get("/reservations/roomsearch")
                .param("id", "H-1")
                .param("city", "GUA")
                .param("providerId", "7"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void cities_ok() throws Exception {
        mvc.perform(get("/reservations/cities"))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
    }

    @Test
    void createReservation_alwaysBadRequest_now() throws Exception {
        mvc.perform(post("/reservations")
                .param("providerId", "1")
                .contentType(APPLICATION_JSON)
                .content("{}"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void getHotelRoomById_alwaysBadRequest_now() throws Exception {
        mvc.perform(get("/reservations/roomsearch")
                .param("id", "H1")
                .param("providerId", "1"))
           .andExpect(status().isBadRequest());
    }
}