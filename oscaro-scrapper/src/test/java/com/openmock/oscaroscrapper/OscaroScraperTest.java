package com.openmock.oscaroscrapper;

import com.openmock.oscaroscrapper.dto.Brand;
import com.openmock.oscaroscrapper.dto.Family;
import com.openmock.oscaroscrapper.dto.Model;
import com.openmock.oscaroscrapper.dto.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OscaroScraperTest {
    private static DefaultOscaroScrapper wrapper;

    @BeforeEach
    public void init() {
        //By default, Oscaro scraper is using "es" as default language
        wrapper = new DefaultOscaroScrapper();
    }

    @Test
    public void getBrands() {
        List<Brand> brands = wrapper.getBrands();
        assertNotNull(brands);
        assertEquals(121, brands.size());
        assertEquals("ma-230", brands.getFirst().getId());
        assertEquals("VOLKSWAGEN", brands.getFirst().getName());
        assertEquals("VOLKSWAGEN", brands.getFirst().getFullName());
    }

    @Test
    public void getBrandsInFrench() {
        wrapper.setLang("fr");
        List<Brand> brands = wrapper.getBrands();
        assertNotNull(brands);
        assertEquals(121, brands.size());
        assertEquals("ma-178", brands.getFirst().getId());
        assertEquals("RENAULT", brands.getFirst().getName());
        assertEquals("RENAULT", brands.getFirst().getFullName());
    }

    @Test
    public void getBrandsInPortuguese() {
        wrapper.setLang("pt");
        List<Brand> brands = wrapper.getBrands();
        assertNotNull(brands);
        assertEquals(121, brands.size());
        assertEquals("ma-178", brands.getFirst().getId());
        assertEquals("RENAULT", brands.getFirst().getName());
        assertEquals("RENAULT", brands.getFirst().getFullName());
    }

    @Test
    public void getFamilies4Brand() {
        List<Family> families = wrapper.getFamilies4Brand("ma-178");
        assertNotNull(families);
        assertEquals(65, families.size());
        assertEquals("fa-650", families.getFirst().getId());
        assertEquals("Mégane", families.getFirst().getName());
    }


    @Test
    public void getModels4Family() {
        List<Model> models = wrapper.getModels4Family("fa-650");
        assertNotNull(models);
        assertEquals(30, models.size());
        assertEquals("mo-1366", models.getFirst().getId());
        assertEquals("II Hatchback (09/2002 - 10/2008)", models.getFirst().getName());
    }

    @Test
    public void getTypes4Model() {
        List<Type> types = wrapper.getTypes4Model("mo-7174");
        assertNotNull(types);
        assertEquals(5, types.size());
        assertEquals("63835", types.getFirst().getId());
        assertEquals("GrandCoupe 1.5 Blue dCi EDC6 115 cv Transmisión automática", types.getFirst().getName());
    }

    @Test
    public void getTypeDetails() {
        Type type = wrapper.getTypeDetails("63833");
        assertNotNull(type);
        assertEquals("RENAULT Mégane", type.getName());
        assertEquals("IV Fase 2 Sedan GrandCoupe 1.3 TCe 16V GPF EDC7 140 cv Transmisión automática", type.getComplementName());
        assertEquals("GrandCoupe 1.3 TCe 16V GPF EDC7 140 cv Transmisión automática", type.getFullFragmentName());
        assertEquals("RENAULT Mégane IV Fase 2 Sedan GrandCoupe 1.3 TCe 16V GPF EDC7 140 cv Transmisión automática", type.getFullName());
        assertEquals("Gasolina", type.getEnergy());
        assertEquals("0", type.getAncestor(0));
        assertEquals("ma-178", type.getAncestor(1));
        assertEquals("fa-650", type.getAncestor(2));
        assertEquals("mo-7174", type.getAncestor(3));
    }

    //@Test
    // The execution fo this test takes too much time (commented)
    void getBrandsTypes() {
        List<Brand> brands = wrapper.getBrandsTypes();
        assertNotNull(brands);
    }


    @Test
    void getBrandTypes() {
        Brand brand = wrapper.getBrandTypes(new Brand("ma-138", "SMART", "SMART"));
        assertNotNull(brand);

        List<Family> families = brand.getFamilies();
        assertNotNull(families);
        assertEquals(6, families.size());
        assertEquals("Fortwo", families.getFirst().getName());

        List<Model> models = families.getFirst().getModels();
        assertNotNull(models);
        assertEquals(8, models.size());
        assertEquals("Coupe (451) (01/2007 - 11/2014)", models.getFirst().getName());

        List<Type> types = models.getFirst().getTypes();
        assertNotNull(types);
        assertEquals(12, types.size());
        assertEquals("SMART Fortwo Coupe (451) ED 41 cv Transmisión automática", types.getFirst().getFullName());
    }
}
