package com.folkcargo.ui;

import com.folkcargo.model.TransportAssignment;
import com.folkcargo.service.ServiceRegistry;
import com.folkcargo.ui.dialogs.TransportDialog;
import com.folkcargo.ui.util.UIUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

import java.util.List;

public class TransportView extends CrudView<TransportAssignment> {

    private final ServiceRegistry services;

    public TransportView(ServiceRegistry services) {
        super(services.transport().getAll());
        this.services = services;

        TableColumn<TransportAssignment, String> shipmentCol = UIUtils.column("Shipment", 110, c -> new SimpleStringProperty(services.shipments().codeOf(c.getValue().getShipmentId())));
        TableColumn<TransportAssignment, String> routeCol = UIUtils.column("Route", "route", 220);
        TableColumn<TransportAssignment, String> vehicleCol = UIUtils.column("Vehicle", 160, c -> new SimpleStringProperty(services.vehicles().labelOf(c.getValue().getVehicleId())));
        TableColumn<TransportAssignment, String> driverCol = UIUtils.column("Driver", 140, c -> new SimpleStringProperty(services.drivers().nameOf(c.getValue().getDriverId())));
        TableColumn<TransportAssignment, String> distanceCol = UIUtils.column("Distance (km)", 100, c -> new SimpleStringProperty(UIUtils.formatNumber(c.getValue().getDistanceKm())));
        TableColumn<TransportAssignment, String> departureCol = UIUtils.column("Departure", 100, c -> new SimpleStringProperty(UIUtils.formatDate(c.getValue().getDepartureDate())));
        TableColumn<TransportAssignment, String> arrivalCol = UIUtils.column("Arrival", 100, c -> new SimpleStringProperty(UIUtils.formatDate(c.getValue().getArrivalDate())));
        TableColumn<TransportAssignment, String> statusCol = UIUtils.column("Status", 110, c -> new SimpleStringProperty(c.getValue().getStatus().toString()));

        setColumns(List.of(shipmentCol, routeCol, vehicleCol, driverCol, distanceCol, departureCol, arrivalCol, statusCol));
    }

    @Override
    protected String getTitle() {
        return "Transport";
    }

    @Override
    protected String getSubtitle() {
        return "Vehicle and driver assignments moving each shipment from origin to destination";
    }

    @Override
    protected boolean matchesSearch(TransportAssignment t, String q) {
        return contains(t.getRoute(), q)
                || contains(services.shipments().codeOf(t.getShipmentId()), q)
                || contains(services.vehicles().labelOf(t.getVehicleId()), q)
                || contains(services.drivers().nameOf(t.getDriverId()), q);
    }

    private boolean contains(String value, String q) {
        return value != null && value.toLowerCase().contains(q);
    }

    @Override
    protected void handleAdd() {
        TransportDialog.showAdd(services.transport(), services.shipments().getAll(), services.vehicles().getAll(), services.drivers().getAll());
    }

    @Override
    protected void handleEdit(TransportAssignment item) {
        if (TransportDialog.showEdit(item, services.shipments().getAll(), services.vehicles().getAll(), services.drivers().getAll())) {
            services.transport().update(item);
        }
    }

    @Override
    protected void handleDelete(TransportAssignment item) {
        services.transport().delete(item);
    }
}
