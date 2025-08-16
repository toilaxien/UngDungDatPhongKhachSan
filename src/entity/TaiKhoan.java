package entity;

public class TaiKhoan {

	private String tenDangNhap;
	private String matKhau;
	private String trangThai;
	private NhanVien nhanVien;
	
	public TaiKhoan() {
		// TODO - implement TaiKhoan.TaiKhoan
	}

	public TaiKhoan(String tenDangNhap, String matKhau, String trangThai, NhanVien nhanVien) {
		super();
		this.tenDangNhap = tenDangNhap;
		this.matKhau = matKhau;
		this.trangThai = trangThai;
		this.nhanVien = nhanVien;
	}

	public TaiKhoan(String tenDangNhap) {
		super();
		this.tenDangNhap = tenDangNhap;
	}

	public String getTenDangNhap() {
		return tenDangNhap;
	}

	public void setTenDangNhap(String tenDangNhap) {
		this.tenDangNhap = tenDangNhap;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}
	
}