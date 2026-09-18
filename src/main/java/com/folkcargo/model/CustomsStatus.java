package com.folkcargo.model;

/**
 * Customs clearance status shared by both {@link ExportRecord} and
 * {@link ImportRecord}.
 */
public enum CustomsStatus {
    PENDING("Pending"),
    UNDER_INSPECTION("Under Inspection"),
    CLEARED("Cleared"),
    HELD("Held");

    private final String label;

    CustomsStatus(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
