package models.dto;

import enums.BookingStatus;
import utils.FormatDate;

import java.time.LocalDateTime;

public class BookingDTO {
    private int id;
    private String customerName;
    private String computerName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalPrice;
    private BookingStatus status;
    private LocalDateTime createdAt;

    public BookingDTO(int id, String customerName, String computerName, LocalDateTime startTime, LocalDateTime endTime, double totalPrice, BookingStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.customerName = customerName;
        this.computerName = computerName;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
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
        return String.format("| %-6d | %-10s | %-10s | %-13s | %-13s | %-15.2f | %-10s | %-15s |%n", id, customerName, computerName, startTime.toString(), endTime.toString(), totalPrice, status.toString(), FormatDate.formatDateTime(createdAt));
    }

    public String toStringNotCreatedAt() {
        return String.format("| %-6d | %-10s | %-10s | %-13s | %-13s | %-15.2f | %-10s |%n", id, customerName, computerName, FormatDate.formatDateTime(startTime), FormatDate.formatDateTime(endTime), totalPrice, status.toString());
    }
}
