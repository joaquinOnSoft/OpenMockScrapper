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
    private List<Amenity>amenities = new LinkedList<>();
    private List<Seat>seats = new LinkedList<>();


    public void addAmenity(Amenity amenity){
        amenities.add(amenity);
    }

    public void addSeat(Seat seat){
        seats.add(seat);
    }

    public void addSeat(String seatClass, int numSeats){
        seats.add(new Seat(seatClass, numSeats));
    }

    //138 Economy
    public void addSeat(String htmlSeat){
        if (htmlSeat != null) {
            int whitespace = htmlSeat.indexOf(" ");
            int numberSeats = Integer.parseInt(htmlSeat.substring(0, whitespace));
            seats.add(new Seat(htmlSeat.substring(whitespace + 1), numberSeats));

        }
    }
}
