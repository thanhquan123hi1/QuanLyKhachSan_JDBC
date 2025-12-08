package DAO;

import Model.HoaDon;
import Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class HoaDonDAO {
    private Connection conn;

    public HoaDonDAO(Connection conn) {
        this.conn = conn;
    }

    public HoaDonDAO() {
        try {
            this.conn = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    public List<HoaDon> dsHoaDon(String tuKhoa) throws SQLException {
        List<HoaDon> danhSachHoaDon = new ArrayList<>();
        String sql = "SELECT hd.MaHD, hd.TenHD, hd.MaKH, kh.TenKH, kh.SDT, hd.TongTien, hd.TinhTrangTT, hd.HinhThucTT, hd.NgayTT, dp.MaPH " +
                     "FROM HoaDon hd " +
                     "JOIN KhachHang kh ON hd.MaKH = kh.MaKH " +
                     "LEFT JOIN DatPhong dp ON hd.MaKH = dp.MaKH " +
                     "WHERE (kh.TenKH LIKE ? OR kh.SDT LIKE ? OR kh.CCCD_VISA LIKE ? OR CAST(COALESCE(dp.MaPH, '') AS CHAR) LIKE ?) " +
                     "AND hd.TinhTrangTT = 'Chua Thanh Toan'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + tuKhoa + "%");
            pstmt.setString(2, "%" + tuKhoa + "%");
            pstmt.setString(3, "%" + tuKhoa + "%");
            pstmt.setString(4, "%" + tuKhoa + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon(
                        rs.getInt("MaHD"),
                        rs.getInt("MaKH"),
                        rs.getString("TenHD"),
                        rs.getDouble("TongTien"), // Đảm bảo lấy giá trị chính xác
                        rs.getString("TinhTrangTT"),
                        rs.getString("HinhThucTT"),
                        rs.getTimestamp("NgayTT")
                    );
                    hd.setTenKH(rs.getString("TenKH"));
                    hd.setSDT(rs.getString("SDT"));
                    int maPH = rs.getInt("MaPH");
                    if (!rs.wasNull()) {
                        hd.setMaPH(maPH);
                    } else {
                        hd.setMaPH(0);
                    }
                    danhSachHoaDon.add(hd);
                }
            }
        }
        return danhSachHoaDon;
    }

    public double tongTienHD(String tuKhoa) throws SQLException {
        String sql = "SELECT SUM(hd.TongTien) as tong_tien " +
                     "FROM HoaDon hd " +
                     "JOIN KhachHang kh ON hd.MaKH = kh.MaKH " +
                     "LEFT JOIN DatPhong dp ON hd.MaKH = dp.MaKH " +
                     "WHERE (kh.TenKH LIKE ? OR kh.SDT LIKE ? OR kh.CCCD_VISA LIKE ? OR CAST(COALESCE(dp.MaPH, '') AS CHAR) LIKE ?) " +
                     "AND hd.TinhTrangTT = 'Chua Thanh Toan'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + tuKhoa + "%");
            pstmt.setString(2, "%" + tuKhoa + "%");
            pstmt.setString(3, "%" + tuKhoa + "%");
            pstmt.setString(4, "%" + tuKhoa + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double tongTien = rs.getDouble("tong_tien");
                    System.out.println("TongTien from DB: " + tongTien); // Debug giá trị
                    return tongTien;
                }
            }
        }
        return 0.0;
    }

    public boolean thanhToanHoaDon(int maKH, int maHD, String hinhThucTT, Timestamp ngayTT, int maPH) throws SQLException {
        String sqlCheck = "SELECT * FROM HoaDon WHERE MaHD = ? AND TinhTrangTT = 'Chua Thanh Toan'";
        HoaDon hoaDon = null;
        try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
            pstmtCheck.setInt(1, maHD);
            try (ResultSet rs = pstmtCheck.executeQuery()) {
                if (rs.next()) {
                    hoaDon = new HoaDon(
                        rs.getInt("MaHD"),
                        rs.getInt("MaKH"),
                        rs.getString("TenHD"),
                        rs.getDouble("TongTien"),
                        rs.getString("TinhTrangTT"),
                        rs.getString("HinhThucTT"),
                        rs.getTimestamp("NgayTT")
                    );
                }
            }
        }

        if (hoaDon == null) {
            return false;
        }

        String sqlUpdate = "UPDATE HoaDon SET TinhTrangTT = ?, HinhThucTT = ?, NgayTT = ? WHERE MaHD = ? AND MaKH = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setString(1, "Da Thanh Toan");
            pstmt.setString(2, hinhThucTT);
            pstmt.setTimestamp(3, ngayTT);
            pstmt.setInt(4, maHD);
            pstmt.setInt(5, maKH);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                if (!hoaDon.getTenHD().equals("Hóa đơn đặt phòng")) {
                    String deleteDVSql = "DELETE FROM SuDungDichVu WHERE MaKH = ?";
                    try (PreparedStatement pstmtDeleteDV = conn.prepareStatement(deleteDVSql)) {
                        pstmtDeleteDV.setInt(1, maKH);
                        pstmtDeleteDV.executeUpdate();
                    }
                }

                if (hoaDon.getTenHD().equals("Hóa đơn đặt phòng")) {
                    String deleteDPSql = "DELETE FROM DatPhong WHERE MaPH = ? AND MaKH = ?";
                    String updatePhongSql = "UPDATE Phong SET TinhTrangPH = 'Trống' WHERE MaPH = ?";
                    try (PreparedStatement pstmtDeleteDP = conn.prepareStatement(deleteDPSql);
                         PreparedStatement pstmtUpdatePhong = conn.prepareStatement(updatePhongSql)) {
                        pstmtDeleteDP.setInt(1, maPH);
                        pstmtDeleteDP.setInt(2, maKH);
                        pstmtDeleteDP.executeUpdate();
                        pstmtUpdatePhong.setInt(1, maPH);
                        pstmtUpdatePhong.executeUpdate();
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean insertHoaDon_1(int maKH, String tenDV, double tongTien, String tinhTrangTT, String hinhThucTT, Timestamp ngayTT) throws SQLException {
        String sql = "INSERT INTO HoaDon (MaKH, TenHD, TongTien, TinhTrangTT, HinhThucTT, NgayTT) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maKH);
            pstmt.setString(2, tenDV);
            pstmt.setDouble(3, tongTien);
            pstmt.setString(4, tinhTrangTT);
            pstmt.setString(5, hinhThucTT);
            pstmt.setTimestamp(6, ngayTT);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean updateHoaDon(int maHD, double tongTien, Timestamp ngayTT) throws SQLException {
        try (PreparedStatement selectStmt = conn.prepareStatement("SELECT TongTien FROM HoaDon WHERE MaHD = ?")) {
            selectStmt.setInt(1, maHD);
            ResultSet rs = selectStmt.executeQuery();
            double tongTienHienTai = 0;
            if (rs.next()) {
                tongTienHienTai = rs.getDouble("TongTien");
            }

            String updateSql = "UPDATE HoaDon SET TongTien = ?, NgayTT = ? WHERE MaHD = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setDouble(1, tongTienHienTai + tongTien);
                updateStmt.setTimestamp(2, ngayTT);
                updateStmt.setInt(3, maHD);
                return updateStmt.executeUpdate() > 0;
            }
        }
    }
}