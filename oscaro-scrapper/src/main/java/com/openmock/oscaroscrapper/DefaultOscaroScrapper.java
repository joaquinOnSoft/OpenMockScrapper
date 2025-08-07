package com.openmock.oscaroscrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openmock.oscaroscrapper.pojo.VehiclesMng;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DefaultOscaroScrapper extends AbstractOscaroScrapper {

    public DefaultOscaroScrapper() {
        super();
    }

    public DefaultOscaroScrapper(String lang) {
        super(lang);
    }

    @Override
    protected VehiclesMng url2Vehicles(URL url) {
        VehiclesMng vehicles = null;


        try {
//            Document doc = Jsoup.connect(url.toString())
//                    .header("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:15.0) Gecko/20100101 Firefox/15.0.1")
//                    .header("Accept", "application/json")
//                    .header("Content-Encoding", "gzip")
//                    .header("Content-Type", "application/transit+json;charset=UTF-8")
//                    .get();
//            String jsonString = doc.text();

            Scanner scanner = new Scanner(url.openStream(), StandardCharsets.UTF_8);
            scanner.useDelimiter("\\A");
            String jsonString = scanner.hasNext() ? scanner.next() : "";

            ObjectMapper MAPPER = new ObjectMapper();
            vehicles = MAPPER.readValue(jsonString, VehiclesMng.class);
        } catch (JsonMappingException ex) {
            log.error("JSON Mapping: ", ex);
        } catch (JsonProcessingException ex) {
            log.error("JSON processing: ", ex);
        } catch (MalformedURLException ex) {
            log.error("Malformed URL: ", ex);
        } catch (IOException ex) {
            if (ex.getMessage().contains("403")) {
                String msg = """
                        ============================================
                        Cloudfare is blocking the access to the page
                        ============================================
                        """;
                log.info(msg);
            }
            log.error("I/O error: ", ex);
        }

        return vehicles;
    }
}
