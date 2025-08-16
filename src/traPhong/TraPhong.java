package traPhong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import GUI.QuanLyDatPhong_GUI;
import dao.ChiTietApDung_DAO;
import dao.ChiTietSuDungPhong_Dao;
import dao.DonDatPhong_Dao;
import dao.KhachHang_Dao;
import dao.KhuyenMai_DAO;
import dao.LoaiPhong_Dao;
import dao.PhieuDichVu_DAO;
import dao.Phong_Dao;
import entity.ChiTietApDung;
import entity.ChiTietSuDungPhong;
import entity.DonDatPhong;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.LoaiPhong;
import entity.Phong;

public class TraPhong extends JFrame implements chiPhiPhatSinh_Dialog.ChiPhiPhatSinhListener {
	private JTextField maDon;
	private JTextField hoVaTen;
	private JTextField ngayNhan;
	private JTable table_phongTra;
	private JTable table_dichVu;
	private int frameWidth;
	private int frameHeight;
	private JTextField ngayTra;
	private JTextField soLuongKhach;
	private JTextField tongTienSDDV;
	private JTable table_TinhChiPhiPhatSinh;
	private DonDatPhong currentDonDatPhong;
	private JPanel hoaDonCT;
	private JPanel body;
	private JTextField tongTienPS;
	private ArrayList<Object[]> danhSachPhongDuocChon = new ArrayList<>();
	private JTextField amountField;
	private JLabel qrLabel;
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DonDatPhong ddp = new DonDatPhong();
					DonDatPhong_Dao dao = new DonDatPhong_Dao();
					ddp = dao.getDonDatPhongTheoMa("26052025LT001006");
					TraPhong window = new TraPhong(ddp);
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public TraPhong(DonDatPhong ddp) {
		this.currentDonDatPhong = ddp;
		initialize(currentDonDatPhong);
		themSuKienCheckbox();
		themSuKienClickTableChiPhi();
	}

	private void initialize(DonDatPhong ddp) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();

		Rectangle screenBounds = gc.getBounds();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

		int screenWidth = screenBounds.width - (screenInsets.left + screenInsets.right);
		int screenHeight = screenBounds.height - (screenInsets.top + screenInsets.bottom);

		frameWidth = (int) (screenWidth);
		frameHeight = (int) (screenHeight);
		System.out.println(frameWidth);
		System.out.println(frameHeight);
		setIconImage(Toolkit.getDefaultToolkit().getImage("img/HinhAnhGiaoDienChinh/logo.png"));
		getContentPane().setBackground(new Color(226, 219, 219));
		setBounds(100, 100, frameWidth, frameHeight);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		JPanel Header = createHeaderPanel();
		getContentPane().add(Header);

		body = createBodyPanel();
		getContentPane().add(body);

		hienThiThongTin(ddp);
		hienThiDanhSachPhong(ddp);
		loadTableDichVu(ddp.getMaDonDatPhong());

		hoaDonCT = hoaDonCT();
		hoaDonCT.setVisible(false);
		getContentPane().add(hoaDonCT);

	}

	private JPanel createHeaderPanel() {
		JPanel Header = new JPanel();
		Header.setBounds(0, 0, frameWidth, (int) (frameHeight * 0.12));
		Header.setBackground(new Color(255, 255, 255));
		Header.setLayout(null);
		Header.setBorder(new LineBorder(Color.black));

		JLabel lblLoGo = new JLabel("");
		ImageIcon originalIcon = new ImageIcon("img/HinhAnhGiaoDienChinh/logo.png");
		Image image = originalIcon.getImage().getScaledInstance(Math.round(88f * frameWidth / 1536),
				Math.round(88f * frameHeight / 816), Image.SCALE_SMOOTH);
		ImageIcon logoIcon = new ImageIcon(image);
		lblLoGo.setIcon(logoIcon);
		lblLoGo.setBounds(Math.round(frameWidth * (5f / 1536f)), Math.round(frameHeight * (5f / 816f)),
				Math.round(frameWidth * (88f / 1536f)), Math.round(frameHeight * (88f / 816f)));
		Header.add(lblLoGo);

		JButton undo = new JButton("");
		undo.setIcon(new ImageIcon("img/HinhAnhGiaoDienChinh/AnhTraPhong/undo.png"));
		undo.setBounds(Math.round(frameWidth * (103f / 1536f)), Math.round(frameHeight * (55f / 816f)),
				Math.round(frameWidth * (45f / 1536f)), Math.round(frameHeight * (38f / 816f)));
		undo.setContentAreaFilled(false);
		undo.setBorderPainted(false);
		undo.setFocusPainted(false);
		undo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (hoaDonCT.isVisible()) {
					// Nếu hóa đơn chi tiết đang hiển thị thì ẩn đi, hiển thị body
					hoaDonCT.setVisible(false);
					body.setVisible(true);
				} else {
					// Nếu body đang hiển thị, quay lại giao diện quản lý đơn đặt phòng
					dispose(); // đóng frame hiện tại
					QuanLyDatPhong_GUI frame = new QuanLyDatPhong_GUI();
					frame.setVisible(true);
				}
			}
		});
		Header.add(undo);

		JButton Home = new JButton("");
		Home.setIcon(new ImageIcon("img/HinhAnhGiaoDienChinh/AnhTraPhong/home.png"));
		Home.setBounds(Math.round(frameWidth * (158f / 1536f)), Math.round(frameHeight * (55f / 816f)),
				Math.round(frameWidth * (37f / 1536f)), Math.round(frameHeight * (38f / 816f)));
		Home.setContentAreaFilled(false);
		Home.setBorderPainted(false);
		Home.setFocusPainted(false);
		Home.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose(); // đóng frame hiện tại
				QuanLyDatPhong_GUI frame = new QuanLyDatPhong_GUI();
				frame.setVisible(true);
			}
		});
		Header.add(Home);

		JLabel lblNewLabel_7 = new JLabel("Trả phòng");
		lblNewLabel_7.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(25f * Math.min(frameWidth / 1536, frameHeight / 816))));
		lblNewLabel_7.setBounds(Math.round(frameWidth * (102f / 1536f)), Math.round(frameHeight * (10f / 816f)),
				Math.round(frameWidth * (115f / 1536f)), Math.round(frameHeight * (35f / 816f)));
		Header.add(lblNewLabel_7);

		JButton help = new JButton("");
		help.setIcon(new ImageIcon("img/HinhAnhGiaoDienChinh/AnhTraPhong/help.png"));
		help.setBounds(Math.round(frameWidth * (1348f / 1536f)), Math.round(frameHeight * (36f / 816f)),
				Math.round(frameWidth * (37f / 1536f)), Math.round(frameHeight * (32f / 816f)));
		help.setContentAreaFilled(false);
		help.setBorderPainted(false);
		help.setFocusPainted(false);
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

		JLabel lblNewLabel_8 = new JLabel("New label");
		lblNewLabel_8.setBounds(Math.round(frameWidth * (1464f / 1536f)), Math.round(frameHeight * (20f / 816f)),
				Math.round(frameWidth * (45f / 1536f)), Math.round(frameHeight * (45f / 816f)));
		ImageIcon user = new ImageIcon("img/HinhAnhGiaoDienChinh/AnhTraPhong/anhdaidien.jpg");

		// Lấy đối tượng Image và resize nó
		Image resizedImage = user.getImage().getScaledInstance(42, 42, Image.SCALE_SMOOTH);

		// Tạo lại ImageIcon từ ảnh đã resize
		ImageIcon resizedIcon = new ImageIcon(resizedImage);

		// Gán icon vào label
		lblNewLabel_8.setIcon(resizedIcon);
		Header.add(lblNewLabel_8);

		JLabel lblNewLabel_9 = new JLabel("Lễ tân");
		lblNewLabel_9.setBounds(Math.round(frameWidth * (1464f / 1536f)), Math.round(frameHeight * (62f / 816f)),
				Math.round(frameWidth * (45f / 1536f)), Math.round(frameHeight * (13f / 816f)));
		Header.add(lblNewLabel_9);

		return Header;
	}

	private JPanel createBodyPanel() {
		JPanel body = new JPanel();
		body.setBackground(new Color(192, 192, 192));
		body.setBounds(0, (int) (frameHeight * 0.12), frameWidth, (int) (frameHeight * 0.88));
		body.setLayout(null);

		JPanel Traipanel = createTraiPanel();
		body.add(Traipanel);

		JPanel Phaipanel = createPhaiPanel();
		body.add(Phaipanel);

		return body;
	}

	private JPanel createTraiPanel() {
		JPanel Traipanel = new JPanel();
		Traipanel.setBackground(new Color(255, 255, 255));
		Traipanel.setBounds(Math.round(frameWidth * (10f / 1536f)), Math.round(frameHeight * (10f / 816f)),
				(int) (frameWidth * 0.5), (int) (frameHeight * 0.83));
		Traipanel.setLayout(null);

		JLabel TTDonDat = new JLabel("Thông tin đơn đặt phòng");
		TTDonDat.setBounds(Math.round(frameWidth * (186f / 1536f)), Math.round(frameHeight * (10f / 816f)),
				Math.round(frameWidth * (324f / 1536f)), Math.round(frameHeight * (52f / 816f)));
		TTDonDat.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(30f * Math.min(frameWidth / 1536, frameHeight / 816))));
		Traipanel.add(TTDonDat);

		JLabel lblNewLabel = new JLabel("Mã đơn đặt phòng:");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536, frameHeight / 816))));
		lblNewLabel.setBounds(Math.round(frameWidth * (50f / 1536f)), Math.round(frameHeight * (72f / 816f)),
				Math.round(frameWidth * (194f / 1536f)), Math.round(frameHeight * (32f / 816f)));
		Traipanel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Họ tên khách hàng:");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536, frameHeight / 816))));
		lblNewLabel_1.setBounds(Math.round(frameWidth * (50f / 1536f)), Math.round(frameHeight * (118f / 816f)),
				Math.round(frameWidth * (175f / 1536f)), Math.round(frameHeight * (24f / 816f)));
		Traipanel.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Ngày nhận phòng:");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536, frameHeight / 816))));
		lblNewLabel_2.setBounds(Math.round(frameWidth * (50f / 1536f)), Math.round(frameHeight * (159f / 816f)),
				Math.round(frameWidth * (175f / 1536f)), Math.round(frameHeight * (24f / 816f)));
		Traipanel.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Ngày trả phòng:");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536, frameHeight / 816))));
		lblNewLabel_3.setBounds(Math.round(frameWidth * (50f / 1536f)), Math.round(frameHeight * (201f / 816f)),
				Math.round(frameWidth * (159f / 1536f)), Math.round(frameHeight * (24f / 816f)));
		Traipanel.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("Số lượng khách:");
		lblNewLabel_4.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536, frameHeight / 816))));
		lblNewLabel_4.setBounds(Math.round(frameWidth * (50f / 1536f)), Math.round(frameHeight * (243f / 816f)),
				Math.round(frameWidth * (159f / 1536f)), Math.round(frameHeight * (24f / 816f)));
		Traipanel.add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("Các dịch vụ đã sử dụng");
		lblNewLabel_5.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(26f * Math.min(frameWidth / 1536, frameHeight / 816))));
		lblNewLabel_5.setBounds(Math.round(frameWidth * (205f / 1536f)), Math.round(frameHeight * (284f / 816f)),
				Math.round(frameWidth * (270f / 1536f)), Math.round(frameHeight * (32f / 816f)));
		Traipanel.add(lblNewLabel_5);

		maDon = new JTextField();
		maDon.setEditable(false);
		maDon.setFocusable(false);
		maDon.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536, frameHeight / 816))));
		maDon.setBounds(Math.round(frameWidth * (275f / 1536f)), Math.round(frameHeight * (75f / 816f)),
				Math.round(frameWidth * (404f / 1536f)), Math.round(frameHeight * (32f / 816f)));
		Traipanel.add(maDon);
		maDon.setColumns(10);

		hoVaTen = new JTextField();
		hoVaTen.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536, frameHeight / 816))));
		hoVaTen.setEditable(false);
		hoVaTen.setFocusable(false);
		hoVaTen.setColumns(10);
		hoVaTen.setBounds(Math.round(frameWidth * (275f / 1536f)), Math.round(frameHeight * (117f / 816f)),
				Math.round(frameWidth * (404f / 1536f)), Math.round(frameHeight * (32f / 816f)));
		Traipanel.add(hoVaTen);

		ngayNhan = new JTextField();
		ngayNhan.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536, frameHeight / 816))));
		ngayNhan.setEditable(false);
		ngayNhan.setFocusable(false);
		ngayNhan.setColumns(10);
		ngayNhan.setBounds(Math.round(frameWidth * (275f / 1536f)), Math.round(frameHeight * (158f / 816f)),
				Math.round(frameWidth * (404f / 1536f)), Math.round(frameHeight * (32f / 816f)));
		Traipanel.add(ngayNhan);

		ngayTra = new JTextField();
		ngayTra.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536, frameHeight / 816))));
		ngayTra.setEditable(false);
		ngayTra.setFocusable(false);
		ngayTra.setColumns(10);
		ngayTra.setBounds(Math.round(frameWidth * (275f / 1536f)), Math.round(frameHeight * (200f / 816f)),
				Math.round(frameWidth * (404f / 1536f)), Math.round(frameHeight * (32f / 816f)));
		Traipanel.add(ngayTra);

		soLuongKhach = new JTextField();
		soLuongKhach.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536, frameHeight / 816))));
		soLuongKhach.setEditable(false);
		soLuongKhach.setFocusable(false);
		soLuongKhach.setColumns(10);
		soLuongKhach.setBounds(Math.round(frameWidth * (275f / 1536f)), Math.round(frameHeight * (242f / 816f)),
				Math.round(frameWidth * (404f / 1536f)), Math.round(frameHeight * (32f / 816f)));
		Traipanel.add(soLuongKhach);

		JScrollPane dichVu = createDichVuScrollPane();
		Traipanel.add(dichVu);

		JLabel lblNewLabel_11 = new JLabel("Tổng tiền sử dụng dịch vụ:");
		lblNewLabel_11.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536, frameHeight / 816))));
		lblNewLabel_11.setBounds(Math.round(frameWidth * (71f / 1536f)), Math.round(frameHeight * (535f / 816f)),
				Math.round(frameWidth * (250f / 1536f)), Math.round(frameHeight * (32f / 816f)));
		Traipanel.add(lblNewLabel_11);

		tongTienSDDV = new JTextField();
		tongTienSDDV.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536, frameHeight / 816))));
		tongTienSDDV.setEditable(false);
		tongTienSDDV.setFocusable(false);
		tongTienSDDV.setBounds(Math.round(frameWidth * (351f / 1536f)), Math.round(frameHeight * (537f / 816f)),
				Math.round(frameWidth * (288f / 1536f)), Math.round(frameHeight * (28f / 816f)));
		Traipanel.add(tongTienSDDV);
		tongTienSDDV.setColumns(10);

		return Traipanel;
	}

	private JScrollPane createDichVuScrollPane() {
		JScrollPane dichVu = new JScrollPane();
		dichVu.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		dichVu.setBorder(null);
		dichVu.setBackground(new Color(255, 255, 255));
		dichVu.setBounds(Math.round(frameWidth * (25f / 1536f)), Math.round(frameHeight * (345f / 816f)),
				Math.round(frameWidth * (707f / 1536f)), Math.round(frameHeight * (159f / 816f)));

		table_dichVu = new JTable();
		DefaultTableModel model_dv = new DefaultTableModel(new Object[][] {},
				new String[] { "Mã phiếu dịch vụ", "Loại dịch vụ", "Ngày lập phiếu", "Thành tiền", "" }) {
			boolean[] columnEditables = new boolean[] { false, false, false, false, true };

			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		table_dichVu.setModel(model_dv);
		table_dichVu.getColumnModel().getColumn(0).setPreferredWidth(Math.round(130f)); // Ví dụ: 15% chiều rộng
		table_dichVu.setShowVerticalLines(false);
		table_dichVu.setShowHorizontalLines(false);
		table_dichVu.setShowGrid(false);
		table_dichVu.setRowHeight(Math.round(frameHeight * (40f / 816f)));
		table_dichVu.setFont(new Font("Times New Roman", Font.PLAIN,
				Math.round(14f * Math.min(frameWidth / 1536, frameHeight / 816))));
		table_dichVu.setFillsViewportHeight(true);
		table_dichVu.setBorder(null);
		JTableHeader header_dv = table_dichVu.getTableHeader();
		header_dv.setBackground(new Color(220, 255, 220));
		header_dv.setPreferredSize(new Dimension(header_dv.getWidth(), Math.round(frameHeight * (30f / 816f))));
		header_dv.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(16f * Math.min(frameWidth / 1536, frameHeight / 816))));
		header_dv.setBorder(null);

		DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setFont(new Font("Times New Roman", Font.BOLD,
						Math.round(16f * Math.min(frameWidth / 1536, frameHeight / 816))));
				label.setBackground(new Color(220, 255, 220));
				label.setBorder(BorderFactory.createEmptyBorder());
				return label;
			}
		};
		header_dv.setDefaultRenderer(headerRenderer);

		table_dichVu.setBackground(Color.WHITE);

		DefaultTableCellRenderer centerRenderer_dv = new DefaultTableCellRenderer();
		centerRenderer_dv.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < table_dichVu.getColumnCount() - 1; i++) {
			table_dichVu.getColumnModel().getColumn(i).setCellRenderer(centerRenderer_dv);
		}

		ButtonRenderer buttonRenderer = new ButtonRenderer();
		ButtonEditor buttonEditor = new ButtonEditor(table_dichVu);

		int buttonColumnIndex = table_dichVu.getColumnCount() - 1;
		table_dichVu.getColumnModel().getColumn(buttonColumnIndex).setCellRenderer(buttonRenderer);
		table_dichVu.getColumnModel().getColumn(buttonColumnIndex).setCellEditor(buttonEditor);

		ButtonPanel buttonPanelEditor = buttonEditor.getButtonPanel();
		if (buttonPanelEditor != null) {
			buttonPanelEditor.getDeleteButton().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int row = table_dichVu.getSelectedRow();
					if (row >= 0 && row <= table_dichVu.getRowCount()) {
						String maPhieuDichVu = (String) table_dichVu.getValueAt(row, 0); // Lấy mã phiếu dịch vụ

						int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa dịch vụ này không?",
								"Xác nhận xóa", JOptionPane.YES_NO_OPTION);

						if (choice == JOptionPane.YES_OPTION) {
							PhieuDichVu_DAO pdv = new PhieuDichVu_DAO();
							boolean aBoolean = false; // Khởi tạo aBoolean

							try {
								aBoolean = pdv.xoaPhieuDichVu(maPhieuDichVu);
								if (aBoolean) {
									((DefaultTableModel) table_dichVu.getModel()).removeRow(row);

									if (table_dichVu.isEditing())
										table_dichVu.getCellEditor().stopCellEditing(); // ❗ Hủy trạng thái editing

									((DefaultTableModel) table_dichVu.getModel()).fireTableDataChanged(); // ❗ Refresh
																											// bảng

									table_dichVu.clearSelection();
									table_dichVu.revalidate();
									table_dichVu.repaint();

									if (table_dichVu.getRowCount() == 0) {
										table_dichVu.clearSelection();
										table_dichVu.revalidate();
										table_dichVu.repaint();
									}

									capNhatTongTien(); // Cập nhật tổng tiền
								}

							} catch (SQLException e1) {
								e1.printStackTrace();
								JOptionPane.showMessageDialog(null,
										"Lỗi cơ sở dữ liệu khi xóa dịch vụ: " + e1.getMessage());
							}

						} else {
							JOptionPane.showMessageDialog(null, "Hủy xóa dịch vụ.");
						}
					}
				}
			});

			buttonPanelEditor.getDetailButton().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int row = table_dichVu.getSelectedRow();
					if (row != -1) {
						String maPhieu = table_dichVu.getValueAt(row, 0).toString();
						String loaiDV = table_dichVu.getValueAt(row, 1).toString();
						String ngayLapPhieu = table_dichVu.getValueAt(row, 2).toString();

						phieuDichVu_Dialog dialog = new phieuDichVu_Dialog(maPhieu, loaiDV, ngayLapPhieu, TraPhong.this,
								true);
						dialog.setLocationRelativeTo(null);
						dialog.hienThiChiTietDichVu(maPhieu, loaiDV);
						dialog.setVisible(true);
					}
				}
			});
		}

		dichVu.setViewportView(table_dichVu);
		return dichVu;
	}

	private JPanel createPhaiPanel() {
		JPanel Phaipanel = new JPanel();
		Phaipanel.setBackground(new Color(255, 255, 255));
		Phaipanel.setBounds(Math.round(frameWidth * (762f / 1536f)), Math.round(frameHeight * (10f / 816f)),
				(int) (frameWidth * 0.5), (int) (frameHeight * 0.83));
		Phaipanel.setLayout(null);

		JLabel lblNewLabel_6 = new JLabel("Chọn phòng muốn trả");
		lblNewLabel_6.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(27f * Math.min(frameWidth / 1536, frameHeight / 816))));
		lblNewLabel_6.setBounds(Math.round(frameWidth * (243f / 1536f)), Math.round(frameHeight * (10f / 816f)),
				Math.round(frameWidth * (260f / 1536f)), Math.round(frameHeight * (37f / 816f)));
		Phaipanel.add(lblNewLabel_6);

		JButton tiepTuc = new JButton("Tiếp tục");
		tiepTuc.setBackground(new Color(0, 255, 64));
		tiepTuc.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(25f * Math.min(frameWidth / 1536, frameHeight / 816))));
		tiepTuc.setBounds(Math.round(frameWidth * (597f / 1536f)), Math.round(frameHeight * (607f / 816f)),
				Math.round(frameWidth * (154f / 1536f)), Math.round(frameHeight * (47f / 816f)));
		Phaipanel.add(tiepTuc);
		tiepTuc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				layDanhSachPhongDuocChon();
				if (danhSachPhongDuocChon.isEmpty()) {
					return;
				} else {
					body.setVisible(false);
					capNhatHoaDonCT();
					hoaDonCT.setVisible(true);
					capNhatTongTienThanhToan();
				}
			}
		});
		JPanel scrollPanel = createPhongTraScrollPane();
		Phaipanel.add(scrollPanel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(Math.round(frameWidth * (34f / 1536f)), Math.round(frameHeight * (349f / 816f)),
				Math.round(frameWidth * (693f / 1536f)), Math.round(frameHeight * (162f / 816f)));
		scrollPane.setBorder(null);
		scrollPane.setBackground(null);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		Phaipanel.add(scrollPane);

		table_TinhChiPhiPhatSinh = new JTable();
		table_TinhChiPhiPhatSinh.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Mã phòng",
				"Loại phòng", "Số giờ thêm", "Chi phí phụ thu", "Chi phí hư hỏng", "Thành tiền" }) {
			boolean[] columnEditables = new boolean[] { false, false, false, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_TinhChiPhiPhatSinh.setRowHeight(Math.round(frameHeight * (30f / 816f)));
		table_TinhChiPhiPhatSinh.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(14f * Math.min(frameWidth / 1536, frameHeight / 816))));
		scrollPane.setViewportView(table_TinhChiPhiPhatSinh);
		table_TinhChiPhiPhatSinh.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getColumn() == 5) { // Cột "Thành tiền" (index 5)
					capNhatTongTienPhatSinh();
				}
			}
		});

		JLabel lblNewLabel_6_1 = new JLabel("Chi phí phát sinh");
		lblNewLabel_6_1.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(27f * Math.min(frameWidth / 1536, frameHeight / 816))));
		lblNewLabel_6_1.setBounds(Math.round(frameWidth * (257f / 1536f)), Math.round(frameHeight * (293f / 816f)),
				Math.round(frameWidth * (260f / 1536f)), Math.round(frameHeight * (37f / 816f)));
		Phaipanel.add(lblNewLabel_6_1);

		tongTienPS = new JTextField();
		tongTienPS.setText((String) null);
		tongTienPS.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536, frameHeight / 816))));
		tongTienPS.setFocusable(false);
		tongTienPS.setEditable(false);
		tongTienPS.setColumns(10);
		tongTienPS.setBounds(Math.round(frameWidth * (407f / 1536f)), Math.round(frameHeight * (537f / 816f)),
				Math.round(frameWidth * (288f / 1536f)), Math.round(frameHeight * (28f / 816f)));
		Phaipanel.add(tongTienPS);

		JLabel lblNewLabel_11 = new JLabel("Tổng chi phi phát sinh:");
		lblNewLabel_11.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536, frameHeight / 816))));
		lblNewLabel_11.setBounds(Math.round(frameWidth * (66f / 1536f)), Math.round(frameHeight * (535f / 816f)),
				Math.round(frameWidth * (250f / 1536f)), Math.round(frameHeight * (32f / 816f)));
		Phaipanel.add(lblNewLabel_11);
		JTableHeader header1 = table_TinhChiPhiPhatSinh.getTableHeader();
		header1.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(15f * Math.min(frameWidth / 1536, frameHeight / 816))));
		header1.setForeground(Color.black);
		header1.setBackground(new Color(220, 255, 220));

		return Phaipanel;
	}

	private JPanel createPhongTraScrollPane() {
		table_phongTra = new JTable();
		table_phongTra.setShowVerticalLines(false);
		table_phongTra.setShowHorizontalLines(false);
		table_phongTra.setBackground(new Color(255, 255, 255));
		table_phongTra.setFillsViewportHeight(true);
		table_phongTra.setRowHeight(Math.round(frameHeight * (34f / 816f))); // Tăng chiều cao dòng để phù hợp nội dung
		table_phongTra.setBorder(null);
		table_phongTra.setShowGrid(false);

		Object[][] data = {};
		String[] columnNames = { "", "Mã phòng", "Loại phòng", "Thời gian", "Đơn giá", "Thành tiền" };
		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			@Override
			public Class<?> getColumnClass(int column) {
				return (column == 0) ? Boolean.class : String.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 0;
			}
		};
		table_phongTra.setModel(model);

		table_phongTra.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JPanel panel = new JPanel();
				panel.setBackground(new Color(220, 255, 220));
				panel.setLayout(new GridBagLayout());

				if (column == 0) {
					JCheckBox checkBox = new JCheckBox();
					checkBox.setBackground(new Color(220, 255, 220));
					checkBox.setHorizontalAlignment(JLabel.CENTER);
					panel.add(checkBox);
				} else {
					JLabel label = new JLabel(value.toString());
					label.setHorizontalAlignment(JLabel.CENTER);
					panel.add(label);
				}

				return panel;
			}
		});

		JTableHeader header = table_phongTra.getTableHeader();
		header.setBackground(new Color(220, 255, 220));
		header.setPreferredSize(new Dimension(header.getWidth(), Math.round(frameHeight * (40f / 816f))));

		JCheckBox selectAll = new JCheckBox();
		selectAll.setBackground(new Color(220, 255, 220));
		selectAll.setHorizontalAlignment(JLabel.CENTER);

		TableColumnModel columnModel = table_phongTra.getColumnModel();
		columnModel.getColumn(0).setHeaderRenderer(new CheckBoxHeader(selectAll));
		columnModel.getColumn(0).setPreferredWidth(Math.round(frameWidth * (20f / 1536f)));
		selectAll.addActionListener(e -> {
			boolean checked = selectAll.isSelected();
			for (int i = 0; i < table_phongTra.getRowCount(); i++) {
				table_phongTra.setValueAt(checked, i, 0);
			}
		});

		header.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int col = columnModel.getColumnIndexAtX(e.getX());
				if (col == 0) {
					selectAll.doClick();
				}
			}
		});

		table_phongTra.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				c.setBackground(new Color(240, 240, 240));
				return c;
			}
		});

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 1; i < table_phongTra.getColumnCount(); i++) {
			table_phongTra.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		int fontSize = Math.round(16f * Math.min(frameWidth / 1536f, frameHeight / 816f));
		table_phongTra.setFont(new Font("Times New Roman", Font.PLAIN, fontSize));
		header.setFont(new Font("Times New Roman", Font.BOLD, fontSize + 2));

		// Tăng độ rộng cho cột thời gian để hiển thị đủ
		columnModel.getColumn(3).setPreferredWidth(Math.round(frameWidth * (230f / 1536f)));

		JScrollPane scrollPane = new JScrollPane(table_phongTra);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 0, Math.round(frameWidth * (693f / 1536f)), Math.round(frameHeight * (226f / 816f)));
		scrollPane.setBackground(new Color(0, 0, 0, 0));
		scrollPane.setBorder(null);
		scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));
		scrollPane.getViewport().setBorder(null);

		JPanel scrollPanel = new JPanel();
		scrollPanel.setBounds(Math.round(frameWidth * (34f / 1536f)), Math.round(frameHeight * (57f / 816f)),
				Math.round(frameWidth * (693f / 1536f)), Math.round(frameHeight * (226f / 816f)));
		scrollPanel.setLayout(null);
		scrollPanel.setBackground(new Color(0, 0, 0, 0));
		scrollPanel.setBorder(null);
		scrollPanel.add(scrollPane);

		return scrollPanel;
	}

	class CheckBoxHeader extends JPanel implements TableCellRenderer {
		JCheckBox checkBox;

		public CheckBoxHeader(JCheckBox checkBox) {
			super(new GridBagLayout());
			this.checkBox = checkBox;
			this.setBackground(new Color(220, 255, 220));
			add(checkBox);
			checkBox.setBackground(new Color(220, 255, 220));
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			return this;
		}
	}

	class ButtonPanel extends JPanel {
		public JButton deleteButton;
		public JButton detailButton;

		public ButtonPanel() {
			setLayout(new FlowLayout(FlowLayout.CENTER, 1, 10));

			deleteButton = new JButton("");
			deleteButton.setIcon(new ImageIcon("img/HinhAnhGiaoDienChinh/AnhTraPhong/xoa.png"));
			deleteButton.setContentAreaFilled(false);
			deleteButton.setBorderPainted(false);
			add(deleteButton);

			detailButton = new JButton();
			detailButton.setIcon(new ImageIcon("img/HinhAnhGiaoDienChinh/AnhTraPhong/chitiet.png"));
			detailButton.setContentAreaFilled(false);
			detailButton.setBorderPainted(false);
			add(detailButton);
		}

		public JButton getDeleteButton() {
			return deleteButton;
		}

		public JButton getDetailButton() {
			return detailButton;
		}
	}

	class ButtonRenderer extends JPanel implements TableCellRenderer {
		private ButtonPanel panel;

		public ButtonRenderer() {
			setOpaque(true);
			panel = new ButtonPanel();
			setLayout(new BorderLayout());
			add(panel, BorderLayout.CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (isSelected) {
				panel.setBackground(table.getSelectionBackground());
			} else {
				panel.setBackground(table.getBackground());
			}
			return panel;
		}
	}

	class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
		private ButtonPanel panel;
		private JTable table;

		public ButtonEditor(JTable table) {
			this.table = table;
			panel = new ButtonPanel();
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
			return panel;
		}

		@Override
		public Object getCellEditorValue() {
			return null;
		}

		public ButtonPanel getButtonPanel() {
			return panel;
		}
	}

	private void hienThiThongTin(DonDatPhong ddp) {
		maDon.setText(ddp.getMaDonDatPhong());
		hoVaTen.setText(ddp.getKhachHang().getHoTen());
		ngayNhan.setText(ddp.getNgayNhanPhong().format(dtf));
		ngayTra.setText(ddp.getNgayTraPhong().format(dtf));
		soLuongKhach.setText(String.valueOf(ddp.getSoKhach()));
	}

	public void hienThiDanhSachPhong(DonDatPhong ddp) {
		String maDonDatPhong = ddp.getMaDonDatPhong();
		Phong_Dao phongDAO = new Phong_Dao();
		ChiTietSuDungPhong_Dao chiTietSuDungPhongDAO = new ChiTietSuDungPhong_Dao();

		ArrayList<ChiTietSuDungPhong> danhSachPhong = chiTietSuDungPhongDAO.getTheoMaDon(maDonDatPhong);
		danhSachPhong.sort(Comparator.comparing(ChiTietSuDungPhong::getNgayBatDau));

		Map<String, Double> tienTungPhong = TinhTienUtil.tinhTienTungPhong(maDonDatPhong);
		DefaultTableModel model = (DefaultTableModel) table_phongTra.getModel();
		model.setRowCount(0);

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm dd/MM");
		double tongThanhTien = 0.0;

		for (ChiTietSuDungPhong ct : danhSachPhong) {
			Phong phong = phongDAO.getPhongTheoMa(ct.getSoPhong());
			String maPhong = phong.getSoPhong();
			String loaiPhong = phong.getLoaiPhong().getTenLoai();

			LocalDateTime batDau = ct.getNgayBatDau();
			LocalDateTime ketThuc = ct.getNgayKetThuc();

			String thoiGian = batDau.format(dtf) + " → " + ketThuc.format(dtf);

			double donGia = 0.0;
			switch (ddp.getLoaiDon()) {
			case "Theo giờ":
				donGia = phong.getLoaiPhong().getGiaTheoGio();
				break;
			case "Theo đêm":
				donGia = phong.getLoaiPhong().getGiaTheoDem();
				break;
			case "Theo ngày":
			default:
				donGia = phong.getLoaiPhong().getGiaTheoNgay();
				break;
			}

			double thanhTien = tienTungPhong.getOrDefault(maPhong, 0.0);
			tongThanhTien += thanhTien;

			Object[] rowData = { false, maPhong, loaiPhong, thoiGian, String.format("%.0f", donGia),
					String.format("%.0f", thanhTien) };
			model.addRow(rowData);
		}

		// có thể cập nhật lbl_tongTien hoặc label tương ứng nếu có
		// lblTongTien.setText(String.format("%.0f VND", tongThanhTien));
	}

	private PhieuDichVu_DAO phieuDichVu_DAO = new PhieuDichVu_DAO();

	public void loadTableDichVu(String maDonDatPhong) {
		DefaultTableModel model = (DefaultTableModel) table_dichVu.getModel();
		model.setRowCount(0); // Xoá dữ liệu cũ

		ArrayList<Object[]> danhSach = phieuDichVu_DAO.getLoaiDichVuVaThanhTienTheoMaDonDatPhong(maDonDatPhong);
		for (Object[] row : danhSach) {
			String maPhieuDV = (String) row[0];
			String tenLoai = (String) row[1];
			String ngayLapPhieu = (String) row[2];
			double thanhTien = (Double) row[3];

			Object[] rowData = { maPhieuDV, tenLoai, ngayLapPhieu, thanhTien };
			model.addRow(rowData);
		}
		capNhatTongTien();
	}

	public void capNhatTongTien() {
		DefaultTableModel model = (DefaultTableModel) table_dichVu.getModel();
		double tong = 0;

		for (int i = 0; i < model.getRowCount(); i++) {
			Object giaObj = model.getValueAt(i, 3); // Cột "Thành tiền"
			if (giaObj != null) {
				try {
					tong += Double.parseDouble(giaObj.toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		tongTienSDDV.setText(String.format("%.0f", tong));
	}

	private void themSuKienCheckbox() {
		table_phongTra.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getColumn() == 0) {
					capNhatTableTinhChiPhiPhatSinh();
				}
			}
		});
	}

	private void capNhatTableTinhChiPhiPhatSinh() {
		DefaultTableModel modelPhongTra = (DefaultTableModel) table_phongTra.getModel();
		DefaultTableModel modelChiPhi = (DefaultTableModel) table_TinhChiPhiPhatSinh.getModel();
		modelChiPhi.setRowCount(0); // Xóa dữ liệu cũ
		for (int i = 0; i < modelPhongTra.getRowCount(); i++) {

			Boolean isChecked = (Boolean) modelPhongTra.getValueAt(i, 0);

			if (isChecked != null && isChecked) {
				String maPhong = (String) modelPhongTra.getValueAt(i, 1);
				String loaiPhong = (String) modelPhongTra.getValueAt(i, 2);
				modelChiPhi.addRow(new Object[] { maPhong, loaiPhong, "", "", "", "" });
			}
		}
		capNhatTongTienPhatSinh();
	}

	private void capNhatTongTienPhatSinh() {
		DefaultTableModel model = (DefaultTableModel) table_TinhChiPhiPhatSinh.getModel();
		double tong = 0;

		for (int i = 0; i < model.getRowCount(); i++) {
			Object thanhTienObj = model.getValueAt(i, 5); // Cột "Thành tiền" (index 5)
			if (thanhTienObj == null || thanhTienObj.toString().isEmpty()) {
				// Nếu ô trống hoặc null, gán giá trị 0
				model.setValueAt(0.0, i, 5);
				tong += 0.0;
			} else {
				try {
					tong += Double.parseDouble(thanhTienObj.toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
					// Nếu có lỗi chuyển đổi, gán giá trị 0 và in lỗi
					model.setValueAt(0.0, i, 5);
					tong += 0.0;
				}
			}
		}
		tongTienPS.setText(String.format("%.0f", tong));
	}

	private void themSuKienClickTableChiPhi() {
		table_TinhChiPhiPhatSinh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table_TinhChiPhiPhatSinh.getSelectedRow();
				if (row >= 0) {
					// Lấy mã phòng từ dòng được click
					String maPhong = (String) table_TinhChiPhiPhatSinh.getValueAt(row, 0);
					LoaiPhong_Dao loaip = new LoaiPhong_Dao();
					LoaiPhong loai = loaip.getLoaiPhongBySoPhong(maPhong);
					// Truyền maDonDatPhong và số thứ tự dòng vào dialog
					chiPhiPhatSinh_Dialog dialog = new chiPhiPhatSinh_Dialog(currentDonDatPhong.getMaDonDatPhong(), row,
							maPhong, loai.getTenLoai(), TraPhong.this, true);
					dialog.setChiPhiPhatSinhListener(TraPhong.this); // Thiết lập donDatPhong làm listener
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setUndecorated(true);
					dialog.setLocationRelativeTo(null);
					dialog.setSize(833, 714);
					dialog.setResizable(false);
					dialog.setVisible(true);
				}
			}
		});
	}

	@Override
	public void onChiPhiPhatSinhUpdated(int row, String maChiPhi, double chiPhiThietBiHong, int soGioThem,
			double chiPhiPhuThu, double tongChiPhi) {
		// TODO Auto-generated method stub
		table_TinhChiPhiPhatSinh.setValueAt(soGioThem, row, 2);
		table_TinhChiPhiPhatSinh.setValueAt(String.format("%.0f", chiPhiPhuThu), row, 3);
		table_TinhChiPhiPhatSinh.setValueAt(String.format("%.0f", chiPhiThietBiHong), row, 4);
		table_TinhChiPhiPhatSinh.setValueAt(String.format("%.0f", tongChiPhi), row, 5);
	}

	private JTextField tongChiPhi1;
	private JTextField tienCoc1;
	private JTextField thanhTien1;
	private JTextField tienKhachDua1;
	private JTextField tienThoi1;
	private JTextField tenKhach1;
	private JTextField dienThoai1;
	private JTextField ngayNhanPhong1;
	private JTextField ngayTraPhong1;
	private JTextField tongTienPhongTable1;
	private JTextField tongTienSuDungDichVu1;
	private JTextField tongChiPhiPhatSinh1;
	private JTextField maHoaDon1;
	private JTextField tienThoiBangChu1;
	private JTable table1;
	private JComboBox khuyenMai;
	private String a;
	private Double tong;

	public JPanel hoaDonCT() {
		String mahd = maDon.getText();
		String hotenString = hoVaTen.getText();
		String dayNhanString = ngayNhan.getText();
		String dayTraString = ngayTra.getText();
		String chiPhiPhatString = tongTienPS.getText();
		String phiDV = tongTienSDDV.getText();

		KhachHang_Dao khachHang_DAO = new KhachHang_Dao();
		KhachHang khachHang = khachHang_DAO.layKhachHangTheoMaDonDatPhong(mahd);
		String sdt = khachHang.getSdt();

		JPanel panel = new JPanel();
		panel.setBounds(0, Math.round(96f * frameHeight / 816f), frameWidth, Math.round(731f * frameHeight / 816f));
		panel.setBackground(new Color(192, 192, 192));
		panel.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(Math.round(10f * frameWidth / 1536f), Math.round(10f * frameHeight / 816f),
				Math.round(864f * frameWidth / 1536f), Math.round(673f * frameHeight / 816f));
		panel.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Hóa đơn thanh toán chi tiết");
		lblNewLabel_1.setBounds(Math.round(222f * frameWidth / 1536f), Math.round(48f * frameHeight / 816f),
				Math.round(391f * frameWidth / 1536f), Math.round(44f * frameHeight / 816f));
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(30f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		panel_1.add(lblNewLabel_1);

		JLabel lblKhchHng = new JLabel("Khách hàng:");
		lblKhchHng.setBounds(Math.round(179f * frameWidth / 1536f), Math.round(102f * frameHeight / 816f),
				Math.round(195f * frameWidth / 1536f), Math.round(28f * frameHeight / 816f));
		lblKhchHng.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		panel_1.add(lblKhchHng);

		JLabel lblinThoi = new JLabel("Điện thoại:");
		lblinThoi.setBounds(Math.round(179f * frameWidth / 1536f), Math.round(145f * frameHeight / 816f),
				Math.round(195f * frameWidth / 1536f), Math.round(28f * frameHeight / 816f));
		lblinThoi.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		panel_1.add(lblinThoi);

		JLabel lblNgyNhnPhng = new JLabel("Ngày nhận phòng:");
		lblNgyNhnPhng.setBounds(Math.round(179f * frameWidth / 1536f), Math.round(188f * frameHeight / 816f),
				Math.round(195f * frameWidth / 1536f), Math.round(28f * frameHeight / 816f));
		lblNgyNhnPhng.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		panel_1.add(lblNgyNhnPhng);

		JLabel lblNgyTrPhng = new JLabel("Ngày trả phòng:");
		lblNgyTrPhng.setBounds(Math.round(179f * frameWidth / 1536f), Math.round(231f * frameHeight / 816f),
				Math.round(195f * frameWidth / 1536f), Math.round(28f * frameHeight / 816f));
		lblNgyTrPhng.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		panel_1.add(lblNgyTrPhng);

		JLabel lblITinPhng = new JLabel("I Tiền phòng:");
		lblITinPhng.setBounds(Math.round(27f * frameWidth / 1536f), Math.round(247f * frameHeight / 816f),
				Math.round(195f * frameWidth / 1536f), Math.round(44f * frameHeight / 816f));
		lblITinPhng.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		panel_1.add(lblITinPhng);

		JLabel lblIiTinS = new JLabel("II Tiền sử dụng dịch vụ:");
		lblIiTinS.setBounds(Math.round(27f * frameWidth / 1536f), Math.round(472f * frameHeight / 816f),
				Math.round(225f * frameWidth / 1536f), Math.round(44f * frameHeight / 816f));
		lblIiTinS.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		panel_1.add(lblIiTinS);

		JLabel lblIiiTinChi = new JLabel("III Tiền chi phí phát sinh:");
		lblIiiTinChi.setBounds(Math.round(27f * frameWidth / 1536f), Math.round(553f * frameHeight / 816f),
				Math.round(225f * frameWidth / 1536f), Math.round(44f * frameHeight / 816f));
		lblIiiTinChi.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		panel_1.add(lblIiiTinChi);

		JLabel lblTngTinPhng = new JLabel("Tổng tiền phòng:");
		lblTngTinPhng.setBounds(Math.round(320f * frameWidth / 1536f), Math.round(434f * frameHeight / 816f),
				Math.round(195f * frameWidth / 1536f), Math.round(44f * frameHeight / 816f));
		lblTngTinPhng.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		panel_1.add(lblTngTinPhng);

		JLabel lblTngTinS = new JLabel("Tổng tiền sử dụng dịch vụ:");
		lblTngTinS.setBounds(Math.round(243f * frameWidth / 1536f), Math.round(508f * frameHeight / 816f),
				Math.round(248f * frameWidth / 1536f), Math.round(44f * frameHeight / 816f));
		lblTngTinS.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		panel_1.add(lblTngTinS);

		JLabel lblNgyTrPhng_2_1 = new JLabel("Tổng tiền chi phí phát sinh:");
		lblNgyTrPhng_2_1.setBounds(Math.round(238f * frameWidth / 1536f), Math.round(598f * frameHeight / 816f),
				Math.round(235f * frameWidth / 1536f), Math.round(44f * frameHeight / 816f));
		lblNgyTrPhng_2_1.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		panel_1.add(lblNgyTrPhng_2_1);

		JScrollPane scrollPane_11 = new JScrollPane();
		scrollPane_11.setBorder(null);
		scrollPane_11.setBackground(null);
		scrollPane_11.getViewport().setOpaque(false);
		scrollPane_11.setBounds(Math.round(37f * frameWidth / 1536f), Math.round(280f * frameHeight / 816f),
				Math.round(792f * frameWidth / 1536f), Math.round(158f * frameHeight / 816f));
		panel_1.add(scrollPane_11);

		table1 = new JTable();
		table1.setFont(new Font("Times New Roman", Font.PLAIN,
				Math.round(19f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		table1.setModel(new DefaultTableModel(new Object[][] { { null, null, null, null, null }, },
				new String[] { "M\u00E3 ph\u00F2ng", "Lo\u1EA1i ph\u00F2ng", "Th\u1EDDi gian", "\u0110\u01A1n gi\u00E1",
						"Th\u00E0nh ti\u1EC1n" }) {
			boolean[] columnEditables = new boolean[] { false, false, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table1.setRowHeight(Math.round(25f * frameHeight / 816f));

		JTableHeader header = table1.getTableHeader();
		header.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(18f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		header.setPreferredSize(new Dimension(header.getWidth(), Math.round(20f * frameHeight / 816f)));
		scrollPane_11.setViewportView(table1);

		DefaultTableModel model_HoaDon = (DefaultTableModel) table1.getModel();
		model_HoaDon.setRowCount(0);
		for (Object[] row : danhSachPhongDuocChon) {
			model_HoaDon.addRow(row);
		}

		tenKhach1 = new JTextField();
		tenKhach1.setEditable(false);
		tenKhach1.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(18f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		tenKhach1.setBounds(Math.round(360f * frameWidth / 1536f), Math.round(102f * frameHeight / 816f),
				Math.round(304f * frameWidth / 1536f), Math.round(28f * frameHeight / 816f));
		panel_1.add(tenKhach1);
		tenKhach1.setColumns(10);
		tenKhach1.setText(hotenString);

		dienThoai1 = new JTextField();
		dienThoai1.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(18f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		dienThoai1.setEditable(false);
		dienThoai1.setColumns(10);
		dienThoai1.setBounds(Math.round(360f * frameWidth / 1536f), Math.round(145f * frameHeight / 816f),
				Math.round(304f * frameWidth / 1536f), Math.round(28f * frameHeight / 816f));
		panel_1.add(dienThoai1);
		dienThoai1.setText(sdt);

		ngayNhanPhong1 = new JTextField();
		ngayNhanPhong1.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(18f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		ngayNhanPhong1.setEditable(false);
		ngayNhanPhong1.setColumns(10);
		ngayNhanPhong1.setBounds(Math.round(360f * frameWidth / 1536f), Math.round(188f * frameHeight / 816f),
				Math.round(304f * frameWidth / 1536f), Math.round(28f * frameHeight / 816f));
		panel_1.add(ngayNhanPhong1);
		ngayNhanPhong1.setText(dayNhanString);

		ngayTraPhong1 = new JTextField();
		ngayTraPhong1.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(18f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		ngayTraPhong1.setEditable(false);
		ngayTraPhong1.setColumns(10);
		ngayTraPhong1.setBounds(Math.round(360f * frameWidth / 1536f), Math.round(231f * frameHeight / 816f),
				Math.round(304f * frameWidth / 1536f), Math.round(28f * frameHeight / 816f));
		panel_1.add(ngayTraPhong1);
		ngayTraPhong1.setText(dayTraString);

		tongTienPhongTable1 = new JTextField();
		tongTienPhongTable1.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(18f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		tongTienPhongTable1.setEditable(false);
		tongTienPhongTable1.setColumns(10);
		tongTienPhongTable1.setBounds(Math.round(483f * frameWidth / 1536f), Math.round(445f * frameHeight / 816f),
				Math.round(275f * frameWidth / 1536f), Math.round(28f * frameHeight / 816f));
		panel_1.add(tongTienPhongTable1);

		tongTienSuDungDichVu1 = new JTextField();
		tongTienSuDungDichVu1.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(18f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		tongTienSuDungDichVu1.setEditable(false);
		tongTienSuDungDichVu1.setColumns(10);
		tongTienSuDungDichVu1.setBounds(Math.round(483f * frameWidth / 1536f), Math.round(517f * frameHeight / 816f),
				Math.round(275f * frameWidth / 1536f), Math.round(28f * frameHeight / 816f));
		panel_1.add(tongTienSuDungDichVu1);
		if (phiDV != null) {
			tongTienSuDungDichVu1.setText(String.valueOf(phiDV));
		} else {
			tongTienSuDungDichVu1.setText("0");
		}

		tongChiPhiPhatSinh1 = new JTextField();
		tongChiPhiPhatSinh1.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(18f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		tongChiPhiPhatSinh1.setEditable(false);
		tongChiPhiPhatSinh1.setColumns(10);
		tongChiPhiPhatSinh1.setBounds(Math.round(483f * frameWidth / 1536f), Math.round(598f * frameHeight / 816f),
				Math.round(275f * frameWidth / 1536f), Math.round(28f * frameHeight / 816f));
		panel_1.add(tongChiPhiPhatSinh1);

		if (chiPhiPhatString != null) {
			tongChiPhiPhatSinh1.setText(String.valueOf(chiPhiPhatString));
		} else {
			tongChiPhiPhatSinh1.setText("0");
		}

		JLabel lblNewLabel_2 = new JLabel("");
		ImageIcon undoIcon = new ImageIcon("img/HinhAnhGiaoDienChinh/AnhTraPhong/undo.png");
		Image scaledUndoImage = undoIcon.getImage().getScaledInstance(Math.round(32f * frameWidth / 1536f),
				Math.round(32f * frameHeight / 816f), Image.SCALE_SMOOTH);
		lblNewLabel_2.setIcon(new ImageIcon(scaledUndoImage));
		lblNewLabel_2.setBounds(Math.round(124f * frameWidth / 1536f), Math.round(89f * frameHeight / 816f),
				Math.round(32f * frameWidth / 1536f), Math.round(32f * frameHeight / 816f));
		panel_1.add(lblNewLabel_2);

		JLabel lblNewLabel_2_1 = new JLabel("");
		ImageIcon phoneIcon = new ImageIcon("img/HinhAnhGiaoDienChinh/AnhTraPhong/phone-call.png");
		Image scaledPhoneImage = phoneIcon.getImage().getScaledInstance(Math.round(32f * frameWidth / 1536f),
				Math.round(32f * frameHeight / 816f), Image.SCALE_SMOOTH);
		lblNewLabel_2_1.setIcon(new ImageIcon(scaledPhoneImage));
		lblNewLabel_2_1.setBounds(Math.round(124f * frameWidth / 1536f), Math.round(138f * frameHeight / 816f),
				Math.round(32f * frameWidth / 1536f), Math.round(32f * frameHeight / 816f));
		panel_1.add(lblNewLabel_2_1);

		JLabel lblNewLabel_2_2 = new JLabel("");
		ImageIcon calendarIcon1 = new ImageIcon("img/HinhAnhGiaoDienChinh/AnhTraPhong/calendar.png");
		Image scaledCalendarImage1 = calendarIcon1.getImage().getScaledInstance(Math.round(32f * frameWidth / 1536f),
				Math.round(32f * frameHeight / 816f), Image.SCALE_SMOOTH);
		lblNewLabel_2_2.setIcon(new ImageIcon(scaledCalendarImage1));
		lblNewLabel_2_2.setBounds(Math.round(124f * frameWidth / 1536f), Math.round(183f * frameHeight / 816f),
				Math.round(32f * frameWidth / 1536f), Math.round(32f * frameHeight / 816f));
		panel_1.add(lblNewLabel_2_2);

		JLabel lblNewLabel_2_3 = new JLabel("");
		ImageIcon calendarIcon2 = new ImageIcon("img/HinhAnhGiaoDienChinh/AnhTraPhong/calendar.png");
		Image scaledCalendarImage2 = calendarIcon2.getImage().getScaledInstance(Math.round(32f * frameWidth / 1536f),
				Math.round(32f * frameHeight / 816f), Image.SCALE_SMOOTH);
		lblNewLabel_2_3.setIcon(new ImageIcon(scaledCalendarImage2));
		lblNewLabel_2_3.setBounds(Math.round(124f * frameWidth / 1536f), Math.round(226f * frameHeight / 816f),
				Math.round(32f * frameWidth / 1536f), Math.round(32f * frameHeight / 816f));
		panel_1.add(lblNewLabel_2_3);

		maHoaDon1 = new JTextField();
		maHoaDon1.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(15f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		maHoaDon1.setEditable(false);
		maHoaDon1.setColumns(10);
		maHoaDon1.setBounds(Math.round(636f * frameWidth / 1536f), Math.round(10f * frameHeight / 816f),
				Math.round(218f * frameWidth / 1536f), Math.round(28f * frameHeight / 816f));
		panel_1.add(maHoaDon1);
		maHoaDon1.setText(mahd);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 255, 255));
		panel_2.setBounds(Math.round(884f * frameWidth / 1536f), Math.round(10f * frameHeight / 816f),
				Math.round(642f * frameWidth / 1536f), Math.round(621f * frameHeight / 816f));
		panel.add(panel_2);
		panel_2.setLayout(null);

		JLabel lblNewLabel = new JLabel("Tổng chi phí:");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		lblNewLabel.setBounds(Math.round(25f * frameWidth / 1536f), Math.round(26f * frameHeight / 816f),
				Math.round(195f * frameWidth / 1536f), Math.round(44f * frameHeight / 816f));
		panel_2.add(lblNewLabel);

		JLabel lblTinCc = new JLabel("Tiền cọc:");
		lblTinCc.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		lblTinCc.setBounds(Math.round(25f * frameWidth / 1536f), Math.round(80f * frameHeight / 816f),
				Math.round(195f * frameWidth / 1536f), Math.round(39f * frameHeight / 816f));
		panel_2.add(lblTinCc);

		JLabel lblThnhTin = new JLabel("Thành tiền:");
		lblThnhTin.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		lblThnhTin.setBounds(Math.round(25f * frameWidth / 1536f), Math.round(219f * frameHeight / 816f),
				Math.round(195f * frameWidth / 1536f), Math.round(44f * frameHeight / 816f));
		panel_2.add(lblThnhTin);

		JRadioButton tienMat = new JRadioButton("Tiền mặt");
		tienMat.setBackground(new Color(255, 255, 255));
		tienMat.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		tienMat.setBounds(Math.round(110f * frameWidth / 1536f), Math.round(307f * frameHeight / 816f),
				Math.round(134f * frameWidth / 1536f), Math.round(39f * frameHeight / 816f));
		panel_2.add(tienMat);

		JRadioButton chuyenKhoan = new JRadioButton("Chuyển khoản");
		chuyenKhoan.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		chuyenKhoan.setBackground(Color.WHITE);
		chuyenKhoan.setBounds(Math.round(369f * frameWidth / 1536f), Math.round(307f * frameHeight / 816f),
				Math.round(201f * frameWidth / 1536f), Math.round(39f * frameHeight / 816f));
		panel_2.add(chuyenKhoan);
		ButtonGroup paymentGroup = new ButtonGroup();
		paymentGroup.add(tienMat);
		paymentGroup.add(chuyenKhoan);

		JLabel lblPhngThcThanh = new JLabel("Phương thức thanh toán:");
		lblPhngThcThanh.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		lblPhngThcThanh.setBounds(Math.round(25f * frameWidth / 1536f), Math.round(273f * frameHeight / 816f),
				Math.round(284f * frameWidth / 1536f), Math.round(44f * frameHeight / 816f));
		panel_2.add(lblPhngThcThanh);

		JPanel panel_thoiTien = new JPanel();
		panel_thoiTien.setBackground(new Color(255, 255, 255));
		panel_thoiTien.setBounds(Math.round(25f * frameWidth / 1536f), Math.round(352f * frameHeight / 816f),
				Math.round(594f * frameWidth / 1536f), Math.round(216f * frameHeight / 816f));
		panel_2.add(panel_thoiTien);
		panel_thoiTien.setLayout(null);

		JLabel lblTinKhcha = new JLabel("Tiền khách đưa:");
		lblTinKhcha.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		lblTinKhcha.setBounds(Math.round(10f * frameWidth / 1536f), Math.round(10f * frameHeight / 816f),
				Math.round(195f * frameWidth / 1536f), Math.round(44f * frameHeight / 816f));
		panel_thoiTien.add(lblTinKhcha);

		JLabel lblNewLabel_1_1 = new JLabel("Tiền thối lại:");
		lblNewLabel_1_1.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		lblNewLabel_1_1.setBounds(Math.round(10f * frameWidth / 1536f), Math.round(134f * frameHeight / 816f),
				Math.round(195f * frameWidth / 1536f), Math.round(33f * frameHeight / 816f));
		panel_thoiTien.add(lblNewLabel_1_1);

		tienKhachDua1 = new JTextField();
		tienKhachDua1.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		tienKhachDua1.setColumns(10);
		tienKhachDua1.setBounds(Math.round(165f * frameWidth / 1536f), Math.round(10f * frameHeight / 816f),
				Math.round(321f * frameWidth / 1536f), Math.round(33f * frameHeight / 816f));
		panel_thoiTien.add(tienKhachDua1);

		tienThoi1 = new JTextField();
		tienThoi1.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		tienThoi1.setEditable(false);
		tienThoi1.setColumns(10);
		tienThoi1.setBounds(Math.round(165f * frameWidth / 1536f), Math.round(136f * frameHeight / 816f),
				Math.round(321f * frameWidth / 1536f), Math.round(33f * frameHeight / 816f));
		panel_thoiTien.add(tienThoi1);

		JButton btnNewButton = new JButton("");
		btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN,
				Math.round(19f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		btnNewButton.setBounds(Math.round(10f * frameWidth / 1536f), Math.round(64f * frameHeight / 816f),
				Math.round(144f * frameWidth / 1536f), Math.round(44f * frameHeight / 816f));
		btnNewButton.setBorderPainted(false);
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setFocusPainted(false);
		panel_thoiTien.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setFont(new Font("Times New Roman", Font.PLAIN,
				Math.round(19f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		btnNewButton_1.setBounds(Math.round(153f * frameWidth / 1536f), Math.round(64f * frameHeight / 816f),
				Math.round(144f * frameWidth / 1536f), Math.round(44f * frameHeight / 816f));
		btnNewButton_1.setBorderPainted(false);
		btnNewButton_1.setContentAreaFilled(false);
		btnNewButton_1.setFocusPainted(false);
		panel_thoiTien.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.setFont(new Font("Times New Roman", Font.PLAIN,
				Math.round(19f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		btnNewButton_2.setBounds(Math.round(292f * frameWidth / 1536f), Math.round(64f * frameHeight / 816f),
				Math.round(144f * frameWidth / 1536f), Math.round(44f * frameHeight / 816f));
		btnNewButton_2.setBorderPainted(false);
		btnNewButton_2.setContentAreaFilled(false);
		btnNewButton_2.setFocusPainted(false);
		panel_thoiTien.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("");
		btnNewButton_3.setFont(new Font("Times New Roman", Font.PLAIN,
				Math.round(19f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		btnNewButton_3.setBounds(Math.round(440f * frameWidth / 1536f), Math.round(64f * frameHeight / 816f),
				Math.round(144f * frameWidth / 1536f), Math.round(44f * frameHeight / 816f));
		btnNewButton_3.setBorderPainted(false);
		btnNewButton_3.setContentAreaFilled(false);
		btnNewButton_3.setFocusPainted(false);
		panel_thoiTien.add(btnNewButton_3);

		ActionListener buttonClickListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton clicked = (JButton) e.getSource();
				String value = clicked.getText().replace(",", "");
				tienKhachDua1.setText(value);
			}
		};

		btnNewButton.addActionListener(buttonClickListener);
		btnNewButton_1.addActionListener(buttonClickListener);
		btnNewButton_2.addActionListener(buttonClickListener);
		btnNewButton_3.addActionListener(buttonClickListener);

		// Ẩn panel_thoiTien lúc đầu
		panel_thoiTien.setVisible(false);
		// Xử lý sự kiện khi chọn tiền mặt

		JPanel panel_chuyenKhoan = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel_chuyenKhoan.setBackground(new Color(255, 255, 255));
		panel_chuyenKhoan.setBounds(Math.round(25f * frameWidth / 1536f), Math.round(352f * frameHeight / 816f),
				Math.round(594f * frameWidth / 1536f), Math.round(216f * frameHeight / 816f));

		panel_2.add(panel_chuyenKhoan);
		panel_chuyenKhoan.setVisible(false);

		tienMat.addActionListener(e -> {
			panel_thoiTien.setVisible(true);
			panel_chuyenKhoan.setVisible(false);
		});
		qrLabel = new JLabel("", JLabel.CENTER);
		panel_chuyenKhoan.add(qrLabel);
		// Xử lý sự kiện khi chọn chuyển khoản
		chuyenKhoan.addActionListener(e -> {
			panel_thoiTien.setVisible(false);
			try {
				String amount = thanhTien1.getText().trim();
				String bankCode = "agribank"; // viết thường và đúng tên code chuẩn
				String account = "7714205086854";
				String name = "NGO BINH XUYEN";
				String content = "THANH TOAN";

				String qrUrl = "https://img.vietqr.io/image/" + bankCode.toLowerCase() + "-" + account + "-compact2.jpg"
						+ "?amount=" + amount + "&addInfo=" + java.net.URLEncoder.encode(content, "UTF-8")
						+ "&accountName=" + java.net.URLEncoder.encode(name, "UTF-8");
				ImageIcon icon = new ImageIcon(new java.net.URL(qrUrl));
				Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH); // Thay 300x300 theo ý bạn
				qrLabel.setIcon(new ImageIcon(img));
				System.out.println("đã in");

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			panel_chuyenKhoan.setVisible(true);
		});

		tongChiPhi1 = new JTextField();
		tongChiPhi1.setFont(new Font("Times New Roman", Font.PLAIN,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		tongChiPhi1.setEditable(false);
		tongChiPhi1.setBorder(BorderFactory.createEmptyBorder());
		tongChiPhi1.setBackground(null);
		tongChiPhi1.setOpaque(false);
		tongChiPhi1.setBounds(Math.round(270f * frameWidth / 1536f), Math.round(20f * frameHeight / 816f),
				Math.round(321f * frameWidth / 1536f), Math.round(33f * frameHeight / 816f));
		panel_2.add(tongChiPhi1);
		tongChiPhi1.setColumns(10);
		capNhatTongChiPhi();

		tienCoc1 = new JTextField();
		tienCoc1.setFont(new Font("Times New Roman", Font.PLAIN,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		tienCoc1.setEditable(false);
		tienCoc1.setBorder(BorderFactory.createEmptyBorder());
		tienCoc1.setBackground(null);
		tienCoc1.setOpaque(false);
		tienCoc1.setColumns(10);
		tienCoc1.setBounds(Math.round(270f * frameWidth / 1536f), Math.round(68f * frameHeight / 816f),
				Math.round(321f * frameWidth / 1536f), Math.round(33f * frameHeight / 816f));
		panel_2.add(tienCoc1);

		DonDatPhong_Dao ddp_DAO = new DonDatPhong_Dao();
		DonDatPhong ddp = ddp_DAO.getDonDatPhongTheoMa(mahd);
		double tiencoc = ddp.getTienCoc();
		tienCoc1.setText(String.valueOf(tiencoc).replace(".0", ""));

		thanhTien1 = new JTextField();
		thanhTien1.setEditable(false);
		thanhTien1.setFont(new Font("Times New Roman", Font.PLAIN,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		thanhTien1.setBorder(BorderFactory.createEmptyBorder());
		thanhTien1.setBackground(null);
		thanhTien1.setOpaque(false);
		thanhTien1.setColumns(10);
		thanhTien1.setBounds(Math.round(270f * frameWidth / 1536f), Math.round(227f * frameHeight / 816f),
				Math.round(321f * frameWidth / 1536f), Math.round(33f * frameHeight / 816f));
		panel_2.add(thanhTien1);
		tienKhachDua1.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				updateTienThoi(); // Tính toán tiền thối
				updateButtons(); // Cập nhật các nút
			}

			public void removeUpdate(DocumentEvent e) {
				updateTienThoi(); // Tính toán tiền thối
				updateButtons(); // Cập nhật các nút
			}

			public void changedUpdate(DocumentEvent e) {
				updateTienThoi(); // Tính toán tiền thối
				updateButtons(); // Cập nhật các nút
			}

			private void updateButtons() {
				String input = tienKhachDua1.getText().trim();

				if (!input.matches("\\d+")) {
					// Nếu không phải số => clear button và tiền thối
					btnNewButton.setText("");
					btnNewButton_1.setText("");
					btnNewButton_2.setText("");
					btnNewButton_3.setText("");
					tienKhachDua1.setText("");
					tienThoi1.setText(""); // Xóa tiền thối
					return;
				}

				// Xử lý các trường hợp độ dài nhập vào
				if (input.length() == 5) {
					btnNewButton_3.setText(""); // Xóa button khi nhập 5 chữ số
					return;
				}
				if (input.length() == 6) {
					btnNewButton_2.setText(""); // Xóa button khi nhập 6 chữ số
					btnNewButton_3.setText(""); // Xóa button khi nhập 6 chữ số
					return;
				}
				if (input.length() > 6) {
					btnNewButton.setText("");
					btnNewButton_1.setText("");
					btnNewButton_2.setText("");
					btnNewButton_3.setText("");
					return;
				}

				try {
					// Nếu số có độ dài hợp lệ (dưới 5 chữ số)
					int base = Integer.parseInt(input);
					btnNewButton.setText(String.format("%,d", base * 1000));
					btnNewButton_1.setText(String.format("%,d", base * 10000));
					btnNewButton_2.setText(String.format("%,d", base * 100000));
					btnNewButton_3.setText(String.format("%,d", base * 1000000));
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}

			private void updateTienThoi() {
				// Lấy giá trị tiền khách đưa
				String input = tienKhachDua1.getText().trim();

				// Kiểm tra nếu tiền khách đưa là số hợp lệ
				if (!input.matches("\\d+")) {
					tienThoi1.setText(""); // Nếu không phải số, không tính tiền thối
					return;
				}

				try {
					// Kiểm tra giá trị thành tiền
					String thanhTienText = thanhTien1.getText().replace(",", "").trim();

					if (thanhTienText.isEmpty() || !thanhTienText.matches("\\d+")) {
						tienThoi1.setText(""); // Nếu thanhTien không hợp lệ
						return;
					}

					// Lấy giá trị của thành tiền
					int thanhTienValue = Integer.parseInt(thanhTienText);

					// Lấy giá trị tiền khách đưa
					int tienKhachDuaValue = Integer.parseInt(input);

					// Tính tiền thối
					int tienThoiValue = tienKhachDuaValue - thanhTienValue;

					// Cập nhật tiền thối
					tienThoi1.setText(String.format("%,d", tienThoiValue)); // Hiển thị kết quả tiền thối

				} catch (NumberFormatException ex) {
					// Nếu có lỗi trong việc chuyển đổi thành số (ví dụ thanhTien không hợp lệ)
					tienThoi1.setText(""); // Cập nhật lại tiền thối là rỗng
				}
			}
		});
		tienThoi1.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				updateTienThoiBangChu();
			}

			public void removeUpdate(DocumentEvent e) {
				updateTienThoiBangChu();
			}

			public void changedUpdate(DocumentEvent e) {
				updateTienThoiBangChu();
			}

			private void updateTienThoiBangChu() {
				String text = tienThoi1.getText().replace(",", "").trim();
				if (!text.matches("\\d+")) {
					tienThoiBangChu1.setText("");
					return;
				}
				try {
					int amount = Integer.parseInt(text);
					String bangChu = convertNumberToWords(amount) + " đồng";
					tienThoiBangChu1.setText(bangChu.substring(0, 1).toUpperCase() + bangChu.substring(1));
				} catch (NumberFormatException ex) {
					tienThoiBangChu1.setText("");
				}
			}
		});

		panel_thoiTien.add(tienThoi1);
		tienThoiBangChu1 = new JTextField();
		tienThoiBangChu1.setFont(new Font("Times New Roman", Font.ITALIC,
				Math.round(15f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		tienThoiBangChu1.setEditable(false);
		tienThoiBangChu1.setBorder(BorderFactory.createEmptyBorder());
		tienThoiBangChu1.setBackground(null);
		tienThoiBangChu1.setOpaque(false);
		tienThoiBangChu1.setBounds(Math.round(10f * frameWidth / 1536f), Math.round(177f * frameHeight / 816f),
				Math.round(574f * frameWidth / 1536f), Math.round(29f * frameHeight / 816f));
		tienThoiBangChu1.setHorizontalAlignment(JTextField.CENTER);
		panel_thoiTien.add(tienThoiBangChu1);
		tienThoiBangChu1.setColumns(10);
		JButton thanhToan = new JButton("Thanh toán");
		thanhToan.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		thanhToan.setBackground(new Color(0, 255, 128));
		thanhToan.setBounds(Math.round(498f * frameWidth / 1536f), Math.round(578f * frameHeight / 816f),
				Math.round(134f * frameWidth / 1536f), Math.round(33f * frameHeight / 816f));
		panel_2.add(thanhToan);
		thanhToan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LocalDate ngayHienTai = LocalDate.now();
				DateTimeFormatter dinhDang = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				String ngayString = ngayHienTai.format(dinhDang);
				double tienPhong = Double.parseDouble(tongTienPhongTable1.getText());
				double tienDichVu = Double.parseDouble(tongTienSuDungDichVu1.getText());
				double chiPhiPhatSinh = Double.parseDouble(tongChiPhiPhatSinh1.getText());
				double tienCoc = Double.parseDouble(tienCoc1.getText());
				double thanhTien = Double.parseDouble(thanhTien1.getText());
//			              setPhong

				ArrayList<String> maPhongs = layDanhSachMaPhong(table1);
				Phong_Dao phong_DAO = new Phong_Dao();
				for (String ma : maPhongs) {
					System.out.println(ma);
					phong_DAO.setTrangThaiPhong(ma, "Trống");
				}

//							setDonDatPhong
				// Kiểm tra trước khi set trạng thái đơn đặt phòng
				DonDatPhong_Dao ddDatPhong_DAO = new DonDatPhong_Dao();
				String maDon = maHoaDon1.getText();
				ddDatPhong_DAO.setTienCocVeKhong(maDon);

				if (ddDatPhong_DAO.coTheCapNhatTrangThai(maDon)) {
					ddDatPhong_DAO.setTrangThaiDonDatPhong(maDon, "Đã thanh toán");
				}

//							SetPhieuDichVu
				ArrayList<Object> maDV = getMaDichVu(table_dichVu);
				PhieuDichVu_DAO pdv = new PhieuDichVu_DAO();
				for (Object maDichVu : maDV) {
					pdv.capNhatTrangThai((String) maDichVu, "Đã thanh toán");
				}
				JLabel qrLabel = new JLabel("", JLabel.CENTER);
				String maKM = (String) khuyenMai.getSelectedItem();
				System.out.println(maKM.split("-")[0].trim());
				ChiTietApDung ctap = new ChiTietApDung(maDon, maKM.split("-")[0].trim(), thanhTien);
				ChiTietApDung_DAO chiTietApDung_DAO = new ChiTietApDung_DAO();
				chiTietApDung_DAO.addChiTietApDung(ctap);
				if (chuyenKhoan.isSelected()) {
					qrLabel.setSize(Math.round(150f * frameWidth / 1536f), Math.round(150f * frameHeight / 816f));
					try {
						String amount = thanhTien1.getText().trim();
						String bankCode = "agribank"; // viết thường và đúng tên code chuẩn
						String account = "7714205086854";
						String name = "NGO BINH XUYEN";
						String content = "THANH TOAN";

						String qrUrl = "https://img.vietqr.io/image/" + bankCode.toLowerCase() + "-" + account
								+ "-compact2.jpg" + "?amount=" + amount + "&addInfo="
								+ java.net.URLEncoder.encode(content, "UTF-8") + "&accountName="
								+ java.net.URLEncoder.encode(name, "UTF-8");

						BufferedImage originalImage = ImageIO.read(new java.net.URL(qrUrl));
						BufferedImage resizedImage = new BufferedImage(Math.round(150f * frameWidth / 1536f),
								Math.round(150f * frameHeight / 816f), BufferedImage.TYPE_INT_ARGB);

						Graphics2D g2d = resizedImage.createGraphics();
						g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
								RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR); // Dành cho ảnh QR rõ nét
						g2d.drawImage(originalImage, 0, 0, Math.round(150f * frameWidth / 1536f),
								Math.round(150f * frameHeight / 816f), null);
						g2d.dispose();
						qrLabel.setIcon(new ImageIcon(resizedImage));

					} catch (Exception ex) {
						ex.printStackTrace();
					}

					ArrayList<Object[]> data = getTableData(table1);
					try {
						String filePath = "FileTestHoaDon/HoaDon_" + maHoaDon1.getText() + ".pdf";

						inHoaDon.taoHoaDon(maHoaDon1.getText(), ngayString, tenKhach1.getText(), sdt, dayNhanString,
								dayTraString, data, tienPhong, tienDichVu, chiPhiPhatSinh, tienCoc,
								(String) khuyenMai.getSelectedItem(), thanhTien, filePath);
						System.out.println("Đã tạo hóa đơn thành công!");
					} catch (Exception e1) {
						System.err.println("Lỗi khi tạo hóa đơn:");
						e1.printStackTrace();
					}
					int option = JOptionPane.showConfirmDialog(null, "Thanh toán thành công!", "Thông báo",
							JOptionPane.OK_CANCEL_OPTION);

					if (option == JOptionPane.OK_OPTION) {
						// Đóng cửa sổ hiện tại
						Window window = SwingUtilities.getWindowAncestor(thanhToan);
						if (window != null)
							window.dispose();

						// Mở trang DatPhong_GUI
						QuanLyDatPhong_GUI frame = new QuanLyDatPhong_GUI();
						frame.setVisible(true);
					}

				} else if (tienMat.isSelected()) {
					ArrayList<Object[]> data = getTableData(table1);
					try {
						String filePath = "FileTestHoaDon/HoaDon_" + maHoaDon1.getText() + ".pdf";

						inHoaDon.taoHoaDon(maHoaDon1.getText(), ngayString, tenKhach1.getText(), sdt, dayNhanString,
								dayTraString, data, tienPhong, tienDichVu, chiPhiPhatSinh, tienCoc,
								(String) khuyenMai.getSelectedItem(), thanhTien, filePath);
						System.out.println("Đã tạo hóa đơn thành công!");
					} catch (Exception e1) {
						System.err.println("Lỗi khi tạo hóa đơn:");
						e1.printStackTrace();
					}
					int option = JOptionPane.showConfirmDialog(null, "Thanh toán thành công!", "Thông báo",
							JOptionPane.OK_OPTION);
					if (option == JOptionPane.OK_OPTION) {
						SwingUtilities.getWindowAncestor(thanhToan).dispose();
						new QuanLyDatPhong_GUI().setVisible(true);
					}
				} else {
					int option = JOptionPane.showConfirmDialog(null, "Bạn chưa chọn phương thức thanh toán",
							"Thông báo", JOptionPane.OK_OPTION);
				}

			}
		});
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(0, 0, 0));
		panel_4.setBounds(Math.round(258f * frameWidth / 1536f), Math.round(198f * frameHeight / 816f),
				Math.round(347f * frameWidth / 1536f), Math.round(2f * frameHeight / 816f));
		panel_2.add(panel_4);

		JLabel lblKhuynMi = new JLabel("Khuyến mãi:");
		lblKhuynMi.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		lblKhuynMi.setBounds(Math.round(25f * frameWidth / 1536f), Math.round(133f * frameHeight / 816f),
				Math.round(195f * frameWidth / 1536f), Math.round(39f * frameHeight / 816f));
		panel_2.add(lblKhuynMi);

		khuyenMai = new JComboBox();
		khuyenMai.setFont(new Font("Times New Roman", Font.BOLD,
				Math.round(20f * Math.min(frameWidth / 1536f, frameHeight / 816f))));
		khuyenMai.setBounds(Math.round(270f * frameWidth / 1536f), Math.round(140f * frameHeight / 816f),
				Math.round(321f * frameWidth / 1536f), Math.round(28f * frameHeight / 816f));
		khuyenMai.setBorder(BorderFactory.createEmptyBorder()); // Tắt viền
		khuyenMai.setBackground(null); // Tắt màu nền
		khuyenMai.setOpaque(false);
		panel_2.add(khuyenMai);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

		LocalDateTime dateTimeTra = LocalDateTime.parse(dayTraString, formatter);
		// Lấy ra phần ngày
		LocalDate ngayTra = dateTimeTra.toLocalDate();
		LocalDateTime dateTimeNhan = LocalDateTime.parse(dayNhanString, formatter);
		// Lấy ra phần ngày
		LocalDate ngayNhan = dateTimeNhan.toLocalDate();

		a = "0";
		// gắn một lần sau khi khởi tạo textfield
		tongChiPhi1.getDocument().addDocumentListener(new DocumentListener() {
			private String phanTramGiam;
			private double tienCoc;

			@Override
			public void insertUpdate(DocumentEvent e) {
				onChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				onChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				onChanged();
			} // HTML docs

			private void onChanged() {
				// luôn chạy trong EDT
				a = tongChiPhi1.getText().trim();
				System.out.println(a);
//		        // TODO: chuyển sang BigDecimal nếu cần	
				// chuẩn hoá -> double
				String clean = a.replaceAll("[^\\d,\\.]", "").replace(".", "").replace(",", ".");
				System.out.println(clean);
				tong = clean.isEmpty() ? 0.0 : Double.parseDouble(clean);
				System.out.println(tong);
				loadKhuyenMaiToComboBox(khuyenMai, ngayTra, ngayNhan, tong);
				tienCoc = Double.parseDouble(tienCoc1.getText());
				phanTramGiam = "0%";
				khuyenMai.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String selected = (String) khuyenMai.getSelectedItem();
						if (selected != null) {
							if (selected.equals("Không")) {
								phanTramGiam = "0%";
							} else if (selected.contains(" - ")) {
								String[] parts = selected.split(" - ");
								String maKhuyenMai = parts[0];
								phanTramGiam = parts[1]; // Gán vào biến toàn cục

							}
						}
//				        System.out.println("Phần trăm giảm: " + phanTramGiam);
//				        System.out.println("Tổng chi phí: " + tong);
//				        System.out.println("Tiền cọc: " + tienCoc);

						// Xử lý tính thành tiền nếu cần
						double tileGiam = Double.parseDouble(phanTramGiam.replace("%", "")) / 100.0;
						double thanhTien = tong - tienCoc - (tong * tileGiam);
						thanhTien1.setText(String.format("%.0f", thanhTien).replace(".0", ""));

					}

				});
			}
		});

		return panel;
	}

	// Giả sử bạn đã có khuyenMai là JComboBox
	public void loadKhuyenMaiToComboBox(JComboBox<String> comboBox, LocalDate ngayTra, LocalDate ngayNhan,
			double tongThanhToan) {
		KhuyenMai_DAO dao = new KhuyenMai_DAO();
		ArrayList<KhuyenMai> danhSachKM = dao.getKhuyenMaiTheoNgay(ngayNhan, ngayTra, tongThanhToan);

		comboBox.removeAllItems(); // Xóa các item cũ
//		comboBox.addItem("Không");
		for (KhuyenMai km : danhSachKM) {
			if (km.getMaKhuyenMai().equalsIgnoreCase("Không")) {
				comboBox.addItem("Không");
			} else {
				comboBox.addItem(km.getMaKhuyenMai() + " - " + km.getGiaTriKhuyenMai() + "%");
			}
		}
	}

	private String convertNumberToWords(int number) {
		String[] donVi = { "", "ngàn", "triệu", "tỷ" };
		String[] chuSo = { "không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín" };

		if (number == 0)
			return "không";

		ArrayList<Integer> groups = new ArrayList<>();
		while (number > 0) {
			groups.add(number % 1000);
			number /= 1000;
		}

		StringBuilder result = new StringBuilder();
		boolean hasPreviousGroup = false;

		for (int i = groups.size() - 1; i >= 0; i--) {
			int group = groups.get(i);
			boolean isFirstGroup = (i == groups.size() - 1);

			if (group != 0 || isFirstGroup) {
				String groupStr = readThreeDigits(group, chuSo, hasPreviousGroup, isFirstGroup);
				if (!groupStr.isEmpty()) {
					result.append(groupStr).append(" ");
					if (!donVi[i].isEmpty()) {
						result.append(donVi[i]).append(" ");
					}
					hasPreviousGroup = true;
				}
			}
		}

		return result.toString().trim().replaceAll("\\s+", " ");
	}

	private String readThreeDigits(int number, String[] chuSo, boolean hasPreviousGroup, boolean isFirstGroup) {
		int tram = number / 100;
		int chuc = (number % 100) / 10;
		int donvi = number % 10;

		StringBuilder sb = new StringBuilder();

		if (tram > 0) {
			sb.append(chuSo[tram]).append(" trăm ");
		} else if (hasPreviousGroup && (chuc > 0 || donvi > 0)) {
			sb.append("không trăm ");
		}

		if (chuc > 1) {
			sb.append(chuSo[chuc]).append(" mươi ");
			if (donvi == 1)
				sb.append("mốt ");
			else if (donvi == 5)
				sb.append("lăm ");
			else if (donvi > 0)
				sb.append(chuSo[donvi]).append(" ");
		} else if (chuc == 1) {
			sb.append("mười ");
			if (donvi == 5)
				sb.append("lăm ");
			else if (donvi > 0)
				sb.append(chuSo[donvi]).append(" ");
		} else if (donvi > 0) {
			// ❌ Chỉ thêm “lẻ” nếu KHÔNG phải nhóm cao nhất
			if (!isFirstGroup)
				sb.append("lẻ ");
			sb.append(chuSo[donvi]).append(" ");
		}

		return sb.toString().trim();
	}

	private void capNhatTongTienThanhToan() {
		double tongTien = 0;
		DefaultTableModel model = (DefaultTableModel) table1.getModel();
		int columnThanhTien = 4; // Cột "Thành tiền" là cột thứ 5 (chỉ số 4)

		for (int i = 0; i < model.getRowCount(); i++) {
			Object value = model.getValueAt(i, columnThanhTien);
			if (value != null && value instanceof Number) {
				tongTien += ((Number) value).doubleValue();
			} else if (value != null) {
				try {
					tongTien += Double.parseDouble(value.toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}

		// Cập nhật vào ô tổng tiền và định dạng đẹp nếu muốn
		tongTienPhongTable1.setText(String.format("%.0f", tongTien)); // Hiển thị kiểu ###,### nếu muốn
	}

	private void capNhatTongChiPhi() {
		try {
			double tienPhong = tongTienPhongTable1.getText().isEmpty() ? 0
					: Double.parseDouble(tongTienPhongTable1.getText());
			double tienDV = tongTienSuDungDichVu1.getText().isEmpty() ? 0
					: Double.parseDouble(tongTienSuDungDichVu1.getText());
			double tienPhatSinh = tongChiPhiPhatSinh1.getText().isEmpty() ? 0
					: Double.parseDouble(tongChiPhiPhatSinh1.getText());
			double tong = tienPhong + tienDV + tienPhatSinh;
			tongChiPhi1.setText(String.format("%.0f", tong));
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Lỗi định dạng số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void layDanhSachPhongDuocChon() {
		danhSachPhongDuocChon.clear(); // Xóa danh sách cũ
		if (table_phongTra != null) {
			DefaultTableModel model = (DefaultTableModel) table_phongTra.getModel();
			int rowCount = model.getRowCount();
			boolean phongDuocChon = false;
			for (int i = 0; i < rowCount; i++) {
				Boolean isSelected = (Boolean) model.getValueAt(i, 0);
				if (isSelected != null && isSelected) {
					phongDuocChon = true; // Đánh dấu là có phòng được chọn
					Object[] phong = new Object[model.getColumnCount() - 1];
					for (int j = 1; j < model.getColumnCount(); j++) {
						phong[j - 1] = model.getValueAt(i, j);
					}
					danhSachPhongDuocChon.add(phong);
				}
			}
			if (!phongDuocChon) {
				JOptionPane.showMessageDialog(null, "Bạn chưa chọn phòng để trả.", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Bảng phòng trống.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void capNhatHoaDonCT() {
		DefaultTableModel model_HoaDon = (DefaultTableModel) table1.getModel();
		model_HoaDon.setRowCount(0);
		for (Object[] row : danhSachPhongDuocChon) {
			model_HoaDon.addRow(row);
		}

		// Cập nhật các trường dữ liệu khác
		tongChiPhiPhatSinh1.setText(tongTienPS.getText().replace(".0", ""));
		tongTienSuDungDichVu1.setText(tongTienSDDV.getText());
		capNhatTongTienThanhToan();
		capNhatTongChiPhi();
//		capNhatThanhTien();
	}

//	private void capNhatThanhTien() {
//		try {
//			double tongChiPhi = Double.parseDouble(tongChiPhi1.getText());
//			double tienCoc = Double.parseDouble(tienCoc1.getText()); // Giả sử tienCoc1 là JTextField chứa tiền cọc
//			double thanhTien = tongChiPhi - tienCoc;
//			thanhTien1.setText(String.format("%.0f", thanhTien).replace(".0", ""));
//		} catch (NumberFormatException e) {
//			JOptionPane.showMessageDialog(null, "Lỗi định dạng số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//			thanhTien1.setText("0"); // Hoặc giá trị mặc định khác
//		}
//	}

	public static ArrayList<Object[]> getTableData(JTable table) {
		ArrayList<Object[]> data = new ArrayList<>();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int rowCount = model.getRowCount();
		int columnCount = model.getColumnCount();

		for (int row = 0; row < rowCount; row++) {
			Object[] rowData = new Object[columnCount];
			for (int col = 0; col < columnCount; col++) {
				rowData[col] = model.getValueAt(row, col);
			}
			data.add(rowData);
		}

		return data;
	}

	public static ArrayList<Object> getMaDichVu(JTable table) {
		ArrayList<Object> maDichVuList = new ArrayList<>();

		// Lấy model của bảng
		DefaultTableModel model = (DefaultTableModel) table.getModel();

		// Lấy số lượng dòng trong bảng
		int rowCount = model.getRowCount();

		// Duyệt qua từng dòng trong bảng
		for (int i = 0; i < rowCount; i++) {
			// Lấy giá trị cột 0 (Mã phiếu dịch vụ) và lưu vào danh sách
			Object maDichVu = model.getValueAt(i, 0); // Cột đầu tiên là mã dịch vụ
			maDichVuList.add(maDichVu);
		}

		return maDichVuList;
	}

	public ArrayList<String> layDanhSachMaPhong(JTable table) {
		ArrayList<String> danhSachMaPhong = new ArrayList<>();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int rowCount = model.getRowCount();

		for (int i = 0; i < rowCount; i++) {
			Object value = model.getValueAt(i, 0); // Cột 1: Mã phòng (sau checkbox)
			if (value != null) {
				danhSachMaPhong.add(value.toString().trim());
			}
		}

		return danhSachMaPhong;
	}

}