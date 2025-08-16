package entity;

public class ChiTietDonDatPhong {

	private DonDatPhong donDatPhong;
	private Phong phong;
	private int soLuong;

	public ChiTietDonDatPhong() {
		// TODO - implement ChiTietDonDatPhong.ChiTietDonDatPhong

	}

	public ChiTietDonDatPhong(DonDatPhong donDatPhong, Phong phong, int soLuong) {
		super();
		this.donDatPhong = donDatPhong;
		this.phong = phong;
		this.soLuong = soLuong;
	}

	public DonDatPhong getDonDatPhong() {
		return donDatPhong;
	}

	public void setDonDatPhong(DonDatPhong donDatPhong) {
		this.donDatPhong = donDatPhong;
	}

	public Phong getPhong() {
		return phong;
	}

	public void setPhong(Phong phong) {
		this.phong = phong;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	
	
	
}