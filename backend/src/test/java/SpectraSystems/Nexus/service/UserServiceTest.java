package SpectraSystems.Nexus.service;

import SpectraSystems.Nexus.exceptions.ResourceNotFoundException;
import SpectraSystems.Nexus.models.Role;
import SpectraSystems.Nexus.models.User;
import SpectraSystems.Nexus.repositories.UserRepository;
import SpectraSystems.Nexus.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks UserService service;

    // ---------- read paths ----------
    @Test
    void getAllUsers_returns_list_from_repo() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));
        assertEquals(2, service.getAllUsers().size());
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_present_and_empty() {
        User u = new User();
        u.setId(7L);
        when(userRepository.findById(7L)).thenReturn(Optional.of(u));
        when(userRepository.findById(9L)).thenReturn(Optional.empty());

        assertTrue(service.getUserById(7L).isPresent());
        assertTrue(service.getUserById(9L).isEmpty());
    }

    // ---------- create with hashing ----------
    @Test
    void createUser_hashes_password_and_saves() {
        User input = User.builder()
                .email("a@b.com")
                .password("raw")
                .role(Role.ROLE_USER)
                .build();

        when(passwordEncoder.encode("raw")).thenReturn("ENC");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = service.createUser(input);

        assertEquals("ENC", saved.getPassword());
        verify(passwordEncoder).encode("raw");

        ArgumentCaptor<User> cap = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(cap.capture());
        assertEquals("ENC", cap.getValue().getPassword());
        assertEquals("a@b.com", cap.getValue().getEmail());
    }

    // ---------- update ----------
    @Test
    void updateUser_happyPath_updates_fields_and_saves() {
        User existing = User.builder()
                .id(5L)
                .email("old@example.com")
                .role(Role.ROLE_USER)
                .build();
        existing.setFirstName("Old");
        existing.setLastName("Name");
        existing.setAge(10);
        existing.setCountry("GT");
        existing.setPassport("P1");
        existing.setPercentage(1);

        User details = User.builder()
                .role(Role.ROLE_ADMIN)
                .build();
        details.setFirstName("New");
        details.setLastName("Last");
        details.setAge(20);
        details.setCountry("US");
        details.setPassport("P2");
        details.setPercentage(50);

        when(userRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User out = service.updateUser(5L, details);

        assertEquals("New", out.getFirstName());
        assertEquals("Last", out.getLastName());
        assertEquals(20, out.getAge());
        assertEquals("US", out.getCountry());
        assertEquals("P2", out.getPassport());
        assertEquals(50, out.getPercentage());
        assertEquals(Role.ROLE_ADMIN, out.getRole());

        verify(userRepository).save(existing);
    }

    @Test
    void updateUser_notFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.updateUser(99L, new User()));
        verify(userRepository, never()).save(any());
    }

    // ---------- delete ----------
    @Test
    void deleteUser_calls_repo_deleteById() {
        doNothing().when(userRepository).deleteById(3L);
        service.deleteUser(3L);
        verify(userRepository).deleteById(3L);
    }

    // ---------- userDetailsService ----------
    @Test
    void userDetailsService_loadByUsername_found_returns_domain_user() {
        User u = User.builder().id(1L).email("x@y.com").password("pw").role(Role.ROLE_USER).build();
        when(userRepository.findByEmail("x@y.com")).thenReturn(Optional.of(u));

        UserDetails out = service.userDetailsService().loadUserByUsername("x@y.com");
        assertSame(u, out); // our User implements UserDetails
        assertEquals("x@y.com", out.getUsername());
    }

    @Test
    void userDetailsService_loadByUsername_missing_throws() {
        when(userRepository.findByEmail("nope")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> service.userDetailsService().loadUserByUsername("nope"));
    }

    // ---------- save(new/existing) timestamps ----------
    @Test
    void save_newUser_sets_createdAt_and_updatedAt() {
        User u = new User(); // id == null
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User out = service.save(u);

        assertNotNull(out.getCreatedAt());
        assertNotNull(out.getUpdatedAt());
        verify(userRepository).save(out);
    }

    @Test
    void save_existing_preserves_createdAt_and_updates_updatedAt() {
        User u = new User();
        u.setId(10L);
        LocalDateTime originalCreated = LocalDateTime.of(2024,1,1,0,0);
        u.setCreatedAt(originalCreated);
        u.setUpdatedAt(LocalDateTime.of(2024,1,1,1,0));

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User out = service.save(u);

        assertEquals(originalCreated, out.getCreatedAt(), "createdAt should be preserved for existing user");
        assertNotNull(out.getUpdatedAt(), "updatedAt should be set");
        verify(userRepository).save(out);
    }
}