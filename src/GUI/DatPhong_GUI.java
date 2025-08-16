package GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;

import dao.ChiTietDonDatPhong_Dao;
import dao.ChiTietPhieuDichVu_DAO;
import dao.DonDatPhong_Dao;
import dao.KhachHang_Dao;
import dao.PhieuDichVu_DAO;
import dao.Phong_Dao;
import doiPhong.NhanPhongUtil;
import entity.ChiTietPhieuDichVu;
import entity.DichVu;
import entity.DonDatPhong;
import entity.KhachHang;
import entity.PhieuDichVu;

public class DatPhong_GUI extends JDialog {
	// Giao diện tổng và bố cục trung tâm
	private JPanel contentPane;
	private JPanel mainPanel;
	private JPanel centerPanel;
	private CardLayout cardLayout;
	
	//panel giao diện phụ
	private JPanel topInfoPanel;
	private JPanel bookingTypePanel;
	private JPanel checkInPanel;
	private JPanel checkOutPanel;
	private JPanel guestPanel;
	private JPanel timePanel;

	//thành phần chọn ngày và giờ và custombutton
	private CustomDateButton checkInButton;
	private CustomDateButton checkOutButton;

	//kích thước màn hình các trang
	private int screenWidthTrang1;
	private int screenHeightTrang1;
	
	//lớp entity
	private PhieuDichVu pdv;
	private ChiTietPhieuDichVu ctpdv;

	//lớp Dao
	private Phong_Dao phongdao;
	private KhachHang_Dao khachhangdao;
	private DonDatPhong_Dao dondatphongdao;
	private ChiTietDonDatPhong_Dao chitietdondatphongdao;
	private PhieuDichVu_DAO phieudichvudao;
	private ChiTietPhieuDichVu_DAO chitietphieudichvudao;

	//kiểu dữ liệu đặt phòng
	private String[] tienIchPhong = {"Ban công", "View biển", "Phòng hút thuốc"};
	private List<JCheckBox> checkBoxes = new ArrayList<>();
	private String moTaChinhXac;
	private int[] roomQuantities;
	private String[] danhSachSoPhongDuocChon;

	private String maNhanVien = "2025LT001";
	private String maDon;
	private String loaiDon = "Theo ngày";
	private String trangThai;
	private int soKhach;

	//	biến xử lý thời gian đặt phòng
	private Timestamp tuNgay;                     // Ngày giờ nhận phòng
	private Timestamp denNgay;                      // Ngày giờ trả phòng

	private Timestamp todayTimestamp = Timestamp.valueOf(LocalDate.now().atStartOfDay());
	private Timestamp tomorrowTimestamp = Timestamp.valueOf(LocalDate.now().plusDays(1).atStartOfDay());

	private LocalTime now;                          // Giờ hiện tại
	private LocalTime roundedHour;                  // Làm tròn giờ kế tiếp (dùng cho đặt theo giờ)
	
	//biến ngày và giờ hiện tại
	private Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
	private Timestamp hanChotCoc = Timestamp.valueOf(LocalDateTime.now().plusHours(3));

	//biến thông tin 
	private JLabel tongTienLabel;
	private JLabel[] totalLabels;
	
	private String kieuDat = "Trực tiếp";

	private long tongTien;
	
	private long tienCoc;
	
	private String moTa;

	JCheckBox cbBanCong = new JCheckBox(tienIchPhong[0]);
	JCheckBox cbHutThuoc = new JCheckBox(tienIchPhong[1]);
	JCheckBox cbViewBien = new JCheckBox(tienIchPhong[2]);
	
	ItemListener tienIchListener;
	JPanel footerPanel;
	
	// Biến lưu số lượng từng dịch vụ
	private int soLuongBuffet = 0;
	private int soLuongThueXeDay = 0;
	private int soLuongBaoMau = 0;
	private int soLuongNoiEmBe = 0;

	//==============TRANG CHÍNH GUI==============
	public DatPhong_GUI(JFrame parentFrame) {
		super(parentFrame, "Form Đặt Phòng", true); // true = modal dialog

		// Giao diện không viền nếu muốn tùy biến
		setUndecorated(true);

		// Lấy kích thước màn hình
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidthTrang1 = (int) (screenSize.width * 0.65);
		screenHeightTrang1 = (int) (screenSize.height * 0.65);

		// Đặt kích thước dialog
		setSize(screenWidthTrang1, screenHeightTrang1);
		setLocationRelativeTo(parentFrame); // Canh giữa theo cửa sổ cha

		// Khởi tạo CardLayout
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		JPanel trang1 = null;
		// Thêm các trang
		try {
			trang1 =taoTrangDatPhong();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mainPanel.add(trang1, "Trang1");
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		cardLayout.show(mainPanel, "Trang1");

		add(mainPanel);
	}
	
	//==============TAO TRANG ĐẶT PHÒNG(TRANG 1)==============
	private JPanel taoTrangDatPhong() throws SQLException {
		// tạo biến cho chi tiet don dat phòng
		chitietdondatphongdao = new ChiTietDonDatPhong_Dao();
		khachhangdao = new KhachHang_Dao();
		phongdao = new Phong_Dao();
		dondatphongdao = new DonDatPhong_Dao();
		phieudichvudao = new PhieuDichVu_DAO();
		chitietphieudichvudao = new ChiTietPhieuDichVu_DAO();
		// Tính toán kích thước các phần
		int headerHeight = (int) (screenHeightTrang1 * 0.3);
		int centerHeight = (int) (screenHeightTrang1 * 0.6);
		int footerHeight = (int) (screenHeightTrang1 * 0.1);
		// Tạo nội dung form (panel con)
		contentPane = new JPanel(new BorderLayout());
		contentPane.setBackground(Color.WHITE); // Màu nền cho contentPane
		contentPane.setBorder(new LineBorder(Color.WHITE, 2));

		// ====== Panel chọn phòng ======
		JPanel chonPhongPanel = new JPanel(new BorderLayout());
		chonPhongPanel.setPreferredSize(new Dimension(800, 300));
		chonPhongPanel.setBackground(Color.RED);


		// =========================================== Header==============================================
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(Color.RED); // Màu xám nhạt
		headerPanel.setPreferredSize(new Dimension(screenWidthTrang1, headerHeight));

		// ============== Panel chứa tiêu đề và nút đóng=================phần 1 của header===================
		// Panel phụ bên phải để chứa cả hai nút
		//panel chứa
		JPanel rightButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		rightButtonsPanel.setBackground(Color.WHITE);
        int rightButtonsPanelWidth = headerHeight;
        int rightButtonsPanelHeight = headerHeight;
		
		JPanel titleClosePanel = new JPanel(new BorderLayout());
		titleClosePanel.setBackground(Color.WHITE);
		titleClosePanel.setPreferredSize(new Dimension(screenWidthTrang1, (int) (headerHeight * 0.22)));

		// Tiêu đề "Chọn phòng"
		JLabel titleLabel = new JLabel("Chọn phòng");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Nút đóng
		JButton closeButton = new JButton("X");
		closeButton.setFont(new Font("Arial", Font.BOLD, 20));
		closeButton.setForeground(Color.BLACK);
		closeButton.setBackground(Color.WHITE);
		closeButton.setFocusPainted(false);
		closeButton.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
		closeButton.addActionListener(e -> dispose());

		// Tạo popup menu
		JPopupMenu popupPanel = new JPopupMenu();
		// Danh sách nội dung của từng ô (50 ô)
		String[] cellContents = {
		    "Tiện ích", "Single", "Twin Room", "Twin Room", "Triple Room",
		    "Giường ngủ", "1 giường đơn", "1 giường đôi", "2 giường đơn", "<html><br>1 giường đôi +<br> 1 giường đơn</html>",
		    "Wifi miễn phí", "✔️", "✔️", "✔️", "✔️",
		    "TV màn hình phẳng", "✔️", "✔️", "✔️", "✔️",
		    "Tủ lạnh mini", "✔️", "✔️", "✔️", "✔️",
		    "Tủ lạnh", "✔️", "", "", "",
		    "Két an toàn", "", "✔️", "✔️", "✔️",
		    "Bồn tắm", "", "", "✔️", "✔️",
		    "Máy sấy tóc", "", "✔️", "✔️", "✔️",
		    "Bàn làm việc", "✔️", "✔️", "✔️", "✔️",
		    "Bình siêu tốc", "", "✔️", "✔️", "✔️",
		    "Ghế sofa", "", "", "✔️", "✔️",
		    "Tủ", "✔️", "✔️", "✔️", "✔️",
		    "Gương", "✔️", "✔️", "✔️", "✔️",
		    "Thảm", "✔️", "✔️", "✔️", "✔️",
		    "Minibar", "", "", "", "✔️",
		    "Dụng cụ bếp", "", "✔️", "✔️", "✔️",
		};
		int numCols = 5;
		int numRows = 17;
		int totalCells = numCols * numRows;

		JPanel gridPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 0;

		for (int i = 0; i < totalCells; i++) {
		    String text = i < cellContents.length ? cellContents[i] : "";
		    JLabel cell = new JLabel("<html>" + text + "</html>", SwingConstants.CENTER);
		    cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		    cell.setHorizontalAlignment(SwingConstants.CENTER);
		    cell.setVerticalAlignment(SwingConstants.CENTER);

		    gbc.gridx = i % numCols;
		    gbc.gridy = i / numCols;
		    int width;
		    int height;

		    if (gbc.gridx >= 1 && gbc.gridx <= 3) {
		        width = 60; // Cột 2, 3, 4
		    } else {
		        width = 120; // Cột 1 và 5
		    }

		    // === Xác định chiều cao theo hàng ===
		    if (gbc.gridy == 1) {
		        height = 60; // Hàng thứ 2
		    } else {
		        height = 30; // Các hàng khác
		    }
	
		    // ✅ Đây chính là dòng anh/chị hỏi: thêm ô vào đúng vị trí
		    gridPanel.add(cell, gbc);
		}
		// Thêm bảng vào popup
		popupPanel.add(gridPanel);

		int btnWidth = (int) (rightButtonsPanelWidth * 0.12); // 0.1%
        int btnHeight = (int) (rightButtonsPanelHeight * 0.13);  // 80%
        System.out.println("fsfsdfsd:"+ btnWidth);
        ImageIcon icon = new ImageIcon("img/HinhAnhGiaoDienChinh/icons8-info-100.png");
		Image scaledImage = icon.getImage().getScaledInstance(btnWidth,btnHeight, Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(scaledImage);
		// Nút mới (nút cài đặt)
		JButton suggestButton = new JButton(imageIcon);
		suggestButton.setFont(new Font("Arial", Font.BOLD, 12)); // nhỏ hơn nút X
		suggestButton.setBackground(Color.WHITE);
		suggestButton.setFocusPainted(false);
		suggestButton.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
		suggestButton.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseEntered(MouseEvent e) {
		        popupPanel.show(suggestButton, 0, suggestButton.getHeight());
		    }

		    @Override
		    public void mouseExited(MouseEvent e) {
		        // Delay để cho phép chuột đi vào popupPanel
		        Timer timer = new Timer(200, new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent evt) {
		                PointerInfo pointerInfo = MouseInfo.getPointerInfo();
		                Point mouseLocation = pointerInfo.getLocation();
		                SwingUtilities.convertPointFromScreen(mouseLocation, popupPanel);

		                if (!popupPanel.getBounds().contains(mouseLocation)) {
		                    popupPanel.setVisible(false);
		                }
		            }
		        });
		        timer.setRepeats(false);
		        timer.start();
		    }
		});

		rightButtonsPanel.add(suggestButton);
		rightButtonsPanel.add(closeButton);

		// THÊM tiêu đề vào bên trái trước khi thêm nút bên phải
		titleClosePanel.add(titleLabel, BorderLayout.WEST);
		titleClosePanel.add(rightButtonsPanel, BorderLayout.EAST);

		// tạo nút chưa 3 nút==================================phần 2 của header==============================
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setPreferredSize(new Dimension((int) (screenWidthTrang1* 0.2), (int) (headerHeight * 0.78) ));

		// ========== Tạo 3 nút với viền======
		// Tạo kích thước chuẩn cho cả 3 nút
		Font fontButton = new Font("Times New Roman", Font.BOLD, 16);
		Dimension buttonSize = new Dimension(140, 40); // Bạn nên định nghĩa rõ nếu chưa có

		// Nút theo ngày
		JButton theoNgayButton = new JButton("Theo ngày");
		theoNgayButton.setFont(fontButton);
		theoNgayButton.setBackground(new Color(0, 255, 128));// Xanh lá đậm thay cho Color.GREEN
		theoNgayButton.setForeground(Color.BLACK);
		theoNgayButton.setFocusPainted(false);
		theoNgayButton.setOpaque(true);
		theoNgayButton.setContentAreaFilled(true);
		theoNgayButton.setBorderPainted(false);
		theoNgayButton.setBorder(new CompoundBorder(
		    new LineBorder(Color.BLACK, 2),
		    new EmptyBorder(5, 15, 5, 15)
		));
		theoNgayButton.setPreferredSize(buttonSize);
		theoNgayButton.setMaximumSize(buttonSize);
		theoNgayButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Nút theo giờ
		JButton theoGioButton = new JButton("Theo giờ");
		theoGioButton.setFont(fontButton);
		theoGioButton.setBackground(Color.WHITE);
		theoGioButton.setForeground(Color.BLACK);
		theoGioButton.setFocusPainted(false);
		theoGioButton.setOpaque(true);
		theoGioButton.setContentAreaFilled(true);
		theoGioButton.setBorderPainted(false);
		theoGioButton.setBorder(new CompoundBorder(
		    new LineBorder(Color.BLACK, 2),
		    new EmptyBorder(5, 15, 5, 15)
		));
		theoGioButton.setPreferredSize(buttonSize);
		theoGioButton.setMaximumSize(buttonSize);
		theoGioButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Nút theo đêm
		JButton theoDemButton = new JButton("Theo đêm");
		theoDemButton.setFont(fontButton);
		theoDemButton.setBackground(Color.WHITE);
		theoDemButton.setForeground(Color.BLACK);
		theoDemButton.setFocusPainted(false);
		theoDemButton.setOpaque(true);
		theoDemButton.setContentAreaFilled(true);
		theoDemButton.setBorderPainted(false);
		theoDemButton.setBorder(new CompoundBorder(
		    new LineBorder(Color.BLACK, 2),
		    new EmptyBorder(5, 15, 5, 15)
		));
		theoDemButton.setPreferredSize(buttonSize);
		theoDemButton.setMaximumSize(buttonSize);
		theoDemButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Thêm vào panel
		buttonPanel.add(Box.createVerticalStrut(10));
		buttonPanel.add(theoGioButton);
		buttonPanel.add(Box.createVerticalStrut(10));
		buttonPanel.add(theoNgayButton);
		buttonPanel.add(Box.createVerticalStrut(10));
		buttonPanel.add(theoDemButton);


		theoGioButton.addActionListener(e -> {
		    updateButtonStyles(theoGioButton, theoNgayButton, theoDemButton);
		    loaiDon = "Theo giờ";

		    // Làm tròn giờ hiện tại lên giờ chẵn tiếp theo
		    now = LocalTime.now().withSecond(0).withNano(0);
		    roundedHour = now.getMinute() > 0 ? now.plusHours(1).withMinute(0) : now;

		    // Cập nhật giờ nhận vào checkInButton
		    String roundedTimeStr = roundedHour.format(DateTimeFormatter.ofPattern("HH:mm"));
		    checkInButton.setFixedTime(roundedTimeStr);
		    checkInButton.setSelectedDate(LocalDate.now());
		    System.out.println("thời gina check in:"+ roundedHour);

		    // ===== Cập nhật giờ trả sau 1 giờ =====
		    LocalTime checkOutTime = roundedHour.plusHours(1);
		    String checkOutTimeStr = checkOutTime.format(DateTimeFormatter.ofPattern("HH:mm"));
		    checkOutButton.setFixedTime(checkOutTimeStr); // ← Đặt lại giờ hiển thị
		    checkOutButton.setSelectedDate(LocalDate.now()); // ← Giữ cùng ngày

		    // Không cho người dùng chỉnh sửa ngày trả
		    checkOutButton.setEnabled(false);

		    // Hiển thị timePanel
		    timePanel.setVisible(true);

		    // Cập nhật lại giao diện
		    setTopInfoLayoutForMode(loaiDon);
		    reloadCenterPanel();
		});

		theoNgayButton.addActionListener(e -> {
		    updateButtonStyles(theoNgayButton, theoGioButton, theoDemButton);
		    loaiDon = "Theo ngày";
		
		    
		    // Đặt lại giờ nhận mặc định
		    checkInButton.setFixedTime("14:00");

		    // Cho chỉnh sửa lại ngày trả
		    checkOutButton.setEnabled(true);
		    checkOutButton.setBackground(Color.WHITE);

		    // Cập nhật lại ngày trả phòng = ngày nhận + 1 và giờ = 12:00
		    LocalDate inDate = checkInButton.getSelectedDate();
		    checkOutButton.setSelectedDate(inDate.plusDays(1));
		    checkOutButton.setFixedTime("12:00");

		    // Ẩn timePanel vì không dùng trong mode "Theo ngày"
		    timePanel.setVisible(false);

		    // Cập nhật giao diện
		    setTopInfoLayoutForMode(loaiDon);
		    reloadCenterPanel();
		});

		theoDemButton.addActionListener(e -> {
		    updateButtonStyles(theoDemButton, theoGioButton, theoNgayButton);
		    loaiDon = "Theo đêm";
		    
		    checkInButton.setFixedTime("20:00");

		    checkOutButton.setEnabled(true);
		    checkOutButton.setBackground(Color.WHITE);

		    // Cập nhật lại ngày trả phòng = ngày nhận + 1 và giờ = 12:00
		    LocalDate inDate = checkInButton.getSelectedDate();
		    checkOutButton.setSelectedDate(inDate.plusDays(1));
		    checkOutButton.setFixedTime("08:00");
		    
		    checkOutButton.setEnabled(false);

		    timePanel.setVisible(false);
		    setTopInfoLayoutForMode(loaiDon);
		    reloadCenterPanel();
		});


		// =============Panel chứa các thông tin ngày và khách==========phần 3 của header=====================
		// infoPanel chính
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
		infoPanel.setPreferredSize(new Dimension((int)(screenWidthTrang1* 0.8),  (int) (headerHeight * 0.78)));
		infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		infoPanel.setBackground(Color.WHITE);

		// ========== Panel trên: chứa ngày nhận, ngày trả và số khách ==========
		timePanel = new JPanel(new BorderLayout());
		topInfoPanel = new JPanel(); // KHỞI TẠO PANEL trước khi dùng
		topInfoPanel.setLayout(new BoxLayout(topInfoPanel, BoxLayout.X_AXIS));

		topInfoPanel.setPreferredSize(new Dimension((int)(screenWidthTrang1* 0.8),  (int) (headerHeight * 0.39))); // Chiếm khoảng nửa infoPanel
		topInfoPanel.setBackground(Color.WHITE);
		int topWidth = topInfoPanel.getPreferredSize().width; 
		int topHeight = topInfoPanel.getPreferredSize().height;
		// Ngày nhận phòng
		checkInPanel = new JPanel(new BorderLayout());
		checkInPanel.setBackground(Color.WHITE);
		JLabel checkInLabel = new JLabel("Ngày nhận phòng");
		checkInLabel.setFont(new Font("Arial", Font.BOLD, 14));
		checkInPanel.add(checkInLabel, BorderLayout.NORTH);
		JPanel checkInWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		checkInWrapper.setBackground(Color.WHITE);
		checkInButton = new CustomDateButton(LocalDate.now(), "14:00", selectedDate -> {
		    todayTimestamp = Timestamp.valueOf(selectedDate.atStartOfDay());
			tuNgay = convertToTimestamp(checkInButton.getSelectedDate(), checkInButton.fixedTime);
			denNgay = convertToTimestamp(checkOutButton.getSelectedDate(), checkOutButton.fixedTime);
//		    tuNgay = Timestamp.valueOf(selectedDate.atTime(roundedHour)); // << thêm dòng này
		    if (loaiDon.equalsIgnoreCase("Theo giờ")) {
			    if (selectedDate.equals(LocalDate.now())) {
			        now = LocalTime.now().withSecond(0).withNano(0);
			        LocalTime rounded = now.getMinute() > 0 ? now.plusHours(1).withMinute(0) : now;
			        checkInButton.setFixedTime(rounded.format(DateTimeFormatter.ofPattern("HH:mm")));
			        checkOutButton.setFixedTime(rounded.plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm")));
	
			        // Cập nhật lại biến toàn cục
			        roundedHour = rounded;
	
			    } else {
			        checkInButton.setFixedTime("00:00");
			        checkOutButton.setFixedTime("01:00");
	
			        // Nếu không phải hôm nay thì đặt roundedHour = 00:00
			        roundedHour = LocalTime.of(0, 0);
			    }
			    checkOutButton.setSelectedDate(selectedDate);
		    }else if (loaiDon.equalsIgnoreCase("Theo ngày")) {
		        checkInButton.setFixedTime("14:00");
		        checkOutButton.setFixedTime("12:00");
		        roundedHour = LocalTime.of(14, 0);

		        // Trả phòng là hôm sau
		        checkOutButton.setSelectedDate(selectedDate.plusDays(1));
		    }

		    else if (loaiDon.equalsIgnoreCase("Theo đêm")) {
		        checkInButton.setFixedTime("20:00");
		        checkOutButton.setFixedTime("08:00");
		        roundedHour = LocalTime.of(20, 0);

		        // Trả phòng là hôm sau
		        checkOutButton.setSelectedDate(selectedDate.plusDays(1));
		    }
		    //tự động load phòng trống
		    capNhatLaiTrang();
		});


		checkInWrapper.add(checkInButton);
		checkInPanel.add(checkInWrapper, BorderLayout.CENTER);
		
		
		//tạo button giờ
		CustomTimeButton timeButton = new CustomTimeButton((selectedTime, duration) -> {
		    System.out.println("Giờ nhận: " + selectedTime);
		    System.out.println("Số giờ sử dụng: " + duration);
		});
		// Tạo panel chứa nút giờ (ẩn mặc định)
		timePanel.setBackground(Color.WHITE);
		JLabel timeLabel = new JLabel("Giờ");
		timeLabel.setFont(new Font("Arial", Font.BOLD, 14));
		timePanel.add(timeLabel, BorderLayout.NORTH);

		JPanel timeWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		timeWrapper.setBackground(Color.WHITE);
		timeWrapper.add(timeButton);
		timePanel.add(timeWrapper, BorderLayout.CENTER);

		timePanel.setVisible(false); // Ẩn mặc định


		
		// Ngày trả phòng
		checkOutPanel = new JPanel(new BorderLayout());
		checkOutPanel.setBackground(Color.WHITE);
		JLabel checkOutLabel = new JLabel("Ngày trả phòng");
		checkOutLabel.setFont(new Font("Arial", Font.BOLD, 14));
		checkOutPanel.add(checkOutLabel, BorderLayout.NORTH);
		JPanel checkOutWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		checkOutWrapper.setBackground(Color.WHITE);
		checkOutButton = new CustomDateButton(LocalDate.now().plusDays(1), "12:00", selectedDate -> {
		    tomorrowTimestamp = Timestamp.valueOf(selectedDate.atStartOfDay());
		    //tự động load phòng trống
		    capNhatLaiTrang();
		});
		checkOutWrapper.add(checkOutButton);
		checkOutPanel.add(checkOutWrapper, BorderLayout.CENTER);

		// Số lượng khách
		guestPanel = new JPanel(new BorderLayout());
		guestPanel.setBackground(Color.WHITE);
		
		JLabel guestLabel = new JLabel("Số lượng khách");
		guestLabel.setFont(new Font("Arial", Font.BOLD, 14));
		guestLabel.setHorizontalAlignment(SwingConstants.CENTER);
		guestPanel.add(guestLabel, BorderLayout.NORTH);
		
		JLabel guestCountLabel = new JLabel("0");
		guestCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
		guestCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
		guestCountLabel.setPreferredSize(new Dimension(30, 25));
		
		JButton plusButton = new JButton("+");
		plusButton.setFont(new Font("Arial", Font.BOLD, 14));
		plusButton.setPreferredSize(new Dimension(45, 25));
		plusButton.addActionListener(e -> {
		    soKhach++;
		    guestCountLabel.setText(String.valueOf(soKhach));
		});
		JPanel guestControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		guestControlPanel.setBackground(Color.WHITE);
		JButton minusButton = new JButton("-");
		minusButton.setFont(new Font("Arial", Font.BOLD, 14));
		minusButton.setPreferredSize(new Dimension(45, 25));
		minusButton.addActionListener(e -> {
		    if (soKhach > 0) {
		        soKhach--;
		        guestCountLabel.setText(String.valueOf(soKhach));
		    }
		});
		guestControlPanel.add(minusButton);
		guestControlPanel.add(guestCountLabel);
		guestControlPanel.add(plusButton);
		guestPanel.add(guestControlPanel, BorderLayout.CENTER);
		
		// Kiểu đặt phòng
		bookingTypePanel = new JPanel(new BorderLayout());
		bookingTypePanel.setBackground(Color.WHITE);

		// Tiêu đề
		JLabel bookingTypeLabel = new JLabel("Kiểu đặt");
		bookingTypeLabel.setFont(new Font("Arial", Font.BOLD, 14));
		bookingTypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bookingTypePanel.add(bookingTypeLabel, BorderLayout.NORTH);

		// ComboBox
		String[] bookingTypes = { "Trực tiếp", "Gián tiếp" };
		
		JComboBox<String> bookingTypeComboBox = new JComboBox<>(bookingTypes);
		bookingTypeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
		bookingTypeComboBox.addActionListener(e -> {
		    kieuDat = (String) bookingTypeComboBox.getSelectedItem();
		});

		JPanel comboBoxWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		bookingTypeComboBox.setPreferredSize(new Dimension(90, bookingTypeComboBox.getPreferredSize().height));
		comboBoxWrapper.setBackground(Color.WHITE);
		comboBoxWrapper.add(bookingTypeComboBox);
		bookingTypePanel.add(comboBoxWrapper, BorderLayout.CENTER);

		checkInPanel.setPreferredSize(new Dimension((int)(topWidth * 0.25), topHeight));     // 30%
		timePanel.setPreferredSize(new Dimension((int)(topWidth * 0.15), topHeight));       // 15%
		checkOutPanel.setPreferredSize(new Dimension((int)(topWidth * 0.25), topHeight));   // 25%
		guestPanel.setPreferredSize(new Dimension((int)(topWidth * 0.2), topHeight));      // 15%
		bookingTypePanel.setPreferredSize(new Dimension((int)(topWidth * 0.15), topHeight));// 15%
		
		// Thêm các panel vào topInfoPanel
		topInfoPanel.add(checkInPanel);
		topInfoPanel.add(checkOutPanel);
		topInfoPanel.add(guestPanel);
		topInfoPanel.add(bookingTypePanel);

		// ========== Panel dưới: chứa 6 checkbox ==========
		JPanel bottomInfoPanel = new JPanel();
		// Đổi layout của bottomInfoPanel
		bottomInfoPanel.setLayout(new BorderLayout());
		bottomInfoPanel.setBackground(Color.WHITE);
		bottomInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Panel chứa checkbox (70%)


		// Set font và background
		cbBanCong.setFont(new Font("Arial", Font.PLAIN, 14));
		cbHutThuoc.setFont(new Font("Arial", Font.PLAIN, 14));
		cbViewBien.setFont(new Font("Arial", Font.PLAIN, 14));

		cbBanCong.setBackground(Color.WHITE);
		cbHutThuoc.setBackground(Color.WHITE);
		cbViewBien.setBackground(Color.WHITE);
		

		tienIchListener = e -> {
			capNhatLaiTrang();
		};
		cbBanCong.addItemListener(tienIchListener);
		cbHutThuoc.addItemListener(tienIchListener);
		cbViewBien.addItemListener(tienIchListener);
		// Thêm vào panel
		JPanel checkboxPanel = new JPanel(new GridLayout(2, 3, 10, 10));
		checkboxPanel.setBackground(Color.WHITE);
		checkboxPanel.add(cbBanCong);
		checkboxPanel.add(cbHutThuoc);
		checkboxPanel.add(cbViewBien);
		


		// Thiết lập kích thước tương đối
		int bottomWidth = (int)(screenWidthTrang1 * 0.8);
		checkboxPanel.setPreferredSize(new Dimension((int)(bottomWidth * 1), 0));


		// Thêm vào bottomInfoPanel
		bottomInfoPanel.add(checkboxPanel, BorderLayout.CENTER);


		// Thêm top và bottom vào infoPanel
		infoPanel.add(topInfoPanel, BorderLayout.NORTH);
		infoPanel.add(bottomInfoPanel, BorderLayout.CENTER);


		// Panel chứa tất cả nội dung header
		JPanel headerContentPanel = new JPanel();
		headerContentPanel.setLayout(new BorderLayout());
		headerContentPanel.setBackground(new Color(220, 220, 220));

		headerContentPanel.add(titleClosePanel, BorderLayout.NORTH);
		headerContentPanel.add(buttonPanel, BorderLayout.WEST);
		headerContentPanel.add(infoPanel, BorderLayout.CENTER);

		headerPanel.add(headerContentPanel, BorderLayout.CENTER);

		// =============================== Center=========================================

		centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setPreferredSize(new Dimension(screenWidthTrang1, centerHeight));
		
		tuNgay = convertToTimestamp(checkInButton.getSelectedDate(), checkInButton.fixedTime);
		denNgay = convertToTimestamp(checkOutButton.getSelectedDate(), checkOutButton.fixedTime);
		System.out.println("test tu ngày:"+tuNgay);
		loadGiaoDienTheoNgay();

		// =============================== Footer=============================================
		footerPanel = new JPanel(new BorderLayout()); 
		footerPanel.setBackground(Color.WHITE);
		footerPanel.setPreferredSize(new Dimension(screenWidthTrang1, footerHeight));
		// ===== Panel trái: Tổng tiền =====
		tongTien= 0;
		tongTienLabel = new JLabel("Tổng tiền: " + String.format("%,d", tongTien) + " VNĐ");
		tongTienLabel.setFont(new Font("Arial", Font.BOLD, 16));
		tongTienLabel.setForeground(Color.BLACK);	
		tongTienLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0)); // top, left, bottom, right

		JPanel leftFooterPanel = new JPanel(new BorderLayout());
		leftFooterPanel.setBackground(Color.WHITE);
		leftFooterPanel.add(tongTienLabel, BorderLayout.CENTER); // tự canh giữa dọc
		
		
		// ===== Panel phải: Nút xác nhận =====
		Font fontButton1 = new Font("Times New Roman", Font.BOLD, 16); // Gợi ý dùng font đồng bộ

		JButton confirmButton = new JButton("Xác nhận");
		confirmButton.setFont(fontButton1);
		confirmButton.setForeground(Color.BLACK);
		confirmButton.setBackground(new Color(0, 255, 128)); // Xanh lá đậm
		confirmButton.setFocusPainted(false);
		confirmButton.setOpaque(true);
		confirmButton.setContentAreaFilled(true);
        confirmButton.setBorderPainted(false);
		confirmButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

		confirmButton.addActionListener(e -> {

	        tuNgay = convertToTimestamp(checkInButton.getSelectedDate(), checkInButton.fixedTime);
			denNgay = convertToTimestamp(checkOutButton.getSelectedDate(), checkOutButton.fixedTime);

	        if (tuNgay == null || denNgay == null) {
	            JOptionPane.showMessageDialog(null, "Vui lòng chọn cả ngày nhận và ngày trả phòng.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
	            return; 
	        }

	        if (!tuNgay.before(denNgay)) {
	            JOptionPane.showMessageDialog(null, "Ngày nhận phòng phải trước ngày trả phòng.", "Lỗi ngày tháng", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        if (tuNgay.before(currentTimestamp)) {
	            JOptionPane.showMessageDialog(null, "Ngày nhận phòng không hợp lệ. Vui lòng chọn từ thời điểm hiện tại trở đi.", "Lỗi ngày nhận", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        int soLuongKhach = Integer.parseInt(guestCountLabel.getText());
	        if (soLuongKhach <= 0) {
	            JOptionPane.showMessageDialog(null, "Số lượng khách phải lớn hơn 0.", "Lỗi số lượng", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        if (loaiDon == null) {
	            JOptionPane.showMessageDialog(null, "Vui lòng chọn kiểu đơn phòng (Giờ / Ngày / Đêm).", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
	            return;
	        }
            
            // Kiểm tra tổng số lượng các loại phòng
            int tongPhong = 0;
            for (int qty : roomQuantities) {
                tongPhong += qty;
            }

            if (tongPhong == 0) {
                JOptionPane.showMessageDialog(
                    null,
                    "Vui lòng chọn ít nhất một loại phòng.",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
                );
                return; // Không cho chuyển trang nếu chưa chọn phòng
            }

			// Chuyển sang trang xác nhận
			JPanel trang2 = null;
			try {
				trang2 = taoTrangXacNhanPhong();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			mainPanel.add(trang2, "Trang2");
			cardLayout.show(mainPanel, "Trang2");
		});
		JPanel rightFooterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightFooterPanel.setBackground(Color.WHITE);
		rightFooterPanel.add(confirmButton);
		// ===== Gắn 2 panel vào footerPanel =====
		footerPanel.add(leftFooterPanel, BorderLayout.WEST);
		footerPanel.add(rightFooterPanel, BorderLayout.EAST);

		// ============end===============Thêm các phần vào panel chọn phòng========================
		chonPhongPanel.add(headerPanel, BorderLayout.NORTH);
		chonPhongPanel.add(centerPanel, BorderLayout.CENTER);
		chonPhongPanel.add(footerPanel, BorderLayout.SOUTH);

		// Thêm panel chọn phòng vào contentPane
		contentPane.add(chonPhongPanel, BorderLayout.CENTER);

		return contentPane;
	}
	private void capNhatLaiTrang() {
	    moTa = layMoTaChinhXac(cbBanCong, cbHutThuoc, cbViewBien);
	    System.out.println(">> moTa để truy vấn SQL: " + moTa);
	    System.out.println(">> từ ngày: " + tuNgay);
	    System.out.println(">> đến ngày: " + denNgay);

	    centerPanel.removeAll();       // Xóa giao diện cũ
	    loadGiaoDienTheoNgay();       // Load lại giao diện theo ngày

	    centerPanel.revalidate();     // Cập nhật lại giao diện
	    centerPanel.repaint();

	    tongTien = 0;
	    capNhatTongTien();
	}


	//==============Hàm cập nhật màu khi ấn nút==============
	private void updateButtonStyles(JButton selected, JButton... others) {
		selected.setBackground(Color.GREEN); // Nút được chọn sẽ tô màu xanh

		for (JButton btn : others) {
			btn.setBackground(Color.WHITE); // Các nút còn lại sẽ về màu trắng
		}
	}
	
	
	public String layMoTaChinhXac(JCheckBox cbBanCong, JCheckBox cbHutThuoc, JCheckBox cbViewBien) {
	    String[] tienIchPhong = {"Ban công", "Phòng hút thuốc", "View biển"};
	    String[] thuTuChuan = {"Ban công", "View biển", "Phòng hút thuốc"};

	    // Lấy danh sách tiện ích đã chọn
	    List<String> daCheck = new ArrayList<>();
	    if (cbBanCong.isSelected()) {
	        daCheck.add("Ban công");
	    }
	    if (cbHutThuoc.isSelected()) {
	        daCheck.add("Phòng hút thuốc");
	    }
	    if (cbViewBien.isSelected()) {
	        daCheck.add("View biển");
	    }

	    // Sắp xếp lại theo thứ tự chuẩn
	    List<String> sapXepTheoThuTu = new ArrayList<>();
	    for (String t : thuTuChuan) {
	        if (daCheck.contains(t)) {
	            sapXepTheoThuTu.add(t);
	        }
	    }

	    // Ghép thành chuỗi moTa chuẩn
	    return String.join(", ", sapXepTheoThuTu);
	}

	
	public static Timestamp convertToTimestamp(LocalDate date, String hourMinute) {
	    String dateTimeStr = date.toString() + " " + hourMinute + ":00";
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
	    return Timestamp.valueOf(dateTime);
	}

	
	private void setTopInfoLayoutForMode(String mode) {
	    topInfoPanel.removeAll(); // Xóa các panel cũ

	    int topWidth = topInfoPanel.getPreferredSize().width;
	    int topHeight = topInfoPanel.getPreferredSize().height;

	    if ("Theo giờ".equals(mode)) {
	        // Thiết lập kích thước theo tỷ lệ 25-15-25-20-15
	        checkInPanel.setPreferredSize(new Dimension((int)(topWidth * 0.25), topHeight));
	        timePanel.setPreferredSize(new Dimension((int)(topWidth * 0.12), topHeight));
	        checkOutPanel.setPreferredSize(new Dimension((int)(topWidth * 0.25), topHeight));
	        guestPanel.setPreferredSize(new Dimension((int)(topWidth * 0.28), topHeight));
	        bookingTypePanel.setPreferredSize(new Dimension((int)(topWidth * 0.15), topHeight));

	        // Thêm đủ 5 panel
	        topInfoPanel.add(checkInPanel);
	        topInfoPanel.add(timePanel);
	        topInfoPanel.add(checkOutPanel);
	        topInfoPanel.add(guestPanel);
	        topInfoPanel.add(bookingTypePanel);

	        timePanel.setVisible(true); // hiển thị panel Giờ
	    } else {
	        // Thiết lập lại tỷ lệ khi không có timePanel: 30-25-25-20
	        checkInPanel.setPreferredSize(new Dimension((int)(topWidth * 0.25), topHeight));
	        checkOutPanel.setPreferredSize(new Dimension((int)(topWidth * 0.25), topHeight));
	        guestPanel.setPreferredSize(new Dimension((int)(topWidth * 0.3), topHeight));
	        bookingTypePanel.setPreferredSize(new Dimension((int)(topWidth * 0.2), topHeight));

	        topInfoPanel.add(checkInPanel);
	        topInfoPanel.add(checkOutPanel);
	        topInfoPanel.add(guestPanel);
	        topInfoPanel.add(bookingTypePanel);

	        timePanel.setVisible(false);
	    }

	    topInfoPanel.revalidate();
	    topInfoPanel.repaint();
	}
	//	cập nhật lại centrelpanel
	private void reloadCenterPanel() {
	    centerPanel.removeAll(); // Xoá giao diện cũ
	    tuNgay = convertToTimestamp(checkInButton.getSelectedDate(), checkInButton.fixedTime);
	    denNgay = convertToTimestamp(checkOutButton.getSelectedDate(), checkOutButton.fixedTime);
	    loadGiaoDienTheoNgay();
	    centerPanel.revalidate();
	    centerPanel.repaint();
	}

	//==============Hàm tạo DatePicker==============
	public class CustomDateButton extends JButton {
	    private LocalDate selectedDate;
	    private String fixedTime;
	    private Consumer<LocalDate> onDateChanged;

	    public CustomDateButton(LocalDate defaultDate, String fixedTime, Consumer<LocalDate> onDateChanged) {
	        super(fixedTime + " - " + defaultDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	        this.selectedDate = defaultDate;
	        this.fixedTime = fixedTime;
	        this.onDateChanged = onDateChanged;

	        setBackground(Color.WHITE);
	        setFocusPainted(false);
	        setFont(new Font("Arial", Font.PLAIN, 14));

	        addActionListener(e -> showDatePickerPopup());
	    }

	    private void showDatePickerPopup() {
	        UtilDateModel model = new UtilDateModel();
	        model.setDate(selectedDate.getYear(), selectedDate.getMonthValue() - 1, selectedDate.getDayOfMonth());
	        model.setSelected(true);

	        Properties p = new Properties();
	        p.put("text.today", "Today");
	        p.put("text.month", "Month");
	        p.put("text.year", "Year");

	        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
	        JPopupMenu popup = new JPopupMenu();
	        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
	        popup.setLayout(new BorderLayout());
	        popup.add(datePanel, BorderLayout.CENTER);
	        popup.show(this, 0, this.getHeight());

	        datePanel.addActionListener(event -> {
	            Date selected = (Date) model.getValue();
	            if (selected != null) {
	                selectedDate = selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	                setText(fixedTime + " - " + selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

	                if (onDateChanged != null) {
	                    onDateChanged.accept(selectedDate); // Cập nhật biến ngoài
	                }

	                popup.setVisible(false);
	            }
	        });
	    }

	    public LocalDate getSelectedDate() {
	        return selectedDate;
	    }

	    public void setSelectedDate(LocalDate date) {
	        this.selectedDate = date;
	        setText(fixedTime + " - " + selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	        if (onDateChanged != null) {
	            onDateChanged.accept(date); // cập nhật lại bên ngoài nếu set thủ công
	        }
	    }
	    public void setFixedTime(String newTime) {
	        this.fixedTime = newTime;
	        setText(fixedTime + " - " + selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	    }

	}

	public class CustomTimeButton extends JButton {
	    private LocalTime selectedTime;

	    private int selectedDuration;
	    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	    private final BiConsumer<LocalTime, Integer> onTimeConfirmed;

	    public CustomTimeButton(BiConsumer<LocalTime, Integer> onTimeConfirmed) {
	        super("1 giờ");
	        this.onTimeConfirmed = onTimeConfirmed;
	        this.selectedTime = todayTimestamp.toLocalDateTime().toLocalTime().withSecond(0).withNano(0);
	        this.selectedDuration = 1;

	        setBackground(Color.WHITE);
	        setFocusPainted(false);
	        setFont(new Font("Arial", Font.PLAIN, 14));

	        addActionListener(e -> showTimePopup());
	    }
	    private void showTimePopup() {
	        Window window = SwingUtilities.getWindowAncestor(this);
	        JDialog dialog = new JDialog(window, "Chọn giờ", Dialog.ModalityType.APPLICATION_MODAL);
	        dialog.setLayout(new GridBagLayout());
	        dialog.setSize(300, 180);
	        dialog.setResizable(false);
	        dialog.setLocationRelativeTo(this);

	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(10, 10, 10, 10);
	        gbc.anchor = GridBagConstraints.WEST;

	        // Giờ hiện tại → làm tròn lên giờ chẵn tiếp theo
	        LocalTime localCopy = roundedHour;
	        System.out.println("Giờ mới: " + localCopy);
	        // Nếu vượt quá 23:00 thì không làm gì cả
	        if (localCopy.isAfter(LocalTime.of(23, 0))) {
	            JOptionPane.showMessageDialog(this, "Không còn khung giờ khả dụng hôm nay.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	            return;
	        }
	        System.out.println("now"+now);
	        System.out.println("localCopy"+localCopy);
	        // Danh sách giờ từ startHour đến 23:00
	        List<String> formattedTimes = new ArrayList<>();
	        while (localCopy.isBefore(LocalTime.of(23, 00))) {
	            formattedTimes.add(localCopy.format(formatter));
	            localCopy = localCopy.plusHours(1);
	        }
	        if (localCopy.isBefore(LocalTime.of(23,30))) {
	            formattedTimes.add("23:00");
	        }
	        System.out.println("time"+formattedTimes);
	        JComboBox<String> timeCombo = new JComboBox<>(formattedTimes.toArray(new String[0]));

	        
	        // Chọn giá trị nếu hợp lệ
	        String selectedFormatted = selectedTime.format(formatter);
	        if (formattedTimes.contains(selectedFormatted)) {
	            timeCombo.setSelectedItem(selectedFormatted);
	        } else {
	            timeCombo.setSelectedIndex(0); // fallback an toàn
	        }

	        JComboBox<Integer> durationCombo = new JComboBox<>();
	        for (int i = 1; i <= 10; i++) {
	            durationCombo.addItem(i);
	        }
	        durationCombo.setSelectedItem(selectedDuration);

	        // Label
	        gbc.gridx = 0; gbc.gridy = 0;
	        dialog.add(new JLabel("Giờ nhận phòng:"), gbc);
	        gbc.gridx = 1;
	        dialog.add(timeCombo, gbc);

	        gbc.gridx = 0; gbc.gridy = 1;
	        dialog.add(new JLabel("Số giờ sử dụng:"), gbc);
	        gbc.gridx = 1;
	        dialog.add(durationCombo, gbc);

	        JButton confirmButton = new JButton("Áp dụng");
	        confirmButton.addActionListener(e -> {
	            try {
	                selectedTime = LocalTime.parse((String) timeCombo.getSelectedItem(), formatter);
	                selectedDuration = (Integer) durationCombo.getSelectedItem();
	                updateButtonText();

	                if (onTimeConfirmed != null) {
	                    onTimeConfirmed.accept(selectedTime, selectedDuration);
	                }

	                // --- Cập nhật giờ nhận phòng trên checkInButton ---
	                String selectedTimeStr = selectedTime.format(formatter);
	                checkInButton.setFixedTime(selectedTimeStr);

	                // --- Tính giờ trả ---
	                LocalTime checkOutTime = selectedTime.plusHours(selectedDuration);
	                String checkOutTimeStr = checkOutTime.format(formatter);

	                // --- Nếu giờ trả vượt quá 23:59, tăng ngày ---
	                LocalDate currentCheckInDate = checkInButton.getSelectedDate();
	                LocalDate newCheckOutDate = checkOutTime.isBefore(selectedTime) ? currentCheckInDate.plusDays(1) : currentCheckInDate;

	                checkOutButton.setFixedTime(checkOutTimeStr);
	                checkOutButton.setSelectedDate(newCheckOutDate);

	            } catch (Exception ex) {
	                JOptionPane.showMessageDialog(this, "Lỗi chọn giờ", "Lỗi", JOptionPane.ERROR_MESSAGE);
	            } finally {
	                dialog.dispose();
	            }
	        });


	        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
	        dialog.add(confirmButton, gbc);

	        dialog.setVisible(true);
	    }


	    private void updateButtonText() {
	        setText(selectedDuration + " giờ");
	    }

	    public LocalTime getSelectedTime() {
	        return selectedTime;
	    }

	    public int getSelectedDuration() {
	        return selectedDuration;
	    }
	    public void setSelectedTime(LocalTime time) {
	        this.selectedTime = time;
	        updateButtonText();
	    }

	}

	//==============Hàm tạo từng row phòng==============
	private JPanel createRoomRow(String roomName, int roomPrice, int soPhongTrong, JLabel[] totalLabels,
			int[] roomQuantities, int index) {
		JPanel rowPanel = new JPanel(new GridLayout(1, 4));
		rowPanel.setPreferredSize(new Dimension((int) (screenWidthTrang1 * 0.95), (int) (screenHeightTrang1 * 0.17)));
		rowPanel.setBackground(Color.decode("#EEEEEE"));
		rowPanel.setBorder(new LineBorder(Color.decode("#D9D9D9"), 2));

		// === Tên phòng + số lượng trống ===
		JPanel namePanel = new JPanel(new BorderLayout());
		namePanel.setBackground(Color.decode("#EEEEEE"));

		JLabel nameLabel = new JLabel(roomName, SwingConstants.CENTER);
		nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

		JLabel availabilityLabel = new JLabel("Còn " + soPhongTrong + " phòng trống", SwingConstants.CENTER);
		availabilityLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		availabilityLabel.setForeground(Color.RED);

		namePanel.add(nameLabel, BorderLayout.NORTH);
		namePanel.add(availabilityLabel, BorderLayout.SOUTH);

		rowPanel.add(namePanel);

		// === Giá phòng ===
		JLabel priceLabel = new JLabel(formatCurrency(roomPrice), SwingConstants.CENTER);

		priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		rowPanel.add(priceLabel);

		// === Số lượng (textbox + nút) ===
		JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		JButton decreaseButton = new JButton("-");
		JTextField quantityField = new JTextField("0", 3);
		quantityField.setHorizontalAlignment(JTextField.CENTER);
		JButton increaseButton = new JButton("+");

		quantityPanel.add(decreaseButton);
		quantityPanel.add(quantityField);
		quantityPanel.add(increaseButton);
		rowPanel.add(quantityPanel);

		// === Tổng cộng ===
		final long[] total = {0}; // dùng mảng 1 phần tử để truy cập được trong lambda

		JLabel totalLabel = new JLabel(formatCurrency(total[0]), SwingConstants.CENTER);
		totalLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		totalLabels[index] = totalLabel;
		rowPanel.add(totalLabel);

		increaseButton.addActionListener(e -> {
		    int qty = roomQuantities[index];
		    if (qty < soPhongTrong) {
		        qty++;
		        roomQuantities[index] = qty;
		        quantityField.setText(String.valueOf(qty));

		        total[0] = (long) qty * roomPrice;
		        totalLabel.setText(formatCurrency(total[0]));
//		        totalLabel.putClientProperty("total", total[0]); // nếu bạn vẫn dùng capNhatTongTien()
		        capNhatTongTien();
		    }
		});

		decreaseButton.addActionListener(e -> {
		    int qty = roomQuantities[index];
		    if (qty > 0) {
		        qty--;
		        roomQuantities[index] = qty;
		        quantityField.setText(String.valueOf(qty));

		        total[0] = (long) qty * roomPrice;
		        totalLabel.setText(formatCurrency(total[0]));
//		        totalLabel.putClientProperty("total", total[0]); // nếu bạn vẫn dùng capNhatTongTien()
		        capNhatTongTien();
		    }
		});



		return rowPanel;
		
	}
	private void capNhatTongTien() {
	    tongTien = 0;
	    for (JLabel label : totalLabels) {
	        if (label != null) {
	            String text = label.getText().replaceAll("[^\\d]", ""); // Lọc số
	            if (!text.isEmpty()) {
	                tongTien += Long.parseLong(text);
	            }
	        }
	    }
	    // Gán lại text hiển thị dựa trên biến tongTien
	    tongTienLabel.setText("Tổng tiền: " + String.format("%,d", tongTien) + " VNĐ");
	}



	private void updateTotal(JLabel totalLabel, int qty, int roomPrice) {
	    long total = (long) qty * roomPrice; // dùng long an toàn hơn int
	    totalLabel.setText(formatCurrency(total));
	    totalLabel.putClientProperty("total", total); // lưu giá trị thật vào label
	}
	
	private String formatCurrency(long amount) {
	    DecimalFormat df = new DecimalFormat("#,###");
	    return df.format(amount).replace(",", ".") + " VND";
	}


	//==============LOAD TRANG THEO NGÀY(TRANG 1)==============
	private void loadGiaoDienTheoNgay() {
		// ======= Header Gợi ý phòng ========
		JPanel headerContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
		headerContainer.setPreferredSize(new Dimension(screenWidthTrang1, 60));
		headerContainer.setBackground(Color.WHITE);

		JPanel headerPanel1 = new JPanel(new GridLayout(1, 4));
		headerPanel1.setBackground(new Color(144, 238, 144));
		headerPanel1.setPreferredSize(new Dimension((int) (screenWidthTrang1 * 0.95), 40));
		headerContainer.add(headerPanel1);
		String[] headers = { "Gợi ý phòng", "Giá", "Số lượng", "Thành tiền" };
		for (String text : headers) {
			JLabel label = new JLabel(text, SwingConstants.CENTER);
			label.setFont(new Font("Arial", Font.BOLD, 14));
			headerPanel1.add(label);
		}

		// Danh sách loại phòng
		// ======== Tạo bảng nội dung bên dưới ========
		String[] roomNames = { "Double Room", "Single Room", "Twin Room", "Triple Room" };

		// ==== Tạo mảng lưu số lượng phòng được chọn ====
		roomQuantities = new int[roomNames.length]; // <- Mảng này sẽ được cập nhật trong createRoomRow()

		// Khai báo mảng JLabel để lưu "Tổng cộng" cho từng loại phòng
		totalLabels = new JLabel[roomNames.length];

		// Panel chứa các hàng
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); // top, left, bottom, right
		contentPanel.setBackground(Color.WHITE);
		// ---------------------------------Double Room =====}
		
		JPanel rowPanelDouble = null;
		try {
			rowPanelDouble = createRoomRow("Double Room", chitietdondatphongdao.getGiaTheoKieu(roomNames[0],loaiDon),
					chitietdondatphongdao.countSoPhongTrong(tuNgay, denNgay, "double",moTa), totalLabels, roomQuantities, 0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentPanel.add(rowPanelDouble);
		contentPanel.add(Box.createVerticalStrut(10));

		// ===== Single Room =====
		JPanel rowPanelSingle = null;
		try {
			rowPanelSingle = createRoomRow("Single Room", chitietdondatphongdao.getGiaTheoKieu(roomNames[1],loaiDon),
					chitietdondatphongdao.countSoPhongTrong(tuNgay, denNgay, "single",moTa), totalLabels, roomQuantities, 1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentPanel.add(rowPanelSingle);
		contentPanel.add(Box.createVerticalStrut(10));

		// ===== Twin Room =====
		JPanel rowPanelTwin = null;
		try {
			rowPanelTwin = createRoomRow("Twin Room", chitietdondatphongdao.getGiaTheoKieu(roomNames[2],loaiDon),
					chitietdondatphongdao.countSoPhongTrong(tuNgay, denNgay, "twin",moTa), totalLabels, roomQuantities, 2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentPanel.add(rowPanelTwin);
		contentPanel.add(Box.createVerticalStrut(10));

		// ===== Triple Room =====
		JPanel rowPanelTriple = null;
		try {
			rowPanelTriple = createRoomRow("Triple Room", chitietdondatphongdao.getGiaTheoKieu(roomNames[3],loaiDon),
					chitietdondatphongdao.countSoPhongTrong(tuNgay, denNgay, "triple",moTa), totalLabels, roomQuantities, 3);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentPanel.add(rowPanelTriple);

		// Thêm header và nội dung vào centerPanel
		centerPanel.add(headerContainer, BorderLayout.NORTH);
		centerPanel.add(contentPanel, BorderLayout.CENTER);
	}

	//==============TAO TRANG XÁC NHẬN PHÒNG(TRANG 2)==============
	private JPanel taoTrangXacNhanPhong() throws SQLException {
		// Tính toán kích thước các phần
		int headerHeight = (int) (screenHeightTrang1 * 0.1);
		int centerHeight = (int) (screenHeightTrang1 * 0.8);
		int footerHeight = (int) (screenHeightTrang1 * 0.1);
		// Tạo nội dung form (panel con)
		contentPane = new JPanel(new BorderLayout());
		contentPane.setBackground(Color.WHITE); // Màu nền cho contentPane
		contentPane.setBorder(new LineBorder(Color.BLACK, 2));

		// ====== Panel chia panel thành 3 phần ======
		JPanel chonPhongPanel = new JPanel(new BorderLayout());
		chonPhongPanel.setBackground(Color.WHITE);
		chonPhongPanel.setPreferredSize(new Dimension(300, 800));

		// =========================================== Header===================
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(Color.WHITE); // Màu xám nhạt
		headerPanel.setPreferredSize(new Dimension(screenWidthTrang1, headerHeight));
		// ============== Panel chứa tiêu đề và nút đóng=================phần 1 của
		// header=============================
		JPanel titleClosePanel = new JPanel(new BorderLayout());
		titleClosePanel.setBackground(Color.WHITE);
		titleClosePanel.setPreferredSize(new Dimension(screenWidthTrang1, (int) (headerHeight * 0.65)));
		// Tiêu đề "Chọn phòng"
		JLabel titleLabel = new JLabel("Xác nhận đặt phòng");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Giả sử đã có mã nhân viên "LT001" và số thứ tự từ hàm đếm đơn (sử dụng hàm demSoLuongDonTrongNgay)

		int soThuTu = chitietdondatphongdao.demSoLuongDonTrongNgay() + 1; // Tăng số thứ tự đơn lên một

		// Gọi hàm tạo mã đơn
		maDon = taoMaDonDatPhong(maNhanVien, soThuTu);

		// Tạo JLabel với mã đơn
		JLabel donDatPhongLabel = new JLabel("<html><span style='background-color: #D9D9D9;'>" + maDon + "</span></html>");
		donDatPhongLabel.setFont(new Font("Arial", Font.BOLD, 16));

		// Nút đóng
		JButton closeButton = new JButton("X");
		closeButton.setFont(new Font("Arial", Font.BOLD, 20));
		closeButton.setForeground(Color.BLACK);
		closeButton.setBackground(Color.WHITE);
		closeButton.setFocusPainted(false);
		closeButton.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		closeButton.addActionListener(e -> dispose());

		titleClosePanel.add(titleLabel, BorderLayout.WEST);
		titleClosePanel.add(donDatPhongLabel, BorderLayout.CENTER);
		titleClosePanel.add(closeButton, BorderLayout.EAST);
		headerPanel.add(titleClosePanel, BorderLayout.NORTH);
		// =============================== Center===================
		int[] soLuongPhong = { roomQuantities[0], roomQuantities[1], roomQuantities[2], roomQuantities[3] };
		String[] roomTypes = { "Double Room", "Single Room", "Twin Room", "Triple Room" };
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
		String tuNgayStr = sdf.format(tuNgay);
		String denNgayStr = sdf.format(denNgay);

		JPanel centerPanel = new JPanel();
		centerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		centerPanel.setBackground(Color.white);
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

		// Header
		JPanel headerPanel1 = new JPanel(new GridLayout(1, 5));
		headerPanel1.setBorder(BorderFactory.createMatteBorder(2, 2, 0, 2, Color.BLACK));
		headerPanel1.setPreferredSize(new Dimension((int) (screenWidthTrang1 * 0.85), 40));
		headerPanel1.setBackground(new Color(144, 238, 144));
		String[] headers = { "Hạng phòng", "Phòng", "Ngày nhận", "Thời gian lưu trú", "Ngày trả" };
		for (String header : headers) {
			JLabel label = new JLabel(header, SwingConstants.CENTER);
			label.setFont(new Font("Arial", Font.BOLD, 14));
			headerPanel1.add(label);
		}
		centerPanel.add(headerPanel1, BorderLayout.NORTH);

		// Dòng phòng
		JPanel rowsPanel = new JPanel();
		rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));
		rowsPanel.setBackground(Color.WHITE);

		List<JComboBox<String>> danhSachComboBox = new ArrayList<>();
		System.out.println("danh sach phòng muốn chọn:"+danhSachComboBox);
		int tongSoLuongPhong = 0;
		for (int soLuong : soLuongPhong) {
		    tongSoLuongPhong += soLuong;
		}
		danhSachSoPhongDuocChon = new String[ tongSoLuongPhong]; // tongSoDong là tổng số hàng bạn có

		int index = 0;
		for (int i = 0; i < soLuongPhong.length; i++) {
		    String loaiPhong = roomTypes[i];

		    for (int j = 0; j < soLuongPhong[i]; j++) {
		        JPanel row = new JPanel(new GridLayout(1, 5, 10, 10));
		        row.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		        row.setBackground(Color.WHITE);
		        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

		        // Hạng phòng
		        JLabel roomTypeLabel = new JLabel(loaiPhong, SwingConstants.CENTER);
		        roomTypeLabel.setFont(new Font("Arial", Font.BOLD, 13));
		        row.add(roomTypeLabel);

		        // Phòng
		        JComboBox<String> phongComboBox = new JComboBox<>();
		        danhSachComboBox.add(phongComboBox);

		        try {
		            String loaiPhongDB = loaiPhong.toLowerCase().split(" ")[0];
		            List<String> danhSachPhong = chitietdondatphongdao.layDanhSachPhongTrong(tuNgay, denNgay, loaiPhongDB,moTa);

		            for (String soPhong : danhSachPhong) {
		                phongComboBox.addItem(soPhong);
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		            JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        }

		        // Gán ActionListener để lưu số phòng vào mảng tại vị trí index
		        int currentIndex = index;
		        phongComboBox.addActionListener(e -> {
		            String soPhongChon = (String) phongComboBox.getSelectedItem();
		            danhSachSoPhongDuocChon[currentIndex] = soPhongChon;
		        });

		        // Gán luôn giá trị mặc định ban đầu
		        if (phongComboBox.getItemCount() > 0) {
		            danhSachSoPhongDuocChon[currentIndex] = (String) phongComboBox.getSelectedItem();
		        }


		        row.add(phongComboBox);

		        // Ngày nhận
		        JTextField ngayNhanField = new JTextField(tuNgayStr);
		        ngayNhanField.setEditable(false);
		        ngayNhanField.setHorizontalAlignment(SwingConstants.CENTER);
		        ngayNhanField.setFont(new Font("Arial", Font.PLAIN, 14));
		        row.add(ngayNhanField);

		        // Tổng ngày
		        JTextField thongTinLuuTruField = new JTextField(getThongTinLuuTru());
		        thongTinLuuTruField.setEditable(false);
		        thongTinLuuTruField.setBorder(null);
		        thongTinLuuTruField.setFont(new Font("Arial", Font.PLAIN, 14));
		        thongTinLuuTruField.setHorizontalAlignment(SwingConstants.CENTER);
		        row.add(thongTinLuuTruField);

		        // Ngày trả
		        JTextField ngayTraField = new JTextField(denNgayStr);
		        ngayTraField.setEditable(false);
		        ngayTraField.setHorizontalAlignment(SwingConstants.CENTER);
		        ngayTraField.setFont(new Font("Arial", Font.PLAIN, 14)); // Tăng kích cỡ chữ lên 14
		        row.add(ngayTraField);
		        
		        
		        rowsPanel.add(row);
		        index++;
		    }
		}


		JScrollPane scrollPane = new JScrollPane(rowsPanel);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		centerPanel.add(scrollPane, BorderLayout.CENTER);

		// Footer
		JPanel footerPanel = new JPanel(new BorderLayout());
		footerPanel.setBackground(Color.WHITE);
		footerPanel.setPreferredSize(new Dimension(screenWidthTrang1, footerHeight));

		// ===== Tiền cọc bên trái =====
		tienCoc = "Gián tiếp".equalsIgnoreCase(kieuDat) ? Math.round(tongTien * 0.3) : 0;
		String tienCocStr = String.format("%,d", tienCoc);

		JLabel tienCocLabel = new JLabel("Tiền cọc: " + tienCocStr + " VNĐ");
		tienCocLabel.setFont(new Font("Arial", Font.BOLD, 16));
		tienCocLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // padding trái

		// ===== Nút xác nhận bên phải =====
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightPanel.setOpaque(false); // giữ màu nền trắng của footerPanel

		JButton confirmButton = new JButton("Xác nhận");
		confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
		confirmButton.setForeground(Color.BLACK);
		confirmButton.setBackground(new Color(0, 180, 0));
		confirmButton.setFocusPainted(false);
		confirmButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

		// Xử lý xác nhận
		confirmButton.addActionListener(e -> {
		    Set<String> daChon = new HashSet<>();
		    for (JComboBox<String> comboBox : danhSachComboBox) {
		        String selected = (String) comboBox.getSelectedItem();
		        if (selected != null) {
		            if (daChon.contains(selected)) {
		                JOptionPane.showMessageDialog(null,
		                        "Vui lòng chọn các phòng khác nhau, không được trùng số phòng!",
		                        "Trùng số phòng", JOptionPane.WARNING_MESSAGE);
		                return;
		            }
		            daChon.add(selected);
		        }
		    }


			// Nếu không trùng phòng thì tiếp tục chuyển trang
			JPanel trang3 = taoTrangNhapThongTin(); // gọi trang tiếp theo
			mainPanel.add(trang3, "Trang3");
			cardLayout.show(mainPanel, "Trang3");
		});

		rightPanel.add(confirmButton);

		// Gắn vào footerPanel
		footerPanel.add(tienCocLabel, BorderLayout.WEST);
		footerPanel.add(rightPanel, BorderLayout.EAST);


		// Thêm các phần vào main panel nếu cần (nếu bạn chưa thêm vào main trước đó)
		chonPhongPanel.add(headerPanel, BorderLayout.NORTH);
		chonPhongPanel.add(centerPanel, BorderLayout.CENTER);
		chonPhongPanel.add(footerPanel, BorderLayout.SOUTH);

		// Thêm panel chọn phòng vào contentPane
		contentPane.add(chonPhongPanel, BorderLayout.CENTER);

		return contentPane;
	}
	
	private String getThongTinLuuTru() {
	    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");

	    LocalDateTime checkIn = LocalDateTime.of(checkInButton.getSelectedDate(),
	                                              LocalTime.parse(checkInButton.fixedTime));
	    LocalDateTime checkOut = LocalDateTime.of(checkOutButton.getSelectedDate(),
	                                               LocalTime.parse(checkOutButton.fixedTime));

	    String tu = checkIn.format(fmt);
	    String den = checkOut.format(fmt);

	    String suffix = "";

	    switch (loaiDon.toLowerCase()) {
	        case "theo giờ":
	            long soGio = ChronoUnit.HOURS.between(checkIn, checkOut);
	            suffix = "" + soGio + " giờ";
	            break;
	        case "theo ngày":
	            long soNgay = ChronoUnit.DAYS.between(checkIn.toLocalDate(), checkOut.toLocalDate());
	            suffix = "" + soNgay + " ngày";
	            break;
	        case "theo đêm":
	        	long soDem = ChronoUnit.DAYS.between(checkIn.toLocalDate(), checkOut.toLocalDate());
	        	suffix = "" + soDem + " đêm";
	            break;
	        default:
	            suffix = "";
	    }

	    return suffix;
	}


	//==============Hàm tạo mã đơn đặt phòng==============
	public String taoMaDonDatPhong(String maNhanVien, int soThuTu) {
	    // Lấy ngày hiện tại
	    LocalDate ngayHienTai = LocalDate.now();
	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
	    String ngayStr = ngayHienTai.format(dtf);

	    // Bỏ 4 ký tự đầu của mã nhân viên
	    String maNhanVienRutGon = maNhanVien.substring(4);

	    String sttStr = String.format("%03d", soThuTu);

	    // Tạo mã
	    return ngayStr + maNhanVienRutGon + sttStr;
	}

	//==============TAO TRANG TẠO THÔNG TIN KHÁCH HÀNG(TRANG 3)==============
	private JPanel taoTrangNhapThongTin() {
		// Tính toán kích thước các phần
		int headerHeight = (int) (screenHeightTrang1 * 0.1);
		int centerHeight = (int) (screenHeightTrang1 * 0.8);
		int footerHeight = (int) (screenHeightTrang1 * 0.1);
		// Tạo nội dung form (panel con)
		contentPane = new JPanel(new BorderLayout());
		contentPane.setBackground(Color.WHITE); // Màu nền cho contentPane
		contentPane.setBorder(new LineBorder(Color.BLACK, 2));

		// ====== Panel chia panel thành 3 phần ======
		JPanel chonPhongPanel = new JPanel(new BorderLayout());
		chonPhongPanel.setBackground(Color.WHITE);
		chonPhongPanel.setPreferredSize(new Dimension(300, 800));

		// =========================================== Header====================================
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(Color.GREEN); // Màu xám nhạt
		headerPanel.setPreferredSize(new Dimension(screenWidthTrang1, headerHeight));

		// ============== Panel chứa tiêu đề và nút đóng=================phần 1 của
		// header=============================
		JButton closeButton = new JButton("X");
		closeButton.setFont(new Font("Arial", Font.BOLD, 20));
		closeButton.setForeground(Color.BLACK);
		closeButton.setBackground(Color.WHITE);
		closeButton.setFocusPainted(false);
		closeButton.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		closeButton.setPreferredSize(new Dimension(45, 30)); // Chiều rộng và chiều cao cố định
		closeButton.addActionListener(e -> dispose());

		// Tạo panel chứa nút để kiểm soát vị trí
		JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		closePanel.setOpaque(false); // Trong suốt để không ảnh hưởng nền
		closePanel.setPreferredSize(new Dimension(60, (int) (headerHeight * 0.5))); // Chỉ chiếm 50% chiều cao
		closePanel.add(closeButton);

		// ======= Panel chứa tiêu đề căn giữa =======
		JLabel titleLabel = new JLabel("Thông tin khách hàng", JLabel.LEFT);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel centerTitlePanel = new JPanel(new BorderLayout());
		centerTitlePanel.setOpaque(false);
		centerTitlePanel.add(titleLabel, BorderLayout.CENTER);

		// ======= Panel tiêu đề chính =======
		JPanel titleClosePanel = new JPanel(new BorderLayout());
		titleClosePanel.setBackground(Color.WHITE);
		titleClosePanel.setPreferredSize(new Dimension(screenWidthTrang1, (int) (headerHeight * 0.25)));

		titleClosePanel.add(centerTitlePanel, BorderLayout.CENTER);
		titleClosePanel.add(closePanel, BorderLayout.EAST); // Gắn panel nhỏ chứa nút "X"

		headerPanel.add(titleClosePanel);

		// =============================== Center=====================================================

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
		centerPanel.setPreferredSize(new Dimension(screenWidthTrang1, centerHeight));

		// ======================= Top Panel: Thông tin khách hàng =====================
		JPanel topPanel = new JPanel(new GridBagLayout()); 
		topPanel.setBackground(Color.WHITE);

		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL; // để các component giãn theo chiều ngang

		int cochu = 14;

		JLabel lblHoTen = new JLabel("<html>Họ và tên <font color='red'>*</font></html>");
		lblHoTen.setFont(new Font("Arial", Font.BOLD, cochu));
		JTextField txtHoTen = new JTextField();

		JLabel lblSDT = new JLabel("<html>Số điện thoại <font color='red'>*</font></html>");
		lblSDT.setFont(new Font("Arial", Font.BOLD, cochu));
		JTextField txtSDT = new JTextField();

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Arial", Font.BOLD, cochu));
		JTextField txtEmail = new JTextField();

		JLabel lblCccd;
		if ("Trực tiếp".equals(kieuDat)) {
		    lblCccd = new JLabel("<html>Căn cước/hộ chiếu<font color='red'>*</font></html>");
		} else {
		    lblCccd = new JLabel("Căn cước/hộ chiếu");
		}

		lblCccd.setFont(new Font("Arial", Font.BOLD, cochu));
		JTextField txtCccd = new JTextField();
		
		String text = "Dịch vụ thuê nôi em bé";
		Font font = new Font("Arial", Font.BOLD, cochu); // cùng font như label bạn dùng
		FontMetrics fm = new JLabel().getFontMetrics(font);
		int width = fm.stringWidth(text);  // ← Đây là chiều rộng tính bằng pixel
		System.out.println("Chiều rộng của chuỗi là: " + width + " px");

		
		JLabel lblBuffet = new JLabel("Dịch vụ buffet");
		lblBuffet.setFont(new Font("Arial", Font.BOLD, cochu));

		JSpinner spnBuffet = new JSpinner(new SpinnerNumberModel(0, 0, 30, 1));
		spnBuffet.setFont(new Font("Arial", Font.PLAIN, cochu));
		spnBuffet.setMinimumSize(new Dimension((int) (screenWidthTrang1 * 0.3), (int)(screenHeightTrang1*0.06)));
		spnBuffet.setPreferredSize(new Dimension((int) (screenWidthTrang1 * 0.1), (int)(screenHeightTrang1*0.06)));
		spnBuffet.setMaximumSize(new Dimension((int) (screenWidthTrang1 * 0.1), (int)(screenHeightTrang1*0.06)));

		JLabel lblThueXeDay = new JLabel("Dịch vụ thuê xe đẩy ");
		lblThueXeDay.setFont(new Font("Arial", Font.BOLD, cochu));

		JSpinner spnThueXeDay = new JSpinner(new SpinnerNumberModel(0, 0, 30, 1));
		spnThueXeDay.setFont(new Font("Arial", Font.PLAIN, cochu));
		spnThueXeDay.setMinimumSize(new Dimension((int) (screenWidthTrang1 * 0.1), (int)(screenHeightTrang1*0.06)));
		spnThueXeDay.setPreferredSize(new Dimension((int) (screenWidthTrang1 * 0.1), (int)(screenHeightTrang1*0.06)));
		spnThueXeDay.setMaximumSize(new Dimension((int) (screenWidthTrang1 * 0.1), (int)(screenHeightTrang1*0.06)));
		
		JLabel lblBaoMau = new JLabel("Dịch vụ bảo mẫu");
		lblBaoMau.setFont(new Font("Arial", Font.BOLD, cochu));

		JSpinner spnBaoMau = new JSpinner(new SpinnerNumberModel(0, 0, 30, 1));
		spnBaoMau.setFont(new Font("Arial", Font.PLAIN, cochu));
		spnBaoMau.setMinimumSize(new Dimension((int) (screenWidthTrang1 * 0.1), (int)(screenHeightTrang1*0.06)));
		spnBaoMau.setPreferredSize(new Dimension((int) (screenWidthTrang1 * 0.1), (int)(screenHeightTrang1*0.06)));
		spnBaoMau.setMaximumSize(new Dimension((int) (screenWidthTrang1 * 0.1), (int)(screenHeightTrang1*0.06)));
		
		JLabel lblNoiEmBe = new JLabel("Dịch vụ thuê nôi em bé");
		lblNoiEmBe.setFont(new Font("Arial", Font.BOLD, cochu));

		JSpinner spnThNoiEmBe = new JSpinner(new SpinnerNumberModel(0, 0, 30, 1));
		spnThNoiEmBe.setFont(new Font("Arial", Font.PLAIN, cochu));
		spnThNoiEmBe.setMinimumSize(new Dimension((int) (screenWidthTrang1 * 0.1), (int)(screenHeightTrang1*0.06)));
		spnThNoiEmBe.setPreferredSize(new Dimension((int) (screenWidthTrang1 * 0.1), (int)(screenHeightTrang1*0.06)));
		spnThNoiEmBe.setMaximumSize(new Dimension((int) (screenWidthTrang1 * 0.1), (int)(screenHeightTrang1*0.06)));

		int row = 0;
		Dimension spinnerSize = new Dimension(150, (int)(screenHeightTrang1 * 0.06));

		// Hàng 1: Số điện thoại
		gbc.gridy = row++;
		gbc.gridx = 0; gbc.gridwidth = 1;
		topPanel.add(lblSDT, gbc);
		gbc.gridx = 1; gbc.gridwidth = 3;
		topPanel.add(txtSDT, gbc);

		// Hàng 2: Họ tên
		gbc.gridy = row++;
		gbc.gridx = 0; gbc.gridwidth = 1;
		topPanel.add(lblHoTen, gbc);
		gbc.gridx = 1; gbc.gridwidth = 3;
		topPanel.add(txtHoTen, gbc);

		// Hàng 3: Email
		gbc.gridy = row++;
		gbc.gridx = 0; gbc.gridwidth = 1;
		topPanel.add(lblEmail, gbc);
		gbc.gridx = 1; gbc.gridwidth = 3;
		topPanel.add(txtEmail, gbc);

		// Hàng 4: CCCD
		gbc.gridy = row++;
		gbc.gridx = 0; gbc.gridwidth = 1;
		topPanel.add(lblCccd, gbc);
		gbc.gridx = 1; gbc.gridwidth = 3;
		topPanel.add(txtCccd, gbc);

		// Set cùng kích thước cho spinner
		spnBuffet.setPreferredSize(spinnerSize);
		spnThueXeDay.setPreferredSize(spinnerSize);
		spnBaoMau.setPreferredSize(spinnerSize);
		spnThNoiEmBe.setPreferredSize(spinnerSize);

		// Hàng 5: Dịch vụ buffet
		// Tạo label mô tả
		JLabel lblBuffetMoTa = new JLabel("");
		lblBuffetMoTa.setFont(new Font("Arial", Font.ITALIC, cochu));

		// Tạo panel chứa spinner + mô tả
		JPanel buffetWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		buffetWrapper.setBackground(Color.WHITE); // giống topPanel
		buffetWrapper.add(spnBuffet);
		buffetWrapper.add(lblBuffetMoTa);

		// Hàng 5: Dịch vụ buffet
		gbc.gridy = row++;
		gbc.gridx = 0; gbc.gridwidth = 1;
		topPanel.add(lblBuffet, gbc);

		gbc.gridx = 1; gbc.gridwidth = 3;
		topPanel.add(buffetWrapper, gbc);



		// ===== Hàng 6: Dịch vụ thuê xe đẩy
		JLabel lblXeDayMoTa = new JLabel("");
		lblXeDayMoTa.setFont(new Font("Arial", Font.ITALIC, cochu));
		JPanel xeDayWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		xeDayWrapper.setBackground(Color.WHITE);
		xeDayWrapper.add(spnThueXeDay);
		xeDayWrapper.add(lblXeDayMoTa);

		gbc.gridy = row++;
		gbc.gridx = 0; gbc.gridwidth = 1;
		topPanel.add(lblThueXeDay, gbc);
		gbc.gridx = 1; gbc.gridwidth = 3;
		topPanel.add(xeDayWrapper, gbc);


		// ===== Hàng 7: Dịch vụ bảo mẫu
		JLabel lblBaoMauMoTa = new JLabel("");
		lblBaoMauMoTa.setFont(new Font("Arial", Font.ITALIC, cochu));
		JPanel baoMauWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		baoMauWrapper.setBackground(Color.WHITE);
		baoMauWrapper.add(spnBaoMau);
		baoMauWrapper.add(lblBaoMauMoTa);

		gbc.gridy = row++;
		gbc.gridx = 0; gbc.gridwidth = 1;
		topPanel.add(lblBaoMau, gbc);
		gbc.gridx = 1; gbc.gridwidth = 3;
		topPanel.add(baoMauWrapper, gbc);


		// ===== Hàng 8: Dịch vụ thuê nôi em bé
		JLabel lblNoiMoTa = new JLabel("");
		lblNoiMoTa.setFont(new Font("Arial", Font.ITALIC, cochu));
		JPanel noiWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		noiWrapper.setBackground(Color.WHITE);
		noiWrapper.add(spnThNoiEmBe);
		noiWrapper.add(lblNoiMoTa);

		gbc.gridy = row++;
		gbc.gridx = 0; gbc.gridwidth = 1;
		topPanel.add(lblNoiEmBe, gbc);
		gbc.gridx = 1; gbc.gridwidth = 3;
		topPanel.add(noiWrapper, gbc);




		centerPanel.add(topPanel, BorderLayout.NORTH);

		int giaBuffetMotNguoi = 150_000;
		int giaXeDay = 50_000;
		int giaBaoMau = 40_000;
		int giaNoiEmBe = 30_000;


		// Spinner dịch vụ buffet
		spnBuffet.addChangeListener(e -> {
		    soLuongBuffet = (int) spnBuffet.getValue();
		    System.out.println("Buffet: " + soLuongBuffet);
		    //cập nhật tiền buffet
		    int soNguoi = (int) spnBuffet.getValue();
		    int tongTien = soNguoi * giaBuffetMotNguoi;
		    lblBuffetMoTa.setText(soNguoi + " vé / " + String.format("%,d", tongTien) + " vnd");
		});

		// Spinner dịch vụ thuê xe đẩy
		spnThueXeDay.addChangeListener(e -> {
		    soLuongThueXeDay = (int) spnThueXeDay.getValue();
		    System.out.println("Thuê xe đẩy: " + soLuongThueXeDay);
			//cập nhật tiền Thuê xe đẩy
		    int soXe = (int) spnThueXeDay.getValue();
		    int tongTien = soXe * giaXeDay;
		    lblXeDayMoTa.setText(soXe + " xe / " + String.format("%,d", tongTien) + " vnd");
		});

		// Spinner dịch vụ bảo mẫu
		spnBaoMau.addChangeListener(e -> {
		    soLuongBaoMau = (int) spnBaoMau.getValue();
		    System.out.println("Bảo mẫu: " + soLuongBaoMau);
			//cập nhật tiền Bảo mẫu
		    int soNguoi = (int) spnBaoMau.getValue();
		    int tongTien = soNguoi * giaBaoMau;
		    lblBaoMauMoTa.setText(soNguoi + " người / " + String.format("%,d", tongTien) + " vnd");
		});

		// Spinner dịch vụ thuê nôi em bé
		spnThNoiEmBe.addChangeListener(e -> {
		    soLuongNoiEmBe = (int) spnThNoiEmBe.getValue();
		    System.out.println("Nôi em bé: " + soLuongNoiEmBe);
			//cập nhật tiền Thuê nôi em bé
		    int soNoi = (int) spnThNoiEmBe.getValue();
		    int tongTien = soNoi * giaNoiEmBe;
		    lblNoiMoTa.setText(soNoi + " nôi / " + String.format("%,d", tongTien) + " vnd");
		});

		
		setTextFieldHeight(txtHoTen);
		setTextFieldHeight(txtSDT);
		setTextFieldHeight(txtEmail);
		setTextFieldHeight(txtCccd);
		
		txtSDT.getDocument().addDocumentListener(new DocumentListener() {
		    private void timKhach() {
		        String sdt = txtSDT.getText().trim();
		        if (sdt.matches("\\d{10}")) {
		            KhachHang kh = khachhangdao.timKhachHangTheoSoDienThoai(sdt);
		            if (kh != null) {
		                txtHoTen.setText(kh.getHoTen());
		                txtCccd.setText(kh.getSoCCCD());
		                txtEmail.setText(kh.getEmail());
		            } else {
		                txtHoTen.setText("");
		                txtCccd.setText("");
		                txtEmail.setText("");
		            }
		        } else {
		            // Nếu chưa đủ 10 số thì clear
		            txtHoTen.setText("");
		            txtCccd.setText("");
		            txtEmail.setText("");
		        }
		    }

		    @Override
		    public void insertUpdate(DocumentEvent e) {
		        timKhach();
		    }

		    @Override
		    public void removeUpdate(DocumentEvent e) {
		        timKhach();
		    }

		    @Override
		    public void changedUpdate(DocumentEvent e) {
		        timKhach();
		    }
		});


		// =============================== Footer=======================================
		JPanel footerPanel = new JPanel(new BorderLayout());
		footerPanel.setBackground(Color.WHITE);
		footerPanel.setPreferredSize(new Dimension(screenWidthTrang1, footerHeight));
		
		// Panel bên trái chứa tổng tiền và tiền cọc
		JPanel leftInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0)); // padding trái
		leftInfoPanel.setBackground(Color.WHITE);

		tongTienLabel = new JLabel("Tổng tiền: 0 VNĐ");
		tongTienLabel.setFont(new Font("Arial", Font.BOLD, 16));
		tongTienLabel.setForeground(Color.BLACK);
		capNhatTongTien();
		// hiển thị tiền cọc
		long tienCoc = "Gián tiếp".equalsIgnoreCase(kieuDat) ? Math.round(tongTien * 0.3) : 0;
		String tienCocStr = String.format("%,d", tienCoc);
		JLabel tienCocLabel = new JLabel("Tiền cọc: " + tienCocStr + " VNĐ");
		tienCocLabel.setFont(new Font("Arial", Font.BOLD, 16));
		tienCocLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // padding trái

		leftInfoPanel.add(tongTienLabel);
		leftInfoPanel.add(tienCocLabel);
		
		
		// Tạo nút xác nhận
		JButton confirmButton = new JButton("Xác nhận đặt phòng");
		confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
		confirmButton.setForeground(Color.BLACK);
		confirmButton.setBackground(new Color(0, 180, 0));
		confirmButton.setFocusPainted(false);
		confirmButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); // Padding trong nút

		// Tạo panel đệm chứa nút, đặt khoảng cách tới cạnh trên/dưới/phải
		JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)); // căn phải
		wrapperPanel.setOpaque(false); // không làm mất màu nền của footerPanel
		wrapperPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 15)); // top, left, bottom, right
		wrapperPanel.add(confirmButton);
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String regexSDT = "^(032|033|034|035|036|037|038|039|096|097|098|086|083|084|085|081|082|088|091|094|070|079|077|076|078|090|093|089|056|058|092|059|099)[0-9]{7}$";
				String regexEmail = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";
				String regexHoTen = "^([A-Za-zÀ-ỹà-ỹ]+)( [A-Za-zÀ-ỹà-ỹ]+)*$";
				String regexCccd = "^(001|002|004|006|008|010|011|012|014|015|017|019|020|022|023|025|026|027|030|031|033|034|035|036|037|038|040|042|044|045|046|048|049|051|052|054|056|058|060|062|064|066|067|068|070|072|074|075|077|079|080|082|083|084|086|087|089|091|092|093|094|095|096)([0-9])([0-9]{2})([0-9]{6})$";
				// Lấy giá trị nhập vào
				String hoTen = txtHoTen.getText().trim();
				String sdtMoi = txtSDT.getText().trim();
				String email = txtEmail.getText().trim();
				String soCccd = txtCccd.getText().trim();

				// Kiểm tra đủ thông tin
				if (hoTen.isEmpty() || sdtMoi.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin khách hàng!");
					return;
				}else {
					// Kiểm tra regex
					if (!hoTen.matches(regexHoTen)) {
						JOptionPane.showMessageDialog(null, "Họ tên không hợp lệ!");
						return;
					}
					if (!sdtMoi.matches(regexSDT)) {
						JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ!");
						return;
					}
					if (!email.isEmpty() && !email.matches(regexEmail)) {
						JOptionPane.showMessageDialog(null, "Email không hợp lệ!");
						return;
					}
					// Xử lý kiểm tra CCCD theo kiểu đặt
					if (kieuDat.equals("Trực tiếp")) {
						// Bắt buộc nhập và hợp lệ
						if (soCccd.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Vui lòng nhập căn cước công dân hoặc hộ chiếu!");
							return;
						}
						if (!soCccd.matches(regexCccd)) {
							JOptionPane.showMessageDialog(null, "Căn cước công dân hoặc hộ chiếu không hợp lệ!");
							return;
						}
					} else {
						// Gián tiếp: Nếu có nhập thì mới kiểm tra định dạng
						if (!soCccd.isEmpty() && !soCccd.matches(regexCccd)) {
							JOptionPane.showMessageDialog(null, "Căn cước công dân hoặc hộ chiếu không hợp lệ!");
							return;
						}
					}
				}
				

				// Thực hiện xử lý khách hàng mới
				LocalDate ngayHienTai = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
				String ngayHienTaiStr = ngayHienTai.format(formatter);


				KhachHang kh = chitietdondatphongdao.timKhachHangTheoSDT(sdtMoi);
				if (kh != null) {
					System.out.println("Mã KH: " + kh.getMaKH());
					System.out.println("Họ tên: " + kh.getHoTen());
				} else {
					if (khachhangdao.themThongTinKhachHang(hoTen, sdtMoi, soCccd,email, ngayHienTaiStr)) {
						System.out.println("thêm thông tin khách hàng thành công");
					} else {
						System.out.println("thêm thông tin khách hàng không thành công");
					}
				}
				kh = chitietdondatphongdao.timKhachHangTheoSDT(sdtMoi);

		        // Gán trạng thái dựa theo kiểu đặt
		        if (kieuDat.equals("Trực tiếp")) {
		            trangThai = "Nhận phòng";
		        } else if (kieuDat.equals("Gián tiếp")) {
		            trangThai = "Đơn tạm";
		        }
				//thêm đơn đặt phòng
		        System.out.println("===== THÔNG TIN ĐƠN ĐẶT PHÒNG =====");
		        System.out.println("Mã đơn: " + maDon);
		        System.out.println("Mã khách hàng: " + kh.getMaKH());
		        System.out.println("Ngày đặt phòng: " + currentTimestamp);
		        System.out.println("Ngày nhận phòng: " + tuNgay + " (Nhận lúc 14:00)");
		        System.out.println("Ngày trả phòng: " + denNgay + " (Trả trước 12:00)");
		        System.out.println("Số khách: " + soKhach);
		        System.out.println("Tiền cọc: " + tienCoc + " VND");
		        System.out.println("Thời gian đặt cọc: " + hanChotCoc);
		        System.out.println("Mã nhân viên xử lý: " + maNhanVien);
		        System.out.println("Loại đơn: " + loaiDon);
		        System.out.println("Trạng thái: " + trangThai);
		        System.out.println("=====================================");
		        boolean themDonThanhCong = chitietdondatphongdao.themDonDatPhong(
		        	    maDon,
		        	    kh.getMaKH(),
		        	    currentTimestamp,         
		        	    tuNgay,
		        	    denNgay,
		        	    soKhach,
		        	    tienCoc,
		        	    hanChotCoc,       
		        	    maNhanVien,
		        	    loaiDon,
		        	    trangThai
		        	);

		        if (themDonThanhCong) {
		            System.out.println("Thêm đơn đặt phòng thành công");
		    		NhanPhongUtil np = new NhanPhongUtil();
		            boolean coLoi = false;

		            for (int i = 0; i < danhSachSoPhongDuocChon.length; i++) {
		                boolean themChiTiet = chitietdondatphongdao.themChiTietDonDatPhong(maDon, danhSachSoPhongDuocChon[i]);
		                if (!themChiTiet) {
		                    coLoi = true;
		                    break;
		                }
				        if (kieuDat.equals("Trực tiếp")) {
			                phongdao.setTrangThaiPhong(danhSachSoPhongDuocChon[i], "Đang ở");
				        } 
		            }
		            np.insertChiTietKhiDatTrucTiep(maDon);

		            if (coLoi) {
		                // Nếu có lỗi, xóa DonDatPhong đã thêm
		                dondatphongdao.xoaDonDatPhong(maDon);
		                System.out.println("Có lỗi khi thêm chi tiết. Đã rollback đơn đặt phòng.");
		            }
		            String maPhieu = maDon+ "001";
		            LocalDateTime ngayLap = currentTimestamp.toLocalDateTime(); 
		            
		            System.out.println("mã phiếu: "+maPhieu);
		            System.out.println("ngày lập phiếu: "+ngayLap);
		            DonDatPhong ddp = new DonDatPhong(maDon);
		            pdv = new PhieuDichVu(maPhieu,ddp,ngayLap,"Chưa thanh toán");
		            if(phieudichvudao.themPhieuDichVu(pdv)) {
		            	System.out.println("Thêm đơn phiếu dịch vụ thành công");
		            }
	                PhieuDichVu phieudichvu = new PhieuDichVu(maPhieu);
		         // 1. Dịch vụ Buffet
		            if (soLuongBuffet > 0) {
		                DichVu dvBuffet = new DichVu("DVBuffet");
		                ChiTietPhieuDichVu ctpdv = new ChiTietPhieuDichVu(phieudichvu, dvBuffet, soLuongBuffet);
		                if (chitietphieudichvudao.them(ctpdv)) {
		                    System.out.println("Đã thêm chi tiết dịch vụ: Buffet");
		                }
		            }

		            // 2. Dịch vụ Thuê xe đẩy
		            if (soLuongThueXeDay > 0) {
		                DichVu dvXeDay = new DichVu("DVThueXeDay");
		                ChiTietPhieuDichVu ctpdv = new ChiTietPhieuDichVu(phieudichvu, dvXeDay, soLuongThueXeDay);
		                if (chitietphieudichvudao.them(ctpdv)) {
		                    System.out.println("Đã thêm chi tiết dịch vụ: Thuê xe đẩy");
		                }
		            }

		            // 3. Dịch vụ Bảo mẫu
		            if (soLuongBaoMau > 0) {
		                DichVu dvBaoMau = new DichVu("DVBaoMau");
		                ChiTietPhieuDichVu ctpdv = new ChiTietPhieuDichVu(phieudichvu, dvBaoMau, soLuongBaoMau);
		                if (chitietphieudichvudao.them(ctpdv)) {
		                    System.out.println("Đã thêm chi tiết dịch vụ: Bảo mẫu");
		                }
		            }

		            // 4. Dịch vụ Nôi em bé
		            if (soLuongNoiEmBe > 0) {
		                DichVu dvNoi = new DichVu("DVNoiEmBe");
		                ChiTietPhieuDichVu ctpdv = new ChiTietPhieuDichVu(phieudichvu, dvNoi, soLuongNoiEmBe);
		                if (chitietphieudichvudao.them(ctpdv)) {
		                    System.out.println("Đã thêm chi tiết dịch vụ: Nôi em bé");
		                }
		            }


		            
		            JOptionPane.showMessageDialog(null, "Đặt phòng thành công!");
		            dispose();
		        } else {
		            System.out.println("Thêm đơn đặt phòng không thành công");
		        }
			}
	});
		
		// Gộp lại bằng BorderLayout để phân bố trái - phải rõ ràng
		JPanel containerPanel = new JPanel(new BorderLayout());
		containerPanel.setBackground(Color.WHITE);
		containerPanel.add(leftInfoPanel, BorderLayout.WEST);
		containerPanel.add(wrapperPanel, BorderLayout.EAST);

		// Thêm containerPanel vào footerPanel (FlowLayout)
		footerPanel.add(containerPanel);

		// ============end===============Thêm các phần vào panel chọn phòng=======================
		chonPhongPanel.add(headerPanel, BorderLayout.NORTH);
		chonPhongPanel.add(centerPanel, BorderLayout.CENTER);
		chonPhongPanel.add(footerPanel, BorderLayout.SOUTH);

		// Thêm panel chọn phòng vào contentPane
		contentPane.add(chonPhongPanel, BorderLayout.CENTER);

		return contentPane;
	}

	//==============Hàm set chiều cao cho textfield==============
	private void setTextFieldHeight(JTextField field) {
		Dimension size = new Dimension(500, 30); // width có thể để 0 hoặc linh hoạt
		field.setMinimumSize(size);
		field.setPreferredSize(size);
		field.setMaximumSize(size);
	}

	//==============HÀM MAIN==============
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame parentFrame = new JFrame("Cửa sổ chính");
			parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			parentFrame.setSize(400, 400);
			parentFrame.setLocationRelativeTo(null);
			parentFrame.setVisible(true);

			// Mở dialog đặt phòng
			DatPhong_GUI datPhongDialog = new DatPhong_GUI(parentFrame);
			datPhongDialog.setVisible(true);
		});
	}
}
