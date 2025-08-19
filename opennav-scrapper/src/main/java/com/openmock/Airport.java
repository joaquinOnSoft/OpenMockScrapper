package com.openmock.openavscrapper;

import lombok.Data;

import java.util.Locale;

@Data
public class Airline {
    private String name;
    private Locale.IsoCountryCode isoCountryCode;
    /** Airport elevation in feet */
    private int elevation;
    /** Airport latitude, e.g., 40° 29' 36.96" N */
    private String latitude;
    /** Airport longitude, e.g., 3° 34' 0.34" W */
    private String longitude;
    private String website;
    private String wikipedia;

    public void setIsoCountryCode(String isoCountryCode) throws IllegalArgumentException{
        this.isoCountryCode = Locale.IsoCountryCode.valueOf(isoCountryCode);
    }
}
