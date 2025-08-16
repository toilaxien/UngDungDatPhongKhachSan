package traPhong;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import dao.ChiTietSuDungPhong_Dao;
import dao.DonDatPhong_Dao;
import dao.Phong_Dao;
import entity.ChiTietSuDungPhong;
import entity.DonDatPhong;
import entity.LoaiPhong;
import entity.Phong;

public class TinhTienUtil {

    public static double tinhTongTienPhongTheoDonDat(String maDonDatPhong) {
        Map<String, Double> tienTungPhong = tinhTienTungPhong(maDonDatPhong);
        return tienTungPhong.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public static Map<String, Double> tinhTienTungPhong(String maDonDatPhong) {
        ChiTietSuDungPhong_Dao chiTietDao = new ChiTietSuDungPhong_Dao();
        Phong_Dao phongDao = new Phong_Dao();
        DonDatPhong_Dao donDao = new DonDatPhong_Dao();

        DonDatPhong don = donDao.getDonDatPhongTheoMa(maDonDatPhong);
        if (don == null) return Collections.emptyMap();

        String loaiDon = don.getLoaiDon();
        ArrayList<ChiTietSuDungPhong> danhSach = chiTietDao.getTheoMaDon(maDonDatPhong);
        danhSach.sort(Comparator.comparing(ChiTietSuDungPhong::getNgayBatDau));

        Map<String, Double> ketQua = new LinkedHashMap<>();
        LocalTime gioCheckIn = LocalTime.of(14, 0);
        LocalTime gioCheckOut = LocalTime.of(12, 0);

        if ("Theo ngày".equalsIgnoreCase(loaiDon)) {
            Map<LocalDate, ChiTietSuDungPhong> banDoNgay = new LinkedHashMap<>();

            for (ChiTietSuDungPhong ct : danhSach) {
                Phong phong = phongDao.getPhongTheoMa(ct.getSoPhong());
                LoaiPhong loai = phong.getLoaiPhong();

                LocalDateTime batDau = ct.getNgayBatDau();
                LocalDateTime ketThuc = ct.getNgayKetThuc();

                LocalDate ngay = batDau.toLocalDate();
                while (!ngay.isAfter(ketThuc.toLocalDate())) {
                    LocalDateTime gioVao = LocalDateTime.of(ngay, gioCheckIn);
                    LocalDateTime gioRa = LocalDateTime.of(ngay.plusDays(1), gioCheckOut);

                    if (!ketThuc.isBefore(gioVao) && !batDau.isAfter(gioRa)) {
                        ChiTietSuDungPhong hienTai = banDoNgay.get(ngay);
                        if (hienTai == null || 
                            phong.getLoaiPhong().getGiaTheoNgay() > 
                            phongDao.getPhongTheoMa(hienTai.getSoPhong()).getLoaiPhong().getGiaTheoNgay()) {
                            banDoNgay.put(ngay, ct);
                        }
                    }
                    ngay = ngay.plusDays(1);
                }
            }

            for (ChiTietSuDungPhong ct : danhSach) {
                ketQua.put(ct.getSoPhong(), 0.0);
            }
            for (ChiTietSuDungPhong ct : banDoNgay.values()) {
                Phong phong = phongDao.getPhongTheoMa(ct.getSoPhong());
                LoaiPhong loai = phong.getLoaiPhong();
                double gia = loai.getGiaTheoNgay();
                ketQua.put(ct.getSoPhong(), ketQua.getOrDefault(ct.getSoPhong(), 0.0) + gia);
            }
        } else {
            for (ChiTietSuDungPhong ct : danhSach) {
                Phong phong = phongDao.getPhongTheoMa(ct.getSoPhong());
                LoaiPhong loai = phong.getLoaiPhong();
                double donGia = 0.0;
                double tien = 0.0;

                LocalDateTime batDau = ct.getNgayBatDau();
                LocalDateTime ketThuc = ct.getNgayKetThuc();

                switch (loaiDon) {
                    case "Theo giờ":
                        long soGio = java.time.temporal.ChronoUnit.HOURS.between(batDau, ketThuc);
                        if (soGio == 0) soGio = 1;
                        donGia = loai.getGiaTheoGio();
                        tien = donGia * soGio;
                        break;
                    case "Theo đêm":
                        donGia = loai.getGiaTheoDem();
                        tien = donGia;
                        break;
                }

                ketQua.put(ct.getSoPhong(), ketQua.getOrDefault(ct.getSoPhong(), 0.0) + tien);
            }
        }

        return ketQua;
    }

    public static void main(String[] args) {
        System.out.println(tinhTongTienPhongTheoDonDat("26052025LT001006"));
    }

    public static double getThanhTienPhong(String maDonDat, String soPhong) {
        Map<String, Double> map = tinhTienTungPhong(maDonDat);
        return map.getOrDefault(soPhong, 0.0);
    }
}
