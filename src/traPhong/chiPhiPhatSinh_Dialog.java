package traPhong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import dao.ChiPhiPhatSinh_DAO;
import dao.LoaiPhong_Dao;
import entity.ChiPhiPhatSinh;
import entity.DonDatPhong;
import entity.LoaiPhong;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;



public class chiPhiPhatSinh_Dialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable kiemTraHuHong;
    private JTextField maChiPhi;
	private JTable phuThuQuaGio;
	private JTextField chiPhiThietBiHong;
	private JTextField tongChiPhiPhatSinh;
    private String maDonDatPhong;
    private int rowIndex; // Thêm biến này
    private String maPhong;
    private String loaiPhong;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
        	JFrame aFrame= new JFrame();
            chiPhiPhatSinh_Dialog dialog = new chiPhiPhatSinh_Dialog("a", 1, "P101", "a", aFrame, true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setUndecorated(true);
			dialog.setLocationRelativeTo(null);
			dialog.setSize(833, 714);
			dialog.setResizable(false);
			dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public chiPhiPhatSinh_Dialog(String maDonDatPhong, int a, String maPhong, String loaiPhong, JFrame parent, boolean modal) {
    	super(parent, modal);
        this.maDonDatPhong = maDonDatPhong;
        this.rowIndex = a; // Khởi tạo rowIndex
        this.maPhong= maPhong;
        this.loaiPhong= loaiPhong;
        
    	setSize(832,714);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(255, 255, 255));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        JLabel lblNewLabel = new JLabel("II.Kiểm tra thiết bị hư hỏng:");
        lblNewLabel.setBounds(20, 246, 250, 30);
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        contentPanel.add(lblNewLabel);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 3); // Tạo viền đen, dày 2 pixel
        getRootPane().setBorder(border);
        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane1.setBounds(20, 287, 766, 211); // Điều chỉnh kích thước scrollPane1
        contentPanel.add(scrollPane1);

        kiemTraHuHong = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 3;
            }
        };
        kiemTraHuHong.setFont(new Font("Times New Roman", Font.BOLD, 15));
        kiemTraHuHong.setRowSelectionAllowed(false);
        kiemTraHuHong.setSurrendersFocusOnKeystroke(true);
        kiemTraHuHong.setModel(new DefaultTableModel(
            new Object[][] {
                {1, "Ga giường bị bẩn hoặc rách (không thể phục hồi)", null, null},
                {2, "Hư hỏng nội thất (bàn, ghế, tủ, gương, thảm, sofa)", null, null},
                {3, "Hư hỏng thiết bị điện tử (TV, điều hòa,...)", null, null},
                {4, "Mất hoặc làm hỏng chìa khóa/thẻ từ", null, null},
            },
            new String[] {
                "STT", "Trang thiết bị", "Giá tiền", "Mô tả chi tiết"
            }
        ));

        kiemTraHuHong.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getColumn() == 2) {
                    updateTongTienHuHong();
                }
            }
        });
        TableColumnModel columnModel = kiemTraHuHong.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(500);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(400);

        DefaultTableCellRenderer centerRenderer_dv = new DefaultTableCellRenderer();
        centerRenderer_dv.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < kiemTraHuHong.getColumnCount() - 1; i++) {
            kiemTraHuHong.getColumnModel().getColumn(i).setCellRenderer(centerRenderer_dv);
        }

        TableColumn giaTienColumn = kiemTraHuHong.getColumnModel().getColumn(2);
        giaTienColumn.setCellEditor(new CustomComboBoxEditor());

        kiemTraHuHong.setRowHeight(47);

        JTableHeader header = kiemTraHuHong.getTableHeader();
        header.setFont(new Font("Times New Roman", Font.BOLD, 17));
        header.setPreferredSize(new Dimension(header.getWidth(), 20));

        scrollPane1.setViewportView(kiemTraHuHong);
        
        JLabel lblNewLabel_1 = new JLabel("Chi phí phát sinh");
        lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 25));
        lblNewLabel_1.setBounds(328, 26, 191, 45);
        contentPanel.add(lblNewLabel_1);
        
        JButton tat = new JButton("X");
        tat.setFont(new Font("Times New Roman", Font.BOLD, 18));
        tat.setBounds(762, 10, 47, 45);
        tat.setContentAreaFilled(false);
        tat.setBorderPainted(false);
        tat.setFocusPainted(false);
		tat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(tat);
				dialog.dispose();
			}
		});
        contentPanel.add(tat);
        
        JLabel lblNewLabel_2 = new JLabel("Mã chi phí:");
        lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblNewLabel_2.setBounds(20, 93, 122, 30);
        contentPanel.add(lblNewLabel_2);
        
        maChiPhi = new JTextField();
        maChiPhi.setFont(new Font("Times New Roman", Font.BOLD, 20));
        maChiPhi.setEditable(false);
        maChiPhi.setBounds(152, 93, 229, 30);
        contentPanel.add(maChiPhi);
        maChiPhi.setColumns(10);
        
        generateMaChiPhi();
        JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setBorder(null);
        scrollPane2.setBackground(null);
        scrollPane2.getViewport().setOpaque(false);
        scrollPane2.setOpaque(false);
        scrollPane2.setBounds(20, 178, 766, 58);
        contentPanel.add(scrollPane2);

        phuThuQuaGio = new JTable();
        phuThuQuaGio.setSurrendersFocusOnKeystroke(true);
        phuThuQuaGio.setFont(new Font("Times New Roman", Font.BOLD, 18));
        phuThuQuaGio.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"M\u00E3 ph\u00F2ng", "Lo\u1EA1i ph\u00F2ng", "\u0110\u01A1n gi\u00E1", "S\u1ED1 gi\u1EDD th\u00EAm", "Th\u00E0nh ti\u1EC1n"
        	}
        ));


        DefaultTableCellRenderer centerRenderer_dv2 = new DefaultTableCellRenderer();
        centerRenderer_dv2.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < phuThuQuaGio.getColumnCount() - 1; i++) {
        	phuThuQuaGio.getColumnModel().getColumn(i).setCellRenderer(centerRenderer_dv2);
        }

      

        // Điều chỉnh kích thước dòng
        phuThuQuaGio.setRowHeight(30);
        LoaiPhong_Dao loai= new LoaiPhong_Dao();
        LoaiPhong loaiPhong2= loai.getLoaiPhongBySoPhong(maPhong);
        themPhuThuQuaGio(maPhong, loaiPhong, loaiPhong2.getPhuThuQuaGio(), 0);
        // Điều chỉnh kích thước header
        JTableHeader header2 = phuThuQuaGio.getTableHeader();
        header2.setFont(new Font("Times New Roman", Font.BOLD, 15));
        header2.setPreferredSize(new Dimension(header2.getWidth(), 25));
        scrollPane2.setViewportView(phuThuQuaGio);
        
        JLabel lblIphThuQu = new JLabel("I.Phụ thu quá giờ:");
        lblIphThuQu.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblIphThuQu.setBounds(20, 133, 250, 30);
        contentPanel.add(lblIphThuQu);
        
        JLabel lblNewLabel_3 = new JLabel("Chi phí thiết bị hỏng:");
        lblNewLabel_3.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblNewLabel_3.setBounds(393, 506, 197, 24);
        contentPanel.add(lblNewLabel_3);
        TableColumn soGioThemColumn = phuThuQuaGio.getColumnModel().getColumn(3);
        soGioThemColumn.setCellEditor(new SpinnerEditor());
        
        chiPhiThietBiHong = new JTextField();
        chiPhiThietBiHong.setFont(new Font("Times New Roman", Font.BOLD, 17));
        chiPhiThietBiHong.setEditable(false);
        chiPhiThietBiHong.setBounds(578, 506, 207, 30);
        contentPanel.add(chiPhiThietBiHong);
        chiPhiThietBiHong.setColumns(10);
        chiPhiThietBiHong.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTongChiPhiPhatSinh();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTongChiPhiPhatSinh();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTongChiPhiPhatSinh();
            }
        });
        
        JLabel lblIiiTngChi = new JLabel("III. Tổng chi phí phát sinh:");
        lblIiiTngChi.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblIiiTngChi.setBounds(20, 557, 250, 30);
        contentPanel.add(lblIiiTngChi);
        
        tongChiPhiPhatSinh = new JTextField();
        tongChiPhiPhatSinh.setFont(new Font("Times New Roman", Font.BOLD, 17));
        tongChiPhiPhatSinh.setEditable(false);
        tongChiPhiPhatSinh.setColumns(10);
        tongChiPhiPhatSinh.setBounds(263, 557, 207, 30);
        contentPanel.add(tongChiPhiPhatSinh);
        
        JButton capNhat = new JButton("Cập nhật");
        capNhat.setBackground(new Color(0, 128, 255));
        capNhat.setForeground(new Color(255, 255, 255));
        capNhat.setFont(new Font("Times New Roman", Font.BOLD, 20));
        capNhat.setBounds(680, 634, 129, 33);
        capNhat.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	    // Lấy dữ liệu từ dialog
        	    String maChiPhiValue = maChiPhi.getText();

        	    // Kiểm tra và gán giá trị mặc định cho chiPhiThietBiHong
        	    double chiPhiThietBiHongValue = 0.0;
        	    try {
        	        String chiPhiThietBiHongText = chiPhiThietBiHong.getText().replace(".0", "");
        	        if (!chiPhiThietBiHongText.isEmpty()) {
        	            chiPhiThietBiHongValue = Double.parseDouble(chiPhiThietBiHongText);
        	        }
        	    } catch (NumberFormatException ex) {
        	        ex.printStackTrace(); // Xử lý lỗi nếu cần
        	    }

        	    // Lưu giá trị từ editor vào bảng
        	    TableCellEditor editor = phuThuQuaGio.getCellEditor(0, 3);
        	    if (editor != null) {
        	        editor.stopCellEditing();
        	    }

        	    // Kiểm tra và gán giá trị mặc định cho soGioThem
        	    int soGioThemValue = 0;
        	    Object soGioThemObject = phuThuQuaGio.getValueAt(0, 3);
        	    if (soGioThemObject != null) {
        	        soGioThemValue = (Integer) soGioThemObject;
        	    }

        	    // Kiểm tra và gán giá trị mặc định cho tongChiPhi
        	    double tongChiPhiValue = 0.0;
        	    try {
        	        String tongChiPhiPhatSinhText = tongChiPhiPhatSinh.getText().replace(".0", "");
        	        if (!tongChiPhiPhatSinhText.isEmpty()) {
        	            tongChiPhiValue = Double.parseDouble(tongChiPhiPhatSinhText);
        	        }
        	    } catch (NumberFormatException ex) {
        	        ex.printStackTrace(); // Xử lý lỗi nếu cần
        	    }

        	    // Kiểm tra và gán giá trị mặc định cho chiPhiPhuThu
        	    double chiPhiPhuThu = 0.0;
        	    Object chiPhiPhuThuObject = phuThuQuaGio.getValueAt(0, 4);
        	    if (chiPhiPhuThuObject != null) {
        	        chiPhiPhuThu = (double) chiPhiPhuThuObject;
        	    }
                StringBuilder moTaBuilder = new StringBuilder();
                for (int i = 0; i < kiemTraHuHong.getRowCount(); i++) {
                    Object moTaValue = kiemTraHuHong.getValueAt(i, 3); // Cột "Mô tả chi tiết" là cột thứ 4 (index 3)
                    if (moTaValue != null && !moTaValue.toString().isEmpty()) {
                        moTaBuilder.append(moTaValue.toString());
                        if (i < kiemTraHuHong.getRowCount() - 1) {
                            moTaBuilder.append("; "); // Ngăn cách các mô tả bằng dấu ";"
                        }
                    }
                }
                String moTa = moTaBuilder.toString();

        	    int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn cập nhật chi phí phát sinh không?", "Xác nhận cập nhật", JOptionPane.YES_NO_OPTION);

        	    if (choice == JOptionPane.YES_OPTION) {
        	        // Gọi listener để truyền dữ liệu về frame
        	        if (listener != null) {
        	            listener.onChiPhiPhatSinhUpdated(rowIndex, maChiPhiValue, chiPhiThietBiHongValue, soGioThemValue, chiPhiPhuThu, tongChiPhiValue);
        	        }
        	        DonDatPhong ddp= new DonDatPhong(maDonDatPhong);
        	        ChiPhiPhatSinh chiPhi= new ChiPhiPhatSinh(maChiPhiValue, chiPhiThietBiHongValue, soGioThemValue, moTa, ddp);
        	        ChiPhiPhatSinh_DAO chiPhiDAO = new ChiPhiPhatSinh_DAO();
        	        if (!chiPhiDAO.themChiPhiPhatSinh(chiPhi)) {
        	            chiPhiDAO.suaChiPhiPhatSinh(chiPhi);
        	        }

        	        // Đóng dialog
        	        dispose();
        	    } else {
        	        // Nếu người dùng chọn "No", không làm gì cả hoặc có thể thêm xử lý khác nếu cần
        	        JOptionPane.showMessageDialog(null,"Hủy cập nhật chi phí phát sinh");
        	        dispose();
        	    }
        	}
        });
        contentPanel.add(capNhat);

    }

    private void updateTongTienHuHong() {
        double total = 0;
        for (int i = 0; i < kiemTraHuHong.getRowCount(); i++) {
            Object value = kiemTraHuHong.getValueAt(i, 2); // Cột "Giá tiền"
            if (value != null && !value.toString().isEmpty()) {
                try {
                    // Chuyển đổi giá trị thành số
                    String priceString = value.toString().replace(".", "").replace(",", ".");
                    double price = Double.parseDouble(priceString);
                    total += price;
                } catch (NumberFormatException e) {
                    // Nếu không thể chuyển đổi, bỏ qua
                }
            }
        }
        chiPhiThietBiHong.setText(String.format("%.0f",total).replace(',', '.')); // Hiển thị tổng tiền
        updateTongChiPhiPhatSinh();
    }

//    public class CustomComboBoxEditor extends DefaultCellEditor {
//    	private JComboBox<String> comboBox;
//    	private JTextField editor;
//    	private String lastInput = ""; // Biến lưu trữ giá trị trước đó
//
//    	public CustomComboBoxEditor() {
//    	    super(new JComboBox<>());
//    	    comboBox = (JComboBox<String>) getComponent();
//    	    comboBox.setEditable(true);
//    	    editor = (JTextField) comboBox.getEditor().getEditorComponent();
//
//    	    editor.addKeyListener(new KeyAdapter() {
//    	        @Override
//    	        public void keyReleased(KeyEvent e) {
//    	            String text = editor.getText().trim();
//    	            // Kiểm tra nếu giá trị nhập vào giống với giá trị trước đó
//    	            if (text.equals(lastInput)) {
//    	                return; // Ngăn không cho nhập hai số giống nhau
//    	            }
//    	            lastInput = text; // Cập nhật giá trị trước đó
//
//    	            // Kiểm tra xem có ký tự không hợp lệ không
//    	            if (!text.matches("\\d*")) { // Chỉ cho phép số
//    	                editor.setText(""); // Xóa nội dung ô nếu có ký tự không hợp lệ
//    	                comboBox.hidePopup(); // Ẩn danh sách gợi ý
//    	                return; // Dừng lại
//    	            }
//
//    	            comboBox.removeAllItems();
//    	            if (!text.isEmpty()) {
//    	                int base = Integer.parseInt(text);
//    	                int[] multipliers = {1000, 10000, 100000, 1000000};
//    	                for (int m : multipliers) {
//    	                    long suggestion = (long) base * m;
//    	                    comboBox.addItem(String.format("%,d", suggestion).replace(',', '.'));
//    	                }
//    	                SwingUtilities.invokeLater(() -> {
//    	                    if (comboBox.isShowing()) {
//    	                        comboBox.showPopup();
//    	                    }
//    	                });
//    	            } else {
//    	                comboBox.hidePopup();
//    	            }
//    	        }
//    	    });
//
//    	    editor.addFocusListener(new FocusAdapter() {
//    	        @Override
//    	        public void focusLost(FocusEvent e) {
//    	            String text = editor.getText();
//    	            if (text != null && !text.isEmpty()) {
//    	                if (!text.matches("\\d+")) { // Kiểm tra nếu là chữ (không phải số)
//    	                    // Nếu giá trị là chữ và không nằm trong danh sách gợi ý, xóa ô
//    	                    if (!isValueInSuggestions(text)) {
//    	                        editor.setText("");
//    	                    }
//    	                }
//    	            }
//    	        }
//    	    });
//    	}
//
//    	private boolean isValueInSuggestions(String value) {
//    	    for (int i = 0; i < comboBox.getItemCount(); i++) {
//    	        if (comboBox.getItemAt(i).equals(value)) {
//    	            return true; // Giá trị hợp lệ
//    	        }
//    	    }
//    	    return false; // Giá trị không hợp lệ
//    	}
//
//    	@Override
//    	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//    	    Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);
//    	    editor.setText(value != null ? value.toString() : "");
//    	    SwingUtilities.invokeLater(() -> {
//    	        if (comboBox.isShowing()) {
//    	            comboBox.showPopup();
//    	        }
//    	    });
//    	    return c;
//    	}
//
//    	@Override
//    	public Object getCellEditorValue() {
//    	    // Chỉ trả về giá trị khi người dùng chọn từ danh sách
//    	    if (comboBox.getSelectedItem() != null) {
//    	        return comboBox.getSelectedItem().toString();
//    	    }
//    	    return ""; // Nếu không có giá trị hợp lệ, trả về chuỗi rỗng
//    	}
//    	}
    public class CustomComboBoxEditor extends DefaultCellEditor {
        private JComboBox<String> comboBox;
        private JTextField editor;

        public CustomComboBoxEditor() {
            super(new JComboBox<>());
            comboBox = (JComboBox<String>) getComponent();
            comboBox.setEditable(true);
            editor = (JTextField) comboBox.getEditor().getEditorComponent();

            editor.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    String text = editor.getText().trim();

                    // Không giới hạn số nhập vào, chỉ hiển thị gợi ý nếu là số
                    if (text.matches("\\d+")) {
                        comboBox.removeAllItems();
                        int base = Integer.parseInt(text);
                        int[] multipliers = {1000, 10000, 100000, 1000000};
                        for (int m : multipliers) {
                            long suggestion = (long) base * m;
                            comboBox.addItem(String.format("%,d", suggestion).replace(',', '.'));
                        }

                        SwingUtilities.invokeLater(() -> {
                            if (comboBox.isShowing()) {
                                comboBox.showPopup();
                            }
                        });
                    } else {
                        comboBox.hidePopup(); // Ẩn nếu là chữ hoặc không hợp lệ
                    }
                }
            });

            editor.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    String text = editor.getText();
                    if (text != null && !text.isEmpty()) {
                        if (!text.matches("\\d+")) { // Nếu chứa ký tự không phải số
                            editor.setText(""); // Xóa nội dung
                        }
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);
            editor.setText(value != null ? value.toString() : "");
            SwingUtilities.invokeLater(() -> {
                if (comboBox.isShowing()) {
                    comboBox.showPopup();
                }
            });
            return c;
        }

        @Override
        public Object getCellEditorValue() {
            return editor.getText(); // Trả về bất kỳ giá trị nào đã nhập (không giới hạn gợi ý)
        }
    }

    // Editor dùng JSpinner
 // Editor dùng JSpinner
    class SpinnerEditor extends AbstractCellEditor implements TableCellEditor, ChangeListener {
        final JSpinner spinner;
        private JTable table;
        private int row;

        public SpinnerEditor() {
            spinner = new JSpinner(new SpinnerNumberModel(0, 0, 24, 1));
            spinner.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            spinner.setFont(new Font("Times New Roman", Font.BOLD, 20));
            spinner.addChangeListener(this); // Lắng nghe sự kiện thay đổi giá trị
        }

        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            spinner.setValue(value);
            this.table = table;
            this.row = row;
            return spinner;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            // Cập nhật cột "Thành tiền" ngay khi giá trị Spinner thay đổi
            int soGioThem = (Integer) spinner.getValue();
            Double donGia = (Double) table.getValueAt(row, 2);
            table.setValueAt(soGioThem * donGia, row, 4);
            updateTongChiPhiPhatSinh();
        }
    }

    // Renderer hiển thị Spinner như cell tĩnh
    class SpinnerRenderer extends JSpinner implements TableCellRenderer {
        public SpinnerRenderer() {
            super(new SpinnerNumberModel(0, 0, 24, 1));
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            setFont(new Font("Times New Roman", Font.BOLD, 20));
            setForeground(Color.black);
            setEnabled(false); // để không chỉnh được
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setValue(value);
            return this;
        }
    }
    private void generateMaChiPhi() {
        if (maDonDatPhong != null && !maDonDatPhong.isEmpty()) {
            String rowNumber = String.format("%02d", rowIndex); // Format số thứ tự dòng thành 2 chữ số
            maChiPhi.setText(maDonDatPhong+ rowNumber);
        }
    }
    private void themPhuThuQuaGio(String maPhong, String loaiPhong, double donGia, int soGioThem) {
        DefaultTableModel model = (DefaultTableModel) phuThuQuaGio.getModel();
        model.addRow(new Object[]{
            maPhong, // Mã phòng
            loaiPhong,
            donGia,
            soGioThem,
            donGia * soGioThem // Thành tiền
        });
    }

    
    private void updateTongChiPhiPhatSinh() {
        double tongThanhTien = 0;
        for (int i = 0; i < phuThuQuaGio.getRowCount(); i++) {
            Double thanhTien = (Double) phuThuQuaGio.getValueAt(i, 4);
            if (thanhTien != null) {
                tongThanhTien += thanhTien;
            }
        }

        double chiPhiThietBi = 0;
        try {
            // Loại bỏ dấu chấm phân cách hàng nghìn trước khi chuyển đổi
            chiPhiThietBi = Double.parseDouble(chiPhiThietBiHong.getText().replace(".0", ""));
        } catch (NumberFormatException e) {
            chiPhiThietBi = 0;
        }

        double tongChiPhi = tongThanhTien + chiPhiThietBi;
        tongChiPhiPhatSinh.setText(String.format("%.0f",tongChiPhi).replace(",", "."));
    }
    public interface ChiPhiPhatSinhListener {
        void onChiPhiPhatSinhUpdated(int row, String maChiPhi, double chiPhiThietBiHong, int soGioThem, double chiPhiPhuThu, double tongChiPhi);
    }

    private ChiPhiPhatSinhListener listener;

    public void setChiPhiPhatSinhListener(ChiPhiPhatSinhListener listener) {
        this.listener = listener;
    }
}
