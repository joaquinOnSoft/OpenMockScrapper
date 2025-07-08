package com.openmock.seatguruscrapper;

import com.openmock.Aircraft;
import com.openmock.AircraftType;
import com.openmock.Airline;
import com.openmock.Amenity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AirlinesScrapper {
    protected static final String SEAT_GURU_URL_BASE = "https://www.seatguru.com";
    protected static final String SEAT_GURU_BROWSE_AIRLINES = SEAT_GURU_URL_BASE + "/browseairlines";

    private static final int AI_LABEL_AIRLINE_CODE = 0;
    private static final int AI_LABEL_WEBSITE = 1;
    private static final int AI_LABEL_RESERVATIONS = 2;

    public static final String LABEL_AIRLINE_CODE = "Airline Code";
    public static final String LABEL_WEBSITE = "Website";
    public static final String LABEL_RESERVATIONS = "Reservations";

    private static final String AIRLINE_URLS_PATTERN = "div[class=browseAirlines] > ul > li > a[href]";
    private static final String AIRLINE_NAME_PATTER = "div[class=content-header] > div[class=title] > h1";
    private static final String AIRLINE_BASIC_INFO_PATTERN = "div[class=airlineBannerLargeRight] > span[class=ai-info]";
    private static final String AIRLINE_POPULAR_DESTINATIONS_PATTERN = "div[id=popular-destinations] > ul > li > div[class=geo-title]";

    private final static Logger log = LogManager.getLogger(AirlinesScrapper.class);

    /// Find all  airlines URLs included in the
    /// [Browse Airlines](https://www.seatguru.com/browseairlines) page
    ///
    /// @return List of airline URLs
    public static List<String> getAirlineURLs() {
        String url = SEAT_GURU_BROWSE_AIRLINES;
        List<String> urls = null;

        log.debug("URL: {}", url);

        try {
            Document doc = Jsoup.connect(url).get();
            Elements eLinks = doc.select(AIRLINE_URLS_PATTERN);

            if (!eLinks.isEmpty()) {
                urls = new LinkedList<>();

                for (Element link : eLinks) {
                    urls.add(SEAT_GURU_URL_BASE + link.attr("href"));
                }
            }
        } catch (IOException e) {
            log.error("Error parsing seatguru.com airlines list: ", e);
        }

        return urls;
    }

    /// Parse the airline page and retrieve all the airline details
    /// e.g.: [Aer Lingus (EI)](https://www.seatguru.com/airlines/Aer_Lingus/information.php)
    public static Airline getAirline(String url){
        Airline airline;

        log.debug("Airline URL: {}", url);

        Document doc;
        try {
            airline = new Airline();

            doc = Jsoup.connect(url).get();

            //Get Airline name
            String title = getName(doc);
            if(title != null)
                airline.setName(title);

            //Get basic airline information
            Map<String, String> data = getAirlineBannerLargeRight(doc);
            if(data != null && !data.isEmpty()){
                if(data.containsKey(LABEL_AIRLINE_CODE)){
                    airline.setCode(data.get(LABEL_AIRLINE_CODE));
                }

                if(data.containsKey(LABEL_WEBSITE)){
                    airline.setWeb(data.get(LABEL_WEBSITE));
                }

                if(data.containsKey(LABEL_RESERVATIONS)){
                    airline.setReservationsPhone(data.get(LABEL_RESERVATIONS));
                }
            }

            //Get Popular destinations
            List<String> destinations = getPopularDestinations(doc);
            if(destinations != null){
                airline.setPopularDestinations(destinations);
            }

            //Get aircraft list
            List<Aircraft> aircrafts = getAircrafts(doc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return airline;
    }

    /// Recover some basic airline information.
    /// Parse a page fragment that looks like this:
    /// ```
    /// <div class="airlineBannerLargeRight">
    ///   <span class="ai-label">Airline Code</span>
    ///   <span class="ai-info">A3</span>
    ///   <span class="ai-label">Website</span>
    ///   <span class="ai-info">
    ///     <p class="phoneNo">
    ///       <a target="_blank" rel="nofollow" href="https://en.aegeanair.com">https://en.aegeanair.com</a>
    ///     </p>
    ///   </span>
    ///   <span class="ai-label">Reservations</span>
    ///   <span class="ai-info">+33 170 031323</span>
    /// </div>
    /// ```
    private static Map<String, String> getAirlineBannerLargeRight(Document doc){
        Map<String, String> data =  null;

        Elements eSpans = doc.select(AIRLINE_BASIC_INFO_PATTERN);
        if(!eSpans.isEmpty()){
            data = new HashMap<>();
            int numElements = eSpans.size();
            for (int i=0; i<numElements; i++){
                switch (i){
                    case AI_LABEL_AIRLINE_CODE -> data.put(LABEL_AIRLINE_CODE, eSpans.get(i).text());
                    case AI_LABEL_WEBSITE -> data.put(LABEL_WEBSITE, eSpans.get(i).text());
                    case AI_LABEL_RESERVATIONS -> data.put(LABEL_RESERVATIONS, eSpans.get(i).text());
                }
            }
        }

        return data;
    }


    ///
    /// ```
    /// <div id="popular-destinations">
    ///   <h2>Popular Destinations</h2>
    ///   <ul>
    ///     <li>
    ///       <a href="/cheap-flights/28970Cheap-Flights-to-Washington-DC">
    ///         <img src="https://cdn.seatguru.com/en_US/img/20250703080841//seatguru/hero_photos/washington-dc.jpg">
    ///       </a>
    ///       <div class="geo-title">Cheap Flights to Washington DC</div>
    ///     </li>
    ///     <li>
    ///       <a href="/cheap-flights/34515Cheap-Flights-to-Orlando">
    ///         <img src="https://cdn.seatguru.com/en_US/img/20250703080841//seatguru/hero_photos/epcot-ball-at-night.jpg">
    ///       </a>
    ///       <div class="geo-title">Cheap Flights to Orlando</div>
    ///     </li>
    ///   </ul>
    /// </div>
    /// ```
    private static List<String> getPopularDestinations(Document doc) {
        List<String> destinations = null;

        Elements eDestinations = doc.select(AIRLINE_POPULAR_DESTINATIONS_PATTERN);
        if(!eDestinations.isEmpty()){
            destinations = new LinkedList<>();
           for(Element destination: eDestinations){
               destinations.add(destination.text().replace("Cheap Flights to ", ""));
           }

        }
        return destinations;
    }

    ///
    /// <div class="chartsTitle">
    ///   <h3>
    ///     <a class="key" href="" onclick="toggleAmenitiesKey(this); return false;">KEY</a>Narrowbody Jets
    ///   </h3>
    ///   <div class="amenities-key" style="display: none;">
    ///     <div class="arrow-up"></div>
    ///     <ul class="box">
    ///       <li>
    ///         <div class="sprite-amenities sprite-baby"></div> Infants
    ///       </li>
    ///       <li>
    ///         <div class="sprite-amenities sprite-food"></div> Food
    ///       </li>
    ///       <li>
    ///         <div class="sprite-amenities sprite-wifi"></div> Internet
    ///       </li>
    ///       <li>
    ///         <div class="sprite-amenities sprite-elec"></div> AC Power
    ///       </li>
    ///       <li>
    ///         <div class="sprite-amenities sprite-tv"></div> Video
    ///       </li>
    ///       <li>
    ///         <div class="sprite-amenities sprite-headphones"></div> Audio
    ///       </li>
    ///     </ul>
    ///   </div>
    /// </div>
    /// <table class="seats" cellpadding="0" cellspacing="0">
    ///   <tbody>
    ///     <tr>
    ///       <td>
    ///         <ul class="amenities-list" id="amenities_list">
    ///           <li class="sprite-amenities sprite-food"></li>
    ///         </ul>
    ///         <div class="aircraft_seats">
    ///           <a href="/airlines/Air_Malta/Air_Malta_Airbus_A320_V1.php">Airbus A320 (320) Layout 1</a>
    ///           <br>
    ///           <span class="seat_label">Seats:</span>
    ///           <span class="seat_class">
    ///             <span class="seat_count">12</span> Business </span>
    ///           <span class="seat_class">
    ///             <span class="seat_count">138</span> Economy </span>
    ///         </div>
    ///       </td>
    ///     </tr>
    ///     <tr>
    ///       <td>
    ///         <ul class="amenities-list" id="amenities_list">
    ///           <li class="sprite-amenities sprite-food"></li>
    ///         </ul>
    ///         <div class="aircraft_seats">
    ///           <a href="/airlines/Air_Malta/Air_Malta_Airbus_A320_V2.php">Airbus A320 (320) Layout 2</a>
    ///           <br>
    ///           <span class="seat_label">Seats:</span>
    ///           <span class="seat_class">
    ///             <span class="seat_count">144</span> Economy </span>
    ///         </div>
    ///       </td>
    ///     </tr>
    ///   </tbody>
    /// </table>
    private static List<Aircraft> getAircrafts(Document doc){
        Aircraft aircraft;
        List<Aircraft> aircrafts = null;
        List<AircraftType> aircraftTypes = new LinkedList<>();

        Elements eAircraftTypes = doc.select("div[class=chartsTitle] > h3");
        if(!eAircraftTypes.isEmpty()){
            aircrafts = new LinkedList<>();
            for(Element aircraftType: eAircraftTypes){
                aircraftTypes.add(AircraftType.valueOfLabel(aircraftType.ownText()));
            }
        }


        Elements eAircraftTable = doc.select("table[class=seats]");
        if(!eAircraftTable.isEmpty()){
            int i=0;
            for(Element aircraftTable: eAircraftTable){

                // Read aircraft for each aircraft type
                List<Element> eAircraftRows = aircraftTable.select("tbody > tr > td");
                if(!eAircraftRows.isEmpty()) {
                    for(Element aircraftRow: eAircraftRows) {
                        aircraft = new Aircraft();
                        aircraft.setType(aircraftTypes.get(i));

                        // Read amenities information for each aircraft
                        List<Element> amenities = aircraftRow.select("ul[class=amenities-list] > li");
                        if (!amenities.isEmpty()) {
                            for (Element amenity : amenities) {

                                aircraft.addAmenity(class2Amenity(amenity.attr("class")));
                            }
                        }

                        // Read amenities information for each aircraft
                        List<Element> seats = aircraftRow.select("div[class=aircraft_seats] > span[class=seat_class]");
                        if (!seats.isEmpty()) {
                            for (Element seat : seats) {
                                    ;
                                aircraft.addSeat(seat.text());
                            }
                        }


                        aircrafts.add(aircraft);
                    }
                }
                i++;
            }
        }

        return aircrafts;
    }

    /// Identify the amenity type base on the css class assigned to a `<li>` tag.
    /// Expects a css class that looks like this:
    /// ```
    /// <ul class="amenities-list" id="amenities_list">
    ///   <li class="sprite-amenities sprite-food"></li>
    ///   <li class="sprite-amenities sprite-elec"></li>
    ///   <li class="sprite-amenities sprite-tv"></li>
    ///   <li class="sprite-amenities sprite-headphones"></li>
    /// </ul>
    /// ```
    private static Amenity class2Amenity(String cssClass){
        Amenity amenity = null;
        if(cssClass != null) {
            if (cssClass.contains("sprite-baby")) {
                amenity = Amenity.INFANTS;
            } else if (cssClass.contains("sprite-food")) {
                amenity = Amenity.FOOD;
            } else if (cssClass.contains("sprite-wifi")) {
                amenity = Amenity.INTERNET;
            } else if (cssClass.contains("sprite-elec")) {
                amenity = Amenity.AC_POWER;
            } else if (cssClass.contains("sprite-tv")) {
                amenity = Amenity.VIDEO;
            } else if (cssClass.contains("sprite-headphones")) {
                amenity = Amenity.AUDIO;
            }
        }
        return amenity;
    }
    
    /// Recover the airline name
    /// Parse a page fragment that looks like this:
    /// ```
    /// <div class="content-header">
    ///   <div class="title">
    ///     <h1>Aer Lingus (EI)</h1>
    ///   </div>
    ///   <!-- end title -->
    ///   <div class="clear"></div>
    /// </div>
    /// ```
    private static String getName(Document doc) {
        String title = null;

        Elements eTitles = doc.select(AIRLINE_NAME_PATTER);
        if(!eTitles.isEmpty()){
            title = eTitles.getFirst().text();
            int index = title.lastIndexOf("(");
            if(index > 0)
                title = title.substring(0, index).trim();

        }
        return title;
    }
}
