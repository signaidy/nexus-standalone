package SpectraSystems.Nexus.controllers;

import SpectraSystems.Nexus.models.User;
import SpectraSystems.Nexus.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerUnitTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController controller;

    @BeforeEach
    void setUp() {
        // nothing special; controller is created with mocked service
    }

    @Test
    void getAllUsers_ok() {
        when(userService.getAllUsers()).thenReturn(List.of(new User(), new User()));

        ResponseEntity<List<User>> resp = controller.getAllUsers();

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(2, resp.getBody().size());
        verify(userService).getAllUsers();
    }

    @Test
    void getUserById_found_and_notFound() {
        when(userService.getUserById(42L)).thenReturn(Optional.of(new User()));
        when(userService.getUserById(99L)).thenReturn(Optional.empty());

        ResponseEntity<User> ok = controller.getUserById(42L);
        ResponseEntity<User> notFound = controller.getUserById(99L);

        assertEquals(HttpStatus.OK, ok.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
    }

    @Test
    void createUser_created() {
        when(userService.createUser(any(User.class))).thenReturn(new User());

        ResponseEntity<User> resp = controller.createUser(new User());

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        verify(userService).createUser(any(User.class));
    }

    @Test
    void updateUser_ok() {
        when(userService.updateUser(eq(7L), any(User.class))).thenReturn(new User());

        ResponseEntity<User> resp = controller.updateUser(7L, new User());

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        verify(userService).updateUser(eq(7L), any(User.class));
    }

    @Test
    void deleteUser_noContent() {
        doNothing().when(userService).deleteUser(5L);

        ResponseEntity<Void> resp = controller.deleteUser(5L);

        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
        verify(userService).deleteUser(5L);
    }
}