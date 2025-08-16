package traPhong;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dao.ChiTietPhieuDichVu_DAO;
import dao.DichVu_Dao;
import entity.ChiTietPhieuDichVu;
import entity.DichVu;

public class phieuDichVu_Dialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel phieuDichVu = new JPanel();
	private JTextField maPhieuDichVu;
	private JTextField loaiDichVu;
	private JTextField ngayLapPhieu;
	private JTable table_PhieuDichVu;
	private JTextField soLuongDichVu;
	private JTextField tongTienDV;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
//			phieuDichVu_Dialog dialog = new phieuDichVu_Dialog();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//			dialog.setLocationRelativeTo(null);
//			dialog.setResizable(false);
//			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public phieuDichVu_Dialog(JFrame parent, boolean modal) {
        super(parent, modal); // GỌI super tại đây
        phieuDichVu();

    }
	public phieuDichVu_Dialog(String maPhieu, String loaiDV, String ngayLap, JFrame parent, boolean modal) {
		this(parent, true); 
		maPhieuDichVu.setText(maPhieu);
		loaiDichVu.setText(loaiDV);
		ngayLapPhieu.setText(ngayLap);
		hienThiChiTietDichVu(maPhieu, loaiDV);
		
	}

	/**
	 * Create the dialog.
	 */
	public void phieuDichVu() {
		setBounds(100, 100, 775, 524);
		setUndecorated(true);
		getContentPane().setLayout(new BorderLayout());
		phieuDichVu.setBackground(new Color(255, 255, 255));
		phieuDichVu.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(phieuDichVu, BorderLayout.CENTER);
		phieuDichVu.setLayout(null);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 3); // Tạo viền đen, dày 2 pixel
        getRootPane().setBorder(border);
		JLabel lblNewLabel = new JLabel("Chi tiết phiếu dịch vụ");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
		lblNewLabel.setBounds(272, 0, 241, 85);
		phieuDichVu.add(lblNewLabel);
		
		JButton tat = new JButton("X");
		tat.setFont(new Font("Times New Roman", Font.BOLD, 20));
		tat.setBounds(707, 10, 58, 43);
		tat.setFocusPainted(false);      
		tat.setBorderPainted(false);    
		tat.setContentAreaFilled(false); 
		tat.setOpaque(false);  
		phieuDichVu.add(tat);
		tat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(tat);
				dialog.dispose();
			}
		});
		JLabel lblNewLabel_1 = new JLabel("Mã phiếu dịch vụ:");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1.setBounds(92, 75, 183, 50);
		phieuDichVu.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Loại dịch vụ:");
		lblNewLabel_1_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1_1.setBounds(92, 111, 183, 50);
		phieuDichVu.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Ngày lập phiếu:");
		lblNewLabel_1_1_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1_1_1.setBounds(92, 147, 183, 50);
		phieuDichVu.add(lblNewLabel_1_1_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(35, 254, 701, 191);
		phieuDichVu.add(scrollPane);
		
		table_PhieuDichVu = new JTable();
		table_PhieuDichVu.setFont(new Font("Times New Roman", Font.BOLD, 20));
		table_PhieuDichVu.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"M\u00E3 d\u1ECBch v\u1EE5", "T\u00EAn d\u1ECBch v\u1EE5", "Gi\u00E1 d\u1ECBch v\u1EE5", "Số lượng"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		table_PhieuDichVu.setRowHeight(25);
		table_PhieuDichVu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JTableHeader header =table_PhieuDichVu.getTableHeader();
		header.setFont(new Font("Times New Roman", Font.BOLD, 18));

		// Căn giữa nội dung trong bảng
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < table_PhieuDichVu.getColumnCount(); i++) {
			table_PhieuDichVu.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		// Căn giữa header
		DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table_PhieuDichVu.getTableHeader().getDefaultRenderer();
		headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setViewportView(table_PhieuDichVu);
		
		maPhieuDichVu = new JTextField();
		maPhieuDichVu.setEditable(false);
		maPhieuDichVu.setFont(new Font("Times New Roman", Font.BOLD, 20));
		maPhieuDichVu.setBounds(282, 84, 330, 27);
		phieuDichVu.add(maPhieuDichVu);
		maPhieuDichVu.setColumns(10);
		
		loaiDichVu = new JTextField();
		loaiDichVu.setEditable(false);
		loaiDichVu.setFont(new Font("Times New Roman", Font.BOLD, 20));
		loaiDichVu.setColumns(10);
		loaiDichVu.setBounds(282, 120, 330, 27);
		phieuDichVu.add(loaiDichVu);
		
		ngayLapPhieu = new JTextField();
		ngayLapPhieu.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ngayLapPhieu.setEditable(false);
		ngayLapPhieu.setColumns(10);
		ngayLapPhieu.setBounds(282, 156, 330, 27);
		phieuDichVu.add(ngayLapPhieu);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Số lượng dịch vụ:");
		lblNewLabel_1_1_1_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1_1_1_1.setBounds(92, 182, 183, 50);
		phieuDichVu.add(lblNewLabel_1_1_1_1);
		
		soLuongDichVu = new JTextField();
		soLuongDichVu.setEditable(false);
		soLuongDichVu.setFont(new Font("Times New Roman", Font.BOLD, 20));
		soLuongDichVu.setColumns(10);
		soLuongDichVu.setBounds(282, 193, 330, 27);
		phieuDichVu.add(soLuongDichVu);
		
		JLabel lblNewLabel_1_1_1_1_1 = new JLabel("Tổng tiền:");
		lblNewLabel_1_1_1_1_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1_1_1_1_1.setBounds(309, 455, 135, 50);
		phieuDichVu.add(lblNewLabel_1_1_1_1_1);
		
		tongTienDV = new JTextField();
		tongTienDV.setEditable(false);
		tongTienDV.setFont(new Font("Times New Roman", Font.BOLD, 20));
		tongTienDV.setColumns(10);
		tongTienDV.setBounds(415, 466, 321, 27);
		phieuDichVu.add(tongTienDV);
	}
	private DichVu_Dao dichVuDAO = new DichVu_Dao(); // thêm ở đầu class
	public void hienThiChiTietDichVu(String maPhieu, String maLoai) {
		DefaultTableModel model = (DefaultTableModel) table_PhieuDichVu.getModel();
		model.setRowCount(0); // clear dữ liệu cũ

        ArrayList<DichVu> danhSach = dichVuDAO.getDichVuTheoPhieuVaLoai(maPhieu, maLoai);
		double tongTien = 0;
		int soluong=0;
		ChiTietPhieuDichVu_DAO ctpdv_DAO= new ChiTietPhieuDichVu_DAO();
		for (DichVu dv : danhSach) {
			ChiTietPhieuDichVu ctpdv= ctpdv_DAO.getChiTietPhieuDichVu(maPhieu, dv.getMaDV());
			Object[] row = {
				dv.getMaDV(),
				dv.getTenDV(),
				String.format("%,.0f", dv.getGiaDV()),
				ctpdv.getSoLuong()
			};
			model.addRow(row);
			soluong +=ctpdv.getSoLuong();
			tongTien += (dv.getGiaDV()*ctpdv.getSoLuong());
		}

		soLuongDichVu.setText(String.valueOf(soluong));
		tongTienDV.setText(String.format("%,.0f", tongTien));
	}

}
