package DAO;

import Model.NhanVien;
import Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QuanLyNhanVienDAO {
    // Không cần danhSachNhanVien nữa vì dữ liệu được lưu trong CSDL
    // private List<NhanVien> danhSachNhanVien;

    public QuanLyNhanVienDAO() {
        // Không cần khởi tạo ArrayList
    }

    // Lấy danh sách tất cả nhân viên từ CSDL
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> danhSachNhanVien = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM nhanvien";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int maNV = rs.getInt("MaNV");
                    String tenNV = rs.getString("TenNV");
                    String chucVu = rs.getString("ChucVu");
                    String CCCD = rs.getString("CCCD");
                    String SDT = rs.getString("SDT");
                    danhSachNhanVien.add(new NhanVien(maNV, tenNV, chucVu, CCCD, SDT));
                }
            }
        } catch (Exception  e) {
            e.printStackTrace();
        }
        return danhSachNhanVien;
    }

    // Thêm nhân viên vào CSDL
    public boolean addNhanVien(NhanVien nv) {
        String query = "INSERT INTO nhanvien (MaNV, TenNV, ChucVu, CCCD, SDT) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, nv.getMaNV());
            pstmt.setString(2, nv.getTenNV());
            pstmt.setString(3, nv.getChucVu());
            pstmt.setString(4, nv.getCCCD());
            pstmt.setString(5, nv.getSDT());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật thông tin nhân viên trong CSDL
    public boolean updateNhanVien(NhanVien nv) {
        String query = "UPDATE nhanvien SET TenNV = ?, ChucVu = ?, CCCD = ?, SDT = ? WHERE MaNV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, nv.getTenNV());
            pstmt.setString(2, nv.getChucVu());
            pstmt.setString(3, nv.getCCCD());
            pstmt.setString(4, nv.getSDT());
            pstmt.setInt(5, nv.getMaNV());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa nhân viên khỏi CSDL
    public boolean deleteNhanVien(int maNV) {
        String query = "DELETE FROM nhanvien WHERE MaNV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, maNV);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm nhân viên theo MaNV từ CSDL
    public NhanVien findNhanVien(int maNV) {
        String query = "SELECT * FROM nhanvien WHERE MaNV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, maNV);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new NhanVien(
                        rs.getInt("MaNV"),
                        rs.getString("TenNV"),
                        rs.getString("ChucVu"),
                        rs.getString("CCCD"),
                        rs.getString("SDT")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}