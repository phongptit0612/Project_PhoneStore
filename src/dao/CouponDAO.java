package dao;

import model.Coupon;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CouponDAO {

    public Coupon findByCode(String code) {
        String sql = "SELECT * FROM coupons WHERE code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean incrementUsage(Connection conn, int couponId) throws SQLException {
        String sql = "UPDATE coupons SET current_usage = current_usage + 1 WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, couponId);
            return pstmt.executeUpdate() > 0;
        }
    }

    private Coupon extractFromResultSet(ResultSet rs) throws SQLException {
        Coupon c = new Coupon();
        c.setId(rs.getInt("id"));
        c.setCode(rs.getString("code"));
        c.setDiscountPercent(rs.getInt("discount_percent"));
        c.setMaxUsage(rs.getInt("max_usage"));
        c.setCurrentUsage(rs.getInt("current_usage"));
        c.setExpiryDate(rs.getDate("expiry_date"));
        return c;
    }
}
