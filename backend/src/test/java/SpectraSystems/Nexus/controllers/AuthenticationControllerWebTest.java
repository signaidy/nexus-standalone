// backend/src/test/java/SpectraSystems/Nexus/controllers/AuthenticationControllerStandaloneTest.java
package SpectraSystems.Nexus.controllers;

import SpectraSystems.Nexus.dto.JwtAuthenticationResponse;
import SpectraSystems.Nexus.services.AuthenticationService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerStandaloneTest {

    @Mock private AuthenticationService authenticationService;

    // We directly construct the controller (no Spring context)
    @InjectMocks private AuthenticationController controller;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                // ensure JSON (de)serialization works in standalone mode
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void login_returns200AndTokenAndUser() throws Exception {
        when(authenticationService.signin(any())).thenReturn(
                JwtAuthenticationResponse.builder().token("JWT-CTRL").user(null).build()
        );

        mockMvc.perform(post("/nexus/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        Map.of("email", "user@example.com", "password", "secret"))))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.token").value("JWT-CTRL"));
    }

    @Test
    void signup_returns200AndToken() throws Exception {
        when(authenticationService.signup(any())).thenReturn(
                JwtAuthenticationResponse.builder().token("JWT-SIGNUP").build()
        );

        mockMvc.perform(post("/nexus/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        Map.of("first_Name","Jane","last_Name","Doe",
                               "email","jane@example.com","password","raw",
                               "age",30,"country","ES","passport","P123"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("JWT-SIGNUP"));
    }
}