package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class JHoaDon extends JPanel {
    private JTextField txtSearch, txtTongTien, txtThue, txtThanhTien;
    private JComboBox<String> cboHinhThucTT;
    private JTable tableHoaDon;
    private JButton btnThanhToan;

    public JHoaDon() {
        // Đặt màu nền nâu nhạt (#D2B48C) để đậm hơn và hợp với màu be của Dashboard
        setBackground(new Color(210, 180, 140));
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false); // Không đặt nền riêng để dùng nền của JHoaDon
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setForeground(new Color(0, 51, 102)); // Màu xanh đậm cho nhãn
        txtSearch = new JTextField(20);
        txtSearch.setBackground(Color.WHITE); // Nền trắng cho trường nhập liệu
        txtSearch.setForeground(new Color(0, 51, 102)); // Màu chữ xanh đậm
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        add(searchPanel, BorderLayout.NORTH);

        // Bảng hóa đơn
        tableHoaDon = new JTable();
        tableHoaDon.setRowHeight(25);
        tableHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableHoaDon.setBackground(Color.WHITE); // Nền trắng cho bảng
        tableHoaDon.setForeground(new Color(0, 51, 102)); // Màu chữ xanh đậm
        tableHoaDon.setGridColor(new Color(200, 200, 200)); // Màu đường lưới nhạt
        tableHoaDon.getTableHeader().setBackground(new Color(230, 218, 184)); // Tiêu đề bảng màu be đậm (#E6DAB8)
        tableHoaDon.getTableHeader().setForeground(new Color(0, 51, 102)); // Màu chữ tiêu đề xanh đậm
        JScrollPane scrollPane = new JScrollPane(tableHoaDon);
        scrollPane.setBackground(new Color(210, 180, 140)); // Nền của JScrollPane khớp với panel
        scrollPane.getViewport().setBackground(new Color(210, 180, 140));
        add(scrollPane, BorderLayout.CENTER);

        // Panel thông tin thanh toán
        JPanel southPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        southPanel.setOpaque(false); // Không đặt nền riêng để dùng nền của JHoaDon
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTongTien = new JLabel("Tổng Tiền:");
        lblTongTien.setForeground(new Color(0, 51, 102)); // Màu xanh đậm
        txtTongTien = new JTextField();
        txtTongTien.setEditable(false);
        txtTongTien.setBackground(Color.WHITE); // Nền trắng
        txtTongTien.setForeground(new Color(0, 51, 102)); // Màu chữ xanh đậm
        southPanel.add(lblTongTien);
        southPanel.add(txtTongTien);

        JLabel lblThue = new JLabel("Thuế (10%):");
        lblThue.setForeground(new Color(0, 51, 102));
        txtThue = new JTextField();
        txtThue.setEditable(false);
        txtThue.setBackground(Color.WHITE);
        txtThue.setForeground(new Color(0, 51, 102));
        southPanel.add(lblThue);
        southPanel.add(txtThue);

        JLabel lblThanhTien = new JLabel("Thành Tiền:");
        lblThanhTien.setForeground(new Color(0, 51, 102));
        txtThanhTien = new JTextField();
        txtThanhTien.setEditable(false);
        txtThanhTien.setBackground(Color.WHITE);
        txtThanhTien.setForeground(new Color(0, 51, 102));
        southPanel.add(lblThanhTien);
        southPanel.add(txtThanhTien);

        JLabel lblHinhThucTT = new JLabel("Hình Thức Thanh Toán:");
        lblHinhThucTT.setForeground(new Color(0, 51, 102));
        cboHinhThucTT = new JComboBox<>(new String[]{"Tiền Mặt", "Thẻ Tín Dụng"});
        cboHinhThucTT.setBackground(Color.WHITE);
        cboHinhThucTT.setForeground(new Color(0, 51, 102));
        southPanel.add(lblHinhThucTT);
        southPanel.add(cboHinhThucTT);

        southPanel.add(new JLabel("")); // Placeholder
        btnThanhToan = new JButton("Thanh Toán");
        btnThanhToan.setBackground(new Color(70, 130, 180)); // Màu xanh thép (#4682B4)
        btnThanhToan.setForeground(Color.WHITE); // Màu chữ trắng
        southPanel.add(btnThanhToan);

        add(southPanel, BorderLayout.SOUTH);
    }

    // Getter methods
    public JTextField getTxtSearch() {
        return txtSearch;
    }

    public JTextField getTxtTongTien() {
        return txtTongTien;
    }

    public JTextField getTxtThue() {
        return txtThue;
    }

    public JTextField getTxtThanhTien() {
        return txtThanhTien;
    }

    public JComboBox<String> getCboHinhThucTT() {
        return cboHinhThucTT;
    }

    public JTable getTableHoaDon() {
        return tableHoaDon;
    }

    public JButton getBtnThanhToan() {
        return btnThanhToan;
    }
}