package motSoCNLT;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import GUI.DangNhap_GUI;
import dao.NhanVien_Dao;
import dao.TaiKhoan_Dao;
import entity.NhanVien;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JCheckBox;

public class quenMatKhau_dialog extends JDialog implements KeyListener {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField maNhanVien;
	private JTextField cccd;
	private JTextField sdt;
	private JTextField taiKhoan;
	private JTextField tenTaiKhoan;
	private JPasswordField matKhauMoi;
	private JPasswordField nhapLaiMK;
	private Image backgroundImage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			quenMatKhau_dialog dialog = new quenMatKhau_dialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public quenMatKhau_dialog() {
		setBounds(100, 100, 1000, 700);
		backgroundImage = new ImageIcon("img/HinhAnhGiaoDienChinh/nenDangNhap.jpg").getImage(); // Đường dẫn đúng

		JPanel backgroundPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
			}
		};
		backgroundPanel.setLayout(null);
		setContentPane(backgroundPanel);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel panel_CapNhatMatKhau = new JPanel();
		panel_CapNhatMatKhau.setBackground(new Color(255, 255, 255));
		panel_CapNhatMatKhau.setBounds(183, 146, 619, 323);
		backgroundPanel.add(panel_CapNhatMatKhau);
		panel_CapNhatMatKhau.setLayout(null);

		JLabel lblNewLabel = new JLabel("Nhập lại mật khẩu");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
		lblNewLabel.setBounds(199, 25, 213, 35);
		panel_CapNhatMatKhau.add(lblNewLabel);

		JLabel lblTnTiKhon = new JLabel("Tên tài khoản:");
		lblTnTiKhon.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblTnTiKhon.setBounds(26, 70, 213, 35);
		panel_CapNhatMatKhau.add(lblTnTiKhon);

		JLabel lblMtKhuMi = new JLabel("Mật khẩu mới:");
		lblMtKhuMi.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblMtKhuMi.setBounds(26, 133, 213, 35);
		panel_CapNhatMatKhau.add(lblMtKhuMi);

		tenTaiKhoan = new JTextField();
		tenTaiKhoan.setEditable(false);
		tenTaiKhoan.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		tenTaiKhoan.setBounds(217, 70, 340, 35);
		panel_CapNhatMatKhau.add(tenTaiKhoan);
		tenTaiKhoan.setColumns(10);

		matKhauMoi = new JPasswordField();
		matKhauMoi.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		matKhauMoi.setColumns(10);
		matKhauMoi.setBounds(217, 127, 243, 35);
		panel_CapNhatMatKhau.add(matKhauMoi);

		JLabel lblNhpLiMt = new JLabel("Nhập lại mật khẩu:");
		lblNhpLiMt.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblNhpLiMt.setBounds(26, 192, 213, 35);
		panel_CapNhatMatKhau.add(lblNhpLiMt);

		nhapLaiMK = new JPasswordField();
		nhapLaiMK.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		nhapLaiMK.setColumns(10);
		nhapLaiMK.setBounds(217, 192, 243, 35);
		panel_CapNhatMatKhau.add(nhapLaiMK);
		panel_CapNhatMatKhau.setVisible(false);

		JButton capNhat = new JButton("Cập nhật");
		capNhat.setBackground(new Color(128, 255, 128));
		capNhat.setFont(new Font("Times New Roman", Font.BOLD, 22));
		capNhat.setBounds(434, 256, 135, 35);

		capNhat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String mkNew = matKhauMoi.getText().trim();
				String nhapLai = nhapLaiMK.getText().trim();
				String tenTK= tenTaiKhoan.getText().trim();
				if(!mkNew.equals(nhapLai)) {
					JOptionPane.showMessageDialog(null, "Mật khẩu chưa đúng.", "Thông báo",
							JOptionPane.WARNING_MESSAGE);
					nhapLaiMK.setText("");
					matKhauMoi.setText("");
					return;
				}
			    TaiKhoan_Dao tKhoan_DAO = new TaiKhoan_Dao();
			    boolean trungMatKhauCu = tKhoan_DAO.kiemTraMatKhauCu(tenTK, mkNew);

			    if (trungMatKhauCu) {
			        JOptionPane.showMessageDialog(null, "Mật khẩu mới trùng với mật khẩu cũ. Vui lòng nhập lại.", "Thông báo",
			                JOptionPane.WARNING_MESSAGE);
			        nhapLaiMK.setText("");
			        matKhauMoi.setText("");
			        return;
			    }

			    try {
			        tKhoan_DAO.capNhatTaiKhoan(mkNew, tenTK);
			        JOptionPane.showMessageDialog(null, "Cập nhật mật khẩu thành công!", "Thông báo",
			                JOptionPane.INFORMATION_MESSAGE);
                     dispose();
                     new DangNhap_GUI();
			    } catch (SQLException e1) {
			        e1.printStackTrace();
			        JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật mật khẩu!", "Lỗi",
			                JOptionPane.ERROR_MESSAGE);
			    }

			}
		});
		panel_CapNhatMatKhau.add(capNhat);

		JCheckBox hienMatNew = new JCheckBox("Hiện mật khẩu");
		hienMatNew.setBackground(new Color(255, 255, 255));
		hienMatNew.setFont(new Font("Times New Roman", Font.BOLD, 15));
		hienMatNew.setBounds(470, 132, 121, 31);
		hienMatNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (hienMatNew.isSelected()) {
					// Nếu được chọn, ẩn mật khẩu
					matKhauMoi.setEchoChar((char) 0);
				} else {
					// Nếu không được chọn, hiển thị mật khẩu
					matKhauMoi.setEchoChar('\u25cf');
				}
			}
		});
		panel_CapNhatMatKhau.add(hienMatNew);

		JCheckBox hienMatKhauTam = new JCheckBox("Hiện mật khẩu");
		hienMatKhauTam.setFont(new Font("Times New Roman", Font.BOLD, 15));
		hienMatKhauTam.setBackground(Color.WHITE);
		hienMatKhauTam.setBounds(470, 192, 121, 31);
		hienMatKhauTam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (hienMatNew.isSelected()) {
					// Nếu được chọn, ẩn mật khẩu
					nhapLaiMK.setEchoChar((char) 0);
				} else {
					// Nếu không được chọn, hiển thị mật khẩu
					nhapLaiMK.setEchoChar('\u25cf');
				}
			}
		});
		panel_CapNhatMatKhau.add(hienMatKhauTam);

		JPanel panel_XacThuc = new JPanel();
		panel_XacThuc.setBackground(new Color(255, 255, 255));
		panel_XacThuc.setBounds(183, 146, 619, 328);
		backgroundPanel.add(panel_XacThuc);
		panel_XacThuc.setLayout(null);

		JLabel lblNewLabel1 = new JLabel("Xác thực tài khoản");
		lblNewLabel1.setFont(new Font("Times New Roman", Font.BOLD, 25));
		lblNewLabel1.setBounds(193, 20, 238, 29);
		panel_XacThuc.add(lblNewLabel1);

		JLabel lblNewLabel_1 = new JLabel("Mã nhân viên:");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1.setBounds(27, 72, 238, 29);
		panel_XacThuc.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Căn cước công dân:");
		lblNewLabel_1_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1_1.setBounds(27, 112, 238, 29);
		panel_XacThuc.add(lblNewLabel_1_1);

		JLabel SĐT = new JLabel("Số điện thoại:");
		SĐT.setFont(new Font("Times New Roman", Font.BOLD, 20));
		SĐT.setBounds(27, 152, 238, 29);
		panel_XacThuc.add(SĐT);

		JLabel lblNewLabel_1_1_1_1 = new JLabel("Tên tài khoản:");
		lblNewLabel_1_1_1_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1_1_1_1.setBounds(27, 192, 238, 29);
		panel_XacThuc.add(lblNewLabel_1_1_1_1);

		maNhanVien = new JTextField();
		maNhanVien.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		maNhanVien.setBounds(212, 71, 362, 30);
		panel_XacThuc.add(maNhanVien);
		maNhanVien.setColumns(10);

		cccd = new JTextField();
		cccd.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		cccd.setColumns(10);
		cccd.setBounds(212, 111, 362, 30);
		panel_XacThuc.add(cccd);

		sdt = new JTextField();
		sdt.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		sdt.setColumns(10);
		sdt.setBounds(212, 151, 362, 30);
		panel_XacThuc.add(sdt);

		taiKhoan = new JTextField();
		taiKhoan.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		taiKhoan.setColumns(10);
		taiKhoan.setBounds(212, 191, 362, 30);
		panel_XacThuc.add(taiKhoan);

		JButton xacThuc = new JButton("Xác thực");
		xacThuc.setBackground(new Color(128, 255, 128));
		xacThuc.setFont(new Font("Times New Roman", Font.BOLD, 20));
		xacThuc.setBounds(456, 266, 119, 40);

		xacThuc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String maNV = maNhanVien.getText().trim();
				String cCCD = cccd.getText().trim();
				String SDT = sdt.getText().trim();
				String tenTK = taiKhoan.getText();
				if (maNV.isEmpty() || cCCD.isEmpty() || SDT.isEmpty() || tenTK.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!", "Thiếu thông tin",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				NhanVien_Dao nVien_DAO = new NhanVien_Dao();
				NhanVien aNhanVien = nVien_DAO.timNhanVien(maNV, cCCD, SDT, tenTK);
				if (aNhanVien != null) {
					tenTaiKhoan.setText(tenTK);
					panel_CapNhatMatKhau.setVisible(true);
					panel_XacThuc.setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "Thông tin không hợp lệ.", "Thông báo",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});
		panel_XacThuc.add(xacThuc);
		setLocationRelativeTo(null);
		setResizable(false);

	}

	public class PlaceholderPasswordField extends JPasswordField {
		private static final long serialVersionUID = 1L;
		private String placeholder;

		public PlaceholderPasswordField(String placeholder) {
			this.placeholder = placeholder;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (getText().isEmpty()) {
				g.setColor(Color.GRAY);
				g.setFont(new Font("Arial", Font.PLAIN, 16));
				g.drawString(placeholder, getInsets().left, g.getFontMetrics().getHeight() + getInsets().top);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
