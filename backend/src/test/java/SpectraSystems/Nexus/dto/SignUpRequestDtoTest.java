package SpectraSystems.Nexus.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignUpRequestDtoTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void builder_getters_setters_equals_hashCode() {
        SignUpRequest a = SignUpRequest.builder()
                .first_Name("Ada")
                .last_Name("Lovelace")
                .email("ada@example.com")
                .password("s3cr3t")
                .age(28)
                .country("UK")
                .passport("P123")
                .build();

        // getters
        assertEquals("Ada", a.getFirst_Name());
        assertEquals("Lovelace", a.getLast_Name());
        assertEquals("ada@example.com", a.getEmail());
        assertEquals("s3cr3t", a.getPassword());
        assertEquals(28, a.getAge());
        assertEquals("UK", a.getCountry());
        assertEquals("P123", a.getPassport());

        // setters
        a.setCountry("GB");
        assertEquals("GB", a.getCountry());

        // equals/hashCode
        SignUpRequest b = SignUpRequest.builder()
                .first_Name("Ada")
                .last_Name("Lovelace")
                .email("ada@example.com")
                .password("s3cr3t")
                .age(28)
                .country("GB")
                .passport("P123")
                .build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertTrue(a.toString().contains("Ada"));
    }

    @Test
    void jackson_roundTrip_preserves_snakeCase_fields() throws Exception {
        SignUpRequest req = SignUpRequest.builder()
                .first_Name("Grace")
                .last_Name("Hopper")
                .email("grace@example.com")
                .password("pw")
                .age(35)
                .country("US")
                .passport("X1")
                .build();

        String json = mapper.writeValueAsString(req);
        // field names should be exactly as declared (including underscore)
        assertTrue(json.contains("\"first_Name\":\"Grace\""));
        assertTrue(json.contains("\"last_Name\":\"Hopper\""));

        SignUpRequest back = mapper.readValue(json, SignUpRequest.class);
        assertEquals(req, back);
    }
}