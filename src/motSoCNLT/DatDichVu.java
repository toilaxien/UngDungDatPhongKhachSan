package motSoCNLT;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import dao.ChiTietPhieuDichVu_DAO;
import dao.DichVu_Dao;
import dao.DonDatPhong_Dao;
import dao.PhieuDichVu_DAO;
import dao.Phong_Dao;
import entity.ChiTietPhieuDichVu;
import entity.DichVu;
import entity.DonDatPhong;
import entity.PhieuDichVu;
import entity.Phong;
import traPhong.panel_timKiem.UnderlineBorder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class DatDichVu extends JDialog {
	private JTextField hoVaTen;
	private JTextField sdt;
	private JTextField maPhong;
	private JTable table_MaPhong;

	private JPanel panel_TimTheoMaPhong;
	private JPanel panel_TimTheoKhachHang;
	private static final long serialVersionUID = 1L;
	// Dữ liệu gốc để lọc
	private String[][] originalData = {};
	private String[] columnNames = { "Mã phòng" };
	private JTable table_DonDatPhong;
	private JTextField maPhieu;
	private JTextField maDon;
	private JTextField mphong;
	private JTextField ngayLap;
	private JTable table_GoiMon;
	private JTable table_GiatUi;
	private JTextField tienGoiMon;
	private JTextField tienGiatUi;
	private JSpinner soLuong;
	private JTextField tienVe;
	private JTable table_DichVuKhac;
	private JTextField tienDichVuKhac;
	private JTextField thanhTien;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			JFrame aFrame= new JFrame();
			DatDichVu dialog = new DatDichVu(aFrame, true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DatDichVu(JFrame parent, Boolean modal) {
		super(parent, modal);
		getContentPane().setBackground(new Color(255, 255, 255));
		setBounds(100, 100, 1300, 770);
		getContentPane().setLayout(null);
		// Panel tìm theo mã phòng
		panel_TimTheoMaPhong = new JPanel();
		panel_TimTheoMaPhong.setBackground(new Color(255, 255, 255));
		panel_TimTheoMaPhong.setBounds(0, 0, 1286, 185);
		panel_TimTheoMaPhong.setLayout(null);
		panel_TimTheoMaPhong.setBorder(BorderFactory.createLineBorder(Color.black, 2)); // Viền xanh dày 2px

		getContentPane().add(panel_TimTheoMaPhong);

		maPhong = new JTextField("Nhập số phòng");
		maPhong.setFont(new Font("Times New Roman", Font.BOLD, 20));
		maPhong.setBounds(26, 71, 138, 30);
		maPhong.setColumns(10);
		maPhong.setOpaque(false); // tắt nền
		maPhong.setBorder(new UnderlineBorder());
		maPhong.setForeground(Color.GRAY); // màu placeholder

		// Xử lý placeholder
		maPhong.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				if (maPhong.getText().equals("Nhập số phòng:")) {
					maPhong.setText("");
					maPhong.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (maPhong.getText().isEmpty()) {
					maPhong.setText("Nhập số phòng:");
					maPhong.setForeground(Color.GRAY);
				}
			}
		});

		panel_TimTheoMaPhong.add(maPhong);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(174, 44, 191, 102);
		panel_TimTheoMaPhong.add(scrollPane);

		table_MaPhong = new JTable();
		table_MaPhong.setFont(new Font("Times New Roman", Font.BOLD, 18));
		table_MaPhong.setModel(new DefaultTableModel(originalData, columnNames) {
			boolean[] columnEditables = new boolean[] { false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_MaPhong.setRowHeight(21);
		table_MaPhong.setBackground(Color.WHITE);
		table_MaPhong.setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		table_MaPhong.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JTableHeader header = table_MaPhong.getTableHeader();
		header.setFont(new Font("Arial", Font.BOLD, 18));

		// Căn giữa nội dung trong bảng
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < table_MaPhong.getColumnCount(); i++) {
			table_MaPhong.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		// Căn giữa header
		DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table_MaPhong.getTableHeader()
				.getDefaultRenderer();
		headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		scrollPane.setViewportView(table_MaPhong);

		JButton xacNhan = new JButton("Xác nhận");
		xacNhan.setBackground(new Color(128, 255, 128));
		xacNhan.setFont(new Font("Times New Roman", Font.BOLD, 22));
        xacNhan.setOpaque(true);
        xacNhan.setContentAreaFilled(true);
        xacNhan.setBorderPainted(false);
		xacNhan.setBounds(230, 150, 138, 30);

		loadDanhSachMaPhong();
		panel_TimTheoMaPhong.add(xacNhan);

		JLabel lblNewLabel = new JLabel("Chọn phòng để đặt dịch vụ");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblNewLabel.setBackground(new Color(255, 255, 255));
		lblNewLabel.setBounds(80, 10, 261, 24);
		panel_TimTheoMaPhong.add(lblNewLabel);

		JLabel lblThngTinPhiu = new JLabel("Thông tin phiếu dịch vụ");
		lblThngTinPhiu.setFont(new Font("Times New Roman", Font.BOLD, 21));
		lblThngTinPhiu.setBackground(Color.WHITE);
		lblThngTinPhiu.setBounds(696, 10, 227, 24);
		panel_TimTheoMaPhong.add(lblThngTinPhiu);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 0, 0));
		panel.setBounds(406, 0, 1, 185);
		panel_TimTheoMaPhong.add(panel);

		JLabel lblMPhiuDch = new JLabel("Mã phiếu dịch vụ:");
		lblMPhiuDch.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblMPhiuDch.setBackground(Color.WHITE);
		lblMPhiuDch.setBounds(471, 63, 156, 24);
		panel_TimTheoMaPhong.add(lblMPhiuDch);

		JLabel lblMnt = new JLabel("Mã đơn đặt phòng:");
		lblMnt.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblMnt.setBackground(Color.WHITE);
		lblMnt.setBounds(471, 112, 170, 24);
		panel_TimTheoMaPhong.add(lblMnt);

		JLabel lblMPhng = new JLabel("Mã phòng:");
		lblMPhng.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblMPhng.setBackground(Color.WHITE);
		lblMPhng.setBounds(882, 63, 156, 24);
		panel_TimTheoMaPhong.add(lblMPhng);

		JLabel lblNgyLpPhiu = new JLabel("Ngày lập:");
		lblNgyLpPhiu.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNgyLpPhiu.setBackground(Color.WHITE);
		lblNgyLpPhiu.setBounds(882, 112, 83, 24);
		panel_TimTheoMaPhong.add(lblNgyLpPhiu);

		maPhieu = new JTextField();
		maPhieu.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		maPhieu.setEditable(false);
		maPhieu.setBounds(651, 57, 221, 30);
		panel_TimTheoMaPhong.add(maPhieu);
		maPhieu.setColumns(10);

		maDon = new JTextField();
		maDon.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		maDon.setEditable(false);
		maDon.setColumns(10);
		maDon.setBounds(651, 106, 221, 30);
		panel_TimTheoMaPhong.add(maDon);

		mphong = new JTextField();
		mphong.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		mphong.setEditable(false);
		mphong.setColumns(10);
		mphong.setBounds(996, 57, 234, 30);
		panel_TimTheoMaPhong.add(mphong);

		ngayLap = new JTextField();
		ngayLap.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		ngayLap.setEditable(false);
		ngayLap.setColumns(10);
		ngayLap.setBounds(996, 106, 234, 30);
		panel_TimTheoMaPhong.add(ngayLap);

		xacNhan.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn tạo phiếu dịch vụ không?",
						"Xác nhận", JOptionPane.YES_NO_OPTION);

				if (confirm != JOptionPane.YES_OPTION) {
					return; // Người dùng chọn "Không", không làm gì cả
				}
				String maPh = maPhong.getText();
				DonDatPhong_Dao donDatPhong_Dao = new DonDatPhong_Dao();
				DonDatPhong ddp = donDatPhong_Dao.getDonDatPhongTheoMaP(maPh);
				LocalDateTime ngayGioHienTai = LocalDateTime.now();
//				LocalDateTime ngayGioHienTai = LocalDateTime.now();
//				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
//				String ngayGioFormatted = ngayGioHienTai.format(formatter);

				String maDDP = ddp.getMaDonDatPhong();
				PhieuDichVu_DAO phieuDichVu_DAO = new PhieuDichVu_DAO();

				boolean daThem = false;
				int soThuTu = 1;

				while (!daThem && soThuTu <= 999) {
					String suffix = String.format("%03d", soThuTu); // tạo '001', '002',...
					String maP = maDDP + suffix;

					Boolean kiemTra = phieuDichVu_DAO.kiemTraTonTaiPhieuDichVu(maP);

					PhieuDichVu pdv = new PhieuDichVu(maP, ddp, ngayGioHienTai, "Chưa thanh toán");

					if (kiemTra) {
						soThuTu++;
					} else {
						daThem = phieuDichVu_DAO.themPhieuDichVu(pdv);
						maPhieu.setText(maP);
						maDon.setText(maDDP);
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
						String ngayGioFormatted = ngayGioHienTai.format(formatter);
						ngayLap.setText(ngayGioFormatted);
						mphong.setText(maPh);
						maPhong.setText("");
					}

				}

				if (!daThem) {
					System.out.println(
							"Không thể tạo phiếu dịch vụ, đã thử đến mã " + maDDP + String.format("%03d", soThuTu - 1));
				}

			}
		});

		JPanel panel_TimTheoMaPhong_1 = new JPanel();
		panel_TimTheoMaPhong_1.setBackground(Color.WHITE);
		panel_TimTheoMaPhong_1.setBounds(0, 183, 1286, 552);
		panel_TimTheoMaPhong_1.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		getContentPane().add(panel_TimTheoMaPhong_1);
		panel_TimTheoMaPhong_1.setLayout(null);

		JLabel lblCcDchV = new JLabel("Các dịch vụ của khách sạn");
		lblCcDchV.setFont(new Font("Times New Roman", Font.BOLD, 25));
		lblCcDchV.setBackground(Color.WHITE);
		lblCcDchV.setBounds(506, 10, 305, 35);
		panel_TimTheoMaPhong_1.add(lblCcDchV);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBounds(10, 42, 413, 494);
		Border border1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
				"Dịch vụ gọi món", TitledBorder.LEFT, TitledBorder.TOP, new Font("Times New Roman", Font.BOLD, 16),
				Color.BLACK);
		panel_1.setBorder(border1);
		panel_TimTheoMaPhong_1.add(panel_1);
		panel_1.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 26, 393, 405);
		panel_1.add(scrollPane_1);

		table_GoiMon = new JTable();
		table_GoiMon.setModel(new DefaultTableModel(new Object[][] { { null, null, null, null }, },
				new String[] { "M\u00E3 d\u1ECBch v\u1EE5", "T\u00EAn d\u1ECBch v\u1EE5", "\u0110\u01A1n gi\u00E1",
						"S\u1ED1 l\u01B0\u1EE3ng" }) {
			boolean[] columnEditables = new boolean[] { false, false, false, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_GoiMon.getColumnModel().getColumn(0).setResizable(false);
		table_GoiMon.getColumnModel().getColumn(0).setPreferredWidth(90);
		table_GoiMon.getColumnModel().getColumn(1).setPreferredWidth(140);
		table_GoiMon.getColumnModel().getColumn(2).setPreferredWidth(80);
		table_GoiMon.getColumnModel().getColumn(3).setPreferredWidth(70);
		table_GoiMon.setRowHeight(40);
		table_GoiMon.getColumnModel().getColumn(3).setCellRenderer(new SoLuongEditorRenderer(table_GoiMon));
		table_GoiMon.getColumnModel().getColumn(3).setCellEditor(new SoLuongEditorRenderer(table_GoiMon));

		table_GoiMon.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		scrollPane_1.setViewportView(table_GoiMon);

		JLabel lblTngTinGi = new JLabel("Tổng tiền gọi món:");
		lblTngTinGi.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblTngTinGi.setBackground(Color.WHITE);
		lblTngTinGi.setBounds(75, 447, 160, 24);
		panel_1.add(lblTngTinGi);

		tienGoiMon = new JTextField();
		tienGoiMon.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		tienGoiMon.setEditable(false);
		tienGoiMon.setColumns(10);
		tienGoiMon.setBounds(236, 441, 167, 30);
		panel_1.add(tienGoiMon);
		DefaultTableModel model_GoiMon = (DefaultTableModel) table_GoiMon.getModel();
		model_GoiMon.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) { // chỉ cột số lượng
					tinhTongTienGoiMon();
				}
			}
		});
		loadDichVuTheoLoaiVaoTable("GoiMon", table_GoiMon);

		JPanel panel_1_1 = new JPanel();
		panel_1_1.setBackground(new Color(255, 255, 255));
		panel_1_1.setBounds(435, 42, 413, 494);
		Border border2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
				"Dịch vụ giặt ủi ", TitledBorder.LEFT, TitledBorder.TOP, new Font("Times New Roman", Font.BOLD, 16),
				Color.BLACK);
		panel_1_1.setBorder(border2);
		panel_TimTheoMaPhong_1.add(panel_1_1);
		panel_1_1.setLayout(null);

		JScrollPane scrollPane_1_1 = new JScrollPane();
		scrollPane_1_1.setBounds(10, 26, 393, 407);
		panel_1_1.add(scrollPane_1_1);

		table_GiatUi = new JTable();
		table_GiatUi.setModel(new DefaultTableModel(new Object[][] { { null, null, null, null }, },
				new String[] { "M\u00E3 d\u1ECBch v\u1EE5", "T\u00EAn d\u1ECBch v\u1EE5", "\u0110\u01A1n gi\u00E1",
						"S\u1ED1 l\u01B0\u1EE3ng" }) {
			boolean[] columnEditables = new boolean[] { false, false, false, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_GiatUi.getColumnModel().getColumn(0).setResizable(false);
		table_GiatUi.getColumnModel().getColumn(0).setPreferredWidth(120);
		table_GiatUi.getColumnModel().getColumn(1).setPreferredWidth(130);
		table_GiatUi.getColumnModel().getColumn(2).setPreferredWidth(80);
		table_GiatUi.getColumnModel().getColumn(3).setPreferredWidth(70);
		table_GiatUi.setRowHeight(40);
		table_GiatUi.getColumnModel().getColumn(3).setCellRenderer(new SoLuongEditorRenderer(table_GiatUi));
		table_GiatUi.getColumnModel().getColumn(3).setCellEditor(new SoLuongEditorRenderer(table_GiatUi));

		table_GiatUi.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		scrollPane_1_1.setViewportView(table_GiatUi);
		loadDichVuTheoLoaiVaoTable("GiatUi", table_GiatUi);

		JLabel lblTngTinGit = new JLabel("Tổng tiền giặt ủi:");
		lblTngTinGit.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblTngTinGit.setBackground(Color.WHITE);
		lblTngTinGit.setBounds(75, 449, 160, 24);
		panel_1_1.add(lblTngTinGit);

		tienGiatUi = new JTextField();
		tienGiatUi.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		tienGiatUi.setEditable(false);
		tienGiatUi.setColumns(10);
		tienGiatUi.setBounds(236, 443, 167, 30);
		DefaultTableModel model_GiatUi = (DefaultTableModel) table_GiatUi.getModel();
		model_GiatUi.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) { // chỉ cột số lượng
					tinhTongTienGiatUi();
				}
			}
		});
		panel_1_1.add(tienGiatUi);

		JPanel panel_1_1_1 = new JPanel();
		panel_1_1_1.setBackground(new Color(255, 255, 255));
		panel_1_1_1.setBounds(863, 42, 413, 127);
		Border border3 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Dịch vụ buffet",
				TitledBorder.LEFT, TitledBorder.TOP, new Font("Times New Roman", Font.BOLD, 16), Color.BLACK);
		panel_1_1_1.setBorder(border3);
		panel_TimTheoMaPhong_1.add(panel_1_1_1);
		panel_1_1_1.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Vé Buffet sáng:");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1.setBounds(57, 37, 143, 24);
		panel_1_1_1.add(lblNewLabel_1);

		soLuong = new JSpinner(new SpinnerNumberModel(0, 0, 30, 1));
		soLuong.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		soLuong.setBounds(210, 27, 55, 30);
		panel_1_1_1.add(soLuong);

		JLabel lblNewLabel_1_1 = new JLabel("Tổng tiền vé:");
		lblNewLabel_1_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1_1.setBounds(57, 71, 143, 24);
		panel_1_1_1.add(lblNewLabel_1_1);

		tienVe = new JTextField();
		tienVe.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		tienVe.setEditable(false);
		tienVe.setColumns(10);
		tienVe.setBounds(210, 67, 179, 30);
		panel_1_1_1.add(tienVe);
		soLuong.addChangeListener(e -> {
			int sl = (int) soLuong.getValue(); // Lấy số lượng
			int tongTien = sl * 150000; // Tính tiền vé
			DecimalFormat df = new DecimalFormat("#");
			tienVe.setText(df.format(tongTien)); // Hiển thị có dấu phân cách nếu muốn
			capNhatThanhTien();
		});

		JPanel panel_1_1_1_1 = new JPanel();
		panel_1_1_1_1.setBackground(new Color(255, 255, 255));
		panel_1_1_1_1.setBounds(863, 179, 413, 232);
		Border border4 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Dịch vụ khác",
				TitledBorder.LEFT, TitledBorder.TOP, new Font("Times New Roman", Font.BOLD, 16), Color.BLACK);
		panel_1_1_1_1.setBorder(border4);
		panel_TimTheoMaPhong_1.add(panel_1_1_1_1);
		panel_1_1_1_1.setLayout(null);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 20, 393, 151);
		panel_1_1_1_1.add(scrollPane_2);

		table_DichVuKhac = new JTable();
		table_DichVuKhac.setModel(new DefaultTableModel(new Object[][] { { null, null, null, null }, },
				new String[] { "M\u00E3 d\u1ECBch v\u1EE5", "T\u00EAn d\u1ECBch v\u1EE5", "\u0110\u01A1n gi\u00E1",
						"S\u1ED1 l\u01B0\u1EE3ng" }) {
			boolean[] columnEditables = new boolean[] { false, false, false, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_DichVuKhac.getColumnModel().getColumn(0).setResizable(false);
		table_DichVuKhac.getColumnModel().getColumn(0).setPreferredWidth(120);
		table_DichVuKhac.getColumnModel().getColumn(1).setPreferredWidth(130);
		table_DichVuKhac.getColumnModel().getColumn(2).setPreferredWidth(80);
		table_DichVuKhac.getColumnModel().getColumn(3).setPreferredWidth(70);
		table_DichVuKhac.setRowHeight(40);
		table_DichVuKhac.getColumnModel().getColumn(3).setCellRenderer(new SoLuongEditorRenderer(table_DichVuKhac));
		table_DichVuKhac.getColumnModel().getColumn(3).setCellEditor(new SoLuongEditorRenderer(table_DichVuKhac));

		table_DichVuKhac.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		scrollPane_2.setViewportView(table_DichVuKhac);
		loadDichVuTheoLoaiVaoTable("DichVuKhac", table_DichVuKhac);

		JLabel lblTngTinDch = new JLabel("Tổng tiền dịch vụ khác:");
		lblTngTinDch.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblTngTinDch.setBackground(Color.WHITE);
		lblTngTinDch.setBounds(20, 187, 215, 24);
		panel_1_1_1_1.add(lblTngTinDch);

		tienDichVuKhac = new JTextField();
		tienDichVuKhac.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		tienDichVuKhac.setEditable(false);
		tienDichVuKhac.setColumns(10);
		tienDichVuKhac.setBounds(236, 181, 167, 30);
		DefaultTableModel model_DVKhac = (DefaultTableModel) table_DichVuKhac.getModel();
		model_DVKhac.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) { // chỉ cột số lượng
					tinhTongTienDVKhac();
				}
			}
		});
		panel_1_1_1_1.add(tienDichVuKhac);

		JButton xacNhanDat = new JButton("Xác nhận đặt");
		xacNhanDat.setFont(new Font("Times New Roman", Font.BOLD, 22));
		xacNhanDat.setBackground(new Color(128, 255, 128));
        xacNhanDat.setOpaque(true);
        xacNhanDat.setContentAreaFilled(true);
        xacNhanDat.setBorderPainted(false);
		xacNhanDat.setBounds(1074, 486, 172, 35);

		panel_TimTheoMaPhong_1.add(xacNhanDat);

		JLabel lblTngThanhTon = new JLabel("Thành tiền:");
		lblTngThanhTon.setFont(new Font("Times New Roman", Font.BOLD, 25));
		lblTngThanhTon.setBackground(Color.WHITE);
		lblTngThanhTon.setBounds(873, 431, 142, 35);
		panel_TimTheoMaPhong_1.add(lblTngThanhTon);

		thanhTien = new JTextField();
		thanhTien.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		thanhTien.setEditable(false);
		thanhTien.setColumns(10);
		thanhTien.setBounds(1025, 435, 221, 30);
		panel_TimTheoMaPhong_1.add(thanhTien);

		xacNhanDat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String mp = maPhieu.getText().trim(); // lấy mã phiếu đã tạo

				// Duyệt qua từng bảng dịch vụ
				boolean thanhCong = true;

				try {
					xuLyChiTietPhieuDichVu(table_DichVuKhac, maPhieu.getText());
					xuLyChiTietPhieuDichVu(table_GoiMon, maPhieu.getText());
					xuLyChiTietPhieuDichVu(table_GiatUi, maPhieu.getText());

					int soL = (Integer) soLuong.getValue();
					if (soL > 0) {
						PhieuDichVu pDichVu = new PhieuDichVu(maPhieu.getText());
						DichVu dVu = new DichVu("DVBuffet");
						ChiTietPhieuDichVu ctpdv = new ChiTietPhieuDichVu(pDichVu, dVu, soL);
						ChiTietPhieuDichVu_DAO chiTietPhieuDichVu_DAO = new ChiTietPhieuDichVu_DAO();
						chiTietPhieuDichVu_DAO.them(ctpdv);
					}
				} catch (Exception exp) {
					thanhCong = false;
					exp.printStackTrace(); // hoặc xử lý lỗi phù hợp
				}

				// Nếu thành công, reset bảng + spinner
				if (thanhCong) {
					loadDichVuTheoLoaiVaoTable("GoiMon", table_GoiMon);
					loadDichVuTheoLoaiVaoTable("GiatUi", table_GiatUi);
					loadDichVuTheoLoaiVaoTable("DichVuKhac", table_DichVuKhac);
					maDon.setText("");
					maPhieu.setText("");
					ngayLap.setText("");
					mphong.setText("");
					tienVe.setText("");
					tienDichVuKhac.setText("");
					tienGoiMon.setText("");
					tienGiatUi.setText("");
					thanhTien.setText("");
					soLuong.setValue(0);
					JOptionPane.showMessageDialog(null, "Đặt dịch vụ thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});

		// Lọc dữ liệu khi nhập vào JTextField
		maPhong.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				update();
			}

			public void removeUpdate(DocumentEvent e) {
				update();
			}

			public void changedUpdate(DocumentEvent e) {
				update();
			}

			private void update() {
				String keyword = maPhong.getText().trim().toLowerCase();
				DefaultTableModel model = (DefaultTableModel) table_MaPhong.getModel();
				model.setRowCount(0); // Xóa dữ liệu cũ
				for (String[] row : originalData) {
					if (row[0].toLowerCase().startsWith(keyword)) {
						model.addRow(new Object[] { row[0] });
					}
				}
			}
		});

		// Nếu người dùng nhập mã không đúng → xóa
		maPhong.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String input = maPhong.getText().trim();
				boolean found = false;
				for (String[] row : originalData) {
					if (row[0].equalsIgnoreCase(input)) {
						found = true;
						break;
					}
				}

			}
		});

		// Chọn dòng trong bảng để cập nhật mã phòng
		table_MaPhong.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table_MaPhong.getSelectedRow();
				if (row != -1) {
					String selected = table_MaPhong.getValueAt(row, 0).toString();
					maPhong.setText(selected);
				}
			}
		});
		setLocationRelativeTo(null);
		setResizable(false);
	}

	public class UnderlineBorder implements Border {
		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(0, 0, 5, 0);
		}

		@Override
		public boolean isBorderOpaque() {
			return false;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			g.setColor(new Color(180, 200, 220)); // màu gạch dưới
			g.drawLine(x, height - 2, x + width, height - 2);
		}
	}

	private void loadDanhSachMaPhong() {
		Phong_Dao phongDAO = new Phong_Dao(); // Nhớ đã có import dao.PhongDAO;
		ArrayList<Phong> danhSach = phongDAO.getPhongChuaThanhToan();

		// Cập nhật originalData
		originalData = new String[danhSach.size()][1];
		for (int i = 0; i < danhSach.size(); i++) {
			originalData[i][0] = danhSach.get(i).getSoPhong();
		}

		// Cập nhật vào JTable
		DefaultTableModel model = (DefaultTableModel) table_MaPhong.getModel();
		model.setRowCount(0); // Xóa dữ liệu cũ
		for (String[] row : originalData) {
			model.addRow(row);
		}
	}

	private void tinhTongTienGoiMon() {
		DefaultTableModel model = (DefaultTableModel) table_GoiMon.getModel();
		double tong = 0.0;

		for (int i = 0; i < model.getRowCount(); i++) {
			Object donGiaObj = model.getValueAt(i, 2); // Đơn giá
			Object soLuongObj = model.getValueAt(i, 3); // Số lượng

			if (donGiaObj != null && soLuongObj != null) {
				try {
					double donGia = Double.parseDouble(donGiaObj.toString());
					int soLuong = Integer.parseInt(soLuongObj.toString());
					tong += donGia * soLuong;
				} catch (NumberFormatException e) {
					// Bỏ qua dòng có dữ liệu không hợp lệ
				}
			}
		}

		tienGoiMon.setText(String.format("%.0f", tong));
		capNhatThanhTien();
	}

	private void tinhTongTienGiatUi() {
		DefaultTableModel model = (DefaultTableModel) table_GiatUi.getModel();
		double tong = 0.0;

		for (int i = 0; i < model.getRowCount(); i++) {
			Object donGiaObj = model.getValueAt(i, 2); // Đơn giá
			Object soLuongObj = model.getValueAt(i, 3); // Số lượng

			if (donGiaObj != null && soLuongObj != null) {
				try {
					double donGia = Double.parseDouble(donGiaObj.toString());
					int soLuong = Integer.parseInt(soLuongObj.toString());
					tong += donGia * soLuong;
				} catch (NumberFormatException e) {
					// Bỏ qua dòng có dữ liệu không hợp lệ
				}
			}
		}

		tienGiatUi.setText(String.format("%.0f", tong));
		capNhatThanhTien();
	}

	private void tinhTongTienDVKhac() {
		DefaultTableModel model = (DefaultTableModel) table_DichVuKhac.getModel();
		double tong = 0.0;

		for (int i = 0; i < model.getRowCount(); i++) {
			Object donGiaObj = model.getValueAt(i, 2); // Đơn giá
			Object soLuongObj = model.getValueAt(i, 3); // Số lượng

			if (donGiaObj != null && soLuongObj != null) {
				try {
					double donGia = Double.parseDouble(donGiaObj.toString());
					int soLuong = Integer.parseInt(soLuongObj.toString());
					tong += donGia * soLuong;
				} catch (NumberFormatException e) {
					// Bỏ qua dòng có dữ liệu không hợp lệ
				}
			}
		}

		tienDichVuKhac.setText(String.format("%.0f", tong));
		capNhatThanhTien();
	}

	class SoLuongEditorRenderer extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
		private JPanel panel = new JPanel(new BorderLayout());
		private JTextField txtSoLuong = new JTextField("0");

		private JButton btnPlus = new JButton("+");
		private JButton btnMinus = new JButton("−");
		private JPanel panelButtons = new JPanel(new GridLayout(2, 1, 0, 2));

		private int currentRow;
		private JTable table;

		public SoLuongEditorRenderer(JTable table) {
			this.table = table;

			txtSoLuong.setHorizontalAlignment(SwingConstants.CENTER);
			txtSoLuong.setPreferredSize(new Dimension(40, 30));
			txtSoLuong.setFont(txtSoLuong.getFont().deriveFont(16f));

			btnPlus.setMargin(new Insets(0, 5, 0, 5));
			btnMinus.setMargin(new Insets(0, 5, 0, 5));

			panelButtons.add(btnPlus);
			panelButtons.add(btnMinus);

			panel.add(txtSoLuong, BorderLayout.CENTER);
			panel.add(panelButtons, BorderLayout.EAST);

			btnPlus.addActionListener(e -> {
				int sl = parseInt(txtSoLuong.getText());
				sl++;
				txtSoLuong.setText(String.valueOf(sl));
				table.setValueAt(sl, currentRow, 3);
				fireEditingStopped();
			});

			btnMinus.addActionListener(e -> {
				int sl = parseInt(txtSoLuong.getText());
				if (sl > 0) {
					sl--;
					txtSoLuong.setText(String.valueOf(sl));
					table.setValueAt(sl, currentRow, 3);
					fireEditingStopped();
				}
			});

			// Optional: cập nhật khi người dùng nhập tay và nhấn Enter
			txtSoLuong.addActionListener(e -> {
				int sl = parseInt(txtSoLuong.getText());
				table.setValueAt(sl, currentRow, 3);
				fireEditingStopped();
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			currentRow = row;
			txtSoLuong.setText(value != null ? value.toString() : "0");
			return panel;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			txtSoLuong.setText(value != null ? value.toString() : "0");
			return panel;
		}

		@Override
		public Object getCellEditorValue() {
			return parseInt(txtSoLuong.getText());
		}

		private int parseInt(String text) {
			try {
				return Integer.parseInt(text.trim());
			} catch (NumberFormatException e) {
				return 0;
			}
		}
	}

	public void loadDichVuTheoLoaiVaoTable(String tenLoai, JTable table) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0); // Xoá toàn bộ dòng cũ

		DichVu_Dao dao = new DichVu_Dao();
		ArrayList<DichVu> ds = dao.getDichVuTheoLoai(tenLoai);

		for (DichVu dv : ds) {
			Object[] rowData = { dv.getMaDV(), dv.getTenDV(), dv.getGiaDV(), 0 // Số lượng mặc định
			};
			model.addRow(rowData);
		}
	}

	private void capNhatThanhTien() {
		int tong = 0;
		try {
			tong += Integer.parseInt(tienGoiMon.getText().replace(",", ""));
		} catch (Exception e) {
		}
		try {
			tong += Integer.parseInt(tienGiatUi.getText().replace(",", ""));
		} catch (Exception e) {
		}
		try {
			tong += Integer.parseInt(tienVe.getText().replace(",", ""));
		} catch (Exception e) {
		}
		try {
			tong += Integer.parseInt(tienDichVuKhac.getText().replace(",", ""));
		} catch (Exception e) {
		}

		DecimalFormat df = new DecimalFormat("#");
		thanhTien.setText(df.format(tong));
	}

	public void xuLyChiTietPhieuDichVu(JTable table, String maPhieu) {
		for (int i = 0; i < table.getRowCount(); i++) {
			Object soLuongObj = table.getValueAt(i, 3);
			if (soLuongObj == null)
				continue;

			int soLuong = 0;
			try {
				soLuong = Integer.parseInt(soLuongObj.toString());
			} catch (NumberFormatException ex) {
				continue;
			}

			if (soLuong > 0) {
				String maDV = table.getValueAt(i, 0).toString();
				double donGia = Double.parseDouble(table.getValueAt(i, 2).toString()); // cột đơn giá
				// Gọi DAO thêm chi tiết phiếu dịch vụ
				PhieuDichVu pDichVu = new PhieuDichVu(maPhieu);
				DichVu dVu = new DichVu(maDV);
				ChiTietPhieuDichVu ctpdv = new ChiTietPhieuDichVu(pDichVu, dVu,
						Integer.parseInt(soLuongObj.toString()));
				ChiTietPhieuDichVu_DAO chiTietPhieuDichVu_DAO = new ChiTietPhieuDichVu_DAO();
				chiTietPhieuDichVu_DAO.them(ctpdv);
			}
		}
	}

}
