package Controller;

import View.Dashboard;
import View.JQuanLyPhong;
import View.JCheckIn;
import Model.Phong;
import javax.swing.*;
import java.awt.*;

public class PhongController {
    private Dashboard dashboard;
    private JQuanLyPhong quanLyPhongPanel;

    public PhongController(Dashboard dashboard) {
        this.dashboard = dashboard;
        initController();
    }

    private void initController() {
        JButton quanLyPhongBtn = dashboard.getQuanLyPhongBtn();
        quanLyPhongBtn.addActionListener(e -> {
            showJQuanLyPhong();
        });
    }

    public void showJQuanLyPhong() {
        dashboard.getContentPanel().removeAll();
        quanLyPhongPanel = new JQuanLyPhong(this);
        dashboard.getContentPanel().add(quanLyPhongPanel, BorderLayout.CENTER);
        dashboard.getContentPanel().revalidate();
        dashboard.getContentPanel().repaint();
    }

    public void onRoomClicked(Phong phong) {
        JFrame checkInFrame = new JFrame("Check In - Phòng " + phong.getMaPH());
        checkInFrame.setSize(400, 500);
        checkInFrame.setLocationRelativeTo(null);
        checkInFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JCheckIn checkInPanel = new JCheckIn(phong, this, checkInFrame, quanLyPhongPanel);
        checkInFrame.add(checkInPanel, BorderLayout.CENTER);
        checkInFrame.setVisible(true);
    }
}