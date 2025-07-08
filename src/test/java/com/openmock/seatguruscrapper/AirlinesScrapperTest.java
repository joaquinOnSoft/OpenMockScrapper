package com.openmock.seatguruscrapper;

import com.openmock.Airline;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AirlinesScrapperTest {

    @Test
    public void getAirlineURLs(){
        List<String> urls = AirlinesScrapper.getAirlineURLs();

        assertNotNull(urls);
        assertFalse(urls.isEmpty());
        assertEquals(174, urls.size());
        assertEquals("https://www.seatguru.com/airlines/Aegean_Airlines/information.php", urls.getFirst());
        assertEquals("https://www.seatguru.com/airlines/Xiamen_Airlines/information.php", urls.getLast());

    }

    @Test
    public void getAirline(){
        Airline airline = AirlinesScrapper.getAirline("https://www.seatguru.com/airlines/Aegean_Airlines/information.php");
        assertNotNull(airline);
        assertEquals("Aegean Airlines", airline.getName());
        assertEquals("A3", airline.getCode());
        assertEquals("https://en.aegeanair.com", airline.getWeb());
        assertEquals("+33 170 031323", airline.getReservationsPhone());

        String[] expectedDestinations = {
                "Washington DC", "Orlando", "Chicago",
                "Las Vegas", "San Francisco", "Honolulu",
                "Mexico", "Toronto", "London",
                "New York City"
        };

        List<String> destinations = airline.getPopularDestinations();
        assertNotNull(destinations);
        int numDestinations = expectedDestinations.length;
        for(int i=0; i<numDestinations; i++){
            assertEquals(expectedDestinations[i], destinations.get(i));
        }
    }
}
