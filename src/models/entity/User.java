package models.entity;

import java.time.LocalDateTime;

public class User {
    private int id;
    private String fullName;
    private String email;
    private String password;
    private String role;
    private double balance;
    private LocalDateTime createdAt;

    public User(int id, String fullName, String email, String password, String role, double balance, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return "User{" + "id=" + id + ", fullName='" + fullName + '\'' + ", email='" + email + '\'' + ", password='" + password + '\'' + ", role='" + role + '\'' + ", balance=" + balance + ", createdAt=" + createdAt + '}';
    }
}
