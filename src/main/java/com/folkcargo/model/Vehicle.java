package com.folkcargo.model;

/**
 * A vehicle/vessel/aircraft in the Folk Cargo Solution fleet used to move
 * cargo between origin and destination on a transport assignment.
 */
public class Vehicle {

    public enum VehicleType {
        TRUCK("Truck"),
        CONTAINER_TRAILER("Container Trailer"),
        CARGO_SHIP("Cargo Ship"),
        AIRCRAFT("Cargo Aircraft"),
        RAIL_WAGON("Rail Wagon");

        private final String label;

        VehicleType(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public enum VehicleStatus {
        AVAILABLE("Available"),
        IN_TRANSIT("In Transit"),
        MAINTENANCE("Under Maintenance"),
        OUT_OF_SERVICE("Out of Service");

        private final String label;

        VehicleStatus(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private int id;
    private String vehicleNumber;
    private VehicleType type;
    private double capacityTons;
    private VehicleStatus status;
    private Integer assignedDriverId;

    public Vehicle() {
    }

    public Vehicle(int id, String vehicleNumber, VehicleType type, double capacityTons,
                    VehicleStatus status, Integer assignedDriverId) {
        this.id = id;
        this.vehicleNumber = vehicleNumber;
        this.type = type;
        this.capacityTons = capacityTons;
        this.status = status;
        this.assignedDriverId = assignedDriverId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public double getCapacityTons() {
        return capacityTons;
    }

    public void setCapacityTons(double capacityTons) {
        this.capacityTons = capacityTons;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public Integer getAssignedDriverId() {
        return assignedDriverId;
    }

    public void setAssignedDriverId(Integer assignedDriverId) {
        this.assignedDriverId = assignedDriverId;
    }

    @Override
    public String toString() {
        return vehicleNumber + " - " + type;
    }
}
