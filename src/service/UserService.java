package service;

import dao.UserDAO;
import model.User;
import util.PasswordUtil;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // kiem tra dang nhap
    public User login(String username, String rawPassword) {
        User user = userDAO.findByUsername(username);
        if (user != null) {
            boolean isMatch = PasswordUtil.checkPassword(rawPassword, user.getPassword());
            if (isMatch) {
                return user;
            }
        }
        return null;
    }

    // dang ki nguoi dung moi
    public boolean register(User user) {
        User existingUser = userDAO.findByUsername(user.getUsername());
        if (existingUser != null) {
            System.out.println("Lỗi: Tên đăng nhập đã tồn tại trong hệ thống!");
            return false;
        }

        String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        if (user.getRole() == null) {
            user.setRole("CUSTOMER");
        }

        return userDAO.save(user);
    }
}
