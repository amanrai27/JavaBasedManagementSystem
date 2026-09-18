package com.folkcargo.service;

import com.folkcargo.data.DataStore;

/**
 * Wires up a single instance of every service on top of the shared
 * {@link DataStore} and hands them out to the UI layer. Keeping one
 * registry avoids every view having to know how services are constructed.
 */
public final class ServiceRegistry {

    private static final ServiceRegistry INSTANCE = new ServiceRegistry();

    private final DataStore dataStore;
    private final CustomerService customerService;
    private final DriverService driverService;
    private final VehicleService vehicleService;
    private final ShipmentService shipmentService;
    private final TransportService transportService;
    private final ExportService exportService;
    private final ImportService importService;
    private final TransactionService transactionService;
    private final InvoiceService invoiceService;
    private final ReportService reportService;

    private ServiceRegistry() {
        this.dataStore = DataStore.getInstance();
        this.dataStore.ensureSeeded();
        this.customerService = new CustomerService(dataStore);
        this.driverService = new DriverService(dataStore);
        this.vehicleService = new VehicleService(dataStore);
        this.shipmentService = new ShipmentService(dataStore);
        this.transportService = new TransportService(dataStore);
        this.exportService = new ExportService(dataStore);
        this.importService = new ImportService(dataStore);
        this.transactionService = new TransactionService(dataStore);
        this.invoiceService = new InvoiceService(dataStore);
        this.reportService = new ReportService(this);
    }

    public static ServiceRegistry getInstance() {
        return INSTANCE;
    }

    public DataStore getDataStore() {
        return dataStore;
    }

    public CustomerService customers() {
        return customerService;
    }

    public DriverService drivers() {
        return driverService;
    }

    public VehicleService vehicles() {
        return vehicleService;
    }

    public ShipmentService shipments() {
        return shipmentService;
    }

    public TransportService transport() {
        return transportService;
    }

    public ExportService exports() {
        return exportService;
    }

    public ImportService imports() {
        return importService;
    }

    public TransactionService transactions() {
        return transactionService;
    }

    public InvoiceService invoices() {
        return invoiceService;
    }

    public ReportService reports() {
        return reportService;
    }
}
