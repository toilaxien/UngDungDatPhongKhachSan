package entity;

public class ChiPhiPhatSinh {

	private String maChiPhi;
	private double chiPhiTBHong;
	private int soGioThem;
	private String moTa;
	private DonDatPhong donDatPhong;
	
	public ChiPhiPhatSinh() {
		// TODO - implement ChiPhiPhatSinh.ChiPhiPhatSinh

	}
	
	public ChiPhiPhatSinh(String maChiPhi, double chiPhiTBHong, int soGioThem, String moTa, DonDatPhong donDatPhong) {
		super();
		this.maChiPhi = maChiPhi;
		this.chiPhiTBHong = chiPhiTBHong;
		this.soGioThem = soGioThem;
		this.moTa = moTa;
		this.donDatPhong = donDatPhong;
	}
	
	public ChiPhiPhatSinh(String maChiPhi) {
		super();
		this.maChiPhi = maChiPhi;
	}
	
	public String getMaChiPhi() {
		return maChiPhi;
	}

	public void setMaChiPhi(String maChiPhi) {
		this.maChiPhi = maChiPhi;
	}

	public double getChiPhiTBHong() {
		return chiPhiTBHong;
	}

	public void setChiPhiTBHong(double chiPhiTBHong) {
		this.chiPhiTBHong = chiPhiTBHong;
	}

	public int getSoGioThem() {
		return soGioThem;
	}

	public void setSoGioThem(int soGioThem) {
		this.soGioThem = soGioThem;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public DonDatPhong getDonDatPhong() {
		return donDatPhong;
	}

	public void setDonDatPhong(DonDatPhong donDatPhong) {
		this.donDatPhong = donDatPhong;
	}
	
	public double tinhPhiPhuThuQuaGio() {
		// TODO - implement ChiPhiPhatSinh.tinhPhiPhuThuQuaGio
		throw new UnsupportedOperationException();
	}

	public double tinhTongCPPS() {
		// TODO - implement ChiPhiPhatSinh.tinhTongCPPS
		throw new UnsupportedOperationException();
	}

}