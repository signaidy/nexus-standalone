package SpectraSystems.Nexus.controllers;

import SpectraSystems.Nexus.models.User;
import SpectraSystems.Nexus.services.ReservationService;
import SpectraSystems.Nexus.services.UserService;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationControllerPrivateTest {

    @Mock ReservationService reservationService;
    @Mock RestTemplate restTemplate;
    @Mock UserService userService;
    @Mock JavaMailSender emailSender;

    private ReservationController controller;

    @BeforeEach
    void setUp() {
        // Manually construct controller with mocked constructor deps
        controller = new ReservationController(reservationService, restTemplate, userService);
        // Inject @Autowired field
        ReflectionTestUtils.setField(controller, "emailSender", emailSender);
    }

    private static Object invoke(Object target, String method, Class<?>[] sig, Object... args) throws Exception {
        Method m = target.getClass().getDeclaredMethod(method, sig);
        m.setAccessible(true);
        return m.invoke(target, args);
    }

    @Test
    void sendPurchaseConfirmationEmail_happyPath_sendsEmail() throws Exception {
        when(userService.getUserById(7L))
                .thenReturn(Optional.of(User.builder().id(7L).email("u@example.com").build()));
        when(emailSender.createMimeMessage())
                .thenReturn(new MimeMessage((Session) null));

        invoke(controller, "sendPurchaseConfirmationEmail", new Class<?>[]{Long.class}, 7L);

        verify(emailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendPurchaseConfirmationEmail_userNotFound_throws_andDoesNotSend() throws Exception {
        when(userService.getUserById(99L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            try {
                invoke(controller, "sendPurchaseConfirmationEmail", new Class<?>[]{Long.class}, 99L);
            } catch (InvocationTargetException ite) {
                Throwable cause = ite.getCause();
                if (cause instanceof RuntimeException re) throw re;
                throw new RuntimeException(cause);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        assertTrue(thrown.getMessage().contains("User with id 99"));

        verify(emailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void constructResponse_maps_id_name_rooms() throws Exception {
        Map<String,Object> hotel = new HashMap<>();
        hotel.put("_id", "H-123");
        hotel.put("name", "Hotel Azul");
        hotel.put("rooms", java.util.List.of(Map.of("type", "KING")));

        Map<String,Object> out = (Map<String,Object>) invoke(
                controller, "constructResponse", new Class<?>[]{Map.class}, hotel);

        assertEquals("H-123", out.get("hotelId"));
        assertEquals("Hotel Azul", out.get("name"));
        assertNotNull(out.get("rooms"));
    }
}