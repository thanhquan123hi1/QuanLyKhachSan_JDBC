package DAO;

import Model.DichVu;
import Model.SuDungDichVu;
import Model.HoaDon;
import Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class DichVuDAO {

    // Lấy danh sách tất cả dịch vụ từ CSDL
    public List<DichVu> getAllDichVu() {
        List<DichVu> dichVus = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM dichvu")) {
            while (rs.next()) {
                int maDV = rs.getInt("MaDV");
                String tenDV = rs.getString("TenDV");
                double soTien = rs.getDouble("GiaDV");
                dichVus.add(new DichVu(maDV, tenDV, soTien));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy danh sách dịch vụ: " + e.getMessage());
        }
        return dichVus;
    }

    // Tìm dịch vụ theo tên
    public DichVu goiDDV(String tenDV) {
        List<DichVu> dichVus = getAllDichVu();
        return dichVus.stream()
                .filter(p -> p.getTenDV().equals(tenDV))
                .findFirst()
                .orElse(null);
    }

    // Lưu thông tin sử dụng dịch vụ vào bảng sudungdichvu
    public boolean luuSuDungDichVu(SuDungDichVu suDungDichVu) {
        String sql = "INSERT INTO SuDungDichVu (maKH, maDV, soLuong, thoiGian) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, suDungDichVu.getMaKH());
            pstmt.setInt(2, suDungDichVu.getMaDV());
            pstmt.setInt(3, suDungDichVu.getSoLuong());
            pstmt.setTimestamp(4, suDungDichVu.getThoiGian());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lưu thông tin sử dụng dịch vụ: " + e.getMessage());
        }
    }

    // Lấy danh sách sử dụng dịch vụ theo maKH
    public List<SuDungDichVu> getSuDungDichVuByMaKH(int maKH) {
        List<SuDungDichVu> suDungDichVus = new ArrayList<>();
        String sql = "SELECT * FROM SuDungDichVu WHERE maKH = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maKH);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SuDungDichVu suDung = new SuDungDichVu();
                    suDung.setMaKH(rs.getInt("maKH"));
                    suDung.setMaDV(rs.getInt("maDV"));
                    suDung.setSoLuong(rs.getInt("soLuong"));
                    suDung.setThoiGian(rs.getTimestamp("thoiGian"));
                    suDungDichVus.add(suDung);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy danh sách sử dụng dịch vụ: " + e.getMessage());
        }
        return suDungDichVus;
    }

    public boolean kiemTraSuDungDichVu(int maKH, int maDV) {
        String sql = "SELECT * FROM SuDungDichVu WHERE maKH = ? AND maDV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maKH);
            stmt.setInt(2, maDV);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Trả về true nếu có bản ghi
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSuDungDichVu(int maKH, int maDV, int soLuongMoi, Timestamp thoiGian) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Lấy số lượng hiện tại
            String selectSql = "SELECT soLuong FROM SuDungDichVu WHERE maKH = ? AND maDV = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setInt(1, maKH);
            selectStmt.setInt(2, maDV);
            ResultSet rs = selectStmt.executeQuery();
            int soLuongHienTai = 0;
            if (rs.next()) {
                soLuongHienTai = rs.getInt("soLuong");
            }

            // Cập nhật số lượng mới (cộng dồn)
            String updateSql = "UPDATE SuDungDichVu SET soLuong = ?, thoiGian = ? WHERE maKH = ? AND maDV = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setInt(1, soLuongHienTai + soLuongMoi);
            updateStmt.setTimestamp(2, thoiGian);
            updateStmt.setInt(3, maKH);
            updateStmt.setInt(4, maDV);
            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int kiemTraKhachDaSDDV(int maKH, String tenDV) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT maHD FROM HoaDon WHERE maKH = ? AND tenHD = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, maKH);
                pstmt.setString(2, tenDV);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("maHD"); // Trả về maHD nếu tìm thấy
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi ra console để debug
        }
        return -1; // Trả về -1 nếu không tìm thấy hoặc có lỗi
    }
}