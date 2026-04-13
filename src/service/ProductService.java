package service;

import dao.ProductDAO;
import model.Product;

import java.util.List;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public List<Product> searchProductsByName(String keyword) {
        return productDAO.findByName(keyword);
    }

    public boolean addProduct(Product p) {
        if (p.getPrice() < 0 || p.getStock() < 0) {
            System.out.println("Lỗi: Giá hoặc số lượng không hợp lệ!");
            return false;
        }
        return productDAO.save(p);
    }

    public boolean updateProduct(Product p) {
        Product existing = productDAO.findById(p.getId());
        if (existing == null) {
            System.out.println("Lỗi: Sản phẩm không tồn tại!");
            return false;
        }
        return productDAO.save(p);
    }

    public boolean deleteProduct(int id) {
        Product existing = productDAO.findById(id);
        if (existing == null) {
            System.out.println("Lỗi: Sản phẩm không tồn tại!");
            return false;
        }
        return productDAO.delete(id);
    }

    public Product getProductById(int id) {
        return productDAO.findById(id);
    }

    public List<Product> getTop5SellingProducts() {
        return productDAO.getTop5SellingProducts();
    }
}
