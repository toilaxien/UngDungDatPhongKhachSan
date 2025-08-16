package entity;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Map;

import dao.ChiTietDonDatPhong_Dao;
import dao.LoaiPhong_Dao;
import dao.Phong_Dao;

public class DonDatPhong {
	private String maDonDatPhong;
	private LocalDateTime ngayDatPhong;
	private LocalDateTime ngayNhanPhong;
	private LocalDateTime ngayTraPhong;
	private int soKhach;
	private double tienCoc;
	private LocalDateTime thoiGianCoc;
	private NhanVien nhanVien;
	private String loaiDon;
	private String trangThai;
	private KhachHang khachHang;
	private ArrayList<ChiTietDonDatPhong> chiTietPhong;

	public DonDatPhong() {
		// TODO - implement DonDatPhong.DonDatPhong

	}

	public DonDatPhong(String maDonDatPhong) {
		super();
		this.maDonDatPhong = maDonDatPhong;
	}

	public DonDatPhong(String maDonDatPhong,  KhachHang khachHang, LocalDateTime ngayDatPhong, LocalDateTime ngayNhanPhong,
			LocalDateTime ngayTraPhong, int soKhach, double tienCoc, LocalDateTime thoiGianCoc, NhanVien nhanVien,
			String loaiDon, String trangThai) {
		super();
		this.maDonDatPhong = maDonDatPhong;
		this.khachHang = khachHang;
		this.ngayDatPhong = ngayDatPhong;
		this.ngayNhanPhong = ngayNhanPhong;
		this.ngayTraPhong = ngayTraPhong;
		this.soKhach = soKhach;
		this.tienCoc = tienCoc;
		this.thoiGianCoc = thoiGianCoc;
		this.nhanVien = nhanVien;
		this.loaiDon = loaiDon;
		this.trangThai = trangThai;
	}

// ======== Getters & Setters ========
	public String getMaDonDatPhong() {
		return maDonDatPhong;
	}

	public void setMaDonDatPhong(String maDonDatPhong) {
		this.maDonDatPhong = maDonDatPhong;
	}

	public LocalDateTime getNgayDatPhong() {
		return ngayDatPhong;
	}

	public void setNgayDatPhong(LocalDateTime ngayDatPhong) {
		this.ngayDatPhong = ngayDatPhong;
	}

	public LocalDateTime getNgayNhanPhong() {
		return ngayNhanPhong;
	}

	public void setNgayNhanPhong(LocalDateTime ngayNhanPhong) {
		this.ngayNhanPhong = ngayNhanPhong;
	}

	public LocalDateTime getNgayTraPhong() {
		return ngayTraPhong;
	}

	public void setNgayTraPhong(LocalDateTime ngayTraPhong) {
		this.ngayTraPhong = ngayTraPhong;
	}

	public int getSoKhach() {
		return soKhach;
	}

	public void setSoKhach(int soKhach) {
		this.soKhach = soKhach;
	}

	public double getTienCoc() {
		return tienCoc;
	}

	public void setTienCoc(double tienCoc) {
		this.tienCoc = tienCoc;
	}

	public LocalDateTime getThoiGianCoc() {
		return thoiGianCoc;
	}

	public void setThoiGianCoc(LocalDateTime thoiGianCoc) {
		this.thoiGianCoc = thoiGianCoc;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public String getLoaiDon() {
		return loaiDon;
	}

	public void setLoaiDon(String loaiDon) {
		this.loaiDon = loaiDon;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public KhachHang getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}

	public ArrayList<ChiTietDonDatPhong> getChiTietPhong() {
		return chiTietPhong;
	}

	public void setChiTietPhong(ArrayList<ChiTietDonDatPhong> chiTietPhong) {
		this.chiTietPhong = chiTietPhong;
	}

	public double tinhTienPhong() {
	    ChiTietDonDatPhong_Dao chiTietDonDatPhong_Dao = new ChiTietDonDatPhong_Dao();
	    chiTietPhong = chiTietDonDatPhong_Dao.getChiTietDonDatPhongTheoMaDon(maDonDatPhong);

	    Phong_Dao phong_Dao = new Phong_Dao();
	    LoaiPhong_Dao loaiPhongDao = new LoaiPhong_Dao();

	    double tongTien = 0;

	    ZoneId zone = ZoneId.systemDefault();
	    Instant start = ngayNhanPhong.atZone(zone).toInstant();
	    Instant end = ngayTraPhong.atZone(zone).toInstant();
	    long tongPhut = Duration.between(start, end).toMinutes();

	    for (ChiTietDonDatPhong ct : chiTietPhong) {
	        Phong phong = phong_Dao.getPhongTheoMa(ct.getPhong().getSoPhong());
	        LoaiPhong loaiPhong = loaiPhongDao.getLoaiPhongTheoMa(phong.getLoaiPhong().getMaLoaiPhong());

	        switch (loaiDon) {
	            case "Theo giờ":
	                long soGio = (long) Math.ceil(tongPhut / 60.0); // Làm tròn lên
	                if (soGio == 0) soGio = 1;
	                tongTien += loaiPhong.getGiaTheoGio() * soGio;
	                break;

	            case "Theo ngày":
	                long soNgay = tongPhut / (60 * 24);
	                long phutLe = tongPhut % (60 * 24);
	                if (phutLe > 12 * 60) soNgay++; // Nếu dư > 12 tiếng thì thêm 1 ngày
	                if (soNgay == 0) soNgay = 1;
	                tongTien += loaiPhong.getGiaTheoNgay() * soNgay;
	                break;

	            case "Theo đêm":
	                tongTien += loaiPhong.getGiaTheoDem(); // Cố định 1 đêm
	                break;

	            default:
	                break;
	        }
	    }

	    return tongTien;
	}


	/**
	 * 
	 * @param Phong
	 */

	public double tinhTongTien() {
		// TODO - implement DonDatPhong.tinhTongTien
		throw new UnsupportedOperationException();
	}

	public void tinhTienCoc(LocalDateTime thoiGianDatPhong) {
		Duration duration = Duration.between(thoiGianDatPhong, ngayNhanPhong);
		long hoursBeforeCheckin = duration.toHours();

		if (hoursBeforeCheckin >= 1) {
			// Đặt trước ít nhất 1 tiếng thì yêu cầu cọc
			this.tienCoc = tinhTienPhong() * 0.3;
			System.out.println("Khách đặt trước, cần đặt cọc: " + tienCoc);
		} else {
			this.tienCoc = 0;
			System.out.println("Khách đặt tại quầy, không cần đặt cọc.");
		}
	}

	public double phiHuyPhong(LocalDateTime thoiGianHuy, LocalDateTime ngayNhanPhong) {
		Duration durationDat = Duration.between(thoiGianHuy, ngayNhanPhong);
		long hoursBeforeCheckinAtDat = durationDat.toHours();

		// Nếu đặt trực tiếp (gần giờ nhận phòng), miễn phí hủy
		if (hoursBeforeCheckinAtDat < 1) {
			return 0;
		}

		long gioTruoc = Duration.between(thoiGianHuy, ngayNhanPhong).toHours();

		if (gioTruoc >= 48) {
			return 0;
		} else if (gioTruoc >= 24) {
			return tienCoc * 0.5;
		} else {
			return tienCoc;
		}
	}

	public double tinhTienHoanCoc() {
		return tienCoc - phiHuyPhong(LocalDateTime.now(), ngayNhanPhong);
	}

}