package com.folkcargo.ui;

import com.folkcargo.model.Customer;
import com.folkcargo.service.ServiceRegistry;
import com.folkcargo.ui.dialogs.CustomerDialog;
import com.folkcargo.ui.util.UIUtils;
import javafx.scene.control.TableColumn;

import java.util.List;

public class CustomerView extends CrudView<Customer> {

    private final ServiceRegistry services;

    public CustomerView(ServiceRegistry services) {
        super(services.customers().getAll());
        this.services = services;

        TableColumn<Customer, Number> idCol = UIUtils.column("ID", 50, c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()));
        TableColumn<Customer, String> nameCol = UIUtils.column("Name", "name", 160);
        TableColumn<Customer, String> companyCol = UIUtils.column("Company", "companyName", 180);
        TableColumn<Customer, String> typeCol = UIUtils.column("Type", 100, c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCustomerType().toString()));
        TableColumn<Customer, String> emailCol = UIUtils.column("Email", "email", 190);
        TableColumn<Customer, String> phoneCol = UIUtils.column("Phone", "phone", 140);
        TableColumn<Customer, String> cityCol = UIUtils.column("City", "city", 120);
        TableColumn<Customer, String> countryCol = UIUtils.column("Country", "country", 110);
        TableColumn<Customer, String> registeredCol = UIUtils.column("Registered", 110, c -> new javafx.beans.property.SimpleStringProperty(UIUtils.formatDate(c.getValue().getRegisteredDate())));

        setColumns(List.of(idCol, nameCol, companyCol, typeCol, emailCol, phoneCol, cityCol, countryCol, registeredCol));
    }

    @Override
    protected String getTitle() {
        return "Customers & Clients";
    }

    @Override
    protected String getSubtitle() {
        return "Individuals and businesses who book shipments with Folk Cargo Solution Pvt. Ltd.";
    }

    @Override
    protected boolean matchesSearch(Customer c, String q) {
        return contains(c.getName(), q) || contains(c.getCompanyName(), q) || contains(c.getEmail(), q)
                || contains(c.getCity(), q) || contains(c.getCountry(), q) || contains(c.getPhone(), q);
    }

    private boolean contains(String value, String q) {
        return value != null && value.toLowerCase().contains(q);
    }

    @Override
    protected void handleAdd() {
        CustomerDialog.showAdd(services.customers());
    }

    @Override
    protected void handleEdit(Customer item) {
        if (CustomerDialog.showEdit(item)) {
            services.customers().update(item);
        }
    }

    @Override
    protected void handleDelete(Customer item) {
        services.customers().delete(item);
    }
}
