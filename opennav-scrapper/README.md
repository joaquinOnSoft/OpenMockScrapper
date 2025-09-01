#  OpenMock's `Open Nav` scrapper

Generates multiple JSON files with airports information, (CAO code, IATA code, FAA code, airport name, or city name), 
from [OPENNAV](https://www.opennav.com)

## Execution

Distributed as an auto-runnable jar file can be executed from the command line.

Accepted parameters:

- `--threads` or `-t`: (Optional) Number of simultaneous threads to be launched to process the page. Default value: 4
- `--output` or `-o`: (Optional) Output path (directory). Default value '.'
            
Call example:

```            
java -jar OpenNavScrapper.jar --threads 8
```

## Output

> The app generates a folder for each country and json file for each airport

The output json file looks like this:

```json
  
{
  "name" : "Madrid–Barajas Airport Adolfo Suárez Madrid–Barajas Airport Aeropuerto Adolfo Suárez Madrid-Barajas",
  "icao" : "LEMD",
  "iata" : "MAD",
  "isoCountryCode" : "ES",
  "elevation" : 2.0,
  "latitude" : "40° 29' 36.96\" N",
  "longitude" : "3° 34' 0.34\" W",
  "website" : "https://www.aena.es/en/adolfo-suarez-madrid-barajas.html",
  "wikipedia" : "https://en.wikipedia.org/wiki/Madrid–Barajas_Airport"
}
```
