package com.example.hexagonal.application.port.output;

import com.example.hexagonal.domain.Address;

public interface AddressLookupOutputPort {

    Address findByZipCode(String zipcode);

}
