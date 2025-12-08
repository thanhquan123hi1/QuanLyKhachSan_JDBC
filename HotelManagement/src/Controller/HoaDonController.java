package Controller;

import DAO.HoaDonDAO;
import DAO.KhachHangDAO;
import Model.HoaDon;
import View.JHoaDon;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class HoaDonController {
    private JHoaDon view;
    private HoaDonDAO hoaDonDAO;
    private KhachHangDAO khachHangDAO;
    private DefaultTableModel tableModel;

    public HoaDonController(JHoaDon view, Connection conn) {
        this.view = view;
        this.hoaDonDAO = new HoaDonDAO(conn);
        this.khachHangDAO = new KhachHangDAO();
        init();
    }

    private void init() {
        // Khởi tạo bảng với các cột
        String[] columnNames = {"Mã HD", "Tên KH", "SĐT", "Tên Hóa Đơn", "Tổng Tiền", "Tình Trạng TT", "Mã PH"};
        tableModel = new DefaultTableModel(columnNames, 0);
        view.getTableHoaDon().setModel(tableModel);

        // Load dữ liệu ban đầu
        loadHoaDon("");

        // Sự kiện tìm kiếm
        view.getTxtSearch().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                loadHoaDon(view.getTxtSearch().getText().trim());
            }
        });

        // Sự kiện thanh toán
        view.getBtnThanhToan().addActionListener(e -> thanhToan());

        // Sự kiện chọn hàng trong bảng
        view.getTableHoaDon().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = view.getTableHoaDon().getSelectedRow();
                    DecimalFormat decimalFormat = new DecimalFormat("#,###"); // Không hiển thị phần thập phân

                    if (selectedRow >= 0) {
                        // Hiển thị thông tin của hàng được chọn
                        String tongTienStr = (String) view.getTableHoaDon().getValueAt(selectedRow, 4); // Cột Tổng Tiền
                        // Loại bỏ tất cả ký tự không phải số
                        tongTienStr = tongTienStr.replaceAll("[^0-9]", "");
                        try {
                            double tongTien = Double.parseDouble(tongTienStr);
                            double thue = tongTien * 0.1; // Thuế 10%
                            double thanhTien = tongTien + thue;

                            view.getTxtTongTien().setText(decimalFormat.format(tongTien));
                            view.getTxtThue().setText(decimalFormat.format(thue));
                            view.getTxtThanhTien().setText(decimalFormat.format(thanhTien));
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(view, "Lỗi định dạng tổng tiền: " + tongTienStr, "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Không có hàng nào được chọn, hiển thị tổng hợp
                        try {
                            double tongTien = hoaDonDAO.tongTienHD(view.getTxtSearch().getText().trim());
                            double thue = tongTien * 0.1; // Thuế 10%
                            double thanhTien = tongTien + thue;

                            view.getTxtTongTien().setText(decimalFormat.format(tongTien));
                            view.getTxtThue().setText(decimalFormat.format(thue));
                            view.getTxtThanhTien().setText(decimalFormat.format(thanhTien));
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(view, "Lỗi khi tính tổng tiền: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    public void loadHoaDon(String tuKhoa) {
        try {
            // Lấy danh sách hóa đơn
            java.util.List<HoaDon> danhSachHoaDon = hoaDonDAO.dsHoaDon(tuKhoa);
            tableModel.setRowCount(0); // Xóa dữ liệu cũ

            DecimalFormat decimalFormat = new DecimalFormat("#,###"); // Không hiển thị phần thập phân

            for (HoaDon hd : danhSachHoaDon) {
                double tongTien = hd.getTongTien();
                System.out.println("TongTien for MaHD " + hd.getMaHD() + ": " + tongTien); // Debug giá trị
                String tongTienFormatted = decimalFormat.format(tongTien);
                Object[] row = {
                    hd.getMaHD(),
                    hd.getTenKH(),
                    hd.getSDT(),
                    hd.getTenHD(),
                    tongTienFormatted,
                    hd.getTinhTrangTT(),
                    hd.getMaPH() == 0 ? "" : hd.getMaPH()
                };
                tableModel.addRow(row);
            }

            // Cập nhật tổng tiền, thuế, thành tiền cho tất cả hóa đơn
            double tongTien = hoaDonDAO.tongTienHD(tuKhoa);
            double thue = tongTien * 0.1; // Giả sử thuế là 10%
            double thanhTien = tongTien + thue;

            view.getTxtTongTien().setText(decimalFormat.format(tongTien));
            view.getTxtThue().setText(decimalFormat.format(thue));
            view.getTxtThanhTien().setText(decimalFormat.format(thanhTien));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Lỗi khi tải danh sách hóa đơn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void thanhToan() {
        int selectedRow = view.getTableHoaDon().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một hóa đơn để thanh toán!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int maHD = (int) view.getTableHoaDon().getValueAt(selectedRow, 0);
        String tenHD = (String) view.getTableHoaDon().getValueAt(selectedRow, 3);
        String sdt = (String) view.getTableHoaDon().getValueAt(selectedRow, 2);
        String hinhThucTT = (String) view.getCboHinhThucTT().getSelectedItem();
        Timestamp ngayTT = Timestamp.valueOf(LocalDateTime.now());

        try {
            int maKH = khachHangDAO.getMaKHFromSDT(sdt);
            if (maKH == -1) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int maPH = 0;
            if (tenHD.equals("Hóa đơn đặt phòng")) {
                Object maPHObj = view.getTableHoaDon().getValueAt(selectedRow, 6); // Cột Mã PH
                if (maPHObj != null && !maPHObj.toString().isEmpty()) {
                    maPH = Integer.parseInt(maPHObj.toString());
                } else {
                    JOptionPane.showMessageDialog(view, "Hóa đơn đặt phòng phải có mã phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            boolean success = hoaDonDAO.thanhToanHoaDon(maKH, maHD, hinhThucTT, ngayTT, maPH);
            if (success) {
                JOptionPane.showMessageDialog(view, "Thanh toán thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadHoaDon(""); // Làm mới danh sách sau khi thanh toán
            } else {
                JOptionPane.showMessageDialog(view, "Hóa đơn không tồn tại hoặc đã thanh toán!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Lỗi khi thanh toán: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}