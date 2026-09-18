package com.folkcargo.model;

/**
 * A driver/operator employed by Folk Cargo Solution Pvt. Ltd. who can be
 * assigned to a vehicle for a transport run.
 */
public class Driver {

    public enum DriverStatus {
        AVAILABLE("Available"),
        ON_DUTY("On Duty"),
        ON_LEAVE("On Leave"),
        OFF_DUTY("Off Duty");

        private final String label;

        DriverStatus(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private int id;
    private String name;
    private String licenseNumber;
    private String phone;
    private int experienceYears;
    private DriverStatus status;

    public Driver() {
    }

    public Driver(int id, String name, String licenseNumber, String phone,
                   int experienceYears, DriverStatus status) {
        this.id = id;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.phone = phone;
        this.experienceYears = experienceYears;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name + " (" + licenseNumber + ")";
    }
}
