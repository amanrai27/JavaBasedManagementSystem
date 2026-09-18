package com.folkcargo.service;

import com.folkcargo.data.DataStore;
import com.folkcargo.model.Shipment;
import com.folkcargo.model.Shipment.ShipmentStatus;
import com.folkcargo.model.Shipment.ShipmentType;

import java.time.LocalDate;

public class ShipmentService extends AbstractCrudService<Shipment> {

    private final DataStore store;

    public ShipmentService(DataStore store) {
        super(store.getShipments(), Shipment::getId);
        this.store = store;
    }

    public Shipment create(int customerId, String origin, String destination, ShipmentType type,
                            String cargoDescription, double weightKg, ShipmentStatus status,
                            LocalDate expectedDeliveryDate) {
        int id = store.nextShipmentId();
        String code = String.format("FCS-SHP-%04d", id);
        Shipment s = new Shipment(id, code, customerId, origin, destination, type, cargoDescription,
                weightKg, status, LocalDate.now(), expectedDeliveryDate, null);
        add(s);
        return s;
    }

    public String codeOf(int shipmentId) {
        return findById(shipmentId).map(Shipment::getShipmentCode).orElse("Unknown Shipment #" + shipmentId);
    }

    public long countByStatus(ShipmentStatus status) {
        return getAll().stream().filter(s -> s.getStatus() == status).count();
    }

    public long countByType(ShipmentType type) {
        return getAll().stream().filter(s -> s.getShipmentType() == type).count();
    }
}
