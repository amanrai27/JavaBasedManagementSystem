package com.folkcargo.ui;

import com.folkcargo.model.Invoice.InvoiceStatus;
import com.folkcargo.model.Shipment.ShipmentStatus;
import com.folkcargo.service.ReportService;
import com.folkcargo.service.ReportService.NamedValue;
import com.folkcargo.service.ServiceRegistry;
import com.folkcargo.ui.util.UIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.Map;

/**
 * Deeper, tabular breakdown reports that complement the chart-oriented
 * {@link DashboardView}: financial summary figures and status/ranking
 * tables useful for exporting to management or printing.
 */
public class ReportView extends BorderPane {

    public record StatusCount(String status, long count) {
    }

    private final ServiceRegistry services;
    private final ReportService reports;

    public ReportView(ServiceRegistry services) {
        this.services = services;
        this.reports = services.reports();
        getStyleClass().add("report-view");

        VBox content = new VBox(22);
        content.setPadding(new Insets(22, 26, 26, 26));

        Label title = new Label("Reports");
        title.getStyleClass().add("view-title");
        Label subtitle = new Label("Financial summary and detailed breakdowns across all modules");
        subtitle.getStyleClass().add("view-subtitle");
        content.getChildren().add(new VBox(4, title, subtitle));

        content.getChildren().add(buildFinancialSummary());

        HBox tablesRow1 = new HBox(16,
                buildStatusTable("Shipments by Status", shipmentStatusData()),
                buildStatusTable("Invoices by Status", invoiceStatusData()));
        HBox.setHgrow(tablesRow1.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(tablesRow1.getChildren().get(1), Priority.ALWAYS);
        content.getChildren().add(tablesRow1);

        HBox tablesRow2 = new HBox(16,
                buildNamedValueTable("Top Customers by Revenue", reports.topCustomersByRevenue(10), true),
                buildNamedValueTable("Top Export Destination Countries", reports.shipmentCountByDestinationCountry(10), false));
        HBox.setHgrow(tablesRow2.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(tablesRow2.getChildren().get(1), Priority.ALWAYS);
        content.getChildren().add(tablesRow2);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        setCenter(scrollPane);
    }

    private GridPane buildFinancialSummary() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("summary-card");
        grid.setHgap(30);
        grid.setVgap(10);
        grid.setPadding(new Insets(18));

        addSummaryRow(grid, 0, "Revenue Collected (Payments Received)", UIUtils.formatCurrency(reports.totalRevenue()));
        addSummaryRow(grid, 1, "Operating Expenses", UIUtils.formatCurrency(reports.totalExpenses()));
        addSummaryRow(grid, 2, "Customs Duty Paid", UIUtils.formatCurrency(reports.totalCustomsDuty()));
        addSummaryRow(grid, 3, "Net Revenue", UIUtils.formatCurrency(reports.netRevenue()));
        addSummaryRow(grid, 4, "Outstanding (Pending + Overdue Invoices)", UIUtils.formatCurrency(reports.totalOutstanding()));
        addSummaryRow(grid, 5, "Fleet Utilization", reports.fleetUtilizationPercent() + "%");

        return grid;
    }

    private void addSummaryRow(GridPane grid, int row, String label, String value) {
        Label l = new Label(label);
        l.getStyleClass().add("summary-label");
        Label v = new Label(value);
        v.getStyleClass().add("summary-value");
        grid.add(l, 0, row);
        grid.add(v, 1, row);
    }

    private ObservableList<StatusCount> shipmentStatusData() {
        ObservableList<StatusCount> list = FXCollections.observableArrayList();
        for (Map.Entry<ShipmentStatus, Long> e : reports.shipmentsByStatus().entrySet()) {
            list.add(new StatusCount(e.getKey().toString(), e.getValue()));
        }
        return list;
    }

    private ObservableList<StatusCount> invoiceStatusData() {
        ObservableList<StatusCount> list = FXCollections.observableArrayList();
        for (Map.Entry<InvoiceStatus, Long> e : reports.invoicesByStatus().entrySet()) {
            list.add(new StatusCount(e.getKey().toString(), e.getValue()));
        }
        return list;
    }

    private VBox buildStatusTable(String title, ObservableList<StatusCount> data) {
        TableView<StatusCount> table = new TableView<>(data);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        TableColumn<StatusCount, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().status()));
        TableColumn<StatusCount, Number> countCol = new TableColumn<>("Count");
        countCol.setCellValueFactory(c -> new javafx.beans.property.SimpleLongProperty(c.getValue().count()));
        table.getColumns().addAll(statusCol, countCol);
        table.setPrefHeight(230);

        Label label = new Label(title);
        label.getStyleClass().add("chart-title");
        VBox box = new VBox(8, label, table);
        box.getStyleClass().add("chart-card");
        box.setPadding(new Insets(16));
        box.setPrefWidth(420);
        return box;
    }

    private VBox buildNamedValueTable(String title, java.util.List<NamedValue> data, boolean currency) {
        TableView<NamedValue> table = new TableView<>(FXCollections.observableArrayList(data));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        TableColumn<NamedValue, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().name()));
        TableColumn<NamedValue, String> valueCol = new TableColumn<>(currency ? "Revenue" : "Shipments");
        valueCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                currency ? UIUtils.formatCurrency(c.getValue().value()) : UIUtils.formatNumber(c.getValue().value())));
        table.getColumns().addAll(nameCol, valueCol);
        table.setPrefHeight(280);
        table.setPlaceholder(new Label("No data yet"));

        Label label = new Label(title);
        label.getStyleClass().add("chart-title");
        VBox box = new VBox(8, label, table);
        box.getStyleClass().add("chart-card");
        box.setPadding(new Insets(16));
        box.setPrefWidth(420);
        return box;
    }
}
