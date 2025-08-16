package entity;

import java.time.LocalDate;

public class KhuyenMai {
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private String loaiKhuyenMai;
    private Double giaTriKhuyenMai;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private Double dieuKienApDung;
    private String trangThai;
	public String getMaKhuyenMai() {
		return maKhuyenMai;
	}
	public void setMaKhuyenMai(String maKhuyenMai) {
		this.maKhuyenMai = maKhuyenMai;
	}
	public String getTenKhuyenMai() {
		return tenKhuyenMai;
	}
	public void setTenKhuyenMai(String tenKhuyenMai) {
		this.tenKhuyenMai = tenKhuyenMai;
	}
	public String getLoaiKhuyenMai() {
		return loaiKhuyenMai;
	}
	public void setLoaiKhuyenMai(String loaiKhuyenMai) {
		this.loaiKhuyenMai = loaiKhuyenMai;
	}
	public Double getGiaTriKhuyenMai() {
		return giaTriKhuyenMai;
	}
	public void setGiaTriKhuyenMai(Double giaTriKhuyenMai) {
		this.giaTriKhuyenMai = giaTriKhuyenMai;
	}
	public LocalDate getNgayBatDau() {
		return ngayBatDau;
	}
	public void setNgayBatDau(LocalDate ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}
	public LocalDate getNgayKetThuc() {
		return ngayKetThuc;
	}
	public void setNgayKetThuc(LocalDate ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}
	public Double getDieuKienApDung() {
		return dieuKienApDung;
	}
	public void setDieuKienApDung(Double dieuKienApDung) {
		this.dieuKienApDung = dieuKienApDung;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, String loaiKhuyenMai, Double giaTriKhuyenMai,
			LocalDate ngayBatDau, LocalDate ngayKetThuc, Double dieuKienApDung, String trangThai) {
		super();
		this.maKhuyenMai = maKhuyenMai;
		this.tenKhuyenMai = tenKhuyenMai;
		this.loaiKhuyenMai = loaiKhuyenMai;
		this.giaTriKhuyenMai = giaTriKhuyenMai;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.dieuKienApDung = dieuKienApDung;
		this.trangThai = trangThai;
	}

    
}
