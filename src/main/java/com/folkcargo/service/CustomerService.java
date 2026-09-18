package com.folkcargo.service;

import com.folkcargo.data.DataStore;
import com.folkcargo.model.Customer;
import com.folkcargo.model.Customer.CustomerType;

import java.time.LocalDate;

public class CustomerService extends AbstractCrudService<Customer> {

    private final DataStore store;

    public CustomerService(DataStore store) {
        super(store.getCustomers(), Customer::getId);
        this.store = store;
    }

    public Customer create(String name, String companyName, CustomerType type, String email, String phone,
                            String address, String city, String country) {
        Customer c = new Customer(store.nextCustomerId(), name, companyName, type, email, phone, address,
                city, country, LocalDate.now());
        add(c);
        return c;
    }

    public String nameOf(int customerId) {
        return findById(customerId).map(Customer::getDisplayName).orElse("Unknown Customer #" + customerId);
    }
}
