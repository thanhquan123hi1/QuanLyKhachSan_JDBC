package View;

import Model.NhanVien;
import Controller.QuanLyNhanVienController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class JQuanLyNhanVien extends JPanel {
    private QuanLyNhanVienController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtMaNV, txtTenNV, txtChucVu, txtCCCD, txtSDT;
    private JButton btnAdd, btnUpdate, btnDelete, btnFind;

    public JQuanLyNhanVien() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setPreferredSize(new Dimension(850, 450));

        // Tiêu đề
        JLabel titleLabel = new JLabel("Quản Lý Nhân Viên");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Panel chứa bảng
        String[] columnNames = {"Mã NV", "Tên NV", "Chức vụ", "CCCD", "SĐT"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setBackground(new Color(245, 238, 220));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 250));
        add(scrollPane, BorderLayout.CENTER);

        // Panel chứa các trường nhập liệu và nút
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Mã NV:"), gbc);
        gbc.gridx = 1;
        txtMaNV = new JTextField(10);
        inputPanel.add(txtMaNV, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Tên NV:"), gbc);
        gbc.gridx = 1;
        txtTenNV = new JTextField(10);
        inputPanel.add(txtTenNV, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Chức vụ:"), gbc);
        gbc.gridx = 1;
        txtChucVu = new JTextField(10);
        inputPanel.add(txtChucVu, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("CCCD:"), gbc);
        gbc.gridx = 1;
        txtCCCD = new JTextField(10);
        inputPanel.add(txtCCCD, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 1;
        txtSDT = new JTextField(10);
        inputPanel.add(txtSDT, gbc);

        // Panel chứa các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);
        btnAdd = createStyledButton("Thêm");
        btnUpdate = createStyledButton("Sửa");
        btnDelete = createStyledButton("Xóa");
        btnFind = createStyledButton("Tìm Kiếm");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnFind);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        add(inputPanel, BorderLayout.SOUTH);

        // Thêm action listener
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tenNV = txtTenNV.getText().trim();
                String chucVu = txtChucVu.getText().trim();
                String cccd = txtCCCD.getText().trim();
                String sdt = txtSDT.getText().trim();

                if (cccd.isEmpty()) {
                    JOptionPane.showMessageDialog(JQuanLyNhanVien.this, "CCCD không được để trống!");
                    return;
                }
                if (tenNV.isEmpty() || chucVu.isEmpty() || sdt.isEmpty()) {
                    JOptionPane.showMessageDialog(JQuanLyNhanVien.this, "Vui lòng điền đầy đủ thông tin!");
                    return;
                }

                controller.addNhanVien(tenNV, chucVu, cccd, sdt);
                clearFields();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int maNV = Integer.parseInt(txtMaNV.getText());
                    String tenNV = txtTenNV.getText().trim();
                    String chucVu = txtChucVu.getText().trim();
                    String cccd = txtCCCD.getText().trim();
                    String sdt = txtSDT.getText().trim();

                    if (cccd.isEmpty()) {
                        JOptionPane.showMessageDialog(JQuanLyNhanVien.this, "CCCD không được để trống!");
                        return;
                    }
                    if (tenNV.isEmpty() || chucVu.isEmpty() || sdt.isEmpty()) {
                        JOptionPane.showMessageDialog(JQuanLyNhanVien.this, "Vui lòng điền đầy đủ thông tin!");
                        return;
                    }

                    controller.updateNhanVien(maNV, tenNV, chucVu, cccd, sdt);
                    clearFields();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(JQuanLyNhanVien.this, "Mã NV phải là số!");
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int maNV = Integer.parseInt(txtMaNV.getText());
                    controller.deleteNhanVien(maNV);
                    clearFields();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(JQuanLyNhanVien.this, "Mã NV phải là số!");
                }
            }
        });

        btnFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int maNV = Integer.parseInt(txtMaNV.getText());
                    NhanVien nv = controller.findNhanVien(maNV);
                    if (nv != null) {
                        txtTenNV.setText(nv.getTenNV());
                        txtChucVu.setText(nv.getChucVu());
                        txtCCCD.setText(nv.getCCCD());
                        txtSDT.setText(nv.getSDT());
                    } else {
                        JOptionPane.showMessageDialog(JQuanLyNhanVien.this, "Không tìm thấy nhân viên!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(JQuanLyNhanVien.this, "Mã NV phải là số!");
                }
            }
        });
        
        // Thêm ListSelectionListener để xử lý khi chọn một hàng trong bảng
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow();
                txtMaNV.setText(tableModel.getValueAt(selectedRow, 0).toString());
                txtTenNV.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtChucVu.setText(tableModel.getValueAt(selectedRow, 2).toString());
                txtCCCD.setText(tableModel.getValueAt(selectedRow, 3).toString());
                txtSDT.setText(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });
    }

    public void setController(QuanLyNhanVienController controller) {
        this.controller = controller;
    }

    public void updateTable(List<NhanVien> danhSach) {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        for (NhanVien nv : danhSach) {
            Object[] row = {nv.getMaNV(), nv.getTenNV(), nv.getChucVu(), nv.getCCCD(), nv.getSDT()};
            tableModel.addRow(row);
        }
    }

    private void clearFields() {
        txtMaNV.setText("");
        txtTenNV.setText("");
        txtChucVu.setText("");
        txtCCCD.setText("");
        txtSDT.setText("");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        GradientPaint gradient = new GradientPaint(0, 0, new Color(236, 239, 202), 0, getHeight(), new Color(10, 50, 90));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
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
        button.setPreferredSize(new Dimension(100, 40));
        button.setBorder(new EmptyBorder(10, 10, 10, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}