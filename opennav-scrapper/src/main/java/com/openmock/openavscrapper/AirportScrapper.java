package com.openmock.openavscrapper;

import com.openmock.Airport;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Log4j2
public class AirportScrapper {
    private static final String OPENNAV_URL_BASE = "https://opennav.com";
    private static final String  AIRPORT_CODES_BASE = "https://opennav.com/airportcodes?%s";
    private static final String[] letters = {
            "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y",
            "Z"};

    enum FieldIndex{
        NAME,
        COUNTRY,
        ELEVATION,
        LATITUDE,
        LONGITUDE,
        WEBSITE,
        WIKIPEDIA
    }

    enum FieldName{
        NAME("Name"),
        COUNTRY("Country"),
        ELEVATION("Elevation"),
        LATITUDE("Latitude"),
        LONGITUDE("Longitude"),
        WEBSITE("Website"),
        WIKIPEDIA("Wikipedia");

        public final String label;

        FieldName(String label) {
            this.label = label;
        }

        public static FieldName valueOfLabel(String label) {
            for (FieldName e : values()) {
                if (e.label.equals(label)) {
                    return e;
                }
            }
            return null;
        }
    }

    public static List<String> getAirportsURLs() {
        List<String> urls = new LinkedList<>();

        String url;
        Document doc;

        for(String letter: letters){
            url = String.format(AIRPORT_CODES_BASE, letter);
            log.info("openav.com Alphabetic airport list page: {}", url);

            try {
                doc = Jsoup.connect(url).get();
                Elements eField = doc.select("td > a[href]");
                if(!eField.isEmpty()){
                    for(Element link: eField){
                        urls.add(OPENNAV_URL_BASE + link.attr("href"));
                    }
                }
            }
            catch (IOException e) {
                log.error(e);
                throw new RuntimeException(e);
            }
        }

        return urls;
    }

    public static Airport getAirport(String url){
        Airport airport;

        log.debug("Airport URL: {}", url);

        Document doc;
        try {
            airport = new Airport();

            doc = Jsoup.connect(url).get();

            // Get airport general information
            Elements eFields = doc.select("td");
            if(!eFields.isEmpty()){
                FieldIndex index = null;
                String value;
                for(Element field: eFields){
                    value = field.text();

                    if(index != null) {
                        switch (index) {
                            case NAME -> airport.setName(value);
                            case COUNTRY -> airport.setIsoCountryCode(value);
                            case ELEVATION -> airport.setElevation(Float.parseFloat(value.replace(",", ".").replace(" feet", "")));
                            case LATITUDE -> airport.setLatitude(value);
                            case LONGITUDE -> airport.setLongitude(value);
                            case WEBSITE -> airport.setWebsite(value);
                            case WIKIPEDIA -> airport.setWikipedia(value);
                        }
                        index = null;
                    }
                    else {
                        switch (FieldName.valueOfLabel(value)) {
                            case NAME -> index = FieldIndex.NAME;
                            case COUNTRY -> index = FieldIndex.COUNTRY;
                            case ELEVATION -> index = FieldIndex.ELEVATION;
                            case LATITUDE -> index = FieldIndex.LATITUDE;
                            case LONGITUDE -> index = FieldIndex.LONGITUDE;
                            case WEBSITE -> index = FieldIndex.WEBSITE;
                            case WIKIPEDIA -> index = FieldIndex.WIKIPEDIA;
                            case null -> { /* Do nothing */ }
                        }
                    }
                }
            }

            // Get ICAO code
            Elements eICAOFields = doc.select("span[itemprop=icaoCode]");
            if(!eICAOFields.isEmpty()) {
                airport.setIcao(eICAOFields.getFirst().text());
            }

            // Get IATA code
            Elements eIATAFields = doc.select("span[itemprop=iataCode]");
            if(!eIATAFields.isEmpty()) {
                airport.setIata(eIATAFields.getFirst().text());
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return airport;
    }


}
