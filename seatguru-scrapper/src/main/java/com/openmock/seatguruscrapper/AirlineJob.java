package com.openmock.seatguruscrapper;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class AirlineJob {
    private String url;
    private boolean last;

    public AirlineJob(){
        this.last = true;
    }

    public AirlineJob(String url){
        this.url = url;
        last = false;
    }
}
