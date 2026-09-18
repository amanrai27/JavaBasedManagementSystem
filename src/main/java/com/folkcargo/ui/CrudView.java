package com.folkcargo.ui;

import com.folkcargo.ui.util.UIUtils;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Generic "list + toolbar + add/edit/delete" screen used by every module in
 * the application (Customers, Fleet, Shipments, Transport, Export, Import,
 * Transactions, Invoices). A concrete subclass only needs to describe its
 * table columns, its search predicate and how to add/edit/delete a record;
 * this base class supplies the layout, the live search filtering, sorting,
 * selection-aware toolbar state and the record-count footer.
 *
 * @param <T> the entity type this screen manages
 */
public abstract class CrudView<T> extends BorderPane {

    protected final TableView<T> table = new TableView<>();
    protected final TextField searchField = new TextField();
    protected final Label recordCountLabel = new Label();

    private final FilteredList<T> filteredData;

    protected CrudView(ObservableList<T> sourceData) {
        getStyleClass().add("crud-view");

        filteredData = new FilteredList<>(sourceData, t -> true);
        SortedList<T> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPlaceholder(new Label("No records yet. Click \"Add\" to create one."));

        setTop(buildHeader());
        setCenter(table);
        setBottom(buildFooter());

        sourceData.addListener((javafx.collections.ListChangeListener<T>) c -> updateRecordCount());
        updateRecordCount();
    }

    private VBox buildHeader() {
        Label title = new Label(getTitle());
        title.getStyleClass().add("view-title");
        Label subtitle = new Label(getSubtitle());
        subtitle.getStyleClass().add("view-subtitle");

        searchField.setPromptText("Search " + getTitle().toLowerCase() + "...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(280);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter(newVal));

        Button addBtn = new Button("+ Add");
        addBtn.getStyleClass().addAll("btn", "btn-primary");
        addBtn.setOnAction(e -> {
            handleAdd();
            table.getSelectionModel().clearSelection();
        });

        Button editBtn = new Button("Edit");
        editBtn.getStyleClass().addAll("btn", "btn-secondary");
        editBtn.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        editBtn.setOnAction(e -> {
            T selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                handleEdit(selected);
                table.refresh();
            }
        });

        Button deleteBtn = new Button("Delete");
        deleteBtn.getStyleClass().addAll("btn", "btn-danger");
        deleteBtn.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        deleteBtn.setOnAction(e -> {
            T selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            if (UIUtils.confirm("Confirm Delete", "Delete this record?",
                    "This action cannot be undone.")) {
                handleDelete(selected);
            }
        });

        Button refreshBtn = new Button("Refresh");
        refreshBtn.getStyleClass().addAll("btn", "btn-ghost");
        refreshBtn.setOnAction(e -> {
            searchField.clear();
            table.getSelectionModel().clearSelection();
            table.refresh();
        });

        HBox toolbar = new HBox(10, searchField, spacer(), addBtn, editBtn, deleteBtn, refreshBtn);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.getStyleClass().add("toolbar");

        VBox header = new VBox(4, title, subtitle, toolbar);
        header.setPadding(new Insets(18, 24, 14, 24));
        header.getStyleClass().add("view-header");
        VBox.setMargin(toolbar, new Insets(10, 0, 0, 0));
        return header;
    }

    private HBox buildFooter() {
        HBox footer = new HBox(recordCountLabel);
        footer.getStyleClass().add("view-footer");
        footer.setPadding(new Insets(8, 24, 8, 24));
        footer.setAlignment(Pos.CENTER_LEFT);
        return footer;
    }

    private static HBox spacer() {
        HBox box = new HBox();
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private void applyFilter(String query) {
        String q = query == null ? "" : query.trim().toLowerCase();
        filteredData.setPredicate(item -> q.isEmpty() || matchesSearch(item, q));
        updateRecordCount();
    }

    private void updateRecordCount() {
        recordCountLabel.setText(table.getItems().size() + " record(s) shown of " + filteredData.getSource().size() + " total");
    }

    /** Installs the module's columns into {@link #table}. Call from the subclass constructor. */
    protected void setColumns(List<TableColumn<T, ?>> columns) {
        table.getColumns().setAll(columns);
    }

    protected abstract String getTitle();

    protected abstract String getSubtitle();

    /** Whether {@code item} matches the (already-lowercased, trimmed) search query. */
    protected abstract boolean matchesSearch(T item, String query);

    /** Opens the "add new record" dialog and, on confirmation, saves the new record via the owning service. */
    protected abstract void handleAdd();

    /** Opens the "edit record" dialog pre-filled with {@code item} and, on confirmation, persists the change. */
    protected abstract void handleEdit(T item);

    /** Deletes {@code item} via the owning service. The user has already confirmed the action. */
    protected abstract void handleDelete(T item);
}
