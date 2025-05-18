package View;

import Controller.DichVuController;
import Model.DichVu;
import Model.HoaDon;
import Model.SuDungDichVu;
import DAO.DichVuDAO;
import DAO.HoaDonDAO;
import Util.DatabaseConnection;
import DAO.KhachHangDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.sql.Timestamp;

public class JDichVu extends JPanel {
    private DichVuController controller;
    private static final Color GRADIENT_START = new Color(32, 87, 129);
    private static final Color GRADIENT_END = new Color(10, 50, 90);
    private JLabel clockLabel;
    private JTextField txtMaDV, txtTenDichVu, txtSoTien, txtThoiGianSDDV, txtSDT, txtSoLuong;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public JDichVu(DichVuController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Panel chứa các trường nhập liệu
        JPanel panelText = new JPanel(new GridLayout(2, 6, 15, 15));
        panelText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelText.setOpaque(false);

        // Tạo các trường nhập liệu và nhãn
        JLabel lbSDT = createLabel("Số Điện Thoại");
        txtSDT = createTextField();
        JLabel lbSoLuong = createLabel("Số Lượng");
        txtSoLuong = createTextField();
        JLabel lbMaDV = createLabel("Mã Dịch Vụ");
        txtMaDV = createTextField();
        JLabel lbTenDichVu = createLabel("Tên Dịch Vụ");
        txtTenDichVu = createTextField();
        JLabel lbThoiGianSDDV = createLabel("Thời Gian");
        txtThoiGianSDDV = createTextField();
        JLabel lbSoTien = createLabel("Số Tiền");
        txtSoTien = createTextField();

        // Hiển thị thời gian hiện tại mặc định trong txtThoiGianSDDV
        LocalDateTime now = LocalDateTime.now();
        txtThoiGianSDDV.setText(now.format(timeFormatter));

        add(panelText, BorderLayout.NORTH);

        // Thêm trực tiếp vào GridLayout
        panelText.add(lbSDT);
        panelText.add(txtSDT);
        panelText.add(lbSoLuong);
        panelText.add(txtSoLuong);
        panelText.add(lbMaDV);
        panelText.add(txtMaDV);
        panelText.add(lbTenDichVu);
        panelText.add(txtTenDichVu);
        panelText.add(lbThoiGianSDDV);
        panelText.add(txtThoiGianSDDV);
        panelText.add(lbSoTien);
        panelText.add(txtSoTien);

        // Panel chứa các dịch vụ
        JPanel pnDV = new JPanel();
        pnDV.setLayout(new BoxLayout(pnDV, BoxLayout.X_AXIS));
        pnDV.setOpaque(false);
        pnDV.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Thêm các panel dịch vụ
        pnDV.add(createServicePanel("Nhà Hàng", "src/src_pic/thuc_anjpg.jpg", "Đặt"));
        pnDV.add(Box.createHorizontalStrut(15));
        pnDV.add(createServicePanel("Bar", "src/src_pic/Bar.jpg", "Đặt"));
        pnDV.add(Box.createHorizontalStrut(15));
        pnDV.add(createServicePanel("Hồ Bơi", "src/src_pic/HB.jpg", "Đặt"));
        pnDV.add(Box.createHorizontalStrut(15));
        pnDV.add(createServicePanel("Máy Giặt", "src/src_pic/MG.jpg", "Đặt"));
        pnDV.add(Box.createHorizontalStrut(15));
        pnDV.add(createServicePanel("Xông Hơi", "src/src_pic/Xong_Hoi.jpg", "Đặt"));

        add(pnDV, BorderLayout.CENTER);

        // Tạo đồng hồ
        clockLabel = new JLabel();
        clockLabel.setPreferredSize(new Dimension(150, 30));
        clockLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        clockLabel.setForeground(Color.WHITE);
        clockLabel.setHorizontalAlignment(SwingConstants.CENTER);
        clockLabel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1, true));

        // Cập nhật thời gian mỗi giây
        Timer timer = new Timer(1000, e -> updateClock());
        timer.start();
        updateClock();

        // Panel chứa nút và đồng hồ ở dưới
        JPanel southPanel = new JPanel(new BorderLayout(10, 0));
        southPanel.setOpaque(false);

        // Nút xác nhận chính
        JButton btnXacNhan = createStyledButton("Xác Nhận");

        btnXacNhan.addActionListener(e -> {
            // Lấy dữ liệu từ các TextField
            String sdt = txtSDT.getText().trim();
            String tenDV = txtTenDichVu.getText().trim();
            String soTienStr = txtSoTien.getText().trim();
            String soLuongStr = txtSoLuong.getText().trim();
            String maDVStr = txtMaDV.getText().trim();

            // Kiểm tra dữ liệu đầu vào
            if (sdt.isEmpty() || tenDV.isEmpty() || soTienStr.isEmpty() || soLuongStr.isEmpty() || maDVStr.isEmpty()) {
                JOptionPane.showMessageDialog(JDichVu.this, "Vui lòng điền đầy đủ thông tin!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Kiểm tra định dạng số
            int maDV, soLuong;
            double tongTien;
            try {
                maDV = Integer.parseInt(maDVStr);
                soLuong = Integer.parseInt(soLuongStr);
                tongTien = Double.parseDouble(soTienStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(JDichVu.this, "Mã dịch vụ, số lượng và số tiền phải là số hợp lệ!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Kiểm tra số lượng dương
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(JDichVu.this, "Số lượng phải lớn hơn 0!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Tính tổng tiền
            tongTien *= soLuong;

            // Lấy thời gian hiện tại
            LocalDateTime now1 = LocalDateTime.now();
            Timestamp thoiGian = Timestamp.valueOf(now1);
            txtThoiGianSDDV.setText(now1.format(timeFormatter));

            // Tìm MaKH dựa trên SDT
            KhachHangDAO khachHangDAO = new KhachHangDAO();
            int maKH = khachHangDAO.getMaKHFromSDT(sdt);
            if (maKH == -1) {
                JOptionPane.showMessageDialog(JDichVu.this, "Khách hàng không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Xử lý dịch vụ
            DichVuDAO dichVuDAO = new DichVuDAO();
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false); // Bắt đầu giao dịch
                HoaDonDAO hoaDonDAO = new HoaDonDAO(conn);

                // Kiểm tra khách hàng đã sử dụng dịch vụ này chưa
                if (dichVuDAO.kiemTraSuDungDichVu(maKH, maDV)) {
                    // Cập nhật SuDungDichVu
                    boolean isUpdated = dichVuDAO.updateSuDungDichVu(maKH, maDV, soLuong, thoiGian);
                    // Cập nhật HoaDon
                    int maHD = dichVuDAO.kiemTraKhachDaSDDV(maKH, tenDV);
                    if (maHD == -1) {
                        JOptionPane.showMessageDialog(JDichVu.this, "Không tìm thấy hóa đơn dịch vụ để cập nhật!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        conn.rollback();
                        return;
                    }
                    boolean isHoaDonUpdated = hoaDonDAO.updateHoaDon(maHD, tongTien, thoiGian);

                    if (isUpdated && isHoaDonUpdated) {
                        conn.commit();
                        JOptionPane.showMessageDialog(JDichVu.this, "Cập nhật dịch vụ thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        conn.rollback();
                        JOptionPane.showMessageDialog(JDichVu.this, "Cập nhật dịch vụ thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Thêm mới SuDungDichVu
                    SuDungDichVu suDungDichVu = new SuDungDichVu();
                    suDungDichVu.setMaKH(maKH);
                    suDungDichVu.setMaDV(maDV);
                    suDungDichVu.setSoLuong(soLuong);
                    suDungDichVu.setThoiGian(thoiGian);
                    boolean isSuDungSaved = dichVuDAO.luuSuDungDichVu(suDungDichVu);

                    // Thêm mới HoaDon
                    boolean isHoaDonInserted = hoaDonDAO.insertHoaDon_1(maKH, tenDV, tongTien, "Chua Thanh Toan", "", thoiGian);

                    if (isSuDungSaved && isHoaDonInserted) {
                        conn.commit();
                        JOptionPane.showMessageDialog(JDichVu.this, "Thêm dịch vụ thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        conn.rollback();
                        JOptionPane.showMessageDialog(JDichVu.this, "Thêm dịch vụ thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(JDichVu.this, "Lỗi cơ sở dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setOpaque(false);
        btnPanel.add(btnXacNhan);
        southPanel.add(btnPanel, BorderLayout.WEST);

        // Đặt đồng hồ ở góc dưới bên phải
        JPanel clockPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        clockPanel.setOpaque(false);
        clockPanel.add(clockLabel);
        southPanel.add(clockPanel, BorderLayout.EAST);

        add(southPanel, BorderLayout.SOUTH);
    }

    public void setController(DichVuController controller) {
        this.controller = controller;
    }

    private void updateClock() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        clockLabel.setText(sdf.format(new Date()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        GradientPaint gradient = new GradientPaint(0, 0, GRADIENT_START, 0, getHeight(), GRADIENT_END);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(150, 30));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        textField.setBackground(new Color(255, 255, 255));
        textField.setOpaque(true);
        return textField;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(Color.WHITE);
        label.setPreferredSize(new Dimension(80, 30));
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(0, 180, 255));
                } else {
                    g2d.setColor(new Color(0, 153, 255));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        button.setBorder(new EmptyBorder(10, 10, 10, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 50)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return button;
    }

    private JPanel createServicePanel(String title, String imagePath, String buttonText) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(245, 238, 220));
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(120, 200));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel picLabel = new JLabel(new ImageIcon(img));
        picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        picLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(32, 87, 129));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton button = createStyledButton(buttonText);
        button.setPreferredSize(new Dimension(140, 40));
        button.addActionListener(e -> {
            if (controller != null) {
                String tenDV = "";
                LocalDateTime now = LocalDateTime.now();
                Timestamp thoiGian = Timestamp.valueOf(now);
                txtThoiGianSDDV.setText(now.format(timeFormatter));

                if (title.equals("Nhà Hàng")) {
                    int hour = now.getHour();
                    if (hour >= 5 && hour < 9) {
                        tenDV = "Nhà hàng ban sáng";
                    } else if (hour >= 9 && hour < 15) {
                        tenDV = "Nhà hàng ban trưa";
                    } else if (hour >= 15 && hour <= 23) {
                        tenDV = "Nhà hàng ban đêm";
                    }
                } else if (title.equals("Bar")) {
                    tenDV = "Bar (ban đêm)";
                } else if (title.equals("Hồ Bơi")) {
                    tenDV = "Bể bơi";
                } else if (title.equals("Máy Giặt")) {
                    tenDV = "Giặt, Sấy";
                } else if (title.equals("Xông Hơi")) {
                    tenDV = "Xông hơi";
                }

                controller.HienThiDV(tenDV);
            }
        });

        panel.add(picLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(button);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0, 153, 255), 2),
                        BorderFactory.createEmptyBorder(13, 13, 13, 13)));
                panel.setLocation(panel.getX(), panel.getY() - 5);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
                panel.setLocation(panel.getX(), panel.getY() + 5);
            }
        });

        return panel;
    }

    public void HienThiDV(DichVu d) {
        String maDV = String.valueOf(d.getMaDV());
        String GiaTien = String.valueOf(d.getGiaDV());
        txtMaDV.setText(maDV);
        txtTenDichVu.setText(d.getTenDV());
        txtSoTien.setText(GiaTien);
    }
}