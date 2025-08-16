package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import connectDB.ConnectDB;
import entity.LoaiPhong;

public class LoaiPhong_Dao {
    private ArrayList<LoaiPhong> dslp;

    public LoaiPhong_Dao() {
        dslp = new ArrayList<LoaiPhong>();
    }

    // Existing methods (unchanged)
    public ArrayList<LoaiPhong> getAllLoaiPhong() {
        dslp.clear(); // Clear the list to avoid duplicates
        try (Connection con = ConnectDB.getConnection()) {
            String sql = "SELECT * FROM LoaiPhong";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                String maLoaiPhong = rs.getString("maLoaiPhong");
                String tenLoai = rs.getString("tenLoai");
                int soLuong = rs.getInt("soLuong");
                float dienTich = rs.getFloat("dienTich");
                double giaTheoGio = rs.getDouble("giaTheoGio");
                double giaTheoNgay = rs.getDouble("giaTheoNgay");
                double giaTheoDem = rs.getDouble("giaTheoDem");
                double phuThuQuaGio = rs.getDouble("phuThuQuaGio");

                LoaiPhong lp = new LoaiPhong(maLoaiPhong, tenLoai, soLuong, dienTich,
                        giaTheoGio, giaTheoNgay, giaTheoDem, phuThuQuaGio);
                dslp.add(lp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dslp;
    }

    public LoaiPhong getLoaiPhongTheoMa(String maLoaiPhong) {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM LoaiPhong WHERE maLoaiPhong = ?")) {
            stmt.setString(1, maLoaiPhong);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new LoaiPhong(
                        rs.getString("maLoaiPhong"),
                        rs.getString("tenLoai"),
                        rs.getInt("soLuong"),
                        rs.getFloat("dienTich"),
                        rs.getDouble("giaTheoGio"),
                        rs.getDouble("giaTheoNgay"),
                        rs.getDouble("giaTheoDem"),
                        rs.getDouble("phuThuQuaGio")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<String> getDanhSachTenLoai() {
        ArrayList<String> ds = new ArrayList<>();
        String sql = "SELECT tenLoai FROM LoaiPhong";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ds.add(rs.getString("tenLoai"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }

    public LoaiPhong getLoaiPhongBySoPhong(String soPhong) {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT lp.* FROM LoaiPhong lp JOIN Phong p ON lp.maLoaiPhong = p.loaiPhong WHERE p.soPhong = ?")) {
            ps.setString(1, soPhong);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new LoaiPhong(
                        rs.getString("maLoaiPhong"),
                        rs.getString("tenLoai"),
                        rs.getInt("soLuong"),
                        rs.getFloat("dienTich"),
                        rs.getDouble("giaTheoGio"),
                        rs.getDouble("giaTheoNgay"),
                        rs.getDouble("giaTheoDem"),
                        rs.getDouble("phuThuQuaGio")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean them(LoaiPhong lp) {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO LoaiPhong (maLoaiPhong, tenLoai, soLuong, dienTich, giaTheoGio, giaTheoNgay, giaTheoDem, phuThuQuaGio) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, lp.getMaLoaiPhong());
            stmt.setString(2, lp.getTenLoai());
            stmt.setInt(3, lp.getSoLuong());
            stmt.setFloat(4, lp.getDienTich());
            stmt.setDouble(5, lp.getGiaTheoGio());
            stmt.setDouble(6, lp.getGiaTheoNgay());
            stmt.setDouble(7, lp.getGiaTheoDem());
            stmt.setDouble(8, lp.getPhuThuQuaGio());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sua(LoaiPhong lp) {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE LoaiPhong SET tenLoai = ?, soLuong = ?, dienTich = ?, giaTheoGio = ?, giaTheoNgay = ?, " +
                             "giaTheoDem = ?, phuThuQuaGio = ? WHERE maLoaiPhong = ?")) {
            stmt.setString(1, lp.getTenLoai());
            stmt.setInt(2, lp.getSoLuong());
            stmt.setFloat(3, lp.getDienTich());
            stmt.setDouble(4, lp.getGiaTheoGio());
            stmt.setDouble(5, lp.getGiaTheoNgay());
            stmt.setDouble(6, lp.getGiaTheoDem());
            stmt.setDouble(7, lp.getPhuThuQuaGio());
            stmt.setString(8, lp.getMaLoaiPhong());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoa(String maLoaiPhong) {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM LoaiPhong WHERE maLoaiPhong = ?")) {
            stmt.setString(1, maLoaiPhong);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsPhongByLoaiPhong(String maLoaiPhong) {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM Phong WHERE loaiPhong = ?")) {
            stmt.setString(1, maLoaiPhong);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // New methods to handle room type operations without modifying existing ones
    public ArrayList<LoaiPhong> getAllLoaiPhongNew() {
        ArrayList<LoaiPhong> danhSachLoaiPhong = new ArrayList<>();
        String sql = "SELECT * FROM LoaiPhong";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LoaiPhong lp = new LoaiPhong(
                        rs.getString("maLoaiPhong"),
                        rs.getString("tenLoai"),
                        rs.getInt("soLuong"),
                        rs.getFloat("dienTich"),
                        rs.getDouble("giaTheoGio"),
                        rs.getDouble("giaTheoNgay"),
                        rs.getDouble("giaTheoDem"),
                        rs.getDouble("phuThuQuaGio")
                );
                danhSachLoaiPhong.add(lp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachLoaiPhong;
    }

    public boolean themLoaiPhongNew(LoaiPhong lp) {
        // Check if room type with maLoaiPhong already exists
        if (getLoaiPhongTheoMa(lp.getMaLoaiPhong()) != null) {
            return false; // Room type already exists
        }

        String sql = "INSERT INTO LoaiPhong (maLoaiPhong, tenLoai, soLuong, dienTich, giaTheoGio, giaTheoNgay, giaTheoDem, phuThuQuaGio) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lp.getMaLoaiPhong());
            stmt.setString(2, lp.getTenLoai());
            stmt.setInt(3, lp.getSoLuong());
            stmt.setFloat(4, lp.getDienTich());
            stmt.setDouble(5, lp.getGiaTheoGio());
            stmt.setDouble(6, lp.getGiaTheoNgay());
            stmt.setDouble(7, lp.getGiaTheoDem());
            stmt.setDouble(8, lp.getPhuThuQuaGio());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaLoaiPhongNew(LoaiPhong lp) {
        String sql = "UPDATE LoaiPhong SET tenLoai = ?, soLuong = ?, dienTich = ?, giaTheoGio = ?, giaTheoNgay = ?, " +
                     "giaTheoDem = ?, phuThuQuaGio = ? WHERE maLoaiPhong = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lp.getTenLoai());
            stmt.setInt(2, lp.getSoLuong());
            stmt.setFloat(3, lp.getDienTich());
            stmt.setDouble(4, lp.getGiaTheoGio());
            stmt.setDouble(5, lp.getGiaTheoNgay());
            stmt.setDouble(6, lp.getGiaTheoDem());
            stmt.setDouble(7, lp.getPhuThuQuaGio());
            stmt.setString(8, lp.getMaLoaiPhong());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaLoaiPhongNew(String maLoaiPhong) {
        // Check if any rooms are associated with this room type
        if (existsPhongByLoaiPhong(maLoaiPhong)) {
            return false; // Cannot delete because rooms are associated
        }

        String sql = "DELETE FROM LoaiPhong WHERE maLoaiPhong = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maLoaiPhong);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean existsTenLoaiPhong(String tenLoai, String excludeMaLoai) throws Exception {
//    	
        // Implementation: Check if room type name exists (excluding given maLoaiPhong)
        ArrayList<LoaiPhong> list = getAllLoaiPhongNew();
        return list.stream().anyMatch(lp -> lp.getTenLoai().equalsIgnoreCase(tenLoai) && !lp.getMaLoaiPhong().equals(excludeMaLoai));
    }

}
