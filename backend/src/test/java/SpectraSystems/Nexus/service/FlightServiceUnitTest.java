package SpectraSystems.Nexus.services;

import SpectraSystems.Nexus.exceptions.ResourceNotFoundException;
import SpectraSystems.Nexus.models.Flight;
import SpectraSystems.Nexus.repositories.FlightRepository;
import SpectraSystems.Nexus.repositories.TicketPurchaseRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Date; // <-- add this
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceUnitTest {

    @Mock FlightRepository flightRepository;
    @Mock RestTemplate restTemplate;
    @Mock TicketPurchaseRepository ticketPurchaseRepository;

    @InjectMocks FlightService service;

    @Test
    void getAllFlights_ok() {
        when(flightRepository.findAll()).thenReturn(List.of(new Flight(), new Flight()));
        List<Flight> out = service.getAllFlights();
        assertEquals(2, out.size());
        verify(flightRepository).findAll();
    }

    @Test
    void getFlightById_found_and_empty() {
        Flight f = Flight.builder().id(1L).build();
        when(flightRepository.findById(1L)).thenReturn(Optional.of(f));
        when(flightRepository.findById(9L)).thenReturn(Optional.empty());

        assertTrue(service.getFlightById(1L).isPresent());
        assertTrue(service.getFlightById(9L).isEmpty());
    }

    @Test
    void getAllFlightsByUserId_ok() {
        when(flightRepository.findByUserid(7L)).thenReturn(List.of());
        assertNotNull(service.getAllFlightsByUserId(7L));
        verify(flightRepository).findByUserid(7L);
    }

    @Test
    void getFlightsByFlightNumber_ok() {
        when(flightRepository.findAllByFlightNumber("AA100")).thenReturn(List.of());
        assertNotNull(service.getFlightsByFlightNumber("AA100"));
        verify(flightRepository).findAllByFlightNumber("AA100");
    }

    @Test
    void createFlight_nonNull_saves() {
        Flight in = Flight.builder().flightNumber("X").build();
        Flight saved = Flight.builder().id(10L).flightNumber("X").build();
        when(flightRepository.save(in)).thenReturn(saved);

        Flight out = service.createFlight(in);

        assertNotNull(out);
        assertEquals(10L, out.getId());
        verify(flightRepository).save(in);
    }

    @Test
    void createFlight_null_returnsNull_andDoesNotSave() {
        Flight out = service.createFlight(null);
        assertNull(out);
        verifyNoInteractions(flightRepository);
    }

    @Test
    void updateFlight_updatesFields_andSaves() {
        Flight existing = new Flight();
        existing.setId(5L);
        existing.setFlightNumber("OLD");
        existing.setDepartureLocation("AAA");
        existing.setArrivalLocation("BBB");

        Flight details = new Flight();
        details.setFlightNumber("NEW");
        details.setDepartureLocation("GUA");
        details.setArrivalLocation("LAX");
        Date expectedDate = java.sql.Date.valueOf(LocalDate.of(2025, 12, 1)); // <-- use java.util.Date
        details.setReturnDate(expectedDate);

        when(flightRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(flightRepository.save(any(Flight.class))).thenAnswer(inv -> inv.getArgument(0));

        Flight out = service.updateFlight(5L, details);

        assertEquals("NEW", out.getFlightNumber());
        assertEquals("GUA", out.getDepartureLocation());
        assertEquals("LAX", out.getArrivalLocation());
        assertEquals(expectedDate, out.getReturnDate()); // <-- assert Date

        ArgumentCaptor<Flight> cap = ArgumentCaptor.forClass(Flight.class);
        verify(flightRepository).save(cap.capture());
        Flight saved = cap.getValue();
        assertEquals(5L, saved.getId());
        assertEquals("NEW", saved.getFlightNumber());
    }

    @Test
    void updateFlight_notFound_throws() {
        when(flightRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.updateFlight(99L, new Flight()));
        verify(flightRepository, never()).save(any());
    }

    @Test
    void deleteFlight_callsRepository() {
        doNothing().when(flightRepository).deleteById(11L);
        service.deleteFlight(11L);
        verify(flightRepository).deleteById(11L);
    }

    @Test
    void getAllFlightsFromOtherBackend_returnsEmpty() {
        assertTrue(service.getAllFlightsFromOtherBackend().isEmpty());
        verifyNoInteractions(restTemplate);
    }

    @Test
    void getOneWayFlightsFromOtherBackend_returnsEmpty() {
        assertTrue(service.getOneWayFlightsFromOtherBackend(1L, 2L, "2025-10-01", 2).isEmpty());
        verifyNoInteractions(restTemplate);
    }

    @Test
    void getAllCitiesFromOtherBackend_returnsEmpty() {
        assertTrue(service.getAllCitiesFromOtherBackend().isEmpty());
        verifyNoInteractions(restTemplate);
    }

    @Test
    void purchaseFlight_doesNothing_now() throws Exception {
        service.purchaseFlight(2, "card", 77L, null);
        verifyNoInteractions(restTemplate, ticketPurchaseRepository, flightRepository);
    }
}