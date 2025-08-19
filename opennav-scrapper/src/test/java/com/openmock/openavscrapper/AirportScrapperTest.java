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

    @ParameterizedTest(name = "Airport URL to process: {0}")
    @CsvSource({
            "https://opennav.com/airport/NTGA",
            "https://opennav.com/airport/YARY",
            "https://opennav.com/airport/HEAR"
    })
    public void getAirlineURL(String url){
        Airport airport = AirportScrapper.getAirport(url);
        assertNotNull(airport);
    }
}
