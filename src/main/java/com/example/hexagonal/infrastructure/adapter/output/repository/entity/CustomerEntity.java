package com.example.hexagonal.infrastructure.adapter.output.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "customers") // Define o nome da coleção no MongoDB
public class CustomerEntity {

    @Id
    private String id;

    private String name;

    private AddressEntity address;

    private String cpf;

    private Boolean isValidCpf;
}