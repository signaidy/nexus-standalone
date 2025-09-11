package SpectraSystems.Nexus.controllers;

import SpectraSystems.Nexus.models.Provider;
import SpectraSystems.Nexus.models.Type;
import SpectraSystems.Nexus.services.ProviderServices;
import SpectraSystems.Nexus.services.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProviderControllerWebTest {

    @Autowired MockMvc mvc;

    @MockBean ProviderServices providerService;

    // Keep the app context happy (SecurityConfig, etc.)
    @MockBean(answer = Answers.RETURNS_DEEP_STUBS) UserService userService;
    @MockBean UserDetailsService userDetailsService;
    @MockBean org.springframework.web.client.RestTemplate restTemplate;

    @Test
    void getAllProviders_ok() throws Exception {
        when(providerService.getAllProviders()).thenReturn(List.of(new Provider(), new Provider()));

        mvc.perform(get("/nexus/providers"))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));

        verify(providerService).getAllProviders();
    }

    @Test
    void getProviderById_found_and_notFound() throws Exception {
        when(providerService.getProviderById(42L)).thenReturn(Optional.of(new Provider()));
        when(providerService.getProviderById(99L)).thenReturn(Optional.empty());

        mvc.perform(get("/nexus/providers/{id}", 42))
           .andExpect(status().isOk());

        mvc.perform(get("/nexus/providers/{id}", 99))
           .andExpect(status().isNotFound());
    }

    @Test
    void getProviderByType_ok() throws Exception {
        when(providerService.getProviderByType(Type.AEROLINEA)).thenReturn(List.of());

        mvc.perform(get("/nexus/providers/type/{type}", "AEROLINEA"))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));

        verify(providerService).getProviderByType(Type.AEROLINEA);
    }

    @Test
    void createProvider_created() throws Exception {
        when(providerService.createProvider(any(Provider.class))).thenReturn(new Provider());

        mvc.perform(post("/nexus/providers")
                .contentType(APPLICATION_JSON)
                .content("{}"))
           .andExpect(status().isCreated());

        verify(providerService).createProvider(any(Provider.class));
    }

    @Test
    void updateProvider_ok() throws Exception {
        when(providerService.updateProvider(any(Provider.class))).thenReturn(new Provider());

        mvc.perform(put("/nexus/providers/{id}", 7L)
                .contentType(APPLICATION_JSON)
                .content("{\"providerName\":\"X\"}"))
           .andExpect(status().isOk());

        verify(providerService).updateProvider(any(Provider.class));
    }

    @Test
    void updateProvider_notFound() throws Exception {
        when(providerService.updateProvider(any(Provider.class))).thenReturn(null);

        mvc.perform(put("/nexus/providers/{id}", 7L)
                .contentType(APPLICATION_JSON)
                .content("{\"providerName\":\"X\"}"))
           .andExpect(status().isNotFound());
    }

    @Test
    void deleteProvider_noContent() throws Exception {
        doNothing().when(providerService).deleteProvider(5L);

        mvc.perform(delete("/nexus/providers/{id}", 5L))
           .andExpect(status().isNoContent());

        verify(providerService).deleteProvider(5L);
    }
}