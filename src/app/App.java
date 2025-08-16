package app;

import javax.swing.*;
import GUI.SplashScreen;
import java.awt.*;
import java.io.InputStream;

public class App {

    public static void main(String[] args) {
        try {
            // Giao diện hệ điều hành
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Cấu hình font toàn hệ thống (nếu dùng font ngoài)
            caiFontMacDinh("C:\\Windows\\Fonts\\segoeui.ttf", 14f);

            // (Tùy chọn) chỉnh màu mặc định toàn bộ
            UIManager.put("Button.background", new Color(240, 240, 240));
            UIManager.put("Button.foreground", Color.BLACK);
            UIManager.put("Table.foreground", Color.BLACK);
            UIManager.put("Label.foreground", Color.BLACK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Hiển thị splash screen
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.show();
            splash.connectToDatabase();
        });
    }

    private static void caiFontMacDinh(String pathFont, float size) {
        try {
            InputStream is = App.class.getResourceAsStream(pathFont);
            if (is != null) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
                UIManager.put("Label.font", font);
                UIManager.put("Button.font", font);
                UIManager.put("TextField.font", font);
                UIManager.put("TextArea.font", font);
                UIManager.put("Table.font", font);
                UIManager.put("TableHeader.font", font);
                UIManager.put("ComboBox.font", font);
            } else {
                System.err.println("Không tìm thấy font: " + pathFont);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải font: " + e.getMessage());
        }
    }
}
