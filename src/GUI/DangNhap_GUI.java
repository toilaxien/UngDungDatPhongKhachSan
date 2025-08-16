package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.UIManager;

import connectDB.ConnectDB;
import dao.TaiKhoan_Dao;
import entity.NhanVien;
import entity.TaiKhoan;
import motSoCNLT.quenMatKhau_dialog;
import quanLy.GDQuanLy_Gui;

import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;

import java.awt.SystemColor;

public class DangNhap_GUI extends JFrame implements KeyListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel jPanel_All;
	private JButton jButton_Login, jButton_clear;
	private PlaceholderTextField text_User;
	private PlaceholderPasswordField text_Password;
	private JCheckBox cbManager;

	public DangNhap_GUI() {
		super("Hotel Lux");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);
		setLocationRelativeTo(null);
		setResizable(false);
		// Set logo
		ImageIcon icon = new ImageIcon("img/HinhAnhGiaoDienChinh/logo.png");
		this.setIconImage(icon.getImage());

		jPanel_All = new JPanel();
		jPanel_All.setForeground(new Color(255, 255, 255));
		jPanel_All.setBackground(new Color(255, 255, 255));
		jPanel_All.setBorder(null);

		getContentPane().add(jPanel_All);
		jPanel_All.setLayout(new BorderLayout(0, 0));

		JPanel jPanel_Login = new JPanel();
		jPanel_Login.setBackground(new Color(255, 255, 255));
		jPanel_All.add(jPanel_Login);
		jPanel_Login.setLayout(null);

		text_User = new PlaceholderTextField("Tên tài khoản");
		text_User.setBackground(new Color(192, 192, 192));
		text_User.setFont(new Font("Tahoma", Font.PLAIN, 15));
		text_User.setBounds(300, 247, 400, 36);
		jPanel_Login.add(text_User);
		text_User.setColumns(20);

		JCheckBox cbHidePassword = new JCheckBox("Hiện mật khẩu !");
		cbHidePassword.setForeground(new Color(255, 255, 255));
		cbHidePassword.setBackground(new Color(255, 255, 255));
		cbHidePassword.setBounds(299, 367, 120, 34);
		cbHidePassword.setOpaque(false);
		jPanel_Login.add(cbHidePassword);

		cbHidePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbHidePassword.isSelected()) {
					// Nếu được chọn, ẩn mật khẩu
					text_Password.setEchoChar((char) 0);
				} else {
					// Nếu không được chọn, hiển thị mật khẩu
					text_Password.setEchoChar('\u25cf');
				}
			}
		});
		
	    JButton jButton_ForgotPassword = new JButton("Quên mật khẩu?");
	    jButton_ForgotPassword.setForeground(new Color(255, 255, 255));
	    jButton_ForgotPassword.setContentAreaFilled(false);
	    jButton_ForgotPassword.setOpaque(false);
	    jButton_ForgotPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
	    
	    jButton_ForgotPassword.setBounds(570, 367, 150, 34); // Điều chỉnh vị trí
	    jButton_ForgotPassword.setBorderPainted(false);
	    jButton_ForgotPassword.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
				quenMatKhau_dialog dialog = new quenMatKhau_dialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
				
			}
		});
	    
	    jPanel_Login.add(jButton_ForgotPassword);

		jButton_Login = new JButton("Đăng nhập");
		jButton_Login.setForeground(new Color(255, 255, 255));
		jButton_Login.setContentAreaFilled(false);
		jButton_Login.setOpaque(true);

		jButton_Login.setBackground(new Color(0, 255, 64));
		jButton_Login.setFont(new Font("Tahoma", Font.BOLD, 18));
		jButton_Login.setBounds(435, 445, 140, 45);
		jPanel_Login.add(jButton_Login);

		text_Password = new PlaceholderPasswordField("Mật khẩu");
		text_Password.setBounds(300, 315, 400, 36);
		jPanel_Login.add(text_Password);



		JLabel jLabel_Title = new JLabel("         Welcome to Lux !");
		jLabel_Title.setForeground(SystemColor.textHighlightText);
		jLabel_Title.setFont(new Font("Monotype Corsiva", Font.BOLD, 70));
		jLabel_Title.setBounds(150, 127, 730, 72);
		jPanel_Login.add(jLabel_Title);

		JPanel transparentPanel = new JPanel();
		transparentPanel.setBackground(new Color(0, 0, 0, 90));
		transparentPanel.setBounds(118, 82, 764, 477);
		jPanel_Login.add(transparentPanel);

		JLabel lblNewLabel = new JLabel("");


		// Resize ảnh
		ImageIcon icon1 = resizeImage(
				"img/HinhAnhGiaoDienChinh/nenDangNhap.jpg",
				1000, 1000);
		lblNewLabel.setIcon(icon1);
		lblNewLabel.setBounds(0, 0, 1541, 841);
		jPanel_Login.add(lblNewLabel);

		jButton_Login.addActionListener(this);
		setVisible(true);

	}

	public static void main(String[] args) {
		new DangNhap_GUI();

	}

	public class PlaceholderTextField extends JTextField {
		private static final long serialVersionUID = 1L;
		private String placeholder;

		public PlaceholderTextField(String placeholder) {
			this.placeholder = placeholder;
		}

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
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() == text_User) {
				text_Password.requestFocus();
			} else if (e.getSource() == text_Password) {
				jButton_Login.doClick();
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o = e.getSource();


		String mkNhapVao=text_Password.getText();
		String tkNhapVao=text_User.getText();
		if(o.equals(jButton_Login)) {
			TaiKhoan_Dao tKhoan_DAO= new TaiKhoan_Dao();
			boolean kiemTraTK= tKhoan_DAO.kiemTraTaiKhoan(tkNhapVao, mkNhapVao);
			if(kiemTraTK) {
				TaiKhoan_Dao tK= new TaiKhoan_Dao();
				TaiKhoan tk= tK.getTaiKhoan(tkNhapVao, mkNhapVao);
				String chucVu= tk.getNhanVien().getChucVu();
				
				System.out.println("Chức vụ: " + chucVu);
				if(tk.getTrangThai().equals("Vô hiệu hóa")) {
					JOptionPane.showMessageDialog(null, "Tài khoản đã bị vô hiệu hóa!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
				}else {
					if(chucVu.equals("Quản lý")) {
						this.dispose();
						GDQuanLy_Gui gdQuanLy = new GDQuanLy_Gui();
						gdQuanLy.showWindow();
					}else if (chucVu.equals("Lễ tân")) {
						  // <- thêm dòng này 
		                this.dispose();
		                GiaoDienChinh giaoDien = new GiaoDienChinh();
		                giaoDien.showWindow();
					}
				}

			}else {
		        // Trường hợp không tìm thấy tài khoản
		        JOptionPane.showMessageDialog(null, "Tài khoản hoặc mật khẩu không chính xác!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
		    }
		}

	}

	private static ImageIcon resizeImage(String imagePath, int width, int height) {
		try {
	        ImageIcon icon = new ImageIcon(imagePath);
			Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
			return new ImageIcon(scaledImage);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
