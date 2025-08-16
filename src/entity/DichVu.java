package entity;

public class DichVu {

	private String maDV;
	private String tenDV;
	private String moTa;
	private double giaDV;
	private LoaiDichVu loaiDichVu;

	public DichVu() {
		// TODO - implement DichVu.DichVu

	}

	public DichVu(String maDV, String tenDV, String moTa, double giaDV, LoaiDichVu loaiDichVu) {
		super();
		this.maDV = maDV;
		this.tenDV = tenDV;
		this.moTa = moTa;
		this.giaDV = giaDV;
		this.loaiDichVu = loaiDichVu;
	}

	public DichVu(String maDV) {
		super();
		this.maDV = maDV;
	}

	public String getMaDV() {
		return maDV;
	}

	public void setMaDV(String maDV) {
		this.maDV = maDV;
	}

	public String getTenDV() {
		return tenDV;
	}

	public void setTenDV(String tenDV) {
		this.tenDV = tenDV;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public double getGiaDV() {
		return giaDV;
	}

	public void setGiaDV(double giaDV) {
		this.giaDV = giaDV;
	}

	public LoaiDichVu getLoaiDichVu() {
		return loaiDichVu;
	}

	public void setLoaiDichVu(LoaiDichVu loaiDichVu) {
		this.loaiDichVu = loaiDichVu;
	}
	
}