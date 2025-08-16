package entity;

public class LoaiDichVu {

	private String maLoai;
	private String tenLoai;

	public LoaiDichVu() {
		// TODO - implement LoaiDichVu.LoaiDichVu

	}

	public LoaiDichVu(String maLoai, String tenLoai) {
		super();
		this.maLoai = maLoai;
		this.tenLoai = tenLoai;
	}

	public LoaiDichVu(String maLoai) {
		super();
		this.maLoai = maLoai;
	}

	public String getMaLoai() {
		return maLoai;
	}

	public void setMaLoai(String maLoai) {
		this.maLoai = maLoai;
	}

	public String getTenLoai() {
		return tenLoai;
	}

	public void setTenLoai(String tenLoai) {
		this.tenLoai = tenLoai;
	}

}