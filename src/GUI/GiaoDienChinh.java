package GUI;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

import motSoCNLT.DatDichVu;
import motSoCNLT.QuanLyKhachHang;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.awt.Toolkit;

public class GiaoDienChinh extends JFrame{

    private JPanel hinhKhachSanpannel;
    private JLabel HinhAnhNen;
    private JButton btnPrev, btnNext;
    private Timer timerSlideShow;
    private ArrayList<String> imagePaths;
    private int currentIndex = 0;
    private final String imageFolderPath = "img/HinhAnhGiaoDienChinh/HinhNen";
    private JPopupMenu quanLyCaPopupMenu;
    private Color defaultMenuItemBackground;
    private Color hoverBackgroundColor = new Color(91, 249, 33); // Màu xanh lá
    private Color hoverForegroundColor = Color.black;
    private JLabel ngaythangnam;
    private JLabel giophutgiay;
    private Timer timerClock; // Timer cho đồng hồ
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                GiaoDienChinh window = new GiaoDienChinh();
                window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the application.
     */
    public GiaoDienChinh() {
        initialize();
        startClock(); // Khởi động đồng hồ khi ứng dụng chạy
    }

    /**
     * Khởi động đồng hồ thời gian thực.
     */
    private void startClock() {
        timerClock = new Timer(1000, new ActionListener() { // Cập nhật mỗi 1 giây
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

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        // Tỷ lệ thiết kế gốc
        final float BASE_WIDTH = 1536f;
        final float BASE_HEIGHT = 816f;

        // Lấy kích thước màn hình thực tế
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        Rectangle screenBounds = gc.getBounds();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        int screenWidth = screenBounds.width - (screenInsets.left + screenInsets.right);
        int screenHeight = screenBounds.height - (screenInsets.top + screenInsets.bottom);

        // Scale helper
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

        setIconImage(Toolkit.getDefaultToolkit().getImage("img/HinhAnhGiaoDienChinh/logo.png"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setLayout(null);

        JPanel Header = new JPanel();
        Header.setBounds(0, 0, screenWidth, h.apply(100));
        Header.setBackground(Color.WHITE);
        Header.setLayout(null);
        Header.setBorder(new LineBorder(Color.BLACK));
        getContentPane().add(Header);

        JLabel lblLoGo = new JLabel();
        ImageIcon originalIcon = new ImageIcon("img/HinhAnhGiaoDienChinh/logo.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(w.apply(88), h.apply(88), Image.SCALE_SMOOTH);
        lblLoGo.setIcon(new ImageIcon(scaledImage));
        lblLoGo.setBounds(w.apply(5), h.apply(5), w.apply(88), h.apply(88));
        Header.add(lblLoGo);

        JButton help = new JButton();
        help.setIcon(new ImageIcon("img/HinhAnhGiaoDienChinh/help.png"));
        help.setBounds(w.apply(1342), h.apply(20), w.apply(50), h.apply(50));
        help.setContentAreaFilled(false);
        help.setBorderPainted(false);
        Header.add(help);
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ví dụ: hiển thị hộp thoại trợ giúp
            	File htmlFile = new File("HTML/HDSD.html");
                try {
					Desktop.getDesktop().browse(htmlFile.toURI());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });


        JLabel lblNewLabel_1 = new JLabel();
     // Tạo ImageIcon từ file ảnh gốc
        ImageIcon user = new ImageIcon("img/HinhAnhGiaoDienChinh/AnhTraPhong/anhdaidien.jpg");

        // Lấy đối tượng Image và resize nó
        Image resizedImage = user.getImage().getScaledInstance(42, 42, Image.SCALE_SMOOTH);

        // Tạo lại ImageIcon từ ảnh đã resize
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        // Gán icon vào label
        lblNewLabel_1.setIcon(resizedIcon);

        lblNewLabel_1.setBounds(w.apply(1466), h.apply(29), w.apply(45), h.apply(45));
        Header.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Lễ tân");
        lblNewLabel_2.setBounds(w.apply(1466), h.apply(74), w.apply(45), h.apply(13));
        Header.add(lblNewLabel_2);

        JPanel Body = new JPanel();
        Body.setBounds(0, Header.getHeight(), screenWidth, screenHeight - Header.getHeight());
        Body.setBackground(new Color(226, 219, 219));
        Body.setLayout(null);
        getContentPane().add(Body);

        JPanel Menupanel = new CustomRoundedPanel(15, 15, 15, 15);
        Menupanel.setBackground(Color.WHITE);
        Menupanel.setBounds(w.apply(5), h.apply(5), w.apply(330), h.apply(600));
        Menupanel.setBorder(new RoundedBorder(20));
        Menupanel.setLayout(null);
        Body.add(Menupanel);

        JMenuItem QuanLyDatPhong = createMenuItem("Quản lý đặt phòng", w.apply(20), h.apply(10), w.apply(285), h.apply(60));
        QuanLyDatPhong.addActionListener(e -> {
        	this.dispose();
        	QuanLyDatPhong_GUI frame = new QuanLyDatPhong_GUI();
			frame.setVisible(true);
        }
        );
        Menupanel.add(QuanLyDatPhong);

        JMenuItem QuanLyKhachHang = createMenuItem("Quản lý khách hàng", w.apply(20), h.apply(70), w.apply(285), h.apply(60));
        QuanLyKhachHang.addActionListener(e ->{
        	QuanLyKhachHang dialog = new QuanLyKhachHang(this, true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
        });
        Menupanel.add(QuanLyKhachHang);

        JMenuItem QuanLyDatDichVu = createMenuItem("Quản lý đặt dịch vụ", w.apply(20), h.apply(140), w.apply(285), h.apply(60));
        QuanLyDatDichVu.addActionListener(e -> {
			DatDichVu dialog = new DatDichVu(this, true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
        });
        Menupanel.add(QuanLyDatDichVu);

        
//        JLabel lblQuanLyCa = createMenuLabel("Quản lý ca", w.apply(30), h.apply(210), w.apply(248), h.apply(60));
//        Menupanel.add(lblQuanLyCa);

        JPanel DongHoPannel = new JPanel();
        DongHoPannel.setBackground(Color.WHITE);
        DongHoPannel.setBounds(w.apply(10), h.apply(483), w.apply(305), h.apply(99));
        DongHoPannel.setLayout(null);
        Menupanel.add(DongHoPannel);

        JLabel Calendar = new JLabel();
        Calendar.setIcon(new ImageIcon("img/HinhAnhGiaoDienChinh/lich.png"));
        Calendar.setBounds(w.apply(28), h.apply(10), w.apply(45), h.apply(34));
        DongHoPannel.add(Calendar);

        JLabel DongHo = new JLabel();
        DongHo.setIcon(new ImageIcon("img/HinhAnhGiaoDienChinh/o'clock.png"));
        DongHo.setBounds(w.apply(30), h.apply(54), w.apply(45), h.apply(35));
        DongHoPannel.add(DongHo);

        ngaythangnam = new JLabel("20/05/2004");
        ngaythangnam.setFont(new Font("Times New Roman", Font.BOLD, h.apply(28)));
        ngaythangnam.setBounds(w.apply(94), h.apply(10), w.apply(173), h.apply(34));
        DongHoPannel.add(ngaythangnam);

        giophutgiay = new JLabel("10:21:13");
        giophutgiay.setFont(new Font("Times New Roman", Font.BOLD, h.apply(28)));
        giophutgiay.setBounds(w.apply(94), h.apply(54), w.apply(183), h.apply(35));
        DongHoPannel.add(giophutgiay);

        JPanel hinhKhachSanpannel = new CustomRoundedPanel(15, 15, 15, 15);
        hinhKhachSanpannel.setBackground(Color.WHITE);
        hinhKhachSanpannel.setBounds(w.apply(350), h.apply(5), w.apply(1170), h.apply(685));
        hinhKhachSanpannel.setBorder(new RoundedBorder(20));
        hinhKhachSanpannel.setLayout(null);
        Body.add(hinhKhachSanpannel);

        HinhAnhNen = new JLabel();
        HinhAnhNen.setBounds(w.apply(5), h.apply(5), w.apply(1160), h.apply(675));
        hinhKhachSanpannel.add(HinhAnhNen);
        loadImagesFromFolder();

        btnPrev = new JButton();
        btnPrev.setIcon(new ImageIcon("img/HinhAnhGiaoDienChinh/prev.png"));
        btnPrev.setBounds(w.apply(10), h.apply(309), w.apply(50), h.apply(73));
        btnPrev.setContentAreaFilled(false);
        btnPrev.setBorderPainted(false);
        btnPrev.setOpaque(false);
        hinhKhachSanpannel.add(btnPrev);
        hinhKhachSanpannel.setComponentZOrder(btnPrev, 0);

        btnNext = new JButton();
        btnNext.setIcon(new ImageIcon("img/HinhAnhGiaoDienChinh/next .png"));
        btnNext.setBounds(w.apply(1123), h.apply(309), w.apply(50), h.apply(73));
        btnNext.setContentAreaFilled(false);
        btnNext.setBorderPainted(false);
        btnNext.setOpaque(false);
        hinhKhachSanpannel.add(btnNext);
        hinhKhachSanpannel.setComponentZOrder(btnNext, 0);

        btnPrev.setVisible(false);
        btnNext.setVisible(false);

        btnPrev.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnPrev.setVisible(true);
            }
            public void mouseExited(MouseEvent e) {
                btnPrev.setVisible(false);
            }
        });

        btnNext.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnNext.setVisible(true);
            }
            public void mouseExited(MouseEvent e) {
                btnNext.setVisible(false);
            }
        });

        hinhKhachSanpannel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                boolean isOverPrev = btnPrev.getBounds().contains(e.getPoint());
                boolean isOverNext = btnNext.getBounds().contains(e.getPoint());
                btnPrev.setVisible(isOverPrev);
                btnNext.setVisible(isOverNext);
            }
        });

        JPanel DangXuatpanel = new CustomRoundedPanel(15, 15, 15, 15);
        DangXuatpanel.setBackground(Color.WHITE);
        DangXuatpanel.setBounds(w.apply(5), h.apply(610), w.apply(330), h.apply(80));
        DangXuatpanel.setBorder(new RoundedBorder(20));
        DangXuatpanel.setLayout(null);
        Body.add(DangXuatpanel);

        JButton DangXuat = new JButton("Đăng Xuất");
        DangXuat.setBounds(w.apply(30), h.apply(10), w.apply(285), h.apply(59));
        DangXuat.setIcon(new ImageIcon("img/HinhAnhGiaoDienChinh/exit.png"));
        DangXuat.setHorizontalAlignment(SwingConstants.LEFT);
        DangXuat.setFont(new Font("Times New Roman", Font.BOLD, h.apply(28)));
        DangXuat.setBackground(Color.WHITE);
        DangXuat.setBorderPainted(false);
        DangXuat.setOpaque(false);
        DangXuatpanel.add(DangXuat);
        DangXuat.addActionListener(e -> {
        	this.dispose();
        	new DangNhap_GUI();
        });

        btnPrev.addActionListener(e -> showPreviousImage());
        btnNext.addActionListener(e -> showNextImage());

        timerSlideShow = new Timer(2000, e -> showNextImage());
        timerSlideShow.start();
    }


    // Hàm tạo JMenuItem với thuộc tính và thêm MouseListener cho hiệu ứng hover
    private JMenuItem createMenuItem(String text, int x, int y, int width, int height) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setBounds(x, y, width, height);
        menuItem.setBackground(new Color(255, 255, 255));
        menuItem.setFont(new Font("Times New Roman", Font.BOLD, 28));

        menuItem.addMouseListener(new MouseAdapter() {
            private Color defaultBackground;
            private Color defaultForeground;

            @Override
            public void mouseEntered(MouseEvent e) {
                defaultBackground = menuItem.getBackground();
                defaultForeground = menuItem.getForeground();
                menuItem.setBackground(hoverBackgroundColor);
                menuItem.setForeground(hoverForegroundColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuItem.setBackground(defaultBackground);
                menuItem.setForeground(defaultForeground);
            }
        });
        return menuItem;
    }

    // Hàm tạo JLabel cho mục menu (ví dụ: "Quản lý ca") với hiệu ứng hover
    private JLabel createMenuLabel(String text, int x, int y, int width, int height) {
        JLabel QuanLyCaLable = new JLabel(text);
        QuanLyCaLable.setBounds(28, 211, width, height);
        QuanLyCaLable.setFont(new Font("Times New Roman", Font.BOLD, 28));
        QuanLyCaLable.setBackground(new Color(255, 255, 255));
        QuanLyCaLable.setForeground(Color.BLACK);
        QuanLyCaLable.setOpaque(true);

        QuanLyCaLable.addMouseListener(new MouseAdapter() {
            private Color defaultBackground;
            private Color defaultForeground;

            @Override
            public void mouseEntered(MouseEvent e) {
                defaultBackground = QuanLyCaLable.getBackground();
                defaultForeground = QuanLyCaLable.getForeground();
                QuanLyCaLable.setBackground(hoverBackgroundColor);
                QuanLyCaLable.setForeground(hoverForegroundColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                QuanLyCaLable.setBackground(defaultBackground);
                QuanLyCaLable.setForeground(defaultForeground);
            }
        });
        return QuanLyCaLable;
    }

    // Hàm tạo JMenuItem cho popup menu
    private JMenuItem createPopupMenuItem(String text) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setFont(new Font("Times New Roman", Font.BOLD, 25));
        menuItem.setBackground(new Color(255, 255, 255));
        menuItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Đã chọn: " + text));
        return menuItem;
    }

    private void loadImagesFromFolder() {
        imagePaths = new ArrayList<>();
        File folder = new File(imageFolderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().matches(".*\\.(png|jpg|jpeg|gif)$"));
            if (files != null) {
                for (File file : files) {
                    imagePaths.add(file.getAbsolutePath());
                }
            }
        }
        if (imagePaths.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hình ảnh trong thư mục!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateImage() {
        if (!imagePaths.isEmpty()) {
            HinhAnhNen.setIcon(new ImageIcon(imagePaths.get(currentIndex)));
        }
    }

    private void showPreviousImage() {
        currentIndex = (currentIndex - 1 + imagePaths.size()) % imagePaths.size();
        updateImage();
    }

    private void showNextImage() {
        currentIndex = (currentIndex + 1) % imagePaths.size();
        updateImage();
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

            // Tạo một GeneralPath để vẽ hình dạng tùy chỉnh
            GeneralPath path = new GeneralPath();

            // Bắt đầu từ góc trên trái
            path.moveTo(topLeft, 0);
            
            // Đường đến góc trên phải
            path.lineTo(width - topRight, 0);
            if (topRight > 0) {
                path.quadTo(width, 0, width, topRight);
            }
            
            // Đường đến góc dưới phải
            path.lineTo(width, height - bottomRight);
            if (bottomRight > 0) {
                path.quadTo(width, height, width - bottomRight, height);
            }
            
            // Đường đến góc dưới trái
            path.lineTo(bottomLeft, height);
            if (bottomLeft > 0) {
                path.quadTo(0, height, 0, height - bottomLeft);
            }
            
            // Đường trở lại góc trên trái
            path.lineTo(0, topLeft);
            if (topLeft > 0) {
                path.quadTo(0, 0, topLeft, 0);
            }

            path.closePath();
            g2.fill(path);
        }
        }
    public void showWindow() {
        setVisible(true);
    }


}