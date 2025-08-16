package doiPhong;

import dao.ChiTietDonDatPhong_Dao;
import dao.ChiTietSuDungPhong_Dao;
import dao.DonDatPhong_Dao;
import entity.ChiTietSuDungPhong;
import entity.DonDatPhong;


import java.time.LocalDateTime;
import java.util.ArrayList;

public class NhanPhongUtil {

    private final ChiTietDonDatPhong_Dao chiTietDonDao = new ChiTietDonDatPhong_Dao();
    private final DonDatPhong_Dao donDatPhongDao = new DonDatPhong_Dao();
    private final ChiTietSuDungPhong_Dao chiTietSuDungDao = new ChiTietSuDungPhong_Dao();

    public void insertChiTietKhiNhanPhong(String maDonDatPhong) {
        DonDatPhong don = donDatPhongDao.getDonDatPhongTheoMa(maDonDatPhong);
        if (don == null) return;

        LocalDateTime batDau = LocalDateTime.now(); // Đã là LocalDateTime
        LocalDateTime ketThuc = don.getNgayTraPhong(); // Đã là LocalDateTime

        ArrayList<String> dsPhong = chiTietDonDao.getDanhSachSoPhongTheoDon(maDonDatPhong);
        for (String soPhong : dsPhong) {
            ChiTietSuDungPhong ct = new ChiTietSuDungPhong(
                maDonDatPhong,
                soPhong,
                batDau,
                ketThuc,
                "" // Không cần ghi chú ban đầu
            );
            chiTietSuDungDao.themChiTiet(ct);
        }
    }
    public void insertChiTietKhiDatTrucTiep(String maDonDatPhong) {
        DonDatPhong don = donDatPhongDao.getDonDatPhongTheoMa(maDonDatPhong);
        if (don == null) return;

        LocalDateTime ngayTra = don.getNgayTraPhong();
        ArrayList<String> dsPhong = chiTietDonDao.getDanhSachSoPhongTheoDon(maDonDatPhong);

        for (String soPhong : dsPhong) {
            LocalDateTime ngayBatDau;

            if (don.getLoaiDon().equalsIgnoreCase("Theo ngày")) {
                // Nếu là đơn ngày → ghi nhận thời điểm hiện tại + 1h
                ngayBatDau = LocalDateTime.now().plusHours(1);
            } else {
                // Đơn giờ / đêm giữ nguyên thời điểm nhận phòng
                ngayBatDau = don.getNgayNhanPhong();
            }

            ChiTietSuDungPhong ct = new ChiTietSuDungPhong(
                maDonDatPhong,
                soPhong,
                ngayBatDau,
                ngayTra,
                "" // Chưa ghi chú gì
            );
            chiTietSuDungDao.themChiTiet(ct);
        }
    }

    public static void main(String[] args) {
		NhanPhongUtil np = new NhanPhongUtil();
		np.insertChiTietKhiDatTrucTiep("26052025LT001001");
	}
}
