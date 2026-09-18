package com.folkcargo.model;

import java.time.LocalDate;

/**
 * A billing document raised against a customer for a shipment, with tax and
 * payment status tracking.
 */
public class Invoice {

    public enum InvoiceStatus {
        DRAFT("Draft"),
        PENDING("Pending"),
        PAID("Paid"),
        OVERDUE("Overdue"),
        CANCELLED("Cancelled");

        private final String label;

        InvoiceStatus(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private int id;
    private String invoiceNumber;
    private int customerId;
    private int shipmentId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private double subtotal;
    private double taxAmount;
    private InvoiceStatus status;

    public Invoice() {
    }

    public Invoice(int id, String invoiceNumber, int customerId, int shipmentId, LocalDate issueDate,
                    LocalDate dueDate, double subtotal, double taxAmount, InvoiceStatus status) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.customerId = customerId;
        this.shipmentId = shipmentId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getTotalAmount() {
        return subtotal + taxAmount;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return invoiceNumber + " (" + status + ")";
    }
}
