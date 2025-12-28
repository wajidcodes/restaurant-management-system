package com.mycompany.restaurant.models;

import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Payment model for transaction tracking
 */
public class Payment {
    private final IntegerProperty paymentId;
    private final IntegerProperty orderId;
    private final DoubleProperty subtotal;
    private final DoubleProperty tax;
    private final DoubleProperty tip;
    private final DoubleProperty discount;
    private final DoubleProperty totalAmount;
    private final StringProperty paymentMethod; // CASH, CARD, DIGITAL, SPLIT
    private final StringProperty status; // PENDING, COMPLETED, REFUNDED, CANCELLED
    private final ObjectProperty<LocalDateTime> paymentTime;
    private final StringProperty cardLastFour;
    private final StringProperty transactionId;

    public Payment(int paymentId, int orderId, double subtotal) {
        this.paymentId = new SimpleIntegerProperty(paymentId);
        this.orderId = new SimpleIntegerProperty(orderId);
        this.subtotal = new SimpleDoubleProperty(subtotal);
        this.tax = new SimpleDoubleProperty(0.0);
        this.tip = new SimpleDoubleProperty(0.0);
        this.discount = new SimpleDoubleProperty(0.0);
        this.totalAmount = new SimpleDoubleProperty(subtotal);
        this.paymentMethod = new SimpleStringProperty("CASH");
        this.status = new SimpleStringProperty("PENDING");
        this.paymentTime = new SimpleObjectProperty<>(LocalDateTime.now());
        this.cardLastFour = new SimpleStringProperty("");
        this.transactionId = new SimpleStringProperty("");
    }

    // Properties
    public IntegerProperty paymentIdProperty() {
        return paymentId;
    }

    public IntegerProperty orderIdProperty() {
        return orderId;
    }

    public DoubleProperty subtotalProperty() {
        return subtotal;
    }

    public DoubleProperty taxProperty() {
        return tax;
    }

    public DoubleProperty tipProperty() {
        return tip;
    }

    public DoubleProperty discountProperty() {
        return discount;
    }

    public DoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    public StringProperty paymentMethodProperty() {
        return paymentMethod;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public ObjectProperty<LocalDateTime> paymentTimeProperty() {
        return paymentTime;
    }

    // Getters
    public int getPaymentId() {
        return paymentId.get();
    }

    public int getOrderId() {
        return orderId.get();
    }

    public double getSubtotal() {
        return subtotal.get();
    }

    public double getTax() {
        return tax.get();
    }

    public double getTip() {
        return tip.get();
    }

    public double getDiscount() {
        return discount.get();
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public String getPaymentMethod() {
        return paymentMethod.get();
    }

    public String getStatus() {
        return status.get();
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime.get();
    }

    public String getCardLastFour() {
        return cardLastFour.get();
    }

    public String getTransactionId() {
        return transactionId.get();
    }

    // Setters
    public void setTax(double value) {
        tax.set(value);
        recalculateTotal();
    }

    public void setTip(double value) {
        tip.set(value);
        recalculateTotal();
    }

    public void setDiscount(double value) {
        discount.set(value);
        recalculateTotal();
    }

    public void setPaymentMethod(String value) {
        paymentMethod.set(value);
    }

    public void setStatus(String value) {
        status.set(value);
    }

    public void setCardLastFour(String value) {
        cardLastFour.set(value);
    }

    public void setTransactionId(String value) {
        transactionId.set(value);
    }

    // Business methods
    private void recalculateTotal() {
        double total = subtotal.get() + tax.get() + tip.get() - discount.get();
        totalAmount.set(Math.max(0, total));
    }

    public void applyTax(double taxRate) {
        tax.set(subtotal.get() * taxRate);
        recalculateTotal();
    }

    public void applyDiscount(double discountAmount) {
        discount.set(discountAmount);
        recalculateTotal();
    }

    public void complete() {
        status.set("COMPLETED");
        paymentTime.set(LocalDateTime.now());
    }

    public void refund() {
        status.set("REFUNDED");
    }

    public void cancel() {
        status.set("CANCELLED");
    }

    public String getFormattedTotal() {
        return String.format("Rs. %.2f", totalAmount.get());
    }

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return paymentTime.get().format(formatter);
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return paymentTime.get().format(formatter);
    }
}
