package com.openmock;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class Airline {
    private String name;
    private String code;
    private String web;
    private String lounge;
    private String reservationsPhone;
    private String frequentFlyerProgram;
    private String alliance;
    private String magazine;
    private List<Aircraft> aircrafts = new LinkedList<>();
    private List<String> popularDestinations = new LinkedList<>();

    public void addAircraft(Aircraft aircraft){
        aircrafts.add(aircraft);
    }

    public void addPopularDestination(String destination){
        popularDestinations.add(destination);
    }
}
