package com.folkcargo.ui;

import com.folkcargo.service.ServiceRegistry;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

/**
 * Groups the {@link VehicleView} and {@link DriverView} screens together
 * under one "Fleet" navigation item, since a vehicle and its driver are
 * managed as a single operational unit.
 */
public class FleetView extends BorderPane {

    public FleetView(ServiceRegistry services) {
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getStyleClass().add("module-tabs");

        Tab vehiclesTab = new Tab("Vehicles", new VehicleView(services));
        Tab driversTab = new Tab("Drivers", new DriverView(services));
        tabs.getTabs().addAll(vehiclesTab, driversTab);

        setCenter(tabs);
    }
}
