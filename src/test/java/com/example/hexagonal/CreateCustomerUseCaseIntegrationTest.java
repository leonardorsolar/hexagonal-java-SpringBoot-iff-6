package com.example.hexagonal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.example.hexagonal.application.port.output.AddressLookupOutputPort;
import com.example.hexagonal.application.port.output.CustomerPersistenceOutputPort;
import com.example.hexagonal.application.usecase.CreateCustomerUseCase;
import com.example.hexagonal.domain.Address;
import com.example.hexagonal.domain.Customer;

public class CreateCustomerUseCaseIntegrationTest {

    @Test
    void deveCriarClienteComEndereco() {
        // Implementações simples das portas
        // CustomerPersistenceOutputPort persistence = new
        // InMemoryCustomerPersistencePort();
        InMemoryCustomerPersistencePort persistence = new InMemoryCustomerPersistencePort();
        AddressLookupOutputPort addressService = new FakeAddressLookupOutputPort();
        CreateCustomerUseCase useCase = new CreateCustomerUseCase(addressService, persistence);

        Customer customer = new Customer();
        customer.setName("Maria");
        customer.setCpf("12345678900");

        useCase.create(customer, "12345678");

        System.out.println("Name " + persistence.getSavedCustomer().getName());
        System.out.println("Rua " + persistence.getSavedCustomer().getAddress().getStreet());

        assertNotNull(persistence.getSavedCustomer());
        assertEquals("Maria", persistence.getSavedCustomer().getName());
        assertEquals("Rua Teste", persistence.getSavedCustomer().getAddress().getStreet());
    }

    // Implementação em memória para teste
    static class InMemoryCustomerPersistencePort implements CustomerPersistenceOutputPort {
        private Customer savedCustomer;

        @Override
        public void save(Customer customer) {
            this.savedCustomer = customer;
        }

        public Customer getSavedCustomer() {
            return savedCustomer;
        }
    }

    // Implementação fake para teste
    static class FakeAddressLookupOutputPort implements AddressLookupOutputPort {
        @Override
        public Address findByZipCode(String zipcode) {
            return new Address("Rua Teste", "Cidade Teste", "Estado Teste");
        }
    }
}