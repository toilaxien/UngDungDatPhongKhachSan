package entity;

public class ChiTietApDung {
	  private String maDonDatPhong;
	    private String maKhuyenMai;
	    private Double tongThanhToanSauApDung;
		public String getMaDonDatPhong() {
			return maDonDatPhong;
		}
		public void setMaDonDatPhong(String maDonDatPhong) {
			this.maDonDatPhong = maDonDatPhong;
		}
		public String getMaKhuyenMai() {
			return maKhuyenMai;
		}
		public void setMaKhuyenMai(String maKhuyenMai) {
			this.maKhuyenMai = maKhuyenMai;
		}
		
		public Double getTongThanhToanSauApDung() {
			return tongThanhToanSauApDung;
		}
		public void setTongThanhToanSauApDung(Double tongThanhToanSauApDung) {
			this.tongThanhToanSauApDung = tongThanhToanSauApDung;
		}
		public ChiTietApDung(String maDonDatPhong, String maKhuyenMai, Double tongThanhToanSauApDung) {
			super();
			this.maDonDatPhong = maDonDatPhong;
			this.maKhuyenMai = maKhuyenMai;
			this.tongThanhToanSauApDung = tongThanhToanSauApDung;
		}

	    
	    
}
