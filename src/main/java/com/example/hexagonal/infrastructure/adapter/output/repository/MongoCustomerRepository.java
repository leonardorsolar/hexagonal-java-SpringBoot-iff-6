package com.example.hexagonal.infrastructure.adapter.output.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.hexagonal.infrastructure.adapter.output.repository.entity.CustomerEntity;

public interface MongoCustomerRepository extends MongoRepository<CustomerEntity, String> {

}