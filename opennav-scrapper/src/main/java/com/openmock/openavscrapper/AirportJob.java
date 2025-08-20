package com.openmock.openavscrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class AirportJob {
    private AirportJobType type;
    private String url;

    AirportJob(String url) {
        this.url = url;
        this.type = AirportJobType.PARSER;
    }

    AirportJob(AirportJobType type) {
        this.url = null;
        this.type = type;
    }
}
