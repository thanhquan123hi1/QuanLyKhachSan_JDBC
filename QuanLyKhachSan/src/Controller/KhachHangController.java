package Controller;

import View.Dashboard;
import View.JKhachHang;
import javax.swing.*;
import java.awt.*;

public class KhachHangController {
    private Dashboard dashboard;
    private JKhachHang khachHangPanel;

    public KhachHangController(Dashboard dashboard) {
        this.dashboard = dashboard;
        initController();
    }

    private void initController() {
        JButton khachHangBtn = dashboard.getKhachHangBtn();
        khachHangBtn.addActionListener(e -> {
            showJKhachHang();
        });
    }

    public void showJKhachHang() {
        dashboard.getContentPanel().removeAll();
        khachHangPanel = new JKhachHang();
        dashboard.getContentPanel().add(khachHangPanel, BorderLayout.CENTER);
        dashboard.getContentPanel().revalidate();
        dashboard.getContentPanel().repaint();
    }
}