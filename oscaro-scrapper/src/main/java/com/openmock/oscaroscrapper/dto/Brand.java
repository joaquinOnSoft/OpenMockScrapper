package com.openmock.oscaroscrapper.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
public class Brand extends AbstractVehicle {

    private String fullName;

    @Setter(AccessLevel.NONE)
    private List<Family> families;

    public Brand(String id, String name, String fullName) {
        super(id, name);
        this.fullName = fullName;
        families = new LinkedList<>();
    }

    public void addFamilies(List<Family> pFamilies) {
        if (families != null) {
            families.addAll(pFamilies);
        }
    }

    public void addFamily(Family family) {
        if (family != null) {
            families.add(family);
        }
    }

    public Family getFamily(int position) {
        Family family = null;
        if (position >= 0 && position < families.size()) {
            family = families.get(position);
        }
        return family;
    }
}
