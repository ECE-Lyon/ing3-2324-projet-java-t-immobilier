package models;

// Définir une classe pour représenter une propriété immobilière
public class Property {
    private final String address;
    private final String type;
    private final String price;

    public Property(String address, String type, String price) {
        this.address = address;
        this.type = type;
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public String getPrice() {
        return price;
    }
}