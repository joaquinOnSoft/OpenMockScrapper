package com.openmock.oscaroscrapper.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
public class Type extends AbstractVehicle {

    private String complementName;
    private String fullFragmentName;
    private String fullName;

    private String energy;

    @Setter(AccessLevel.NONE)
    private List<String> ancestors;

    public Type(String id, String name) {
        super(id, name);
        ancestors = new LinkedList<>();
    }

    public Type(String id, String name, String complementName, String fullFragmentName, String fullName, String energy) {
        this(id, name);
        this.complementName = complementName;
        this.fullFragmentName = fullFragmentName;
        this.fullName = fullName;
        this.energy = energy;
    }

    public void addAncestor(String ancestorId) {
        if (ancestorId != null) {
            ancestors.add(ancestorId);
        }
    }

    public String getAncestor(int level) {
        String ancestorId = null;
        if (level >= 0 && level < ancestors.size()) {
            ancestorId = ancestors.get(level);
        }
        return ancestorId;
    }
}
