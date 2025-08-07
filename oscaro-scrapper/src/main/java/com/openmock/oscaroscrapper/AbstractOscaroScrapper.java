package com.openmock.oscaroscrapper;

import com.openmock.oscaroscrapper.dto.Brand;
import com.openmock.oscaroscrapper.dto.Family;
import com.openmock.oscaroscrapper.dto.Model;
import com.openmock.oscaroscrapper.dto.Type;
import com.openmock.oscaroscrapper.pojo.Ancestor;
import com.openmock.oscaroscrapper.pojo.Child;
import com.openmock.oscaroscrapper.pojo.Vehicle;
import com.openmock.oscaroscrapper.pojo.VehiclesMng;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
public abstract class AbstractOscaroScrapper {
    @Setter(AccessLevel.NONE)
    protected static final String LEVEL_PLACEHOLDER = "%LEVEL%";
    @Setter(AccessLevel.NONE)
    protected static final String LANG_PLACEHOLDER = "%LANG%";
    @Setter(AccessLevel.NONE)
    protected static final String ID_PLACEHOLDER = "%ID%";
    @Setter(AccessLevel.NONE)
    protected static final String BASE_URL = "https://www.oscaro.com";
    @Setter(AccessLevel.NONE)
    protected static final String VEHICLES_URL = "/xhr/nav/vehicles/%LANG%/%LANG%?vehicles-id=%ID%&tree-level=%LEVEL%&page-type=home";
    @Setter
    protected String lang;

    @Setter(AccessLevel.NONE)
    protected static final Logger log = LogManager.getLogger(AbstractOscaroScrapper.class);

    public AbstractOscaroScrapper(){
        lang = "es";
    }


    /**
     * Recover all the families, models and types for all the brands
     *
     * @return List of brands with all the families, models and types
     */
    public List<Brand> getBrandsTypes() {
        List<Brand> brands = getBrands();

        if (brands != null) {
            int numBrands = brands.size();
            // Adding families for each brand
            for (int i = 0; i < numBrands; i++) {
                brands.set(i, getBrandTypes(brands.get(i)));
            }
        }

        return brands;
    }

    /**
     * Recover all the families, models and types for a given brand
     *
     * @param brand - Brand to get all the families, models and types
     * @return List of brands with all the families, models and types for a given brand
     */
    public Brand getBrandTypes(Brand brand) {
        List<Family> families;
        List<Model> models;
        List<Type> types;

        if (brand != null) {
            log.debug(brand.getName());
            families = getFamilies4Brand(brand.getId());

            if (families != null) {
                brand.addFamilies(families);

                // Adding models for each family
                for (Family family : families) {
                    log.debug("Family:\t{}", family.getName());
                    models = getModels4Family(family.getId());
                    if (models != null) {
                        family.addModels(models);

                        // Adding types for each model
                        for (Model model : models) {
                            log.debug("Model:\t\t{}", model.getName());
                            types = getTypes4Model(model.getId());
                            if (types != null) {
                                for (Type type : types) {
                                    log.debug("Type:\t\t\t{}", type.getName());
                                    type = getTypeDetails(type.getId());
                                    model.addType(type);
                                }
                            }
                        }
                    }
                }
            }
        }

        return brand;
    }

    public List<Brand> getBrands() {
        List<Brand> brands = null;

        VehiclesMng vehicles = url2Vehicles(getURL("0", Level.ROOT));

        if (vehicles != null && vehicles.getVehicles() != null && !vehicles.getVehicles().isEmpty()) {
            brands = new LinkedList<>();
            for (Child child : vehicles.getVehicles().getFirst().getChildren()) {
                brands.add(new Brand(child.getId(), child.getLabels().getLabel().get(lang), child.getLabels().getFullLabelFragment().get(lang)));
            }
        }

        return brands;
    }

    protected List<Family> getFamilies4Brand(String brandId) {
        List<Family> families = null;

        VehiclesMng vehicles = url2Vehicles(getURL(brandId, Level.BRAND));

        if (vehicles != null && vehicles.getVehicles() != null && !vehicles.getVehicles().isEmpty()) {
            families = new LinkedList<>();
            for (Child child : vehicles.getVehicles().getFirst().getChildren()) {
                families.add(new Family(child.getId(), child.getLabels().getFullLabelFragment().get(lang)));
            }
        }

        return families;
    }

    protected List<Model> getModels4Family(String familyId) {
        List<Model> models = null;

        VehiclesMng vehicles = url2Vehicles(getURL(familyId, Level.FAMILY));

        if (vehicles != null && vehicles.getVehicles() != null && !vehicles.getVehicles().isEmpty()) {
            models = new LinkedList<>();
            for (Child child : vehicles.getVehicles().getFirst().getChildren()) {
                models.add(new Model(child.getId(), child.getLabels().getFullLabelFragment().get(lang)));
            }
        }

        return models;
    }

    protected List<Type> getTypes4Model(String modelId) {
        List<Type> types = null;

        VehiclesMng vehicles = url2Vehicles(getURL(modelId, Level.MODEL));

        if (vehicles != null && vehicles.getVehicles() != null && !vehicles.getVehicles().isEmpty()) {
            types = new LinkedList<>();
            for (Child child : vehicles.getVehicles().getFirst().getChildren()) {
                types.add(new Type(child.getId(), child.getLabels().getFullLabelFragment().get(lang)));
            }
        }

        return types;
    }

    protected Type getTypeDetails(String typeId) {
        Type type = null;

        VehiclesMng vehicles = url2Vehicles(getURL(typeId, Level.TYPE));

        if (vehicles != null && vehicles.getVehicles() != null && !vehicles.getVehicles().isEmpty()) {
            Vehicle child = vehicles.getVehicles().getFirst();
            type = new Type(child.getId(),
                    child.getLabels().getCoreLabel().get(lang),
                    child.getLabels().getComplementLabel().get(lang),
                    child.getLabels().getFullLabelFragment().get(lang),
                    child.getLabels().getFullLabel().get(lang),
                    child.getEnergy().getLabel().get(lang)
            );

            for (Ancestor ancestor : child.getAncestors()) {
                type.addAncestor(ancestor.getId());
            }
        }

        return type;
    }


    protected abstract VehiclesMng url2Vehicles(URL url);

    /**
     * Generate the URL to recover the vehicle information
     * URL examples:
     * <ul>
     *     <li><a href="https://www.oscaro.com/xhr/nav/vehicles/fr/fr?vehicles-id=0&tree-level=root&init=true&page-type=home">Brands (all)</a></li>
     *     <li><a href="https://www.oscaro.com/xhr/nav/vehicles/fr/fr?vehicles-id=ma-178&tree-level=brand&page-type=home">Families for Brand</a></li>
     *     <li><a href="https://www.oscaro.com/xhr/nav/vehicles/fr/fr?vehicles-id=fa-650&tree-level=family&page-type=home">Models for Family</a></li>
     *     <li><a href="https://www.oscaro.com/xhr/nav/vehicles/fr/fr?vehicles-id=mo-7174&tree-level=model&page-type=home">Types for Model</a></li>
     *     <li><a href="https://www.oscaro.com/xhr/nav/vehicles/fr/fr?vehicles-id=63833&tree-level=type&page-type=home">type</a></li>
     * </ul>
     *
     * @param id    - Vehicle identifier
     * @param level - Information level. Possible values: root, brand, family, model
     * @return URL to recover
     */
    protected URL getURL(String id, Level level) {
        URL url = null;

        String urlStr = BASE_URL + VEHICLES_URL
                .replace(ID_PLACEHOLDER, id)
                .replace(LEVEL_PLACEHOLDER, level.toString())
                .replace(LANG_PLACEHOLDER, lang);

        if (level.toString().compareToIgnoreCase("root") == 0) {
            urlStr += "&init=true";
        }

        try {
            url = new URI(urlStr).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            log.error("", e);
        }

        return url;
    }

    protected enum Level {
        ROOT("root"),
        BRAND("brand"),
        FAMILY("family"),
        MODEL("model"),
        TYPE("type");

        private final String label;

        Level(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
