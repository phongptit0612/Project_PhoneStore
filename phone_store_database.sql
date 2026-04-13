CREATE DATABASE IF NOT EXISTS phonestore_db;
USE phonestore_db;

-- Bảng lưu trữ người dùng
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL, -- Mật khẩu đã được băm (BCrypt)
    role ENUM('ADMIN', 'CUSTOMER') DEFAULT 'CUSTOMER',
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15) UNIQUE,
    address VARCHAR(255)
);

-- Bảng lưu trữ danh mục sản phẩm 
CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    is_deleted BOOLEAN DEFAULT 0
);

-- Bảng lưu trữ sản phẩm (Điện thoại)
CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    name VARCHAR(150) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    capacity VARCHAR(50), -- 128GB, 256GB
    color VARCHAR(50),
    price DECIMAL(15, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    is_flash_sale BOOLEAN DEFAULT 0,
    flash_sale_price DECIMAL(15,2) DEFAULT 0,
    description TEXT,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);

-- Bảng lưu trữ đơn hàng
CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    status ENUM('PENDING', 'SHIPPING', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    total_price DECIMAL(15, 2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Bảng chi tiết đơn hàng
CREATE TABLE IF NOT EXISTS order_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(15, 2) NOT NULL, -- Giá tại thời điểm mua
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
);

-- Bảng lưu trữ mã giảm giá (Nâng cao)
CREATE TABLE IF NOT EXISTS coupons (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    discount_percent INT NOT NULL, -- Từ 1 đến 100
    max_usage INT NOT NULL DEFAULT 100,
    current_usage INT NOT NULL DEFAULT 0,
    expiry_date DATE NOT NULL
);

-- Thêm tài khoản admin mặc định: admin/admin123 
INSERT INTO users (username, password, role, name, email, phone, address) 
VALUES ('admin', '$2a$12$kGL/ADKo25avuoo7ayr53uZzijtiVxqrmL6oQIh3XeZ5W5jtXLXhK', 'ADMIN', 'Quản trị viên', 'admin@phonestore.com', '0123456789', 'TPHCM')
ON DUPLICATE KEY UPDATE username='admin';

INSERT INTO categories (name) VALUES 
('Apple'),
('Samsung'),
('Xiaomi'),
('Oppo');

INSERT INTO products (category_id, name, brand, capacity, color, price, stock, description) VALUES 
(1, 'iPhone 15 Pro Max', 'Apple', '256GB', 'Titan Tự Nhiên', 34990000, 50, 'Chip A17 Pro mạnh mẽ, camera siêu thu phóng 5x, khung titan siêu nhẹ.'),
(1, 'iPhone 14 Plus', 'Apple', '128GB', 'Xanh Dương', 21500000, 20, 'Pin trâu, màn hình lớn 6.7 inch hiển thị sắc nét.'),
(2, 'Samsung Galaxy S24 Ultra', 'Samsung', '512GB', 'Xám Titan', 37990000, 30, 'Tích hợp Galaxy AI thông minh nâng tầm cuộc sống, camera 200MP.'),
(2, 'Samsung Galaxy Z Flip 5', 'Samsung', '256GB', 'Tím', 22990000, 15, 'Thiết kế gập vỏ sò thời thượng, màn hình phụ kích thước lớn.'),
(3, 'Xiaomi 14 Pro', 'Xiaomi', '256GB', 'Đen', 19990000, 40, 'Hệ thống camera Leica đẳng cấp, sạc siêu tốc 120W, chip Snapdragon 8 Gen 3.'),
(3, 'Redmi Note 13', 'Xiaomi', '128GB', 'Xanh Lá', 4990000, 100, 'Lựa chọn quốc dân giá rẻ, màn hình 120Hz mượt mà.'),
(4, 'Oppo Reno11 5G', 'Oppo', '256GB', 'Trắng', 10990000, 25, 'Chuyên gia nhiếp ảnh chân dung, sạc nhanh SuperVOOC.');

-- MK nam123
INSERT INTO users (username, password, role, name, email, phone, address) VALUES 
('nam', '$2a$12$/Hepc1Y.YOSau5icwhMwnemrD7cEMivjrFwznn2Vxoj61WKIBHtRS', 'CUSTOMER', 'Nam', 'nam@gmail.com', '0947325662', 'Số 9, Blank, Hà Nội'),
-- MK huy123
('huy', '$2a$12$JZjA.S4uaDtVESX6b3NyCeR4QAFw7sca9lPuMNzhFUi9FDyB/oxj6', 'CUSTOMER', 'Huy', 'huyb@gmail.com', '0234567891', 'Quận 1, TP Hồ Chí Minh');

INSERT INTO coupons (code, discount_percent, max_usage, current_usage, expiry_date) VALUES 
('GIAITAN', 10, 50, 0, '2026-12-31'),
('FLASHSALE', 50, 10, 0, '2026-05-01');

select * from users;
drop database phonestore_db;