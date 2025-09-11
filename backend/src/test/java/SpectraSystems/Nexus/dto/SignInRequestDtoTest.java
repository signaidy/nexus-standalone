package SpectraSystems.Nexus.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignInRequestDtoTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void builder_getters_equals_hashCode() {
        SignInRequest a = SignInRequest.builder()
                .email("user@example.com")
                .password("pw")
                .build();

        assertEquals("user@example.com", a.getEmail());
        assertEquals("pw", a.getPassword());

        SignInRequest b = new SignInRequest();
        b.setEmail("user@example.com");
        b.setPassword("pw");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertTrue(a.toString().contains("user@example.com"));
    }

    @Test
    void jackson_roundTrip() throws Exception {
        SignInRequest req = SignInRequest.builder()
                .email("e@x.com")
                .password("p")
                .build();

        String json = mapper.writeValueAsString(req);
        SignInRequest back = mapper.readValue(json, SignInRequest.class);

        assertEquals(req, back);
    }
}