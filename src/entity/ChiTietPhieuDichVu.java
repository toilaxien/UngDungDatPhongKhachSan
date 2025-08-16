package entity;

public class ChiTietPhieuDichVu {

	private PhieuDichVu phieuDichVu;
	private DichVu dichVu;
	private int soLuong;

	public ChiTietPhieuDichVu() {
		// TODO - implement ChiTietPhieuDichVu.ChiTietPhieuDichVu

	}

	public ChiTietPhieuDichVu(PhieuDichVu phieuDichVu, DichVu dichVu, int soLuong) {
		super();
		this.phieuDichVu = phieuDichVu;
		this.dichVu = dichVu;
		this.soLuong = soLuong;
	}

	public PhieuDichVu getPhieuDichVu() {
		return phieuDichVu;
	}

	public void setPhieuDichVu(PhieuDichVu phieuDichVu) {
		this.phieuDichVu = phieuDichVu;
	}

	public DichVu getDichVu() {
		return dichVu;
	}

	public void setDichVu(DichVu dichVu) {
		this.dichVu = dichVu;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

}