package SpectraSystems.Nexus.controllers;

import SpectraSystems.Nexus.filters.JwtAuthenticationFilter;
import SpectraSystems.Nexus.models.Aboutus;
import SpectraSystems.Nexus.services.AboutUsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AboutUsController.class,
        excludeAutoConfiguration = { SecurityAutoConfiguration.class }
)
@AutoConfigureMockMvc(addFilters = false)
class AboutUsControllerWebTest {

    @Autowired MockMvc mvc;

    @MockBean AboutUsService aboutUsService;
    @MockBean JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void getAllAboutUs_ok() throws Exception {
        when(aboutUsService.findAll()).thenReturn(List.of(new Aboutus(), new Aboutus()));

        mvc.perform(get("/aboutus").accept(APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));

        verify(aboutUsService).findAll();
    }

    @Test
    void getAboutUsById_found_and_notFound() throws Exception {
        when(aboutUsService.findById(1L)).thenReturn(Optional.of(new Aboutus()));
        when(aboutUsService.findById(99L)).thenReturn(Optional.empty());

        mvc.perform(get("/aboutus/{id}", 1L).accept(APPLICATION_JSON))
           .andExpect(status().isOk());

        mvc.perform(get("/aboutus/{id}", 99L).accept(APPLICATION_JSON))
           .andExpect(status().isNotFound());
    }

    @Test
    void createOrUpdateAboutUs_created() throws Exception {
        when(aboutUsService.saveOrUpdate(any(Aboutus.class))).thenReturn(new Aboutus());

        mvc.perform(post("/aboutus")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content("{}"))
           .andExpect(status().isCreated());

        verify(aboutUsService).saveOrUpdate(any(Aboutus.class));
    }

    @Test
    void updateAboutUs_ok() throws Exception {
        when(aboutUsService.findById(7L)).thenReturn(Optional.of(new Aboutus()));
        when(aboutUsService.saveOrUpdate(any(Aboutus.class))).thenReturn(new Aboutus());

        mvc.perform(put("/aboutus/{id}", 7L)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content("{}"))
           .andExpect(status().isOk());

        verify(aboutUsService).findById(7L);
        verify(aboutUsService).saveOrUpdate(any(Aboutus.class));
    }

    @Test
    void updateAboutUs_notFound() throws Exception {
        when(aboutUsService.findById(404L)).thenReturn(Optional.empty());

        mvc.perform(put("/aboutus/{id}", 404L)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content("{}"))
           .andExpect(status().isNotFound());

        verify(aboutUsService).findById(404L);
        verify(aboutUsService, never()).saveOrUpdate(any());
    }

    @Test
    void deleteAboutUs_noContent() throws Exception {
        doNothing().when(aboutUsService).deleteById(5L);

        mvc.perform(delete("/aboutus/{id}", 5L))
           .andExpect(status().isNoContent());

        verify(aboutUsService).deleteById(5L);
    }
}