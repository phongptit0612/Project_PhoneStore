package service;

import dao.OrderDAO;
import dao.CouponDAO;
import model.Order;
import model.OrderDetail;
import model.Coupon;
import util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private OrderDAO orderDAO;
    private CouponDAO couponDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.couponDAO = new CouponDAO();
    }

    // checkout gio hang
    public boolean checkout(Order order, List<OrderDetail> details, Coupon coupon) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // tao don va lay ma don hang
            int orderId = orderDAO.insertOrder(conn, order);

            // them tung chi tiet cua don hang
            for (OrderDetail detail : details) {
                detail.setOrderId(orderId);
                orderDAO.insertOrderDetail(conn, detail);

                // giam so luong kho
                // goi phuong thuc rieng cua connection trong productDAO
                // cap nhat chung 1 giao dich
                String updateStockSql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement(updateStockSql)) {
                    pstmt.setInt(1, detail.getQuantity());
                    pstmt.setInt(2, detail.getProductId());
                    pstmt.setInt(3, detail.getQuantity());
                    int updated = pstmt.executeUpdate();
                    if (updated == 0) {
                        throw new SQLException("Sản phẩm ID " + detail.getProductId() + " không đủ hàng tồn kho");
                    }
                }
            }

            // Neu co Coupon thi tang so luot su dung tren DB
            if (coupon != null) {
                boolean inc = couponDAO.incrementUsage(conn, coupon.getId());
                if (!inc) {
                    throw new SQLException("Không thể cập nhật lượt sử dụng mã giảm giá");
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Đã hủy giao dịch an toàn");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    public List<Order> getOrdersByUserId(int userId) {
        return orderDAO.findByUserId(userId);
    }

    public boolean updateOrderStatus(int orderId, String status) {
        return orderDAO.updateStatus(orderId, status);
    }
}
