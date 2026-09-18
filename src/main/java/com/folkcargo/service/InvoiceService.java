package com.folkcargo.service;

import com.folkcargo.data.DataStore;
import com.folkcargo.model.Invoice;
import com.folkcargo.model.Invoice.InvoiceStatus;

import java.time.LocalDate;

public class InvoiceService extends AbstractCrudService<Invoice> {

    private final DataStore store;

    public InvoiceService(DataStore store) {
        super(store.getInvoices(), Invoice::getId);
        this.store = store;
    }

    public Invoice create(int customerId, int shipmentId, LocalDate issueDate, LocalDate dueDate,
                           double subtotal, double taxAmount, InvoiceStatus status) {
        int id = store.nextInvoiceId();
        String number = String.format("FCS-INV-%05d", id);
        Invoice inv = new Invoice(id, number, customerId, shipmentId, issueDate, dueDate, subtotal, taxAmount, status);
        add(inv);
        return inv;
    }

    public double totalOutstanding() {
        return getAll().stream()
                .filter(i -> i.getStatus() == InvoiceStatus.PENDING || i.getStatus() == InvoiceStatus.OVERDUE)
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
    }

    public double totalCollected() {
        return getAll().stream()
                .filter(i -> i.getStatus() == InvoiceStatus.PAID)
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
    }
}
