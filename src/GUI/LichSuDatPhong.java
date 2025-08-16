package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import dao.ChiTietApDung_DAO;
import dao.DonDatPhong_Dao;
import dao.Phong_Dao;
import entity.ChiTietApDung;
import entity.DonDatPhong;
import entity.KhachHang;
import entity.Phong;

import javax.swing.JTextField;

public class LichSuDatPhong extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JTextField hoVaTen;
	private JTextField SDT;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			JFrame aFrame= new JFrame();
			LichSuDatPhong dialog = new LichSuDatPhong(aFrame, true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public LichSuDatPhong(JFrame parent, Boolean modal) {
		super(parent, modal);
		setBounds(100, 100, 1084, 636);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 255, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Lịch sử đặt phòng");
			lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 28));
			lblNewLabel.setBounds(412, 10, 241, 41);
			contentPanel.add(lblNewLabel);
		}

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(10, 246, 1050, 343);
		contentPanel.add(panel);
		panel.setLayout(null);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		JLabel lblCcnt = new JLabel("Các đơn đặt phòng đã thanh toán");
		lblCcnt.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblCcnt.setBounds(342, 10, 350, 41);
		panel.add(lblCcnt);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 63, 1030, 270);
		panel.add(scrollPane);

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] { { null, null, null, null, null, null }, },
				new String[] { "M\u00E3 \u0111\u01A1n ", "H\u1ECD v\u00E0 t\u00EAn kh\u00E1ch h\u00E0ng",
						" S\u1ED1 \u0111i\u1EC7n tho\u1EA1i", "Ng\u00E0y tr\u1EA3 ph\u00F2ng", "T\u1ED5ng ti\u1EC1n",
						"" }) {
			boolean[] columnEditables = new boolean[] { true, false, false, false, false, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(130);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setResizable(false);
		table.getColumnModel().getColumn(3).setPreferredWidth(118);
		table.getColumnModel().getColumn(4).setResizable(false);
		table.getColumnModel().getColumn(4).setPreferredWidth(112);
		table.getColumnModel().getColumn(5).setResizable(false);
		table.getColumnModel().getColumn(5).setPreferredWidth(30);
		table.setFont(new Font("Times New Roman", Font.PLAIN, 18));

		table.setRowHeight(26);
		table.setBackground(Color.WHITE);
		table.setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JTableHeader header = table.getTableHeader();
		header.setFont(new Font("Times New Roman", Font.BOLD, 18));
		header.setBackground(new Color(220, 255, 220));
		// Căn giữa nội dung trong bảng
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		// Gán renderer và editor cho cột cuối cùng (cột "Chi tiết")
		table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
		table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), table));

		// Căn giữa header
		DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader()
				.getDefaultRenderer();
		headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setViewportView(table);

		JButton taiLai = new JButton("Tải lại");
		taiLai.setFont(new Font("Times New Roman", Font.BOLD, 22));
		taiLai.setBackground(new Color(128, 255, 128));
        taiLai.setBackground(new Color(0, 255, 128));
        taiLai.setOpaque(true);
        taiLai.setContentAreaFilled(true);
        taiLai.setBorderPainted(false);
		taiLai.setBounds(933, 10, 107, 41);
		taiLai.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.setRowCount(0); // Xóa dữ liệu cũ
				DonDatPhong_Dao aDao = new DonDatPhong_Dao();
				ArrayList<DonDatPhong> danhSach = aDao.getDonDatPhongDaThanhToan(); // Lấy danh sách đơn đã thanh toán

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

				for (DonDatPhong ddp : danhSach) {
					KhachHang kh = ddp.getKhachHang();
					String maDon = ddp.getMaDonDatPhong();
					String hoTen = (kh != null && kh.getHoTen() != null) ? kh.getHoTen() : "";
					String sdt = (kh != null && kh.getSdt() != null) ? kh.getSdt() : "";
					String ngayTra = ddp.getNgayTraPhong() != null ? ddp.getNgayTraPhong().format(formatter) : "";

					double tongTien = 0.0;
					ChiTietApDung_DAO ctap = new ChiTietApDung_DAO();
					ArrayList<ChiTietApDung> apDungs = ctap.getDanhSachChiTietApDungTheoMaDon(ddp.getMaDonDatPhong());
					for (ChiTietApDung ct : apDungs) {
						tongTien += ct.getTongThanhToanSauApDung();
					}
					DecimalFormat df = new DecimalFormat("#,###.##");
					String tongTienFormatted = df.format(tongTien);
					model.addRow(new Object[] { maDon, hoTen, sdt, ngayTra, tongTienFormatted, "Chi tiết" });
				}
			}
		});

		panel.add(taiLai);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBounds(10, 48, 538, 192);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);
		panel_1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2),
				"Tìm kiếm đơn đặt phòng", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16),
				Color.DARK_GRAY));

		JLabel lblHVTn = new JLabel("Họ và tên:");
		lblHVTn.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblHVTn.setBounds(25, 40, 130, 41);
		panel_1.add(lblHVTn);

		JLabel lblSinThoi = new JLabel("Số điện thoại:");
		lblSinThoi.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblSinThoi.setBounds(25, 91, 156, 41);
		panel_1.add(lblSinThoi);

		hoVaTen = new JTextField();
		hoVaTen.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		hoVaTen.setBounds(186, 40, 286, 34);
		panel_1.add(hoVaTen);
		hoVaTen.setColumns(10);

		SDT = new JTextField();
		SDT.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		SDT.setColumns(10);
		SDT.setBounds(186, 91, 286, 34);
		panel_1.add(SDT);

		JButton tim = new JButton("Tìm");
		tim.setBackground(new Color(128, 255, 128));
		tim.setFont(new Font("Times New Roman", Font.BOLD, 22));
        tim.setBackground(new Color(0, 255, 128));
        tim.setOpaque(true);
        tim.setContentAreaFilled(true);
        tim.setBorderPainted(false);
		tim.setBounds(398, 135, 74, 32);

		tim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ten = hoVaTen.getText().trim();
				String sdt = SDT.getText().trim();

				// Kiểm tra nếu tên hoặc số điện thoại rỗng
				if (ten.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Vui lòng nhập họ và tên.");
					hoVaTen.requestFocus();
					return;
				}
				if (sdt.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Vui lòng nhập số điện thoại.");
					SDT.requestFocus();
					return;
				}

				DonDatPhong_Dao dao = new DonDatPhong_Dao();
				ArrayList<DonDatPhong> danhSach = dao.getDonDatPhongDaThanhToanTheoTenVaSĐT(ten, sdt);

				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.setRowCount(0); // Xóa dữ liệu cũ

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
				for (DonDatPhong ddp : danhSach) {
					KhachHang kh = ddp.getKhachHang();
					String maDon = ddp.getMaDonDatPhong();
					String hoTen = (kh != null && kh.getHoTen() != null) ? kh.getHoTen() : "";
					String sdt1 = (kh != null && kh.getSdt() != null) ? kh.getSdt() : "";
					String ngayTra = ddp.getNgayTraPhong() != null ? ddp.getNgayTraPhong().format(formatter) : "";
					double tongTien = 0.0;
					ChiTietApDung_DAO ctap = new ChiTietApDung_DAO();
					ArrayList<ChiTietApDung> apDungs = ctap.getDanhSachChiTietApDungTheoMaDon(ddp.getMaDonDatPhong());
					for (ChiTietApDung ct : apDungs) {
						tongTien += ct.getTongThanhToanSauApDung();
					}

					DecimalFormat df = new DecimalFormat("#,###.##");
					String tongTienFormatted = df.format(tongTien);

					model.addRow(new Object[] { maDon, hoTen, sdt1, ngayTra, tongTienFormatted, "Chi tiết" });
				}
				hoVaTen.setText("");
				SDT.setText("");
			}
		});

		panel_1.add(tim);
		loadDataToTable();
		setLocationRelativeTo(null);
		setResizable(false);

	}

	class ButtonRenderer extends JButton implements TableCellRenderer {
		public ButtonRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			setText((value == null) ? "Xem" : value.toString());
			return this;
		}
	}

	class ButtonEditor extends DefaultCellEditor {
		private JButton button;
		private boolean isPushed;
		private JTable table;

		public ButtonEditor(JCheckBox checkBox, JTable table) {
			super(checkBox);
			this.table = table;
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(e -> fireEditingStopped());
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			button.setText((value == null) ? "Chi tiết" : value.toString());
			isPushed = true;
			return button;
		}

		@Override
		public Object getCellEditorValue() {
			if (isPushed) {
				int row = table.getSelectedRow();
				if (row >= 0) {
					String maDon = table.getValueAt(row, 0).toString();
					DonDatPhong_Dao dao = new DonDatPhong_Dao();
					DonDatPhong ddp = dao.getDonDatPhongTheoMa(maDon);
					ChiTietApDung_DAO ctap = new ChiTietApDung_DAO();
					Phong_Dao phong_DAO = new Phong_Dao();
					ArrayList<ChiTietApDung> apDungs = ctap.getDanhSachChiTietApDungTheoMaDon(maDon);

					ArrayList<Phong> phong = phong_DAO.getPhongTheoMaDonDatPhong1(maDon);
					String maPhongStr = phong.stream().map(Phong::getSoPhong).collect(Collectors.joining(", "));

					StringBuilder dsMaKhuyenMai = new StringBuilder();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
					String ngayTra = ddp.getNgayTraPhong() != null ? ddp.getNgayTraPhong().format(formatter) : "";
					String ngayDat = ddp.getNgayDatPhong() != null ? ddp.getNgayDatPhong().format(formatter) : "";
					String ngayNhan = ddp.getNgayNhanPhong() != null ? ddp.getNgayNhanPhong().format(formatter) : "";
					String tongTien = table.getValueAt(row, 4).toString();
					for (ChiTietApDung ct : apDungs) {
						if (dsMaKhuyenMai.length() > 0) {
							dsMaKhuyenMai.append(", ");
						}
						dsMaKhuyenMai.append(ct.getMaKhuyenMai());
					}
					String maKhuyenMaiGop = dsMaKhuyenMai.toString();

					// Mở dialog chi tiết
					ThanhToanChiTiet dialog = new ThanhToanChiTiet();
					dialog.setThongTinDatPhong(maDon, ddp.getKhachHang().getHoTen(), ddp.getKhachHang().getSdt(),
							maPhongStr, ngayDat, ngayNhan, ngayTra, maKhuyenMaiGop, tongTien);
					dialog.setModal(true);
					dialog.setVisible(true);
				}
			}
			isPushed = false;
			return button.getText();
		}

		@Override
		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}
	}

	public void loadDataToTable() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0); // Xóa dữ liệu cũ
		DonDatPhong_Dao aDao = new DonDatPhong_Dao();
		ArrayList<DonDatPhong> danhSach = aDao.getDonDatPhongDaThanhToan();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

		for (DonDatPhong ddp : danhSach) {
			KhachHang kh = ddp.getKhachHang();
			String maDon = ddp.getMaDonDatPhong();
			String hoTen = (kh != null && kh.getHoTen() != null) ? kh.getHoTen() : "";
			String sdt = (kh != null && kh.getSdt() != null) ? kh.getSdt() : "";
			String ngayTra = ddp.getNgayTraPhong() != null ? ddp.getNgayTraPhong().format(formatter) : "";
			double tongTien = 0.0;
			ChiTietApDung_DAO ctap = new ChiTietApDung_DAO();
			ArrayList<ChiTietApDung> apDungs = ctap.getDanhSachChiTietApDungTheoMaDon(ddp.getMaDonDatPhong());
			for (ChiTietApDung ct : apDungs) {
				tongTien += ct.getTongThanhToanSauApDung();
			}
			DecimalFormat df = new DecimalFormat("#,###.##");
			String tongTienFormatted = df.format(tongTien);
			model.addRow(new Object[] { maDon, hoTen, sdt, ngayTra, tongTienFormatted, "Chi tiết" });
		}
	}
}
