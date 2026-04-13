package dao;

import model.Order;
import model.OrderDetail;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Thêm đơn hàng, trả về id của đơn hàng vừa tạo (hoặc -1 nếu lỗi)
    // conn được truyền từ Service để quản lý Transaction
    public int insertOrder(Connection conn, Order order) throws SQLException {
        String sql = "INSERT INTO orders (user_id, status, total_price) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, order.getUserId());
            pstmt.setString(2, order.getStatus());
            pstmt.setDouble(3, order.getTotalPrice());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        }
    }

    // Thêm chi tiết đơn hàng
    // conn được truyền từ Service để quản lý Transaction
    public void insertOrderDetail(Connection conn, OrderDetail detail) throws SQLException {
        String sql = "INSERT INTO order_details (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, detail.getOrderId());
            pstmt.setInt(2, detail.getProductId());
            pstmt.setInt(3, detail.getQuantity());
            pstmt.setDouble(4, detail.getPrice());
            pstmt.executeUpdate();
        }
    }

    public List<Order> findAll() {
        return getOrderList("SELECT * FROM orders ORDER BY created_at DESC");
    }

    public List<Order> findByUserId(int userId) {
        return getOrderList("SELECT * FROM orders WHERE user_id = " + userId + " ORDER BY created_at DESC");
    }

    private List<Order> getOrderList(String sql) {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
             
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setStatus(rs.getString("status"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setCreatedAt(rs.getTimestamp("created_at"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public boolean updateStatus(int orderId, String newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println(" Lỗi Cập nhật trạng thái: " + e.getMessage());
            return false;
        }
    }
}
