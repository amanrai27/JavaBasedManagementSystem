package com.folkcargo.data;

import com.folkcargo.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Central in-memory "database" for the application. A single instance
 * ({@link #getInstance()}) holds every entity collection as an
 * {@link ObservableList} so that JavaFX {@code TableView}s stay in sync
 * automatically whenever a service adds, edits or removes a record.
 *
 * <p>This is intentionally a simple in-memory store (no JDBC/JPA) since the
 * application currently runs entirely on generated dummy data. Swapping it
 * for a real database later only means changing this class and the
 * {@code service} layer that sits on top of it — the UI layer does not need
 * to change.</p>
 */
public final class DataStore {

    private static final DataStore INSTANCE = new DataStore();

    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private final ObservableList<Driver> drivers = FXCollections.observableArrayList();
    private final ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();
    private final ObservableList<Shipment> shipments = FXCollections.observableArrayList();
    private final ObservableList<TransportAssignment> transportAssignments = FXCollections.observableArrayList();
    private final ObservableList<ExportRecord> exportRecords = FXCollections.observableArrayList();
    private final ObservableList<ImportRecord> importRecords = FXCollections.observableArrayList();
    private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    private final ObservableList<Invoice> invoices = FXCollections.observableArrayList();

    private final AtomicInteger customerIdSeq = new AtomicInteger(0);
    private final AtomicInteger driverIdSeq = new AtomicInteger(0);
    private final AtomicInteger vehicleIdSeq = new AtomicInteger(0);
    private final AtomicInteger shipmentIdSeq = new AtomicInteger(0);
    private final AtomicInteger transportIdSeq = new AtomicInteger(0);
    private final AtomicInteger exportIdSeq = new AtomicInteger(0);
    private final AtomicInteger importIdSeq = new AtomicInteger(0);
    private final AtomicInteger transactionIdSeq = new AtomicInteger(0);
    private final AtomicInteger invoiceIdSeq = new AtomicInteger(0);

    private boolean seeded = false;

    private DataStore() {
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    public synchronized void ensureSeeded() {
        if (!seeded) {
            seeded = true;
            DummyDataGenerator.seed(this);
        }
    }

    // ---- Collections -----------------------------------------------------

    public ObservableList<Customer> getCustomers() {
        return customers;
    }

    public ObservableList<Driver> getDrivers() {
        return drivers;
    }

    public ObservableList<Vehicle> getVehicles() {
        return vehicles;
    }

    public ObservableList<Shipment> getShipments() {
        return shipments;
    }

    public ObservableList<TransportAssignment> getTransportAssignments() {
        return transportAssignments;
    }

    public ObservableList<ExportRecord> getExportRecords() {
        return exportRecords;
    }

    public ObservableList<ImportRecord> getImportRecords() {
        return importRecords;
    }

    public ObservableList<Transaction> getTransactions() {
        return transactions;
    }

    public ObservableList<Invoice> getInvoices() {
        return invoices;
    }

    // ---- ID sequences ------------------------------------------------------

    public int nextCustomerId() {
        return customerIdSeq.incrementAndGet();
    }

    public int nextDriverId() {
        return driverIdSeq.incrementAndGet();
    }

    public int nextVehicleId() {
        return vehicleIdSeq.incrementAndGet();
    }

    public int nextShipmentId() {
        return shipmentIdSeq.incrementAndGet();
    }

    public int nextTransportId() {
        return transportIdSeq.incrementAndGet();
    }

    public int nextExportId() {
        return exportIdSeq.incrementAndGet();
    }

    public int nextImportId() {
        return importIdSeq.incrementAndGet();
    }

    public int nextTransactionId() {
        return transactionIdSeq.incrementAndGet();
    }

    public int nextInvoiceId() {
        return invoiceIdSeq.incrementAndGet();
    }
}
