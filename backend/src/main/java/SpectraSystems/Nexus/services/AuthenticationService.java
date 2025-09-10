package SpectraSystems.Nexus.services;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import SpectraSystems.Nexus.repositories.UserRepository;
import SpectraSystems.Nexus.dto.JwtAuthenticationResponse;
import SpectraSystems.Nexus.dto.SignInRequest;
import SpectraSystems.Nexus.dto.SignUpRequest;
import SpectraSystems.Nexus.models.Role;
import SpectraSystems.Nexus.models.User;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  
  /** 
   * @param request
   * @return 'JwtAuthenticationResponse'
   */
  public JwtAuthenticationResponse signup(SignUpRequest request) {
      var user = User
                  .builder()
                  .first_Name(request.getFirst_Name())
                  .last_Name(request.getLast_Name())
                  .email(request.getEmail())
                  .password(passwordEncoder.encode(request.getPassword()))
                  .role(Role.ROLE_USER)
                  .age(request.getAge())
                  .country(request.getCountry())
                  .passport(request.getPassport())
                  .build();

      user = userService.save(user);
      var jwt = jwtService.generateToken(user);
      return JwtAuthenticationResponse.builder().token(jwt).build();
  }


  
  /** 
   * @param request
   * @return 'JwtAuthenticationResponse'
   */
  public JwtAuthenticationResponse signin(SignInRequest request) {
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
      var user = userRepository.findByEmail(request.getEmail())
              .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
      var jwt = jwtService.generateToken(user);
      return JwtAuthenticationResponse.builder().token(jwt).user(user).build();
  }
  
}
