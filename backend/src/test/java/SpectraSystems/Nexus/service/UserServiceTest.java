package SpectraSystems.Nexus.service;

import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import SpectraSystems.Nexus.models.Role;
import SpectraSystems.Nexus.models.User;
import SpectraSystems.Nexus.repositories.UserRepository;
import SpectraSystems.Nexus.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    public void userService_CreateUser_ReturnsUser(){
        User user = User.builder().first_Name("John").last_Name("Doe").email("john@doe.com").age(8).password("1234567890").country("Spain").passport("1234567890").role(Role.ROLE_USER).build();
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        User savedUser = userService.createUser(user);
        Assertions.assertThat(savedUser).isNotNull();
    }
}
