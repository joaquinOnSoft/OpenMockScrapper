package com.openmock.oscaroscrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openmock.oscaroscrapper.pojo.VehiclesMng;
import com.openmock.util.FileUtil;
import org.apache.hc.client5.http.fluent.Request;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;


public class ZenRowsOscaroScrapper extends AbstractOscaroScrapper {

    private static final String APIKEY_PLACEHOLDER = "API_KEY";
    private static final String ZEN_BASE = "https://api.zenrows.com/v1/?js_render=true&apikey=API_KEY&url=";

    public ZenRowsOscaroScrapper(){
        this("es");
    }

    public ZenRowsOscaroScrapper(String lang){
        super(lang);


    }

    private String getZenRowsBaseURL(){
        String apikey="";
        Properties prop = FileUtil.loadProperties("zenrows.properties");
        if(prop  != null){
            apikey = prop.getProperty("apikey");
        }

        return ZEN_BASE.replace(APIKEY_PLACEHOLDER, apikey);
    }

    /**
     * Generate the URL to recover the vehicle information
     * URL examples:
     * <ul>
     *     <li><a href="https://www.oscaro.com/xhr/nav/vehicles/fr/fr?vehicles-id=0&tree-level=root&init=true&page-type=home">Brands (all)</a></li>
     *     <li><a href="https://www.oscaro.com/xhr/nav/vehicles/fr/fr?vehicles-id=ma-178&tree-level=brand&page-type=home">Families for Brand</a></li>
     *     <li><a href="https://www.oscaro.com/xhr/nav/vehicles/fr/fr?vehicles-id=fa-650&tree-level=family&page-type=home">Models for Family</a></li>
     *     <li><a href="https://www.oscaro.com/xhr/nav/vehicles/fr/fr?vehicles-id=mo-7174&tree-level=model&page-type=home">Types for Model</a></li>
     *     <li><a href="https://www.oscaro.com/xhr/nav/vehicles/fr/fr?vehicles-id=63833&tree-level=type&page-type=home">type</a></li>
     * </ul>
     *
     * @param id    - Vehicle identifier
     * @param level - Information level. Possible values: root, brand, family, model
     * @return URL to recover
     */
    @Override
    protected URL getURL(String id, Level level) {
        URL url = null;
        String urlStr;

        try {
            urlStr = getZenRowsBaseURL() + URLEncoder.encode(super.getURL(id,level).toString(), StandardCharsets.UTF_8);

            url = new URI(urlStr).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            log.error("", e);
        }

        return url;
    }

    @Override
    protected VehiclesMng url2Vehicles(URL url) {
        VehiclesMng vehicles = null;


        try {
            String jsonString = Request.get(url.toString())
                    .execute().returnContent().asString();

            ObjectMapper MAPPER = new ObjectMapper();
            vehicles = MAPPER.readValue(jsonString, VehiclesMng.class);
        } catch (JsonMappingException ex) {
            log.error("JSON Mapping: ", ex);
        } catch (JsonProcessingException ex) {
            log.error("JSON processing: ", ex);
        } catch (MalformedURLException ex) {
            log.error("Malformed URL: ", ex);
        } catch (IOException ex) {
            if(ex.getMessage().contains("403")){
                String msg= """
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
