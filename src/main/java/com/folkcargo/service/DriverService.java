package com.folkcargo.service;

import com.folkcargo.data.DataStore;
import com.folkcargo.model.Driver;
import com.folkcargo.model.Driver.DriverStatus;

public class DriverService extends AbstractCrudService<Driver> {

    private final DataStore store;

    public DriverService(DataStore store) {
        super(store.getDrivers(), Driver::getId);
        this.store = store;
    }

    public Driver create(String name, String licenseNumber, String phone, int experienceYears,
                          DriverStatus status) {
        Driver d = new Driver(store.nextDriverId(), name, licenseNumber, phone, experienceYears, status);
        add(d);
        return d;
    }

    public String nameOf(int driverId) {
        return findById(driverId).map(Driver::getName).orElse("Unassigned");
    }
}
