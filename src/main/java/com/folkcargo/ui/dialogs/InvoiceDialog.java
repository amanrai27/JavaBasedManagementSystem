package com.folkcargo.ui.dialogs;

import com.folkcargo.model.Customer;
import com.folkcargo.model.Invoice;
import com.folkcargo.model.Invoice.InvoiceStatus;
import com.folkcargo.model.Shipment;
import com.folkcargo.service.InvoiceService;
import com.folkcargo.ui.util.FormGrid;
import com.folkcargo.ui.util.UIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.Optional;

public final class InvoiceDialog {

    private InvoiceDialog() {
    }

    public static Optional<Invoice> showAdd(InvoiceService service, ObservableList<Customer> customers,
                                             ObservableList<Shipment> shipments) {
        Dialog<Invoice> dialog = new Dialog<>();
        dialog.setTitle("Add Invoice");
        dialog.setHeaderText("Raise a new invoice for a shipment");
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Shipment> shipmentBox = new ComboBox<>(shipments);
        shipmentBox.setMaxWidth(Double.MAX_VALUE);
        ComboBox<Customer> customerBox = new ComboBox<>(customers);
        customerBox.setMaxWidth(Double.MAX_VALUE);
        shipmentBox.valueProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                customers.stream().filter(c -> c.getId() == sel.getCustomerId()).findFirst().ifPresent(customerBox::setValue);
            }
        });
        DatePicker issuePicker = UIUtils.datePicker(LocalDate.now());
        DatePicker duePicker = UIUtils.datePicker(LocalDate.now().plusDays(15));
        TextField subtotalField = UIUtils.numericField(0);
        TextField taxField = UIUtils.numericField(0);
        ComboBox<InvoiceStatus> statusBox = new ComboBox<>(FXCollections.observableArrayList(InvoiceStatus.values()));
        statusBox.getSelectionModel().select(InvoiceStatus.DRAFT);
        statusBox.setMaxWidth(Double.MAX_VALUE);

        FormGrid form = new FormGrid()
                .addRow("Shipment *", shipmentBox)
                .addRow("Customer *", customerBox)
                .addRow("Issue Date", issuePicker)
                .addRow("Due Date", duePicker)
                .addRow("Subtotal *", subtotalField)
                .addRow("Tax Amount", taxField)
                .addRow("Status", statusBox);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(shipmentBox.valueProperty().isNull()
                .or(customerBox.valueProperty().isNull())
                .or(subtotalField.textProperty().isEmpty()));

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return null;
            return service.create(customerBox.getValue().getId(), shipmentBox.getValue().getId(),
                    issuePicker.getValue(), duePicker.getValue(), UIUtils.parseDoubleOrZero(subtotalField.getText()),
                    UIUtils.parseDoubleOrZero(taxField.getText()), statusBox.getValue());
        });

        return dialog.showAndWait();
    }

    public static boolean showEdit(Invoice invoice, ObservableList<Customer> customers, ObservableList<Shipment> shipments) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Invoice");
        dialog.setHeaderText("Update invoice " + invoice.getInvoiceNumber());
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Shipment> shipmentBox = new ComboBox<>(shipments);
        shipments.stream().filter(s -> s.getId() == invoice.getShipmentId()).findFirst().ifPresent(shipmentBox::setValue);
        shipmentBox.setMaxWidth(Double.MAX_VALUE);
        ComboBox<Customer> customerBox = new ComboBox<>(customers);
        customers.stream().filter(c -> c.getId() == invoice.getCustomerId()).findFirst().ifPresent(customerBox::setValue);
        customerBox.setMaxWidth(Double.MAX_VALUE);
        DatePicker issuePicker = UIUtils.datePicker(invoice.getIssueDate());
        DatePicker duePicker = UIUtils.datePicker(invoice.getDueDate());
        TextField subtotalField = UIUtils.numericField(invoice.getSubtotal());
        TextField taxField = UIUtils.numericField(invoice.getTaxAmount());
        ComboBox<InvoiceStatus> statusBox = new ComboBox<>(FXCollections.observableArrayList(InvoiceStatus.values()));
        statusBox.getSelectionModel().select(invoice.getStatus());
        statusBox.setMaxWidth(Double.MAX_VALUE);

        FormGrid form = new FormGrid()
                .addRow("Shipment *", shipmentBox)
                .addRow("Customer *", customerBox)
                .addRow("Issue Date", issuePicker)
                .addRow("Due Date", duePicker)
                .addRow("Subtotal *", subtotalField)
                .addRow("Tax Amount", taxField)
                .addRow("Status", statusBox);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(shipmentBox.valueProperty().isNull()
                .or(customerBox.valueProperty().isNull())
                .or(subtotalField.textProperty().isEmpty()));

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return false;
            invoice.setShipmentId(shipmentBox.getValue().getId());
            invoice.setCustomerId(customerBox.getValue().getId());
            invoice.setIssueDate(issuePicker.getValue());
            invoice.setDueDate(duePicker.getValue());
            invoice.setSubtotal(UIUtils.parseDoubleOrZero(subtotalField.getText()));
            invoice.setTaxAmount(UIUtils.parseDoubleOrZero(taxField.getText()));
            invoice.setStatus(statusBox.getValue());
            return true;
        });

        return dialog.showAndWait().orElse(false);
    }
}
