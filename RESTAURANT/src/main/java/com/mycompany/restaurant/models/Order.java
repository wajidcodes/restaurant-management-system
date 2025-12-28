package com.mycompany.restaurant.models;

import javafx.beans.property.*;
import javafx.collections.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Order model for restaurant orders
 */
public class Order {
    private final IntegerProperty orderId;
    private final IntegerProperty tableNumber;
    private final ObservableList<MenuItem> items;
    private final DoubleProperty totalAmount;
    private final StringProperty status; // NEW, PREPARING, READY, SERVED
    private final ObjectProperty<LocalDateTime> orderTime;
    private final StringProperty specialInstructions;

    public Order(int orderId, int tableNumber) {
        this.orderId = new SimpleIntegerProperty(orderId);
        this.tableNumber = new SimpleIntegerProperty(tableNumber);
        this.items = FXCollections.observableArrayList();
        this.totalAmount = new SimpleDoubleProperty(0.0);
        this.status = new SimpleStringProperty("NEW");
        this.orderTime = new SimpleObjectProperty<>(LocalDateTime.now());
        this.specialInstructions = new SimpleStringProperty("");
    }

    // Properties
    public IntegerProperty orderIdProperty() {
        return orderId;
    }

    public IntegerProperty tableNumberProperty() {
        return tableNumber;
    }

    public DoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public ObjectProperty<LocalDateTime> orderTimeProperty() {
        return orderTime;
    }

    public StringProperty specialInstructionsProperty() {
        return specialInstructions;
    }

    // Getters
    public int getOrderId() {
        return orderId.get();
    }

    public int getTableNumber() {
        return tableNumber.get();
    }

    public ObservableList<MenuItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public String getStatus() {
        return status.get();
    }

    public LocalDateTime getOrderTime() {
        return orderTime.get();
    }

    public String getSpecialInstructions() {
        return specialInstructions.get();
    }

    // Setters
    public void setStatus(String value) {
        status.set(value);
    }

    public void setSpecialInstructions(String value) {
        specialInstructions.set(value);
    }

    // Business methods
    public void addItem(MenuItem item) {
        items.add(item);
        recalculateTotal();
    }

    public void removeItem(MenuItem item) {
        items.remove(item);
        recalculateTotal();
    }

    private void recalculateTotal() {
        double total = items.stream().mapToDouble(MenuItem::getPrice).sum();
        totalAmount.set(total);
    }

    public long getMinutesSinceOrder() {
        return java.time.Duration.between(orderTime.get(), LocalDateTime.now()).toMinutes();
    }

    public String getOrderTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return orderTime.get().format(formatter);
    }

    public String getTotalFormatted() {
        return String.format("Rs. %.0f", totalAmount.get());
    }
}
