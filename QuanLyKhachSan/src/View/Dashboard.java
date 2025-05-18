package View;

import Controller.PhongController;
import Controller.KhachHangController;
import Controller.DichVuController;
import Controller.QuanLyNhanVienController;
import Controller.HoaDonController;
import View.JDichVu;
import View.JQuanLyNhanVien;
import View.JHoaDon;
import Util.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class Dashboard extends JFrame {
    private JPanel sidebar;
    private JButton quanLyPhongBtn;
    private JButton khachHangBtn;
    private JButton dichVuBtn;
    private JButton hoaDonBtn;
    private JButton quanLyNhanVienBtn;
    private JButton dangXuatBtn;
    private JPanel contentPanel;
    private HoaDonController hoaDonController;

    public Dashboard() {
        // Thiết lập khung giao diện
        setTitle("Panda Bamboo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720); // Standard window size
        setMinimumSize(new Dimension(1200, 700)); // Minimum size for responsiveness

        // Tạo panel chính với BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245)); // Soft cream background

        // Nhãn chào mừng
        JLabel welcomeLabel = new JLabel("WELCOME TO PANDA BAMBOO");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setOpaque(true);
        welcomeLabel.setBackground(new Color(42, 93, 82)); // Darker green header
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Thanh bên trái
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0)); // Wider sidebar
        sidebar.setBackground(new Color(26, 60, 52)); // Deep forest green sidebar

        // Logo
        String path = "src/src_pic/LG4.png";
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel logoIcon = new JLabel(new ImageIcon(img));
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoIcon.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

      
        logoIcon.setFont(new Font("Arial", Font.BOLD, 60));
        logoIcon.setForeground(new Color(255, 255, 255)); // Soft white
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoIcon.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebar.add(logoIcon);
        JLabel logoText = new JLabel("Panda Bamboo");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoText.setForeground(new Color(255, 255, 255)); // Soft white
        logoText.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logoText);
        sidebar.add(Box.createVerticalStrut(25));

        // Menu items with icons
        String[] menuItems = {"Quản lý phòng", "Khách hàng", "Dịch vụ", "Hóa Đơn", "Quản lý nhân viên", "Đăng xuất"};
        for (int i = 0; i < menuItems.length; i++) {
            JButton button = createStyledButton(menuItems[i]);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(200, 40)); // Wider button
            sidebar.add(button);
            sidebar.add(Box.createVerticalStrut(5)); // Compact spacing
            if (menuItems[i].equals("Quản lý phòng")) {
                quanLyPhongBtn = button;
            }
            if (menuItems[i].equals("Khách hàng")) {
                khachHangBtn = button;
            }
            if (menuItems[i].equals("Dịch vụ")) {
                dichVuBtn = button;
            }
            if (menuItems[i].equals("Hóa Đơn")) {
                hoaDonBtn = button;
            }
            if (menuItems[i].equals("Quản lý nhân viên")) {
                quanLyNhanVienBtn = button;
            }
            if (menuItems[i].equals("Đăng xuất")) {
                dangXuatBtn = button;
            }
        }

        mainPanel.add(sidebar, BorderLayout.WEST);

        // Panel trung tâm để chứa nội dung thay đổi
        contentPanel = new JPanel();
        contentPanel.setBackground(new Color(250, 250, 249)); // Light cream background
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel logoLabel = new JLabel("PANDA BAMBOO");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        logoLabel.setForeground(new Color(26, 60, 52)); // Deep green
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sloganLabel = new JLabel("STAY GREEN, STAY SERENE");
        sloganLabel.setFont(new Font("Segoe UI", Font.ITALIC, 24));
        sloganLabel.setForeground(new Color(74, 85, 104)); // Muted gray
        sloganLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(logoLabel);
        contentPanel.add(Box.createVerticalStrut(10)); // Khoảng cách
        contentPanel.add(sloganLabel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Thêm panel chính vào khung
        add(mainPanel);

        // Căn giữa khung giao diện
        setLocationRelativeTo(null);

        // Khởi tạo các panel và controller
        JDichVu jDichVu = new JDichVu(null); // Sẽ setController sau
        JQuanLyNhanVien jQuanLyNhanVien = new JQuanLyNhanVien();
        JHoaDon jHoaDon = new JHoaDon();

        new PhongController(this);
        new KhachHangController(this);
        new DichVuController(this, jDichVu);
        new QuanLyNhanVienController(jQuanLyNhanVien);

        // Khởi tạo HoaDonController với kết nối cơ sở dữ liệu
        try {
            Connection conn = DatabaseConnection.getConnection();
            hoaDonController = new HoaDonController(jHoaDon, conn); // Lưu tham chiếu
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }

        // Gán controller cho JDichVu
        jDichVu.setController(new DichVuController(this, jDichVu));

        // Thêm sự kiện cho nút "Hóa Đơn"
        hoaDonBtn.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(jHoaDon, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
            hoaDonController.loadHoaDon(""); // Làm mới danh sách hóa đơn
        });

        // Thêm sự kiện cho nút "Quản lý nhân viên"
        quanLyNhanVienBtn.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(jQuanLyNhanVien, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        // Thêm sự kiện cho nút "Đăng xuất"
        dangXuatBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                // Có thể thêm logic để quay lại màn hình đăng nhập nếu có
            }
        });

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text); // Plain text with icon
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(74, 85, 104)); // Neutral gray
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Adjusted padding
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT); // Align text left

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(104, 211, 145)); // Light green
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(74, 85, 104));
            }
        });

        return button;
    }

    // Getter methods
    public JPanel getSidebar() {
        return sidebar;
    }

    public JButton getQuanLyPhongBtn() {
        return quanLyPhongBtn;
    }

    public JButton getKhachHangBtn() {
        return khachHangBtn;
    }

    public JButton getDV() {
        return dichVuBtn;
    }

    public JButton getHoaDonBtn() {
        return hoaDonBtn;
    }

    public JButton getQuanLyNhanVienBtn() {
        return quanLyNhanVienBtn;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }
}