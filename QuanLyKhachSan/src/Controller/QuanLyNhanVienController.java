package Controller;

import DAO.QuanLyNhanVienDAO;
import Model.NhanVien;
import View.JQuanLyNhanVien;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuanLyNhanVienController {
    private QuanLyNhanVienDAO dao;
    private JQuanLyNhanVien view;

    public QuanLyNhanVienController(JQuanLyNhanVien view) {
        this.dao = new QuanLyNhanVienDAO();
        this.view = view;
        this.view.setController(this);
        loadData();
    }

    public void loadData() {
        view.updateTable(dao.getAllNhanVien());
    }

    public void addNhanVien(String tenNV, String chucVu, String cccd, String sdt) {
        try {
            int maNV = dao.getAllNhanVien().size() + 1; // Tự động tăng mã NV
            NhanVien nv = new NhanVien(maNV, tenNV, chucVu, cccd, sdt);
            if (dao.addNhanVien(nv)) {
                JOptionPane.showMessageDialog(view, "Thêm nhân viên thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(view, "Mã nhân viên đã tồn tại hoặc lỗi khác!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi khi thêm nhân viên: " + e.getMessage());
        } 
    }

    public void updateNhanVien(int maNV, String tenNV, String chucVu, String cccd, String sdt) {
        NhanVien nv = new NhanVien(maNV, tenNV, chucVu, cccd, sdt);
        if (dao.updateNhanVien(nv)) {
            JOptionPane.showMessageDialog(view, "Cập nhật nhân viên thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(view, "Không tìm thấy nhân viên để cập nhật!");
        }
    }

    public void deleteNhanVien(int maNV) {
        if (dao.deleteNhanVien(maNV)) {
            JOptionPane.showMessageDialog(view, "Xóa nhân viên thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(view, "Không tìm thấy nhân viên để xóa!");
        }
    }

    public NhanVien findNhanVien(int maNV) {
        return dao.findNhanVien(maNV);
    }
}