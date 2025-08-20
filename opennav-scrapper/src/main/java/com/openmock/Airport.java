package com.openmock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.Locale;

@Data
@Log4j2
public class Airport {
    private String name;
    /// The ICAO airport code or location indicator is a four-letter code designating aerodromes
    /// around the world. These codes, as defined by the International Civil Aviation Organization
    /// and published quarterly in ICAO Document 7910: Location Indicators, are used by air traffic
    /// control and airline operations such as flight planning. ICAO codes are also used to identify
    /// other aviation facilities such as weather stations, international flight service stations,
    /// or area control centers (and by extension their flight information regions), regardless of
    /// whether they are located at airports.
    private String icao;
    /// IATA code assigned to the airport by IATA consists of 3 letters and created through
    /// the airport and city names. Codes are mostly generated with the letters chosen from
    /// a city's name. 2-character codes of airports are given in 1930 for the first time.
    /// Then three-character codes started to be used.
    private String iata;
    private Locale isoCountryCode;
    ///Airport elevation in feet
    private float elevation;
    /// Airport latitude, e.g., 40° 29' 36.96" N
    private String latitude;
    ///Airport longitude, e.g., 3° 34' 0.34" W
    private String longitude;
    private String website;
    private String wikipedia;

    public void setIsoCountryCode(String isoCountryCode) {
        this.isoCountryCode =  Locale.of("", isoCountryCode);
    }

    public String toJSON() {
        String json = null;
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            json = ow.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("Error generating JSON: ", e);
        }
        return json;
    }
}
