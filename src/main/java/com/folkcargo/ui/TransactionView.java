package com.folkcargo.ui;

import com.folkcargo.model.Transaction;
import com.folkcargo.service.ServiceRegistry;
import com.folkcargo.ui.dialogs.TransactionDialog;
import com.folkcargo.ui.util.UIUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

import java.util.List;

public class TransactionView extends CrudView<Transaction> {

    private final ServiceRegistry services;

    public TransactionView(ServiceRegistry services) {
        super(services.transactions().getAll());
        this.services = services;

        TableColumn<Transaction, String> codeCol = UIUtils.column("Txn Code", "transactionCode", 110);
        TableColumn<Transaction, String> customerCol = UIUtils.column("Customer", 170, c -> new SimpleStringProperty(services.customers().nameOf(c.getValue().getCustomerId())));
        TableColumn<Transaction, String> shipmentCol = UIUtils.column("Shipment", 110, c -> new SimpleStringProperty(
                c.getValue().getShipmentId() == null ? "-" : services.shipments().codeOf(c.getValue().getShipmentId())));
        TableColumn<Transaction, String> typeCol = UIUtils.column("Type", 140, c -> new SimpleStringProperty(c.getValue().getType().toString()));
        TableColumn<Transaction, String> amountCol = UIUtils.column("Amount", 120, c -> new SimpleStringProperty(UIUtils.formatCurrency(c.getValue().getAmount())));
        TableColumn<Transaction, String> methodCol = UIUtils.column("Method", 130, c -> new SimpleStringProperty(c.getValue().getPaymentMethod().toString()));
        TableColumn<Transaction, String> dateCol = UIUtils.column("Date", 100, c -> new SimpleStringProperty(UIUtils.formatDate(c.getValue().getDate())));
        TableColumn<Transaction, String> descCol = UIUtils.column("Description", "description", 220);

        setColumns(List.of(codeCol, customerCol, shipmentCol, typeCol, amountCol, methodCol, dateCol, descCol));
    }

    @Override
    protected String getTitle() {
        return "Transactions";
    }

    @Override
    protected String getSubtitle() {
        return "Payments received, refunds, expenses and customs duty movements";
    }

    @Override
    protected boolean matchesSearch(Transaction t, String q) {
        return contains(t.getTransactionCode(), q) || contains(t.getDescription(), q)
                || contains(services.customers().nameOf(t.getCustomerId()), q)
                || contains(t.getType().toString(), q);
    }

    private boolean contains(String value, String q) {
        return value != null && value.toLowerCase().contains(q);
    }

    @Override
    protected void handleAdd() {
        TransactionDialog.showAdd(services.transactions(), services.customers().getAll(), services.shipments().getAll());
    }

    @Override
    protected void handleEdit(Transaction item) {
        if (TransactionDialog.showEdit(item, services.customers().getAll(), services.shipments().getAll())) {
            services.transactions().update(item);
        }
    }

    @Override
    protected void handleDelete(Transaction item) {
        services.transactions().delete(item);
    }
}
