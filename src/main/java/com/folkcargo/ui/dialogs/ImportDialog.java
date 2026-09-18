package com.folkcargo.ui.dialogs;

import com.folkcargo.model.CustomsStatus;
import com.folkcargo.model.ImportRecord;
import com.folkcargo.model.Shipment;
import com.folkcargo.model.Shipment.ShipmentType;
import com.folkcargo.service.ImportService;
import com.folkcargo.ui.util.FormGrid;
import com.folkcargo.ui.util.UIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.Optional;

public final class ImportDialog {

    private ImportDialog() {
    }

    public static Optional<ImportRecord> showAdd(ImportService service, ObservableList<Shipment> allShipments) {
        Dialog<ImportRecord> dialog = new Dialog<>();
        dialog.setTitle("Add Import Record");
        dialog.setHeaderText("Record customs/import paperwork for a shipment");
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ObservableList<Shipment> importShipments = allShipments.filtered(s -> s.getShipmentType() == ShipmentType.IMPORT);
        ComboBox<Shipment> shipmentBox = new ComboBox<>(importShipments);
        shipmentBox.setMaxWidth(Double.MAX_VALUE);
        TextField licenseField = new TextField();
        TextField originField = new TextField();
        TextField portField = new TextField();
        TextField hsCodeField = new TextField();
        DatePicker importDatePicker = UIUtils.datePicker(LocalDate.now());
        TextField dutyField = UIUtils.numericField(0);
        ComboBox<CustomsStatus> statusBox = new ComboBox<>(FXCollections.observableArrayList(CustomsStatus.values()));
        statusBox.getSelectionModel().select(CustomsStatus.PENDING);
        statusBox.setMaxWidth(Double.MAX_VALUE);

        FormGrid form = new FormGrid()
                .addRow("Shipment (import) *", shipmentBox)
                .addRow("Import License No. *", licenseField)
                .addRow("Origin Country", originField)
                .addRow("Port of Entry", portField)
                .addRow("HS Code", hsCodeField)
                .addRow("Import Date", importDatePicker)
                .addRow("Duty Amount", dutyField)
                .addRow("Customs Status", statusBox);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(shipmentBox.valueProperty().isNull().or(licenseField.textProperty().isEmpty()));

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return null;
            return service.create(shipmentBox.getValue().getId(), licenseField.getText().trim(),
                    originField.getText().trim(), portField.getText().trim(), hsCodeField.getText().trim(),
                    importDatePicker.getValue(), UIUtils.parseDoubleOrZero(dutyField.getText()), statusBox.getValue());
        });

        if (importShipments.isEmpty()) {
            UIUtils.warn("No Import Shipments", "Nothing to link",
                    "There are currently no shipments of type IMPORT to attach a customs record to. Create an import shipment first.");
            return Optional.empty();
        }

        return dialog.showAndWait();
    }

    public static boolean showEdit(ImportRecord record, ObservableList<Shipment> allShipments) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Import Record");
        dialog.setHeaderText("Update import record #" + record.getId());
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ObservableList<Shipment> importShipments = allShipments.filtered(s -> s.getShipmentType() == ShipmentType.IMPORT);
        ComboBox<Shipment> shipmentBox = new ComboBox<>(importShipments);
        importShipments.stream().filter(s -> s.getId() == record.getShipmentId()).findFirst().ifPresent(shipmentBox::setValue);
        shipmentBox.setMaxWidth(Double.MAX_VALUE);
        TextField licenseField = new TextField(record.getImportLicenseNumber());
        TextField originField = new TextField(record.getOriginCountry());
        TextField portField = new TextField(record.getPortOfEntry());
        TextField hsCodeField = new TextField(record.getHsCode());
        DatePicker importDatePicker = UIUtils.datePicker(record.getImportDate());
        TextField dutyField = UIUtils.numericField(record.getDutyAmount());
        ComboBox<CustomsStatus> statusBox = new ComboBox<>(FXCollections.observableArrayList(CustomsStatus.values()));
        statusBox.getSelectionModel().select(record.getCustomsStatus());
        statusBox.setMaxWidth(Double.MAX_VALUE);

        FormGrid form = new FormGrid()
                .addRow("Shipment (import) *", shipmentBox)
                .addRow("Import License No. *", licenseField)
                .addRow("Origin Country", originField)
                .addRow("Port of Entry", portField)
                .addRow("HS Code", hsCodeField)
                .addRow("Import Date", importDatePicker)
                .addRow("Duty Amount", dutyField)
                .addRow("Customs Status", statusBox);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(shipmentBox.valueProperty().isNull().or(licenseField.textProperty().isEmpty()));

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return false;
            record.setShipmentId(shipmentBox.getValue().getId());
            record.setImportLicenseNumber(licenseField.getText().trim());
            record.setOriginCountry(originField.getText().trim());
            record.setPortOfEntry(portField.getText().trim());
            record.setHsCode(hsCodeField.getText().trim());
            record.setImportDate(importDatePicker.getValue());
            record.setDutyAmount(UIUtils.parseDoubleOrZero(dutyField.getText()));
            record.setCustomsStatus(statusBox.getValue());
            return true;
        });

        return dialog.showAndWait().orElse(false);
    }
}
