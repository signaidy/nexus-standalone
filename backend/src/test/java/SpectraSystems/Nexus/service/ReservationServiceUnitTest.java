package SpectraSystems.Nexus.services;

import SpectraSystems.Nexus.exceptions.ResourceNotFoundException;
import SpectraSystems.Nexus.models.Flight;
import SpectraSystems.Nexus.models.Reservation;
import SpectraSystems.Nexus.models.User;
import SpectraSystems.Nexus.repositories.FlightRepository;
import SpectraSystems.Nexus.repositories.ReservationRepository;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceUnitTest {

    @Mock ReservationRepository reservationRepository;
    @Mock FlightRepository flightRepository;
    @Mock UserService userService;
    @Mock JavaMailSender emailSender;

    @InjectMocks ReservationService service;

    @AfterEach
    void clearCtx() {
        SecurityContextHolder.clearContext();
    }

    // ---------- simple passthroughs ----------
    @Test
    void getAllReservations_ok() {
        when(reservationRepository.findAll()).thenReturn(List.of(new Reservation(), new Reservation()));
        assertEquals(2, service.getAllReservations().size());
        verify(reservationRepository).findAll();
    }

    @Test
    void getReservationById_present_and_empty() {
        Reservation r = new Reservation(); r.setId(1L);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));
        when(reservationRepository.findById(9L)).thenReturn(Optional.empty());

        assertTrue(service.getReservationById(1L).isPresent());
        assertTrue(service.getReservationById(9L).isEmpty());
    }

    @Test
    void getReservationByReservationNumber_ok() {
        Reservation r = new Reservation(); r.setReservationNumber("ABC");
        when(reservationRepository.findByReservationNumber("ABC")).thenReturn(Optional.of(r));
        assertTrue(service.getReservationByReservationNumber("ABC").isPresent());
        verify(reservationRepository).findByReservationNumber("ABC");
    }

    @Test
    void getAllReservationsByUserId_ok() {
        when(reservationRepository.findByUserid(7L)).thenReturn(List.of());
        assertNotNull(service.getAllReservationsByUserId(7L));
        verify(reservationRepository).findByUserid(7L);
    }

    // ---------- createReservation uses SecurityContext principal ----------
    @Test
    void createReservation_setsAuthenticatedUser_andActive_andSaves() {
        // Arrange authenticated principal (your domain User)
        User principal = User.builder().id(123L).email("u@example.com").build();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(principal, null, List.of());
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        ctx.setAuthentication(auth);
        SecurityContextHolder.setContext(ctx);

        Reservation input = new Reservation();
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(inv -> inv.getArgument(0)); // echo back saved

        // Act
        Reservation out = service.createReservation(input);

        // Assert
        assertEquals(123L, out.getUser());
        assertEquals("active", out.getState());

        ArgumentCaptor<Reservation> cap = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository).save(cap.capture());
        Reservation saved = cap.getValue();
        assertEquals(123L, saved.getUser());
        assertEquals("active", saved.getState());
    }

    // ---------- cancel by hotel id propagates to flights ----------
    @Test
    void cancelReservationsByHotelId_marksReservations_and_Flights_cancelled() {
        Reservation r1 = new Reservation();
        r1.setId(1L); r1.setBundle("B1"); r1.setState("active");

        Reservation r2 = new Reservation();
        r2.setId(2L); r2.setBundle("B2"); r2.setState("active");

        when(reservationRepository.findAllByHotelId("H-10")).thenReturn(List.of(r1, r2));

        Flight f1 = new Flight(); f1.setId(11L); f1.setState("active");
        when(flightRepository.findByBundle("B1")).thenReturn(List.of(f1));
        when(flightRepository.findByBundle("B2")).thenReturn(List.of()); // none

        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));
        when(flightRepository.save(any(Flight.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        service.cancelReservationsByHotelId("H-10");

        // Assert reservations saved with "cancelled"
        ArgumentCaptor<Reservation> rcap = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository, times(2)).save(rcap.capture());
        assertTrue(rcap.getAllValues().stream().allMatch(r -> "cancelled".equals(r.getState())));

        // Assert flight saved with "cancelled"
        ArgumentCaptor<Flight> fcap = ArgumentCaptor.forClass(Flight.class);
        verify(flightRepository).save(fcap.capture());
        assertEquals("cancelled", fcap.getValue().getState());
    }

    // ---------- cancel by reservation number sends emails & cascades ----------
    @Test
    void cancelReservationsById_ok_sendsEmails_andCancelsFlights() throws Exception {
        // Arrange a reservation found by its reservation number
        Reservation r = new Reservation();
        r.setReservationNumber("RES-1");
        r.setBundle("B-1");
        r.setUser(7L);

        when(reservationRepository.findByReservationNumber("RES-1"))
                .thenReturn(Optional.of(r));

        // Flights with the same bundle
        Flight f1 = new Flight(); f1.setId(1L);
        Flight f2 = new Flight(); f2.setId(2L);
        when(flightRepository.findByBundle("B-1"))
                .thenReturn(List.of(f1, f2));

        // Email + user lookups for sendCancellationEmail(...)
        when(userService.getUserById(7L))
                .thenReturn(Optional.of(User.builder().id(7L).email("u@e.com").build()));

        // Make sure the service has our mock injected even if @InjectMocks skips field injection
        ReflectionTestUtils.setField(service, "emailSender", emailSender);

        when(emailSender.createMimeMessage())
                .thenReturn(new jakarta.mail.internet.MimeMessage((jakarta.mail.Session) null));
        doNothing().when(emailSender).send(any(jakarta.mail.internet.MimeMessage.class));

        // Act
        service.cancelReservationsById("RES-1");

        // Assert: reservation cancelled & saved
        assertEquals("cancelled", r.getState());
        verify(reservationRepository).save(r);

        // Assert: both flights cancelled & saved
        verify(flightRepository, times(2)).save(any(Flight.class));

        // Assert: three emails total (1 for reservation + 2 for flights)
        verify(emailSender, times(3)).send(any(jakarta.mail.internet.MimeMessage.class));
    }

    @Test
    void cancelReservationsById_notFound_throws() {
        when(reservationRepository.findByReservationNumber("NOPE")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.cancelReservationsById("NOPE"));
        verify(emailSender, never()).send(any(MimeMessage.class));
        verify(flightRepository, never()).save(any());
    }

    // ---------- update / delete ----------
    @Test
    void updateReservation_happyPath_updatesFields_andSaves() {
        Reservation existing = new Reservation();
        existing.setId(5L);
        existing.setLocation("OLD");

        Reservation details = new Reservation();
        details.setUser(9L);
        details.setHotel("Hotel X");
        details.setRoomType("KING");
        details.setReservationNumber("R-123");
        details.setLocation("NEW");

        when(reservationRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));

        Reservation out = service.updateReservation(5L, details);

        assertEquals(9L, out.getUser());
        assertEquals("Hotel X", out.getHotel());
        assertEquals("KING", out.getRoomType());
        assertEquals("R-123", out.getReservationNumber());
        assertEquals("NEW", out.getLocation());

        ArgumentCaptor<Reservation> cap = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository).save(cap.capture());
        assertEquals(5L, cap.getValue().getId());
    }

    @Test
    void updateReservation_notFound_throws() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());
        Reservation details = new Reservation();
        assertThrows(ResourceNotFoundException.class, () -> service.updateReservation(99L, details));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void deleteReservation_callsRepository() {
        doNothing().when(reservationRepository).deleteById(7L);
        service.deleteReservation(7L);
        verify(reservationRepository).deleteById(7L);
    }

    @Test
    void getReservationsByHotelId_ok() {
        when(reservationRepository.findAllByHotelId("H-1")).thenReturn(List.of(new Reservation()));
        assertEquals(1, service.getReservationsByHotelId("H-1").size());
        verify(reservationRepository).findAllByHotelId("H-1");
    }
}