package motSoCNLT;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dao.KhachHang_Dao;
import entity.KhachHang;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class QuanLyKhachHang extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private JTextField maKhachHang;
	private JTextField hoTen;
	private JTextField sdt;
	private JTextField cccd;
	private JTextField email;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
//			QuanLyKhachHang dialog = new QuanLyKhachHang();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public QuanLyKhachHang(JFrame parent, boolean modal) {
		super(parent, modal);
		setBounds(100, 100, 1107, 700);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(0, 0, 1093, 663);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Quản lý khách hàng");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 28));
		lblNewLabel.setBounds(422, 10, 256, 37);
		panel.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 342, 1073, 311);
		panel.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
			},
			new String[] {
				"M\u00E3 kh\u00E1ch h\u00E0ng", "H\u1ECD v\u00E0 t\u00EAn", "S\u1ED1 \u0111i\u1EC7n tho\u1EA1i", "CCCD", "email"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(70);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(2).setPreferredWidth(50);
		table.getColumnModel().getColumn(3).setResizable(false);
		table.getColumnModel().getColumn(3).setPreferredWidth(62);
		table.getColumnModel().getColumn(4).setResizable(false);
		table.getColumnModel().getColumn(4).setPreferredWidth(120);
		table.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		scrollPane.setViewportView(table);
		JTableHeader header = table.getTableHeader();
		header.setFont(new Font("Arial", Font.BOLD, 15));
		header.setPreferredSize(new Dimension(header.getWidth(), 30));
		// Căn giữa nội dung trong bảng
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		table.setRowHeight(28);
		// Bắt sự kiện click vào bảng để đổ dữ liệu vào các JTextField
		table.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int row = table.getSelectedRow();
		        if (row >= 0) {
		            // Lấy dữ liệu từ bảng
		            String maKH = table.getValueAt(row, 0).toString();
		            String hoTenKH = table.getValueAt(row, 1).toString();
		            String sdtKH = table.getValueAt(row, 2).toString();
		            String cccdKH = table.getValueAt(row, 3).toString();
		            String emailKH = table.getValueAt(row, 4).toString();

		            // Gán vào các JTextField
		            maKhachHang.setText(maKH);
		            hoTen.setText(hoTenKH);
		            sdt.setText(sdtKH);
		            cccd.setText(cccdKH);
		            email.setText(emailKH);
		        }
		    }
		});

		// Căn giữa header
		DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader()
				.getDefaultRenderer();
		headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setViewportView(table);
		
		JLabel lblDanhSchKhch = new JLabel("Danh sách khách hàng");
		lblDanhSchKhch.setFont(new Font("Times New Roman", Font.BOLD, 25));
		lblDanhSchKhch.setBounds(422, 295, 256, 37);
		panel.add(lblDanhSchKhch);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBounds(10, 63, 1073, 222);
		panel.add(panel_1);
		Border border = BorderFactory.createTitledBorder(
		        BorderFactory.createLineBorder(Color.BLACK),        
		        "Thông tin khách hàng",                               
		        TitledBorder.LEFT, TitledBorder.TOP,               
		        new Font("Times New Roman", Font.BOLD, 16),        
		        Color.BLACK                                         
		);
		panel_1.setBorder(border);
		panel_1.setLayout(null);
		
		JLabel lblMKhchHng = new JLabel("Mã khách hàng:");
		lblMKhchHng.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblMKhchHng.setBounds(55, 39, 166, 37);
		panel_1.add(lblMKhchHng);
		
		JLabel lblHVTn = new JLabel("Họ và tên:");
		lblHVTn.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblHVTn.setBounds(55, 86, 147, 37);
		panel_1.add(lblHVTn);
		
		JLabel lblSinThoi = new JLabel("Số điện thoại:");
		lblSinThoi.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblSinThoi.setBounds(55, 133, 147, 37);
		panel_1.add(lblSinThoi);
		
		JLabel lblCccd = new JLabel("CCCD:");
		lblCccd.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblCccd.setBounds(617, 46, 90, 37);
		panel_1.add(lblCccd);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblEmail.setBounds(617, 95, 90, 37);
		panel_1.add(lblEmail);
		
		maKhachHang = new JTextField();
		maKhachHang.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		maKhachHang.setEditable(false);
		maKhachHang.setBounds(231, 39, 305, 30);
		panel_1.add(maKhachHang);
		maKhachHang.setColumns(10);
		
		hoTen = new JTextField();
		hoTen.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		hoTen.setColumns(10);
		hoTen.setBounds(231, 89, 305, 30);
		panel_1.add(hoTen);
		
		sdt = new JTextField();
		sdt.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		sdt.setColumns(10);
		sdt.setBounds(231, 136, 305, 30);
		panel_1.add(sdt);
		
		cccd = new JTextField();
		cccd.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		cccd.setColumns(10);
		cccd.setBounds(717, 51, 305, 30);
		panel_1.add(cccd);
		
		email = new JTextField();
		email.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		email.setColumns(10);
		email.setBounds(717, 102, 305, 30);
		panel_1.add(email);
		
		JButton capNhat = new JButton("Cập nhật");
		capNhat.setBackground(new Color(128, 255, 128));
		capNhat.setFont(new Font("Times New Roman", Font.BOLD, 22));
		capNhat.setBounds(890, 175, 132, 37);
        capNhat.setOpaque(true);
        capNhat.setContentAreaFilled(true);
        capNhat.setBorderPainted(false);
		capNhat.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String maKh= maKhachHang.getText();
				String hoTenKH= hoTen.getText();
				String sdtKH= sdt.getText();
				String cccdKH=cccd.getText();
				String emailKH=email.getText();
				KhachHang khachHang= new KhachHang(maKh, hoTenKH, sdtKH, cccdKH, emailKH);
				KhachHang_Dao kHang_Dao= new KhachHang_Dao();
				boolean kq = kHang_Dao.sua(khachHang); // giả sử hàm `sua()` trả về true/false
		        if (kq) {
		            JOptionPane.showMessageDialog(null, "Cập nhật thông tin khách hàng thành công!");
		            loadKhachHangVaoTable(); // Load lại dữ liệu lên bảng
		        } else {
		            JOptionPane.showMessageDialog(null, "Cập nhật thất bại. Vui lòng kiểm tra lại dữ liệu!");
		        }				
			}
		});
		panel_1.add(capNhat);
		loadKhachHangVaoTable();
		setLocationRelativeTo(null);
		setResizable(false);
	}
	public void loadKhachHangVaoTable() {
	   KhachHang_Dao kHang_Dao= new KhachHang_Dao();
		ArrayList<KhachHang> ds =kHang_Dao.getAllKhachHang(); // Gọi hàm DAO
		DefaultTableModel model = (DefaultTableModel) table.getModel();
	    model.setRowCount(0); // Xóa dữ liệu cũ


	    for (KhachHang kh : ds) {
	        model.addRow(new Object[] {
	            kh.getMaKH(),
	            kh.getHoTen(),
	            kh.getSdt(),
	            kh.getSoCCCD(),
	            kh.getEmail()
	        });
	    }
	}

}
