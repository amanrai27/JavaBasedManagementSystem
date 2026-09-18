package com.folkcargo.service;

import com.folkcargo.data.DataStore;
import com.folkcargo.model.CustomsStatus;
import com.folkcargo.model.ExportRecord;

import java.time.LocalDate;

public class ExportService extends AbstractCrudService<ExportRecord> {

    private final DataStore store;

    public ExportService(DataStore store) {
        super(store.getExportRecords(), ExportRecord::getId);
        this.store = store;
    }

    public ExportRecord create(int shipmentId, String exportLicenseNumber, String destinationCountry,
                                String portOfExit, String hsCode, LocalDate exportDate, double dutyAmount,
                                CustomsStatus customsStatus) {
        ExportRecord e = new ExportRecord(store.nextExportId(), shipmentId, exportLicenseNumber,
                destinationCountry, portOfExit, hsCode, exportDate, dutyAmount, customsStatus);
        add(e);
        return e;
    }
}
