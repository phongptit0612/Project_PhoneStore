package service;

import dao.CategoryDAO;
import model.Category;

import java.util.List;

public class CategoryService {
    private CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    public boolean addCategory(String name) {
        Category newCat = new Category();
        newCat.setName(name);
        return categoryDAO.save(newCat);
    }

    public boolean updateCategory(int id, String newName) {
        Category cat = categoryDAO.findById(id);
        if (cat == null) {
            System.out.println("Lỗi: Không tìm thấy danh mục mang ID " + id);
            return false;
        }
        cat.setName(newName);
        return categoryDAO.save(cat);
    }

    public boolean deleteCategory(int id) {
        Category cat = categoryDAO.findById(id);
        if (cat == null) {
            System.out.println("Lỗi: Không tìm thấy danh mục mang ID " + id);
            return false;
        }
        return categoryDAO.delete(id);
    }
    
    public Category getCategoryById(int id) {
        return categoryDAO.findById(id);
    }
}
