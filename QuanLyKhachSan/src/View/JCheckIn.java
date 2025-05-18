package View;

import Controller.PhongController;
import Model.Phong;
import DAO.DatPhongDAO;
import javax.swing.*;
import java.awt.*;
import com.toedter.calendar.JDateChooser;
import java.util.Date;
import java.sql.SQLException;

public class JCheckIn extends JPanel {
    private JButton phongBtn;
    private PhongController controller;
    private JFrame parentFrame;
    private JComboBox<String> hinhThucComboBox;
    private JDateChooser checkinDateChooser;
    private JDateChooser checkoutDateChooser;
    private JTextField hoVaTenField;
    private JTextField quocTichField;
    private JTextField cccdField;
    private JTextField sdtField;
    private JQuanLyPhong quanLyPhongPanel; // Tham chiếu đến panel danh sách phòng

    public JCheckIn(Phong phong, PhongController controller, JFrame parentFrame, JQuanLyPhong quanLyPhongPanel) {
        this.controller = controller;
        this.parentFrame = parentFrame;
        this.quanLyPhongPanel = quanLyPhongPanel;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 220));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(245, 245, 220));
        JLabel titleLabel = new JLabel("CHECK IN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(34, 139, 34));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(245, 245, 220));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Họ và tên
        hoVaTenField = createInputField(formPanel, "Họ và tên         ");

        // Quốc tịch
        quocTichField = createInputField(formPanel, "Quốc tịch        ");

        // CCCD / Visa
        cccdField = createInputField(formPanel, "CCCD or VISA");

        // Số điện thoại
        sdtField = createInputField(formPanel, "Số điện thoại   ");

        // Nút phòng
        JPanel phongPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        phongPanel.setBackground(new Color(245, 245, 220));
        phongBtn = new JButton("Phòng " + phong.getMaPH());
        phongBtn.setBackground(new Color(255, 165, 0));
        phongBtn.setForeground(Color.WHITE);
        phongBtn.setFocusPainted(false);
        phongBtn.setFont(new Font("Arial", Font.BOLD, 12));
        phongBtn.setPreferredSize(new Dimension(100, 30));
        phongPanel.add(phongBtn);
        formPanel.add(phongPanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Hình thức đặt phòng
        JPanel hinhThucPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        hinhThucPanel.setBackground(new Color(245, 245, 220));
        JLabel hinhThucLabel = new JLabel("Hình thức           ");
        hinhThucLabel.setForeground(Color.GRAY);
        hinhThucLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        String[] hinhThucOptions = {"Online", "Trực Tiếp"};
        hinhThucComboBox = new JComboBox<>(hinhThucOptions);
        hinhThucComboBox.setPreferredSize(new Dimension(220, 25));
        hinhThucPanel.add(hinhThucLabel);
        hinhThucPanel.add(hinhThucComboBox);
        formPanel.add(hinhThucPanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Ngày Check-in, Check-out
        JPanel datePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        datePanel.setBackground(new Color(245, 245, 220));

        JLabel checkinLabel = new JLabel("Ngày Checkin   ");
        checkinLabel.setForeground(Color.GRAY);
        checkinLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        checkinDateChooser = new JDateChooser();
        checkinDateChooser.setDateFormatString("dd/MM/yyyy");
        checkinDateChooser.setDate(new Date());
        checkinDateChooser.setPreferredSize(new Dimension(220, 25));
        datePanel.add(checkinLabel);
        datePanel.add(checkinDateChooser);

        JLabel checkoutLabel = new JLabel("Ngày Checkout ");
        checkoutLabel.setForeground(Color.GRAY);
        checkoutLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        checkoutDateChooser = new JDateChooser();
        checkoutDateChooser.setDateFormatString("dd/MM/yyyy");
        checkoutDateChooser.setDate(new Date());
        checkoutDateChooser.setPreferredSize(new Dimension(220, 25));
        datePanel.add(checkoutLabel);
        datePanel.add(checkoutDateChooser);

        formPanel.add(datePanel);
        formPanel.add(Box.createVerticalStrut(20));

        // Nút đặt phòng
        JButton datPhongBtn = new JButton("Đặt phòng");
        datPhongBtn.setBackground(new Color(255, 105, 180));
        datPhongBtn.setForeground(Color.WHITE);
        datPhongBtn.setFocusPainted(false);
        datPhongBtn.setFont(new Font("Arial", Font.BOLD, 14));
        datPhongBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        datPhongBtn.setPreferredSize(new Dimension(150, 35));
        formPanel.add(datPhongBtn);
        formPanel.add(Box.createVerticalStrut(15));

        // Sự kiện đặt phòng
        datPhongBtn.addActionListener(e -> {
            String tenKH = hoVaTenField.getText().trim();
            String quocTich = quocTichField.getText().trim();
            String cccdVisa = cccdField.getText().trim();
            String sdt = sdtField.getText().trim();
            String hinhThuc = (String) hinhThucComboBox.getSelectedItem();
            Date checkinDate = checkinDateChooser.getDate();
            Date checkoutDate = checkoutDateChooser.getDate();

            // Kiểm tra dữ liệu đầu vào
            if (tenKH.isEmpty() || quocTich.isEmpty() || cccdVisa.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(JCheckIn.this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (checkinDate == null || checkoutDate == null) {
                JOptionPane.showMessageDialog(JCheckIn.this, "Vui lòng chọn ngày Check-in và Check-out!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (checkoutDate.before(checkinDate)) {
                JOptionPane.showMessageDialog(JCheckIn.this, "Ngày Check-out phải lớn hơn hoặc bằng ngày Check-in!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (tenKH.length() > 100) {
                JOptionPane.showMessageDialog(JCheckIn.this, "Họ và tên không được vượt quá 100 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (quocTich.length() > 50) {
                JOptionPane.showMessageDialog(JCheckIn.this, "Quốc tịch không được vượt quá 50 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (cccdVisa.length() > 50) {
                JOptionPane.showMessageDialog(JCheckIn.this, "CCCD or VISA không được vượt quá 50 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (sdt.length() > 20) {
                JOptionPane.showMessageDialog(JCheckIn.this, "Số điện thoại không được vượt quá 20 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!sdt.matches("\\d+")) {
                JOptionPane.showMessageDialog(JCheckIn.this, "Số điện thoại chỉ được chứa số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Gọi DatPhongDAO để thực hiện đặt phòng
            DatPhongDAO datPhongDAO = new DatPhongDAO();
            try {
                boolean success = datPhongDAO.insertKhachHangDatPhong(
                    tenKH, quocTich, cccdVisa, sdt, phong.getMaPH(), hinhThuc, checkinDate, checkoutDate
                );
                if (success) {
                    JOptionPane.showMessageDialog(JCheckIn.this, "Đặt phòng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    // Làm mới danh sách phòng
                    if (quanLyPhongPanel != null) {
                        quanLyPhongPanel.refreshRoomList();
                    }
                    parentFrame.dispose(); // Đóng cửa sổ Check-in
                } else {
                    JOptionPane.showMessageDialog(JCheckIn.this, "Khách hàng đã tồn tại với CCCD/Visa hoặc số điện thoại này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                String errorMessage = ex.getMessage();
                if (errorMessage.contains("Ngày trả phòng phải sau ngày nhận phòng")) {
                    JOptionPane.showMessageDialog(JCheckIn.this, "Ngày trả phòng phải sau ngày nhận phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } else if (errorMessage.contains("Không tìm thấy phòng")) {
                    JOptionPane.showMessageDialog(JCheckIn.this, "Phòng không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(JCheckIn.this, "Lỗi cơ sở dữ liệu: " + errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(formPanel, BorderLayout.CENTER);
    }

    private JTextField createInputField(JPanel parent, String labelText) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(245, 245, 220));
        JLabel label = new JLabel(labelText + "          ");
        label.setForeground(Color.GRAY);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 12));
        textField.setPreferredSize(new Dimension(300, 25));
        panel.add(label);
        panel.add(textField);
        parent.add(panel);
        parent.add(Box.createVerticalStrut(10));
        return textField;
    }

    // Getter
    public JButton getPhongBtn() {
        return phongBtn;
    }

    public JComboBox<String> getHinhThucComboBox() {
        return hinhThucComboBox;
    }

    public JDateChooser getCheckinDateChooser() {
        return checkinDateChooser;
    }

    public JDateChooser getCheckoutDateChooser() {
        return checkoutDateChooser;
    }
}