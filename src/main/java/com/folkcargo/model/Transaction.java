package com.folkcargo.model;

import java.time.LocalDate;

/**
 * A single financial movement (a payment received, a refund issued, an
 * operating expense, or a customs duty payment) tied to a shipment/customer.
 */
public class Transaction {

    public enum TransactionType {
        PAYMENT_RECEIVED("Payment Received"),
        REFUND("Refund"),
        EXPENSE("Expense"),
        CUSTOMS_DUTY("Customs Duty");

        private final String label;

        TransactionType(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public enum PaymentMethod {
        CASH("Cash"),
        BANK_TRANSFER("Bank Transfer"),
        CREDIT_CARD("Credit Card"),
        CHEQUE("Cheque"),
        ONLINE_WALLET("Online Wallet");

        private final String label;

        PaymentMethod(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private int id;
    private String transactionCode;
    private Integer shipmentId;
    private int customerId;
    private TransactionType type;
    private double amount;
    private PaymentMethod paymentMethod;
    private LocalDate date;
    private String description;

    public Transaction() {
    }

    public Transaction(int id, String transactionCode, Integer shipmentId, int customerId,
                        TransactionType type, double amount, PaymentMethod paymentMethod,
                        LocalDate date, String description) {
        this.id = id;
        this.transactionCode = transactionCode;
        this.shipmentId = shipmentId;
        this.customerId = customerId;
        this.type = type;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.date = date;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public Integer getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Integer shipmentId) {
        this.shipmentId = shipmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return transactionCode + " (" + type + ")";
    }
}
