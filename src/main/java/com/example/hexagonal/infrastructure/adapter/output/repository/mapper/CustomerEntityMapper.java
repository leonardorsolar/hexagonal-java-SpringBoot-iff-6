package com.example.hexagonal.infrastructure.adapter.output.repository.mapper;

import com.example.hexagonal.domain.Customer;
import com.example.hexagonal.infrastructure.adapter.output.repository.entity.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerEntityMapper {

    // eu recebo um Customer e retorno um CustomerEntity
    CustomerEntity toCustomerEntity(Customer customer);

    // Customer toCustomer(CustomerEntity customerEntity);

}
