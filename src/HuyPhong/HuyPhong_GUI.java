package HuyPhong;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import dao.*;
import entity.*;

public class HuyPhong_GUI extends JDialog implements ActionListener {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public HuyPhong_GUI(Frame parent) {
        super(parent, "Đơn Hủy Phòng", true); // Modal dialog
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
    }

    public void taoDonHuyPhong(DonDatPhong donDatPhong, Runnable reloadCallback) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // I. Thông tin khách hàng
        JPanel customerPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        customerPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));

        customerPanel.add(new JLabel("👤 Khách hàng:"));
        JTextField txtCustomer = new JTextField(donDatPhong.getKhachHang().getHoTen());
        txtCustomer.setEditable(false);
        customerPanel.add(txtCustomer);

        customerPanel.add(new JLabel("📞 Điện thoại:"));
        JTextField txtPhone = new JTextField(donDatPhong.getKhachHang().getSdt());
        txtPhone.setEditable(false);
        customerPanel.add(txtPhone);

        customerPanel.add(new JLabel("📅 Ngày đặt phòng:"));
        JTextField txtBookingDate = new JTextField(donDatPhong.getNgayDatPhong().format(formatter));
        txtBookingDate.setEditable(false);
        customerPanel.add(txtBookingDate);

        customerPanel.add(new JLabel("📅 Ngày nhận phòng:"));
        JTextField txtCheckIn = new JTextField(donDatPhong.getNgayNhanPhong().format(formatter));
        txtCheckIn.setEditable(false);
        customerPanel.add(txtCheckIn);

        customerPanel.add(new JLabel("📅 Ngày trả phòng:"));
        JTextField txtCheckOut = new JTextField(donDatPhong.getNgayTraPhong().format(formatter));
        txtCheckOut.setEditable(false);
        customerPanel.add(txtCheckOut);

        mainPanel.add(customerPanel);

        // II. Thông tin phòng
        JPanel roomPanel = new JPanel(new BorderLayout());
        roomPanel.setBorder(BorderFactory.createTitledBorder("Thông tin phòng"));
        ArrayList<LoaiPhong> listLoaiPhong = new LoaiPhong_Dao().getAllLoaiPhong();
        hienThiDonDatPhong(donDatPhong, listLoaiPhong, roomPanel);
        mainPanel.add(roomPanel);

        // III. Tiền cọc
        JPanel depositPanel = new JPanel(new GridLayout(2, 2));
        depositPanel.setBorder(BorderFactory.createTitledBorder("Tiền cọc"));
        depositPanel.add(new JLabel(String.format("Số tiền khách hàng cọc: %, .0f VND", donDatPhong.getTienCoc())));
        mainPanel.add(depositPanel);

        // IV. Chi phí hủy phòng
        JPanel cancelFeePanel = taoCancelFeePanel(donDatPhong);
        mainPanel.add(cancelFeePanel);

        // V. Nút xác nhận hủy
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnConfirm = new JButton("Xác nhận hủy");
        btnConfirm.setBackground(Color.RED);
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFont(new Font("Arial", Font.BOLD, 14));
        btnConfirm.setBackground(new Color(0, 255, 128));
        btnConfirm.setOpaque(true);
        btnConfirm.setContentAreaFilled(true);
        btnConfirm.setBorderPainted(false);
        buttonPanel.add(btnConfirm);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buttonPanel);

        ganSuKienHuyDon(btnConfirm, donDatPhong, this, reloadCallback);

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
    }

    public static JPanel taoCancelFeePanel(DonDatPhong don) {
        JPanel cancelFeePanel = new JPanel(new BorderLayout());
        cancelFeePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Chi phí hủy phòng", TitledBorder.LEFT, TitledBorder.TOP));

        LocalDateTime now = LocalDateTime.now();
        String ngayHuy = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String thoiGianHuy = now.format(DateTimeFormatter.ofPattern("HH:mm"));

        Duration duration = Duration.between(now, don.getNgayNhanPhong());
        long soGio = duration.toHours();
        long soNgay = duration.toDays();
        String huyTruoc = soNgay + " ngày " + (soGio % 24) + " giờ";

        String[] headers = {"Ngày hủy phòng", "Thời gian hủy", "Hủy trước bao lâu", "Phí hủy", "Số tiền hoàn cọc"};
        Object[][] data = {{
            ngayHuy,
            thoiGianHuy,
            huyTruoc,
            String.format("%,.0f VND", don.phiHuyPhong(now, don.getNgayNhanPhong())),
            String.format("%,.0f VND", don.tinhTienHoanCoc())
        }};

        JTable table = new JTable(new DefaultTableModel(data, headers));
        table.setEnabled(false);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(400, 60));
        cancelFeePanel.add(scroll, BorderLayout.CENTER);
        return cancelFeePanel;
    }

    public void hienThiDonDatPhong(DonDatPhong don, ArrayList<LoaiPhong> dsLoaiPhong, JPanel roomPanel) {
        roomPanel.removeAll();
        roomPanel.setLayout(new BorderLayout());
        ChiTietDonDatPhong_Dao ctDao = new ChiTietDonDatPhong_Dao();
        Phong_Dao phongDao = new Phong_Dao();
        LoaiPhong_Dao loaiPhongDao = new LoaiPhong_Dao();

        ArrayList<ChiTietDonDatPhong> chiTietList = ctDao.getChiTietDonDatPhongTheoMaDon(don.getMaDonDatPhong());
        Object[][] roomData = new Object[chiTietList.size()][4];

        LocalDateTime nhanPhong = don.getNgayNhanPhong();
        LocalDateTime traPhong = don.getNgayTraPhong();

        long soNgay = ChronoUnit.DAYS.between(nhanPhong.toLocalDate(), traPhong.toLocalDate());
        if (traPhong.toLocalTime().isAfter(nhanPhong.toLocalTime())) soNgay++;
        if (soNgay == 0) soNgay = 1;

        long soGio = Duration.between(nhanPhong, traPhong).toHours();
        if (soGio == 0) soGio = 1;

        for (int i = 0; i < chiTietList.size(); i++) {
            ChiTietDonDatPhong ct = chiTietList.get(i);
            Phong phong = phongDao.getPhongTheoMa(ct.getPhong().getSoPhong());
            LoaiPhong loai = loaiPhongDao.getLoaiPhongTheoMa(phong.getLoaiPhong().getMaLoaiPhong());

            roomData[i][0] = phong.getSoPhong();
            roomData[i][1] = loai.getTenLoai();

            switch (don.getLoaiDon()) {
                case "Theo giờ" -> {
                    roomData[i][2] = soGio;
                    roomData[i][3] = String.format("%,.0f VND", loai.getGiaTheoGio());
                }
                case "Theo ngày" -> {
                    roomData[i][2] = soNgay;
                    roomData[i][3] = String.format("%,.0f VND", loai.getGiaTheoNgay());
                }
                case "Theo đêm" -> {
                    roomData[i][2] = soNgay;
                    roomData[i][3] = String.format("%,.0f VND", loai.getGiaTheoDem());
                }
            }
        }

        String[] headers = {"Mã Phòng", "Loại phòng", "Số " + switch (don.getLoaiDon()) {
            case "Theo giờ" -> "giờ";
            case "Theo đêm" -> "đêm";
            default -> "ngày";
        }, "Giá phòng"};

        JTable table = new JTable(new DefaultTableModel(roomData, headers));
        table.setEnabled(false);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(table);
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

    private void ganSuKienHuyDon(JButton btnConfirm, DonDatPhong don, JDialog dialog, Runnable reloadCallback) {
        btnConfirm.addActionListener(e -> {
            int luaChon = JOptionPane.showConfirmDialog(
                    dialog,
                    "Bạn có chắc chắn muốn hủy đơn đặt phòng này không?",
                    "Xác nhận hủy",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (luaChon == JOptionPane.OK_OPTION) {
                DonDatPhong_Dao dao = new DonDatPhong_Dao();
                boolean ok = dao.setTrangThaiDonDatPhong(don.getMaDonDatPhong(), "Đã hủy");

                if (ok) {
                    JOptionPane.showMessageDialog(dialog, "Đã hủy đơn đặt phòng thành công!");
                    dialog.dispose();
                    if (reloadCallback != null) reloadCallback.run();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Có lỗi xảy ra khi hủy đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
