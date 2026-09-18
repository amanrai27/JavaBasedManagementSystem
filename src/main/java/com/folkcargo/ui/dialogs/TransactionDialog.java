package com.folkcargo.ui.dialogs;

import com.folkcargo.model.Customer;
import com.folkcargo.model.Shipment;
import com.folkcargo.model.Transaction;
import com.folkcargo.model.Transaction.PaymentMethod;
import com.folkcargo.model.Transaction.TransactionType;
import com.folkcargo.service.TransactionService;
import com.folkcargo.ui.util.FormGrid;
import com.folkcargo.ui.util.UIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.time.LocalDate;
import java.util.Optional;

public final class TransactionDialog {

    private TransactionDialog() {
    }

    public static Optional<Transaction> showAdd(TransactionService service, ObservableList<Customer> customers,
                                                 ObservableList<Shipment> shipments) {
        Dialog<Transaction> dialog = new Dialog<>();
        dialog.setTitle("Add Transaction");
        dialog.setHeaderText("Record a payment, refund, expense or customs duty");
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Customer> customerBox = new ComboBox<>(customers);
        customerBox.setMaxWidth(Double.MAX_VALUE);
        ComboBox<Shipment> shipmentBox = new ComboBox<>(shipments);
        shipmentBox.setPromptText("(none - general transaction)");
        shipmentBox.setMaxWidth(Double.MAX_VALUE);
        Button clearShipmentBtn = new Button("Clear");
        clearShipmentBtn.setOnAction(e -> shipmentBox.setValue(null));
        HBox shipmentRow = new HBox(8, shipmentBox, clearShipmentBtn);
        HBox.setHgrow(shipmentBox, Priority.ALWAYS);

        ComboBox<TransactionType> typeBox = new ComboBox<>(FXCollections.observableArrayList(TransactionType.values()));
        typeBox.getSelectionModel().select(TransactionType.PAYMENT_RECEIVED);
        typeBox.setMaxWidth(Double.MAX_VALUE);
        TextField amountField = UIUtils.numericField(0);
        ComboBox<PaymentMethod> methodBox = new ComboBox<>(FXCollections.observableArrayList(PaymentMethod.values()));
        methodBox.getSelectionModel().select(PaymentMethod.BANK_TRANSFER);
        methodBox.setMaxWidth(Double.MAX_VALUE);
        DatePicker datePicker = UIUtils.datePicker(LocalDate.now());
        TextField descriptionField = new TextField();

        FormGrid form = new FormGrid()
                .addRow("Customer *", customerBox)
                .addRow("Linked Shipment", shipmentRow)
                .addRow("Type", typeBox)
                .addRow("Amount *", amountField)
                .addRow("Payment Method", methodBox)
                .addRow("Date", datePicker)
                .addRow("Description", descriptionField);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(customerBox.valueProperty().isNull().or(amountField.textProperty().isEmpty()));

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return null;
            Shipment s = shipmentBox.getValue();
            return service.create(s == null ? null : s.getId(), customerBox.getValue().getId(),
                    typeBox.getValue(), UIUtils.parseDoubleOrZero(amountField.getText()), methodBox.getValue(),
                    datePicker.getValue(), descriptionField.getText().trim());
        });

        return dialog.showAndWait();
    }

    public static boolean showEdit(Transaction transaction, ObservableList<Customer> customers,
                                    ObservableList<Shipment> shipments) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Transaction");
        dialog.setHeaderText("Update transaction " + transaction.getTransactionCode());
        UIUtils.styleDialog(dialog);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Customer> customerBox = new ComboBox<>(customers);
        customers.stream().filter(c -> c.getId() == transaction.getCustomerId()).findFirst().ifPresent(customerBox::setValue);
        customerBox.setMaxWidth(Double.MAX_VALUE);
        ComboBox<Shipment> shipmentBox = new ComboBox<>(shipments);
        if (transaction.getShipmentId() != null) {
            shipments.stream().filter(s -> s.getId() == transaction.getShipmentId()).findFirst().ifPresent(shipmentBox::setValue);
        }
        shipmentBox.setPromptText("(none - general transaction)");
        shipmentBox.setMaxWidth(Double.MAX_VALUE);
        Button clearShipmentBtn = new Button("Clear");
        clearShipmentBtn.setOnAction(e -> shipmentBox.setValue(null));
        HBox shipmentRow = new HBox(8, shipmentBox, clearShipmentBtn);
        HBox.setHgrow(shipmentBox, Priority.ALWAYS);

        ComboBox<TransactionType> typeBox = new ComboBox<>(FXCollections.observableArrayList(TransactionType.values()));
        typeBox.getSelectionModel().select(transaction.getType());
        typeBox.setMaxWidth(Double.MAX_VALUE);
        TextField amountField = UIUtils.numericField(transaction.getAmount());
        ComboBox<PaymentMethod> methodBox = new ComboBox<>(FXCollections.observableArrayList(PaymentMethod.values()));
        methodBox.getSelectionModel().select(transaction.getPaymentMethod());
        methodBox.setMaxWidth(Double.MAX_VALUE);
        DatePicker datePicker = UIUtils.datePicker(transaction.getDate());
        TextField descriptionField = new TextField(transaction.getDescription());

        FormGrid form = new FormGrid()
                .addRow("Customer *", customerBox)
                .addRow("Linked Shipment", shipmentRow)
                .addRow("Type", typeBox)
                .addRow("Amount *", amountField)
                .addRow("Payment Method", methodBox)
                .addRow("Date", datePicker)
                .addRow("Description", descriptionField);
        dialog.getDialogPane().setContent(form.build());

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(customerBox.valueProperty().isNull().or(amountField.textProperty().isEmpty()));

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return false;
            Shipment s = shipmentBox.getValue();
            transaction.setCustomerId(customerBox.getValue().getId());
            transaction.setShipmentId(s == null ? null : s.getId());
            transaction.setType(typeBox.getValue());
            transaction.setAmount(UIUtils.parseDoubleOrZero(amountField.getText()));
            transaction.setPaymentMethod(methodBox.getValue());
            transaction.setDate(datePicker.getValue());
            transaction.setDescription(descriptionField.getText().trim());
            return true;
        });

        return dialog.showAndWait().orElse(false);
    }
}
