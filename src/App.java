import model.User;
import presentation.AdminMenu;
import presentation.CustomerMenu;
import service.UserService;
import util.Validator;

public class App {
    private static UserService userService = new UserService();

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("SmartPhone Store");
        System.out.println("=========================================");

        while (true) {
            System.out.println("\n--- MENU HỆ THỐNG ---");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký");
            System.out.println("0. Thoát hệ thống");
            
            int choice = Validator.getInt("Chọn chức năng: ");
            
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 0:
                    System.out.println("Tạm biệt! Hẹn gặp lại.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Lỗi: Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void login() {
        System.out.println("\n--- ĐĂNG NHẬP ---");
        String username = Validator.getString("Tên đăng nhập: ");
        String password = Validator.getString("Mật khẩu: ");

        User currentUser = userService.login(username, password);
        if (currentUser != null) {
            System.out.println("Đăng nhập thành công! Xin chào, " + currentUser.getName());
            if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
                AdminMenu.showMenu();
            } else {
                CustomerMenu.showMenu(currentUser);
            }
        } else {
            System.out.println("Đăng nhập thất bại. Vui lòng kiểm tra lại tài khoản hoặc mật khẩu.");
        }
    }

    private static void register() {
        System.out.println("\n--- ĐĂNG KÝ TÀI KHOẢN ---");
        User newUser = new User();
        newUser.setUsername(Validator.getString("Nhập tên đăng nhập: "));
        newUser.setPassword(Validator.getString("Nhập mật khẩu: "));
        newUser.setName(Validator.getString("Nhập họ tên: "));
        newUser.setEmail(Validator.getEmail("Nhập Email: "));
        newUser.setPhone(Validator.getPhone("Nhập Số điện thoại: "));
        newUser.setAddress(Validator.getString("Nhập Địa chỉ: "));
        newUser.setRole("CUSTOMER"); // Mặc định là khách hàng

        if (userService.register(newUser)) {
            System.out.println("Đăng ký tài khoản thành công! Bây giờ bạn có thể đăng nhập.");
        } else {
            System.out.println("Đăng ký thất bại.");
        }
    }
}
