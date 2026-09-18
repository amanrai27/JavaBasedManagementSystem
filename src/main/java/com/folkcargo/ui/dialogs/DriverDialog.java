package com.folkcargo.ui.dialogs;

import com.folkcargo.model.Driver;
import com.folkcargo.model.Driver.DriverStatus;
import com.folkcargo.service.DriverService;
import com.folkcargo.ui.util.FormGrid;
import com.folkcargo.ui.util.UIUtils;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.Optional;

public final class DriverDialog {

    private DriverDialog() {
    }

    public static Optional<Driver> showAdd(DriverService service) {
        Dialog<Driver> dialog = new Dialog<>();
        dialog.setTitle("Add Driver");
        dialog.setHeaderText("Register a new driver");
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField nameField = new TextField();
        TextField licenseField = new TextField();
        TextField phoneField = new TextField();
        Spinner<Integer> experienceSpinner = new Spinner<>(0, 50, 2);
        experienceSpinner.setEditable(true);
        experienceSpinner.setMaxWidth(Double.MAX_VALUE);
        ComboBox<DriverStatus> statusBox = new ComboBox<>(FXCollections.observableArrayList(DriverStatus.values()));
        statusBox.getSelectionModel().select(DriverStatus.AVAILABLE);
        statusBox.setMaxWidth(Double.MAX_VALUE);

        FormGrid form = new FormGrid()
                .addRow("Full Name *", nameField)
                .addRow("License Number *", licenseField)
                .addRow("Phone", phoneField)
                .addRow("Experience (yrs)", experienceSpinner)
                .addRow("Status", statusBox);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(nameField.textProperty().isEmpty().or(licenseField.textProperty().isEmpty()));

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return null;
            return service.create(nameField.getText().trim(), licenseField.getText().trim(),
                    phoneField.getText().trim(), experienceSpinner.getValue(), statusBox.getValue());
        });

        return dialog.showAndWait();
    }

    public static boolean showEdit(Driver driver) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Driver");
        dialog.setHeaderText("Update driver #" + driver.getId());
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField nameField = new TextField(driver.getName());
        TextField licenseField = new TextField(driver.getLicenseNumber());
        TextField phoneField = new TextField(driver.getPhone());
        Spinner<Integer> experienceSpinner = new Spinner<>(0, 50, driver.getExperienceYears());
        experienceSpinner.setEditable(true);
        experienceSpinner.setMaxWidth(Double.MAX_VALUE);
        ComboBox<DriverStatus> statusBox = new ComboBox<>(FXCollections.observableArrayList(DriverStatus.values()));
        statusBox.getSelectionModel().select(driver.getStatus());
        statusBox.setMaxWidth(Double.MAX_VALUE);

        FormGrid form = new FormGrid()
                .addRow("Full Name *", nameField)
                .addRow("License Number *", licenseField)
                .addRow("Phone", phoneField)
                .addRow("Experience (yrs)", experienceSpinner)
                .addRow("Status", statusBox);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(nameField.textProperty().isEmpty().or(licenseField.textProperty().isEmpty()));

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return false;
            driver.setName(nameField.getText().trim());
            driver.setLicenseNumber(licenseField.getText().trim());
            driver.setPhone(phoneField.getText().trim());
            driver.setExperienceYears(experienceSpinner.getValue());
            driver.setStatus(statusBox.getValue());
            return true;
        });

        return dialog.showAndWait().orElse(false);
    }
}
