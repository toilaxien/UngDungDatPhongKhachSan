package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import dao.ChiTietDonDatPhong_Dao;
import dao.DonDatPhong_Dao;
import dao.LoaiPhong_Dao;
import dao.Phong_Dao;
import entity.ChiTietDonDatPhong;
import entity.DonDatPhong;
import entity.LoaiPhong;
import entity.Phong;

public class HuyPhong_GUI extends JFrame implements ActionListener{
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	DonDatPhong_Dao donDatPhongDao = new DonDatPhong_Dao();
//	public static void main(String[] args) {
//
//		String maDonDatPhong = "23052025LT001003";
//		DonDatPhong_Dao donDatPhong_Dao = new DonDatPhong_Dao();
//		DonDatPhong donDatPhong = donDatPhong_Dao.timDonTheoMa(maDonDatPhong);
//		SwingUtilities.invokeLater(() -> {
//			new HuyPhong_GUI().taoDonHuyPhong(donDatPhong);
//		});
//	}

	public void hienThiDonDatPhong(DonDatPhong don, ArrayList<LoaiPhong> dsLoaiPhong, JPanel roomPanel) {
		LoaiPhong_Dao loaiPhongDao = new LoaiPhong_Dao();
		dsLoaiPhong = loaiPhongDao.getAllLoaiPhong();
		Phong_Dao phongDao = new Phong_Dao();

		roomPanel.removeAll(); // Xoá sạch để làm mới giao diện
		roomPanel.setLayout(new BorderLayout());
		roomPanel.setBorder(BorderFactory.createTitledBorder("Thông tin phòng"));
		ChiTietDonDatPhong_Dao dao = new ChiTietDonDatPhong_Dao();
		ArrayList<ChiTietDonDatPhong> chiTietList = dao.getChiTietDonDatPhongTheoMaDon(don.getMaDonDatPhong());
		Object[][] roomData = new Object[chiTietList.size()][4];

		ZoneId zone = ZoneId.systemDefault();
		LocalDateTime nhanPhong = don.getNgayNhanPhong();
		LocalDateTime traPhong = don.getNgayTraPhong();

		// Lấy ngày từ LocalDateTime để tính số ngày giữa
		long soNgay = ChronoUnit.DAYS.between(nhanPhong.toLocalDate(), traPhong.toLocalDate());

		// Nếu giờ checkout > giờ checkin ⇒ tính thêm 1 ngày nếu muốn làm tròn
		if (traPhong.toLocalTime().isAfter(nhanPhong.toLocalTime())) {
		    soNgay++;
		}

		if (soNgay == 0) soNgay = 1; // Tối thiểu 1 ngày

		// Tính số giờ
		long soGio = Duration.between(nhanPhong, traPhong).toHours();
		if (soGio == 0) soGio = 1;

		// Số đêm (đơn giản bằng số ngày ngủ lại)
		long soDem = ChronoUnit.DAYS.between(nhanPhong.toLocalDate(), traPhong.toLocalDate());
		if (soDem == 0) soDem = 1;


		for (int i = 0; i < chiTietList.size(); i++) {
			ChiTietDonDatPhong ct = chiTietList.get(i);
			Phong phong = ct.getPhong();
			phong = phongDao.getPhongTheoMa(phong.getSoPhong());
			LoaiPhong loai = loaiPhongDao.getLoaiPhongTheoMa(phong.getLoaiPhong().getMaLoaiPhong());

			roomData[i][0] = phong.getSoPhong();
			roomData[i][1] = loai.getTenLoai();

			switch (don.getLoaiDon()) {
			case "Theo giờ":
				roomData[i][2] = soGio;
				roomData[i][3] = String.format("%,.0f VND", loai.getGiaTheoGio());
				break;
			case "Theo ngày":
				roomData[i][2] = soNgay;
				roomData[i][3] = String.format("%,.0f VND", loai.getGiaTheoNgay());
				break;
			case "Theo đêm":
				roomData[i][2] = soNgay;
				roomData[i][3] = String.format("%,.0f VND", loai.getGiaTheoDem());
				break;
			}
		}

		String donLoai = don.getLoaiDon(); // giả sử GIO, NGAY, DEM

		String soLuongText = switch (donLoai) {
		    case "Theo giờ" -> "giờ";
		    case "Theo đêm" -> "đêm";
		    default -> "ngày";
		};
		String[] roomHeaders = { "Mã Phòng", "Loại phòng", "Số " + soLuongText, "Giá phòng" };
		
		DefaultTableModel model = new DefaultTableModel(roomData, roomHeaders);
		JTable roomTable = new JTable(model);

		roomTable.setEnabled(false);
		roomTable.setRowHeight(25);
		roomTable.setFont(new Font("Arial", Font.PLAIN, 13));
		roomTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

		JScrollPane scroll = new JScrollPane(roomTable);
		scroll.setPreferredSize(new Dimension(400, 60));

		double tongTien = don.tinhTienPhong();
		JLabel tongTienLabel = new JLabel(String.format("Tổng tiền thuê: %,.0f VND", tongTien));
		tongTienLabel.setFont(new Font("Arial", Font.BOLD, 13));
		tongTienLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

		roomPanel.add(scroll, BorderLayout.CENTER);
		roomPanel.add(tongTienLabel, BorderLayout.SOUTH);

		roomPanel.revalidate();
		roomPanel.repaint();
	}

	public static JPanel taoCancelFeePanel(DonDatPhong don) {
		JPanel cancelFeePanel = new JPanel(new BorderLayout());
		cancelFeePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Chi phí hủy phòng", TitledBorder.LEFT, TitledBorder.TOP));

		// Định dạng ngày giờ hiện tại
		LocalDateTime now = LocalDateTime.now();
		String ngayHuy = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		String thoiGianHuy = now.format(DateTimeFormatter.ofPattern("HH:mm"));
		
		// Hủy trước bao lâu
		Duration duration = Duration.between(now, don.getNgayNhanPhong());
		long soGio = duration.toHours();
		long soNgay = duration.toDays();
		String huyTruoc = soNgay + " ngày " + (soGio % 24) + " giờ";

		
		// Tạo dữ liệu bảng
		String[] headers = { "Ngày hủy phòng", "Thời gian hủy", "Hủy trước bao lâu", "Phí hủy", "Số tiền hoàn cọc" };
		Object[][] data = { { ngayHuy, thoiGianHuy, huyTruoc ,String.format("%,.0f VND", don.phiHuyPhong(now, don.getNgayNhanPhong())), String.format("%,.0f VND", don.tinhTienHoanCoc()) } };

		// Tạo bảng bằng DefaultTableModel
		DefaultTableModel model = new DefaultTableModel(data, headers);
		JTable feeTable = new JTable(model);
		feeTable.setEnabled(false);
		feeTable.setRowHeight(25);
		feeTable.setFont(new Font("Arial", Font.PLAIN, 13));
		feeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

		// Đặt bảng vào scroll pane
		JScrollPane scrollCancel = new JScrollPane(feeTable);
		scrollCancel.setPreferredSize(new Dimension(400, 60));

		cancelFeePanel.add(scrollCancel, BorderLayout.CENTER);
		return cancelFeePanel;
	}

	public void taoDonHuyPhong(DonDatPhong donDatPhong, Runnable reloadCallback) {
		JFrame frame = new JFrame("Đơn Hủy Phòng");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1000, 700);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// I. Thông tin khách hàng
		JPanel customerPanel = new JPanel(new GridLayout(6, 2, 5, 5));
		customerPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));

		customerPanel.add(new JLabel("👤 Khách hàng:"));
		JTextField txtCustomer = new JTextField(donDatPhong.getKhachHang().getHoTen());
		customerPanel.add(txtCustomer);
		txtCustomer.setEditable(false);

		customerPanel.add(new JLabel("📞 Điện thoại:"));
		JTextField txtPhone = new JTextField(donDatPhong.getKhachHang().getSdt());
		customerPanel.add(txtPhone);
		txtPhone.setEditable(false);
		
		customerPanel.add(new JLabel("📅 Ngày đặt phòng:"));
		JTextField txtBookingDate = new JTextField(donDatPhong.getNgayDatPhong().format(formatter));
		customerPanel.add(txtBookingDate);
		txtBookingDate.setEditable(false);
		
		customerPanel.add(new JLabel("📅 Ngày nhận phòng:"));
		JTextField txtCheckIn = new JTextField(donDatPhong.getNgayNhanPhong().format(formatter));
		customerPanel.add(txtCheckIn);
		txtCheckIn.setEditable(false);

		customerPanel.add(new JLabel("📅 Ngày trả phòng:"));
		JTextField txtCheckOut = new JTextField(donDatPhong.getNgayTraPhong().format(formatter));
		customerPanel.add(txtCheckOut);
		txtCheckOut.setEditable(false);

		mainPanel.add(customerPanel);

		// II. Thông tin phòng
		JPanel roomPanel = new JPanel(new BorderLayout());
		roomPanel.setBorder(BorderFactory.createTitledBorder("Thông tin phòng"));
		LoaiPhong_Dao loaiPhongDao = new LoaiPhong_Dao();
		ArrayList<LoaiPhong> listLoaiPhong = loaiPhongDao.getAllLoaiPhong();

		// Đưa vào Map để gọi hàm
		hienThiDonDatPhong(donDatPhong, listLoaiPhong, roomPanel);
		mainPanel.add(roomPanel);
		// III. Tiền cọc
		JPanel depositPanel = new JPanel(new GridLayout(2, 2));
		depositPanel.setBorder(BorderFactory.createTitledBorder("Tiền cọc"));

		depositPanel.add(new JLabel(String.format("Số tiền khách hàng cọc: %, .0f VND", donDatPhong.getTienCoc())));

		mainPanel.add(depositPanel);

		// IV. Chi phí hủy phòng
		JPanel cancelFeePanel = new JPanel(new BorderLayout());
		cancelFeePanel = taoCancelFeePanel(donDatPhong);
		mainPanel.add(cancelFeePanel);

		// Nút xác nhận
		JPanel buttonHuy = new JPanel((LayoutManager) new FlowLayout(FlowLayout.RIGHT));
		JButton btnConfirm = new JButton("Xác nhận hủy");
		btnConfirm.setBackground(Color.RED);
		btnConfirm.setForeground(Color.WHITE);
		btnConfirm.setFont(new Font("Arial", Font.BOLD, 14));
		buttonHuy.add(btnConfirm);
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(buttonHuy);
		ganSuKienHuyDon(btnConfirm, donDatPhong, frame, reloadCallback);

		frame.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
		frame.setVisible(true);
	}
	
	private void ganSuKienHuyDon(JButton btnConfirm, DonDatPhong don, JFrame frame, Runnable reloadCallback) {
	    btnConfirm.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            int luaChon = JOptionPane.showConfirmDialog(
	                null,
	                "Bạn có chắc chắn muốn hủy đơn đặt phòng này không?",
	                "Xác nhận hủy",
	                JOptionPane.OK_CANCEL_OPTION,
	                JOptionPane.WARNING_MESSAGE
	            );

	            if (luaChon == JOptionPane.OK_OPTION) {
	                DonDatPhong_Dao ddpDao = new DonDatPhong_Dao();
	                // Cập nhật trạng thái đơn đặt phòng
	                boolean capNhatDon = ddpDao.setTrangThaiDonDatPhong(don.getMaDonDatPhong(), "Đã hủy");

	                if (capNhatDon) {
	                    JOptionPane.showMessageDialog(null, "Đã hủy đơn đặt phòng thành công!");
	                    frame.dispose(); // 👉 đóng cửa sổ hiện tại
	                    if (reloadCallback != null) reloadCallback.run();
	                } else {
	                    JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi hủy đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        }

	    });
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}
