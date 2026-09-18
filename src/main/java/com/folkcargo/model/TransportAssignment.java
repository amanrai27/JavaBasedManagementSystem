package com.folkcargo.model;

import java.time.LocalDate;

/**
 * Links a {@link Shipment} to the {@link Vehicle} and {@link Driver} that
 * physically move it, along with the route and schedule.
 */
public class TransportAssignment {

    public enum TransportStatus {
        SCHEDULED("Scheduled"),
        ONGOING("Ongoing"),
        COMPLETED("Completed"),
        DELAYED("Delayed"),
        CANCELLED("Cancelled");

        private final String label;

        TransportStatus(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private int id;
    private int shipmentId;
    private int vehicleId;
    private int driverId;
    private String route;
    private double distanceKm;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private TransportStatus status;

    public TransportAssignment() {
    }

    public TransportAssignment(int id, int shipmentId, int vehicleId, int driverId, String route,
                                double distanceKm, LocalDate departureDate, LocalDate arrivalDate,
                                TransportStatus status) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.route = route;
        this.distanceKm = distanceKm;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public TransportStatus getStatus() {
        return status;
    }

    public void setStatus(TransportStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return route + " [" + status + "]";
    }
}
