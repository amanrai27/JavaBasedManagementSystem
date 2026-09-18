package com.folkcargo.data;

import com.folkcargo.model.*;
import com.folkcargo.model.Customer.CustomerType;
import com.folkcargo.model.Driver.DriverStatus;
import com.folkcargo.model.Invoice.InvoiceStatus;
import com.folkcargo.model.Shipment.ShipmentStatus;
import com.folkcargo.model.Shipment.ShipmentType;
import com.folkcargo.model.Transaction.PaymentMethod;
import com.folkcargo.model.Transaction.TransactionType;
import com.folkcargo.model.TransportAssignment.TransportStatus;
import com.folkcargo.model.Vehicle.VehicleStatus;
import com.folkcargo.model.Vehicle.VehicleType;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

/**
 * Populates a {@link DataStore} with realistic-looking, internally
 * consistent sample data so the whole application can be explored end to
 * end without a real database. All IDs are cross-referenced correctly
 * (e.g. every {@link TransportAssignment} points at a real shipment,
 * vehicle and driver).
 */
final class DummyDataGenerator {

    private static final Random RNG = new Random(42);

    private DummyDataGenerator() {
    }

    static void seed(DataStore store) {
        List<Customer> customers = seedCustomers(store);
        List<Driver> drivers = seedDrivers(store);
        List<Vehicle> vehicles = seedVehicles(store, drivers);
        List<Shipment> shipments = seedShipments(store, customers);
        seedTransportAssignments(store, shipments, vehicles, drivers);
        seedExportImportRecords(store, shipments);
        List<Invoice> invoices = seedInvoices(store, shipments, customers);
        seedTransactions(store, shipments, customers, invoices);
    }

    private static List<Customer> seedCustomers(DataStore store) {
        Object[][] rows = {
                {"Ravi Kumar", "Kumar Textiles Pvt Ltd", CustomerType.BUSINESS, "ravi.kumar@kumartextiles.in", "+91-9820011223", "MIDC Industrial Area", "Mumbai", "India"},
                {"Anjali Deshmukh", "", CustomerType.INDIVIDUAL, "anjali.deshmukh@gmail.com", "+91-9822334455", "Koregaon Park", "Pune", "India"},
                {"Global Spices Exports", "Global Spices Exports LLP", CustomerType.BUSINESS, "ops@globalspices.com", "+91-4424455667", "Anna Salai", "Chennai", "India"},
                {"Meera Iyer", "", CustomerType.INDIVIDUAL, "meera.iyer@yahoo.com", "+91-9840099887", "T Nagar", "Chennai", "India"},
                {"Silverline Auto Parts", "Silverline Auto Parts Ltd", CustomerType.BUSINESS, "logistics@silverlineauto.com", "+91-1204455332", "Sector 58", "Gurugram", "India"},
                {"Oceanic Traders Pte", "Oceanic Traders Pte Ltd", CustomerType.BUSINESS, "trade@oceanictraders.sg", "+65-64221190", "Raffles Place", "Singapore", "Singapore"},
                {"Rahul Verma", "", CustomerType.INDIVIDUAL, "rahul.verma91@gmail.com", "+91-9931122334", "Bistupur", "Jamshedpur", "India"},
                {"NorthStar Electronics", "NorthStar Electronics Inc", CustomerType.BUSINESS, "shipping@northstarelec.com", "+1-4155550132", "Market Street", "San Francisco", "USA"},
                {"Priya Nair", "", CustomerType.INDIVIDUAL, "priya.nair@outlook.com", "+91-9447700112", "Marine Drive", "Kochi", "India"},
                {"Emirates Freight Traders", "Emirates Freight Traders FZE", CustomerType.BUSINESS, "cargo@emiratesfreight.ae", "+971-42233445", "Jebel Ali", "Dubai", "UAE"},
                {"Sunrise Agro Foods", "Sunrise Agro Foods Pvt Ltd", CustomerType.BUSINESS, "exports@sunriseagro.in", "+91-8022110099", "Whitefield", "Bengaluru", "India"},
                {"Arjun Malhotra", "", CustomerType.INDIVIDUAL, "arjun.malhotra@gmail.com", "+91-9811223344", "Connaught Place", "New Delhi", "India"},
        };
        for (Object[] r : rows) {
            Customer c = new Customer(
                    store.nextCustomerId(),
                    (String) r[0],
                    (String) r[1],
                    (CustomerType) r[2],
                    (String) r[3],
                    (String) r[4],
                    (String) r[5],
                    (String) r[6],
                    (String) r[7],
                    LocalDate.of(2022, 1, 1).plusDays(RNG.nextInt(900))
            );
            store.getCustomers().add(c);
        }
        return store.getCustomers();
    }

    private static List<Driver> seedDrivers(DataStore store) {
        String[][] rows = {
                {"Suresh Pillai", "DL-MH-2019-0011223"},
                {"Vikram Singh", "DL-DL-2018-0033445"},
                {"Manoj Tiwari", "DL-JH-2020-0055667"},
                {"Ganesh Rao", "DL-KA-2017-0077889"},
                {"Faisal Ahmed", "DL-TN-2021-0099001"},
                {"David Fernandes", "DL-GA-2016-0112233"},
                {"Harpreet Singh", "DL-PB-2019-0223344"},
                {"Naveen Reddy", "DL-TS-2020-0334455"},
                {"Aslam Sheikh", "DL-MH-2015-0445566"},
                {"Kiran Joshi", "DL-GJ-2018-0556677"},
        };
        DriverStatus[] statuses = DriverStatus.values();
        for (String[] r : rows) {
            Driver d = new Driver(
                    store.nextDriverId(),
                    r[0],
                    r[1],
                    "+91-9" + (100000000 + RNG.nextInt(899999999) % 100000000),
                    2 + RNG.nextInt(20),
                    statuses[RNG.nextInt(statuses.length)]
            );
            store.getDrivers().add(d);
        }
        return store.getDrivers();
    }

    private static List<Vehicle> seedVehicles(DataStore store, List<Driver> drivers) {
        Object[][] rows = {
                {"MH-04-AB-1234", VehicleType.TRUCK, 12.0},
                {"MH-12-CD-5678", VehicleType.TRUCK, 16.5},
                {"GJ-01-EF-9012", VehicleType.CONTAINER_TRAILER, 28.0},
                {"TN-09-GH-3456", VehicleType.CONTAINER_TRAILER, 30.0},
                {"KA-05-IJ-7890", VehicleType.TRUCK, 10.0},
                {"FCS-VSL-OCEAN-01", VehicleType.CARGO_SHIP, 25000.0},
                {"FCS-VSL-OCEAN-02", VehicleType.CARGO_SHIP, 18000.0},
                {"FCS-AIR-CARGO-01", VehicleType.AIRCRAFT, 45.0},
                {"FCS-RAIL-WAGON-11", VehicleType.RAIL_WAGON, 60.0},
                {"DL-03-KL-2468", VehicleType.TRUCK, 14.0},
        };
        VehicleStatus[] statuses = VehicleStatus.values();
        int i = 0;
        for (Object[] r : rows) {
            Integer driverId = (i < drivers.size()) ? drivers.get(i % drivers.size()).getId() : null;
            Vehicle v = new Vehicle(
                    store.nextVehicleId(),
                    (String) r[0],
                    (VehicleType) r[1],
                    (double) r[2],
                    statuses[RNG.nextInt(statuses.length)],
                    driverId
            );
            store.getVehicles().add(v);
            i++;
        }
        return store.getVehicles();
    }

    private static List<Shipment> seedShipments(DataStore store, List<Customer> customers) {
        String[][] routes = {
                {"Mumbai, India", "Pune, India"},
                {"Chennai, India", "Singapore"},
                {"Nhava Sheva Port, India", "Rotterdam, Netherlands"},
                {"Los Angeles, USA", "Kolkata, India"},
                {"Jebel Ali, UAE", "Mumbai, India"},
                {"Bengaluru, India", "Hyderabad, India"},
                {"Kochi, India", "Colombo, Sri Lanka"},
                {"Shanghai, China", "Chennai, India"},
                {"New Delhi, India", "Jaipur, India"},
                {"Kandla Port, India", "Hamburg, Germany"},
                {"Gurugram, India", "Ahmedabad, India"},
                {"Singapore", "Jawaharlal Nehru Port, India"},
                {"Jamshedpur, India", "Kolkata, India"},
                {"Dubai, UAE", "Nhava Sheva Port, India"},
                {"San Francisco, USA", "Chennai Port, India"},
        };
        String[] cargoTypes = {
                "Cotton textile rolls", "Auto spare parts", "Spice consignment (pepper & cardamom)",
                "Consumer electronics", "Pharmaceutical supplies", "Processed food & agro products",
                "Industrial machinery parts", "Furniture & home decor", "Leather goods",
                "Chemical raw materials (non-hazardous)", "Handicrafts", "Packaged tea",
                "Steel components", "Paper and packaging material", "IT hardware"
        };
        ShipmentStatus[] statuses = ShipmentStatus.values();
        int count = 30;
        for (int i = 0; i < count; i++) {
            Customer customer = customers.get(RNG.nextInt(customers.size()));
            String[] route = routes[i % routes.length];
            ShipmentType type;
            boolean crossBorder = !route[0].split(",\\s*")[route[0].split(",\\s*").length - 1]
                    .equals(route[1].split(",\\s*")[route[1].split(",\\s*").length - 1]);
            if (!crossBorder) {
                type = ShipmentType.DOMESTIC;
            } else {
                // Treat routes leaving an Indian origin as EXPORT, arriving in India as IMPORT.
                type = route[0].contains("India") ? ShipmentType.EXPORT : ShipmentType.IMPORT;
            }
            ShipmentStatus status = statuses[RNG.nextInt(statuses.length)];
            LocalDate created = LocalDate.of(2026, 1, 1).plusDays(RNG.nextInt(250));
            LocalDate expected = created.plusDays(3 + RNG.nextInt(20));
            LocalDate actual = status == ShipmentStatus.DELIVERED ? expected.plusDays(RNG.nextInt(4) - 1) : null;

            Shipment s = new Shipment(
                    store.nextShipmentId(),
                    String.format("FCS-SHP-%04d", i + 1),
                    customer.getId(),
                    route[0],
                    route[1],
                    type,
                    cargoTypes[i % cargoTypes.length],
                    Math.round((150 + RNG.nextDouble() * 9850) * 10) / 10.0,
                    status,
                    created,
                    expected,
                    actual
            );
            store.getShipments().add(s);
        }
        return store.getShipments();
    }

    private static void seedTransportAssignments(DataStore store, List<Shipment> shipments,
                                                   List<Vehicle> vehicles, List<Driver> drivers) {
        for (Shipment s : shipments) {
            if (s.getStatus() == ShipmentStatus.PENDING && RNG.nextBoolean()) {
                continue; // Some pending shipments have not been assigned transport yet.
            }
            Vehicle vehicle = vehicles.get(RNG.nextInt(vehicles.size()));
            Driver driver = drivers.get(RNG.nextInt(drivers.size()));
            TransportStatus tStatus = switch (s.getStatus()) {
                case DELIVERED -> TransportStatus.COMPLETED;
                case IN_TRANSIT -> TransportStatus.ONGOING;
                case DELAYED -> TransportStatus.DELAYED;
                case CANCELLED -> TransportStatus.CANCELLED;
                default -> TransportStatus.SCHEDULED;
            };
            LocalDate departure = s.getCreatedDate().plusDays(1 + RNG.nextInt(3));
            LocalDate arrival = s.getExpectedDeliveryDate();
            TransportAssignment t = new TransportAssignment(
                    store.nextTransportId(),
                    s.getId(),
                    vehicle.getId(),
                    driver.getId(),
                    s.getOrigin() + " -> " + s.getDestination(),
                    Math.round((80 + RNG.nextDouble() * 9000) * 10) / 10.0,
                    departure,
                    arrival,
                    tStatus
            );
            store.getTransportAssignments().add(t);
        }
    }

    private static void seedExportImportRecords(DataStore store, List<Shipment> shipments) {
        String[] hsCodes = {"5208.11", "8708.99", "0904.11", "8471.30", "3004.90", "2101.20", "7318.15", "9403.60"};
        CustomsStatus[] statuses = CustomsStatus.values();
        int exportSeq = 1;
        int importSeq = 1;
        for (Shipment s : shipments) {
            if (s.getShipmentType() == ShipmentType.EXPORT) {
                ExportRecord e = new ExportRecord(
                        store.nextExportId(),
                        s.getId(),
                        String.format("EXP-LIC-%05d", exportSeq++),
                        s.getDestination().contains(",") ? s.getDestination().split(",\\s*")[1] : s.getDestination(),
                        pickExitPort(s.getOrigin()),
                        hsCodes[RNG.nextInt(hsCodes.length)],
                        s.getCreatedDate().plusDays(1),
                        Math.round(s.getWeightKg() * (2 + RNG.nextDouble() * 6)) / 1.0,
                        statuses[RNG.nextInt(statuses.length)]
                );
                store.getExportRecords().add(e);
            } else if (s.getShipmentType() == ShipmentType.IMPORT) {
                ImportRecord im = new ImportRecord(
                        store.nextImportId(),
                        s.getId(),
                        String.format("IMP-LIC-%05d", importSeq++),
                        s.getOrigin().contains(",") ? s.getOrigin().split(",\\s*")[1] : s.getOrigin(),
                        pickEntryPort(s.getDestination()),
                        hsCodes[RNG.nextInt(hsCodes.length)],
                        s.getCreatedDate().plusDays(1),
                        Math.round(s.getWeightKg() * (2 + RNG.nextDouble() * 6)) / 1.0,
                        statuses[RNG.nextInt(statuses.length)]
                );
                store.getImportRecords().add(im);
            }
        }
    }

    private static String pickExitPort(String origin) {
        if (origin.contains("Mumbai") || origin.contains("Nhava Sheva")) return "Nhava Sheva Port (JNPT)";
        if (origin.contains("Chennai")) return "Chennai Port";
        if (origin.contains("Kochi")) return "Kochi Port (Cochin)";
        if (origin.contains("Kandla")) return "Kandla Port";
        return "Jawaharlal Nehru Port";
    }

    private static String pickEntryPort(String destination) {
        if (destination.contains("Kolkata")) return "Kolkata Port";
        if (destination.contains("Chennai")) return "Chennai Port";
        if (destination.contains("Mumbai") || destination.contains("Nhava Sheva")) return "Nhava Sheva Port (JNPT)";
        return "Jawaharlal Nehru Port";
    }

    private static List<Invoice> seedInvoices(DataStore store, List<Shipment> shipments, List<Customer> customers) {
        int seq = 1;
        for (Shipment s : shipments) {
            if (s.getShipmentType() == null) continue;
            if (RNG.nextInt(10) == 0) continue; // a few shipments intentionally not invoiced yet
            double subtotal = Math.round(s.getWeightKg() * (8 + RNG.nextDouble() * 12)) / 1.0;
            double tax = Math.round(subtotal * 0.18 * 100) / 100.0;
            LocalDate issue = s.getCreatedDate().plusDays(1);
            LocalDate due = issue.plusDays(15);
            InvoiceStatus status;
            if (s.getStatus() == ShipmentStatus.DELIVERED) {
                status = RNG.nextBoolean() ? InvoiceStatus.PAID : InvoiceStatus.OVERDUE;
            } else if (s.getStatus() == ShipmentStatus.CANCELLED) {
                status = InvoiceStatus.CANCELLED;
            } else {
                status = RNG.nextBoolean() ? InvoiceStatus.PENDING : InvoiceStatus.DRAFT;
            }
            Invoice inv = new Invoice(
                    store.nextInvoiceId(),
                    String.format("FCS-INV-%05d", seq++),
                    s.getCustomerId(),
                    s.getId(),
                    issue,
                    due,
                    subtotal,
                    tax,
                    status
            );
            store.getInvoices().add(inv);
        }
        return store.getInvoices();
    }

    private static void seedTransactions(DataStore store, List<Shipment> shipments, List<Customer> customers,
                                          List<Invoice> invoices) {
        PaymentMethod[] methods = PaymentMethod.values();
        int seq = 1;

        for (Invoice inv : invoices) {
            if (inv.getStatus() == InvoiceStatus.PAID) {
                Transaction t = new Transaction(
                        store.nextTransactionId(),
                        String.format("FCS-TXN-%05d", seq++),
                        inv.getShipmentId(),
                        inv.getCustomerId(),
                        TransactionType.PAYMENT_RECEIVED,
                        Math.round(inv.getTotalAmount() * 100) / 100.0,
                        methods[RNG.nextInt(methods.length)],
                        inv.getDueDate().minusDays(RNG.nextInt(5)),
                        "Payment received for invoice " + inv.getInvoiceNumber()
                );
                store.getTransactions().add(t);
            } else if (inv.getStatus() == InvoiceStatus.PENDING && RNG.nextBoolean()) {
                // partial advance payment
                Transaction t = new Transaction(
                        store.nextTransactionId(),
                        String.format("FCS-TXN-%05d", seq++),
                        inv.getShipmentId(),
                        inv.getCustomerId(),
                        TransactionType.PAYMENT_RECEIVED,
                        Math.round(inv.getTotalAmount() * 0.5 * 100) / 100.0,
                        methods[RNG.nextInt(methods.length)],
                        inv.getIssueDate().plusDays(1),
                        "Advance payment for invoice " + inv.getInvoiceNumber()
                );
                store.getTransactions().add(t);
            }
        }

        // Customs duty transactions for export/import shipments
        for (ExportRecord e : store.getExportRecords()) {
            if (e.getDutyAmount() > 0) {
                Shipment s = shipments.stream().filter(sh -> sh.getId() == e.getShipmentId()).findFirst().orElse(null);
                if (s == null) continue;
                Transaction t = new Transaction(
                        store.nextTransactionId(),
                        String.format("FCS-TXN-%05d", seq++),
                        s.getId(),
                        s.getCustomerId(),
                        TransactionType.CUSTOMS_DUTY,
                        e.getDutyAmount(),
                        PaymentMethod.BANK_TRANSFER,
                        e.getExportDate(),
                        "Export customs duty for " + s.getShipmentCode()
                );
                store.getTransactions().add(t);
            }
        }
        for (ImportRecord im : store.getImportRecords()) {
            if (im.getDutyAmount() > 0) {
                Shipment s = shipments.stream().filter(sh -> sh.getId() == im.getShipmentId()).findFirst().orElse(null);
                if (s == null) continue;
                Transaction t = new Transaction(
                        store.nextTransactionId(),
                        String.format("FCS-TXN-%05d", seq++),
                        s.getId(),
                        s.getCustomerId(),
                        TransactionType.CUSTOMS_DUTY,
                        im.getDutyAmount(),
                        PaymentMethod.BANK_TRANSFER,
                        im.getImportDate(),
                        "Import customs duty for " + s.getShipmentCode()
                );
                store.getTransactions().add(t);
            }
        }

        // A handful of operating expenses and refunds not tied to a specific shipment's invoice
        String[] expenseDescriptions = {
                "Fuel and vehicle maintenance", "Warehouse rental", "Port handling charges",
                "Staff wages", "Insurance premium", "Office utilities"
        };
        for (int i = 0; i < 8; i++) {
            Customer c = customers.get(RNG.nextInt(customers.size()));
            Transaction t = new Transaction(
                    store.nextTransactionId(),
                    String.format("FCS-TXN-%05d", seq++),
                    null,
                    c.getId(),
                    TransactionType.EXPENSE,
                    Math.round((500 + RNG.nextDouble() * 15000) * 100) / 100.0,
                    methods[RNG.nextInt(methods.length)],
                    LocalDate.of(2026, 1, 1).plusDays(RNG.nextInt(250)),
                    expenseDescriptions[i % expenseDescriptions.length]
            );
            store.getTransactions().add(t);
        }

        for (int i = 0; i < 4; i++) {
            Invoice inv = invoices.get(RNG.nextInt(invoices.size()));
            Transaction t = new Transaction(
                    store.nextTransactionId(),
                    String.format("FCS-TXN-%05d", seq++),
                    inv.getShipmentId(),
                    inv.getCustomerId(),
                    TransactionType.REFUND,
                    Math.round(inv.getTotalAmount() * 0.1 * 100) / 100.0,
                    methods[RNG.nextInt(methods.length)],
                    inv.getIssueDate().plusDays(10),
                    "Partial refund for invoice " + inv.getInvoiceNumber()
            );
            store.getTransactions().add(t);
        }
    }
}
