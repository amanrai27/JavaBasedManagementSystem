package com.folkcargo.service;

import com.folkcargo.data.DataStore;
import com.folkcargo.model.Vehicle;
import com.folkcargo.model.Vehicle.VehicleStatus;
import com.folkcargo.model.Vehicle.VehicleType;

public class VehicleService extends AbstractCrudService<Vehicle> {

    private final DataStore store;

    public VehicleService(DataStore store) {
        super(store.getVehicles(), Vehicle::getId);
        this.store = store;
    }

    public Vehicle create(String vehicleNumber, VehicleType type, double capacityTons, VehicleStatus status,
                           Integer assignedDriverId) {
        Vehicle v = new Vehicle(store.nextVehicleId(), vehicleNumber, type, capacityTons, status, assignedDriverId);
        add(v);
        return v;
    }

    public String labelOf(int vehicleId) {
        return findById(vehicleId).map(Vehicle::toString).orElse("Unknown Vehicle #" + vehicleId);
    }
}
