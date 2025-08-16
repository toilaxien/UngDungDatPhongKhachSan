package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;

public class ThanhToanChiTiet extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField maDon;
	private JTextField tenKhanh;
	private JTextField sdt;
	private JTextField ngayNhan;
	private JTextField ngayTra;
	private JTextField maPhong;
	private JTextField tongThanhToan;
	private JTextField maGiam;
	private JTextField ngayDatPhong;

	public static void main(String[] args) {
		try {
			ThanhToanChiTiet dialog = new ThanhToanChiTiet();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setThongTinDatPhong("DP001", "Nguyễn Văn A", "0123456789", "P102, P102", "2025-05-20", "2025-05-22",
					"2025-05-25", "KM2025", "2,500,000 VND");
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ThanhToanChiTiet() {
		setBounds(100, 100, 627, 616);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 255, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Thông tin đơn đặt phòng");
		lblNewLabel.setBounds(164, 10, 310, 48);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 28));
		lblNewLabel.setBackground(new Color(255, 255, 255));
		contentPanel.add(lblNewLabel);

		JLabel lblMnt = new JLabel("Mã đơn đặt phòng:");
		lblMnt.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblMnt.setBackground(Color.WHITE);
		lblMnt.setBounds(28, 68, 259, 48);
		contentPanel.add(lblMnt);

		JLabel lblHVTn = new JLabel("Khách hàng:");
		lblHVTn.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblHVTn.setBackground(Color.WHITE);
		lblHVTn.setBounds(28, 111, 259, 48);
		contentPanel.add(lblHVTn);

		JLabel lblSt = new JLabel("SĐT:");
		lblSt.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblSt.setBackground(Color.WHITE);
		lblSt.setBounds(28, 153, 259, 48);
		contentPanel.add(lblSt);

		JLabel lblNgyNhnPhng = new JLabel("Ngày nhận phòng:");
		lblNgyNhnPhng.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblNgyNhnPhng.setBackground(Color.WHITE);
		lblNgyNhnPhng.setBounds(28, 325, 259, 48);
		contentPanel.add(lblNgyNhnPhng);

		JLabel lblNgyTrPhng = new JLabel("Ngày trả phòng:");
		lblNgyTrPhng.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblNgyTrPhng.setBackground(Color.WHITE);
		lblNgyTrPhng.setBounds(28, 370, 259, 48);
		contentPanel.add(lblNgyTrPhng);

		maDon = new JTextField();
		maDon.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		maDon.setEditable(false);
		maDon.setBounds(263, 80, 304, 34);
		contentPanel.add(maDon);
		maDon.setColumns(10);

		tenKhanh = new JTextField();
		tenKhanh.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		tenKhanh.setEditable(false);
		tenKhanh.setColumns(10);
		tenKhanh.setBounds(263, 125, 304, 34);
		contentPanel.add(tenKhanh);

		sdt = new JTextField();
		sdt.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		sdt.setEditable(false);
		sdt.setColumns(10);
		sdt.setBounds(263, 169, 304, 34);
		contentPanel.add(sdt);

		ngayNhan = new JTextField();
		ngayNhan.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		ngayNhan.setEditable(false);
		ngayNhan.setColumns(10);
		ngayNhan.setBounds(263, 340, 304, 34);
		contentPanel.add(ngayNhan);

		ngayTra = new JTextField();
		ngayTra.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		ngayTra.setEditable(false);
		ngayTra.setColumns(10);
		ngayTra.setBounds(263, 384, 304, 34);
		contentPanel.add(ngayTra);

		JLabel lblTngThanhTon = new JLabel("Tổng thanh toán");
		lblTngThanhTon.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblTngThanhTon.setBackground(Color.WHITE);
		lblTngThanhTon.setBounds(244, 410, 230, 48);
		contentPanel.add(lblTngThanhTon);

		JLabel lblThngTinPhng = new JLabel("Thông tin phòng");
		lblThngTinPhng.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblThngTinPhng.setBackground(Color.WHITE);
		lblThngTinPhng.setBounds(244, 211, 230, 48);
		contentPanel.add(lblThngTinPhng);

		JLabel lblMGimGi = new JLabel("Mã giảm giá áp dụng:");
		lblMGimGi.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblMGimGi.setBackground(Color.WHITE);
		lblMGimGi.setBounds(28, 456, 259, 48);
		contentPanel.add(lblMGimGi);

		maPhong = new JTextField();
		maPhong.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		maPhong.setEditable(false);
		maPhong.setColumns(10);
		maPhong.setBounds(263, 258, 304, 34);
		contentPanel.add(maPhong);

		JLabel lblMPhng = new JLabel("Mã phòng:");
		lblMPhng.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblMPhng.setBackground(Color.WHITE);
		lblMPhng.setBounds(28, 247, 259, 48);
		contentPanel.add(lblMPhng);

		JLabel lblTngThanhTon_1 = new JLabel("Tổng thanh toán:");
		lblTngThanhTon_1.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblTngThanhTon_1.setBackground(Color.WHITE);
		lblTngThanhTon_1.setBounds(28, 498, 259, 48);
		contentPanel.add(lblTngThanhTon_1);

		tongThanhToan = new JTextField();
		tongThanhToan.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		tongThanhToan.setEditable(false);
		tongThanhToan.setColumns(10);
		tongThanhToan.setBounds(263, 512, 304, 34);
		contentPanel.add(tongThanhToan);

		maGiam = new JTextField();
		maGiam.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		maGiam.setEditable(false);
		maGiam.setColumns(10);
		maGiam.setBounds(263, 468, 304, 34);
		contentPanel.add(maGiam);

		JLabel lblNgytPhng = new JLabel("Ngày đặt phòng:");
		lblNgytPhng.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblNgytPhng.setBackground(Color.WHITE);
		lblNgytPhng.setBounds(28, 284, 259, 48);
		contentPanel.add(lblNgytPhng);

		ngayDatPhong = new JTextField();
		ngayDatPhong.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		ngayDatPhong.setEditable(false);
		ngayDatPhong.setColumns(10);
		ngayDatPhong.setBounds(263, 299, 304, 34);
		contentPanel.add(ngayDatPhong);
		setLocationRelativeTo(null);
		setResizable(false);
	}

	public void setThongTinDatPhong(String maDonDatPhong, String tenKhachHang, String soDienThoai, String maPhong,
			String ngayDat, String ngayNhan, String ngayTra, String maGiamGia, String tongTien) {
		this.maDon.setText(maDonDatPhong);
		this.tenKhanh.setText(tenKhachHang);
		this.sdt.setText(soDienThoai);
		this.maPhong.setText(maPhong);
		this.ngayDatPhong.setText(ngayDat);
		this.ngayNhan.setText(ngayNhan);
		this.ngayTra.setText(ngayTra);
		this.maGiam.setText(maGiamGia);
		this.tongThanhToan.setText(tongTien);
	}

}
