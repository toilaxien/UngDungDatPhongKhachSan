package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import dao.KhuyenMai_DAO;
import entity.KhuyenMai;

public class KhuyenMaiDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField tenKhuyenMai;
	private JTextField maKhuyenMai;
	private JTextField ngayBatDau;
	private JTextField ngayKetThuc;
	private JTable table_KhuyenMai;
	private JTextField comboBox_DieuKienNgayO;
	private JTextField comboBox_DieuKienThanhToan;
	private JDatePickerImpl btn_NgayBatDau;
	private JDatePickerImpl btn_NgayKetThuc;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			KhuyenMaiDialog dialog = new KhuyenMaiDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public KhuyenMaiDialog() {

		KhuyenMai_DAO dao = new KhuyenMai_DAO();
		dao.capNhatTrangThaiKhuyenMaiHetHan(); // Cập nhật trước

		ArrayList<KhuyenMai> danhSachKM = dao.getAllKhuyenMai(); // Lấy danh sách đã cập nhật

		setModalityType(ModalityType.TOOLKIT_MODAL);
		setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
		setBounds(100, 100, 1211, 713);
		setLocationRelativeTo(null);
		setResizable(false);
		// Thay thế contentPanel bằng BackgroundPanel
		BackgroundPanel backgroundPanel = new BackgroundPanel(
				"C:\\Users\\TOILAXIEN\\Downloads\\ChatGPT Image 10_01_07 16 thg 5, 2025.png");
		setContentPane(backgroundPanel); // Đặt backgroundPanel làm content pane chính

		// Tạo panel chứa nội dung (phải setOpaque=false để thấy được nền)
		JPanel contentPanel = new JPanel();
		contentPanel.setOpaque(false); // QUAN TRỌNG: Đặt trong suốt
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null); // Giữ nguyên layout null nếu cần

		backgroundPanel.add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Tạo chương trình khuyến mãi");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblNewLabel.setBackground(new Color(255, 255, 255));
		lblNewLabel.setBounds(429, 0, 337, 39);
		contentPanel.add(lblNewLabel);

		JPanel ttKM = new JPanel();
		ttKM.setBackground(new Color(255, 255, 255));
		ttKM.setBounds(48, 49, 517, 151);
		contentPanel.add(ttKM);
		ttKM.setLayout(null);

		tenKhuyenMai = new JTextField();
		tenKhuyenMai.setBounds(170, 30, 337, 32);
		ttKM.add(tenKhuyenMai);
		tenKhuyenMai.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Tên khuyến mãi:");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel_1.setBounds(10, 31, 153, 25);
		ttKM.add(lblNewLabel_1);

		JLabel lblNewLabel_1_2 = new JLabel("Mã khuyến mãi:");
		lblNewLabel_1_2.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel_1_2.setBounds(10, 90, 153, 25);
		ttKM.add(lblNewLabel_1_2);

		maKhuyenMai = new JTextField();
		maKhuyenMai.setColumns(10);
		maKhuyenMai.setBounds(170, 90, 337, 32);
		ttKM.add(maKhuyenMai);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBounds(48, 210, 517, 143);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel_1_1 = new JLabel("Loại khuyến mãi:");
		lblNewLabel_1_1.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel_1_1.setBounds(10, 23, 153, 25);
		panel_1.add(lblNewLabel_1_1);

		JComboBox loaiKhuyenMai = new JComboBox();
		loaiKhuyenMai.setFont(new Font("Times New Roman", Font.BOLD, 18));
		loaiKhuyenMai.setBounds(173, 19, 334, 32);
		loaiKhuyenMai.addItem("Khuyến mãi theo số ngày khách ở");
		loaiKhuyenMai.addItem("Khuyến mãi theo dịp lễ");
		loaiKhuyenMai.setSelectedIndex(1);
		panel_1.add(loaiKhuyenMai);

		JLabel lblNewLabel_1_1_1 = new JLabel("Giá trị khuyến mãi:");
		lblNewLabel_1_1_1.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel_1_1_1.setBounds(10, 78, 180, 25);
		panel_1.add(lblNewLabel_1_1_1);

		JComboBox giaTriKhuyenMai = new JComboBox();
		giaTriKhuyenMai.setFont(new Font("Times New Roman", Font.BOLD, 18));
		giaTriKhuyenMai.setBounds(173, 76, 96, 32);

		for (int i = 10; i <= 50; i += 10) {
			giaTriKhuyenMai.addItem(i);
		}

		panel_1.add(giaTriKhuyenMai);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 255, 255));
		panel_2.setBounds(575, 49, 572, 151);
		contentPanel.add(panel_2);
		panel_2.setLayout(null);

		JLabel lblNewLabel_1_3 = new JLabel("Thời hạn áp dụng:");
		lblNewLabel_1_3.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel_1_3.setBounds(10, 10, 153, 25);
		panel_2.add(lblNewLabel_1_3);

		JLabel lblNewLabel_1_3_1 = new JLabel("Ngày bắt đầu");
		lblNewLabel_1_3_1.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel_1_3_1.setBounds(76, 37, 153, 25);
		panel_2.add(lblNewLabel_1_3_1);

		JLabel lblNewLabel_1_3_2 = new JLabel("Ngày kết thúc");
		lblNewLabel_1_3_2.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel_1_3_2.setBounds(358, 37, 153, 25);
		panel_2.add(lblNewLabel_1_3_2);

		ngayBatDau = new JTextField();
		ngayBatDau.setFont(new Font("Times New Roman", Font.BOLD, 18));
		ngayBatDau.setBounds(96, 74, 179, 32);
		panel_2.add(ngayBatDau);
		ngayBatDau.setColumns(10);

		ngayKetThuc = new JTextField();
		ngayKetThuc.setFont(new Font("Times New Roman", Font.BOLD, 18));
		ngayKetThuc.setColumns(10);
		ngayKetThuc.setBounds(382, 72, 180, 32);
		panel_2.add(ngayKetThuc);
		btn_NgayBatDau = createDatePicker(LocalDate.now(), ngayBatDau);
		btn_NgayBatDau.setBounds(39, 72, 55, 34);
		panel_2.add(btn_NgayBatDau);

		btn_NgayKetThuc = createDatePicker(LocalDate.now(), ngayKetThuc);
		btn_NgayKetThuc.setBounds(317, 72, 55, 34);
		panel_2.add(btn_NgayKetThuc);

		JPanel panel_1_1 = new JPanel();
		panel_1_1.setLayout(null);
		panel_1_1.setBackground(Color.WHITE);
		panel_1_1.setBounds(575, 210, 572, 143);
		contentPanel.add(panel_1_1);

		JLabel lblNewLabel_1_3_3 = new JLabel("Điều kiện áp dụng:");
		lblNewLabel_1_3_3.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel_1_3_3.setBounds(10, 10, 153, 25);
		panel_1_1.add(lblNewLabel_1_3_3);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(20, 45, 519, 75);
		panel_1_1.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel_2 = new JLabel("Tổng thanh toán trên");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel_2.setBounds(59, 25, 173, 21);
		panel.add(lblNewLabel_2);

		comboBox_DieuKienNgayO = new JTextField();
		comboBox_DieuKienNgayO.setFont(new Font("Times New Roman", Font.BOLD, 18));
		comboBox_DieuKienNgayO.setBounds(234, 21, 225, 32);
		panel.add(comboBox_DieuKienNgayO);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(20, 45, 519, 75);
		panel_1_1.add(panel_3);
		panel_3.setLayout(null);
		panel_3.setBackground(Color.WHITE);

		JLabel lblNewLabel_2_1 = new JLabel("Khách ở hơn");
		lblNewLabel_2_1.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel_2_1.setBounds(105, 25, 127, 21);
		panel_3.add(lblNewLabel_2_1);

		comboBox_DieuKienThanhToan = new JTextField();
		comboBox_DieuKienThanhToan.setBounds(234, 21, 225, 32);
		panel_3.add(comboBox_DieuKienThanhToan);
		loaiKhuyenMai.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = loaiKhuyenMai.getSelectedItem().toString();
				if (selected.equals("Khuyến mãi theo số ngày khách ở")) {
					panel.setVisible(false);
					panel_3.setVisible(true);
				} else if (selected.equals("Khuyến mãi theo dịp lễ")) {
					panel.setVisible(true);
					panel_3.setVisible(false);
				}
			}
		});

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(255, 255, 255));
		panel_4.setBounds(48, 400, 1099, 252);
		contentPanel.add(panel_4);
		panel_4.setLayout(null);

		JLabel lblNewLabel_3 = new JLabel("Danh sách các khuyến mãi đã áp dụng");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblNewLabel_3.setBounds(320, 10, 406, 41);
		panel_4.add(lblNewLabel_3);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(32, 65, 1025, 177);
		panel_4.add(scrollPane);

		table_KhuyenMai = new JTable();
		table_KhuyenMai.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "T\u00EAn khuy\u1EBFn m\u00E3i", "M\u00E3 khuy\u1EBFn m\u00E3i",
						"Lo\u1EA1i khuy\u1EBFn m\u00E3i", "Gi\u00E1 tr\u1ECB ", "Ng\u00E0y b\u1EAFt \u0111\u1EA7u",
						"Ng\u00E0y k\u1EBFt th\u00FAc", "Tr\u1EA1ng th\u00E1i", "D\u1EEBng KM" }) {
			boolean[] columnEditables = new boolean[] { false, false, false, false, false, false, false, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_KhuyenMai.getColumnModel().getColumn(0).setResizable(false);
		table_KhuyenMai.getColumnModel().getColumn(1).setResizable(false);
		table_KhuyenMai.getColumnModel().getColumn(2).setResizable(false);
		table_KhuyenMai.getColumnModel().getColumn(3).setResizable(false);
		table_KhuyenMai.getColumnModel().getColumn(4).setResizable(false);
		table_KhuyenMai.getColumnModel().getColumn(5).setResizable(false);
		table_KhuyenMai.getColumnModel().getColumn(6).setResizable(false);
		table_KhuyenMai.getColumnModel().getColumn(7).setResizable(false);
		table_KhuyenMai.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		table_KhuyenMai.setRowHeight(30);

		JTableHeader header = table_KhuyenMai.getTableHeader();
		header.setFont(new Font("Arial", Font.BOLD, 15));
		header.setPreferredSize(new Dimension(header.getWidth(), 35));
		// Căn giữa nội dung trong bảng
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < table_KhuyenMai.getColumnCount(); i++) {
			table_KhuyenMai.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		// Căn giữa header
		DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table_KhuyenMai.getTableHeader()
				.getDefaultRenderer();
		headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setViewportView(table_KhuyenMai);

		JButton btnCapNhat = new JButton("Cập nhật");
		btnCapNhat.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnCapNhat.setBackground(new Color(0, 255, 128));
		btnCapNhat.setBounds(1016, 356, 131, 34);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		btnCapNhat.addActionListener(e -> {
			try {
				Date start = sdf.parse(ngayBatDau.getText());
				Date end = sdf.parse(ngayKetThuc.getText());
				if (start.after(end)) {
					JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được sau ngày kết thúc!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);

					// Làm nổi bật (focus) lại ô ngày bắt đầu
					ngayKetThuc.getText();
				} else {
					// Cho phép tiếp tục xử lý
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ! (dd/MM/yyyy)", "Lỗi định dạng",
						JOptionPane.WARNING_MESSAGE);
			}

			KhuyenMai km = new KhuyenMai(maKhuyenMai.getText(), tenKhuyenMai.getText(),
					loaiKhuyenMai.getSelectedItem().toString(),
					Double.parseDouble(giaTriKhuyenMai.getSelectedItem().toString()),
					LocalDate.parse(ngayBatDau.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
					LocalDate.parse(ngayKetThuc.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
					Double.parseDouble(loaiKhuyenMai.getSelectedIndex() == 0 ? comboBox_DieuKienThanhToan.getText()
							: comboBox_DieuKienNgayO.getText()),
					"Đang áp dụng");
			KhuyenMai_DAO kMai_DAO = new KhuyenMai_DAO();
			if (kMai_DAO.addKhuyenMai(km)) {
				DefaultTableModel model = (DefaultTableModel) table_KhuyenMai.getModel();
				model.addRow(new Object[] { km.getTenKhuyenMai(), km.getMaKhuyenMai(), km.getLoaiKhuyenMai(),
						km.getGiaTriKhuyenMai() + "%",
						km.getNgayBatDau().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
						km.getNgayKetThuc().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), km.getTrangThai(),
						"Dừng" });
				JOptionPane.showMessageDialog(this, "Thêm thành công!");
			}
		});

		contentPanel.add(btnCapNhat);

		table_KhuyenMai.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
		table_KhuyenMai.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox()));
		loadTable(danhSachKM);

	}

	class BackgroundPanel extends JPanel {
		private Image backgroundImage;

		public BackgroundPanel(String imagePath) {
			this.backgroundImage = new ImageIcon(imagePath).getImage();
			setLayout(new BorderLayout()); // Sử dụng BorderLayout để quản lý layout
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			// Vẽ ảnh nền co giãn theo kích thước panel
			if (backgroundImage != null) {
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
			}
		}
	}

	private JDatePickerImpl createDatePicker(LocalDate defaultDate, JTextField targetTextField) {
		UtilDateModel model = new UtilDateModel();

		LocalDate today = LocalDate.now();
		LocalDate startDate = (defaultDate != null && !defaultDate.isBefore(today)) ? defaultDate : today;

		model.setDate(startDate.getYear(), startDate.getMonthValue() - 1, startDate.getDayOfMonth());
		model.setSelected(true);

		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");

		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		datePanel.setBackground(Color.WHITE);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

		// Ẩn ô nhập văn bản (JFormattedTextField)
		for (Component comp : datePicker.getComponents()) {
			if (comp instanceof JFormattedTextField) {
				comp.setVisible(false);
				comp.setBackground(Color.WHITE);
			}
		}

		// Kiểm tra ngày chọn, không cho chọn ngày trong quá khứ
		datePicker.addActionListener(e -> {
			Date selectedDate = (Date) datePicker.getModel().getValue();
			if (selectedDate != null) {
				LocalDate chosenDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				if (chosenDate.isBefore(today)) {
					// Reset về ngày hôm nay
					datePicker.getModel().setDate(today.getYear(), today.getMonthValue() - 1, today.getDayOfMonth());
					datePicker.getModel().setSelected(true);
					targetTextField.setText("");
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					targetTextField.setText(sdf.format(selectedDate));
				}
			}
		});

		// Tùy chỉnh icon của button chọn ngày
		try {
			if (datePicker.getComponentCount() > 1) {
				Component buttonComponent = datePicker.getComponent(1);
				if (buttonComponent instanceof JButton) {
					JButton button = (JButton) buttonComponent;
					button.setText("");
					int width = 10;
					int height = 20;
					// Đặt màu nền trắng
					button.setBackground(Color.WHITE);
					button.setOpaque(true);
					button.setContentAreaFilled(true);

					// Tùy chọn: tắt viền và focus nếu cần
					button.setBorderPainted(false);
					button.setFocusPainted(false);
					ImageIcon icon = new ImageIcon(
							"C:\\Users\\TOILAXIEN\\OneDrive\\Máy tính\\Phat_trien_ung_dung\\Test\\BaiCuaXien\\src\\HinhAnhGiaoDienChinh\\lich.png");

					// Resize ảnh theo đúng kích thước của button (dựa theo preferred size)
					Image scaledImage = icon.getImage().getScaledInstance(width + 20, height + 10, Image.SCALE_SMOOTH);
					button.setIcon(new ImageIcon(scaledImage));
				}
			}
		} catch (Exception ex) {
			System.err.println("Không thể tùy chỉnh date picker: " + ex.getMessage());
		}

		return datePicker;
	}

	// ==============Hàm tạo fomat lại Ngày nhận trả==============
	public class DateLabelFormatter extends AbstractFormatter {
		private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'thg' M, yyyy");

		@Override
		public Object stringToValue(String text) throws ParseException {
			return LocalDate.parse(text, formatter);
		}

		@Override
		public String valueToString(Object value) throws ParseException {
			if (value != null) {
				Calendar cal = (Calendar) value;
				LocalDate date = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				return date.format(formatter);
			}
			return "";
		}
	}

	class ButtonRenderer extends JButton implements TableCellRenderer {
		public ButtonRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			setText((value == null) ? "Dừng" : value.toString());
			return this;
		}
	}

	class ButtonEditor extends DefaultCellEditor {
		protected JButton button;
		private String label;
		private boolean clicked;
		private JTable table;

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(e -> fireEditingStopped());
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			this.table = table;
			label = (value == null) ? "Dừng" : value.toString();
			button.setText(label);
			clicked = true;
			return button;
		}

		public Object getCellEditorValue() {
			if (clicked) {
				int row = table.getSelectedRow();
				String maKM = table.getValueAt(row, 1).toString(); // Mã khuyến mãi
				KhuyenMai_DAO khuyenMai_DAO = new KhuyenMai_DAO();
				// Gọi cập nhật trạng thái trong database
				if (khuyenMai_DAO.updateTrangThaiKhuyenMai(maKM, "Đã dừng")) {
					table.setValueAt("Đã dừng", row, 6); // Cập nhật trong bảng
					JOptionPane.showMessageDialog(button, "Đã dừng khuyến mãi!");
				} else {
					JOptionPane.showMessageDialog(button, "Cập nhật thất bại!");
				}
			}
			clicked = false;
			return label;
		}

		public boolean stopCellEditing() {
			clicked = false;
			return super.stopCellEditing();
		}
	}

	public void loadTable(ArrayList<KhuyenMai> danhSachKM) {
		ArrayList<KhuyenMai> sortedList = danhSachKM.stream()
			    .sorted((km1, km2) -> {
			        boolean km1Active = "Đang áp dụng".equals(km1.getTrangThai());
			        boolean km2Active = "Đang áp dụng".equals(km2.getTrangThai());
			        return Boolean.compare(!km1Active, !km2Active); // "Đang áp dụng" lên trước
			    })
			    .collect(Collectors.toCollection(ArrayList::new));

	    DefaultTableModel model = (DefaultTableModel) table_KhuyenMai.getModel();
	    model.setRowCount(0); // Xóa dữ liệu cũ

	    for (KhuyenMai km : sortedList) {
	    	 if ("Không".equalsIgnoreCase(km.getMaKhuyenMai())) {
	    	        continue;
	    	    }

	        model.addRow(new Object[] {
	            km.getTenKhuyenMai(),
	            km.getMaKhuyenMai(),
	            km.getLoaiKhuyenMai(),
	            km.getGiaTriKhuyenMai() + "%",
	            km.getNgayBatDau(),
	            km.getNgayKetThuc(),
	            km.getTrangThai()
	        });
	    }
	}


}
