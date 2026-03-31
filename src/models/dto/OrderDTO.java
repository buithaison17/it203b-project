package models.dto;

import enums.OrderStatus;
import utils.FormatDate;

import java.time.LocalDateTime;

public class OrderDTO {
    private int id;
    private int customerName;
    private double totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public OrderDTO(int id, int customerName, double totalPrice, OrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerName() {
        return customerName;
    }

    public void setCustomerName(int customerName) {
        this.customerName = customerName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
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
        return String.format("| %-6d | %-20s | %-12.2f | %-12s | %-19s |",
                id,
                customerName,
                totalPrice,
                status,
                FormatDate.formatDateTime(createdAt)
        );
    }
}
