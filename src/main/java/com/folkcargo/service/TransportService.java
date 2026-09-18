package com.folkcargo.service;

import com.folkcargo.data.DataStore;
import com.folkcargo.model.TransportAssignment;
import com.folkcargo.model.TransportAssignment.TransportStatus;

import java.time.LocalDate;

public class TransportService extends AbstractCrudService<TransportAssignment> {

    private final DataStore store;

    public TransportService(DataStore store) {
        super(store.getTransportAssignments(), TransportAssignment::getId);
        this.store = store;
    }

    public TransportAssignment create(int shipmentId, int vehicleId, int driverId, String route,
                                       double distanceKm, LocalDate departureDate, LocalDate arrivalDate,
                                       TransportStatus status) {
        TransportAssignment t = new TransportAssignment(store.nextTransportId(), shipmentId, vehicleId,
                driverId, route, distanceKm, departureDate, arrivalDate, status);
        add(t);
        return t;
    }

    public boolean hasAssignment(int shipmentId) {
        return getAll().stream().anyMatch(t -> t.getShipmentId() == shipmentId);
    }
}
