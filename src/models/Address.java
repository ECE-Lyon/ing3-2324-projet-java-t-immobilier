package models;

public class Address {
    private int idAddress;
    private int numberStreet;
    private String city;
    private String postalCode;
    private String nameStreet;
    private int idProperty;

    public Address(int idAddress, int numberStreet, String city, String postalCode, String nameStreet, int idProperty) {
        this.idAddress = idAddress;
        this.numberStreet = numberStreet;
        this.city = city;
        this.postalCode = postalCode;
        this.nameStreet = nameStreet;
        this.idProperty = idProperty;
    }

    public int getIdAddress() {
        return idAddress;
    }

    public int getNumberStreet() {
        return numberStreet;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getNameStreet() {
        return nameStreet;
    }

    public int getIdProperty() {
        return idProperty;
    }
}
