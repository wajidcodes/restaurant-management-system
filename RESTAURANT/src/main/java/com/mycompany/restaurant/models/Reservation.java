package com.mycompany.restaurant.models;

import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Reservation model for managing table bookings
 */
public class Reservation {
    private final IntegerProperty reservationId;
    private final StringProperty customerName;
    private final StringProperty phoneNumber;
    private final IntegerProperty partySize;
    private final ObjectProperty<LocalDateTime> reservationDateTime;
    private final IntegerProperty tableNumber;
    private final StringProperty status; // PENDING, CONFIRMED, SEATED, CANCELLED, NO_SHOW
    private final StringProperty specialRequests;

    public Reservation(int reservationId, String customerName, String phoneNumber,
            int partySize, LocalDateTime dateTime) {
        this.reservationId = new SimpleIntegerProperty(reservationId);
        this.customerName = new SimpleStringProperty(customerName);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.partySize = new SimpleIntegerProperty(partySize);
        this.reservationDateTime = new SimpleObjectProperty<>(dateTime);
        this.tableNumber = new SimpleIntegerProperty(0);
        this.status = new SimpleStringProperty("PENDING");
        this.specialRequests = new SimpleStringProperty("");
    }

    // Properties
    public IntegerProperty reservationIdProperty() {
        return reservationId;
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public IntegerProperty partySizeProperty() {
        return partySize;
    }

    public ObjectProperty<LocalDateTime> reservationDateTimeProperty() {
        return reservationDateTime;
    }

    public IntegerProperty tableNumberProperty() {
        return tableNumber;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public StringProperty specialRequestsProperty() {
        return specialRequests;
    }

    // Getters
    public int getReservationId() {
        return reservationId.get();
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public int getPartySize() {
        return partySize.get();
    }

    public LocalDateTime getReservationDateTime() {
        return reservationDateTime.get();
    }

    public int getTableNumber() {
        return tableNumber.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getSpecialRequests() {
        return specialRequests.get();
    }

    // Setters
    public void setCustomerName(String value) {
        customerName.set(value);
    }

    public void setPhoneNumber(String value) {
        phoneNumber.set(value);
    }

    public void setPartySize(int value) {
        partySize.set(value);
    }

    public void setReservationDateTime(LocalDateTime value) {
        reservationDateTime.set(value);
    }

    public void setTableNumber(int value) {
        tableNumber.set(value);
    }

    public void setStatus(String value) {
        status.set(value);
    }

    public void setSpecialRequests(String value) {
        specialRequests.set(value);
    }

    // Business methods
    public void confirm(int tableNumber) {
        this.tableNumber.set(tableNumber);
        this.status.set("CONFIRMED");
    }

    public void markSeated() {
        this.status.set("SEATED");
    }

    public void cancel() {
        this.status.set("CANCELLED");
    }

    public void markNoShow() {
        this.status.set("NO_SHOW");
    }

    public String getFormattedDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        return reservationDateTime.get().format(formatter);
    }

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return reservationDateTime.get().format(formatter);
    }

    public boolean isToday() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime resDate = reservationDateTime.get();
        return now.toLocalDate().equals(resDate.toLocalDate());
    }
}
