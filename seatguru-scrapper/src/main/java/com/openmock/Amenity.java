package com.openmock;

public enum Amenity {
    AUDIO("Audio"),
    VIDEO("Video"),
    AC_POWER("AC Power"),
    INTERNET("Internet"),
    FOOD("Food"),
    INFANTS("Infants");

    public final String label;

    Amenity(String label) {
        this.label = label;
    }

    public static Amenity valueOfLabel(String label) {
        for (Amenity e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
