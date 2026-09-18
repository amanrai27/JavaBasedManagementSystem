package com.folkcargo.ui;

import com.folkcargo.model.Vehicle;
import com.folkcargo.service.ServiceRegistry;
import com.folkcargo.ui.dialogs.VehicleDialog;
import com.folkcargo.ui.util.UIUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

import java.util.List;

public class VehicleView extends CrudView<Vehicle> {

    private final ServiceRegistry services;

    public VehicleView(ServiceRegistry services) {
        super(services.vehicles().getAll());
        this.services = services;

        TableColumn<Vehicle, Number> idCol = UIUtils.column("ID", 50, c -> new SimpleIntegerProperty(c.getValue().getId()));
        TableColumn<Vehicle, String> numberCol = UIUtils.column("Vehicle No.", "vehicleNumber", 170);
        TableColumn<Vehicle, String> typeCol = UIUtils.column("Type", 150, c -> new SimpleStringProperty(c.getValue().getType().toString()));
        TableColumn<Vehicle, String> capacityCol = UIUtils.column("Capacity (t)", 100, c -> new SimpleStringProperty(UIUtils.formatNumber(c.getValue().getCapacityTons())));
        TableColumn<Vehicle, String> statusCol = UIUtils.column("Status", 140, c -> new SimpleStringProperty(c.getValue().getStatus().toString()));
        TableColumn<Vehicle, String> driverCol = UIUtils.column("Assigned Driver", 170, c -> {
            Integer driverId = c.getValue().getAssignedDriverId();
            return new SimpleStringProperty(driverId == null ? "Unassigned" : services.drivers().nameOf(driverId));
        });

        setColumns(List.of(idCol, numberCol, typeCol, capacityCol, statusCol, driverCol));
    }

    @Override
    protected String getTitle() {
        return "Vehicles & Fleet";
    }

    @Override
    protected String getSubtitle() {
        return "Trucks, container trailers, cargo ships, aircraft and rail wagons owned or leased by Folk Cargo Solution";
    }

    @Override
    protected boolean matchesSearch(Vehicle v, String q) {
        return contains(v.getVehicleNumber(), q) || contains(v.getType().toString(), q) || contains(v.getStatus().toString(), q);
    }

    private boolean contains(String value, String q) {
        return value != null && value.toLowerCase().contains(q);
    }

    @Override
    protected void handleAdd() {
        VehicleDialog.showAdd(services.vehicles(), services.drivers().getAll());
    }

    @Override
    protected void handleEdit(Vehicle item) {
        if (VehicleDialog.showEdit(item, services.drivers().getAll())) {
            services.vehicles().update(item);
        }
    }

    @Override
    protected void handleDelete(Vehicle item) {
        services.vehicles().delete(item);
    }
}
