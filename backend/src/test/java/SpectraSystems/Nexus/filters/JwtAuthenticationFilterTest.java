package SpectraSystems.Nexus.filters;

import SpectraSystems.Nexus.services.JwtService;
import SpectraSystems.Nexus.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.FilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock JwtService jwtService;
    @Mock UserService userService;
    @Mock UserDetailsService userDetailsService;
    @Mock FilterChain chain;

    JwtAuthenticationFilter filter;

    MockHttpServletRequest req;
    MockHttpServletResponse res;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        filter = new JwtAuthenticationFilter(jwtService, userService);
        req = new MockHttpServletRequest();
        res = new MockHttpServletResponse();
    }

    @Test
    void proceeds_whenNoAuthorizationHeader() throws Exception {
        filter.doFilter(req, res, chain);
        verify(chain).doFilter(req, res);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void proceeds_whenNonBearerScheme() throws Exception {
        req.addHeader("Authorization", "Basic xyz");
        filter.doFilter(req, res, chain);
        verify(chain).doFilter(req, res);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void proceeds_whenBearerButBlankToken() throws Exception {
        req.addHeader("Authorization", "Bearer ");
        when(jwtService.extractUserName("")).thenReturn("");
        filter.doFilter(req, res, chain);
        verify(chain).doFilter(req, res);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void authenticates_whenValidToken() throws Exception {
        String token = "good.token";
        String username = "alice";
        req.addHeader("Authorization", "Bearer " + token);

        UserDetails user = User.withUsername(username).password("x").roles("USER").build();

        when(jwtService.extractUserName(token)).thenReturn(username);
        when(userService.userDetailsService()).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(user);
        when(jwtService.isTokenValid(token, user)).thenReturn(true);

        filter.doFilter(req, res, chain);

        verify(chain).doFilter(req, res);
        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(auth.getPrincipal()).isEqualTo(user);
        assertThat(auth.isAuthenticated()).isTrue();

        verify(jwtService).extractUserName(token);
        verify(jwtService).isTokenValid(token, user);
        verify(userDetailsService).loadUserByUsername(username);
    }

    @Test
    void doesNotAuthenticate_whenTokenInvalid() throws Exception {
        String token = "bad.token";
        String username = "alice";
        req.addHeader("Authorization", "Bearer " + token);

        UserDetails user = User.withUsername(username).password("x").roles("USER").build();

        when(jwtService.extractUserName(token)).thenReturn(username);
        when(userService.userDetailsService()).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(user);
        when(jwtService.isTokenValid(token, user)).thenReturn(false);

        filter.doFilter(req, res, chain);

        verify(chain).doFilter(req, res);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void leavesExistingAuthentication_whenAlreadyAuthenticated() throws Exception {
        var existing = new UsernamePasswordAuthenticationToken("pre", "N/A");
        SecurityContextHolder.getContext().setAuthentication(existing);

        req.addHeader("Authorization", "Bearer some.token");
        when(jwtService.extractUserName("some.token")).thenReturn("alice");

        filter.doFilter(req, res, chain);

        verify(chain).doFilter(req, res);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(existing);
        // No need to hit userService/jwtService further
        verify(userService, never()).userDetailsService();
        verify(jwtService, never()).isTokenValid(anyString(), any());
    }
}