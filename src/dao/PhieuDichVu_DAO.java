package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.DonDatPhong;
import entity.PhieuDichVu;

public class PhieuDichVu_DAO {
    private ArrayList<PhieuDichVu> dsPhieuDichVu;

    public PhieuDichVu_DAO() {
        dsPhieuDichVu = new ArrayList<>();
    }

    // Thêm phiếu dịch vụ
    public boolean themPhieuDichVu(PhieuDichVu pdv) {
        if (dsPhieuDichVu.contains(pdv)) {
            return false; // đã tồn tại
        } else {
        	Connection con = ConnectDB.getConnection();
			PreparedStatement stmt = null;
            int n = 0;
            try {
                stmt = con.prepareStatement("INSERT INTO PhieuDichVu VALUES (?, ?, ?, ?)");
                stmt.setString(1, pdv.getMaPhieuDichVu());
                stmt.setString(2, pdv.getDonDatPhong().getMaDonDatPhong());
                stmt.setTimestamp(3, Timestamp.valueOf(pdv.getNgayLapPhieu()));
                stmt.setString(4, pdv.getTrangThai());

                n = stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return n > 0;
        }
    }
    
    public boolean xoaPhieuDichVu(String maPhieuDichVu) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectDB.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Xóa các chi tiết phiếu dịch vụ liên quan
            String deleteChiTietSQL = "DELETE FROM ChiTietPhieuDichVu WHERE maPhieuDichVu = ?";
            stmt = conn.prepareStatement(deleteChiTietSQL);
            stmt.setString(1, maPhieuDichVu);
            stmt.executeUpdate();

            // Xóa phiếu dịch vụ
            String deletePhieuSQL = "DELETE FROM PhieuDichVu WHERE maPhieuDichVu = ?";
            stmt = conn.prepareStatement(deletePhieuSQL);
            stmt.setString(1, maPhieuDichVu);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted == 0) {
                conn.rollback(); // Rollback nếu không có hàng nào bị xóa
                return false; // Phiếu dịch vụ không tồn tại
            }

            conn.commit(); // Commit transaction nếu thành công
            return true; // Xóa thành công

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Rollback nếu có lỗi
            }
            e.printStackTrace();
            return false; // Xóa thất bại
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true); // Đặt lại auto-commit
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public ArrayList<PhieuDichVu> getPhieuDichVuByMaDonDatPhong(String maDonDatPhong) {
        ArrayList<PhieuDichVu> list = new ArrayList<>();
        String sql = "SELECT * FROM PhieuDichVu WHERE maDonDatPhong = ? AND trangThai = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maDonDatPhong);
            stmt.setString(2, "Chưa thanh toán");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String maPhieuDichVu = rs.getString("maPhieuDichVu");

                // Lấy ngày lập phiếu và chuyển sang LocalDateTime
                Timestamp timestamp = rs.getTimestamp("ngayLapPhieu");
                LocalDateTime ngayLapPhieu = null;
                if (timestamp != null) {
                    ngayLapPhieu = timestamp.toLocalDateTime();
                }
                String trangThai = rs.getString("trangThai");
                DonDatPhong ddp = new DonDatPhong();
                ddp.setMaDonDatPhong(maDonDatPhong);

                PhieuDichVu pdv = new PhieuDichVu(maPhieuDichVu, ddp, ngayLapPhieu, trangThai);
                list.add(pdv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    
	public ArrayList<Object[]> getLoaiDichVuVaThanhTienTheoMaDonDatPhong(String maDonDatPhong) {
	    ArrayList<Object[]> danhSach = new ArrayList<>();

	    try {
	        Connection conn = ConnectDB.getConnection();
	        String sql = """
	            SELECT pdv.maPhieuDichVu, ldv.tenLoai, MAX(pdv.ngayLapPhieu) AS ngayLapPhieu, SUM(dv.giaDV * ctpdv.soLuong) AS thanhTien
	            FROM PhieuDichVu pdv
	            JOIN ChiTietPhieuDichVu ctpdv ON pdv.maPhieuDichVu = ctpdv.maPhieuDichVu
	            JOIN DichVu dv ON ctpdv.maDichVu = dv.maDV
	            JOIN LoaiDichVu ldv ON dv.maLoai = ldv.maLoai
	            WHERE pdv.maDonDatPhong = ? AND pdv.trangThai =  ?
	            GROUP BY pdv.maPhieuDichVu, ldv.tenLoai
	        """;

	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, maDonDatPhong);
	        ps.setString(2, "Chưa thanh toán");
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            String maPhieu = rs.getString("maPhieuDichVu");
	            String tenLoai = rs.getString("tenLoai");
	            double thanhTien = rs.getDouble("thanhTien");
	            Timestamp ts = rs.getTimestamp("ngayLapPhieu");
	            String ngayLapPhieuStr = ts.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

	            danhSach.add(new Object[]{maPhieu, tenLoai, ngayLapPhieuStr, thanhTien});
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return danhSach;
	}
	
	public boolean capNhatTrangThai(String maPhieuDichVu, String trangThaiMoi) {
        String sql = "UPDATE PhieuDichVu SET trangThai = ? WHERE maPhieuDichVu = ?";
        Connection conn = null;
        conn = ConnectDB.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, trangThaiMoi);
            stmt.setString(2, maPhieuDichVu);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	public boolean kiemTraTonTaiPhieuDichVu(String maPhieuDichVu) {
	    Connection con = ConnectDB.getConnection();
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        String sql = "SELECT 1 FROM PhieuDichVu WHERE maPhieuDichVu = ?";
	        stmt = con.prepareStatement(sql);
	        stmt.setString(1, maPhieuDichVu);
	        rs = stmt.executeQuery();

	        return rs.next(); // nếu có kết quả => tồn tại => true
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return false; // lỗi hoặc không có => false
	}

}
