package com.folkcargo.service;

import com.folkcargo.data.DataStore;
import com.folkcargo.model.Transaction;
import com.folkcargo.model.Transaction.PaymentMethod;
import com.folkcargo.model.Transaction.TransactionType;

import java.time.LocalDate;

public class TransactionService extends AbstractCrudService<Transaction> {

    private final DataStore store;

    public TransactionService(DataStore store) {
        super(store.getTransactions(), Transaction::getId);
        this.store = store;
    }

    public Transaction create(Integer shipmentId, int customerId, TransactionType type, double amount,
                               PaymentMethod method, LocalDate date, String description) {
        int id = store.nextTransactionId();
        String code = String.format("FCS-TXN-%05d", id);
        Transaction t = new Transaction(id, code, shipmentId, customerId, type, amount, method, date, description);
        add(t);
        return t;
    }

    public double totalByType(TransactionType type) {
        return getAll().stream().filter(t -> t.getType() == type).mapToDouble(Transaction::getAmount).sum();
    }

    public double netRevenue() {
        return totalByType(TransactionType.PAYMENT_RECEIVED)
                - totalByType(TransactionType.REFUND)
                - totalByType(TransactionType.EXPENSE)
                - totalByType(TransactionType.CUSTOMS_DUTY);
    }
}
