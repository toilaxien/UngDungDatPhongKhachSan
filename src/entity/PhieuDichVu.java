package entity;

import java.time.LocalDateTime;

public class PhieuDichVu {

	private String maPhieuDichVu;
	private DonDatPhong donDatPhong;
	private LocalDateTime ngayLapPhieu;
	private String trangThai;

	public PhieuDichVu() {
		// TODO - implement PhieuDichVu.PhieuDichVu
	}

	public double tinhTienDichVu() {
		// TODO - implement PhieuDichVu.tinhTienDichVu
		throw new UnsupportedOperationException();
	}

	public PhieuDichVu(String maPhieuDichVu, DonDatPhong donDatPhong, LocalDateTime ngayLapPhieu, String trangThai) {
		super();
		this.maPhieuDichVu = maPhieuDichVu;
		this.donDatPhong = donDatPhong;
		this.ngayLapPhieu = ngayLapPhieu;
		this.trangThai = trangThai;
	}

	public PhieuDichVu(String maPhieuDichVu) {
		super();
		this.maPhieuDichVu = maPhieuDichVu;
	}

	public String getMaPhieuDichVu() {
		return maPhieuDichVu;
	}

	public void setMaPhieuDichVu(String maPhieuDichVu) {
		this.maPhieuDichVu = maPhieuDichVu;
	}

	public DonDatPhong getDonDatPhong() {
		return donDatPhong;
	}

	public void setDonDatPhong(DonDatPhong donDatPhong) {
		this.donDatPhong = donDatPhong;
	}

	public LocalDateTime getNgayLapPhieu() {
		return ngayLapPhieu;
	}

	public void setNgayLapPhieu(LocalDateTime ngayLapPhieu) {
		this.ngayLapPhieu = ngayLapPhieu;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

}