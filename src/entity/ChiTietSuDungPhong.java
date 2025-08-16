package entity;

import java.time.LocalDateTime;

public class ChiTietSuDungPhong {
    private String maDonDatPhong;
    private String soPhong;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private String ghiChu;

    public ChiTietSuDungPhong(String maDonDatPhong, String soPhong, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, String ghiChu) {
        this.maDonDatPhong = maDonDatPhong;
        this.soPhong = soPhong;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.ghiChu = ghiChu;
    }

    public String getMaDonDatPhong() {
        return maDonDatPhong;
    }

    public void setMaDonDatPhong(String maDonDatPhong) {
        this.maDonDatPhong = maDonDatPhong;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    public LocalDateTime getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDateTime ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDateTime getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDateTime ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
