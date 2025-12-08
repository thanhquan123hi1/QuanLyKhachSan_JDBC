# Hotel Management System (Hệ thống Quản lý Khách sạn)

## Giới thiệu (Description)
**Hotel Management System** là một ứng dụng desktop được xây dựng bằng ngôn ngữ **Java** (Swing) và sử dụng hệ quản trị cơ sở dữ liệu **MySQL**. Ứng dụng giúp đơn giản hóa quy trình quản lý khách sạn, bao gồm các hoạt động như đặt phòng, check-in, check-out, quản lý dịch vụ, và quản lý nhân viên.

Dự án này sử dụng mô hình JDBC để kết nối và thao tác với cơ sở dữ liệu.

## Tính năng chính (Features)
Ứng dụng cung cấp các chức năng quản lý toàn diện:

*   **Đăng nhập & Đăng ký (Login/Registration):** Hệ thống bảo mật cho nhân viên và quản lý.
*   **Quản lý Phòng (Room Management):** Xem trạng thái phòng, thêm/sửa/xóa thông tin phòng.
*   **Check-In / Đặt phòng:** Quy trình nhận phòng nhanh chóng cho khách hàng.
*   **Quản lý Khách hàng (Customer Management):** Lưu trữ và quản lý thông tin khách hàng.
*   **Dịch vụ (Services):** Quản lý các dịch vụ đi kèm (ăn uống, giặt ủi, v.v.).
*   **Hóa đơn (Invoice):** Tính toán và xuất hóa đơn thanh toán cho khách.
*   **Quản lý Nhân viên (Employee Management):** Quản lý thông tin và tài khoản nhân viên.
*   **Dashboard:** Giao diện tổng quan về tình hình hoạt động của khách sạn.

## Yêu cầu hệ thống (Requirements)
*   **Java Development Kit (JDK):** Phiên bản 21 trở lên.
*   **MySQL Server:** Phiên bản 8.0 trở lên.
*   **Maven:** Để quản lý các thư viện phụ thuộc.

## Cài đặt và Hướng dẫn sử dụng (Installation)

### 1. Thiết lập Cơ sở dữ liệu (Database Setup)
Trước khi chạy ứng dụng, bạn cần khởi tạo cơ sở dữ liệu MySQL.

1.  Mở công cụ quản lý MySQL (như MySQL Workbench, HeidiSQL, hoặc Command Line).
2.  Tạo database và bảng bằng cách chạy script:
    `MySQL/QuanLyKhachSan.sql`
3.  (Tùy chọn) Thêm dữ liệu mẫu bằng cách chạy script:
    `MySQL/Data.sql`

> **Lưu ý:** Kiểm tra cấu hình kết nối Database trong source code (thường ở file class kết nối database trong `src/DAO` hoặc `src/Util`) để đảm bảo `url`, `username`, và `password` khớp với cấu hình MySQL trên máy của bạn.

### 2. Cài đặt và Chạy ứng dụng (Build & Run)
Dự án được cấu hình bằng Maven.

1.  Clone repository hoặc tải về source code.
2.  Mở terminal tại thư mục gốc của dự án (`HotelManagement`).
3.  Cài đặt các thư viện phụ thuộc:
    ```bash
    mvn clean install
    ```
4.  Chạy ứng dụng:
    Bạn có thể chạy trực tiếp từ IDE (Eclipse, IntelliJ IDEA) bằng cách chạy file `LoginForm.java` (hoặc file main tương ứng trong `src/View` hoặc `src/QuanLyKhachSan`).

## Công nghệ sử dụng (Technologies)
*   **Ngôn ngữ:** Java 21
*   **GUI Framework:** Java Swing
*   **Database:** MySQL
*   **Thư viện hỗ trợ:**
    *   `mysql-connector-java`: Kết nối MySQL.
    *   `jcalendar`: Hỗ trợ chọn ngày tháng trên giao diện.

## Cấu trúc thư mục (Folder Structure)
*   `HotelManagement/src/View`: Chứa các file giao diện (GUI).
*   `HotelManagement/src/Controller`: Chứa logic điều khiển.
*   `HotelManagement/src/DAO`: Chứa Data Access Objects để thao tác với DB.
*   `HotelManagement/src/Model`: Chứa các entity (Khách hàng, Phòng, ...).
*   `MySQL/`: Chứa các script SQL.

---
*Dự án được phát triển cho mục đích học tập và thực hành Java JDBC.*
