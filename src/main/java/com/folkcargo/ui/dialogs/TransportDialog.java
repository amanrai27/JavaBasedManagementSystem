package com.folkcargo.ui.dialogs;

import com.folkcargo.model.Shipment;
import com.folkcargo.model.TransportAssignment;
import com.folkcargo.model.TransportAssignment.TransportStatus;
import com.folkcargo.model.Vehicle;
import com.folkcargo.model.Driver;
import com.folkcargo.service.TransportService;
import com.folkcargo.ui.util.FormGrid;
import com.folkcargo.ui.util.UIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.Optional;

public final class TransportDialog {

    private TransportDialog() {
    }

    public static Optional<TransportAssignment> showAdd(TransportService service, ObservableList<Shipment> shipments,
                                                          ObservableList<Vehicle> vehicles, ObservableList<Driver> drivers) {
        Dialog<TransportAssignment> dialog = new Dialog<>();
        dialog.setTitle("Add Transport Assignment");
        dialog.setHeaderText("Assign a vehicle and driver to a shipment");
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Shipment> shipmentBox = new ComboBox<>(shipments);
        shipmentBox.setMaxWidth(Double.MAX_VALUE);
        ComboBox<Vehicle> vehicleBox = new ComboBox<>(vehicles);
        vehicleBox.setMaxWidth(Double.MAX_VALUE);
        ComboBox<Driver> driverBox = new ComboBox<>(drivers);
        driverBox.setMaxWidth(Double.MAX_VALUE);
        TextField distanceField = UIUtils.numericField(100);
        DatePicker departurePicker = UIUtils.datePicker(LocalDate.now());
        DatePicker arrivalPicker = UIUtils.datePicker(LocalDate.now().plusDays(5));
        ComboBox<TransportStatus> statusBox = new ComboBox<>(FXCollections.observableArrayList(TransportStatus.values()));
        statusBox.getSelectionModel().select(TransportStatus.SCHEDULED);
        statusBox.setMaxWidth(Double.MAX_VALUE);

        shipmentBox.valueProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                // no-op placeholder for future auto-route suggestion
            }
        });

        FormGrid form = new FormGrid()
                .addRow("Shipment *", shipmentBox)
                .addRow("Vehicle *", vehicleBox)
                .addRow("Driver *", driverBox)
                .addRow("Distance (km)", distanceField)
                .addRow("Departure Date", departurePicker)
                .addRow("Arrival Date", arrivalPicker)
                .addRow("Status", statusBox);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(shipmentBox.valueProperty().isNull()
                .or(vehicleBox.valueProperty().isNull())
                .or(driverBox.valueProperty().isNull()));

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return null;
            Shipment s = shipmentBox.getValue();
            String route = s.getOrigin() + " -> " + s.getDestination();
            return service.create(s.getId(), vehicleBox.getValue().getId(), driverBox.getValue().getId(),
                    route, UIUtils.parseDoubleOrZero(distanceField.getText()),
                    departurePicker.getValue(), arrivalPicker.getValue(), statusBox.getValue());
        });

        return dialog.showAndWait();
    }

    public static boolean showEdit(TransportAssignment assignment, ObservableList<Shipment> shipments,
                                    ObservableList<Vehicle> vehicles, ObservableList<Driver> drivers) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Transport Assignment");
        dialog.setHeaderText("Update transport assignment #" + assignment.getId());
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Shipment> shipmentBox = new ComboBox<>(shipments);
        shipments.stream().filter(s -> s.getId() == assignment.getShipmentId()).findFirst().ifPresent(shipmentBox::setValue);
        shipmentBox.setMaxWidth(Double.MAX_VALUE);
        ComboBox<Vehicle> vehicleBox = new ComboBox<>(vehicles);
        vehicles.stream().filter(v -> v.getId() == assignment.getVehicleId()).findFirst().ifPresent(vehicleBox::setValue);
        vehicleBox.setMaxWidth(Double.MAX_VALUE);
        ComboBox<Driver> driverBox = new ComboBox<>(drivers);
        drivers.stream().filter(d -> d.getId() == assignment.getDriverId()).findFirst().ifPresent(driverBox::setValue);
        driverBox.setMaxWidth(Double.MAX_VALUE);
        TextField distanceField = UIUtils.numericField(assignment.getDistanceKm());
        DatePicker departurePicker = UIUtils.datePicker(assignment.getDepartureDate());
        DatePicker arrivalPicker = UIUtils.datePicker(assignment.getArrivalDate());
        ComboBox<TransportStatus> statusBox = new ComboBox<>(FXCollections.observableArrayList(TransportStatus.values()));
        statusBox.getSelectionModel().select(assignment.getStatus());
        statusBox.setMaxWidth(Double.MAX_VALUE);

        FormGrid form = new FormGrid()
                .addRow("Shipment *", shipmentBox)
                .addRow("Vehicle *", vehicleBox)
                .addRow("Driver *", driverBox)
                .addRow("Distance (km)", distanceField)
                .addRow("Departure Date", departurePicker)
                .addRow("Arrival Date", arrivalPicker)
                .addRow("Status", statusBox);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(shipmentBox.valueProperty().isNull()
                .or(vehicleBox.valueProperty().isNull())
                .or(driverBox.valueProperty().isNull()));

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return false;
            Shipment s = shipmentBox.getValue();
            assignment.setShipmentId(s.getId());
            assignment.setVehicleId(vehicleBox.getValue().getId());
            assignment.setDriverId(driverBox.getValue().getId());
            assignment.setRoute(s.getOrigin() + " -> " + s.getDestination());
            assignment.setDistanceKm(UIUtils.parseDoubleOrZero(distanceField.getText()));
            assignment.setDepartureDate(departurePicker.getValue());
            assignment.setArrivalDate(arrivalPicker.getValue());
            assignment.setStatus(statusBox.getValue());
            return true;
        });

        return dialog.showAndWait().orElse(false);
    }
}
