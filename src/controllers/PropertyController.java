// PropertySearchController.java
package controllers;

import dao.PropertyDAO;
import models.Property;
import java.util.List;

public class PropertyController {
    private final PropertyDAO propertyDAO;

    public PropertyController() {
        this.propertyDAO = new PropertyDAO();
    }

    public List<Property> searchProperties(String city, String propertyType, double budget, double size, boolean hasPool, boolean hasGarden) {
        return propertyDAO.searchProperties(city, propertyType, budget, size, hasPool, hasGarden);
    }
}
