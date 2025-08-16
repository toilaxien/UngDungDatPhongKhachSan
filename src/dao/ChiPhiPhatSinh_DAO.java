package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectDB.ConnectDB;
import entity.ChiPhiPhatSinh;
import entity.DonDatPhong;

public class ChiPhiPhatSinh_DAO {

    public boolean themChiPhiPhatSinh(ChiPhiPhatSinh chiPhi) {
        String sql = "INSERT INTO ChiPhiPhatSinh (maChiPhi, chiPhiTBHong, soGioOQuaGio, maDonDatPhong, moTa) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, chiPhi.getMaChiPhi());
            pst.setDouble(2, chiPhi.getChiPhiTBHong());
            pst.setInt(3, chiPhi.getSoGioThem());
            pst.setString(4, chiPhi.getDonDatPhong().getMaDonDatPhong()); 
            pst.setString(5, chiPhi.getMoTa());

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ChiPhiPhatSinh getChiPhiPhatSinhByMaChiPhi(String maChiPhi) {
        String sql = "SELECT * FROM ChiPhiPhatSinh WHERE maChiPhi = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, maChiPhi);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                ChiPhiPhatSinh chiPhi = new ChiPhiPhatSinh();
                chiPhi.setMaChiPhi(rs.getString("maChiPhi"));
                chiPhi.setChiPhiTBHong(rs.getDouble("chiPhiTBHong"));
                chiPhi.setSoGioThem(rs.getInt("soGioThem"));

                // DonDatPhong ऑब्जेक्ट मिळवा
                DonDatPhong donDatPhong = new DonDatPhong();
                donDatPhong.setMaDonDatPhong(rs.getString("maDonDatPhong"));
                chiPhi.setDonDatPhong(donDatPhong);

                chiPhi.setMoTa(rs.getString("moTa"));
                return chiPhi;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean suaChiPhiPhatSinh(ChiPhiPhatSinh chiPhi) {
        String sql = "UPDATE ChiPhiPhatSinh SET chiPhiTBHong = ?, soGioOQuaGio = ?, maDonDatPhong = ?, moTa = ? WHERE maChiPhi = ?";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setDouble(1, chiPhi.getChiPhiTBHong());
            pst.setInt(2, chiPhi.getSoGioThem());
            pst.setString(3, chiPhi.getDonDatPhong().getMaDonDatPhong());
            pst.setString(4, chiPhi.getMoTa());
            pst.setString(5, chiPhi.getMaChiPhi());  // WHERE điều kiện sửa theo mã

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}