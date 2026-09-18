package com.folkcargo.ui.dialogs;

import com.folkcargo.model.Customer;
import com.folkcargo.model.Customer.CustomerType;
import com.folkcargo.service.CustomerService;
import com.folkcargo.ui.util.FormGrid;
import com.folkcargo.ui.util.UIUtils;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.Optional;

public final class CustomerDialog {

    private CustomerDialog() {
    }

    public static Optional<Customer> showAdd(CustomerService service) {
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Add Customer");
        dialog.setHeaderText("Register a new customer / client");
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField nameField = new TextField();
        TextField companyField = new TextField();
        ComboBox<CustomerType> typeBox = new ComboBox<>(javafx.collections.FXCollections.observableArrayList(CustomerType.values()));
        typeBox.getSelectionModel().select(CustomerType.INDIVIDUAL);
        typeBox.setMaxWidth(Double.MAX_VALUE);
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();
        TextField cityField = new TextField();
        TextField countryField = new TextField();

        FormGrid form = new FormGrid()
                .addRow("Full Name *", nameField)
                .addRow("Company Name", companyField)
                .addRow("Customer Type", typeBox)
                .addRow("Email", emailField)
                .addRow("Phone", phoneField)
                .addRow("Address", addressField)
                .addRow("City", cityField)
                .addRow("Country", countryField);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(nameField.textProperty().isEmpty());

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return null;
            return service.create(
                    nameField.getText().trim(),
                    companyField.getText().trim(),
                    typeBox.getValue(),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    addressField.getText().trim(),
                    cityField.getText().trim(),
                    countryField.getText().trim()
            );
        });

        return dialog.showAndWait();
    }

    public static boolean showEdit(Customer customer) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Customer");
        dialog.setHeaderText("Update customer #" + customer.getId());
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField nameField = new TextField(customer.getName());
        TextField companyField = new TextField(customer.getCompanyName());
        ComboBox<CustomerType> typeBox = new ComboBox<>(javafx.collections.FXCollections.observableArrayList(CustomerType.values()));
        typeBox.getSelectionModel().select(customer.getCustomerType());
        typeBox.setMaxWidth(Double.MAX_VALUE);
        TextField emailField = new TextField(customer.getEmail());
        TextField phoneField = new TextField(customer.getPhone());
        TextField addressField = new TextField(customer.getAddress());
        TextField cityField = new TextField(customer.getCity());
        TextField countryField = new TextField(customer.getCountry());

        FormGrid form = new FormGrid()
                .addRow("Full Name *", nameField)
                .addRow("Company Name", companyField)
                .addRow("Customer Type", typeBox)
                .addRow("Email", emailField)
                .addRow("Phone", phoneField)
                .addRow("Address", addressField)
                .addRow("City", cityField)
                .addRow("Country", countryField);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(nameField.textProperty().isEmpty());

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return false;
            customer.setName(nameField.getText().trim());
            customer.setCompanyName(companyField.getText().trim());
            customer.setCustomerType(typeBox.getValue());
            customer.setEmail(emailField.getText().trim());
            customer.setPhone(phoneField.getText().trim());
            customer.setAddress(addressField.getText().trim());
            customer.setCity(cityField.getText().trim());
            customer.setCountry(countryField.getText().trim());
            return true;
        });

        return dialog.showAndWait().orElse(false);
    }
}
