package com.folkcargo.ui.util;

import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Small collection of formatting and dialog helpers reused across every
 * module view, so each screen doesn't have to reinvent currency/date
 * formatting or the "are you sure?" confirmation flow.
 */
public final class UIUtils {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private UIUtils() {
    }

    public static String formatCurrency(double amount) {
        return String.format("₹%,.2f", amount);
    }

    public static String formatDate(LocalDate date) {
        return date == null ? "-" : date.format(DATE_FORMAT);
    }

    public static String formatNumber(double value) {
        if (value == Math.floor(value)) {
            return String.format("%,.0f", value);
        }
        return String.format("%,.2f", value);
    }

    /** Builds a plain text TableColumn backed by a JavaBean-style getter (PropertyValueFactory). */
    public static <S, T> TableColumn<S, T> column(String title, String property, double prefWidth) {
        TableColumn<S, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setPrefWidth(prefWidth);
        return col;
    }

    /** Builds a TableColumn from an arbitrary value-extraction lambda (for computed / joined columns). */
    public static <S, T> TableColumn<S, T> column(String title, double prefWidth,
                                                    Callback<CellDataFeatures<S, T>, javafx.beans.value.ObservableValue<T>> factory) {
        TableColumn<S, T> col = new TableColumn<>(title);
        col.setCellValueFactory(factory);
        col.setPrefWidth(prefWidth);
        return col;
    }

    public static boolean confirm(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        styleDialog(alert);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public static void info(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        styleDialog(alert);
        alert.showAndWait();
    }

    public static void warn(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        styleDialog(alert);
        alert.showAndWait();
    }

    public static void styleDialog(Dialog<?> dialog) {
        dialog.getDialogPane().getStylesheets().add(
                UIUtils.class.getResource("/com/folkcargo/css/styles.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("fcs-dialog");
    }

    public static StringConverter<LocalDate> localDateConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date == null ? "" : date.format(DATE_FORMAT);
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.isBlank()) return null;
                try {
                    return LocalDate.parse(string, DATE_FORMAT);
                } catch (Exception e) {
                    return LocalDate.now();
                }
            }
        };
    }

    public static DatePicker datePicker(LocalDate initial) {
        DatePicker picker = new DatePicker(initial);
        picker.setConverter(localDateConverter());
        picker.setPromptText("dd MMM yyyy");
        picker.setMaxWidth(Double.MAX_VALUE);
        return picker;
    }

    public static TextField numericField(double initial) {
        TextField field = new TextField(formatNumber(initial));
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                field.setText(oldVal);
            }
        });
        return field;
    }

    public static double parseDoubleOrZero(String text) {
        try {
            return text == null || text.isBlank() ? 0 : Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int parseIntOrZero(String text) {
        try {
            return text == null || text.isBlank() ? 0 : Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
