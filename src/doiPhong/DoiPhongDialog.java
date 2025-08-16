package doiPhong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;

//import dao.DonDatPhong_Dao;
//import dao.LoaiPhong_Dao;
import dao.Phong_Dao;
import entity.ChiTietSuDungPhong;
import entity.DonDatPhong;

import org.jdatepicker.impl.*;

import dao.ChiTietSuDungPhong_Dao;
import dao.DonDatPhong_Dao;
import dao.LoaiPhong_Dao;

import java.util.Properties;

public class DoiPhongDialog extends JDialog {
    private JTextField txtPhongCu;
    private JComboBox<String> cmbLoaiPhong;
    private JComboBox<String> cmbPhongMoi;
    private JDatePickerImpl datePicker;
    private JSpinner timeSpinner;
    private JButton btnXacNhan;

    private String maDonDatPhong;
    private String soPhongCu;
    private LocalDateTime ngayKetThucCu;

    private Phong_Dao phongDAO = new Phong_Dao();
    private LoaiPhong_Dao loaiPhongDAO = new LoaiPhong_Dao();

    public DoiPhongDialog(Window parent, String maDonDatPhong, String soPhongCu, LocalDateTime ngayKetThucCu) {
        super(parent, "Đổi phòng", ModalityType.APPLICATION_MODAL);
        this.maDonDatPhong = maDonDatPhong;
        this.soPhongCu = soPhongCu;
        this.ngayKetThucCu = ngayKetThucCu;
        khoiTaoGiaoDien();
        taiDanhSachLoaiPhong();
        batSuKien();
    }

    private void khoiTaoGiaoDien() {
        setLayout(new BorderLayout(10, 10));
        setSize(500, 320);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        txtPhongCu = new JTextField(soPhongCu);
        txtPhongCu.setEditable(false);

        cmbLoaiPhong = new JComboBox<>();
        cmbPhongMoi = new JComboBox<>();

        // JDatePicker
        UtilDateModel model = new UtilDateModel();
        model.setValue(new Date());
        Properties p = new Properties();
        p.put("text.today", "Hôm nay");
        p.put("text.month", "Tháng");
        p.put("text.year", "Năm");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        // JSpinner for time
        timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date());

        btnXacNhan = new JButton("Xác nhận đổi phòng");

        panel.add(new JLabel("Phòng hiện tại:"));
        panel.add(txtPhongCu);
        panel.add(new JLabel("Ngày đổi phòng:"));
        panel.add(datePicker);
        panel.add(new JLabel("Giờ đổi phòng:"));
        panel.add(timeSpinner);
        panel.add(new JLabel("Loại phòng mới:"));
        panel.add(cmbLoaiPhong);
        panel.add(new JLabel("Phòng mới:"));
        panel.add(cmbPhongMoi);

        add(panel, BorderLayout.CENTER);
        add(btnXacNhan, BorderLayout.SOUTH);
    }
    
    private int getMucDoMacDinh(String tenLoai) {
        switch (tenLoai.toLowerCase()) {
            case "single room": return 1;
            case "twin room": return 2;
            case "double room": return 3;
            case "triple room": return 4;
            default: return Integer.MAX_VALUE;
        }
    }
    
    private void taiDanhSachPhongMoi() {
        String loaiPhongChon = (String) cmbLoaiPhong.getSelectedItem();
        LocalDateTime thoiDiemDoi = ngayKetThucCu; // hoặc giờ hiện tại

        ArrayList<String> dsPhong = phongDAO.getPhongTrongTheoLoaiVaThoiGian(loaiPhongChon, thoiDiemDoi);
        cmbPhongMoi.removeAllItems();

        for (String soPhong : dsPhong) {
            if (!soPhong.equals(soPhongCu)) {
                cmbPhongMoi.addItem(soPhong);
            }
        }
    }


    private void taiDanhSachLoaiPhong() {
        String tenLoaiPhongCu = phongDAO.getTenLoaiTheoSoPhong(soPhongCu);
        int mucHienTai = getMucDoMacDinh(tenLoaiPhongCu);
        
        ArrayList<String> dsLoai = loaiPhongDAO.getDanhSachTenLoai();
        cmbLoaiPhong.removeAllItems();
        
        for (String tenLoai : dsLoai) {
            if (getMucDoMacDinh(tenLoai) >= mucHienTai) {
                cmbLoaiPhong.addItem(tenLoai);
            }
        }

        cmbLoaiPhong.setSelectedItem(tenLoaiPhongCu); // gợi ý chọn đúng ban đầu
        taiDanhSachPhongMoi(); // load danh sách phòng phù hợp
        cmbLoaiPhong.addActionListener(e -> taiDanhSachPhongMoi());
    }

    private void batSuKien() {
        cmbLoaiPhong.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String tenLoai = cmbLoaiPhong.getSelectedItem().toString();
                LocalDate datePart = (LocalDate) ((Date) datePicker.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalTime timePart = ((Date) timeSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                LocalDateTime thoiDiemDoi = LocalDateTime.of(datePart, timePart);
                ArrayList<String> dsPhong = phongDAO.getPhongTrongTheoLoaiVaThoiGian(tenLoai, thoiDiemDoi);
                cmbPhongMoi.removeAllItems();
                for (String soPhong : dsPhong) {
                    if (!soPhong.equals(soPhongCu)) {
                        cmbPhongMoi.addItem(soPhong);
                    }
                }
            }
        });

        btnXacNhan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String phongMoi = cmbPhongMoi.getSelectedItem().toString();

                LocalDate datePart = (LocalDate) ((Date) datePicker.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalTime timePart = ((Date) timeSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                LocalDateTime thoiDiemDoi = LocalDateTime.of(datePart, timePart);

                ChiTietSuDungPhong_Dao dao = new ChiTietSuDungPhong_Dao();
                dao.capNhatNgayKetThuc(maDonDatPhong, soPhongCu, thoiDiemDoi);

                DonDatPhong_Dao donDao = new DonDatPhong_Dao();
                LocalDateTime ngayTra = donDao.getDonDatPhongTheoMa(maDonDatPhong).getNgayTraPhong();

                ChiTietSuDungPhong moi = new ChiTietSuDungPhong(
                    maDonDatPhong,
                    phongMoi,
                    thoiDiemDoi,
                    ngayTra,
                    "Đổi từ " + soPhongCu
                );
                dao.themChiTiet(moi);
                phongDAO.setTrangThaiPhong(phongMoi, "Đang ở");

                JOptionPane.showMessageDialog(DoiPhongDialog.this, "Đổi phòng thành công!");
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String maDon = "DD001";
            String soPhongCu = "P101";
            LocalDateTime ngayKetThucCu = LocalDateTime.of(2025, 5, 25, 12, 0);
            new DoiPhongDialog(null, maDon, soPhongCu, ngayKetThucCu).setVisible(true);
        });
    }
}
