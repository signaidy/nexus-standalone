package SpectraSystems.Nexus.service;

import SpectraSystems.Nexus.dto.JwtAuthenticationResponse;
import SpectraSystems.Nexus.dto.SignInRequest;
import SpectraSystems.Nexus.dto.SignUpRequest;
import SpectraSystems.Nexus.models.Role;
import SpectraSystems.Nexus.models.User;
import SpectraSystems.Nexus.repositories.UserRepository;
import SpectraSystems.Nexus.services.AuthenticationService;
import SpectraSystems.Nexus.services.JwtService;
import SpectraSystems.Nexus.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserService userService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService auth; // class under test

    private User demoUser;

    @BeforeEach
    void setUp() {
        demoUser = User.builder()
                .first_Name("Jane")
                .last_Name("Doe")
                .email("jane@example.com")
                .password("hashed")
                .role(Role.ROLE_USER)
                .age(30)
                .country("ES")
                .passport("P123")
                .build();
    }

    @Test
    void signup_encodesPassword_savesUser_andReturnsToken() {
        // Arrange
        SignUpRequest req = new SignUpRequest();
        req.setFirst_Name("Jane");
        req.setLast_Name("Doe");
        req.setEmail("jane@example.com");
        req.setPassword("rawpass");
        req.setAge(30);
        req.setCountry("ES");
        req.setPassport("P123");

        when(passwordEncoder.encode("rawpass")).thenReturn("hashed");
        when(userService.save(any(User.class))).thenAnswer(inv -> {
            return inv.getArgument(0); // return saved user
        });
        when(jwtService.generateToken(any(User.class))).thenReturn("JWT123");

        // Act
        JwtAuthenticationResponse res = auth.signup(req);

        // Assert
        assertThat(res.getToken()).isEqualTo("JWT123");
        verify(passwordEncoder).encode("rawpass");
        verify(userService).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void signin_authenticates_loadsUser_generatesToken_andReturnsUser() {
        // Arrange
        SignInRequest req = new SignInRequest();
        req.setEmail("jane@example.com");
        req.setPassword("rawpass");

        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken("jane@example.com", "rawpass"));

        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(demoUser));
        when(jwtService.generateToken(demoUser)).thenReturn("JWT999");

        // Act
        JwtAuthenticationResponse res = auth.signin(req);

        // Assert
        assertThat(res.getToken()).isEqualTo("JWT999");
        assertThat(res.getUser()).isNotNull();
        assertThat(res.getUser().getEmail()).isEqualTo("jane@example.com");
        verify(authenticationManager).authenticate(any());
        verify(userRepository).findByEmail("jane@example.com");
        verify(jwtService).generateToken(demoUser);
    }

    @Test
    void signin_throwsOnBadCredentials() {
        SignInRequest req = new SignInRequest();
        req.setEmail("bad@example.com");
        req.setPassword("nope");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad creds"));

        assertThrows(BadCredentialsException.class, () -> auth.signin(req));
        verify(userRepository, never()).findByEmail(anyString());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void signin_throwsIfUserNotFoundAfterAuthentication() {
        SignInRequest req = new SignInRequest();
        req.setEmail("ghost@example.com");
        req.setPassword("ok");

        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken("ghost@example.com", "ok"));

        when(userRepository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> auth.signin(req));
        assertThat(ex).hasMessageContaining("Invalid email or password.");
        verify(jwtService, never()).generateToken(any());
    }
}