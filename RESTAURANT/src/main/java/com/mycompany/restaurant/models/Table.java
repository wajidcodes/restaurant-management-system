package com.mycompany.restaurant.models;

import javafx.beans.property.*;

/**
 * Table model representing a restaurant table
 */
public class Table {
    private final IntegerProperty tableNumber;
    private final IntegerProperty capacity;
    private final StringProperty status; // AVAILABLE, OCCUPIED, RESERVED, CLEANING
    private final StringProperty assignedServer;
    private final IntegerProperty partySize;
    private final LongProperty seatedTime;

    public Table(int tableNumber, int capacity) {
        this.tableNumber = new SimpleIntegerProperty(tableNumber);
        this.capacity = new SimpleIntegerProperty(capacity);
        this.status = new SimpleStringProperty("AVAILABLE");
        this.assignedServer = new SimpleStringProperty("");
        this.partySize = new SimpleIntegerProperty(0);
        this.seatedTime = new SimpleLongProperty(0);
    }

    // Properties for JavaFX binding
    public IntegerProperty tableNumberProperty() {
        return tableNumber;
    }

    public IntegerProperty capacityProperty() {
        return capacity;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public StringProperty assignedServerProperty() {
        return assignedServer;
    }

    public IntegerProperty partySizeProperty() {
        return partySize;
    }

    public LongProperty seatedTimeProperty() {
        return seatedTime;
    }

    // Getters
    public int getTableNumber() {
        return tableNumber.get();
    }

    public int getCapacity() {
        return capacity.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getAssignedServer() {
        return assignedServer.get();
    }

    public int getPartySize() {
        return partySize.get();
    }

    public long getSeatedTime() {
        return seatedTime.get();
    }

    // Setters
    public void setTableNumber(int value) {
        tableNumber.set(value);
    }

    public void setCapacity(int value) {
        capacity.set(value);
    }

    public void setStatus(String value) {
        status.set(value);
    }

    public void setAssignedServer(String value) {
        assignedServer.set(value);
    }

    public void setPartySize(int value) {
        partySize.set(value);
    }

    public void setSeatedTime(long value) {
        seatedTime.set(value);
    }

    // Business methods
    public void seatParty(int size, String server) {
        this.partySize.set(size);
        this.assignedServer.set(server);
        this.status.set("OCCUPIED");
        this.seatedTime.set(System.currentTimeMillis());
    }

    public void clearTable() {
        this.partySize.set(0);
        this.assignedServer.set("");
        this.status.set("CLEANING");
        this.seatedTime.set(0);
    }

    public void makeAvailable() {
        this.status.set("AVAILABLE");
    }

    public void reserve() {
        this.status.set("RESERVED");
    }

    public long getOccupiedMinutes() {
        if (seatedTime.get() == 0)
            return 0;
        return (System.currentTimeMillis() - seatedTime.get()) / 60000;
    }
}
