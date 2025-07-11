package com.example.hexagonal.infrastructure.adapter.input.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.hexagonal.domain.Customer;
import com.example.hexagonal.infrastructure.adapter.input.controller.request.CustomerRequestDTO;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "isValidCpf", ignore = true)

    Customer toCustomer(CustomerRequestDTO customerRequest);

}
