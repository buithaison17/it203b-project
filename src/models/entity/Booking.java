package models.entity;

import java.time.LocalDateTime;

public class Booking {
    private int id;
    private int customerId;
    private int computerId;
    private double hours;
    private LocalDateTime createdAt;

    public Booking(int id, int customerId, int computerId, double hours, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.computerId = computerId;
        this.hours = hours;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getComputerId() {
        return computerId;
    }

    public void setComputerId(int computerId) {
        this.computerId = computerId;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
