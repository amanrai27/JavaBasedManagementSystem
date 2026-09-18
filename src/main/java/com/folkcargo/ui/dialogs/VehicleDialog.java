package com.folkcargo.ui.dialogs;

import com.folkcargo.model.Driver;
import com.folkcargo.model.Vehicle;
import com.folkcargo.model.Vehicle.VehicleStatus;
import com.folkcargo.model.Vehicle.VehicleType;
import com.folkcargo.service.VehicleService;
import com.folkcargo.ui.util.FormGrid;
import com.folkcargo.ui.util.UIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.Optional;

public final class VehicleDialog {

    private VehicleDialog() {
    }

    public static Optional<Vehicle> showAdd(VehicleService service, ObservableList<Driver> drivers) {
        Dialog<Vehicle> dialog = new Dialog<>();
        dialog.setTitle("Add Vehicle");
        dialog.setHeaderText("Register a new fleet vehicle");
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField numberField = new TextField();
        ComboBox<VehicleType> typeBox = new ComboBox<>(FXCollections.observableArrayList(VehicleType.values()));
        typeBox.getSelectionModel().select(VehicleType.TRUCK);
        typeBox.setMaxWidth(Double.MAX_VALUE);
        TextField capacityField = UIUtils.numericField(10);
        ComboBox<VehicleStatus> statusBox = new ComboBox<>(FXCollections.observableArrayList(VehicleStatus.values()));
        statusBox.getSelectionModel().select(VehicleStatus.AVAILABLE);
        statusBox.setMaxWidth(Double.MAX_VALUE);
        ComboBox<Driver> driverBox = new ComboBox<>(drivers);
        driverBox.setPromptText("Unassigned");
        driverBox.setMaxWidth(Double.MAX_VALUE);

        FormGrid form = new FormGrid()
                .addRow("Vehicle / Reg. Number *", numberField)
                .addRow("Type", typeBox)
                .addRow("Capacity (tons)", capacityField)
                .addRow("Status", statusBox)
                .addRow("Assigned Driver", driverBox);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(numberField.textProperty().isEmpty());

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return null;
            Driver d = driverBox.getValue();
            return service.create(numberField.getText().trim(), typeBox.getValue(),
                    UIUtils.parseDoubleOrZero(capacityField.getText()), statusBox.getValue(),
                    d == null ? null : d.getId());
        });

        return dialog.showAndWait();
    }

    public static boolean showEdit(Vehicle vehicle, ObservableList<Driver> drivers) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Vehicle");
        dialog.setHeaderText("Update vehicle #" + vehicle.getId());
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField numberField = new TextField(vehicle.getVehicleNumber());
        ComboBox<VehicleType> typeBox = new ComboBox<>(FXCollections.observableArrayList(VehicleType.values()));
        typeBox.getSelectionModel().select(vehicle.getType());
        typeBox.setMaxWidth(Double.MAX_VALUE);
        TextField capacityField = UIUtils.numericField(vehicle.getCapacityTons());
        ComboBox<VehicleStatus> statusBox = new ComboBox<>(FXCollections.observableArrayList(VehicleStatus.values()));
        statusBox.getSelectionModel().select(vehicle.getStatus());
        statusBox.setMaxWidth(Double.MAX_VALUE);
        ComboBox<Driver> driverBox = new ComboBox<>(drivers);
        driverBox.setPromptText("Unassigned");
        driverBox.setMaxWidth(Double.MAX_VALUE);
        if (vehicle.getAssignedDriverId() != null) {
            drivers.stream().filter(d -> d.getId() == vehicle.getAssignedDriverId())
                    .findFirst().ifPresent(driverBox::setValue);
        }

        FormGrid form = new FormGrid()
                .addRow("Vehicle / Reg. Number *", numberField)
                .addRow("Type", typeBox)
                .addRow("Capacity (tons)", capacityField)
                .addRow("Status", statusBox)
                .addRow("Assigned Driver", driverBox);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(numberField.textProperty().isEmpty());

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return false;
            vehicle.setVehicleNumber(numberField.getText().trim());
            vehicle.setType(typeBox.getValue());
            vehicle.setCapacityTons(UIUtils.parseDoubleOrZero(capacityField.getText()));
            vehicle.setStatus(statusBox.getValue());
            Driver d = driverBox.getValue();
            vehicle.setAssignedDriverId(d == null ? null : d.getId());
            return true;
        });

        return dialog.showAndWait().orElse(false);
    }
}
