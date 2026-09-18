package com.folkcargo.model;

import java.time.LocalDate;

/**
 * Customs/export paperwork associated with a {@link Shipment} of type
 * {@code EXPORT} leaving the country.
 */
public class ExportRecord {

    private int id;
    private int shipmentId;
    private String exportLicenseNumber;
    private String destinationCountry;
    private String portOfExit;
    private String hsCode;
    private LocalDate exportDate;
    private double dutyAmount;
    private CustomsStatus customsStatus;

    public ExportRecord() {
    }

    public ExportRecord(int id, int shipmentId, String exportLicenseNumber, String destinationCountry,
                         String portOfExit, String hsCode, LocalDate exportDate, double dutyAmount,
                         CustomsStatus customsStatus) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.exportLicenseNumber = exportLicenseNumber;
        this.destinationCountry = destinationCountry;
        this.portOfExit = portOfExit;
        this.hsCode = hsCode;
        this.exportDate = exportDate;
        this.dutyAmount = dutyAmount;
        this.customsStatus = customsStatus;
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

    public String getExportLicenseNumber() {
        return exportLicenseNumber;
    }

    public void setExportLicenseNumber(String exportLicenseNumber) {
        this.exportLicenseNumber = exportLicenseNumber;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getPortOfExit() {
        return portOfExit;
    }

    public void setPortOfExit(String portOfExit) {
        this.portOfExit = portOfExit;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public LocalDate getExportDate() {
        return exportDate;
    }

    public void setExportDate(LocalDate exportDate) {
        this.exportDate = exportDate;
    }

    public double getDutyAmount() {
        return dutyAmount;
    }

    public void setDutyAmount(double dutyAmount) {
        this.dutyAmount = dutyAmount;
    }

    public CustomsStatus getCustomsStatus() {
        return customsStatus;
    }

    public void setCustomsStatus(CustomsStatus customsStatus) {
        this.customsStatus = customsStatus;
    }

    @Override
    public String toString() {
        return exportLicenseNumber + " -> " + destinationCountry;
    }
}
