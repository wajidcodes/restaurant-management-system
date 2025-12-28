package com.mycompany.restaurant.models;

import javafx.beans.property.*;
import javafx.collections.*;

/**
 * Category model for menu organization
 */
public class Category {
    private final IntegerProperty categoryId;
    private final StringProperty name;
    private final StringProperty description;
    private final BooleanProperty isActive;
    private final ObservableList<MenuItem> items;
    private final IntegerProperty displayOrder;

    public Category(int categoryId, String name, String description) {
        this.categoryId = new SimpleIntegerProperty(categoryId);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.isActive = new SimpleBooleanProperty(true);
        this.items = FXCollections.observableArrayList();
        this.displayOrder = new SimpleIntegerProperty(0);
    }

    // Properties
    public IntegerProperty categoryIdProperty() {
        return categoryId;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public BooleanProperty isActiveProperty() {
        return isActive;
    }

    public IntegerProperty displayOrderProperty() {
        return displayOrder;
    }

    // Getters
    public int getCategoryId() {
        return categoryId.get();
    }

    public String getName() {
        return name.get();
    }

    public String getDescription() {
        return description.get();
    }

    public boolean isActive() {
        return isActive.get();
    }

    public ObservableList<MenuItem> getItems() {
        return items;
    }

    public int getDisplayOrder() {
        return displayOrder.get();
    }

    // Setters
    public void setName(String value) {
        name.set(value);
    }

    public void setDescription(String value) {
        description.set(value);
    }

    public void setActive(boolean value) {
        isActive.set(value);
    }

    public void setDisplayOrder(int value) {
        displayOrder.set(value);
    }

    // Business methods
    public void addItem(MenuItem item) {
        items.add(item);
    }

    public void removeItem(MenuItem item) {
        items.remove(item);
    }

    public int getItemCount() {
        return items.size();
    }

    public int getAvailableItemCount() {
        return (int) items.stream().filter(MenuItem::isAvailable).count();
    }
}
