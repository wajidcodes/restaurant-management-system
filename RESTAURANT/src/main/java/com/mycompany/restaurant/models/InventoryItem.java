package com.mycompany.restaurant.models;

import javafx.beans.property.*;
import java.time.LocalDate;

/**
 * InventoryItem model for stock management
 */
public class InventoryItem {
    private final IntegerProperty itemId;
    private final StringProperty itemName;
    private final StringProperty category; // Ingredients, Beverages, Supplies, Equipment
    private final DoubleProperty currentStock;
    private final DoubleProperty minStock;
    private final StringProperty unit; // kg, liters, pieces, etc.
    private final DoubleProperty unitPrice;
    private final StringProperty supplier;
    private final ObjectProperty<LocalDate> expiryDate;
    private final ObjectProperty<LocalDate> lastRestocked;

    public InventoryItem(int itemId, String itemName, String category, double currentStock,
            double minStock, String unit) {
        this.itemId = new SimpleIntegerProperty(itemId);
        this.itemName = new SimpleStringProperty(itemName);
        this.category = new SimpleStringProperty(category);
        this.currentStock = new SimpleDoubleProperty(currentStock);
        this.minStock = new SimpleDoubleProperty(minStock);
        this.unit = new SimpleStringProperty(unit);
        this.unitPrice = new SimpleDoubleProperty(0.0);
        this.supplier = new SimpleStringProperty("");
        this.expiryDate = new SimpleObjectProperty<>(null);
        this.lastRestocked = new SimpleObjectProperty<>(LocalDate.now());
    }

    // Constructor matching usage in InventoryManagementView
    public InventoryItem(int itemId, String itemName, double currentStock, String unit, double minStock) {
        this(itemId, itemName, "General", currentStock, minStock, unit);
    }

    // Properties
    public IntegerProperty itemIdProperty() {
        return itemId;
    }

    public StringProperty itemNameProperty() {
        return itemName;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public DoubleProperty currentStockProperty() {
        return currentStock;
    }

    public DoubleProperty minStockProperty() {
        return minStock;
    }

    public StringProperty unitProperty() {
        return unit;
    }

    public DoubleProperty unitPriceProperty() {
        return unitPrice;
    }

    public StringProperty supplierProperty() {
        return supplier;
    }

    public ObjectProperty<LocalDate> expiryDateProperty() {
        return expiryDate;
    }

    public ObjectProperty<LocalDate> lastRestockedProperty() {
        return lastRestocked;
    }

    // Getters
    public int getItemId() {
        return itemId.get();
    }

    public String getItemName() {
        return itemName.get();
    }

    public String getCategory() {
        return category.get();
    }

    public double getCurrentStock() {
        return currentStock.get();
    }

    public double getMinStock() {
        return minStock.get();
    }

    public String getUnit() {
        return unit.get();
    }

    public double getUnitPrice() {
        return unitPrice.get();
    }

    public String getSupplier() {
        return supplier.get();
    }

    public LocalDate getExpiryDate() {
        return expiryDate.get();
    }

    public LocalDate getLastRestocked() {
        return lastRestocked.get();
    }

    // Setters
    public void setItemName(String value) {
        itemName.set(value);
    }

    public void setCategory(String value) {
        category.set(value);
    }

    public void setCurrentStock(double value) {
        currentStock.set(value);
    }

    public void setMinStock(double value) {
        minStock.set(value);
    }

    public void setUnit(String value) {
        unit.set(value);
    }

    public void setUnitPrice(double value) {
        unitPrice.set(value);
    }

    public void setSupplier(String value) {
        supplier.set(value);
    }

    public void setExpiryDate(LocalDate value) {
        expiryDate.set(value);
    }

    public void setLastRestocked(LocalDate value) {
        lastRestocked.set(value);
    }

    // Business methods
    public boolean isLowStock() {
        return currentStock.get() <= minStock.get();
    }

    public boolean isExpiringSoon() {
        if (expiryDate.get() == null)
            return false;
        return expiryDate.get().isBefore(LocalDate.now().plusDays(7));
    }

    public boolean isExpired() {
        if (expiryDate.get() == null)
            return false;
        return expiryDate.get().isBefore(LocalDate.now());
    }

    public void addStock(double quantity) {
        currentStock.set(currentStock.get() + quantity);
        lastRestocked.set(LocalDate.now());
    }

    public void reduceStock(double quantity) {
        double newStock = currentStock.get() - quantity;
        currentStock.set(Math.max(0, newStock));
    }

    public double getStockValue() {
        return currentStock.get() * unitPrice.get();
    }

    public String getFormattedStock() {
        return String.format("%.2f %s", currentStock.get(), unit.get());
    }

    public String getStockStatus() {
        if (isExpired())
            return "EXPIRED";
        if (isExpiringSoon())
            return "EXPIRING";
        if (isLowStock())
            return "LOW";
        return "OK";
    }
}
