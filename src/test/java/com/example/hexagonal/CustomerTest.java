package com.example.hexagonal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.example.hexagonal.domain.Customer;

public class CustomerTest {
    @Test
    void testCustomerDefaultConstructor() {
        Customer customer = new Customer();

        System.out.println("CPF válido? " + customer.getIsValidCpf());
        System.out.println("ID: " + customer.getId());

        assertFalse(customer.getIsValidCpf());
        assertNull(customer.getId());
        assertNull(customer.getName());
        assertNull(customer.getAddress());
        assertNull(customer.getCpf());
    }

    // @Test
    // void testCustomerFullConstructorAndGetters() {
    // Address mockAddress = new Address("localhost", 5672); // Mock de Address
    // Customer customer = new Customer("123", "Leonardo", mockAddress,
    // "11122233344", true);

    // assertEquals("123", customer.getId());
    // assertEquals("Leonardo", customer.getName());
    // assertEquals(mockAddress, customer.getAddress());
    // assertEquals("11122233344", customer.getCpf());
    // assertTrue(customer.getIsValidCpf());
    // }

    // @Test
    // void testSetters() {
    // Customer customer = new Customer();
    // Address mockAddress = new Address("127.0.0.1", 5672);

    // customer.setId("999");
    // customer.setName("Maria");
    // customer.setAddress(mockAddress);
    // customer.setCpf("00011122233");
    // customer.setIsValidCpf(true);

    // assertEquals("999", customer.getId());
    // assertEquals("Maria", customer.getName());
    // assertEquals(mockAddress, customer.getAddress());
    // assertEquals("00011122233", customer.getCpf());
    // assertTrue(customer.getIsValidCpf());
    // }
}
