package presentation;

import model.Category;
import model.Order;
import model.Product;
import service.CategoryService;
import service.OrderService;
import service.ProductService;
import util.Validator;

import java.util.List;

public class AdminMenu {
    private static CategoryService categoryService = new CategoryService();
    private static ProductService productService = new ProductService();
    private static OrderService orderService = new OrderService();

    public static void showMenu() {
        while (true) {
            System.out.println("\n=== QUẢN TRỊ VIÊN (ADMIN) ===");
            System.out.println("1. Quản lý Danh mục");
            System.out.println("2. Quản lý Sản phẩm");
            System.out.println("3. Quản lý Đơn hàng");
            System.out.println("0. Đăng xuất");

            int choice = Validator.getInt("Chọn chức năng: ");
            switch (choice) {
                case 1:
                    categoryMenu();
                    break;
                case 2:
                    productMenu();
                    break;
                case 3:
                    orderMenu();
                    break;
                case 0:
                    System.out.println("Đã đăng xuất tài khoản Admin.");
                    return;
                default:
                    System.out.println("Lỗi: Lựa chọn không hợp lệ!");
            }
        }
    }

    // --- CATEGORY MANAGER ---
    private static void categoryMenu() {
        while (true) {
            System.out.println("\n--- Quản lý Danh mục ---");
            System.out.println("1. Hiển thị danh mục");
            System.out.println("2. Thêm danh mục");
            System.out.println("3. Cập nhật danh mục");
            System.out.println("4. Xóa danh mục");
            System.out.println("0. Quay lại");

            int choice = Validator.getInt("Chọn chức năng: ");
            switch (choice) {
                case 1:
                    List<Category> list = categoryService.getAllCategories();
                    System.out.println("--- Danh sách Danh mục ---");
                    list.forEach(c -> System.out.println("ID: " + c.getId() + " - Tên: " + c.getName()));
                    break;
                case 2:
                    String name = Validator.getString("Nhập tên danh mục mới: ");
                    if (categoryService.addCategory(name)) {
                        System.out.println("Thêm thành công!");
                    }
                    break;
                case 3:
                    int idEdit = Validator.getInt("Nhập ID danh mục cần cập nhật: ");
                    String newName = Validator.getString("Nhập tên mới: ");
                    if (categoryService.updateCategory(idEdit, newName)) {
                        System.out.println("Cập nhật thành công!");
                    }
                    break;
                case 4:
                    int idDel = Validator.getInt("Nhập ID danh mục cần xóa: ");
                    if (categoryService.deleteCategory(idDel)) {
                        System.out.println("Xóa thành công!");
                    }
                    break;
                case 0:
                    return;
            }
        }
    }

    // --- PRODUCT MANAGER ---
    private static void productMenu() {
        while (true) {
            System.out.println("\n--- Quản lý Sản phẩm ---");
            System.out.println("1. Hiển thị sản phẩm");
            System.out.println("2. Thêm sản phẩm");
            System.out.println("3. Sửa sản phẩm");
            System.out.println("4. Xóa sản phẩm");
            System.out.println("5. Tìm kiếm sản phẩm theo tên");
            System.out.println("6. Xem báo cáo Top 5 bán chạy");
            System.out.println("0. Quay lại");

            int choice = Validator.getInt("Chọn chức năng: ");
            switch (choice) {
                case 1:
                    showAllProducts();
                    break;
                case 2:
                    addProduct();
                    break;
                case 3:
                    updateProduct();
                    break;
                case 4:
                    int idDel = Validator.getInt("Nhập ID sản phẩm cần xóa: ");
                    String confirm = Validator.getString("Bạn có chắc chắn muốn xóa? (Y/N): ");
                    if (confirm.equalsIgnoreCase("Y")) {
                        if (productService.deleteProduct(idDel)) {
                            System.out.println("Xóa thành công!");
                        }
                    } else {
                        System.out.println("Đã hủy thao tác xóa.");
                    }
                    break;
                case 5:
                    String keyword = Validator.getString("Nhập từ khóa tìm kiếm: ");
                    List<Product> list = productService.searchProductsByName(keyword);
                    System.out.println("--- Kết quả tìm kiếm ---");
                    list.forEach(p -> System.out.println(
                            p.getId() + " - " + p.getName() + " - Giá: " + p.getPrice() + " - Tồn: " + p.getStock()));
                    break;
                case 6:
                    showTop5Products();
                    break;
                case 0:
                    return;
            }
        }
    }

    private static void showAllProducts() {
        List<Product> list = productService.getAllProducts();
        if (list.isEmpty()) {
            System.out.println("Danh sách trống!");
            return;
        }

        System.out.println("1. Sắp xếp giá tăng dần | 2. Giá giảm dần | 3. Không sắp xếp");
        int sortChoice = Validator.getInt("Chọn (1-3): ");
        if (sortChoice == 1)
            list.sort((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));
        else if (sortChoice == 2)
            list.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));

        System.out.printf("%-5s %-20s %-15s %-10s %-10s %-15s %-10s\n", "ID", "Tên", "Hãng", "Màu", "DL",
                "Giá (VNĐ)", "Tồn kho");
        for (Product p : list) {
            System.out.printf("%-5d %-20s %-15s %-10s %-10s %,-15.0f %-10d\n",
                    p.getId(), p.getName(), p.getBrand(), p.getColor(), p.getCapacity(), p.getPrice(),
                    p.getStock());
        }
    }

    private static void showTop5Products() {
        List<Product> top = productService.getTop5SellingProducts();
        System.out.println("\n--- TOP 5 SẢN PHẨM BÁN CHẠY ---");
        System.out.printf("%-5s %-20s %-15s\n", "ID", "Tên sản phẩm", "Đã Bán");
        for (Product p : top) {
            System.out.printf("%-5d %-20s %-15d\n", p.getId(), p.getName(), p.getTotalSold());
        }
    }

    private static void addProduct() {
        Product p = new Product();
        p.setCategoryId(Validator.getInt("Nhập ID Danh mục: "));
        p.setName(Validator.getString("Nhập tên điện thoại: "));
        p.setBrand(Validator.getString("Nhập Hãng (VD: Apple): "));
        p.setCapacity(Validator.getString("Nhập dung lượng (VD: 128GB): "));
        p.setColor(Validator.getString("Nhập Màu sắc: "));
        p.setPrice(Validator.getDouble("Nhập Giá bán: "));
        p.setStock(Validator.getInt("Nhập số lượng nhập kho: "));
        p.setDescription(Validator.getString("Nhập Mô tả: "));

        int isFlash = Validator.getInt("Tham gia Flash Sale? (1: Có, 0: Không): ");
        if (isFlash == 1) {
            p.setFlashSale(true);
            p.setFlashSalePrice(Validator.getDouble("Nhập Giá Flash Sale: "));
        } else {
            p.setFlashSale(false);
            p.setFlashSalePrice(0);
        }

        if (productService.addProduct(p)) {
            System.out.println("Thêm sản phẩm thành công!");
        }
    }

    private static void updateProduct() {
        int id = Validator.getInt("Nhập ID sản phẩm cần sửa: ");
        Product p = productService.getProductById(id);
        if (p == null) {
            System.out.println(" Không tìm thấy sản phẩm!");
            return;
        }
        System.out.println("- GHI CHÚ: Bỏ trống (Enter) nếu không muốn thay đổi");

        Integer catId = Validator.getIntOrEmpty(String.format("ID Danh mục [%d]: ", p.getCategoryId()));
        if (catId != null)
            p.setCategoryId(catId);

        String name = Validator.getStringOrEmpty(String.format("Tên điện thoại [%s]: ", p.getName()));
        if (!name.isEmpty())
            p.setName(name);

        String brand = Validator.getStringOrEmpty(String.format("Hãng [%s]: ", p.getBrand()));
        if (!brand.isEmpty())
            p.setBrand(brand);

        String cap = Validator.getStringOrEmpty(String.format("Dung lượng [%s]: ", p.getCapacity()));
        if (!cap.isEmpty())
            p.setCapacity(cap);

        String color = Validator.getStringOrEmpty(String.format("Màu sắc [%s]: ", p.getColor()));
        if (!color.isEmpty())
            p.setColor(color);

        Double price = Validator.getDoubleOrEmpty(String.format("Giá bán [%.0f]: ", p.getPrice()));
        if (price != null)
            p.setPrice(price);

        Integer stock = Validator.getIntOrEmpty(String.format("Tồn kho [%d]: ", p.getStock()));
        if (stock != null)
            p.setStock(stock);

        String desc = Validator.getStringOrEmpty(String.format("Mô tả [%s]: ", p.getDescription()));
        if (!desc.isEmpty())
            p.setDescription(desc);

        Integer flashSaleChoice = Validator
                .getIntOrEmpty(String.format("Tham gia Flash sale (1: Có, 0: Không) [%d]: ", p.isFlashSale() ? 1 : 0));
        if (flashSaleChoice != null) {
            if (flashSaleChoice == 1) {
                p.setFlashSale(true);
                Double flashPrice = Validator
                        .getDoubleOrEmpty(String.format("Giá Flash Sale [%.0f]: ", p.getFlashSalePrice()));
                if (flashPrice != null)
                    p.setFlashSalePrice(flashPrice);
            } else {
                p.setFlashSale(false);
                p.setFlashSalePrice(0);
            }
        } else if (p.isFlashSale()) {
            // Nếu khách bấm Enter bó qua (giữ nguyên cờ Flash Sale cũ là Có)
            Double flashPrice = Validator
                    .getDoubleOrEmpty(String.format("Giá Flash Sale [%.0f]: ", p.getFlashSalePrice()));
            if (flashPrice != null)
                p.setFlashSalePrice(flashPrice);
        }

        if (productService.updateProduct(p)) {
            System.out.println("Cập nhật sản phẩm thành công!");
        }
    }

    // --- ORDER MANAGER ---
    private static void orderMenu() {
        while (true) {
            System.out.println("\n--- Quản lý Đơn hàng ---");
            System.out.println("1. Hiển thị tất cả đơn hàng");
            System.out.println("2. Cập nhật trạng thái đơn hàng (SHIPPING, DELIVERED, CANCELLED)");
            System.out.println("0. Quay lại");

            int choice = Validator.getInt("Chọn chức năng: ");
            switch (choice) {
                case 1:
                    List<Order> list = orderService.getAllOrders();
                    System.out.println("--- Danh sách Đơn hàng ---");
                    for (Order o : list) {
                        System.out.println("Mã ĐH: " + o.getId() + " | User ID: " + o.getUserId() +
                                " | Tổng tiền: " + o.getTotalPrice() + " | Trạng thái: " + o.getStatus() +
                                " | Ngày đặt: " + o.getCreatedAt());
                    }
                    break;
                case 2:
                    int orderId = Validator.getInt("Nhập Mã đơn hàng cần cập nhật trạng thái: ");
                    System.out.println(
                            "Chọn trạng thái: 1. SHIPPING (Đang giao) | 2. DELIVERED (Đã giao) | 3. CANCELLED (Hủy đơn)");
                    int statusChoice = Validator.getInt("Chọn (1-3): ");
                    String status = "PENDING";
                    if (statusChoice == 1)
                        status = "SHIPPING";
                    else if (statusChoice == 2)
                        status = "DELIVERED";
                    else if (statusChoice == 3)
                        status = "CANCELLED";
                    else {
                        System.out.println(" Không hợp lệ!");
                        continue;
                    }
                    if (orderService.updateOrderStatus(orderId, status)) {
                        System.out.println("Cập nhật trạng thái đơn hàng thành công!");
                    }
                    break;
                case 0:
                    return;
            }
        }
    }
}
