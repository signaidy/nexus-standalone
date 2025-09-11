package SpectraSystems.Nexus.services;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static String b64Key(byte[] raw) {
        return Base64.getEncoder().encodeToString(raw);
    }

    @Test
    void generate_extract_validate_happyPath_and_mismatchedUser() {
        // Arrange
        JwtService svc = new JwtService();

        // 32 bytes -> 256-bit key (HS256). Base64-encoded because JwtService decodes it.
        byte[] rawKey = "01234567890123456789012345678901".getBytes(StandardCharsets.UTF_8);
        svc.jwtSecretKey = b64Key(rawKey);
        svc.jwtExpirationMs = 3_600_000L; // 1 hour

        UserDetails alice = User.withUsername("alice").password("x").roles("USER").build();
        UserDetails bob   = User.withUsername("bob").password("x").roles("USER").build();

        // Act
        String token = svc.generateToken(alice);

        // Assert
        assertNotNull(token, "token should be generated");
        assertEquals("alice", svc.extractUserName(token), "subject should round-trip");
        assertTrue(svc.isTokenValid(token, alice), "token valid for correct user");
        assertFalse(svc.isTokenValid(token, bob), "token invalid for different user");
    }

    @Test
    void expired_token_isDetected() throws InterruptedException {
        // Arrange
        JwtService svc = new JwtService();
        byte[] rawKey = "01234567890123456789012345678901".getBytes(StandardCharsets.UTF_8);
        svc.jwtSecretKey = b64Key(rawKey);
        svc.jwtExpirationMs = 1L; // expire essentially immediately

        UserDetails user = User.withUsername("alice").password("x").roles("USER").build();

        // Act
        String token = svc.generateToken(user);
        Thread.sleep(5); // let it expire

        // Assert
        assertFalse(svc.isTokenValid(token, user), "expired token must be invalid");
    }

    @Test
    void tampered_token_isInvalid() {
    JwtService svc = new JwtService();
    byte[] rawKey = "01234567890123456789012345678901".getBytes(StandardCharsets.UTF_8);
    svc.jwtSecretKey = Base64.getEncoder().encodeToString(rawKey);
    svc.jwtExpirationMs = 3_600_000L;

    UserDetails user = User.withUsername("alice").password("x").roles("USER").build();
    String token = svc.generateToken(user);

    // Tamper the token so signature no longer matches
    String bad = token + "x";

    assertFalse(svc.isTokenValid(bad, user), "tampered token must be invalid");
    }

    @Test
    void wrong_signing_key_isInvalid() {
    JwtService issuer = new JwtService();
    issuer.jwtSecretKey = Base64.getEncoder().encodeToString("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".getBytes(StandardCharsets.UTF_8));
    issuer.jwtExpirationMs = 3_600_000L;

    JwtService verifier = new JwtService();
    verifier.jwtSecretKey = Base64.getEncoder().encodeToString("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb".getBytes(StandardCharsets.UTF_8));
    verifier.jwtExpirationMs = 3_600_000L;

    UserDetails user = User.withUsername("alice").password("x").roles("USER").build();
    String token = issuer.generateToken(user);

    assertFalse(verifier.isTokenValid(token, user), "token signed with a different key must be invalid");
    }
}