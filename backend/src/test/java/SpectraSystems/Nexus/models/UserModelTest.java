package SpectraSystems.Nexus.models;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    private Role anyRole() {
        Role[] vals = Role.values();
        assertTrue(vals.length > 0, "Role enum must have at least one value");
        return vals[0];
    }

    @Test
    void builder_and_basic_getters_setters_work() {
        Role role = anyRole();

        User u = User.builder()
                .id(7L)
                .email("u@example.com")
                .password("secret")
                .age(30)
                .country("GT")
                .passport("P12345")
                .percentage(15)
                .role(role)
                .createdAt(LocalDateTime.of(2024, 1, 1, 10, 0))
                .updatedAt(LocalDateTime.of(2024, 1, 2, 11, 0))
                .build();

        u.setFirstName("Ada");
        u.setLastName("Lovelace");

        assertEquals(7L, u.getId());
        assertEquals("Ada", u.getFirstName());
        assertEquals("Lovelace", u.getLastName());
        assertEquals("u@example.com", u.getEmail());
        assertEquals("secret", u.getPassword());
        assertEquals(30, u.getAge());
        assertEquals("GT", u.getCountry());
        assertEquals("P12345", u.getPassport());
        assertEquals(15, u.getPercentage());
        assertEquals(role, u.getRole());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), u.getCreatedAt());
        assertEquals(LocalDateTime.of(2024, 1, 2, 11, 0), u.getUpdatedAt());
    }

    @Test
    void userDetails_contract_is_respected() {
        Role role = anyRole();

        User u = User.builder()
                .id(1L)
                .email("alice@example.com")
                .password("pw")
                .role(role)
                .build();
        u.setFirstName("Alice");
        u.setLastName("Doe");

        assertEquals("alice@example.com", u.getUsername());

        Collection<? extends GrantedAuthority> auths = u.getAuthorities();
        assertEquals(1, auths.size());
        assertTrue(auths.stream().anyMatch(a -> role.name().equals(a.getAuthority())));

        assertTrue(u.isAccountNonExpired());
        assertTrue(u.isAccountNonLocked());
        assertTrue(u.isCredentialsNonExpired());
        assertTrue(u.isEnabled());
    }

    @Test
    void equals_hashCode_and_toString_have_basic_sanity() {
        Role role = anyRole();

        User a = User.builder()
                .id(10L)
                .email("same@example.com")
                .password("x")
                .age(20)
                .country("US")
                .passport("A1")
                .percentage(5)
                .role(role)
                .build();
        a.setFirstName("First");
        a.setLastName("Last");

        User b = User.builder()
                .id(10L)
                .email("same@example.com")
                .password("x")
                .age(20)
                .country("US")
                .passport("A1")
                .percentage(5)
                .role(role)
                .build();
        b.setFirstName("First");
        b.setLastName("Last");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        String s = a.toString();
        assertTrue(s.contains("same@example.com"));
        assertTrue(s.contains(role.name()));
    }

    @Test
    void noArgsConstructor_and_mutators_work() {
        User u = new User();
        u.setId(99L);
        u.setFirstName("Grace");
        u.setLastName("Hopper");
        u.setEmail("grace@example.com");
        u.setAge(85);
        u.setPassword("pwd");
        u.setCountry("US");
        u.setPassport("Z999");
        u.setPercentage(42);

        assertEquals(99L, u.getId());
        assertEquals("Grace", u.getFirstName());
        assertEquals("Hopper", u.getLastName());
        assertEquals("grace@example.com", u.getEmail());
        assertEquals(85, u.getAge());
        assertEquals("pwd", u.getPassword());
        assertEquals("US", u.getCountry());
        assertEquals("Z999", u.getPassport());
        assertEquals(42, u.getPercentage());
    }

    @Test
    void explicit_constructor_sets_all_fields() {
        Role role = anyRole(); // uses the helper in this test class

        User u = new User(
                "Ada",
                "Lovelace",
                "ada@example.com",
                36,
                "pw",
                "UK",
                "P-1",
                role
        );

        // id not set by ctor
        assertNull(u.getId());

        // fields from ctor
        assertEquals("Ada", u.getFirstName());
        assertEquals("Lovelace", u.getLastName());
        assertEquals("ada@example.com", u.getEmail());
        assertEquals(36, u.getAge());
        assertEquals("pw", u.getPassword());
        assertEquals("UK", u.getCountry());
        assertEquals("P-1", u.getPassport());
        assertEquals(role, u.getRole());

        // fields not touched by ctor remain null
        assertNull(u.getPercentage());
        assertNull(u.getCreatedAt());
        assertNull(u.getUpdatedAt());
    }
}