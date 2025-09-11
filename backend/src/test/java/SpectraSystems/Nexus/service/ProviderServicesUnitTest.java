package SpectraSystems.Nexus.services;

import SpectraSystems.Nexus.models.Provider;
import SpectraSystems.Nexus.models.Type;
import SpectraSystems.Nexus.repositories.ProviderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProviderServicesUnitTest {

    @Mock ProviderRepository repo;
    @InjectMocks ProviderServices service;

    @Test
    void getAllProviders_delegates_to_repo() {
        when(repo.findAll()).thenReturn(List.of(new Provider(), new Provider()));
        assertEquals(2, service.getAllProviders().size());
        verify(repo).findAll();
    }

    @Test
    void getProviderById_present_and_empty() {
        Provider p = new Provider();
        when(repo.findById(1L)).thenReturn(Optional.of(p));
        when(repo.findById(9L)).thenReturn(Optional.empty());

        assertTrue(service.getProviderById(1L).isPresent());
        assertTrue(service.getProviderById(9L).isEmpty());
    }

    @Test
    void getProviderByType_delegates_to_repo() {
        when(repo.findByType(Type.HOTEL)).thenReturn(List.of(new Provider()));
        assertEquals(1, service.getProviderByType(Type.HOTEL).size());
        verify(repo).findByType(Type.HOTEL);
    }

    @Test
    void createProvider_saves_and_returns() {
        Provider in = Provider.builder()
                .providerName("Avianca")
                .providerUrl("https://api.example.com")
                .type(Type.AEROLINEA)
                .gainsFlights(0.1)
                .gainsHotel(0.0)
                .percentageDiscount(0.05)
                .build();

        Provider saved = new Provider();
        saved.setId(7L);
        when(repo.save(in)).thenReturn(saved);

        Provider out = service.createProvider(in);
        assertSame(saved, out);
        verify(repo).save(in);
    }

    @Test
    void updateProvider_happyPath_copies_fields_and_saves() {
        // existing entity in DB
        Provider existing = new Provider();
        existing.setId(10L);
        existing.setProviderName("OldName");
        existing.setProviderUrl("http://old");
        existing.setType(Type.HOTEL);
        existing.setPercentageDiscount(0.0);
        existing.setGainsFlights(0.0);
        existing.setGainsHotel(0.0);

        // incoming update (note: setters take primitives for some fields)
        Provider incoming = new Provider();
        incoming.setId(10L);
        incoming.setProviderName("NewName");
        incoming.setProviderUrl("https://new");
        incoming.setType(Type.AEROLINEA);
        incoming.setPercentageDiscount(0.15);
        incoming.setGainsFlights(0.2);
        incoming.setGainsHotel(0.3);

        when(repo.findById(10L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Provider.class))).thenAnswer(inv -> inv.getArgument(0));

        Provider out = service.updateProvider(incoming);

        assertNotNull(out);
        assertEquals(10L, out.getId());
        assertEquals("NewName", out.getProviderName());
        assertEquals("https://new", out.getProviderUrl());
        assertEquals(Type.AEROLINEA, out.getType());
        assertEquals(0.15, out.getPercentageDiscount());
        assertEquals(0.2, out.getGainsFlights());
        assertEquals(0.3, out.getGainsHotel());

        ArgumentCaptor<Provider> cap = ArgumentCaptor.forClass(Provider.class);
        verify(repo).save(cap.capture());
        Provider saved = cap.getValue();
        assertEquals("NewName", saved.getProviderName());
        assertEquals(Type.AEROLINEA, saved.getType());
    }

    @Test
    void updateProvider_notFound_returns_null_and_doesNotSave() {
        Provider incoming = new Provider();
        incoming.setId(999L);
        when(repo.findById(999L)).thenReturn(Optional.empty());

        Provider out = service.updateProvider(incoming);

        assertNull(out);
        verify(repo, never()).save(any());
    }

    @Test
    void deleteProvider_calls_repo() {
        doNothing().when(repo).deleteById(5L);
        service.deleteProvider(5L);
        verify(repo).deleteById(5L);
    }
}