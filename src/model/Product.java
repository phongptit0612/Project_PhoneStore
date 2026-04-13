package model;

public class Product {
    private int id;
    private int categoryId;
    private String name;
    private String brand;
    private String capacity;
    private String color;
    private double price;
    private int stock;
    private String description;
    private boolean isFlashSale;
    private double flashSalePrice;
    private int totalSold; // Dùng cho báo cáo thống kê

    public Product() {
    }

    public Product(int id, int categoryId, String name, String brand, String capacity, String color, double price, int stock, String description) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.brand = brand;
        this.capacity = capacity;
        this.color = color;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public boolean isFlashSale() { return isFlashSale; }
    public void setFlashSale(boolean flashSale) { this.isFlashSale = flashSale; }

    public double getFlashSalePrice() { return flashSalePrice; }
    public void setFlashSalePrice(double flashSalePrice) { this.flashSalePrice = flashSalePrice; }

    public int getTotalSold() { return totalSold; }
    public void setTotalSold(int totalSold) { this.totalSold = totalSold; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCapacity() { return capacity; }
    public void setCapacity(String capacity) { this.capacity = capacity; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
