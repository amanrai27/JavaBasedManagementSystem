package com.folkcargo.ui;

import com.folkcargo.model.ExportRecord;
import com.folkcargo.service.ServiceRegistry;
import com.folkcargo.ui.dialogs.ExportDialog;
import com.folkcargo.ui.util.UIUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

import java.util.List;

public class ExportView extends CrudView<ExportRecord> {

    private final ServiceRegistry services;

    public ExportView(ServiceRegistry services) {
        super(services.exports().getAll());
        this.services = services;

        TableColumn<ExportRecord, String> shipmentCol = UIUtils.column("Shipment", 110, c -> new SimpleStringProperty(services.shipments().codeOf(c.getValue().getShipmentId())));
        TableColumn<ExportRecord, String> licenseCol = UIUtils.column("License No.", "exportLicenseNumber", 130);
        TableColumn<ExportRecord, String> destCol = UIUtils.column("Destination Country", "destinationCountry", 150);
        TableColumn<ExportRecord, String> portCol = UIUtils.column("Port of Exit", "portOfExit", 170);
        TableColumn<ExportRecord, String> hsCol = UIUtils.column("HS Code", "hsCode", 90);
        TableColumn<ExportRecord, String> dateCol = UIUtils.column("Export Date", 100, c -> new SimpleStringProperty(UIUtils.formatDate(c.getValue().getExportDate())));
        TableColumn<ExportRecord, String> dutyCol = UIUtils.column("Duty", 110, c -> new SimpleStringProperty(UIUtils.formatCurrency(c.getValue().getDutyAmount())));
        TableColumn<ExportRecord, String> statusCol = UIUtils.column("Customs Status", 130, c -> new SimpleStringProperty(c.getValue().getCustomsStatus().toString()));

        setColumns(List.of(shipmentCol, licenseCol, destCol, portCol, hsCol, dateCol, dutyCol, statusCol));
    }

    @Override
    protected String getTitle() {
        return "Export";
    }

    @Override
    protected String getSubtitle() {
        return "Customs and export clearance paperwork for shipments leaving the country";
    }

    @Override
    protected boolean matchesSearch(ExportRecord e, String q) {
        return contains(e.getExportLicenseNumber(), q) || contains(e.getDestinationCountry(), q)
                || contains(e.getPortOfExit(), q) || contains(e.getHsCode(), q)
                || contains(services.shipments().codeOf(e.getShipmentId()), q);
    }

    private boolean contains(String value, String q) {
        return value != null && value.toLowerCase().contains(q);
    }

    @Override
    protected void handleAdd() {
        ExportDialog.showAdd(services.exports(), services.shipments().getAll());
    }

    @Override
    protected void handleEdit(ExportRecord item) {
        if (ExportDialog.showEdit(item, services.shipments().getAll())) {
            services.exports().update(item);
        }
    }

    @Override
    protected void handleDelete(ExportRecord item) {
        services.exports().delete(item);
    }
}
