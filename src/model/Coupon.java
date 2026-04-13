package model;

import java.sql.Date;

public class Coupon {
    private int id;
    private String code;
    private int discountPercent;
    private int maxUsage;
    private int currentUsage;
    private Date expiryDate;

    public Coupon() {
    }

    public Coupon(int id, String code, int discountPercent, int maxUsage, int currentUsage, Date expiryDate) {
        this.id = id;
        this.code = code;
        this.discountPercent = discountPercent;
        this.maxUsage = maxUsage;
        this.currentUsage = currentUsage;
        this.expiryDate = expiryDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(int discountPercent) { this.discountPercent = discountPercent; }

    public int getMaxUsage() { return maxUsage; }
    public void setMaxUsage(int maxUsage) { this.maxUsage = maxUsage; }

    public int getCurrentUsage() { return currentUsage; }
    public void setCurrentUsage(int currentUsage) { this.currentUsage = currentUsage; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
}
