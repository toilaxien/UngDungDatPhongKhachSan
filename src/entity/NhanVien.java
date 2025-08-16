package entity;

import java.time.LocalDate;

public class NhanVien {

	private String maNV;
	private String hoTen;
	private LocalDate ngaySinh;
	private String sdt;
	private String diaChi;
	private String soCCCD;
	private String chucVu;
	private String caLamViec;

	public NhanVien() {
		// TODO - implement NhanVien.NhanVien
	}

	public NhanVien(String maNV, String hoTen, LocalDate ngaySinh, String sdt, String diaChi, String soCCCD,
			String chucVu, String caLamViec) {
		super();
		this.maNV = maNV;
		this.hoTen = hoTen;
		this.ngaySinh = ngaySinh;
		this.sdt = sdt;
		this.diaChi = diaChi;
		this.soCCCD = soCCCD;
		this.chucVu = chucVu;
		this.caLamViec = caLamViec;
	}

	public NhanVien(String maNV) {
		super();
		this.maNV = maNV;
	}

	public String getMaNV() {
		return maNV;
	}

	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}

	public LocalDate getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(LocalDate ngaySinh) {
		this.ngaySinh = ngaySinh;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getSoCCCD() {
		return soCCCD;
	}

	public void setSoCCCD(String soCCCD) {
		this.soCCCD = soCCCD;
	}

	public String getChucVu() {
		return chucVu;
	}

	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}

	public String getCaLamViec() {
		return caLamViec;
	}

	public void setCaLamViec(String caLamViec) {
		this.caLamViec = caLamViec;
	}
	@Override
	public String toString() {
	    return "NhanVien{" +
	           "maNV='" + maNV + '\'' +
	           ", hoTen='" + hoTen + '\'' +
	           ", ngaySinh=" + ngaySinh +
	           ", sdt='" + sdt + '\'' +
	           ", diaChi='" + diaChi + '\'' +
	           ", soCCCD='" + soCCCD + '\'' +
	           ", chucVu='" + chucVu + '\'' +
	           ", caLamViec='" + caLamViec + '\'' +
	           '}';
	}

}