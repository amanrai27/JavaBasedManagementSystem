package com.folkcargo.model;

import java.time.LocalDate;

/**
 * A single cargo shipment booked by a customer. A shipment may be purely
 * domestic, or it may be the subject of an {@link ExportRecord} or an
 * {@link ImportRecord} when it crosses a border.
 */
public class Shipment {

    public enum ShipmentType {
        DOMESTIC("Domestic"),
        EXPORT("Export"),
        IMPORT("Import");

        private final String label;

        ShipmentType(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public enum ShipmentStatus {
        PENDING("Pending"),
        CONFIRMED("Confirmed"),
        IN_TRANSIT("In Transit"),
        DELIVERED("Delivered"),
        DELAYED("Delayed"),
        CANCELLED("Cancelled");

        private final String label;

        ShipmentStatus(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private int id;
    private String shipmentCode;
    private int customerId;
    private String origin;
    private String destination;
    private ShipmentType shipmentType;
    private String cargoDescription;
    private double weightKg;
    private ShipmentStatus status;
    private LocalDate createdDate;
    private LocalDate expectedDeliveryDate;
    private LocalDate actualDeliveryDate;

    public Shipment() {
    }

    public Shipment(int id, String shipmentCode, int customerId, String origin, String destination,
                     ShipmentType shipmentType, String cargoDescription, double weightKg,
                     ShipmentStatus status, LocalDate createdDate, LocalDate expectedDeliveryDate,
                     LocalDate actualDeliveryDate) {
        this.id = id;
        this.shipmentCode = shipmentCode;
        this.customerId = customerId;
        this.origin = origin;
        this.destination = destination;
        this.shipmentType = shipmentType;
        this.cargoDescription = cargoDescription;
        this.weightKg = weightKg;
        this.status = status;
        this.createdDate = createdDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.actualDeliveryDate = actualDeliveryDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShipmentCode() {
        return shipmentCode;
    }

    public void setShipmentCode(String shipmentCode) {
        this.shipmentCode = shipmentCode;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public ShipmentType getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(ShipmentType shipmentType) {
        this.shipmentType = shipmentType;
    }

    public String getCargoDescription() {
        return cargoDescription;
    }

    public void setCargoDescription(String cargoDescription) {
        this.cargoDescription = cargoDescription;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public LocalDate getActualDeliveryDate() {
        return actualDeliveryDate;
    }

    public void setActualDeliveryDate(LocalDate actualDeliveryDate) {
        this.actualDeliveryDate = actualDeliveryDate;
    }

    @Override
    public String toString() {
        return shipmentCode + " (" + origin + " -> " + destination + ")";
    }
}
