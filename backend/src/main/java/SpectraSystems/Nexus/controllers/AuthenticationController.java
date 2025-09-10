package SpectraSystems.Nexus.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import SpectraSystems.Nexus.dto.JwtAuthenticationResponse;
import SpectraSystems.Nexus.dto.SignInRequest;
import SpectraSystems.Nexus.dto.SignUpRequest;
import SpectraSystems.Nexus.services.AuthenticationService;

@RestController
@RequestMapping("/nexus/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    
    /** 
     * @param request
     * @return the 'JwtAuthenticationResponse'
     */
    @PostMapping("/signup")
    public JwtAuthenticationResponse signup(@RequestBody SignUpRequest request) {
        return authenticationService.signup(request);
    }

    
    /** 
     * @param request
     * @return the'JwtAuthenticationResponse'
     */
    @PostMapping("/login")
    public JwtAuthenticationResponse signin(@RequestBody SignInRequest request) {
        return authenticationService.signin(request);
    }
}