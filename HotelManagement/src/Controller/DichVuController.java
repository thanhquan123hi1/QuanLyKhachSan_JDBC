package Controller;

import View.Dashboard;
import View.JDichVu;
import Model.DichVu;
import DAO.DichVuDAO;

import javax.swing.*;
import java.awt.*;

public class DichVuController {
    private Dashboard dashboard;
    private JDichVu jDV;

    public DichVuController(Dashboard dashboard, JDichVu jDV) {
        this.dashboard = dashboard;
        this.jDV = jDV;
        initController();
    }

    private void initController() {
        // Lấy nút "Dịch vụ" từ Dashboard
        JButton DV = dashboard.getDV();

        // Thêm sự kiện cho nút "Dịch vụ"
        DV.addActionListener(e -> {
            // Xóa nội dung hiện tại của contentPanel
            dashboard.getContentPanel().removeAll();

            // Thêm panel JDichVu vào contentPanel
            dashboard.getContentPanel().add(jDV, BorderLayout.CENTER);

            // Cập nhật giao diện
            dashboard.getContentPanel().revalidate();
            dashboard.getContentPanel().repaint();
        });
    }

    public void HienThiDV(String tenDV) {
        try {
            DichVuDAO d = new DichVuDAO();
            DichVu dichVu = d.goiDDV(tenDV); // Sửa GoiDDV thành goiDDV để khớp với DichVuDAO
            if (dichVu != null) {
                jDV.HienThiDV(dichVu);
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy dịch vụ: " + tenDV);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi hiển thị dịch vụ: " + e.getMessage());
        }
    }
}