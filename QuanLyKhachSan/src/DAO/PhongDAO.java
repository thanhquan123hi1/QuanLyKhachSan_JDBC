package DAO;

import Model.Phong;
import Util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PhongDAO {
    List<Phong> Phongs = new ArrayList<>();

    public List<Phong> getAllPhong() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM phong";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int maPH = rs.getInt("MaPH");
                    String loaiPH = rs.getString("LoaiPH");
                    String tinhTrangPH = rs.getString("TinhTrangPH");
                    double giaPH = rs.getDouble("GiaPH");
                    Phongs.add(new Phong(maPH, loaiPH, tinhTrangPH, giaPH));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Phongs;
    }

    public List<Phong> searchPhong(String tang, String maPH, String status) {
        List<Phong> phongList = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM Phong WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (status != null && !status.equals("Tất cả")) {
            query.append(" AND TinhTrangPH = ?");
            params.add(status);
        }
        if (maPH != null && !maPH.isEmpty()) {
            query.append(" AND MaPH = ?");
            params.add(Integer.parseInt(maPH));
        }
        if (tang != null && !tang.isEmpty()) {
            query.append(" AND MaPH DIV 100 = ?");
            params.add(Integer.parseInt(tang));
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Phong phong = new Phong();
                phong.setMaPH(rs.getInt("MaPH"));
                phong.setLoaiPH(rs.getString("LoaiPH"));
                phong.setTinhTrangPH(rs.getString("TinhTrangPH"));
                phong.setGiaPH(rs.getDouble("GiaPH"));
                phongList.add(phong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phongList;
    }
}