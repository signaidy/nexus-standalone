package SpectraSystems.Nexus.models;

import jakarta.persistence.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class ProviderModelTest {

    @Test
    void builder_setsAllFields() {
        Provider p = Provider.builder()
                .id(10L)
                .providerName("Avianca")
                .providerUrl("https://api.example.com")
                .type(Type.AEROLINEA)
                .gainsFlights(0.15)
                .gainsHotel(0.10)
                .percentageDiscount(0.05)
                .build();

        assertEquals(10L, p.getId());
        assertEquals("Avianca", p.getProviderName());
        assertEquals("https://api.example.com", p.getProviderUrl());
        assertEquals(Type.AEROLINEA, p.getType());
        assertEquals(0.15, p.getGainsFlights());
        assertEquals(0.10, p.getGainsHotel());
        assertEquals(0.05, p.getPercentageDiscount());
    }

    @Test
    void constructors_mapFieldsCorrectly() {
        Provider a = new Provider(5L, "ProvA", "http://a", Type.HOTEL, 1.1, 2.2, 0.3);
        assertEquals(5L, a.getId());
        assertEquals("ProvA", a.getProviderName());
        assertEquals("http://a", a.getProviderUrl());
        assertEquals(Type.HOTEL, a.getType());
        assertEquals(1.1, a.getGainsFlights());
        assertEquals(2.2, a.getGainsHotel());
        assertEquals(0.3, a.getPercentageDiscount());

        // Uses the second ctor (note parameter order!)
        Provider b = new Provider("ProvB", "http://b", 3.3, Type.AEROLINEA, 4.4, 0.5);
        assertNull(b.getId());
        assertEquals("ProvB", b.getProviderName());
        assertEquals("http://b", b.getProviderUrl());
        assertEquals(Type.AEROLINEA, b.getType());
        assertEquals(3.3, b.getGainsFlights());
        assertEquals(4.4, b.getGainsHotel());
        assertEquals(0.5, b.getPercentageDiscount());
    }

    @Test
    void numericGetters_returnZeroWhenNull() {
        Provider p = new Provider(); // all null
        assertEquals(0.0, p.getGainsFlights());
        assertEquals(0.0, p.getGainsHotel());
        assertEquals(0.0, p.getPercentageDiscount());
    }

    @Test
    void setters_updateValues() {
        Provider p = new Provider();
        p.setId(1L);
        p.setProviderName("Name");
        p.setProviderUrl("http://url");
        p.setType(Type.HOTEL);
        p.setGainsFlights(7.7);
        p.setGainsHotel(8.8);
        p.setPercentageDiscount(0.9);

        assertEquals(1L, p.getId());
        assertEquals("Name", p.getProviderName());
        assertEquals("http://url", p.getProviderUrl());
        assertEquals(Type.HOTEL, p.getType());
        assertEquals(7.7, p.getGainsFlights());
        assertEquals(8.8, p.getGainsHotel());
        assertEquals(0.9, p.getPercentageDiscount());
    }

    @Test
    void jpaAnnotations_present_andConfigured() throws Exception {
        // Class-level
        assertTrue(Provider.class.isAnnotationPresent(Entity.class));
        Table table = Provider.class.getAnnotation(Table.class);
        assertNotNull(table);
        assertEquals("PROVIDERS", table.name());

        // id field
        Field id = Provider.class.getDeclaredField("id");
        assertNotNull(id.getAnnotation(Id.class));
        GeneratedValue gv = id.getAnnotation(GeneratedValue.class);
        assertNotNull(gv);
        assertEquals(GenerationType.IDENTITY, gv.strategy());

        // Column constraints we care about
        Column nameCol = Provider.class.getDeclaredField("providerName").getAnnotation(Column.class);
        assertNotNull(nameCol);
        assertEquals("provider_name", nameCol.name());
        assertFalse(nameCol.nullable());

        Column urlCol = Provider.class.getDeclaredField("providerUrl").getAnnotation(Column.class);
        assertNotNull(urlCol);
        assertEquals("provider_url", urlCol.name());
        assertFalse(urlCol.nullable());

        Column typeCol = Provider.class.getDeclaredField("type").getAnnotation(Column.class);
        assertNotNull(typeCol);
        assertFalse(typeCol.nullable());
    }
}