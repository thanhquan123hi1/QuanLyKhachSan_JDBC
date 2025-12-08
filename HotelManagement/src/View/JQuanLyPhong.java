package View;

import DAO.PhongDAO;
import Model.Phong;
import Controller.PhongController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JQuanLyPhong extends JPanel {
    private PhongController controller;
    private JTextField searchTangField;
    private JTextField searchMaPHField;
    private JComboBox<String> statusComboBox;
    private JPanel roomListPanel;

    public JQuanLyPhong(PhongController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 220));

        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(245, 245, 220));
        JLabel titleLabel = new JLabel("Danh sách phòng");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Panel chứa các nút điều khiển và tìm kiếm
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setBackground(new Color(245, 245, 220));

        // Tìm kiếm theo tầng
        JLabel tangLabel = new JLabel("Tầng:");
        searchTangField = new JTextField(5);
        controlPanel.add(tangLabel);
        controlPanel.add(searchTangField);

        // Tìm kiếm theo mã phòng
        JLabel maPHLabel = new JLabel("Mã phòng:");
        searchMaPHField = new JTextField(5);
        controlPanel.add(maPHLabel);
        controlPanel.add(searchMaPHField);

        // ComboBox trạng thái
        JLabel statusLabel = new JLabel("Trạng thái:");
        String[] statuses = {"Tất cả", "Trống", "Đã đặt"};
        statusComboBox = new JComboBox<>(statuses);
        controlPanel.add(statusLabel);
        controlPanel.add(statusComboBox);

        // Nút tìm kiếm
        JButton timKiemBtn = new JButton("Tìm kiếm");
        timKiemBtn.setBackground(new Color(33, 150, 243));
        timKiemBtn.setForeground(Color.WHITE);
        timKiemBtn.setFocusPainted(false);
        controlPanel.add(timKiemBtn);

        titlePanel.add(controlPanel, BorderLayout.EAST);
        add(titlePanel, BorderLayout.NORTH);

        // Panel chứa danh sách phòng
        roomListPanel = new JPanel();
        roomListPanel.setBackground(new Color(245, 245, 220));

        // Hiển thị danh sách phòng ban đầu
        refreshRoomList();

        // Thêm panel danh sách phòng vào JScrollPane
        JScrollPane scrollPane = new JScrollPane(roomListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // Sự kiện nút Tìm kiếm
        timKiemBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tang = searchTangField.getText().trim();
                String maPH = searchMaPHField.getText().trim();
                String status = (String) statusComboBox.getSelectedItem();
                // Kiểm tra đầu vào
                if (!tang.isEmpty() && !isNumeric(tang)) {
                    JOptionPane.showMessageDialog(JQuanLyPhong.this, "Tầng phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!maPH.isEmpty() && !isNumeric(maPH)) {
                    JOptionPane.showMessageDialog(JQuanLyPhong.this, "Mã phòng phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                updateRoomList(tang, maPH, status);
            }
        });

        revalidate();
        repaint();
    }

    // Kiểm tra chuỗi có phải là số
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Làm mới danh sách phòng
    public void refreshRoomList() {
        updateRoomList("", "", "Tất cả");
    }

    // Phương thức cập nhật danh sách phòng
    private void updateRoomList(String tang, String maPH, String status) {
        roomListPanel.removeAll();
        PhongDAO phongDAO = new PhongDAO();
        java.util.List<Phong> phongList = phongDAO.searchPhong(tang, maPH, status);

        // Thiết lập GridLayout với số cột cố định
        int columns = 5;
        int roomCount = phongList.size();
        int rows = (int) Math.ceil((double) roomCount / columns);
        roomListPanel.setLayout(new GridLayout(rows, columns, 10, 10));

        // Hiển thị danh sách phòng
        for (Phong phong : phongList) {
            JButton roomButton = new JButton();
            roomButton.setPreferredSize(new Dimension(150, 150));
            roomButton.setFont(new Font("Arial", Font.PLAIN, 14));
            roomButton.setFocusPainted(false);
            roomButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));
            roomButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            String tinhTrangPH = phong.getTinhTrangPH() != null ? phong.getTinhTrangPH() : "Không xác định";
            // Định dạng nội dung nút bằng HTML
            String buttonText = "<html><center>" +
                    "<b>Phòng: " + phong.getMaPH() + "</b><br>" +
                    "Loại: " + phong.getLoaiPH() + "<br>" +
                    "Trạng thái: " + tinhTrangPH +
                    "</center></html>";
            roomButton.setText(buttonText);

            // Thiết lập màu nền theo trạng thái
            if (tinhTrangPH.equals("Trống")) {
                roomButton.setBackground(new Color(200, 230, 201)); // Xanh lá nhạt (#C8E6C9)
            } else {
                roomButton.setBackground(new Color(255, 204, 188)); // Cam nhạt (#FFCCBC)
            }
            roomButton.setForeground(Color.BLACK);

            // Hiệu ứng khi di chuột
            roomButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    roomButton.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 2, true));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    roomButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));
                }
            });

            // Sự kiện nhấn nút
            roomButton.addActionListener(e -> {
                if (tinhTrangPH.equals("Đã đặt")) {
                    JOptionPane.showMessageDialog(
                            JQuanLyPhong.this,
                            "Phòng " + phong.getMaPH() + " đã được đặt, không thể thực hiện hành động này!",
                            "Thông báo",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else if (controller != null) {
                    controller.onRoomClicked(phong); // Mở cửa sổ Check-in cho phòng Trống
                }
            });

            roomListPanel.add(roomButton);
        }

        // Điền các ô trống nếu cần để hoàn thiện lưới
        int remainingCells = rows * columns - roomCount;
        for (int i = 0; i < remainingCells; i++) {
            roomListPanel.add(new JPanel()); // Thêm panel trống để giữ bố cục
        }

        // Không cần đặt kích thước cố định cho roomListPanel, GridLayout sẽ tự điều chỉnh
        roomListPanel.revalidate();
        roomListPanel.repaint();
    }
}