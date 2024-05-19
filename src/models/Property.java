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
    private int nbRoom;
    private boolean programVisit;
    private int idClient;
    private int idEmployee;

    public Property(int idProperty, double size, String description, double price, boolean statusSold, boolean hasPool, boolean hasGarden, String propertyType, int nbRoom, boolean programVisit, int idClient, int idEmployee) {
        this.idProperty = idProperty;
        this.size = size;
        this.description = description;
        this.price = price;
        this.statusSold = statusSold;
        this.hasPool = hasPool;
        this.hasGarden = hasGarden;
        this.propertyType = propertyType;
        this.nbRoom = nbRoom;
        this.programVisit = programVisit;
        this.idClient = idClient;
        this.idEmployee = idEmployee;
    }

    public int getIdProperty() {
        return idProperty;
    }

    public double getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public boolean isStatusSold() {
        return statusSold;
    }

    public boolean isHasPool() {
        return hasPool;
    }

    public boolean isHasGarden() {
        return hasGarden;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public int getNbRoom() {
        return nbRoom;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatusSold(boolean statusSold) {
        this.statusSold = statusSold;
    }

    public void setHasPool(boolean hasPool) {
        this.hasPool = hasPool;
    }

    public void setHasGarden(boolean hasGarden) {
        this.hasGarden = hasGarden;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public void setNbRoom(int nbRoom) {
        this.nbRoom = nbRoom;
    }

    public void setProgramVisit(boolean programVisit) {
        this.programVisit = programVisit;
    }

    public boolean isProgramVisit() {
        return programVisit;
    }

    public int getIdClient() {
        return idClient;
    }

    public int getIdEmployee() {
        return idEmployee;
    }
}
