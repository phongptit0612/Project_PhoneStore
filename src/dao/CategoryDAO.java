package dao;

import model.Category;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE is_deleted = 0";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Category cat = new Category();
                cat.setId(rs.getInt("id"));
                cat.setName(rs.getString("name"));
                categories.add(cat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public boolean save(Category category) {
        String sql;
        boolean isUpdate = category.getId() > 0;

        if (isUpdate) {
            sql = "UPDATE categories SET name = ? WHERE id = ?";
        } else {
            sql = "INSERT INTO categories (name) VALUES (?)";
        }

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category.getName());
            if (isUpdate) {
                pstmt.setInt(2, category.getId());
            }

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // 1062 la ma bat trung lap trong sqlwworkbench
                System.out.println("\nLỗi: Tên danh mục này đã tồn tại trong hệ thống, hãy nhập tên khác");
            } else {
                System.out.println("\nLỗi Database: " + e.getMessage());
            }
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "UPDATE categories SET is_deleted = 1 WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Lỗi Database (Có thể danh mục này đã có sản phẩm): " + e.getMessage());
            return false;
        }
    }

    public Category findById(int id) {
        String sql = "SELECT * FROM categories WHERE id = ? AND is_deleted = 0";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Category cat = new Category();
                cat.setId(rs.getInt("id"));
                cat.setName(rs.getString("name"));
                return cat;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
