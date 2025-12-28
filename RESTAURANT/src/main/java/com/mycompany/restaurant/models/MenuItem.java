package com.mycompany.restaurant.models;

import javafx.beans.property.*;

/**
 * MenuItem model for restaurant menu
 */
public class MenuItem {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty category;
    private final DoubleProperty price;
    private final StringProperty description;
    private final BooleanProperty available;

    public MenuItem(int id, String name, String category, double price, String description) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.price = new SimpleDoubleProperty(price);
        this.description = new SimpleStringProperty(description);
        this.available = new SimpleBooleanProperty(true);
    }

    // Properties
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public BooleanProperty availableProperty() {
        return available;
    }

    // Getters
    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getCategory() {
        return category.get();
    }

    public double getPrice() {
        return price.get();
    }

    public String getDescription() {
        return description.get();
    }

    public boolean isAvailable() {
        return available.get();
    }

    // Setters
    public void setName(String value) {
        name.set(value);
    }

    public void setCategory(String value) {
        category.set(value);
    }

    public void setPrice(double value) {
        price.set(value);
    }

    public void setDescription(String value) {
        description.set(value);
    }

    public void setAvailable(boolean value) {
        available.set(value);
    }

    public String getPriceFormatted() {
        return String.format("Rs. %.0f", price.get());
    }
}
