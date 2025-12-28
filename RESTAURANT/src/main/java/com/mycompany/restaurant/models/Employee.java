package com.mycompany.restaurant.models;

import javafx.beans.property.*;

/**
 * Employee model - Enhanced version for staff management
 */
public class Employee {
    private final IntegerProperty employeeId;
    private final StringProperty name;
    private final StringProperty address;
    private final StringProperty email;
    private final StringProperty phone;
    private final IntegerProperty age;
    private final StringProperty role; // Manager, Chef, Waiter, Cashier, Cleaner
    private final DoubleProperty hourlyRate;
    private final IntegerProperty hoursWorked;
    private final BooleanProperty isActive;
    private final ObjectProperty<java.time.LocalDate> hireDate;

    public Employee(int employeeId, String name, String email, String phone, String role) {
        this.employeeId = new SimpleIntegerProperty(employeeId);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty("");
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.age = new SimpleIntegerProperty(0);
        this.role = new SimpleStringProperty(role);
        this.hourlyRate = new SimpleDoubleProperty(0.0);
        this.hoursWorked = new SimpleIntegerProperty(0);
        this.isActive = new SimpleBooleanProperty(true);
        this.hireDate = new SimpleObjectProperty<>(java.time.LocalDate.now());
    }

    // Properties
    public IntegerProperty employeeIdProperty() {
        return employeeId;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public StringProperty roleProperty() {
        return role;
    }

    public DoubleProperty hourlyRateProperty() {
        return hourlyRate;
    }

    public IntegerProperty hoursWorkedProperty() {
        return hoursWorked;
    }

    public BooleanProperty isActiveProperty() {
        return isActive;
    }

    // Getters
    public int getEmployeeId() {
        return employeeId.get();
    }

    public String getName() {
        return name.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getPhone() {
        return phone.get();
    }

    public int getAge() {
        return age.get();
    }

    public String getRole() {
        return role.get();
    }

    public double getHourlyRate() {
        return hourlyRate.get();
    }

    public int getHoursWorked() {
        return hoursWorked.get();
    }

    public boolean isActive() {
        return isActive.get();
    }

    public java.time.LocalDate getHireDate() {
        return hireDate.get();
    }

    // Setters
    public void setName(String value) {
        name.set(value);
    }

    public void setAddress(String value) {
        address.set(value);
    }

    public void setEmail(String value) {
        email.set(value);
    }

    public void setPhone(String value) {
        phone.set(value);
    }

    public void setAge(int value) {
        age.set(value);
    }

    public void setRole(String value) {
        role.set(value);
    }

    public void setHourlyRate(double value) {
        hourlyRate.set(value);
    }

    public void setHoursWorked(int value) {
        hoursWorked.set(value);
    }

    public void setActive(boolean value) {
        isActive.set(value);
    }

    public void setHireDate(java.time.LocalDate value) {
        hireDate.set(value);
    }

    // Business methods
    public double calculateSalary() {
        return hourlyRate.get() * hoursWorked.get();
    }

    public void addHours(int hours) {
        hoursWorked.set(hoursWorked.get() + hours);
    }

    public void resetHours() {
        hoursWorked.set(0);
    }

    public String getFormattedSalary() {
        return String.format("Rs. %.2f", calculateSalary());
    }
}
