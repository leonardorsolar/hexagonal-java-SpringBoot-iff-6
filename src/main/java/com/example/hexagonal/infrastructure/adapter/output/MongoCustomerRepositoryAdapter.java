package com.example.hexagonal.infrastructure.adapter.output;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.hexagonal.application.port.output.CustomerPersistenceOutputPort;
import com.example.hexagonal.domain.Customer;
import com.example.hexagonal.infrastructure.adapter.output.repository.MongoCustomerRepository;
import com.example.hexagonal.infrastructure.adapter.output.repository.entity.CustomerEntity;
import com.example.hexagonal.infrastructure.adapter.output.repository.mapper.CustomerEntityMapper;

// pensar em nomear MongoCreateCustomerRepositoryAdapter
@Component
public class MongoCustomerRepositoryAdapter implements CustomerPersistenceOutputPort {

    @Autowired
    private MongoCustomerRepository repository;

    @Autowired
    private CustomerEntityMapper mapper;

    @Override
    public void save(Customer customer) {
        CustomerEntity entity = mapper.toCustomerEntity(customer);
        repository.save(entity);
    }
}