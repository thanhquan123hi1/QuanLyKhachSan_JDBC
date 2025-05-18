package View;

import DAO.KhachHangDAO;
import Model.KhachHang;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.List;

public class JKhachHang extends JPanel {
    private JTable khachHangTable;
    private JTextField searchField;
    private KhachHangDAO khachHangDAO;

    public JKhachHang() {
        khachHangDAO = new KhachHangDAO();
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 220)); // Nền màu be, đồng bộ với Dashboard

        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(135, 206, 235)); // Màu xanh nhạt cho tiêu đề
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Khách hàng");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 63, 92)); // Màu xanh đậm cho tiêu đề
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // Thanh tìm kiếm
        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), // Viền xám nhạt
            BorderFactory.createEmptyBorder(0, 5, 0, 0)
        ));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateTableData(searchField.getText().trim());
            }
        });
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(new Color(135, 206, 235)); // Đồng bộ với tiêu đề
        searchPanel.add(new JLabel("Q "));
        searchPanel.add(searchField);
        titlePanel.add(searchPanel, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);

        // Bảng khách hàng
        String[] columnNames = {"MaKH", "Tên khách hàng", "Quốc Tịch", "CCCD/VISA", "Ngày nhận phòng", "Ngày trả phòng", "Số điện thoại", "Số phòng"};
        updateTableData(""); // Khởi tạo bảng với dữ liệu mặc định
        khachHangTable.setFont(new Font("Arial", Font.PLAIN, 12));
        khachHangTable.setRowHeight(30);
        khachHangTable.setEnabled(false); // Không cho phép chỉnh sửa trực tiếp
        khachHangTable.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200))); // Viền xám nhạt
        khachHangTable.setBackground(Color.WHITE); // Nền bảng màu trắng
        khachHangTable.setGridColor(new Color(200, 200, 200)); // Đường lưới xám nhạt
        JScrollPane tableScrollPane = new JScrollPane(khachHangTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private void updateTableData(String tuKhoa) {
        List<KhachHang> khachHangList = khachHangDAO.getKhachDatPhong(tuKhoa);
        String[] columnNames = {"MaKH", "Tên khách hàng", "Quốc Tịch", "CCCD/VISA", "Ngày nhận phòng", "Ngày trả phòng", "Số điện thoại", "Số phòng"};
        Object[][] data = new Object[khachHangList.size()][8];
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < khachHangList.size(); i++) {
            KhachHang kh = khachHangList.get(i);
            data[i][0] = kh.getMaKH();
            data[i][1] = kh.getTenKH();
            data[i][2] = kh.getQuocTich();
            data[i][3] = kh.getCCCD_VISA();
            data[i][4] = kh.getNgNhanPH() != null ? dateFormat.format(kh.getNgNhanPH()) : "";
            data[i][5] = kh.getNgTraPH() != null ? dateFormat.format(kh.getNgTraPH()) : "";
            data[i][6] = kh.getSDT();
            data[i][7] = kh.getMaPH();
        }

        if (khachHangTable == null) {
            khachHangTable = new JTable(data, columnNames);
        } else {
            khachHangTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        }
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                for (Component innerComponent : panel.getComponents()) {
                    if (innerComponent instanceof JLabel && ((JLabel) innerComponent).getText().contains("Khách hàng")) {
                        ((JLabel) innerComponent).setText(khachHangList.size() + " Khách hàng");
                        break;
                    }
                }
            }
        }
    }
}