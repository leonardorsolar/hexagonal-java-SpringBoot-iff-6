package com.example.hexagonal.application.usecase;

import com.example.hexagonal.application.port.output.AddressLookupOutputPort;
import com.example.hexagonal.application.port.output.CustomerPersistenceOutputPort;
import com.example.hexagonal.domain.Customer;

public class CreateCustomerUseCase {

    private final AddressLookupOutputPort addressLookupOutputPort;
    private final CustomerPersistenceOutputPort customerPersistenceOutputPort;

    public CreateCustomerUseCase(AddressLookupOutputPort addressLookupOutputPort,
            CustomerPersistenceOutputPort customerPersistenceOutputPort) {
        this.addressLookupOutputPort = addressLookupOutputPort;
        this.customerPersistenceOutputPort = customerPersistenceOutputPort;
    }

    // this.cpfValidationMessagePort = cpfValidationMessagePort;

    public void create(Customer customer, String zipCode) {
        var address = addressLookupOutputPort.findByZipCode(zipCode);
        customer.setAddress(address);
        customerPersistenceOutputPort.save(customer);
        // cpfValidationMessagePort.sendCpfForValidation(customer.getCpf());
    }

}
