package com.openmock.openavscrapper;

import com.openmock.Airport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AirportScrapperTest {
    @Test
    public void getAirportsURLs(){
        List<String> airportURLs = AirportScrapper.getAirportsURLs();

        assertNotNull(airportURLs);
        assertEquals(7801, airportURLs.size());
        assertEquals("https://opennav.com/airport/NTGA", airportURLs.getFirst());
        assertEquals("https://opennav.com/airport/KZZV", airportURLs.getLast());
    }


    @ParameterizedTest
    @CsvSource({
            "https://opennav.com/airport/NTGA, NTGA, AAA, Anaa Airport",
            "https://opennav.com/airport/YARY, YARY, AAB, Arrabury Airport",
            "https://opennav.com/airport/HEAR, HEAR, AAC, El Arish International Airport"
    })
    public void getAirportURLTest(String url, String icao, String iata, String name) {
        Airport airport = AirportScrapper.getAirport(url);

        // Asegura que el objeto Airport no sea nulo.
        assertNotNull(airport, "Airport object must NOT be null");

        assertEquals(icao, airport.getIcao(), "ICAO doesn't match");
        assertEquals(iata, airport.getIata(), "IATA code doesn't match");
        assertEquals(name, airport.getName(), "Airport name doesn't match");
    }
}
