package com.example.hexagonal.application.port.input;

import com.example.hexagonal.domain.Customer;

public interface CreateCustomerInputPort {

    void create(Customer customer, String zipCode);

}
