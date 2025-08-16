package traPhong;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dao.DonDatPhong_Dao;
import dao.Phong_Dao;
import entity.DonDatPhong;
import entity.Phong;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.stream.Collectors;
public class panel_timKiem extends JPanel {
	private JFrame frame;
	private static final long serialVersionUID = 1L;
	private JTextField hoVaTen;
	private JTextField sdt;
	private JTextField maPhong;
	private JTable table_MaPhong;

	private JPanel panel_TimTheoMaPhong;
	private JPanel panel_TimTheoKhachHang;

	// Dữ liệu gốc để lọc
	private String[][] originalData = {
	};
	private String[] columnNames = {"Mã phòng"};
	private JTable table_DonDatPhong;

	public panel_timKiem(JFrame parent) {
		
		setBackground(Color.WHITE);
		setLayout(null);
		setBorder(new LineBorder(Color.black, 2, true));
		setBounds(0, 0, 929, 629); 
		JLabel lblNewLabel = new JLabel("Tìm kiếm đơn đặt phòng");
		lblNewLabel.setBounds(304, 21, 323, 38);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
		add(lblNewLabel);

		JButton tat = new JButton("X");
		tat.setFocusPainted(false);
		tat.setFont(new Font("Times New Roman", Font.BOLD, 18));
		tat.setBounds(846, 10, 59, 49);
		tat.setBorderPainted(false);
		tat.setContentAreaFilled(false);
		tat.setOpaque(false);
		add(tat);

		// Panel tìm theo mã phòng
		panel_TimTheoMaPhong = new JPanel();
		panel_TimTheoMaPhong.setBackground(new Color(255, 255, 255));
		panel_TimTheoMaPhong.setBorder(new LineBorder(Color.black, 2, true));
		panel_TimTheoMaPhong.setBounds(197, 117, 517, 194);
		panel_TimTheoMaPhong.setLayout(null);
		panel_TimTheoMaPhong.setVisible(false);
		add(panel_TimTheoMaPhong);

		maPhong = new JTextField("Nhập số phòng");
		maPhong.setFont(new Font("Times New Roman", Font.BOLD, 20));
		maPhong.setBounds(22, 83, 257, 30);
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
		scrollPane.setBounds(304, 10, 191, 174);
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
		DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table_MaPhong.getTableHeader().getDefaultRenderer();
		headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		scrollPane.setViewportView(table_MaPhong);
		
		// Panel tìm theo khách hàng
		panel_TimTheoKhachHang = new JPanel();
		panel_TimTheoKhachHang.setBackground(new Color(255, 255, 255));
		panel_TimTheoKhachHang.setBorder(new LineBorder(Color.black, 2, true));
		panel_TimTheoKhachHang.setBounds(197, 136, 517, 194);
		add(panel_TimTheoKhachHang);
		panel_TimTheoKhachHang.setLayout(null);
		panel_TimTheoKhachHang.setVisible(true);
				
		JLabel lblNewLabel_11 = new JLabel("Họ và tên:");
		lblNewLabel_11.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel_11.setBounds(30, 32, 97, 30);
		panel_TimTheoKhachHang.add(lblNewLabel_11);
						
		JLabel lblNewLabel_1_1 = new JLabel("Số điện thoại:");
		lblNewLabel_1_1.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel_1_1.setBounds(30, 100, 125, 30);
		panel_TimTheoKhachHang.add(lblNewLabel_1_1);
								
		hoVaTen = new JTextField();
		hoVaTen.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		hoVaTen.setBounds(148, 32, 312, 30);
		panel_TimTheoKhachHang.add(hoVaTen);
		hoVaTen.setColumns(10);
										
		sdt = new JTextField();
		sdt.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		sdt.setColumns(10);
		sdt.setBounds(148, 100, 312, 30);
		panel_TimTheoKhachHang.add(sdt);
		loadDanhSachMaPhong();
		JButton timKiem = new JButton("Tìm kiếm");
		timKiem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        String soPhong = maPhong.getText().trim();
		        String tenKH = hoVaTen.getText();
		        String sdt1 = sdt.getText().trim();
                if(soPhong.equals("Nhập số phòng")) {
                	soPhong="";
                }
		        if (!soPhong.isEmpty()) {
		            DonDatPhong_Dao dao = new DonDatPhong_Dao();
		            System.out.println(soPhong);
		    	    DonDatPhong ddp = dao.getDonDatPhongTheoMaP(soPhong);
		    	    if (ddp != null) {
		                // Tắt cửa sổ hiện tại nếu là JDialog hoặc JFrame
		                Window window = SwingUtilities.getWindowAncestor(table_DonDatPhong);
		                if (window != null) {
		                    window.dispose();
		                }

		                EventQueue.invokeLater(() -> {
		                    TraPhong chiTiet= new TraPhong(ddp);
		                    chiTiet.setVisible(true);
		                    parent.dispose();
		                });
		            }
		        } else if (!tenKH.isEmpty() && !sdt1.isEmpty()) {
		            loadDonDatPhongTheoTenVaSDT(tenKH, sdt1);
		        } else if (tenKH == null || tenKH.trim().isEmpty() || sdt == null || sdt.getText().isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Vui lòng nhập số phòng hoặc tên + số điện thoại khách hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
		        }
		    }
		});
		timKiem.setBackground(new Color(0, 255, 128));
		timKiem.setFont(new Font("Times New Roman", Font.BOLD, 20));
		timKiem.setBounds(753, 320, 121, 38);
        timKiem.setBackground(new Color(0, 255, 128));
        timKiem.setOpaque(true);
        timKiem.setContentAreaFilled(true);
        timKiem.setBorderPainted(false);
		add(timKiem);
		
		JLabel lblDanhSchn = new JLabel("Danh sách đơn đặt phòng");
		lblDanhSchn.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblDanhSchn.setBounds(289, 390, 345, 38);
		add(lblDanhSchn);
		
		Font fontTable = new Font("Times New Roman", Font.PLAIN, 18);
		Font fontHeader = new Font("Times New Roman", Font.BOLD, 20);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(47, 450, 851, 104);
		add(scrollPane_1);

		table_DonDatPhong = new JTable();
		table_DonDatPhong.setFont(fontTable);
		table_DonDatPhong.setRowHeight(25);

		// Đặt model và cấu hình cột không chỉnh sửa
		table_DonDatPhong.setModel(new DefaultTableModel(
		    new Object[][] {},
		    new String[] {
		        "Mã đơn đặt phòng", "Tên khách", "Số điện thoại", "Trạng thái", "Số phòng"
		    }
		) {
		    boolean[] columnEditables = new boolean[] {
		        false, false, true, false, false
		    };
		    public boolean isCellEditable(int row, int column) {
		        return columnEditables[column];
		    }
		});

		// Đặt font cho phần tiêu đề bảng
		JTableHeader header1 = table_DonDatPhong.getTableHeader();
		header1.setFont(fontHeader);
		header1.setPreferredSize(new Dimension(header1.getWidth(), 30));

		// (Tuỳ chọn) căn giữa dữ liệu trong bảng
		DefaultTableCellRenderer centerRenderer1 = new DefaultTableCellRenderer();
		centerRenderer1.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < table_DonDatPhong.getColumnCount(); i++) {
		    table_DonDatPhong.getColumnModel().getColumn(i).setCellRenderer(centerRenderer1);
		}

		scrollPane_1.setViewportView(table_DonDatPhong);

		table_DonDatPhong.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int selectedRow = table_DonDatPhong.getSelectedRow();
		        if (selectedRow != -1) {
		            String maDonDatPhong = table_DonDatPhong.getValueAt(selectedRow, 0).toString();

		            DonDatPhong_Dao dao = new DonDatPhong_Dao();
		            DonDatPhong ddp = dao.getDonDatPhongTheoMa(maDonDatPhong);

		            if (ddp != null) {
		                // Tắt cửa sổ hiện tại nếu là JDialog hoặc JFrame
		                Window window = SwingUtilities.getWindowAncestor(table_DonDatPhong);
		                if (window != null) {
		                    window.dispose(); // Tắt cửa sổ chứa bảng
		                }

		                EventQueue.invokeLater(() -> {
		                    TraPhong chiTiet= new TraPhong(ddp);
//		                    chiTiet = new donDatPhong(ddp);
		                    chiTiet.setVisible(true);
		                    parent.dispose();
		                });
		            }
		        }
		    }
		});

		JTableHeader header11 = table_DonDatPhong.getTableHeader();
		header11.setFont(new Font("Times New Roman", Font.BOLD, 20));
		header11.setForeground(Color.BLACK); 
		header11.setBackground(Color.GRAY); 

		// Căn giữa nội dung trong bảng
		DefaultTableCellRenderer centerRenderer11 = new DefaultTableCellRenderer();
		centerRenderer11.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < table_DonDatPhong.getColumnCount(); i++) {
			table_DonDatPhong.getColumnModel().getColumn(i).setCellRenderer(centerRenderer11);
		}
		// Căn giữa header
		DefaultTableCellRenderer headerRenderer1 = (DefaultTableCellRenderer) table_DonDatPhong.getTableHeader().getDefaultRenderer();
		headerRenderer1.setHorizontalAlignment(SwingConstants.CENTER);
		
		scrollPane_1.setViewportView(table_DonDatPhong);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 0, 0));
		panel.setBounds(1, 369, 920, 1);
		add(panel);
		
//		JButton btnNewButton = new JButton("Tìm kiếm mã phòng");
//		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
//		btnNewButton.setBounds(226, 69, 231, 38);
//		add(btnNewButton);
//		
//		JButton btnTmKimTheo = new JButton("Tìm theo khách hàng");
//		btnTmKimTheo.setFont(new Font("Times New Roman", Font.BOLD, 18));
//		btnTmKimTheo.setBounds(450, 69, 231, 38);
//		add(btnTmKimTheo);
		
		JButton timTheoMaPhong = new RoundedButton("Tìm theo số phòng", 10, 0, 0, 10);
		timTheoMaPhong.setBounds(226, 69, 231, 38);
		add(timTheoMaPhong);
		timTheoMaPhong.setFocusPainted(false);
	    timTheoMaPhong.setForeground(new Color(0, 0, 0));
		timTheoMaPhong.setFont(new Font("Times New Roman", Font.BOLD, 18));
		timTheoMaPhong.setBackground(new Color(255, 255, 255));
				
		JButton timTheoKhachHang = new RoundedButton("Tìm theo khách hàng", 0, 10, 10, 0);
		timTheoKhachHang.setBounds(450, 69, 231, 38);
		add(timTheoKhachHang);
		timTheoKhachHang.setFocusPainted(false);
		timTheoKhachHang.setForeground(new Color(255, 255, 255));
		timTheoKhachHang.setFont(new Font("Times New Roman", Font.BOLD, 18));
		timTheoKhachHang.setBackground(new Color(33, 150, 243));
		
		timTheoKhachHang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timTheoMaPhong.setBackground(new Color(255, 255, 255));
				timTheoMaPhong.setForeground(Color.black);
				timTheoKhachHang.setBackground(new Color(33, 150, 243));
				timTheoKhachHang.setForeground(Color.white);
				panel_TimTheoMaPhong.setVisible(false);
				panel_TimTheoKhachHang.setVisible(true);
				maPhong.setText("");
				((DefaultTableModel) table_DonDatPhong.getModel()).setRowCount(0);
				setBounds(0, 0, 929, 629);
			}
		});
				
		timTheoMaPhong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timTheoMaPhong.setBackground(new Color(33, 150, 243));
				timTheoMaPhong.setForeground(Color.white);
				timTheoKhachHang.setBackground(new Color(255, 255, 255));
				timTheoKhachHang.setForeground(Color.black);
				panel_TimTheoMaPhong.setVisible(true);
				panel_TimTheoKhachHang.setVisible(false);
				hoVaTen.setText("");
				sdt.setText("");
				((DefaultTableModel) table_DonDatPhong.getModel()).setRowCount(0);
				setBounds(0, 0, 929, 371); 
				
			}
		});

		// ===== Sự kiện =====

		tat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(tat);
				dialog.dispose();
			}
		});

		// Lọc dữ liệu khi nhập vào JTextField
		maPhong.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) { update(); }
			public void removeUpdate(DocumentEvent e) { update(); }
			public void changedUpdate(DocumentEvent e) { update(); }

			private void update() {
				String keyword = maPhong.getText().trim().toLowerCase();
				DefaultTableModel model = (DefaultTableModel) table_MaPhong.getModel();
				model.setRowCount(0); // Xóa dữ liệu cũ
				for (String[] row : originalData) {
					if (row[0].toLowerCase().startsWith(keyword)) {
						model.addRow(new Object[]{row[0]});
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
//	private void loadDonDatPhongTheoSoPhong(String soPhong) {
//	    DonDatPhong_DAO dao = new DonDatPhong_DAO();
//	    List<DonDatPhong> danhSach = dao.getDonDatPhongTheoMaPhong(soPhong);
//
//	    DefaultTableModel model = (DefaultTableModel) table_DonDatPhong.getModel();
//	    model.setRowCount(0); // Xóa dữ liệu cũ
//
//	    if (danhSach.isEmpty()) {
//	        JOptionPane.showMessageDialog(this, "Không tìm thấy đơn đặt phòng cho số phòng: " + soPhong, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//	        return;
//	    }
//
//	    for (DonDatPhong ddp : danhSach) {
//	        model.addRow(new Object[] {
//	            ddp.getMaDonDatPhong(),
//	            ddp.getKhachHang().getHoTen(),
//	            ddp.getKhachHang().getSdt(),
//	            ddp.getTrangThai()
//	        });
//	    }
//	}
	private void loadDonDatPhongTheoTenVaSDT(String tenKH, String sdt) {
	    DonDatPhong_Dao dao = new DonDatPhong_Dao();
	    ArrayList<DonDatPhong> danhSach = dao.getDonDatPhongTheoTenVaSDT(tenKH, sdt);

	    DefaultTableModel model = (DefaultTableModel) table_DonDatPhong.getModel();
	    model.setRowCount(0); // Xóa dữ liệu cũ
        
	    Phong_Dao phong_DAO= new Phong_Dao();
	    
	    if (danhSach.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Không tìm thấy đơn đặt phòng cho khách hàng: " + tenKH + ", SDT: " + sdt, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	        return;
	    }

	    for (DonDatPhong ddp : danhSach) {
	    	ArrayList<Phong> phong= phong_DAO.getPhongTheoMaDonDatPhong(ddp.getMaDonDatPhong());
	    	String maPhongStr = phong.stream().map(Phong::getSoPhong) .collect(Collectors.joining(", "));
	        model.addRow(new Object[] {
	            ddp.getMaDonDatPhong(),
	            ddp.getKhachHang().getHoTen(),
	            ddp.getKhachHang().getSdt(),
	            ddp.getTrangThai(),
	            maPhongStr
	        });
	    }
	}
	public class RoundedButton extends JButton {
	    private int topLeft, topRight, bottomRight, bottomLeft;

	    public RoundedButton(String text, int topLeft, int topRight, int bottomRight, int bottomLeft) {
	        super(text);
	        this.topLeft = topLeft;
	        this.topRight = topRight;
	        this.bottomRight = bottomRight;
	        this.bottomLeft = bottomLeft;
	        setContentAreaFilled(false);
	        setFocusPainted(false);
	        setBorderPainted(false);
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        Graphics2D g2 = (Graphics2D) g.create();
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	        int w = getWidth();
	        int h = getHeight();

	        // Tạo path với 4 góc bo riêng biệt
	        Path2D.Float path = new Path2D.Float();
	        path.moveTo(topLeft, 0);
	        path.lineTo(w - topRight, 0);
	        path.quadTo(w, 0, w, topRight);
	        path.lineTo(w, h - bottomRight);
	        path.quadTo(w, h, w - bottomRight, h);
	        path.lineTo(bottomLeft, h);
	        path.quadTo(0, h, 0, h - bottomLeft);
	        path.lineTo(0, topLeft);
	        path.quadTo(0, 0, topLeft, 0);
	        path.closePath();

	        // Tô nền
	        g2.setColor(getBackground());
	        g2.fill(path);

	        // Vẽ viền
	        g2.setColor(new Color(33, 150, 243)); // hoặc getForeground() hay customColor nếu bạn muốn
	        g2.setStroke(new BasicStroke(2)); // độ dày viền
	        g2.draw(path);

	        // Cắt phần nội dung text theo path
	        g2.setClip(path);

	        // Vẽ text
	        super.paintComponent(g2);
	        g2.dispose();
	    }
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
}
