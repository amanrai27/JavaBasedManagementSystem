package com.folkcargo;

import com.folkcargo.service.ServiceRegistry;
import com.folkcargo.ui.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Entry point of the Folk Cargo Solution Pvt. Ltd. management application.
 * Builds the overall shell: a left navigation sidebar and a content area
 * that swaps between the Dashboard and the various operational modules.
 * All data shown is generated dummy data held in {@link com.folkcargo.data.DataStore}.
 */
public class MainApp extends Application {

    private final Map<String, Node> viewCache = new LinkedHashMap<>();
    private final Map<String, Button> navButtons = new LinkedHashMap<>();
    private BorderPane root;
    private ServiceRegistry services;

    @Override
    public void start(Stage stage) {
        services = ServiceRegistry.getInstance();

        root = new BorderPane();
        root.getStyleClass().add("app-root");
        root.setLeft(buildSidebar());

        selectView("Dashboard");

        Scene scene = new Scene(root, 1360, 860);
        scene.getStylesheets().add(getClass().getResource("/com/folkcargo/css/styles.css").toExternalForm());

        stage.setTitle("Folk Cargo Solution Pvt. Ltd. - Management System");
        stage.setScene(scene);
        stage.setMinWidth(1080);
        stage.setMinHeight(680);
        stage.show();
    }

    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(230);

        VBox brand = new VBox(2);
        brand.getStyleClass().add("brand-box");
        Label brandName = new Label("Folk Cargo Solution");
        brandName.getStyleClass().add("brand-name");
        Label brandSub = new Label("Private Limited");
        brandSub.getStyleClass().add("brand-sub");
        brand.getChildren().addAll(brandName, brandSub);
        brand.setPadding(new Insets(22, 18, 18, 18));

        VBox navBox = new VBox(2);
        navBox.setPadding(new Insets(6, 10, 10, 10));

        addNavSection(navBox, "OVERVIEW");
        addNavItem(navBox, "Dashboard", () -> new DashboardView(services));

        addNavSection(navBox, "OPERATIONS");
        addNavItem(navBox, "Shipments", () -> new ShipmentView(services));
        addNavItem(navBox, "Transport", () -> new TransportView(services));
        addNavItem(navBox, "Export", () -> new ExportView(services));
        addNavItem(navBox, "Import", () -> new ImportView(services));

        addNavSection(navBox, "RESOURCES");
        addNavItem(navBox, "Customers", () -> new CustomerView(services));
        addNavItem(navBox, "Fleet", () -> new FleetView(services));

        addNavSection(navBox, "FINANCE");
        addNavItem(navBox, "Transactions", () -> new TransactionView(services));
        addNavItem(navBox, "Invoices", () -> new InvoiceView(services));

        addNavSection(navBox, "INSIGHTS");
        addNavItem(navBox, "Reports", () -> new ReportView(services));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label footer = new Label("Dummy data — for demo use only");
        footer.getStyleClass().add("sidebar-footer");
        footer.setPadding(new Insets(10, 18, 16, 18));
        footer.setWrapText(true);

        sidebar.getChildren().addAll(brand, new Separator(), navBox, spacer, footer);
        return sidebar;
    }

    private void addNavSection(VBox navBox, String text) {
        Label section = new Label(text);
        section.getStyleClass().add("nav-section");
        VBox.setMargin(section, new Insets(14, 12, 4, 12));
        navBox.getChildren().add(section);
    }

    private void addNavItem(VBox navBox, String name, Supplier<Node> viewFactory) {
        Button button = new Button(name);
        button.getStyleClass().add("nav-item");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setOnAction(e -> {
            if (!viewCache.containsKey(name)) {
                viewCache.put(name, viewFactory.get());
            }
            selectView(name);
        });
        navButtons.put(name, button);
        navBox.getChildren().add(button);

        if (!viewCache.containsKey(name) && "Dashboard".equals(name)) {
            viewCache.put(name, viewFactory.get());
        }
    }

    private void selectView(String name) {
        Node view = viewCache.get(name);
        if (view == null) return;
        root.setCenter(view);
        navButtons.forEach((n, btn) -> {
            if (n.equals(name)) {
                if (!btn.getStyleClass().contains("nav-item-active")) btn.getStyleClass().add("nav-item-active");
            } else {
                btn.getStyleClass().remove("nav-item-active");
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
