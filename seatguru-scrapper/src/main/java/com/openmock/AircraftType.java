package com.openmock;

import lombok.extern.log4j.Log4j2;

@Log4j2
public enum AircraftType {
    NARROW_BODY_JETS("Narrowbody Jets"),
    REGIONAL_JETS("Regional Jets"),
    TURBOPROPS("Turboprops"),
    WIDE_BODY_JETS("Widebody Jets");

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

        log.warn("UNKNOWN aircraft type: {}", label);
        return null;
    }
}
