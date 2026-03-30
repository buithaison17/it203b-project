package models.dto;

import java.time.LocalDateTime;

public class UserDTO {
    private int id;
    private String fullName;
    private String email;
    private String role;
    private double balance;
    private LocalDateTime createdAt;

    public UserDTO(int id, String fullName, String email, String role, double balance, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("| %-7s | %-15s | %-20s | %-10s | %-10.2f | %-20s |",
                id, fullName, email, role, balance, createdAt);
    }

    public String toStringNotCreatedAtAndRole() {
        return String.format("| %-7s | %-15s | %-20s | %-10.2f |",
                id, fullName, email, balance);
    }
}
