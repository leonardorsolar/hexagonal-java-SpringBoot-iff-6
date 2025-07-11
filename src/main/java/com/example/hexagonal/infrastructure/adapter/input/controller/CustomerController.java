package com.example.hexagonal.infrastructure.adapter.input.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hexagonal.application.port.input.CreateCustomerInputPort;
import com.example.hexagonal.infrastructure.adapter.input.controller.mapper.CustomerMapper;
import com.example.hexagonal.infrastructure.adapter.input.controller.request.CustomerRequestDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CreateCustomerInputPort createCustomerInputPort;

    @Autowired
    private CustomerMapper customerMapper;

    @PostMapping
    public ResponseEntity<Void> createCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        var customer = customerMapper.toCustomer(customerRequestDTO);
        createCustomerInputPort.create(customer, customerRequestDTO.getZipCode());
        return ResponseEntity.ok().build();
    }
}
