package com.openmock;

public enum AircraftType {
    NARROW_BODY_JETS("Narrowbody Jets"),
    TURBOPROPS("Turboprops");

    public final String label;

    AircraftType(String label) {
        this.label = label;
    }

    public static AircraftType valueOfLabel(String label) {
        for (AircraftType e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
