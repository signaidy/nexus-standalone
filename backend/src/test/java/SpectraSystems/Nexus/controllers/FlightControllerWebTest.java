package SpectraSystems.Nexus.controllers;

import SpectraSystems.Nexus.models.*;
import SpectraSystems.Nexus.services.FlightService;
import SpectraSystems.Nexus.repositories.FlightRepository;
import SpectraSystems.Nexus.repositories.ReservationRepository;
import SpectraSystems.Nexus.services.UserService;
import SpectraSystems.Nexus.testsupport.TestSecurityConfig;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// IMPORTANT: limit to the controller(s) under test
@WebMvcTest(controllers = FlightController.class)
@AutoConfigureMockMvc(addFilters = false) // no security filters
@Import(TestSecurityConfig.class)         // minimal permit-all chain to satisfy security config
@ActiveProfiles("test")
class FlightControllerWebTest {

    @Autowired MockMvc mvc;
    
    @MockBean
    private SpectraSystems.Nexus.services.JwtService jwtService;

    // Mock every collaborator the controller touches
    @MockBean FlightService flightService;
    @MockBean FlightRepository flightRepository;
    @MockBean ReservationRepository reservationRepository;
    @MockBean(answer = Answers.RETURNS_DEEP_STUBS) UserService userService;
    @MockBean UserDetailsService userDetailsService;
    @MockBean org.springframework.web.client.RestTemplate restTemplate;
    @MockBean JavaMailSender emailSender;

    // ---------- simple GETs ----------
    @Test
    void getAllFlights_ok() throws Exception {
        Flight f1 = Flight.builder().id(1L).flightNumber("AA100").build();
        Flight f2 = Flight.builder().id(2L).flightNumber("AA200").build();
        when(flightService.getAllFlights()).thenReturn(List.of(f1, f2));

        mvc.perform(get("/flights"))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(flightService).getAllFlights();
    }

    @Test
    void getFlightById_found_and_notFound() throws Exception {
        Flight f = Flight.builder().id(42L).flightNumber("XY42").build();
        when(flightService.getFlightById(42L)).thenReturn(Optional.of(f));
        when(flightService.getFlightById(99L)).thenReturn(Optional.empty());

        mvc.perform(get("/flights/{id}", 42))
           .andExpect(status().isOk());
        mvc.perform(get("/flights/{id}", 99))
           .andExpect(status().isNotFound());
    }

    @Test
    void getAllFlightsByUserId_ok() throws Exception {
        when(flightService.getAllFlightsByUserId(7L)).thenReturn(List.of());
        mvc.perform(get("/flights/user/{userId}", 7))
           .andExpect(status().isOk());
        verify(flightService).getAllFlightsByUserId(7L);
    }

    @Test
    void avianca_lists_ok() throws Exception {
        when(flightService.getAllFlightsFromOtherBackend()).thenReturn(List.of());
        mvc.perform(get("/flights/avianca/flights"))
           .andExpect(status().isOk());

        when(flightService.getOneWayFlightsFromOtherBackend(1L, 2L, "2025-09-01", 2))
                .thenReturn(List.of());
        mvc.perform(get("/flights/avianca/one-way-flights")
                .param("originCity", "1")
                .param("destinationCity", "2")
                .param("departureDay", "2025-09-01")
                .param("passengers", "2"))
           .andExpect(status().isOk());

        // For round-trip we just exercise the path (empty lists is fine)
        when(flightService.getOneWayFlightsFromOtherBackend(10L, 20L, "2025-12-01", 1))
                .thenReturn(List.of());
        when(flightService.getOneWayFlightsFromOtherBackend(20L, 10L, "2025-12-10", 1))
                .thenReturn(List.of());
        mvc.perform(get("/flights/avianca/round-trip-flights")
                .param("originCity", "10")
                .param("destinationCity", "20")
                .param("departureDay", "2025-12-01")
                .param("returnDay", "2025-12-10")
                .param("passengers", "1"))
           .andExpect(status().isOk());
    }

   @Test
   void cities_ok() throws Exception {
      when(flightService.getAllCitiesFromOtherBackend()).thenReturn(List.of());

      mvc.perform(get("/flights/avianca/cities"))
         .andExpect(status().isOk());
      verify(flightService).getAllCitiesFromOtherBackend();
   }

    // ---------- create / update / delete (secured) ----------
    @Test
    @WithMockUser(roles = "USER")
    void createFlight_created() throws Exception {
        Flight created = Flight.builder().id(100L).flightNumber("NEW").build();
        when(flightService.createFlight(any(Flight.class))).thenReturn(created);

        mvc.perform(post("/flights")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")) // minimal body, we mock the service anyway
           .andExpect(status().isCreated());

        verify(flightService).createFlight(any(Flight.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateFlight_ok() throws Exception {
        Flight updated = Flight.builder().id(5L).flightNumber("UPD").build();
        when(flightService.updateFlight(eq(5L), any(Flight.class))).thenReturn(updated);

        mvc.perform(put("/flights/{id}", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"flightNumber\":\"UPD\"}"))
           .andExpect(status().isOk());

        verify(flightService).updateFlight(eq(5L), any(Flight.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteFlight_noContent() throws Exception {
        doNothing().when(flightService).deleteFlight(9L);
        mvc.perform(delete("/flights/{id}", 9))
           .andExpect(status().isNoContent());
        verify(flightService).deleteFlight(9L);
    }

    // ---------- purchase (emails + service call) ----------
    @Test
    void purchaseFlight_ok_sendsEmail() throws Exception {
        // service call succeeds
        doNothing().when(flightService)
                .purchaseFlight(eq(2), eq("card"), eq(77L), any(FlightPurchaseRequest.class));

        // email plumbing
        when(userService.getUserById(7L)).thenReturn(Optional.of(User.builder()
                .id(7L).email("user@example.com").build()));
        when(emailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        doNothing().when(emailSender).send(any(MimeMessage.class));

        String body = """
        {
          "user_id": 7,
          "flightId": 123,
          "type": "ECONOMY",
          "departureDate": "2025-10-01",
          "departureLocation": "GUA",
          "arrivalLocation": "LAX",
          "price": 199.99,
          "bundle": "B-123"
        }
        """;

        mvc.perform(post("/flights/purchase/{amount}/{method}/{providerId}", 2, "card", 77)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.message").value("Flight purchased successfully."));

        verify(flightService).purchaseFlight(eq(2), eq("card"), eq(77L), any(FlightPurchaseRequest.class));
        verify(emailSender).send(any(MimeMessage.class));
    }

    // ---------- deactivate by flight number ----------
    @Test
    void deactivateByFlightNumber_ok_flow() throws Exception {
        Flight f = new Flight();
        f.setId(1L);
        f.setBundle("BUNDLE1");
        f.setUser(7L);

        when(flightService.getFlightsByFlightNumber("AA100")).thenReturn(List.of(f));
        when(flightService.updateFlight(eq(1L), any(Flight.class))).thenReturn(f);

        Flight fb = new Flight();
        fb.setId(2L);
        fb.setBundle("BUNDLE1");
        fb.setUser(7L);
        when(flightRepository.findByBundle("BUNDLE1")).thenReturn(List.of(fb));

        Reservation r = new Reservation();
        r.setId(3L);
        r.setBundle("BUNDLE1");
        r.setUser(7L);
        when(reservationRepository.findByBundle("BUNDLE1")).thenReturn(List.of(r));

        when(userService.getUserById(7L)).thenReturn(Optional.of(User.builder().id(7L).email("u@e.com").build()));
        when(emailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        mvc.perform(put("/flights/deactivate/{flightNumber}", "AA100"))
           .andExpect(status().isOk());

        verify(flightService).getFlightsByFlightNumber("AA100");
        verify(flightRepository, atLeastOnce()).save(any(Flight.class));
        verify(reservationRepository, atLeastOnce()).save(any(Reservation.class));
        verify(emailSender, atLeastOnce()).send(any(MimeMessage.class));
    }

    @Test
    void deactivateByFlightNumber_notFound() throws Exception {
        when(flightService.getFlightsByFlightNumber("NONE")).thenReturn(List.of());
        mvc.perform(put("/flights/deactivate/{flightNumber}", "NONE"))
           .andExpect(status().isNotFound());
    }

    // ---------- deactivate by id ----------
    @Test
    void deactivateById_ok_and_notFound() throws Exception {
        Flight f = new Flight();
        f.setId(10L);
        f.setBundle("BUN-X");
        f.setUser(7L);

        when(flightService.getFlightById(10L)).thenReturn(Optional.of(f));
        when(flightRepository.findByBundle("BUN-X")).thenReturn(List.of(f));
        when(reservationRepository.findByBundle("BUN-X")).thenReturn(List.of());

        when(userService.getUserById(7L)).thenReturn(Optional.of(User.builder().id(7L).email("u@e.com").build()));
        when(emailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        mvc.perform(put("/flights/deactivateTicket/{id}", 10))
           .andExpect(status().isOk());

        when(flightService.getFlightById(99L)).thenReturn(Optional.empty());
        mvc.perform(put("/flights/deactivateTicket/{id}", 99))
           .andExpect(status().isNotFound());
    }

   private static externalFlight ef(long origin, long dest) {
      externalFlight e = new externalFlight();
      e.setOriginCityId(origin);
      e.setDestinationCityId(dest);
      return e;
   }

   @Test
   void roundTrip_match_when_bothHaveScale() throws Exception {
      // outbound: origin=10, outbound.scale.dest=99
      externalFlight outbound = ef(10, 20);
      outbound.setScale(ef(0, 99));

      // return: origin=99, return.scale.dest=10 (matches branch 1)
      externalFlight ret = ef(99, 77);
      ret.setScale(ef(0, 10));

      when(flightService.getOneWayFlightsFromOtherBackend(10L, 20L, "2025-12-01", 1))
               .thenReturn(List.of(outbound));
      when(flightService.getOneWayFlightsFromOtherBackend(20L, 10L, "2025-12-10", 1))
               .thenReturn(List.of(ret));

      mvc.perform(get("/flights/avianca/round-trip-flights")
               .param("originCity", "10")
               .param("destinationCity", "20")
               .param("departureDay", "2025-12-01")
               .param("returnDay", "2025-12-10")
               .param("passengers", "1"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$[0].returnFlight").exists());
   }

   @Test
   void roundTrip_match_when_onlyOutboundHasScale() throws Exception {
      // outbound: origin=10, outbound.scale.dest=99
      externalFlight outbound = ef(10, 20);
      outbound.setScale(ef(0, 99));

      // return: origin=99, dest=10 (matches branch 2)
      externalFlight ret = ef(99, 10);

      when(flightService.getOneWayFlightsFromOtherBackend(10L, 20L, "2025-12-01", 1))
               .thenReturn(List.of(outbound));
      when(flightService.getOneWayFlightsFromOtherBackend(20L, 10L, "2025-12-10", 1))
               .thenReturn(List.of(ret));

      mvc.perform(get("/flights/avianca/round-trip-flights")
               .param("originCity", "10")
               .param("destinationCity", "20")
               .param("departureDay", "2025-12-01")
               .param("returnDay", "2025-12-10")
               .param("passengers", "1"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$[0].returnFlight").exists());
   }

   @Test
   void roundTrip_match_when_noScale() throws Exception {
      // outbound: origin=10, dest=20
      externalFlight outbound = ef(10, 20);

      // return: origin=20, dest=10 (matches branch 3 / else)
      externalFlight ret = ef(20, 10);

      when(flightService.getOneWayFlightsFromOtherBackend(10L, 20L, "2025-12-01", 1))
               .thenReturn(List.of(outbound));
      when(flightService.getOneWayFlightsFromOtherBackend(20L, 10L, "2025-12-10", 1))
               .thenReturn(List.of(ret));

      mvc.perform(get("/flights/avianca/round-trip-flights")
               .param("originCity", "10")
               .param("destinationCity", "20")
               .param("departureDay", "2025-12-01")
               .param("returnDay", "2025-12-10")
               .param("passengers", "1"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$[0].returnFlight").exists());
   }
}