package models.entity;

import enums.ComputerStatus;

import java.time.LocalDateTime;

public class Computer {
    private int id;
    private String name;
    private String configuration;
    private Category category;
    private double pricePerHours;
    private ComputerStatus status;
    private LocalDateTime createdAt;

    public Computer(int id, String name, String configuration, Category category, double pricePerHours, ComputerStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.configuration = configuration;
        this.category = category;
        this.pricePerHours = pricePerHours;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getPricePerHours() {
        return pricePerHours;
    }

    public void setPricePerHours(double pricePerHours) {
        this.pricePerHours = pricePerHours;
    }

    public ComputerStatus getStatus() {
        return status;
    }

    public void setStatus(ComputerStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("| %-8d | %-20s | %-30s | %-10s | %-12.2f | %-15s | %-15s |",
                id, name, configuration, category.getName(), pricePerHours, status, createdAt);
    }
}
