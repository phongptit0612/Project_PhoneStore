package model;

import java.sql.Timestamp;

public class Order {
    private int id;
    private int userId;
    private String status; // PENDING, SHIPPING, DELIVERED, CANCELLED
    private double totalPrice;
    private Timestamp createdAt;

    public Order() {
    }

    public Order(int id, int userId, String status, double totalPrice, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
