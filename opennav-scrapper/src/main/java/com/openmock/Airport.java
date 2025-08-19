package com.openmock;

import lombok.Data;

import java.util.Locale;

@Data
public class Airport {
    private String name;
    private Locale isoCountryCode;
    /** Airport elevation in feet */
    private int elevation;
    /** Airport latitude, e.g., 40° 29' 36.96" N */
    private String latitude;
    /** Airport longitude, e.g., 3° 34' 0.34" W */
    private String longitude;
    private String website;
    private String wikipedia;

    public void setIsoCountryCode(String isoCountryCode) {
        this.isoCountryCode =  Locale.of("", isoCountryCode);
    }
}
