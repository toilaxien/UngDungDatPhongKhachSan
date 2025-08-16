package traPhong;

import java.awt.*;

import java.awt.geom.GeneralPath;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import javax.swing.text.NumberFormatter;

public class KiemTraThietBi_GUI extends JPanel {
	private JTextField tfTen, tfSoLuong, tfGiaTien, tfMoTaChiTiet;
	private JCheckBox chkHuHong;
	private DefaultTableModel modelThietBiPhoBien, modelThietBiDaThem;
	private JTable table_thietBiPhoBien, table_thietBiDaThem;
	private Map<Integer, Integer> mapGiaTienGoc = new HashMap<>();

	public KiemTraThietBi_GUI() {
		setLayout(new BorderLayout());
		setBackground(Color.LIGHT_GRAY);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();

		// Khởi tạo các biến
		tfTen = new JTextField();
		tfMoTaChiTiet = new JTextField();
		tfSoLuong = new JTextField();
		tfGiaTien = new JTextField();
		chkHuHong = new JCheckBox("Thiết bị đã bị hỏng");
		chkHuHong.setBackground(Color.white);
		chkHuHong.setFont(new Font("Arial", Font.BOLD, 14));
		chkHuHong.setSelected(true);
		table_thietBiPhoBien = new JTable();
		table_thietBiDaThem = new JTable();

		// ===== Panel bên trái =====
		JPanel panelTrai = new JPanel();
		panelTrai.setLayout(new BorderLayout());
		panelTrai.setBackground(Color.WHITE);

		// Panel nhập thiết bị hỏng
		JPanel panelNhapThietBi = new JPanel();
		panelNhapThietBi.setLayout(new BoxLayout(panelNhapThietBi, BoxLayout.Y_AXIS));
		panelNhapThietBi.setOpaque(false);

		// Tiêu đề
		JLabel lblTieuDeNhap = new JLabel("Thêm thiết bị hỏng", SwingConstants.CENTER);
		lblTieuDeNhap.setFont(new Font("Times New Roman", Font.BOLD, 27));
		lblTieuDeNhap.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		lblTieuDeNhap.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Panel nhập liệu
		JPanel panelNhapLieu = new JPanel(new GridBagLayout());
		panelNhapLieu.setBackground(Color.white);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 10, 10, 10);

		// Tên thiết bị
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		JLabel lblTenThietBi = new JLabel("Tên thiết bị:");
		lblTenThietBi.setFont(new Font("Arial", Font.BOLD, 16));
		panelNhapLieu.add(lblTenThietBi, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		tfTen.setPreferredSize(new Dimension(300, 30));
		panelNhapLieu.add(tfTen, gbc);

		// Số lượng
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.3;
		JLabel lblSoLuong = new JLabel("Số lượng:");
		lblSoLuong.setFont(new Font("Arial", Font.BOLD, 16));
		panelNhapLieu.add(lblSoLuong, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		tfSoLuong.setPreferredSize(new Dimension(300, 30));
		panelNhapLieu.add(tfSoLuong, gbc);

		// Giá tiền
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.3;
		JLabel lblGiaTien = new JLabel("Giá tiền:");
		lblGiaTien.setFont(new Font("Arial", Font.BOLD, 16));
		panelNhapLieu.add(lblGiaTien, gbc);

		// Tạo định dạng số cho Giá tiền
		NumberFormat format = NumberFormat.getNumberInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Long.class);
		formatter.setAllowsInvalid(false);
		formatter.setMinimum(0L);

		// Sử dụng JFormattedTextField để tạo ô nhập liệu với định dạng số
		JFormattedTextField tfGiaTien = new JFormattedTextField(formatter);
		tfGiaTien.setPreferredSize(new Dimension(300, 30));
		panelNhapLieu.add(tfGiaTien, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		tfGiaTien.setPreferredSize(new Dimension(300, 30));
		panelNhapLieu.add(tfGiaTien, gbc);

		// Checkbox hư hỏng
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		panelNhapLieu.add(chkHuHong, gbc);

		// Tạo nút "Thêm vào danh sách"
		JButton btnThem = new JButton("Thêm vào danh sách");
		btnThem.setBackground(new Color(0, 255, 64));
		btnThem.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnThem.setPreferredSize(new Dimension(200, 30));

		// Thêm ActionListener cho nút
		btnThem.addActionListener(e -> {
			String tenThietBi = tfTen.getText().trim(); // Lấy giá trị từ JTextField và loại bỏ khoảng trắng thừa
			String soLuong = tfSoLuong.getText().trim();
			String giaTien = tfGiaTien.getText().trim();

			// Kiểm tra ô checkbox (nếu có)
			boolean huHong = chkHuHong.isSelected(); // Kiểm tra xem thiết bị có hỏng không

			// Kiểm tra dữ liệu nhập vào
			if (tenThietBi.isEmpty() || soLuong.isEmpty() || giaTien.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Kiểm tra số lượng và giá tiền là số hợp lệ
			try {
				Integer.parseInt(soLuong); // Kiểm tra số lượng có phải là số nguyên không
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Thêm vào bảng bên phải
			modelThietBiDaThem.addRow(new Object[] { modelThietBiDaThem.getRowCount() + 1, // STT
					tenThietBi, soLuong, giaTien, huHong ? "Hỏng" : "Nguyên vẹn", tfMoTaChiTiet.getText().trim() });

			// Xóa thông tin sau khi thêm
			tfTen.setText("");
			tfSoLuong.setText("");
			tfGiaTien.setValue(null);
			tfMoTaChiTiet.setText(""); // Xóa mô tả chi tiết
		});

		// Căn giữa nút bằng panel phụ
		JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelNut.setOpaque(false);
		panelNut.add(btnThem);

		// Thêm các thành phần vào panelNhapThietBi
		panelNhapThietBi.add(lblTieuDeNhap);
		panelNhapThietBi.add(Box.createVerticalGlue());
		panelNhapThietBi.add(panelNhapLieu);
		panelNhapThietBi.add(Box.createVerticalStrut(5));
		panelNhapThietBi.add(panelNut);
		panelNhapThietBi.add(Box.createVerticalGlue());

		// Bảng thiết bị hỏng phổ biến
		JPanel panelThietBiPhoBien = new JPanel();
		panelThietBiPhoBien.setLayout(new BoxLayout(panelThietBiPhoBien, BoxLayout.Y_AXIS));
		panelThietBiPhoBien.setOpaque(false);

		// Tiêu đề bảng
		JLabel lblTieuDePhoBien = new JLabel("Thiết bị hỏng phổ biến", SwingConstants.CENTER);
		lblTieuDePhoBien.setFont(new Font("Times New Roman", Font.BOLD, 27));
		lblTieuDePhoBien.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		lblTieuDePhoBien.setAlignmentX(Component.CENTER_ALIGNMENT);

		JScrollPane scrollBangPhoBien = createThietBiPhoBienScrollPane();

		panelThietBiPhoBien.add(lblTieuDePhoBien);
		panelThietBiPhoBien.add(Box.createVerticalGlue());
		panelThietBiPhoBien.add(scrollBangPhoBien);

		panelTrai.add(panelNhapThietBi, BorderLayout.NORTH);
		panelTrai.add(panelThietBiPhoBien, BorderLayout.CENTER);

		// ===== Panel bên phải =====
		CustomRoundedPanel panelPhai = new CustomRoundedPanel(0, 0, 0, 0);
		panelPhai.setBackground(Color.WHITE);
		panelPhai.setLayout(new BorderLayout());
		panelPhai.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));

		JLabel lblTieuDeDanhSach = new JLabel("Danh sách thiết bị hỏng", SwingConstants.CENTER);
		lblTieuDeDanhSach.setFont(new Font("Times New Roman", Font.BOLD, 27));
		lblTieuDeDanhSach.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

		JPanel panelDanhSach = createDanhSachThietBiDaThem();

		panelPhai.add(lblTieuDeDanhSach, BorderLayout.NORTH);
		panelPhai.add(panelDanhSach, BorderLayout.CENTER);

		// Thêm vào giao diện chính
		add(panelTrai, BorderLayout.WEST);
		add(panelPhai, BorderLayout.CENTER);
	}

	// Hàm tạo JScrollPane cho bảng thiết bị phổ biến
	private JScrollPane createThietBiPhoBienScrollPane() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setBackground(Color.WHITE);

		modelThietBiPhoBien = new DefaultTableModel(new Object[][] { { false, "Tivi", 1000000 },
				{ false, "Máy lạnh", 5000000 }, { false, "Tủ lạnh", 3000000 }, { false, "Đèn ngủ", 150000 },
				{ false, "Máy sấy tóc", 250000 }, { false, "Bàn ủi", 350000 }, { false, "Máy lọc không khí", 2200000 },
				{ false, "Quạt đứng", 800000 }, { false, "Quạt trần", 1200000 }, { false, "Bình đun siêu tốc", 300000 },
				{ false, "Lò vi sóng", 2000000 }, { false, "Ghế sofa", 4500000 }, { false, "Bàn làm việc", 1500000 },
				{ false, "Gương toàn thân", 700000 }, { false, "Đèn đọc sách", 250000 },
				{ false, "Điện thoại bàn", 500000 }, { false, "Tủ quần áo", 3000000 },
				{ false, "Bồn rửa mặt", 2000000 }, { false, "Máy nước nóng", 3500000 }, { false, "Bồn tắm", 8000000 } },
				new String[] { "Chọn", "Tên thiết bị", "Giá tiền" }) {
			Class<?>[] columnTypes = new Class[] { Boolean.class, String.class, Integer.class };
			boolean[] columnEditables = new boolean[] { true, false, false };

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};

		table_thietBiPhoBien.setModel(modelThietBiPhoBien);
		table_thietBiPhoBien.setRowHeight(40);
		table_thietBiPhoBien.setFont(new Font("Dialog", Font.PLAIN, 14));
		table_thietBiPhoBien.setFillsViewportHeight(true);
		table_thietBiPhoBien.setBorder(null);
		table_thietBiPhoBien.setShowVerticalLines(true);
		table_thietBiPhoBien.setShowHorizontalLines(true);
		table_thietBiPhoBien.setGridColor(Color.LIGHT_GRAY);

		JTableHeader header = table_thietBiPhoBien.getTableHeader();
		header.setBackground(new Color(220, 255, 220));
		header.setFont(new Font("Times New Roman", Font.BOLD, 16));
		header.setPreferredSize(new Dimension(header.getWidth(), 30));
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 1; i < modelThietBiPhoBien.getColumnCount(); i++) {
			table_thietBiPhoBien.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		table_thietBiPhoBien.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (value instanceof Integer) {
					setText(formatCurrency((Integer) value));
				}
				setHorizontalAlignment(SwingConstants.CENTER);
				return c;
			}
		});

		// Thêm sự kiện khi chọn checkbox trong bảng thiết bị phổ biến
		table_thietBiPhoBien.getModel().addTableModelListener(e -> {
			int row = e.getFirstRow();
			Boolean isSelected = (Boolean) modelThietBiPhoBien.getValueAt(row, 0);
			if (isSelected != null && isSelected) {
				addDeviceToTableDaThem(row);
			}
		});

		scrollPane.setViewportView(table_thietBiPhoBien);
		return scrollPane;
	}
	private String calculateTotal(int giaTien, String mucDoHuHong, int soLuong) {
	    double tongTien = switch (mucDoHuHong) {
	        case "Cần thay mới" -> giaTien * 1.2;
	        case "Cần sửa chữa" -> giaTien * 0.5;
	        case "Hư hỏng nhẹ" -> giaTien * 0.3;
	        default -> 0.0;
	    };
	    tongTien *= soLuong;
	    return formatCurrency((int) tongTien); // Trả về giá trị đã định dạng
	}

	private void addDeviceToTableDaThem(int row) {
	    String tenThietBi = (String) modelThietBiPhoBien.getValueAt(row, 1);
	    int giaTien = (Integer) modelThietBiPhoBien.getValueAt(row, 2);
	    String mucDo = "Cần sửa chữa";
	    int soLuong = 1;
	    int newRow = modelThietBiDaThem.getRowCount();

	    // Thêm thiết bị vào bảng với giá tiền đã được định dạng
	    modelThietBiDaThem.addRow(new Object[] {
	        newRow + 1,
	        tenThietBi,
	        soLuong,
	        formatCurrency(giaTien), // Định dạng giá tiền
	        mucDo,
	        calculateTotal(giaTien, mucDo, soLuong), // Tổng tiền chưa định dạng
	        ""
	    });

	    // Thêm sự kiện TableModelListener để cập nhật tổng tiền khi số lượng hoặc mức độ hư hỏng thay đổi
	    table_thietBiDaThem.getModel().addTableModelListener(new TableModelListener() {
	        @Override
	        public void tableChanged(TableModelEvent e) {
	            if (e.getType() == TableModelEvent.UPDATE && (e.getColumn() == 2 || e.getColumn() == 4)) {
	                int row1 = e.getFirstRow();
	                int soLuongMoi = (Integer) modelThietBiDaThem.getValueAt(row1, 2);
	                String mucDoMoi = (String) modelThietBiDaThem.getValueAt(row1, 4);
	                // Lấy giá tiền từ cột 3 dưới dạng chuỗi và chuyển đổi về số nguyên
	                String giaTienStr = (String) modelThietBiDaThem.getValueAt(row1, 3);
	                int giaTienMoi = Integer.parseInt(giaTienStr.replaceAll("[^\\d]", "")); // Loại bỏ ký tự không phải số

	                // Tính tổng tiền mới và định dạng lại
	                String tongTienMoi = calculateTotal(giaTienMoi, mucDoMoi, soLuongMoi);
	                modelThietBiDaThem.setValueAt(tongTienMoi, row1, 5); // Cập nhật tổng tiền đã định dạng
	            }
	        }
	    });
	}

	public void tableChanged(TableModelEvent e) {
	    if (e.getType() == TableModelEvent.UPDATE && (e.getColumn() == 2 || e.getColumn() == 4)) {
	        int row1 = e.getFirstRow();

	        // Lấy số lượng
	        Object soLuongObj = modelThietBiDaThem.getValueAt(row1, 2);
	        int soLuongMoi = (soLuongObj instanceof Integer) ? (Integer) soLuongObj
	                : Integer.parseInt(soLuongObj.toString());

	        // Lấy mức độ hư hỏng
	        String mucDoMoi = (String) modelThietBiDaThem.getValueAt(row1, 4);

	        // Lấy giá tiền đã định dạng và chuyển đổi về số nguyên
	        String giaTienStr = (String) modelThietBiDaThem.getValueAt(row1, 3);
	        int giaTienMoi = Integer.parseInt(giaTienStr.replaceAll("[^\\d]", "")); // Loại bỏ ký tự không phải số

	        // Tính toán tổng tiền mới
	        String tongTienMoi = calculateTotal(giaTienMoi, mucDoMoi, soLuongMoi);
	        modelThietBiDaThem.setValueAt(tongTienMoi, row1, 5); // Cập nhật tổng tiền đã định dạng
	    }
	}

	// Hàm định dạng lại giá tiền với dấu phân cách hàng nghìn
	private String formatCurrency(int amount) {
		DecimalFormat formatter = new DecimalFormat("#,###");
		return formatter.format(amount) + " VND";
	}

	private JPanel createDanhSachThietBiDaThem() {
		// Tạo JScrollPane cho bảng
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		scrollPane.setBackground(Color.WHITE);

		// Tạo bảng với DefaultTableModel
		table_thietBiDaThem = new JTable();
		modelThietBiDaThem = new DefaultTableModel(new Object[][] {}, new String[] { "STT", "Tên thiết bị", "SL",
				"Giá tiền", "Mức độ hỏng", "Tổng tiền", "Mô tả chi tiết" }) {

			Class[] columnTypes = new Class[] { String.class, String.class, Integer.class, String.class, String.class,
					String.class, String.class };
			boolean[] columnEditables = new boolean[] { false, false, true, false, true, false, true };

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};

		table_thietBiDaThem.setModel(modelThietBiDaThem);
		table_thietBiDaThem.setRowHeight(40);
		table_thietBiDaThem.setFont(new Font("Dialog", Font.PLAIN, 14));
		table_thietBiDaThem.setFillsViewportHeight(true);
		table_thietBiDaThem.setBorder(null);
		table_thietBiDaThem.setShowVerticalLines(true);
		table_thietBiDaThem.setShowHorizontalLines(true);
		table_thietBiDaThem.setGridColor(Color.LIGHT_GRAY);

		JTableHeader header = table_thietBiDaThem.getTableHeader();
		header.setBackground(new Color(220, 255, 220));
		header.setFont(new Font("Times New Roman", Font.BOLD, 16));
		header.setPreferredSize(new Dimension(header.getWidth(), 30));

		// Tùy chỉnh renderer để căn giữa cho tất cả các cột
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < modelThietBiDaThem.getColumnCount(); i++) {
			table_thietBiDaThem.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		// Tùy chỉnh cho cột số lượng sử dụng SoLuongRendererEditor (giả sử lớp này đã
		// được định nghĩa)
		TableColumn slColumn = table_thietBiDaThem.getColumnModel().getColumn(2);
		slColumn.setCellEditor(new SoLuongRendererEditor(table_thietBiDaThem));

		// Tùy chỉnh cho cột Mức độ hư hỏng sử dụng JComboBox
		String[] mucDoHuHong = { "Cần thay mới", "Cần sửa chữa", "Hư hỏng nhẹ" };
		JComboBox<String> comboBox = new JComboBox<>(mucDoHuHong);
		TableColumn huHongColumn = table_thietBiDaThem.getColumnModel().getColumn(4);
		huHongColumn.setCellEditor(new DefaultCellEditor(comboBox));

		// Thêm JScrollPane vào table
		scrollPane.setViewportView(table_thietBiDaThem);

		// === Thêm nút Xóa ===
		JButton btnXoa = new JButton("Xóa thiết bị");
		btnXoa.setFocusPainted(false);
		btnXoa.setBackground(new Color(255, 80, 80));
		btnXoa.setForeground(Color.WHITE);
		btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnXoa.setPreferredSize(new Dimension(130, 40));
		btnXoa.addActionListener(e -> {
			int selectedRow = table_thietBiDaThem.getSelectedRow();
			if (selectedRow != -1) {
				modelThietBiDaThem.removeRow(selectedRow);
				// Cập nhật lại STT sau khi xóa
				for (int i = 0; i < modelThietBiDaThem.getRowCount(); i++) {
					modelThietBiDaThem.setValueAt(i + 1, i, 0); // Cập nhật lại số thứ tự
				}
			} else {
				JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng để xóa.");
			}
		});

		// Tạo panel chứa nút xóa
		JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
		panelButton.setBackground(Color.WHITE);
		panelButton.add(btnXoa);

		// === Thêm nút Tiếp tục ===
		JButton tiepTuc = new JButton("Tiếp tục");
		tiepTuc.setBackground(new Color(0, 255, 64));
		tiepTuc.setFont(new Font("Times New Roman", Font.BOLD, 25));
		tiepTuc.setPreferredSize(new Dimension(130, 40)); // Kích thước nút
		tiepTuc.addActionListener(e -> {
			// Hành động khi nút "Tiếp tục" được nhấn
			JOptionPane.showMessageDialog(null, "Chuyển sang trang tiếp theo");
			// Ở đây bạn có thể thêm hành động chuyển đến trang hoặc panel khác
			// Ví dụ, bạn có thể tạo một phương thức để chuyển sang panel mới
			// chuyểnTrangMoi();
		});

		// Tạo panel chứa cả hai nút
		JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
		panelBottom.setBackground(Color.WHITE);
		panelBottom.add(btnXoa);
		panelBottom.add(tiepTuc);

		// Tạo panel chứa bảng và các nút
		JPanel containerPanel = new JPanel(new BorderLayout());
		containerPanel.setBackground(Color.WHITE);
		containerPanel.add(scrollPane, BorderLayout.CENTER);
		containerPanel.add(panelBottom, BorderLayout.SOUTH); // Thêm panel chứa cả hai nút vào phía dưới

		return containerPanel; // Trả về JPanel chứa cả bảng và hai nút
	}

	class SoLuongRendererEditor extends AbstractCellEditor implements TableCellEditor {
		private JPanel panel;
		private JLabel label;
		private JButton btnCong, btnTru;
		private int row;

		private JTable table;
		private DefaultTableModel model;

		public SoLuongRendererEditor(JTable table) {
			this.table = table;
			this.model = (DefaultTableModel) table.getModel();

			panel = new JPanel(new GridLayout(1, 3));
			label = new JLabel("", SwingConstants.CENTER);
			label.setPreferredSize(new Dimension(50, 30));

			btnTru = new JButton("-");
			btnCong = new JButton("+");

			btnTru.addActionListener(e -> updateQuantity(-1));
			btnCong.addActionListener(e -> updateQuantity(1));

			panel.add(btnTru);
			panel.add(label);
			panel.add(btnCong);
		}

		private void updateQuantity(int delta) {
			try {
				int quantity = Integer.parseInt(label.getText()) + delta;
				if (quantity < 0)
					quantity = 0;
				label.setText(String.valueOf(quantity));
				model.setValueAt(quantity, row, 2);
				updatePrice(row);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void updatePrice(int row) {
			try {
				int quantity = Integer.parseInt(model.getValueAt(row, 2).toString());
				String donGiaStr = model.getValueAt(row, 3).toString().replaceAll("[^\\d]", "");
				int donGia = Integer.parseInt(donGiaStr);
				int tong = donGia * quantity;
				model.setValueAt(formatCurrency(tong), row, 5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			this.row = row;
			label.setText(String.valueOf(value));
			return panel;
		}

		@Override
		public Object getCellEditorValue() {
			return Integer.parseInt(label.getText());
		}
	}

	// Custom Panel có bo góc
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
}
