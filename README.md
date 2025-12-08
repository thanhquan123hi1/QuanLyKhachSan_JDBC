#  Panda Bamboo Hotel Management System

![Java](https://img.shields.io/badge/Java-21-orange)
![Database](https://img.shields.io/badge/MySQL-8.0-blue)
![IDE](https://img.shields.io/badge/Eclipse-Maven-grey)

> **"STAY GREEN, STAY SERENE"**

Hệ thống phần mềm quản lý khách sạn trọn gói (Desktop Application), được thiết kế để tối ưu hóa quy trình vận hành khách sạn từ khâu đặt phòng, quản lý dịch vụ cho đến thanh toán và báo cáo doanh thu. Dự án sử dụng kiến trúc MVC và kết nối cơ sở dữ liệu qua JDBC.

##  Tính năng chính

Dựa trên mã nguồn hiện tại, hệ thống bao gồm các chức năng:

* ** Đăng nhập & Phân quyền:**
    * Giao diện đăng nhập với hiệu ứng hoạt hình (mây trôi) đẹp mắt.
    * Đăng ký tài khoản mới và xác thực người dùng.
* ** Quản lý Phòng (Room Management):**
    * Xem sơ đồ phòng trực quan (Trống/Đã đặt) theo màu sắc.
    * Tìm kiếm phòng theo tầng, mã phòng, trạng thái.
    * Check-in nhanh cho khách hàng.
* ** Dịch vụ (Services):**
    * Đặt dịch vụ cho phòng: Nhà hàng (Sáng/Trưa/Tối), Bar, Hồ bơi, Giặt sấy, Xông hơi...
    * Tự động tính tiền dịch vụ vào hóa đơn.
* ** Quản lý Khách hàng:**
    * Lưu trữ thông tin khách hàng, CCCD/Visa, Quốc tịch.
    * Tra cứu lịch sử đặt phòng.
* ** Quản lý Nhân viên:**
    * CRUD (Thêm, Xóa, Sửa, Tìm kiếm) thông tin nhân viên.
* ** Hóa đơn & Thanh toán:**
    * Tự động tính tổng tiền phòng và dịch vụ.
    * Hỗ trợ thanh toán tiền mặt hoặc thẻ tín dụng.
    * Tính thuế (VAT) và xuất hóa đơn.

##  Công nghệ sử dụng

* **Ngôn ngữ:** Java (JDK 21)
* **Giao diện:** Java Swing (Giao diện tùy chỉnh với Gradient, Rounded Borders).
* **Cơ sở dữ liệu:** MySQL.
* **Kết nối:** JDBC (Java Database Connectivity).
* **Quản lý dự án:** Maven.
* **Thư viện hỗ trợ:** `jcalendar` (JDateChooser cho việc chọn ngày tháng).

##  Cài đặt và Chạy ứng dụng

### Yêu cầu
* Java Development Kit (JDK) 21 trở lên.
* MySQL Server.
* Eclipse IDE (hoặc IntelliJ IDEA).

### Các bước thực hiện

1.  **Clone repository:**
    ```bash
    git clone [https://github.com/thanhquan123hi1/PandaBamboo-HMS-JDBC.git](https://github.com/thanhquan123hi1/PandaBamboo-HMS-JDBC.git)
    ```

2.  **Cấu hình Cơ sở dữ liệu:**
    * Mở MySQL Workbench hoặc công cụ quản lý DB.
    * Chạy file script `MySQL/QuanLyKhachSan.sql` để tạo bảng.
    * Chạy file script `MySQL/Data.sql` để thêm dữ liệu mẫu (Users, Phòng, Dịch vụ).

3.  **Cấu hình kết nối JDBC:**
    * Mở file `src/Util/DatabaseConnection.java`.
    * Cập nhật `USER` và `PASSWORD` tương ứng với MySQL của bạn:
    ```java
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/quanlykhachsan";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password"; // Thay đổi mật khẩu của bạn
    ```

4.  **Chạy ứng dụng:**
    * Mở dự án trong Eclipse dưới dạng Maven Project.
    * Chạy file `src/QuanLyKhachSan/QuanLyKhachSan.java`.

---
*Dự án được thực hiện cho môn học Lập trình Window.*
