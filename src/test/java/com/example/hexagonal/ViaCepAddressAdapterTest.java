package com.example.hexagonal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.example.hexagonal.domain.Address;
import com.example.hexagonal.infrastructure.adapter.output.client.ViaCepAddressAdapter;

public class ViaCepAddressAdapterTest {

    @Test
    void deveBuscarEnderecoRealDaApiViaCep() {
        // Arrange
        ViaCepAddressAdapter adapter = new ViaCepAddressAdapter();

        // Act
        Address address = adapter.findByZipCode("28300000"); // CEP válido da Praça da Sé - SP

        System.out.println("Estado: " + address.getCity());
        System.out.println("UF: " + address.getState());

        // Assert
        assertNotNull(address);
        assertEquals("Itaperuna", address.getCity());
        assertEquals("RJ", address.getState());
        // logradouro pode variar, então não fixamos aqui
    }
}
