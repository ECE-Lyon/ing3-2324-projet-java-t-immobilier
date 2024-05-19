package models;

public class Address  {
    private int numberStreet;

    private String city;

    private String nameStreet;

    private int postalCode;

    public Address(int numberStreet, String city, String nameStreet, int postalCode) {
        this.numberStreet = numberStreet;
        this.city = city;
        this.nameStreet = nameStreet;
        this.postalCode = postalCode;

    }
    public String getCity() {
        return city;
    }
    public int getPostalCode(){
        return postalCode;
    }
    public String getStreetName(){
        return nameStreet;
    }
    public int getStreetNumber(){
        return numberStreet;
    }
}
