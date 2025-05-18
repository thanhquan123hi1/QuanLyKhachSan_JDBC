package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import Controller.User;
public class LoginForm extends JFrame {
    private AnimatedTextField txtEmail;
    private AnimatedTextField txtPassword;
    private static final Color GRADIENT_START = new Color(70, 130, 180); // Xanh da trời nhạt
    private static final Color GRADIENT_END = new Color(30, 144, 255);   // Xanh da trời đậm
    private List<Cloud> clouds;

    public LoginForm() {
        // Cài đặt JFrame
        setTitle("Quản Lý Khách Sạn");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
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
        contentPanel.setPreferredSize(new Dimension(400, 300));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Form đăng nhập
        JPanel formPanel = new JPanel(new GridLayout(5, 1, 0, 20));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Tiêu đề
        JLabel titleLabel = new JLabel("Quản Lý Khách Sạn");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(titleLabel);

        // Trường Email
        JPanel emailPanel = new JPanel(new BorderLayout(10, 0));
        emailPanel.setOpaque(false);
        JLabel emailIcon;
        try {
            emailIcon = new JLabel(new ImageIcon(new ImageIcon("email_icon.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            emailIcon = new JLabel("📧");
        }
        emailIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
        txtEmail = new AnimatedTextField("Username");
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEmail.setPreferredSize(new Dimension(300, 40));
        emailPanel.add(emailIcon, BorderLayout.WEST);
        emailPanel.add(txtEmail, BorderLayout.CENTER);
        formPanel.add(emailPanel);

        // Trường Password
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 0));
        passwordPanel.setOpaque(false);
        JLabel passwordIcon;
        try {
            passwordIcon = new JLabel(new ImageIcon(new ImageIcon("lock_icon.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            passwordIcon = new JLabel("🔒");
        }
        passwordIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
        txtPassword = new AnimatedTextField("Password", true);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(300, 40));
        passwordPanel.add(passwordIcon, BorderLayout.WEST);
        passwordPanel.add(txtPassword, BorderLayout.CENTER);
        formPanel.add(passwordPanel);

        // Nút Login
        JButton btnLogin = new JButton("LOGIN") {
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
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(300, 50));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            if (email.isEmpty() || password.isEmpty() || email.equals("Username") || password.equals("Password")) {
                JOptionPane.showMessageDialog(LoginForm.this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
            	btnLogin.addActionListener(s ->{
            		String username = txtEmail.getText();
            	    String passwords = txtPassword.getText();
            	    User u = new User();
            	    u.KiemTraDangNhap(username, password);
            	});
            }
        });
        formPanel.add(btnLogin);

        // Panel liên kết với hiệu ứng đẹp hơn
        JPanel linkPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        linkPanel.setOpaque(false);
        linkPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

        JLabel forgotLink = new JLabel("Forgot Username or Password?");
        Font linkFont = new Font("Segoe UI", Font.BOLD, 14);
        if (!Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()).contains("Segoe UI")) {
            linkFont = new Font("Arial", Font.BOLD, 14);
        }
        forgotLink.setFont(linkFont);
        forgotLink.setForeground(Color.WHITE);
        forgotLink.setHorizontalAlignment(SwingConstants.CENTER);
        forgotLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                forgotLink.setForeground(new Color(255, 215, 0)); // Vàng khi hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                forgotLink.setForeground(Color.WHITE); // Trắng khi không hover
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginForm.this, "Chức năng quên mật khẩu chưa được triển khai!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JLabel createLink = new JLabel("Create your Account");
        createLink.setFont(linkFont);
        createLink.setForeground(Color.WHITE);
        createLink.setHorizontalAlignment(SwingConstants.CENTER);
        createLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                createLink.setForeground(new Color(255, 215, 0)); // Vàng khi hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                createLink.setForeground(Color.WHITE); // Trắng khi không hover
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                RegistrationForm R = new   RegistrationForm ();
                R.setVisible(true);
            	
            }
        });

        linkPanel.add(forgotLink);
        linkPanel.add(createLink);
        formPanel.add(linkPanel);

        contentPanel.add(formPanel, BorderLayout.CENTER);
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
            return passwordField.getPassword();
        }

        @Override
        public void setFont(Font font) {
            super.setFont(font);
            if (textField != null) textField.setFont(font);
            if (passwordField != null) passwordField.setFont(font);
        }
    }

   
}