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
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // health & auth – allow both with/without /nexus
                .requestMatchers("/healthz", "/nexus/healthz").permitAll()
                .requestMatchers("/auth/**", "/nexus/auth/**").permitAll()

                // preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // public GET APIs (both forms)
                .requestMatchers(HttpMethod.GET,
                    "/flights/**", "/reservations/**", "/aboutus/**",
                    "/nexus/flights/**", "/nexus/reservations/**", "/nexus/aboutus/**"
                ).permitAll()

                // public PUTs (both forms)
                .requestMatchers(HttpMethod.PUT,
                    "/flights/deactivateTicket/**",
                    "/flights/deactivate/**",
                    "/reservations/cancel/**",
                    "/reservations/cancelHotel/**",
                    "/nexus/flights/deactivateTicket/**",
                    "/nexus/flights/deactivate/**",
                    "/nexus/reservations/cancel/**",
                    "/nexus/reservations/cancelHotel/**"
                ).permitAll()

                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}