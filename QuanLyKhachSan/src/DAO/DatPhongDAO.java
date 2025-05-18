package DAO;

import Model.KhachHang;
import Model.DatPhong;
import Util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DatPhongDAO {

    /**
     * Thêm khách hàng, đặt phòng và tạo hóa đơn vào cơ sở dữ liệu
     * @param tenKH Tên khách hàng
     * @param quocTich Quốc tịch
     * @param cccdVisa CCCD hoặc Visa
     * @param sdt Số điện thoại
     * @param maPH Mã phòng
     * @param hinhThucDP Hình thức đặt phòng
     * @param ngNhanPH Ngày nhận phòng
     * @param ngTraPH Ngày trả phòng
     * @return true nếu thành công, false nếu khách hàng đã tồn tại hoặc phòng không hợp lệ
     * @throws SQLException nếu có lỗi truy vấn
     */
    public boolean insertKhachHangDatPhong(String tenKH, String quocTich, String cccdVisa, String sdt,
                                          int maPH, String hinhThucDP, Date ngNhanPH, Date ngTraPH) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // Kiểm tra khách hàng đã tồn tại chưa (dựa trên CCCD_VISA hoặc SDT)
            String checkKhachHangQuery = "SELECT MaKH FROM KhachHang WHERE CCCD_VISA = ? OR SDT = ?";
            stmt = conn.prepareStatement(checkKhachHangQuery);
            stmt.setString(1, cccdVisa);
            stmt.setString(2, sdt);
            rs = stmt.executeQuery();

            int maKH;
            if (rs.next()) {
                // Khách hàng đã tồn tại, trả về false
                return false;
            } else {
                // Thêm khách hàng mới
                String insertKhachHangQuery = "INSERT INTO KhachHang (TenKH, QuocTich, CCCD_VISA, SDT) VALUES (?, ?, ?, ?)";
                stmt = conn.prepareStatement(insertKhachHangQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setString(1, tenKH);
                stmt.setString(2, quocTich);
                stmt.setString(3, cccdVisa);
                stmt.setString(4, sdt);
                stmt.executeUpdate();

                // Lấy MaKH vừa được tạo
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    maKH = rs.getInt(1);
                } else {
                    throw new SQLException("Không thể lấy MaKH sau khi thêm khách hàng.");
                }
            }

            // Lấy thông tin phòng
            String getPhongQuery = "SELECT GiaPH FROM Phong WHERE MaPH = ?";
            stmt = conn.prepareStatement(getPhongQuery);
            stmt.setInt(1, maPH);
            rs = stmt.executeQuery();
            double giaPH;
            if (rs.next()) {
                giaPH = rs.getDouble("GiaPH");
            } else {
                throw new SQLException("Không tìm thấy phòng với MaPH = " + maPH);
            }

            // Tính số ngày lưu trú
            long diffInMillies = ngTraPH.getTime() - ngNhanPH.getTime();
            long soNgay = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (soNgay <= 0) {
                throw new SQLException("Ngày trả phòng phải sau ngày nhận phòng.");
            }

            // Tính tổng tiền
            double tongTien = soNgay * giaPH;

            // Thêm thông tin đặt phòng
            String insertDatPhongQuery = "INSERT INTO DatPhong (MaPH, MaKH, HinhThucDP, NgNhanPH, NgTraPH) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertDatPhongQuery);
            stmt.setInt(1, maPH);
            stmt.setInt(2, maKH);
            stmt.setString(3, hinhThucDP);
            stmt.setTimestamp(4, new Timestamp(ngNhanPH.getTime()));
            stmt.setTimestamp(5, new Timestamp(ngTraPH.getTime()));
            stmt.executeUpdate();

            // Cập nhật trạng thái phòng thành "Đã đặt"
            String updatePhongQuery = "UPDATE Phong SET TinhTrangPH = 'Đã đặt' WHERE MaPH = ?";
            stmt = conn.prepareStatement(updatePhongQuery);
            stmt.setInt(1, maPH);
            stmt.executeUpdate();

            // Tạo hóa đơn
            String insertHoaDonQuery = "INSERT INTO HoaDon (MaKH, TenHD, TongTien, TinhTrangTT, HinhThucTT, NgayTT) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertHoaDonQuery);
            stmt.setInt(1, maKH);
            stmt.setString(2, "Hóa đơn đặt phòng");
            stmt.setDouble(3, tongTien);
            stmt.setString(4, "Chua Thanh Toan");
            stmt.setString(5, "Chưa TT");
            stmt.setNull(6, java.sql.Types.TIMESTAMP); // NgayTT = NULL
            stmt.executeUpdate();

            conn.commit(); // Xác nhận giao dịch
            success = true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Hoàn tác giao dịch nếu có lỗi
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw e; // Ném ngoại lệ để xử lý ở tầng trên
        } finally {
            // Đóng tài nguyên
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return success;
    }
}