package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectDB.ConnectDB;
import entity.ChiTietPhieuDichVu;
import entity.DichVu;
import entity.PhieuDichVu;

public class ChiTietPhieuDichVu_DAO {

    public boolean xoaChiTietPhieuDichVuTheoMaDichVu(String maDichVu){
        String sql = "DELETE FROM ChiTietPhieuDichVu WHERE maDichVu = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maDichVu);
            int rowsDeleted = stmt.executeUpdate();

            // Nếu có ít nhất một hàng bị xóa, tức là xóa thành công
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Xóa thất bại
        }
    }
    public boolean them(ChiTietPhieuDichVu ctpdv) {
        String sql = "INSERT INTO ChiTietPhieuDichVu (maPhieuDichVu, maDichVu, soLuong) VALUES (?, ?, ?)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ctpdv.getPhieuDichVu().getMaPhieuDichVu());
            stmt.setString(2, ctpdv.getDichVu().getMaDV());
            stmt.setInt(3, ctpdv.getSoLuong());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ChiTietPhieuDichVu getChiTietPhieuDichVu(String maPhieuDichVu, String maDichVu) {
        ChiTietPhieuDichVu chiTiet = null;

        String sql = "SELECT soLuong FROM ChiTietPhieuDichVu WHERE maPhieuDichVu = ? AND maDichVu = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maPhieuDichVu);
            stmt.setString(2, maDichVu);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int soLuong = rs.getInt("soLuong");

                PhieuDichVu phieu = new PhieuDichVu(maPhieuDichVu); // cần constructor phù hợp
                DichVu dv = new DichVu(maDichVu);                   // cần constructor phù hợp

                chiTiet = new ChiTietPhieuDichVu(phieu, dv, soLuong);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chiTiet;
    }
}
