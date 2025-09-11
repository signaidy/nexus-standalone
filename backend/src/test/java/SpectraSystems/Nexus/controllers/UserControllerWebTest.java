package SpectraSystems.Nexus.controllers;

import SpectraSystems.Nexus.models.User;
import SpectraSystems.Nexus.services.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = UserController.class,
    excludeAutoConfiguration = { SecurityAutoConfiguration.class }
)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerWebTest {

    @Autowired MockMvc mvc;

    @MockBean UserService userService; // only dependency the controller needs

    @Test
    void getAllUsers_ok() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(new User(), new User()));

        mvc.perform(get("/nexus/users").accept(APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));

        verify(userService).getAllUsers();
    }

    @Test
    void getUserById_found_and_notFound() throws Exception {
        when(userService.getUserById(42L)).thenReturn(Optional.of(new User()));
        when(userService.getUserById(99L)).thenReturn(Optional.empty());

        mvc.perform(get("/nexus/users/{id}", 42).accept(APPLICATION_JSON))
           .andExpect(status().isOk());

        mvc.perform(get("/nexus/users/{id}", 99).accept(APPLICATION_JSON))
           .andExpect(status().isNotFound());
    }

    @Test
    void createUser_created() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(new User());

        mvc.perform(post("/nexus/users")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content("{}"))
           .andExpect(status().isCreated());

        verify(userService).createUser(any(User.class));
    }

    @Test
    void updateUser_ok() throws Exception {
        when(userService.updateUser(eq(7L), any(User.class))).thenReturn(new User());

        mvc.perform(put("/nexus/users/{id}", 7L)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content("{\"email\":\"u@example.com\"}"))
           .andExpect(status().isOk());

        verify(userService).updateUser(eq(7L), any(User.class));
    }

    @Test
    void deleteUser_noContent() throws Exception {
        doNothing().when(userService).deleteUser(5L);

        mvc.perform(delete("/nexus/users/{id}", 5L))
           .andExpect(status().isNoContent());

        verify(userService).deleteUser(5L);
    }
}