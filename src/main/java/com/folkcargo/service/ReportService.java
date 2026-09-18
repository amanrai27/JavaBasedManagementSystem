package com.folkcargo.service;

import com.folkcargo.model.Customer;
import com.folkcargo.model.CustomsStatus;
import com.folkcargo.model.Invoice.InvoiceStatus;
import com.folkcargo.model.Shipment;
import com.folkcargo.model.Shipment.ShipmentStatus;
import com.folkcargo.model.Shipment.ShipmentType;
import com.folkcargo.model.Transaction;
import com.folkcargo.model.Transaction.TransactionType;
import com.folkcargo.model.Vehicle.VehicleStatus;

import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Read-only aggregation queries over every other service, used to power the
 * Dashboard and Reports screens. Nothing here mutates data.
 */
public class ReportService {

    /** Simple (name, value) pair used for chart data such as "top customers by revenue". */
    public record NamedValue(String name, double value) {
    }

    private final ServiceRegistry services;

    public ReportService(ServiceRegistry services) {
        this.services = services;
    }

    public int totalCustomers() {
        return services.customers().count();
    }

    public int totalShipments() {
        return services.shipments().count();
    }

    public int totalVehicles() {
        return services.vehicles().count();
    }

    public int totalDrivers() {
        return services.drivers().count();
    }

    public double totalRevenue() {
        return services.transactions().totalByType(TransactionType.PAYMENT_RECEIVED);
    }

    public double totalOutstanding() {
        return services.invoices().totalOutstanding();
    }

    public double totalExpenses() {
        return services.transactions().totalByType(TransactionType.EXPENSE);
    }

    public double totalCustomsDuty() {
        return services.transactions().totalByType(TransactionType.CUSTOMS_DUTY);
    }

    public double netRevenue() {
        return services.transactions().netRevenue();
    }

    public long pendingExports() {
        return services.exports().getAll().stream()
                .filter(e -> e.getCustomsStatus() != CustomsStatus.CLEARED)
                .count();
    }

    public long pendingImports() {
        return services.imports().getAll().stream()
                .filter(i -> i.getCustomsStatus() != CustomsStatus.CLEARED)
                .count();
    }

    public int activeTransportRuns() {
        return (int) services.transport().getAll().stream()
                .filter(t -> t.getStatus() == com.folkcargo.model.TransportAssignment.TransportStatus.ONGOING)
                .count();
    }

    public double fleetUtilizationPercent() {
        int total = services.vehicles().count();
        if (total == 0) return 0;
        long busy = services.vehicles().getAll().stream()
                .filter(v -> v.getStatus() == VehicleStatus.IN_TRANSIT)
                .count();
        return Math.round((busy * 1000.0) / total) / 10.0;
    }

    public Map<ShipmentStatus, Long> shipmentsByStatus() {
        Map<ShipmentStatus, Long> map = new LinkedHashMap<>();
        for (ShipmentStatus status : ShipmentStatus.values()) {
            map.put(status, services.shipments().countByStatus(status));
        }
        return map;
    }

    public Map<ShipmentType, Long> shipmentsByType() {
        Map<ShipmentType, Long> map = new LinkedHashMap<>();
        for (ShipmentType type : ShipmentType.values()) {
            map.put(type, services.shipments().countByType(type));
        }
        return map;
    }

    public Map<InvoiceStatus, Long> invoicesByStatus() {
        Map<InvoiceStatus, Long> map = new LinkedHashMap<>();
        for (InvoiceStatus status : InvoiceStatus.values()) {
            long c = services.invoices().getAll().stream().filter(i -> i.getStatus() == status).count();
            map.put(status, c);
        }
        return map;
    }

    /** Revenue (payments received) grouped by calendar month, in chronological order. */
    public Map<String, Double> monthlyRevenue() {
        Map<String, Double> byMonth = new LinkedHashMap<>();
        List<Transaction> payments = services.transactions().getAll().stream()
                .filter(t -> t.getType() == TransactionType.PAYMENT_RECEIVED)
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .toList();
        for (Transaction t : payments) {
            String label = t.getDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + t.getDate().getYear();
            byMonth.merge(label, t.getAmount(), Double::sum);
        }
        return byMonth;
    }

    /** Top customers ranked by total payments received, descending. */
    public List<NamedValue> topCustomersByRevenue(int limit) {
        Map<Integer, Double> byCustomer = services.transactions().getAll().stream()
                .filter(t -> t.getType() == TransactionType.PAYMENT_RECEIVED)
                .collect(Collectors.groupingBy(Transaction::getCustomerId, Collectors.summingDouble(Transaction::getAmount)));

        return byCustomer.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(e -> {
                    Customer c = services.customers().findById(e.getKey()).orElse(null);
                    String name = c != null ? c.getDisplayName() : ("Customer #" + e.getKey());
                    return new NamedValue(name, Math.round(e.getValue() * 100) / 100.0);
                })
                .toList();
    }

    public List<NamedValue> shipmentCountByDestinationCountry(int limit) {
        Map<String, Long> byCountry = services.shipments().getAll().stream()
                .filter(s -> s.getShipmentType() == ShipmentType.EXPORT)
                .collect(Collectors.groupingBy(this::lastCommaSegment, Collectors.counting()));
        return byCountry.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(e -> new NamedValue(e.getKey(), e.getValue()))
                .toList();
    }

    private String lastCommaSegment(Shipment s) {
        String dest = s.getDestination();
        String[] parts = dest.split(",\\s*");
        return parts[parts.length - 1];
    }
}
