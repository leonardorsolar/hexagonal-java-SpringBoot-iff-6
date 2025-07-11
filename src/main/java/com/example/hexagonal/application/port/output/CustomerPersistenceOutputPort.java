package com.example.hexagonal.application.port.output;

import com.example.hexagonal.domain.Customer;

public interface CustomerPersistenceOutputPort {

    void save(Customer customer);

}
