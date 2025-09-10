package SpectraSystems.Nexus.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import SpectraSystems.Nexus.filters.JwtAuthenticationFilter;
import SpectraSystems.Nexus.services.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    
    /** 
     * @return returns the 'AuthenticationProvider'
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    
    /** 
     * @param config
     * @return returns the 'AuthenticationManager'
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    
    /** 
     * @param http
     * @return returns the 'SecurityFilterChain'
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.PUT,  "/nexus/flights/deactivateTicket/{id}", "/nexus/flights/deactivate/{flightNumber}", "/nexus/reservations/cancel/{id}", "/nexus/reservations/cancelHotel/{hotelId}" ).permitAll()
                .requestMatchers(HttpMethod.POST, "/nexus/auth/signup", "/nexus/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/nexus/flights", "/nexus/flights/{id}", "/nexus/reservations", "/nexus/reservations/{id}", "/nexus/reservations/user/{userId}", "/nexus/aboutus", "/nexus/aboutus/{id}", "/nexus/flights/avianca/one-way-flights", "/nexus/flights/avianca/round-trip-flights" , "/nexus/flights/avianca/cities", "/nexus/reservations/hotelsearch", "/nexus/reservations/cities", "/nexus/reservations/hotelsearch/{id}", "/nexus/flights/user/{userId}").permitAll()
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
    }

}
