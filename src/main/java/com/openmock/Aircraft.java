package com.openmock;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class Aircraft {
    private AircraftType type;
    private String name;
    private int numSeats;
    private String seatClass;
    private List<Amenity>amenities = new LinkedList<>();

    public void addAmenity(Amenity amenity){
        amenities.add(amenity);
    }

}
