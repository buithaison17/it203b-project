package models.dto;

import enums.ComputerStatus;
import models.entity.Category;

public class ComputerDTO {
    private int id;
    private String name;
    private String configuration;
    private double price;
    private Category category;

    public ComputerDTO(int id, String name, String configuration, double price, Category category) {
        this.id = id;
        this.name = name;
        this.configuration = configuration;
        this.price = price;
        this.category = category;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return String.format("| %-8d | %-20s | %-30s | %-10.2f | %-15s |",
                id, name, configuration, price, category.getName());
    }
}
