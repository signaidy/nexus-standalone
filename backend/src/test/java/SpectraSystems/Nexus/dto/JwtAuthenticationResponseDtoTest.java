package SpectraSystems.Nexus.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import SpectraSystems.Nexus.models.User;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationResponseDtoTest {

    // Use field visibility so Jackson doesn't call entity getters (which may NPE)
    private final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    @Test
    void builder_getters_equals_hashCode() {
        User u = User.builder().id(7L).email("u@example.com").build();

        JwtAuthenticationResponse a = JwtAuthenticationResponse.builder()
                .token("abc.def.ghi")
                .user(u)
                .build();

        assertEquals("abc.def.ghi", a.getToken());
        assertEquals(7L, a.getUser().getId());

        JwtAuthenticationResponse b = new JwtAuthenticationResponse();
        b.setToken("abc.def.ghi");
        b.setUser(User.builder().id(7L).email("u@example.com").build());

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertTrue(a.toString().contains("abc.def.ghi"));
    }

    @Test
    void jackson_serializes_with_nestedUser_fields() throws Exception {
        JwtAuthenticationResponse resp = JwtAuthenticationResponse.builder()
                .token("tkn")
                .user(User.builder().id(1L).email("x@y.com").build())
                .build();

        String json = mapper.writeValueAsString(resp);
        assertTrue(json.contains("\"token\":\"tkn\""));
        assertTrue(json.contains("\"email\":\"x@y.com\""));

        JsonNode root = mapper.readTree(json);
        assertEquals("tkn", root.path("token").asText());

        JsonNode user = root.path("user");
        assertEquals("x@y.com", user.path("email").asText());
        assertEquals(1L, user.path("id").asLong());
    }
}