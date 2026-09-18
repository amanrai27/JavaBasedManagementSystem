package com.folkcargo.ui;

import com.folkcargo.model.Shipment;
import com.folkcargo.service.ServiceRegistry;
import com.folkcargo.ui.dialogs.ShipmentDialog;
import com.folkcargo.ui.util.UIUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

import java.util.List;

public class ShipmentView extends CrudView<Shipment> {

    private final ServiceRegistry services;

    public ShipmentView(ServiceRegistry services) {
        super(services.shipments().getAll());
        this.services = services;

        TableColumn<Shipment, String> codeCol = UIUtils.column("Code", "shipmentCode", 110);
        TableColumn<Shipment, String> customerCol = UIUtils.column("Customer", 170, c -> new SimpleStringProperty(services.customers().nameOf(c.getValue().getCustomerId())));
        TableColumn<Shipment, String> originCol = UIUtils.column("Origin", "origin", 150);
        TableColumn<Shipment, String> destCol = UIUtils.column("Destination", "destination", 150);
        TableColumn<Shipment, String> typeCol = UIUtils.column("Type", 90, c -> new SimpleStringProperty(c.getValue().getShipmentType().toString()));
        TableColumn<Shipment, String> weightCol = UIUtils.column("Weight (kg)", 100, c -> new SimpleStringProperty(UIUtils.formatNumber(c.getValue().getWeightKg())));
        TableColumn<Shipment, String> statusCol = UIUtils.column("Status", 110, c -> new SimpleStringProperty(c.getValue().getStatus().toString()));
        TableColumn<Shipment, String> createdCol = UIUtils.column("Created", 100, c -> new SimpleStringProperty(UIUtils.formatDate(c.getValue().getCreatedDate())));
        TableColumn<Shipment, String> expectedCol = UIUtils.column("Expected", 100, c -> new SimpleStringProperty(UIUtils.formatDate(c.getValue().getExpectedDeliveryDate())));

        setColumns(List.of(codeCol, customerCol, originCol, destCol, typeCol, weightCol, statusCol, createdCol, expectedCol));
    }

    @Override
    protected String getTitle() {
        return "Shipments";
    }

    @Override
    protected String getSubtitle() {
        return "All cargo shipments booked with Folk Cargo Solution Pvt. Ltd.";
    }

    @Override
    protected boolean matchesSearch(Shipment s, String q) {
        return contains(s.getShipmentCode(), q) || contains(s.getOrigin(), q) || contains(s.getDestination(), q)
                || contains(s.getCargoDescription(), q) || contains(services.customers().nameOf(s.getCustomerId()), q);
    }

    private boolean contains(String value, String q) {
        return value != null && value.toLowerCase().contains(q);
    }

    @Override
    protected void handleAdd() {
        ShipmentDialog.showAdd(services.shipments(), services.customers().getAll());
    }

    @Override
    protected void handleEdit(Shipment item) {
        if (ShipmentDialog.showEdit(item, services.customers().getAll())) {
            services.shipments().update(item);
        }
    }

    @Override
    protected void handleDelete(Shipment item) {
        services.shipments().delete(item);
    }
}
