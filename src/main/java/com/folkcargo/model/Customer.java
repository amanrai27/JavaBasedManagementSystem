package com.folkcargo.model;

import java.time.LocalDate;

/**
 * Represents a client of Folk Cargo Solution Pvt. Ltd. — either an individual
 * or a registered business — who books shipments with the company.
 */
public class Customer {

    public enum CustomerType {
        INDIVIDUAL("Individual"),
        BUSINESS("Business");

        private final String label;

        CustomerType(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private int id;
    private String name;
    private String companyName;
    private CustomerType customerType;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String country;
    private LocalDate registeredDate;

    public Customer() {
    }

    public Customer(int id, String name, String companyName, CustomerType customerType,
                     String email, String phone, String address, String city, String country,
                     LocalDate registeredDate) {
        this.id = id;
        this.name = name;
        this.companyName = companyName;
        this.customerType = customerType;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.country = country;
        this.registeredDate = registeredDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(LocalDate registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getDisplayName() {
        return (companyName != null && !companyName.isBlank()) ? companyName + " (" + name + ")" : name;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
