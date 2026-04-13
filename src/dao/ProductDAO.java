package dao;

import model.Product;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> findAll() {
        return getList("SELECT * FROM products");
    }

    public List<Product> findByName(String keyword) {
        return getList("SELECT * FROM products WHERE name LIKE '%" + keyword + "%'");
    }

    private List<Product> getList(String sql) {
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
             
            while (rs.next()) {
                products.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public boolean save(Product product) {
        String sql;
        boolean isUpdate = product.getId() > 0;
        
        if (isUpdate) {
            sql = "UPDATE products SET category_id=?, name=?, brand=?, capacity=?, color=?, price=?, stock=?, description=?, is_flash_sale=?, flash_sale_price=? WHERE id=?";
        } else {
            sql = "INSERT INTO products (category_id, name, brand, capacity, color, price, stock, description, is_flash_sale, flash_sale_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, product.getCategoryId());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getBrand());
            pstmt.setString(4, product.getCapacity());
            pstmt.setString(5, product.getColor());
            pstmt.setDouble(6, product.getPrice());
            pstmt.setInt(7, product.getStock());
            pstmt.setString(8, product.getDescription());
            pstmt.setBoolean(9, product.isFlashSale());
            pstmt.setDouble(10, product.getFlashSalePrice());
            
            if (isUpdate) {
                pstmt.setInt(11, product.getId());
            }
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Lỗi Database: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Lỗi Database (Không thể xóa sản phẩm đã có đơn hàng): " + e.getMessage());
            return false;
        }
    }

    public Product findById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateStock(int productId, int stockChange) {
        String sql = "UPDATE products SET stock = stock + ? WHERE id = ? AND (stock + ?) >= 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, stockChange);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, stockChange);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Product> getTop5SellingProducts() {
        List<Product> topList = new ArrayList<>();
        String sql = "SELECT p.*, SUM(od.quantity) as total_sold " +
                     "FROM products p " +
                     "JOIN order_details od ON p.id = od.product_id " +
                     "JOIN orders o ON od.order_id = o.id " +
                     "WHERE o.status = 'DELIVERED' " +
                     "GROUP BY p.id " +
                     "ORDER BY total_sold DESC " +
                     "LIMIT 5";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
             
            while (rs.next()) {
                Product p = extractFromResultSet(rs);
                // Vì extractFromResultSet không móc cột total_sold, ta phải tự set thêm:
                p.setTotalSold(rs.getInt("total_sold"));
                topList.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topList;
    }

    private Product extractFromResultSet(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand"));
        p.setCapacity(rs.getString("capacity"));
        p.setColor(rs.getString("color"));
        p.setPrice(rs.getDouble("price"));
        p.setStock(rs.getInt("stock"));
        p.setDescription(rs.getString("description"));
        p.setFlashSale(rs.getBoolean("is_flash_sale"));
        p.setFlashSalePrice(rs.getDouble("flash_sale_price"));
        return p;
    }
}
