package entity;

import enums.ComputerStatus;

import java.time.LocalDateTime;

public class Computer {
    private int id;
    private String name;
    private String configuration;
    private double price;
    private ComputerStatus status;
    private LocalDateTime createdAt;

    public Computer(int id, String name, String configuration, double price, ComputerStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.configuration = configuration;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
}
