package entity;

public class KhachHang {

	private String maKH;
	private String hoTen;
	private String sdt;
	private String soCCCD;
	private String email;

	public KhachHang() {
		// TODO - implement KhachHang.KhachHang

	}


	
	public KhachHang(String maKH, String hoTen, String sdt, String soCCCD, String email) {
		super();
		this.maKH = maKH;
		this.hoTen = hoTen;
		this.sdt = sdt;
		this.soCCCD = soCCCD;
		this.email = email;
	}



	public KhachHang(String maKH, String hoTen) {
        this.maKH = maKH;
        this.hoTen = hoTen;
    }
	
	public KhachHang(String maKH) {
		super();
		this.maKH = maKH;
	}

	public String getMaKH() {
		return maKH;
	}

	public void setMaKH(String maKH) {
		this.maKH = maKH;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public String getSoCCCD() {
		return soCCCD;
	}

	public void setSoCCCD(String soCCCD) {
		this.soCCCD = soCCCD;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}
	
}