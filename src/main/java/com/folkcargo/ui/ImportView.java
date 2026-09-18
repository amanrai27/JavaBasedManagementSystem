package com.folkcargo.ui;

import com.folkcargo.model.ImportRecord;
import com.folkcargo.service.ServiceRegistry;
import com.folkcargo.ui.dialogs.ImportDialog;
import com.folkcargo.ui.util.UIUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

import java.util.List;

public class ImportView extends CrudView<ImportRecord> {

    private final ServiceRegistry services;

    public ImportView(ServiceRegistry services) {
        super(services.imports().getAll());
        this.services = services;

        TableColumn<ImportRecord, String> shipmentCol = UIUtils.column("Shipment", 110, c -> new SimpleStringProperty(services.shipments().codeOf(c.getValue().getShipmentId())));
        TableColumn<ImportRecord, String> licenseCol = UIUtils.column("License No.", "importLicenseNumber", 130);
        TableColumn<ImportRecord, String> originCol = UIUtils.column("Origin Country", "originCountry", 150);
        TableColumn<ImportRecord, String> portCol = UIUtils.column("Port of Entry", "portOfEntry", 170);
        TableColumn<ImportRecord, String> hsCol = UIUtils.column("HS Code", "hsCode", 90);
        TableColumn<ImportRecord, String> dateCol = UIUtils.column("Import Date", 100, c -> new SimpleStringProperty(UIUtils.formatDate(c.getValue().getImportDate())));
        TableColumn<ImportRecord, String> dutyCol = UIUtils.column("Duty", 110, c -> new SimpleStringProperty(UIUtils.formatCurrency(c.getValue().getDutyAmount())));
        TableColumn<ImportRecord, String> statusCol = UIUtils.column("Customs Status", 130, c -> new SimpleStringProperty(c.getValue().getCustomsStatus().toString()));

        setColumns(List.of(shipmentCol, licenseCol, originCol, portCol, hsCol, dateCol, dutyCol, statusCol));
    }

    @Override
    protected String getTitle() {
        return "Import";
    }

    @Override
    protected String getSubtitle() {
        return "Customs and import clearance paperwork for shipments entering the country";
    }

    @Override
    protected boolean matchesSearch(ImportRecord im, String q) {
        return contains(im.getImportLicenseNumber(), q) || contains(im.getOriginCountry(), q)
                || contains(im.getPortOfEntry(), q) || contains(im.getHsCode(), q)
                || contains(services.shipments().codeOf(im.getShipmentId()), q);
    }

    private boolean contains(String value, String q) {
        return value != null && value.toLowerCase().contains(q);
    }

    @Override
    protected void handleAdd() {
        ImportDialog.showAdd(services.imports(), services.shipments().getAll());
    }

    @Override
    protected void handleEdit(ImportRecord item) {
        if (ImportDialog.showEdit(item, services.shipments().getAll())) {
            services.imports().update(item);
        }
    }

    @Override
    protected void handleDelete(ImportRecord item) {
        services.imports().delete(item);
    }
}
