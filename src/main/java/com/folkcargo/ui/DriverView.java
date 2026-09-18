package com.folkcargo.ui;

import com.folkcargo.model.Driver;
import com.folkcargo.service.ServiceRegistry;
import com.folkcargo.ui.dialogs.DriverDialog;
import com.folkcargo.ui.util.UIUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

import java.util.List;

public class DriverView extends CrudView<Driver> {

    private final ServiceRegistry services;

    public DriverView(ServiceRegistry services) {
        super(services.drivers().getAll());
        this.services = services;

        TableColumn<Driver, Number> idCol = UIUtils.column("ID", 50, c -> new SimpleIntegerProperty(c.getValue().getId()));
        TableColumn<Driver, String> nameCol = UIUtils.column("Name", "name", 170);
        TableColumn<Driver, String> licenseCol = UIUtils.column("License No.", "licenseNumber", 170);
        TableColumn<Driver, String> phoneCol = UIUtils.column("Phone", "phone", 140);
        TableColumn<Driver, Number> expCol = UIUtils.column("Experience", 90, c -> new SimpleIntegerProperty(c.getValue().getExperienceYears()));
        TableColumn<Driver, String> statusCol = UIUtils.column("Status", 120, c -> new SimpleStringProperty(c.getValue().getStatus().toString()));

        setColumns(List.of(idCol, nameCol, licenseCol, phoneCol, expCol, statusCol));
    }

    @Override
    protected String getTitle() {
        return "Drivers";
    }

    @Override
    protected String getSubtitle() {
        return "Driving staff available for transport assignments";
    }

    @Override
    protected boolean matchesSearch(Driver d, String q) {
        return contains(d.getName(), q) || contains(d.getLicenseNumber(), q) || contains(d.getPhone(), q);
    }

    private boolean contains(String value, String q) {
        return value != null && value.toLowerCase().contains(q);
    }

    @Override
    protected void handleAdd() {
        DriverDialog.showAdd(services.drivers());
    }

    @Override
    protected void handleEdit(Driver item) {
        if (DriverDialog.showEdit(item)) {
            services.drivers().update(item);
        }
    }

    @Override
    protected void handleDelete(Driver item) {
        services.drivers().delete(item);
    }
}
