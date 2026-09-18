package com.folkcargo.ui;

import com.folkcargo.model.Shipment.ShipmentStatus;
import com.folkcargo.service.ReportService;
import com.folkcargo.service.ReportService.NamedValue;
import com.folkcargo.service.ServiceRegistry;
import com.folkcargo.ui.util.UIUtils;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.Map;

/**
 * Landing screen of the application: at-a-glance KPIs plus a handful of
 * charts summarising shipment volume, revenue trend, fleet utilisation and
 * top customers, all computed live from {@link ReportService}.
 */
public class DashboardView extends BorderPane {

    private final ServiceRegistry services;
    private final ReportService reports;

    public DashboardView(ServiceRegistry services) {
        this.services = services;
        this.reports = services.reports();
        getStyleClass().add("dashboard-view");

        VBox content = new VBox(22);
        content.setPadding(new Insets(22, 26, 26, 26));

        content.getChildren().add(buildHeader());
        content.getChildren().add(buildKpiRow());
        content.getChildren().add(buildChartsRowOne());
        content.getChildren().add(buildChartsRowTwo());

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("dashboard-scroll");
        setCenter(scrollPane);
    }

    private VBox buildHeader() {
        Label title = new Label("Dashboard");
        title.getStyleClass().add("view-title");
        Label subtitle = new Label("Live operational and financial snapshot for Folk Cargo Solution Pvt. Ltd.");
        subtitle.getStyleClass().add("view-subtitle");
        return new VBox(4, title, subtitle);
    }

    private FlowPane buildKpiRow() {
        FlowPane row = new FlowPane(16, 16);
        row.getChildren().addAll(
                kpiCard("Total Customers", String.valueOf(reports.totalCustomers()), "registered clients", "kpi-blue"),
                kpiCard("Total Shipments", String.valueOf(reports.totalShipments()), "all-time bookings", "kpi-purple"),
                kpiCard("Fleet Size", reports.totalVehicles() + " vehicles", reports.totalDrivers() + " drivers on roster", "kpi-teal"),
                kpiCard("Active Transport Runs", String.valueOf(reports.activeTransportRuns()), "currently ongoing", "kpi-orange"),
                kpiCard("Revenue Collected", UIUtils.formatCurrency(reports.totalRevenue()), "total payments received", "kpi-green"),
                kpiCard("Outstanding", UIUtils.formatCurrency(reports.totalOutstanding()), "pending + overdue invoices", "kpi-red"),
                kpiCard("Pending Exports", String.valueOf(reports.pendingExports()), "not yet cleared customs", "kpi-orange"),
                kpiCard("Pending Imports", String.valueOf(reports.pendingImports()), "not yet cleared customs", "kpi-orange"),
                kpiCard("Fleet Utilization", reports.fleetUtilizationPercent() + "%", "vehicles currently in transit", "kpi-teal")
        );
        return row;
    }

    private VBox kpiCard(String label, String value, String caption, String accentClass) {
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("kpi-value");
        Label nameLabel = new Label(label);
        nameLabel.getStyleClass().add("kpi-label");
        Label captionLabel = new Label(caption);
        captionLabel.getStyleClass().add("kpi-caption");

        VBox card = new VBox(4, nameLabel, valueLabel, captionLabel);
        card.getStyleClass().addAll("kpi-card", accentClass);
        card.setPrefWidth(230);
        card.setPadding(new Insets(16, 18, 16, 18));
        return card;
    }

    private HBox buildChartsRowOne() {
        PieChart pie = buildShipmentStatusChart();
        BarChart<String, Number> typeChart = buildShipmentTypeChart();
        HBox row = new HBox(16, wrapChart("Shipments by Status", pie), wrapChart("Shipments by Type", typeChart));
        HBox.setHgrow(pie, Priority.ALWAYS);
        HBox.setHgrow(typeChart, Priority.ALWAYS);
        return row;
    }

    private HBox buildChartsRowTwo() {
        LineChart<String, Number> revenueChart = buildMonthlyRevenueChart();
        BarChart<String, Number> topCustomersChart = buildTopCustomersChart();
        HBox row = new HBox(16, wrapChart("Monthly Revenue Trend", revenueChart), wrapChart("Top Customers by Revenue", topCustomersChart));
        HBox.setHgrow(revenueChart, Priority.ALWAYS);
        HBox.setHgrow(topCustomersChart, Priority.ALWAYS);
        return row;
    }

    private VBox wrapChart(String title, javafx.scene.chart.Chart chart) {
        Label label = new Label(title);
        label.getStyleClass().add("chart-title");
        chart.setLegendVisible(chart instanceof PieChart);
        chart.setAnimated(false);
        VBox box = new VBox(8, label, chart);
        box.getStyleClass().add("chart-card");
        box.setPadding(new Insets(16));
        box.setPrefWidth(480);
        VBox.setVgrow(chart, Priority.ALWAYS);
        return box;
    }

    private PieChart buildShipmentStatusChart() {
        PieChart chart = new PieChart();
        Map<ShipmentStatus, Long> data = reports.shipmentsByStatus();
        for (Map.Entry<ShipmentStatus, Long> e : data.entrySet()) {
            if (e.getValue() > 0) {
                chart.getData().add(new PieChart.Data(e.getKey().toString() + " (" + e.getValue() + ")", e.getValue()));
            }
        }
        return chart;
    }

    private BarChart<String, Number> buildShipmentTypeChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Type");
        yAxis.setLabel("Shipments");
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Shipments");
        reports.shipmentsByType().forEach((type, count) -> series.getData().add(new XYChart.Data<>(type.toString(), count)));
        chart.getData().add(series);
        return chart;
    }

    private LineChart<String, Number> buildMonthlyRevenueChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");
        yAxis.setLabel("Revenue (₹)");
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Payments Received");
        Map<String, Double> monthly = reports.monthlyRevenue();
        if (monthly.isEmpty()) {
            series.getData().add(new XYChart.Data<>("No data", 0));
        } else {
            monthly.forEach((month, amount) -> series.getData().add(new XYChart.Data<>(month, amount)));
        }
        chart.getData().add(series);
        return chart;
    }

    private BarChart<String, Number> buildTopCustomersChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Customer");
        yAxis.setLabel("Revenue (₹)");
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setCategoryGap(20);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue");
        for (NamedValue nv : reports.topCustomersByRevenue(6)) {
            series.getData().add(new XYChart.Data<>(nv.name(), nv.value()));
        }
        chart.getData().add(series);
        return chart;
    }
}
