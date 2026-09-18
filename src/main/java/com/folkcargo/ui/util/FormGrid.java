package com.folkcargo.ui.util;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * A small fluent wrapper around {@link GridPane} used by every "Add / Edit"
 * dialog in the app, so each form only has to say which label goes with
 * which control instead of re-deriving grid layout constraints every time.
 */
public final class FormGrid {

    private final GridPane grid = new GridPane();
    private int row = 0;

    public FormGrid() {
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPadding(new Insets(18, 12, 8, 12));
        ColumnConstraints labelCol = new ColumnConstraints(130);
        ColumnConstraints fieldCol = new ColumnConstraints();
        fieldCol.setHgrow(Priority.ALWAYS);
        fieldCol.setMinWidth(240);
        grid.getColumnConstraints().addAll(labelCol, fieldCol);
    }

    public FormGrid addRow(String labelText, Node field) {
        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");
        grid.add(label, 0, row);
        grid.add(field, 1, row);
        row++;
        return this;
    }

    public GridPane build() {
        return grid;
    }
}
