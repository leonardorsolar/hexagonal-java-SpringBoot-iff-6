package com.example.hexagonal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.example.hexagonal.domain.Address;

public class AddressTest {
    @Test
    void testDefaultConstructor() {
        Address address = new Address();

        System.out.println("Classe Address " + address);
        System.out.println("Classe Address " + address.getStreet());

        assertNull(address.getStreet());
        assertNull(address.getCity());
        assertNull(address.getState());
    }

    @Test
    void testParameterizedConstructor() {
        Address address = new Address("Rua A", "Campos", "RJ");

        System.out.println("Classe Address " + address);
        System.out.println("Classe Address " + address.getStreet());

        assertEquals("Rua A", address.getStreet());
        assertEquals("Campos", address.getCity());
        assertEquals("RJ", address.getState());
    }

    @Test
    void testSettersAndGetters() {
        Address address = new Address();

        address.setStreet("Av. Brasil");
        address.setCity("Rio de Janeiro");
        address.setState("RJ");

        System.out.println("Classe Address " + address);
        System.out.println("Classe Address " + address.getStreet());

        assertEquals("Av. Brasil", address.getStreet());
        assertEquals("Rio de Janeiro", address.getCity());
        assertEquals("RJ", address.getState());
    }
}
