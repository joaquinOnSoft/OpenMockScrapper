package com.openmock.oscaroscrapper.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
public class Family extends AbstractVehicle {
    @Setter(AccessLevel.NONE)
    private List<Model> models;

    public Family(String id, String name) {
        super(id, name);
        models = new LinkedList<>();
    }

    public void addModels(List<Model> pModels) {
        if (pModels != null) {
            models.addAll(pModels);
        }
    }

    public void addModel(Model model) {
        if (model != null) {
            models.add(model);
        }
    }

    public Model getModel(int position) {
        Model model = null;
        if (position >= 0 && position < models.size()) {
            model = models.get(position);
        }
        return model;
    }
}
