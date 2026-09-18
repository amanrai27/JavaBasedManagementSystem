package com.folkcargo.ui.dialogs;

import com.folkcargo.model.Customer;
import com.folkcargo.model.Shipment;
import com.folkcargo.model.Shipment.ShipmentStatus;
import com.folkcargo.model.Shipment.ShipmentType;
import com.folkcargo.service.ShipmentService;
import com.folkcargo.ui.util.FormGrid;
import com.folkcargo.ui.util.UIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.Optional;

public final class ShipmentDialog {

    private ShipmentDialog() {
    }

    public static Optional<Shipment> showAdd(ShipmentService service, ObservableList<Customer> customers) {
        Dialog<Shipment> dialog = new Dialog<>();
        dialog.setTitle("Add Shipment");
        dialog.setHeaderText("Book a new shipment");
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Customer> customerBox = new ComboBox<>(customers);
        customerBox.setMaxWidth(Double.MAX_VALUE);
        TextField originField = new TextField();
        TextField destinationField = new TextField();
        ComboBox<ShipmentType> typeBox = new ComboBox<>(FXCollections.observableArrayList(ShipmentType.values()));
        typeBox.getSelectionModel().select(ShipmentType.DOMESTIC);
        typeBox.setMaxWidth(Double.MAX_VALUE);
        TextField cargoField = new TextField();
        TextField weightField = UIUtils.numericField(100);
        ComboBox<ShipmentStatus> statusBox = new ComboBox<>(FXCollections.observableArrayList(ShipmentStatus.values()));
        statusBox.getSelectionModel().select(ShipmentStatus.PENDING);
        statusBox.setMaxWidth(Double.MAX_VALUE);
        DatePicker expectedPicker = UIUtils.datePicker(LocalDate.now().plusDays(7));

        FormGrid form = new FormGrid()
                .addRow("Customer *", customerBox)
                .addRow("Origin *", originField)
                .addRow("Destination *", destinationField)
                .addRow("Shipment Type", typeBox)
                .addRow("Cargo Description", cargoField)
                .addRow("Weight (kg)", weightField)
                .addRow("Status", statusBox)
                .addRow("Expected Delivery", expectedPicker);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(originField.textProperty().isEmpty()
                .or(destinationField.textProperty().isEmpty())
                .or(customerBox.valueProperty().isNull()));

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return null;
            return service.create(customerBox.getValue().getId(), originField.getText().trim(),
                    destinationField.getText().trim(), typeBox.getValue(), cargoField.getText().trim(),
                    UIUtils.parseDoubleOrZero(weightField.getText()), statusBox.getValue(),
                    expectedPicker.getValue());
        });

        return dialog.showAndWait();
    }

    public static boolean showEdit(Shipment shipment, ObservableList<Customer> customers) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Shipment");
        dialog.setHeaderText("Update shipment " + shipment.getShipmentCode());
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Customer> customerBox = new ComboBox<>(customers);
        customers.stream().filter(c -> c.getId() == shipment.getCustomerId()).findFirst().ifPresent(customerBox::setValue);
        customerBox.setMaxWidth(Double.MAX_VALUE);
        TextField originField = new TextField(shipment.getOrigin());
        TextField destinationField = new TextField(shipment.getDestination());
        ComboBox<ShipmentType> typeBox = new ComboBox<>(FXCollections.observableArrayList(ShipmentType.values()));
        typeBox.getSelectionModel().select(shipment.getShipmentType());
        typeBox.setMaxWidth(Double.MAX_VALUE);
        TextField cargoField = new TextField(shipment.getCargoDescription());
        TextField weightField = UIUtils.numericField(shipment.getWeightKg());
        ComboBox<ShipmentStatus> statusBox = new ComboBox<>(FXCollections.observableArrayList(ShipmentStatus.values()));
        statusBox.getSelectionModel().select(shipment.getStatus());
        statusBox.setMaxWidth(Double.MAX_VALUE);
        DatePicker expectedPicker = UIUtils.datePicker(shipment.getExpectedDeliveryDate());
        DatePicker actualPicker = UIUtils.datePicker(shipment.getActualDeliveryDate());

        FormGrid form = new FormGrid()
                .addRow("Customer *", customerBox)
                .addRow("Origin *", originField)
                .addRow("Destination *", destinationField)
                .addRow("Shipment Type", typeBox)
                .addRow("Cargo Description", cargoField)
                .addRow("Weight (kg)", weightField)
                .addRow("Status", statusBox)
                .addRow("Expected Delivery", expectedPicker)
                .addRow("Actual Delivery", actualPicker);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(originField.textProperty().isEmpty()
                .or(destinationField.textProperty().isEmpty())
                .or(customerBox.valueProperty().isNull()));

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return false;
            shipment.setCustomerId(customerBox.getValue().getId());
            shipment.setOrigin(originField.getText().trim());
            shipment.setDestination(destinationField.getText().trim());
            shipment.setShipmentType(typeBox.getValue());
            shipment.setCargoDescription(cargoField.getText().trim());
            shipment.setWeightKg(UIUtils.parseDoubleOrZero(weightField.getText()));
            shipment.setStatus(statusBox.getValue());
            shipment.setExpectedDeliveryDate(expectedPicker.getValue());
            shipment.setActualDeliveryDate(actualPicker.getValue());
            return true;
        });

        return dialog.showAndWait().orElse(false);
    }
}
