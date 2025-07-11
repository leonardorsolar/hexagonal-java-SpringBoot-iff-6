package com.example.hexagonal.infrastructure.adapter.output.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.hexagonal.application.port.output.AddressLookupOutputPort;
import com.example.hexagonal.domain.Address;

@Component
public class ViaCepAddressAdapter implements AddressLookupOutputPort {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Address findByZipCode(String zipcode) {
        String url = "https://viacep.com.br/ws/" + zipcode + "/json";
        ViaCepResponseDTO response = restTemplate.getForObject(url, ViaCepResponseDTO.class);

        return new Address(
                response.getLogradouro(), // street
                response.getLocalidade(), // city
                response.getUf() // state
        );
    }
}