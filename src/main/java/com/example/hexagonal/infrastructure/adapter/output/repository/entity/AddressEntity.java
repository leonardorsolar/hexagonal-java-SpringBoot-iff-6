package com.example.hexagonal.infrastructure.adapter.output.repository.entity;

import lombok.Data;

@Data
public class AddressEntity {
    private String street;
    private String city;
    private String state;
}
