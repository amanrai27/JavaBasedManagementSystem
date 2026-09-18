package com.folkcargo.ui;

import com.folkcargo.model.Invoice;
import com.folkcargo.service.ServiceRegistry;
import com.folkcargo.ui.dialogs.InvoiceDialog;
import com.folkcargo.ui.util.UIUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

import java.util.List;

public class InvoiceView extends CrudView<Invoice> {

    private final ServiceRegistry services;

    public InvoiceView(ServiceRegistry services) {
        super(services.invoices().getAll());
        this.services = services;

        TableColumn<Invoice, String> numberCol = UIUtils.column("Invoice No.", "invoiceNumber", 120);
        TableColumn<Invoice, String> customerCol = UIUtils.column("Customer", 170, c -> new SimpleStringProperty(services.customers().nameOf(c.getValue().getCustomerId())));
        TableColumn<Invoice, String> shipmentCol = UIUtils.column("Shipment", 110, c -> new SimpleStringProperty(services.shipments().codeOf(c.getValue().getShipmentId())));
        TableColumn<Invoice, String> issueCol = UIUtils.column("Issue Date", 100, c -> new SimpleStringProperty(UIUtils.formatDate(c.getValue().getIssueDate())));
        TableColumn<Invoice, String> dueCol = UIUtils.column("Due Date", 100, c -> new SimpleStringProperty(UIUtils.formatDate(c.getValue().getDueDate())));
        TableColumn<Invoice, String> subtotalCol = UIUtils.column("Subtotal", 110, c -> new SimpleStringProperty(UIUtils.formatCurrency(c.getValue().getSubtotal())));
        TableColumn<Invoice, String> taxCol = UIUtils.column("Tax", 100, c -> new SimpleStringProperty(UIUtils.formatCurrency(c.getValue().getTaxAmount())));
        TableColumn<Invoice, String> totalCol = UIUtils.column("Total", 110, c -> new SimpleStringProperty(UIUtils.formatCurrency(c.getValue().getTotalAmount())));
        TableColumn<Invoice, String> statusCol = UIUtils.column("Status", 100, c -> new SimpleStringProperty(c.getValue().getStatus().toString()));

        setColumns(List.of(numberCol, customerCol, shipmentCol, issueCol, dueCol, subtotalCol, taxCol, totalCol, statusCol));
    }

    @Override
    protected String getTitle() {
        return "Invoices & Payments";
    }

    @Override
    protected String getSubtitle() {
        return "Billing raised against customers, with tax and payment status tracking";
    }

    @Override
    protected boolean matchesSearch(Invoice inv, String q) {
        return contains(inv.getInvoiceNumber(), q) || contains(services.customers().nameOf(inv.getCustomerId()), q)
                || contains(services.shipments().codeOf(inv.getShipmentId()), q) || contains(inv.getStatus().toString(), q);
    }

    private boolean contains(String value, String q) {
        return value != null && value.toLowerCase().contains(q);
    }

    @Override
    protected void handleAdd() {
        InvoiceDialog.showAdd(services.invoices(), services.customers().getAll(), services.shipments().getAll());
    }

    @Override
    protected void handleEdit(Invoice item) {
        if (InvoiceDialog.showEdit(item, services.customers().getAll(), services.shipments().getAll())) {
            services.invoices().update(item);
        }
    }

    @Override
    protected void handleDelete(Invoice item) {
        services.invoices().delete(item);
    }
}
