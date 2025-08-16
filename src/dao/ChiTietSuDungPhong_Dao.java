package dao;

import connectDB.ConnectDB;
import entity.ChiTietSuDungPhong;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ChiTietSuDungPhong_Dao {

    public boolean themChiTiet(ChiTietSuDungPhong ct) {
        String sql = "INSERT INTO ChiTietSuDungPhong (maDonDatPhong, soPhong, ngayBatDau, ngayKetThuc, ghiChu) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ct.getMaDonDatPhong());
            ps.setString(2, ct.getSoPhong());
            ps.setTimestamp(3, Timestamp.valueOf(ct.getNgayBatDau()));
            ps.setTimestamp(4, Timestamp.valueOf(ct.getNgayKetThuc()));
            ps.setString(5, ct.getGhiChu());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatNgayKetThuc(String maDon, String soPhong, LocalDateTime ngayKetThucMoi) {
        String sql = "UPDATE ChiTietSuDungPhong SET ngayKetThuc = ? WHERE maDonDatPhong = ? AND soPhong = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(ngayKetThucMoi));
            ps.setString(2, maDon);
            ps.setString(3, soPhong);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<ChiTietSuDungPhong> getDanhSachTheoDon(String maDon) {
        ArrayList<ChiTietSuDungPhong> ds = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietSuDungPhong WHERE maDonDatPhong = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maDon);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ChiTietSuDungPhong ct = new ChiTietSuDungPhong(
                        rs.getString("maDonDatPhong"),
                        rs.getString("soPhong"),
                        rs.getTimestamp("ngayBatDau").toLocalDateTime(),
                        rs.getTimestamp("ngayKetThuc").toLocalDateTime(),
                        rs.getString("ghiChu")
                );
                ds.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
    
    public ArrayList<ChiTietSuDungPhong> getDanhSachPhongDangO() {
        ArrayList<ChiTietSuDungPhong> ds = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietSuDungPhong WHERE ? BETWEEN ngayBatDau AND ngayKetThuc";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            ps.setTimestamp(1, now);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietSuDungPhong ct = new ChiTietSuDungPhong(
                    rs.getString("maDonDatPhong"),
                    rs.getString("soPhong"),
                    rs.getTimestamp("ngayBatDau").toLocalDateTime(),
                    rs.getTimestamp("ngayKetThuc").toLocalDateTime(),
                    rs.getString("ghiChu")
                );
                ds.add(ct);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }
    public ArrayList<ChiTietSuDungPhong> getTheoMaDon(String maDon) {
        ArrayList<ChiTietSuDungPhong> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietSuDungPhong WHERE maDonDatPhong = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maDon);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String maDonDat = rs.getString("maDonDatPhong");
                String soPhong = rs.getString("soPhong");
                LocalDateTime batDau = rs.getTimestamp("ngayBatDau").toLocalDateTime();
                LocalDateTime ketThuc = rs.getTimestamp("ngayKetThuc").toLocalDateTime();
                String ghiChu = rs.getString("ghiChu");

                ChiTietSuDungPhong ct = new ChiTietSuDungPhong(maDonDat, soPhong, batDau, ketThuc, ghiChu);
                danhSach.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return danhSach;
    }


}
