package dao;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.KhuyenMai;


public class KhuyenMai_DAO {

    // Lấy tất cả khuyến mãi
    public ArrayList<KhuyenMai> getAllKhuyenMai() {
        ArrayList<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String ma = rs.getString("maKhuyenMai");
                String ten = rs.getString("tenKhuyenMai");
                String loai = rs.getString("loaiKhuyenMai");
                Double giaTri = rs.getDouble("giaTriKhuyenMai");
                LocalDate ngayBD = rs.getDate("ngayBatDau").toLocalDate();
                LocalDate ngayKT = rs.getDate("ngayKetThuc").toLocalDate();
                Double dieuKien = rs.getDouble("dieuKienApDung");
                String trangThai = rs.getString("trangThai");

                KhuyenMai km = new KhuyenMai(ma, ten, loai, giaTri, ngayBD, ngayKT, dieuKien, trangThai);
                list.add(km);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public ArrayList<KhuyenMai> getKhuyenMaiTheoNgay(LocalDate ngayNhan, LocalDate ngayTra, double tongThanhToan) {
        ArrayList<KhuyenMai> list = new ArrayList<>();
        String sql = """
            SELECT *
            FROM   KhuyenMai
            WHERE  ? BETWEEN ngayBatDau AND ngayKetThuc
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(ngayTra));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String ma       = rs.getString("maKhuyenMai");
                    String ten      = rs.getString("tenKhuyenMai");
                    String loai     = rs.getString("loaiKhuyenMai");
                    double giaTri   = rs.getDouble("giaTriKhuyenMai");
                    LocalDate ngayBD= rs.getDate("ngayBatDau").toLocalDate();
                    LocalDate ngayKT= rs.getDate("ngayKetThuc").toLocalDate();
                    double dieuKien = rs.getDouble("dieuKienApDung");
                    String trangThai= rs.getString("trangThai");

                    boolean hopLe = false;
                    if (loai.equalsIgnoreCase("Khuyến mãi theo dịp lễ")) {
                        hopLe = tongThanhToan >= dieuKien;
                    } else if (loai.equalsIgnoreCase("Khuyến mãi theo số ngày khách ở")) {
                        long soNgayO = ChronoUnit.DAYS.between(ngayNhan, ngayTra);
                        hopLe = soNgayO >= dieuKien;
                    }

                    if (hopLe) {
                        list.add(new KhuyenMai(
                                ma, ten, loai, giaTri,
                                ngayBD, ngayKT, dieuKien, trangThai));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public void capNhatTrangThaiKhuyenMaiHetHan() {
        String sql = "UPDATE KhuyenMai SET trangThai = 'Đã dừng' WHERE ngayKetThuc < ? AND trangThai != 'Đã dừng'";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Thêm khuyến mãi
    public boolean addKhuyenMai(KhuyenMai km) {
        String sql = "INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, giaTriKhuyenMai, ngayBatDau, ngayKetThuc, dieuKienApDung, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, km.getMaKhuyenMai());
            stmt.setString(2, km.getTenKhuyenMai());
            stmt.setString(3, km.getLoaiKhuyenMai());
            stmt.setDouble(4, km.getGiaTriKhuyenMai());
            stmt.setDate(5, Date.valueOf(km.getNgayBatDau()));
            stmt.setDate(6, Date.valueOf(km.getNgayKetThuc()));
            stmt.setDouble(7, km.getDieuKienApDung());
            stmt.setString(8, km.getTrangThai());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Cập nhật khuyến mãi
    public boolean updateKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET tenKhuyenMai=?, loaiKhuyenMai=?, giaTriKhuyenMai=?, ngayBatDau=?, ngayKetThuc=?, dieuKienApDung=?, trangThai=? WHERE maKhuyenMai=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, km.getTenKhuyenMai());
            stmt.setString(2, km.getLoaiKhuyenMai());
            stmt.setDouble(3, km.getGiaTriKhuyenMai());
            stmt.setDate(4, Date.valueOf(km.getNgayBatDau()));
            stmt.setDate(5, Date.valueOf(km.getNgayKetThuc()));
            stmt.setDouble(6, km.getDieuKienApDung());
            stmt.setString(7, km.getTrangThai());
            stmt.setString(8, km.getMaKhuyenMai());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Xoá khuyến mãi
    public boolean deleteKhuyenMai(String maKhuyenMai) {
        String sql = "DELETE FROM KhuyenMai WHERE maKhuyenMai = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maKhuyenMai);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Tìm kiếm khuyến mãi theo mã
    public KhuyenMai findById(String maKhuyenMai) {
        String sql = "SELECT * FROM KhuyenMai WHERE maKhuyenMai = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maKhuyenMai);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new KhuyenMai(
                    rs.getString("maKhuyenMai"),
                    rs.getString("tenKhuyenMai"),
                    rs.getString("loaiKhuyenMai"),
                    rs.getDouble("giaTriKhuyenMai"),
                    rs.getDate("ngayBatDau").toLocalDate(),
                    rs.getDate("ngayKetThuc").toLocalDate(),
                    rs.getDouble("dieuKienApDung"),
                    rs.getString("trangThai")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public boolean updateTrangThaiKhuyenMai(String maKM, String trangThai) {
        String sql = "UPDATE KhuyenMai SET trangThai = ? WHERE maKhuyenMai = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, trangThai);
            stmt.setString(2, maKM);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
