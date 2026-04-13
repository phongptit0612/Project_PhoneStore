package util;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Validator {
    private static final Scanner scanner = new Scanner(System.in);

    // check tong (nhan vao tham so voi do dai tu do va khong rong)
    public static String getString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String result = scanner.nextLine().trim();
            if (!result.isEmpty()) {
                return result;
            }
            System.out.println("Lỗi: Không được để trống. Vui lòng nhập lại!");
        }
    }

    public static String getStringOrEmpty(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // check nhap so nguyen
    public static int getInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int result = Integer.parseInt(scanner.nextLine().trim());
                return result;
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Phải nhập số nguyên hợp lệ. Vui lòng nhập lại!");
            }
        }
    }

    public static Integer getIntOrEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return null;
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Phải nhập số nguyên hợp lệ. Vui lòng nhập lại!");
            }
        }
    }

    // check nhap so thuc
    public static double getDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double result = Double.parseDouble(scanner.nextLine().trim());
                return result;
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Phải nhập số thực hợp lệ. Vui lòng nhập lại!");
            }
        }
    }

    public static Double getDoubleOrEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return null;
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Phải nhập số thực hợp lệ. Vui lòng nhập lại!");
            }
        }
    }

    // check so dien thoai 10 kitu va bat dau bang 0
    public static String getPhone(String prompt) {
        String phoneRegex = "^0\\d{9}$";
        while (true) {
            String phone = getString(prompt);
            if (Pattern.matches(phoneRegex, phone)) {
                return phone;
            }
            System.out.println("Lỗi: Số điện thoại phải gồm 10 chữ số và bắt đầu bằng số 0.");
        }
    }

    // check email hop le
    public static String getEmail(String prompt) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        while (true) {
            String email = getString(prompt);
            if (Pattern.matches(emailRegex, email)) {
                return email;
            }
            System.out.println("Lỗi: Định dạng Email không hợp lệ (Ví dụ: abc@gmail.com).");
        }
    }
}
