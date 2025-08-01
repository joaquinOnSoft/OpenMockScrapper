package com.openmock.oscaroscrapper.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {
    @Test
    public void initModel() {
        Model model = new Model("mo-1366", "II Hatchback (09/2002 - 10/2008)");
        assertNotNull(model.getManufacturedFrom());
        assertEquals(1030831200000L, model.getManufacturedFrom().getTime());
        assertNotNull(model.getManufacturedTo());


        model = new Model("mo-1366", "II Hatchback (09/2002 - hoy)");
        assertNotNull(model.getManufacturedFrom());
        assertEquals(1030831200000L, model.getManufacturedFrom().getTime());
        assertNull(model.getManufacturedTo());
    }
}
