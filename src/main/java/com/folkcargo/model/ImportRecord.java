package com.folkcargo.model;

import java.time.LocalDate;

/**
 * Customs/import paperwork associated with a {@link Shipment} of type
 * {@code IMPORT} entering the country.
 */
public class ImportRecord {

    private int id;
    private int shipmentId;
    private String importLicenseNumber;
    private String originCountry;
    private String portOfEntry;
    private String hsCode;
    private LocalDate importDate;
    private double dutyAmount;
    private CustomsStatus customsStatus;

    public ImportRecord() {
    }

    public ImportRecord(int id, int shipmentId, String importLicenseNumber, String originCountry,
                         String portOfEntry, String hsCode, LocalDate importDate, double dutyAmount,
                         CustomsStatus customsStatus) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.importLicenseNumber = importLicenseNumber;
        this.originCountry = originCountry;
        this.portOfEntry = portOfEntry;
        this.hsCode = hsCode;
        this.importDate = importDate;
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

    public String getImportLicenseNumber() {
        return importLicenseNumber;
    }

    public void setImportLicenseNumber(String importLicenseNumber) {
        this.importLicenseNumber = importLicenseNumber;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getPortOfEntry() {
        return portOfEntry;
    }

    public void setPortOfEntry(String portOfEntry) {
        this.portOfEntry = portOfEntry;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public LocalDate getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDate importDate) {
        this.importDate = importDate;
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
        return importLicenseNumber + " <- " + originCountry;
    }
}
