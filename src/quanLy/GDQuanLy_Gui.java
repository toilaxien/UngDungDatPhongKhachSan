package quanLy;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import GUI.DangNhap_GUI;
import GUI.RoundedBorder;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.text.SimpleDateFormat;
import java.util.*;

public class GDQuanLy_Gui {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JLabel ngaythangnam, giophutgiay;
    private Timer timerClock;
    private final String imageFolderPath = "img/HinhAnhGiaoDienChinh";
    private ArrayList<JButton> menuButtons = new ArrayList<>();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                GDQuanLy_Gui window = new GDQuanLy_Gui();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GDQuanLy_Gui() {
        initialize();
        startClock();
    }

    private void startClock() {
        timerClock = new Timer(1000, new ActionListener() {
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar now = Calendar.getInstance();
                ngaythangnam.setText(sdfDate.format(now.getTime()));
                giophutgiay.setText(sdfTime.format(now.getTime()));
            }
        });
        timerClock.start();
    }

    private void initialize() {
        final float BASE_WIDTH = 1536f;
        final float BASE_HEIGHT = 816f;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        Rectangle screenBounds = gc.getBounds();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

        int screenWidth = screenBounds.width - (screenInsets.left + screenInsets.right);
        int screenHeight = screenBounds.height - (screenInsets.top + screenInsets.bottom);

        int frameWidth = screenWidth;
        int frameHeight = screenHeight;

        frame = new JFrame();
        frame.setBackground(Color.WHITE);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("img/HinhAnhGiaoDienChinh/logo.png"));
        frame.setBounds(100, 100, frameWidth, frameHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel Header = new JPanel();
        Header.setBounds(0, 0, frameWidth, (int) (frameHeight * 0.12));
        Header.setBackground(Color.WHITE);
        Header.setLayout(null);
        Header.setBorder(new LineBorder(Color.BLACK));
        frame.getContentPane().add(Header);

        JLabel lblLoGo = new JLabel();
        ImageIcon originalIcon = new ImageIcon("img/HinhAnhGiaoDienChinh/logo.png");
        Image image = originalIcon.getImage().getScaledInstance(88, 88, Image.SCALE_SMOOTH);
        lblLoGo.setIcon(new ImageIcon(image));
        lblLoGo.setBounds(5, 5, 88, 88);
        Header.add(lblLoGo);

        JButton help = new JButton();
        help.setIcon(new ImageIcon("img/HinhAnhGiaoDienChinh/help.png"));
        help.setBounds(1342, 20, 50, 50);
        help.setContentAreaFilled(false);
        help.setBorderPainted(false);
        Header.add(help);

        class Scaler {
            float base;
            int actual;
            Scaler(float base, int actual) {
                this.base = base;
                this.actual = actual;
            }
            int apply(float value) {
                return Math.round(value * actual / base);
            }
        }
        Scaler w = new Scaler(BASE_WIDTH, screenWidth);
        Scaler h = new Scaler(BASE_HEIGHT, screenHeight);

        JLabel lblNewLabel_1 = new JLabel();
        ImageIcon user = new ImageIcon("img/HinhAnhGiaoDienChinh/AnhTraPhong/anhdaidien.jpg");
        Image resizedImage = user.getImage().getScaledInstance(42, 42, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        lblNewLabel_1.setIcon(resizedIcon);
        lblNewLabel_1.setBounds(w.apply(1466), h.apply(29), w.apply(45), h.apply(45));
        Header.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Quản lý");
        lblNewLabel_2.setBounds(w.apply(1466), h.apply(74), w.apply(45), h.apply(13));
        Header.add(lblNewLabel_2);

        JPanel Body = new JPanel();
        Body.setBounds(0, 96, frameWidth, (int) (frameHeight * 0.86));
        Body.setBackground(new Color(226, 219, 219));
        Body.setLayout(null);
        frame.getContentPane().add(Body);

        JPanel Menupanel = new CustomRoundedPanel(15, 15, 15, 15);
        Menupanel.setBackground(Color.WHITE);
        Menupanel.setBounds(5, 5, (int) (frameWidth * 0.216), (int) (frameHeight * 0.74));
        Menupanel.setBorder(new RoundedBorder(20));
        Menupanel.setLayout(null);
        Body.add(Menupanel);

        addMenuButton(Menupanel, "Quản lý nhân viên", 10, e -> cardLayout.show(mainPanel, "QLNV"));
        addMenuButton(Menupanel, "Quản lý tài khoản", 70, e -> cardLayout.show(mainPanel, "QLTK"));
        addMenuButton(Menupanel, "Quản lý dịch vụ", 130, e -> cardLayout.show(mainPanel, "QLDV"));
        addMenuButton(Menupanel, "Quản lý phòng", 190, e -> cardLayout.show(mainPanel, "QLP"));
        addMenuButton(Menupanel, "Quản lý khuyến mãi", 250, e -> cardLayout.show(mainPanel, "QLKM"));
        addMenuButton(Menupanel, "Thống kê doanh thu", 310, e -> cardLayout.show(mainPanel, "TKDT"));

        JPanel DongHoPannel = new JPanel();
        DongHoPannel.setBackground(Color.WHITE);
        DongHoPannel.setBounds(10, 483, 305, 99);
        DongHoPannel.setLayout(null);
        Menupanel.add(DongHoPannel);

        JLabel Calendar = new JLabel();
        Calendar.setIcon(new ImageIcon("img/HinhAnhGiaoDienChinh/lich.png"));
        Calendar.setBounds(28, 10, 45, 34);
        DongHoPannel.add(Calendar);

        JLabel DongHo = new JLabel();
        DongHo.setIcon(new ImageIcon("img/HinhAnhGiaoDienChinh/o'clock.png"));
        DongHo.setBounds(30, 54, 45, 35);
        DongHoPannel.add(DongHo);

        ngaythangnam = new JLabel("00/00/0000");
        ngaythangnam.setFont(new Font("Times New Roman", Font.BOLD, 28));
        ngaythangnam.setBounds(94, 10, 173, 34);
        DongHoPannel.add(ngaythangnam);

        giophutgiay = new JLabel("00:00:00");
        giophutgiay.setFont(new Font("Times New Roman", Font.BOLD, 28));
        giophutgiay.setBounds(94, 54, 183, 35);
        DongHoPannel.add(giophutgiay);

        JPanel DangXuatpanel = new CustomRoundedPanel(15, 15, 15, 15);
        DangXuatpanel.setBackground(Color.WHITE);
        DangXuatpanel.setBounds(5, 610, (int) (frameWidth * 0.215), (int) (frameHeight * 0.1));
        DangXuatpanel.setBorder(new RoundedBorder(20));
        DangXuatpanel.setLayout(null);
        Body.add(DangXuatpanel);

        JButton DangXuat = new JButton("Đăng Xuất");
        DangXuat.setBounds(30, 10, 285, 59);
        DangXuat.setIcon(new ImageIcon("img/HinhAnhGiaoDienChinh/exit.png"));
        DangXuat.setHorizontalAlignment(SwingConstants.LEFT);
        DangXuat.setFont(new Font("Times New Roman", Font.BOLD, 28));
        DangXuat.setBackground(Color.WHITE);
        DangXuat.setBorderPainted(false);
        DangXuat.setOpaque(false);
        DangXuatpanel.add(DangXuat);
        DangXuat.addActionListener(e -> {
            frame.dispose();
            new DangNhap_GUI();
        });

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBounds(350, 5, (int) (frameWidth * 0.77), (int) (frameHeight * 0.841));
        mainPanel.setBorder(new RoundedBorder(20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(cardLayout);
        Body.add(mainPanel);

        QuanLyTaiKhoan_Panel taiKhoanPanel = new QuanLyTaiKhoan_Panel();
        QuanLyNhanVien_Panel nhanVienPanel = new QuanLyNhanVien_Panel(taiKhoanPanel);

        mainPanel.add(nhanVienPanel, "QLNV");
        mainPanel.add(taiKhoanPanel, "QLTK");
        mainPanel.add(new QuanLyDichVu_Panel(), "QLDV");
        mainPanel.add(new QuanLyPhong_Panel(), "QLP");
        mainPanel.add(new QuanLyKhuyenMai_Panel(), "QLKM");
        mainPanel.add(new ThongKeDoanhThuGUI(), "TKDT");

        if (!menuButtons.isEmpty()) {
            menuButtons.get(0).doClick();
        }
    }

    private void addMenuButton(JPanel panel, String text, int y, ActionListener action) {
        JButton button = new JButton(text);
        button.setBounds(20, y, 285, 50);
        button.setFont(new Font("Times New Roman", Font.BOLD, 24));
        button.setBackground(Color.WHITE);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorderPainted(false);

        button.addActionListener(e -> {
            for (JButton btn : menuButtons) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(Color.BLACK);
            }
            button.setBackground(new Color(91, 249, 33));
            button.setForeground(Color.BLACK);
            action.actionPerformed(e);
        });

        panel.add(button);
        menuButtons.add(button);
    }

    class CustomRoundedPanel extends JPanel {
        private int topLeft, topRight, bottomLeft, bottomRight;

        public CustomRoundedPanel(int topLeft, int topRight, int bottomLeft, int bottomRight) {
            this.topLeft = topLeft;
            this.topRight = topRight;
            this.bottomLeft = bottomLeft;
            this.bottomRight = bottomRight;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            g2.setColor(getBackground());

            GeneralPath path = new GeneralPath();
            path.moveTo(topLeft, 0);
            path.lineTo(width - topRight, 0);
            if (topRight > 0)
                path.quadTo(width, 0, width, topRight);
            path.lineTo(width, height - bottomRight);
            if (bottomRight > 0)
                path.quadTo(width, height, width - bottomRight, height);
            path.lineTo(bottomLeft, height);
            if (bottomLeft > 0)
                path.quadTo(0, height, 0, height - bottomLeft);
            path.lineTo(0, topLeft);
            if (topLeft > 0)
                path.quadTo(0, 0, topLeft, 0);
            path.closePath();
            g2.fill(path);
        }
    }

    public void showWindow() {
        frame.setVisible(true);
    }
}