package com.folkcargo.ui.dialogs;

import com.folkcargo.model.CustomsStatus;
import com.folkcargo.model.ExportRecord;
import com.folkcargo.model.Shipment;
import com.folkcargo.model.Shipment.ShipmentType;
import com.folkcargo.service.ExportService;
import com.folkcargo.ui.util.FormGrid;
import com.folkcargo.ui.util.UIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.Optional;

public final class ExportDialog {

    private ExportDialog() {
    }

    public static Optional<ExportRecord> showAdd(ExportService service, ObservableList<Shipment> allShipments) {
        Dialog<ExportRecord> dialog = new Dialog<>();
        dialog.setTitle("Add Export Record");
        dialog.setHeaderText("Record customs/export paperwork for a shipment");
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ObservableList<Shipment> exportShipments = allShipments.filtered(s -> s.getShipmentType() == ShipmentType.EXPORT);
        ComboBox<Shipment> shipmentBox = new ComboBox<>(exportShipments);
        shipmentBox.setMaxWidth(Double.MAX_VALUE);
        TextField licenseField = new TextField();
        TextField destinationField = new TextField();
        TextField portField = new TextField();
        TextField hsCodeField = new TextField();
        DatePicker exportDatePicker = UIUtils.datePicker(LocalDate.now());
        TextField dutyField = UIUtils.numericField(0);
        ComboBox<CustomsStatus> statusBox = new ComboBox<>(FXCollections.observableArrayList(CustomsStatus.values()));
        statusBox.getSelectionModel().select(CustomsStatus.PENDING);
        statusBox.setMaxWidth(Double.MAX_VALUE);

        FormGrid form = new FormGrid()
                .addRow("Shipment (export) *", shipmentBox)
                .addRow("Export License No. *", licenseField)
                .addRow("Destination Country", destinationField)
                .addRow("Port of Exit", portField)
                .addRow("HS Code", hsCodeField)
                .addRow("Export Date", exportDatePicker)
                .addRow("Duty Amount", dutyField)
                .addRow("Customs Status", statusBox);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(shipmentBox.valueProperty().isNull().or(licenseField.textProperty().isEmpty()));

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return null;
            return service.create(shipmentBox.getValue().getId(), licenseField.getText().trim(),
                    destinationField.getText().trim(), portField.getText().trim(), hsCodeField.getText().trim(),
                    exportDatePicker.getValue(), UIUtils.parseDoubleOrZero(dutyField.getText()), statusBox.getValue());
        });

        if (exportShipments.isEmpty()) {
            UIUtils.warn("No Export Shipments", "Nothing to link",
                    "There are currently no shipments of type EXPORT to attach a customs record to. Create an export shipment first.");
            return Optional.empty();
        }

        return dialog.showAndWait();
    }

    public static boolean showEdit(ExportRecord record, ObservableList<Shipment> allShipments) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Export Record");
        dialog.setHeaderText("Update export record #" + record.getId());
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ObservableList<Shipment> exportShipments = allShipments.filtered(s -> s.getShipmentType() == ShipmentType.EXPORT);
        ComboBox<Shipment> shipmentBox = new ComboBox<>(exportShipments);
        exportShipments.stream().filter(s -> s.getId() == record.getShipmentId()).findFirst().ifPresent(shipmentBox::setValue);
        shipmentBox.setMaxWidth(Double.MAX_VALUE);
        TextField licenseField = new TextField(record.getExportLicenseNumber());
        TextField destinationField = new TextField(record.getDestinationCountry());
        TextField portField = new TextField(record.getPortOfExit());
        TextField hsCodeField = new TextField(record.getHsCode());
        DatePicker exportDatePicker = UIUtils.datePicker(record.getExportDate());
        TextField dutyField = UIUtils.numericField(record.getDutyAmount());
        ComboBox<CustomsStatus> statusBox = new ComboBox<>(FXCollections.observableArrayList(CustomsStatus.values()));
        statusBox.getSelectionModel().select(record.getCustomsStatus());
        statusBox.setMaxWidth(Double.MAX_VALUE);

        FormGrid form = new FormGrid()
                .addRow("Shipment (export) *", shipmentBox)
                .addRow("Export License No. *", licenseField)
                .addRow("Destination Country", destinationField)
                .addRow("Port of Exit", portField)
                .addRow("HS Code", hsCodeField)
                .addRow("Export Date", exportDatePicker)
                .addRow("Duty Amount", dutyField)
                .addRow("Customs Status", statusBox);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(shipmentBox.valueProperty().isNull().or(licenseField.textProperty().isEmpty()));

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return false;
            record.setShipmentId(shipmentBox.getValue().getId());
            record.setExportLicenseNumber(licenseField.getText().trim());
            record.setDestinationCountry(destinationField.getText().trim());
            record.setPortOfExit(portField.getText().trim());
            record.setHsCode(hsCodeField.getText().trim());
            record.setExportDate(exportDatePicker.getValue());
            record.setDutyAmount(UIUtils.parseDoubleOrZero(dutyField.getText()));
            record.setCustomsStatus(statusBox.getValue());
            return true;
        });

        return dialog.showAndWait().orElse(false);
    }
}
