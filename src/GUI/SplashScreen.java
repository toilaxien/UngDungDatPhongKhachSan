package GUI;

import javax.swing.*;

import connectDB.ConnectDB;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SplashScreen {
    private JWindow window;
    private JProgressBar progressBar;
    private JLabel statusLabel;

    public SplashScreen() {
        createSplashScreen();
    }

    private void createSplashScreen() {
        // Tạo cửa sổ splash không thanh tiêu đề
        window = new JWindow();
        window.setSize(800, 500);
        window.setLocationRelativeTo(null);
        window.setShape(new RoundRectangle2D.Double(0, 0, 800, 500, 30, 30)); // Bo góc cửa sổ

        // Panel chính sẽ chứa ảnh nền
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Vẽ background mờ nếu không có ảnh
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(30, 30, 30));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        ImageIcon icon = new ImageIcon("img/HinhAnhGiaoDienChinh/nenDangNhap.jpg");
		Image scaledImage = icon.getImage().getScaledInstance(window.getWidth(), window.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(scaledImage);

		// Panel ảnh nền
		JLabel backgroundLabel = new JLabel(imageIcon);
		backgroundLabel.setLayout(new BorderLayout());
		panel.add(backgroundLabel, BorderLayout.CENTER);

		// Thêm lớp phủ mờ để chữ dễ đọc
		JPanel overlay = new JPanel();
		overlay.setLayout(new BoxLayout(overlay, BoxLayout.Y_AXIS));
		overlay.setOpaque(false);
		overlay.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

		// Tiêu đề
		JLabel titleLabel = new JLabel("Welcome to LUX", JLabel.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

		// Thông tin trạng thái
		statusLabel = new JLabel("Initializing application...", JLabel.CENTER);
		statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		statusLabel.setForeground(Color.WHITE);
		statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

		// Progress bar màu trắng
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(false);
		progressBar.setStringPainted(true);
		progressBar.setForeground(Color.WHITE);
		progressBar.setBackground(new Color(255, 255, 255, 50)); // Nền trong suốt
		progressBar.setBorder(BorderFactory.createEmptyBorder(5, 100, 5, 100));
		progressBar.setString("0%");
		progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Thêm các thành phần vào overlay
		overlay.add(Box.createVerticalGlue());
		overlay.add(titleLabel);
		overlay.add(statusLabel);
		overlay.add(progressBar);
		overlay.add(Box.createVerticalGlue());

		// Thêm overlay lên trên ảnh nền
		backgroundLabel.add(overlay, BorderLayout.CENTER);

        window.setContentPane(panel);
    }

    public void show() {
        window.setVisible(true);
        System.out.println("hiển thị thành công");
    }

    public void hide() {
        window.dispose();
    }

    public void setProgress(int value, String message) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(value);
            progressBar.setString(value + "%");
            statusLabel.setText(message);
        });
    }

    public void connectToDatabase() {
        new Thread(() -> {
            try {
                setProgress(10, "Loading configuration...");
                Thread.sleep(500);

                setProgress(25, "Connecting to database...");
                Connection conn = ConnectDB.getConnection();
                
                if (conn != null) {
                    setProgress(60, "Database connection established");
                    Thread.sleep(500);
                    
                    setProgress(80, "Loading application modules...");
                    Thread.sleep(800);
                    
                    setProgress(100, "Ready to start");
                    Thread.sleep(300);
                    
                    conn.close();
                } else {
                    setProgress(100, "Failed to connect to database");
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                setProgress(100, "Error: " + e.getMessage());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                setProgress(100, "Database error: " + e.getMessage());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } finally {
                hide();
                new DangNhap_GUI();
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.show();
            splash.connectToDatabase();
        });
    }
}