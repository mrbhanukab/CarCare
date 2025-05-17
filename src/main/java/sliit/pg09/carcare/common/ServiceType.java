package sliit.pg09.carcare.common;

public enum ServiceType {
    OIL_CHANGE("Oil Change"),
    GENERAL_REPAIR("General Repair"),
    PARTS_REPLACEMENT("Parts Replacement"),
    FULL_INSPECTION("Full Inspection"),
    ELECTRICAL_SYSTEM("Electrical System"),
    AC_SERVICE("A/C Service");

    private final String displayName;

    ServiceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
