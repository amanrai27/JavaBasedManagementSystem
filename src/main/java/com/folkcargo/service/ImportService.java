package com.folkcargo.service;

import com.folkcargo.data.DataStore;
import com.folkcargo.model.CustomsStatus;
import com.folkcargo.model.ImportRecord;

import java.time.LocalDate;

public class ImportService extends AbstractCrudService<ImportRecord> {

    private final DataStore store;

    public ImportService(DataStore store) {
        super(store.getImportRecords(), ImportRecord::getId);
        this.store = store;
    }

    public ImportRecord create(int shipmentId, String importLicenseNumber, String originCountry,
                                String portOfEntry, String hsCode, LocalDate importDate, double dutyAmount,
                                CustomsStatus customsStatus) {
        ImportRecord im = new ImportRecord(store.nextImportId(), shipmentId, importLicenseNumber,
                originCountry, portOfEntry, hsCode, importDate, dutyAmount, customsStatus);
        add(im);
        return im;
    }
}
