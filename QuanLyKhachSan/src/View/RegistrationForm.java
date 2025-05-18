package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controller.User;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class RegistrationForm extends JFrame {
    private AnimatedTextField txtUsername, txtEmail, txtPassword, txtConfirmPassword;
    private static final Color GRADIENT_START = new Color(70, 130, 180); // Xanh da trời nhạt
    private static final Color GRADIENT_END = new Color(30, 144, 255);   // Xanh da trời đậm
    private List<Cloud> clouds;
    private List<String> registeredUsers = new ArrayList<>(); // Giả lập lưu trữ
    private JLabel passwordStrengthLabel;

    public RegistrationForm() {
        // Cài đặt JFrame
        setTitle("Đăng Ký - Quản Lý Khách Sạn");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Khởi tạo danh sách mây
        clouds = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            clouds.add(new Cloud(
                    random.nextInt(getWidth()),
                    random.nextInt(getHeight()),
                    random.nextInt(50) + 30,
                    random.nextInt(30) + 20,
                    random.nextFloat() * 0.3f + 0.05f
            ));
        }

        // Panel chính với nền động (mây trôi)
        JPanel mainPanel = new JPanel() {
            private Image bufferImage;
            private Graphics2D bufferGraphics;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Tạo bộ đệm nếu chưa có
                if (bufferImage == null || bufferImage.getWidth(null) != getWidth() || bufferImage.getHeight(null) != getHeight()) {
                    bufferImage = createImage(getWidth(), getHeight());
                    bufferGraphics = (Graphics2D) bufferImage.getGraphics();
                    bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                }

                // Vẽ lên bộ đệm
                bufferGraphics.setPaint(new GradientPaint(0, 0, GRADIENT_START, getWidth(), getHeight(), GRADIENT_END));
                bufferGraphics.fillRect(0, 0, getWidth(), getHeight());

                // Vẽ mây
                for (Cloud cloud : clouds) {
                    cloud.draw(bufferGraphics);
                }

                // Vẽ bộ đệm lên màn hình
                g.drawImage(bufferImage, 0, 0, null);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Tạo hiệu ứng mây trôi mượt mà
        Timer cloudTimer = new Timer(30, e -> {
            for (Cloud cloud : clouds) {
                cloud.update(getWidth());
            }
            mainPanel.repaint();
        });
        cloudTimer.start();

        setContentPane(mainPanel);

        // Panel nội dung (trong suốt)
        JPanel contentPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
        };
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setPreferredSize(new Dimension(400, 500));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Form đăng ký
        JPanel formPanel = new JPanel(new GridLayout(7, 1, 0, 15));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Tiêu đề
        JLabel titleLabel = new JLabel("Đăng Ký Tài Khoản");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(titleLabel);

        // Trường Username
        JPanel usernamePanel = new JPanel(new BorderLayout(10, 0));
        usernamePanel.setOpaque(false);
        JLabel usernameIcon = new JLabel("👤");
        usernameIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
        txtUsername = new AnimatedTextField("Tên đăng nhập");
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsername.setPreferredSize(new Dimension(300, 40));
        usernamePanel.add(usernameIcon, BorderLayout.WEST);
        usernamePanel.add(txtUsername, BorderLayout.CENTER);
        formPanel.add(usernamePanel);

        // Trường Email
        JPanel emailPanel = new JPanel(new BorderLayout(10, 0));
        emailPanel.setOpaque(false);
        JLabel emailIcon = new JLabel("📧");
        emailIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
        txtEmail = new AnimatedTextField("Email");
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEmail.setPreferredSize(new Dimension(300, 40));
        emailPanel.add(emailIcon, BorderLayout.WEST);
        emailPanel.add(txtEmail, BorderLayout.CENTER);
        formPanel.add(emailPanel);

        // Trường Password
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 0));
        passwordPanel.setOpaque(false);
        JLabel passwordIcon = new JLabel("🔒");
        passwordIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
        txtPassword = new AnimatedTextField("Mật khẩu", true);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(300, 40));
        passwordPanel.add(passwordIcon, BorderLayout.WEST);
        passwordPanel.add(txtPassword, BorderLayout.CENTER);
        formPanel.add(passwordPanel);

        // Trường Confirm Password
        JPanel confirmPasswordPanel = new JPanel(new BorderLayout(10, 0));
        confirmPasswordPanel.setOpaque(false);
        JLabel confirmPasswordIcon = new JLabel("🔒");
        confirmPasswordIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
        txtConfirmPassword = new AnimatedTextField("Xác nhận mật khẩu", true);
        txtConfirmPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtConfirmPassword.setPreferredSize(new Dimension(300, 40));
        confirmPasswordPanel.add(confirmPasswordIcon, BorderLayout.WEST);
        confirmPasswordPanel.add(txtConfirmPassword, BorderLayout.CENTER);
        formPanel.add(confirmPasswordPanel);

        // Nhãn độ mạnh mật khẩu
        passwordStrengthLabel = new JLabel("Độ mạnh mật khẩu: Yếu", SwingConstants.CENTER);
        passwordStrengthLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordStrengthLabel.setForeground(Color.WHITE);
        formPanel.add(passwordStrengthLabel);

        // Nút Register
        JButton btnRegister = new JButton("ĐĂNG KÝ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(46, 204, 113), 0, getHeight(), new Color(39, 174, 96));
                if (getModel().isRollover()) {
                    g2d.setPaint(new GradientPaint(0, 0, new Color(52, 152, 219), 0, getHeight(), new Color(41, 128, 185)));
                } else {
                    g2d.setPaint(gradient);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
            }
        };
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setFocusPainted(false);
        btnRegister.setPreferredSize(new Dimension(300, 50));
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> {
        	 handleRegister();        	
        });
        formPanel.add(btnRegister);

        contentPanel.add(formPanel, BorderLayout.CENTER);

        // Kiểm tra độ mạnh mật khẩu
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkPasswordStrength();
            }
        });
    }

    // Lớp để quản lý mây
    private static class Cloud {
        private float x, y;
        private int width, height;
        private float speed;
        private float currentX;

        public Cloud(float x, float y, int width, int height, float speed) {
            this.x = x;
            this.currentX = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.speed = speed;
        }

        public void update(int panelWidth) {
            x += speed;
            currentX += (x - currentX) * 0.1f;
            if (x > panelWidth) {
                x = -width;
                currentX = x;
            }
        }

        public void draw(Graphics2D g2d) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            g2d.setColor(Color.WHITE);
            Ellipse2D cloudPart1 = new Ellipse2D.Float(currentX, y, width, height);
            Ellipse2D cloudPart2 = new Ellipse2D.Float(currentX + width / 2, y - height / 4, width * 0.8f, height * 0.8f);
            Ellipse2D cloudPart3 = new Ellipse2D.Float(currentX - width / 4, y + height / 4, width * 0.6f, height * 0.6f);
            g2d.fill(cloudPart1);
            g2d.fill(cloudPart2);
            g2d.fill(cloudPart3);
        }
    }

    // Lớp tùy chỉnh cho JTextField với hiệu ứng động và placeholder
    private static class AnimatedTextField extends JPanel {
        private JTextField textField;
        private JPasswordField passwordField;
        private boolean isPassword;
        private float glowPhase = 0;
        private boolean hasFocus = false;
        private String placeholder;

        public AnimatedTextField(String placeholder) {
            this(placeholder, false);
        }

        public AnimatedTextField(String placeholder, boolean isPassword) {
            this.placeholder = placeholder;
            this.isPassword = isPassword;
            setOpaque(false);
            setLayout(new BorderLayout());
            if (isPassword) {
                passwordField = new JPasswordField();
                passwordField.setOpaque(false);
                passwordField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                passwordField.setForeground(Color.GRAY);
                passwordField.setText(placeholder);
                passwordField.setEchoChar((char) 0); // Hiển thị placeholder ban đầu
                add(passwordField, BorderLayout.CENTER);
            } else {
                textField = new JTextField();
                textField.setOpaque(false);
                textField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                textField.setForeground(Color.GRAY);
                textField.setText(placeholder);
                add(textField, BorderLayout.CENTER);
            }

            // Hiệu ứng ánh sáng khi focus
            Timer glowTimer = new Timer(50, e -> {
                glowPhase += 0.1;
                repaint();
            });
            glowTimer.start();

            FocusAdapter focusAdapter = new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    hasFocus = true;
                    if (isPassword) {
                        if (new String(passwordField.getPassword()).equals(placeholder)) {
                            passwordField.setText("");
                            passwordField.setForeground(Color.BLACK);
                            passwordField.setEchoChar('•');
                        }
                    } else {
                        if (textField.getText().equals(placeholder)) {
                            textField.setText("");
                            textField.setForeground(Color.BLACK);
                        }
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {

                    hasFocus = false;
                    if (isPassword) {
                        if (new String(passwordField.getPassword()).isEmpty()) {
                            passwordField.setText(placeholder);
                            passwordField.setForeground(Color.GRAY);
                            passwordField.setEchoChar((char) 0);
                        }
                    } else {
                        if (textField.getText().isEmpty()) {
                            textField.setText(placeholder);
                            textField.setForeground(Color.GRAY);
                        }
                    }
                }
            };

            if (isPassword) {
                passwordField.addFocusListener(focusAdapter);
                passwordField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        passwordField.setForeground(Color.BLACK);
                        passwordField.setEchoChar('•');
                    }
                });
            } else {
                textField.addFocusListener(focusAdapter);
                textField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        textField.setForeground(Color.BLACK);
                    }
                });
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Vẽ nền trắng cho JTextField
            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

            // Vẽ viền
            g2d.setColor(new Color(200, 200, 200));
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

            // Hiệu ứng ánh sáng khi focus
            if (hasFocus) {
                g2d.setPaint(new GradientPaint(
                        (float) (getWidth() * (Math.sin(glowPhase) + 1) / 2), 0,
                        new Color(52, 152, 219, 150),
                        (float) (getWidth() * (Math.sin(glowPhase + 1) + 1) / 2), 0,
                        new Color(52, 152, 219, 0)));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        }

        public String getText() {
            String text = isPassword ? new String(passwordField.getPassword()) : textField.getText();
            return text.equals(placeholder) ? "" : text;
        }

        public char[] getPassword() {
            return isPassword ? passwordField.getPassword() : new char[0];
        }

        @Override
        public void setFont(Font font) {
            super.setFont(font);
            if (textField != null) textField.setFont(font);
            if (passwordField != null) passwordField.setFont(font);
        }

		public void setEchoChar(char c) {
			// TODO Auto-generated method stub
			
		}
    }

    private void checkPasswordStrength() {
        String password = new String(txtPassword.getPassword()).trim();
        System.out.println("Checking password: " + password); // Debug

        if (password.isEmpty() || password.equals("Mật khẩu")) {
            passwordStrengthLabel.setText("Độ mạnh mật khẩu: Yếu");
            passwordStrengthLabel.setForeground(new Color(239, 68, 68));
            System.out.println("Password strength: Yếu (empty or placeholder)");
            return;
        }

        int strength = 0;
        if (password.length() >= 8) {
            strength++;
            System.out.println("Length >= 8: +1");
        }
        if (Pattern.compile("[A-Z]").matcher(password).find()) {
            strength++;
            System.out.println("Has uppercase: +1");
        }
        if (Pattern.compile("[0-9]").matcher(password).find()) {
            strength++;
            System.out.println("Has digit: +1");
        }
        if (Pattern.compile("[!@#$%^&*]").matcher(password).find()) {
            strength++;
            System.out.println("Has special char: +1");
        }

        System.out.println("Total strength score: " + strength);

        switch (strength) {
            case 0:
            case 1:
                passwordStrengthLabel.setText("Độ mạnh mật khẩu: Yếu");
                passwordStrengthLabel.setForeground(new Color(239, 68, 68));
                break;
            case 2:
                passwordStrengthLabel.setText("Độ mạnh mật khẩu: Trung bình");
                passwordStrengthLabel.setForeground(new Color(245, 158, 11));
                break;
            case 3:
                passwordStrengthLabel.setText("Độ mạnh mật khẩu: Mạnh");
                passwordStrengthLabel.setForeground(new Color(34, 197, 94));
                break;
            case 4:
                passwordStrengthLabel.setText("Độ mạnh mật khẩu: Rất mạnh");
                passwordStrengthLabel.setForeground(new Color(34, 197, 94));
                break;
        }
    }

    private void handleRegister() {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();

        // Kiểm tra đầu vào
        if (username.isEmpty() || username.equals("Tên đăng nhập")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (email.isEmpty() || email.equals("Email")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập email!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.isEmpty() || password.equals("Mật khẩu")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (confirmPassword.isEmpty() || confirmPassword.equals("Xác nhận mật khẩu")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập xác nhận mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu và xác nhận mật khẩu không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra độ mạnh mật khẩu
        checkPasswordStrength(); // Cập nhật lại độ mạnh trước khi kiểm tra
        if (!passwordStrengthLabel.getText().contains("Mạnh") && !passwordStrengthLabel.getText().contains("Rất mạnh")) {
            JOptionPane.showMessageDialog(this, "Mật khẩu quá yếu, vui lòng chọn mật khẩu mạnh hơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra email trùng lặp
        if (registeredUsers.contains(email)) {
            JOptionPane.showMessageDialog(this, "Email đã được đăng ký!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        User U = new User();
    	U.createTK(username, email, password); 
    	

        // Reset form
        txtUsername.setToolTipText("Tên đăng nhập");
        txtUsername.setForeground(Color.GRAY);
        txtEmail.setToolTipText("Email");
        txtEmail.setForeground(Color.GRAY);
        txtPassword.setToolTipText("Mật khẩu");
        txtPassword.setForeground(Color.GRAY);
        txtPassword.setEchoChar((char) 0);
        txtConfirmPassword.setToolTipText("Xác nhận mật khẩu");
        txtConfirmPassword.setForeground(Color.GRAY);
        txtConfirmPassword.setEchoChar((char) 0);
        passwordStrengthLabel.setText("Độ mạnh mật khẩu: Yếu");
        passwordStrengthLabel.setForeground(new Color(239, 68, 68));
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

  
}