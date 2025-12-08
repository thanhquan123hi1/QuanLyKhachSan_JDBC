package DAO;

import Model.KhachHang;
import Util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class KhachHangDAO {

    public KhachHangDAO() {
		super();
	}

	public List<KhachHang> getKhachDatPhong(String tuKhoa) {
        List<KhachHang> khachHangList = new ArrayList<>();
        String query = "SELECT dp.MaPH, dp.NgNhanPH, dp.NgTraPH, kh.MaKH, kh.TenKH, kh.QuocTich, kh.CCCD_VISA, kh.SDT " +
                       "FROM DatPhong dp " +
                       "JOIN KhachHang kh ON dp.MaKH = kh.MaKH " +
                       "WHERE kh.MaKH LIKE ? " +
                       "OR kh.TenKH LIKE ? " +
                       "OR kh.SDT LIKE ? " +
                       "OR kh.CCCD_VISA LIKE ? " +
                       "OR dp.MaPH LIKE ? " +
                       "OR dp.NgNhanPH LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Thêm % vào tuKhoa để thực hiện tìm kiếm kiểu LIKE
            String searchPattern = "%" + tuKhoa + "%";
            for (int i = 1; i <= 6; i++) {
                stmt.setString(i, searchPattern);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getInt("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setQuocTich(rs.getString("QuocTich"));
                kh.setCCCD_VISA(rs.getString("CCCD_VISA"));
                kh.setSDT(rs.getString("SDT"));
                kh.setNgNhanPH(rs.getTimestamp("NgNhanPH")); // Thêm trường từ DatPhong
                kh.setNgTraPH(rs.getTimestamp("NgTraPH"));   // Thêm trường từ DatPhong
                kh.setMaPH(rs.getInt("MaPH"));               // Thêm trường từ DatPhong
                khachHangList.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return khachHangList;
    }

    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> khachHangList = new ArrayList<>();
        String query = "SELECT MaKH, TenKH, QuocTich, CCCD_VISA, SDT FROM KhachHang";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getInt("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setQuocTich(rs.getString("QuocTich"));
                kh.setCCCD_VISA(rs.getString("CCCD_VISA"));
                kh.setSDT(rs.getString("SDT"));
                khachHangList.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return khachHangList;
    }
    
    public int getMaKHFromSDT(String sdt) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT MaKH FROM KhachHang WHERE SDT = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, sdt);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("MaKH");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}