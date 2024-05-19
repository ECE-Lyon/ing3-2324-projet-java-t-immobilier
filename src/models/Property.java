package models;

public class Property {
    private int idProperty;
    private double size;
    private String description;
    private double price;
    private boolean statusSold;
    private boolean hasPool;
    private boolean hasGarden;
    private String propertyType;
    private int numberOfRooms;
    private boolean programVisit;
    private int idBuyer;
    private int idSeller;
    private int idVisit;
    private int idEmployee;
    private String title;
    private String imagePath;
    private String city;

    public Property(int idProperty, double size, String description, double price, boolean statusSold, boolean hasPool, boolean hasGarden, String propertyType, int numberOfRooms, boolean programVisit, int idBuyer, int idSeller, int idVisit, int idEmployee, String title, String imagePath,String city) {
        this.idProperty = idProperty;
        this.size = size;
        this.description = description;
        this.price = price;
        this.statusSold = statusSold;
        this.hasPool = hasPool;
        this.hasGarden = hasGarden;
        this.propertyType = propertyType;
        this.numberOfRooms = numberOfRooms;
        this.programVisit = programVisit;
        this.idBuyer = idBuyer;
        this.idSeller = idSeller;
        this.idVisit = idVisit;
        this.idEmployee = idEmployee;
        this.imagePath= imagePath;
        this.title= title;
        this.city = city;
    }

    public Property(int id, String streetNumber, String streetName, String postalCode, String city, double price, double size, int rooms, boolean garden, boolean pool, String type) {
    }


    // Getters et setters
    public int getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(int idProperty) {
        this.idProperty = idProperty;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isStatusSold() {
        return statusSold;
    }

    public void setStatusSold(boolean statusSold) {
        this.statusSold = statusSold;
    }

    public boolean isHasPool() {
        return hasPool;
    }

    public void setHasPool(boolean hasPool) {
        this.hasPool = hasPool;
    }

    public boolean isHasGarden() {
        return hasGarden;
    }

    public void setHasGarden(boolean hasGarden) {
        this.hasGarden = hasGarden;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public boolean isProgramVisit() {
        return programVisit;
    }

    public void setProgramVisit(boolean programVisit) {
        this.programVisit = programVisit;
    }

    public int getIdBuyer() {
        return idBuyer;
    }

    public void setIdBuyer(int idBuyer) {
        this.idBuyer = idBuyer;
    }

    public int getIdSeller() {
        return idSeller;
    }

    public void setIdSeller(int idSeller) {
        this.idSeller = idSeller;
    }

    public int getIdVisit() {
        return idVisit;
    }

    public void setIdVisit(int idVisit) {
        this.idVisit = idVisit;
    }

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;    }


}
