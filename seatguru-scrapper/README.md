# Open Mock seatguru.com Scrapper

```
Open Mock is a random data generator library for testing.
```

This web scrapper reads information about airlines, popular destinations
and airplane models used by the airline.

## Execution

Distributed as an auto-runnable jar file can be executed from the command line.

Accepted parameters:

- `--threads` Number of simultaneous threads to be launched to process the page

Execution example:

```console 
java -jar seatguru-scrapper-25.08.jar --theards 8
```

## Output

It generates a `json` file for each airline. The `json` file looks like this:

```json
{
  "name" : "Aer Lingus",
  "code" : "EI",
  "web" : "https://www.aerlingus.com",
  "lounge" : "Gold Circle Club",
  "reservationsPhone" : "1-516-622-4222",
  "frequentFlyerProgram" : "Gold Circle Club",
  "alliance" : "N/A",
  "magazine" : "Cara",
  "aircrafts" : [ {
    "type" : "WIDE_BODY_JETS",
    "name" : "Airbus A330-200 (332) Layout 1",
    "amenities" : [ "FOOD", "INTERNET", "AC_POWER", "VIDEO", "AUDIO" ],
    "seats" : [ {
      "seatClass" : "Business",
      "numSeats" : 23
    }, {
      "seatClass" : "Economy",
      "numSeats" : 248
    } ]
  }, {
    "type" : "WIDE_BODY_JETS",
    "name" : "Airbus A330-200 (332) Layout 2",
    "amenities" : [ "FOOD", "INTERNET", "AC_POWER", "VIDEO", "AUDIO" ],
    "seats" : [ {
      "seatClass" : "Business",
      "numSeats" : 23
    }, {
      "seatClass" : "Economy",
      "numSeats" : 243
    } ]
  }, {
    "type" : "WIDE_BODY_JETS",
    "name" : "Airbus A330-300 (333)",
    "amenities" : [ "FOOD", "INTERNET", "AC_POWER", "VIDEO", "AUDIO" ],
    "seats" : [ {
      "seatClass" : "Business",
      "numSeats" : 30
    }, {
      "seatClass" : "Economy",
      "numSeats" : 287
    } ]
  }, {
    "type" : "NARROW_BODY_JETS",
    "name" : "Airbus A320 (320)",
    "amenities" : [ "FOOD" ],
    "seats" : [ {
      "seatClass" : "Economy",
      "numSeats" : 174
    } ]
  }, {
    "type" : "NARROW_BODY_JETS",
    "name" : "Airbus A321 (321)",
    "amenities" : [ "FOOD" ],
    "seats" : [ {
      "seatClass" : "Economy",
      "numSeats" : 212
    } ]
  }, {
    "type" : "NARROW_BODY_JETS",
    "name" : "Airbus A321LR (321)",
    "amenities" : [ "FOOD", "INTERNET", "AC_POWER", "VIDEO", "AUDIO" ],
    "seats" : [ {
      "seatClass" : "Business",
      "numSeats" : 16
    }, {
      "seatClass" : "Economy",
      "numSeats" : 168
    } ]
  }, {
    "type" : "NARROW_BODY_JETS",
    "name" : "Boeing 757-200 (752)",
    "amenities" : [ "FOOD", "AC_POWER", "VIDEO", "AUDIO" ],
    "seats" : [ {
      "seatClass" : "Business",
      "numSeats" : 12
    }, {
      "seatClass" : "Economy",
      "numSeats" : 165
    } ]
  } ],
  "popularDestinations" : [ "Washington DC", "Orlando", "Chicago", "Las Vegas", "San Francisco", "Honolulu", "Mexico", "Toronto", "London", "New York City" ]
}
```

## TO DO list

- [x] Recover magazine for each airline
- [x] Review airline types supported
- [ ] Replace ENUM values for literals in the output json