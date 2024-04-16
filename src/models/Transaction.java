package models;

import javafx.beans.property.*;

import java.util.Date;

public class Transaction  {
    private final IntegerProperty idProperty;
    private final StringProperty propertyDescription;
    private final DoubleProperty propertyPrice;
    private final ObjectProperty<Date> transactionDate;

    public Transaction(int idProperty, String propertyDescription, double propertyPrice, Date transactionDate) {
        this.idProperty = new SimpleIntegerProperty(idProperty);
        this.propertyDescription = new SimpleStringProperty(propertyDescription);
        this.propertyPrice = new SimpleDoubleProperty(propertyPrice);
        this.transactionDate = new SimpleObjectProperty<>(transactionDate);
    }

    public int getIdProperty() {
        return idProperty.get();
    }

    public IntegerProperty idPropertyProperty() {
        return idProperty;
    }

    public String getPropertyDescription() {
        return propertyDescription.get();
    }

    public StringProperty propertyDescriptionProperty() {
        return propertyDescription;
    }

    public double getPropertyPrice() {
        return propertyPrice.get();
    }

    public DoubleProperty propertyPriceProperty() {
        return propertyPrice;
    }

    public Date getTransactionDate() {
        return transactionDate.get();
    }

    public ObjectProperty<Date> transactionDateProperty() {
        return transactionDate;
    }
}
