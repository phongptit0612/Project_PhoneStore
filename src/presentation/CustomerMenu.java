package presentation;

import model.Order;
import model.OrderDetail;
import model.Product;
import model.User;
import model.Coupon;
import service.OrderService;
import service.ProductService;
import service.CouponService;
import util.Validator;

import java.util.ArrayList;
import java.util.List;

public class CustomerMenu {
    private static ProductService productService = new ProductService();
    private static OrderService orderService = new OrderService();
    private static CouponService couponService = new CouponService();

    // Giỏ hàng được lưu tạm trong bộ nhớ khi người dùng đang thao tác
    private static List<OrderDetail> cart = new ArrayList<>();

    public static void showMenu(User user) {
        // Mỗi khi đăng nhập vào, làm rỗng giỏ hàng cũ để an toàn
        cart.clear();

        while (true) {
            System.out.println("\n=== KHÁCH HÀNG: " + user.getName().toUpperCase() + " ===");
            System.out.println("1. Xem danh sách sản phẩm");
            System.out.println("2. Tìm kiếm sản phẩm");
            System.out.println("3. Săn Flash Sale (!)");
            System.out.println("4. Xem giỏ hàng & Đặt hàng");
            System.out.println("5. Lịch sử đơn mua");
            System.out.println("0. Đăng xuất");

            int choice = Validator.getInt("Chọn chức năng: ");
            switch (choice) {
                case 1:
                    browseProducts();
                    break;
                case 2:
                    searchProducts();
                    break;
                case 3:
                    browseFlashSaleProducts();
                    break;
                case 4:
                    viewCartAndCheckout(user);
                    break;
                case 5:
                    viewOrderHistory(user);
                    break;
                case 0:
                    System.out.println("Đã đăng xuất!");
                    return;
                default:
                    System.out.println(" Lỗi: Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void browseProducts() {
        List<Product> list = productService.getAllProducts();
        System.out.println("\n--- SAN PHAM HIEN CO ---");
        for (Product p : list) {
            if (p.getStock() > 0) {
                System.out.printf("%d - %s | Tồn kho: %d | Giá: %,.0f VNĐ\n", p.getId(), p.getName(), p.getStock(), p.getPrice());
            }
        }

        System.out.println("\n1. Thêm vào giỏ hàng");
        System.out.println("0. Quay lại");
        int subChoice = Validator.getInt("Chọn: ");
        if (subChoice == 1) {
            addToCart();
        }
    }

    private static void searchProducts() {
        String keyword = Validator.getString("Nhập tên điện thoại cần tìm: ");
        List<Product> list = productService.searchProductsByName(keyword);
        if (list.isEmpty()) {
            System.out.println("Không tìm thấy sản phẩm nào.");
            return;
        }
        System.out.println("\n--- KET QUA ---");
        for (Product p : list) {
            if (p.getStock() > 0) {
                System.out.printf("%d - %s | Tồn kho: %d | Giá: %,.0f VNĐ\n", p.getId(), p.getName(), p.getStock(), p.getPrice());
            }
        }

        System.out.println("\n1. Thêm vào giỏ hàng");
        System.out.println("0. Quay lại");
        int subChoice = Validator.getInt("Chọn: ");
        if (subChoice == 1) {
            addToCart();
        }
    }

    private static void addToCart() {
        int productId = Validator.getInt("Nhập ID sản phẩm muốn mua: ");
        Product p = productService.getProductById(productId);

        if (p == null) {
            System.out.println(" Lỗi: Không tồn tại sản phẩm này!");
            return;
        }
        if (p.getStock() <= 0) {
            System.out.println(" Lỗi: Sản phẩm đã hết hàng!");
            return;
        }

        int quantity = Validator.getInt("Nhập số lượng: ");
        if (quantity > p.getStock()) {
            System.out.println(" Lỗi: Không đủ hàng trong kho (Chỉ còn " + p.getStock() + ")");
            return;
        }

        // Kiểm tra xem trong giỏ đã có sp này chưa, nếu có thì cộng dồn số lượng
        boolean exists = false;
        for (OrderDetail item : cart) {
            if (item.getProductId() == productId) {
                // Phải check lại tổng số lượng
                if (item.getQuantity() + quantity > p.getStock()) {
                    System.out.println(" Lỗi: Vượt quá số lượng tồn kho!");
                    return;
                }
                item.setQuantity(item.getQuantity() + quantity);
                exists = true;
                break;
            }
        }

        if (!exists) {
            OrderDetail detail = new OrderDetail();
            detail.setProductId(productId);
            detail.setQuantity(quantity);
            detail.setPrice(p.isFlashSale() ? p.getFlashSalePrice() : p.getPrice());
            cart.add(detail);
        }

        System.out.println("Đã thêm " + p.getName() + " x" + quantity + " vào giỏ hàng!");
    }

    private static void browseFlashSaleProducts() {
        List<Product> list = productService.getAllProducts();
        System.out.println("\n--- SẢN PHẨM FLASH SALE ---");
        boolean hasSale = false;
        for (Product p : list) {
            if (p.isFlashSale() && p.getStock() > 0) {
                System.out.printf("%d - %s | Tồn kho: %d | Giá Gốc: %,.0f đ | GIÁ SỐC: %,.0f đ\n", 
                    p.getId(), p.getName(), p.getStock(), p.getPrice(), p.getFlashSalePrice());
                hasSale = true;
            }
        }
        if (!hasSale) {
            System.out.println("Hiện không có Flash Sale nào đang mở.");
            return;
        }

        System.out.println("\n1. Thêm vào giỏ hàng");
        System.out.println("0. Quay lại");
        int subChoice = Validator.getInt("Chọn: ");
        if (subChoice == 1) {
            addToCart();
        }
    }

    private static void viewCartAndCheckout(User user) {
        if (cart.isEmpty()) {
            System.out.println("Giỏ hàng của bạn đang trống!");
            return;
        }

        System.out.println("\n--- GIỎ HÀNG CỦA BẠN ---");
        double totalAmount = 0;
        for (OrderDetail detail : cart) {
            Product p = productService.getProductById(detail.getProductId());
            double subTotal = detail.getQuantity() * detail.getPrice();
            totalAmount += subTotal;
            System.out.printf("- %s | SL: %d | Giá: %,.0f VNĐ | Thành tiền: %,.0f VNĐ\n", p.getName(),
                    detail.getQuantity(), detail.getPrice(), subTotal);
        }
        System.out.printf(">> TỔNG TIỀN (Chưa giảm): %,.0f VNĐ\n", totalAmount);

        System.out.println("\nBạn có Mã giảm giá không? (Nhập mã hoặc bỏ trống 'Enter' để qua)");
        String couponCode = Validator.getStringOrEmpty("Nhập mã: ");
        Coupon appliedCoupon = null;
        if (!couponCode.isEmpty()) {
            appliedCoupon = couponService.applyCoupon(couponCode);
            if (appliedCoupon != null) {
                double discount = totalAmount * appliedCoupon.getDiscountPercent() / 100.0;
                System.out.printf(" Áp dụng mã %s thành công (-%.0f%%): -%,.0f VNĐ\n", 
                                couponCode, appliedCoupon.getDiscountPercent(), discount);
                totalAmount -= discount;
            }
        }

        System.out.printf(">> TỔNG TIỀN THANH TOÁN: %,.0f VNĐ\n", totalAmount);

        System.out.println("\n1. Xác nhận Đặt hàng (Checkout)");
        System.out.println("2. Xóa toàn bộ giỏ hàng");
        System.out.println("0. Quay lại");

        int choice = Validator.getInt("Chọn: ");
        if (choice == 1) {
            Order order = new Order();
            order.setUserId(user.getId());
            order.setStatus("PENDING");
            order.setTotalPrice(totalAmount);

            if (orderService.checkout(order, cart, appliedCoupon)) {
                System.out.println("Đặt hàng thành công! Vui lòng chờ Shop chuẩn bị hàng.");
                cart.clear(); // Xóa giỏ hàng sau khi đặt thành công
            } else {
                System.out.println(" Có lỗi xảy ra trong quá trình thanh toán, có thể do kho hàng vượt lượng yêu cầu.");
            }
        } else if (choice == 2) {
            cart.clear();
            System.out.println("Đã xóa toàn bộ giỏ hàng!");
        }
    }

    private static void viewOrderHistory(User user) {
        List<Order> list = orderService.getOrdersByUserId(user.getId());
        if (list.isEmpty()) {
            System.out.println("Bạn chưa có đơn hàng nào.");
            return;
        }
        System.out.println("\n--- LỊCH SỬ ĐƠN HÀNG ---");
        for (Order o : list) {
            System.out.printf("Mã ĐH: %d | Tổng: %,.0f VNĐ | Trạng thái: %s | Ngày: %s\n", o.getId(), o.getTotalPrice(),
                    o.getStatus(), o.getCreatedAt());
        }
    }
}
