package entity;

public class Phong {

	private String soPhong;
	private String trangThai;
	private LoaiPhong loaiPhong;
	private String moTa;

	public Phong() {
		// TODO - implement Phong.Phong
	}

	public Phong(String soPhong, String trangThai, LoaiPhong loaiPhong, String moTa) {
		super();
		this.soPhong = soPhong;
		this.trangThai = trangThai;
		this.loaiPhong = loaiPhong;
		this.moTa = moTa;
	}

	public Phong(String soPhong) {
		super();
		this.soPhong = soPhong;
	}

	public String getSoPhong() {
		return soPhong;
	}

	public void setSoPhong(String soPhong) {
		this.soPhong = soPhong;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public LoaiPhong getLoaiPhong() {
		return loaiPhong;
	}

	public void setLoaiPhong(LoaiPhong loaiPhong) {
		this.loaiPhong = loaiPhong;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	
}