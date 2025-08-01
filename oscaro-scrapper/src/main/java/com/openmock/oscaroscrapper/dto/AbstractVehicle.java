package com.openmock.oscaroscrapper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public abstract class AbstractVehicle {
    protected String id;
    protected String name;
}
