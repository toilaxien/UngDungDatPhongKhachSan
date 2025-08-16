package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.LoaiPhong;
import entity.Phong;

public class Phong_Dao {
    private ArrayList<Phong> dsp;

    public Phong_Dao() {
        dsp = new ArrayList<Phong>();
    }

    // Existing methods (unchanged)
    public ArrayList<Phong> getAllPhong() {
        try (Connection con = ConnectDB.getConnection()) {
            String sql = "SELECT * FROM Phong";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                String soPhong = rs.getString(1);
                String trangThai = rs.getString(2);
                LoaiPhong loaiPhong = new LoaiPhong(rs.getString(3));
                String moTa = rs.getString(4);

                Phong p = new Phong(soPhong, trangThai, loaiPhong, moTa);
                dsp.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsp;
    }

    public ArrayList<Phong> getDanhSachPhongTheoLoai(String maLoaiPhong) {
        ArrayList<Phong> danhSachPhong = new ArrayList<>();
        String sql = "SELECT * FROM Phong WHERE loaiPhong = ?";
        
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maLoaiPhong);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String soPhong = rs.getString("soPhong");
                String trangThai = rs.getString("trangThai");
                String loaiPhongMa = rs.getString("loaiPhong");
                String moTa = rs.getString("moTa");

                LoaiPhong loaiPhong = new LoaiPhong(loaiPhongMa);
                danhSachPhong.add(new Phong(soPhong, trangThai, loaiPhong, moTa));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachPhong;
    }

    public Phong getPhongTheoMa(String soPhong) {
        Phong phong = null;
        Connection conn = ConnectDB.getConnection();
        String sql = "SELECT * FROM Phong WHERE soPhong = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, soPhong);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String trangThai = rs.getString("trangThai");
                String loaiPhongMa = rs.getString("loaiPhong");
                String moTa = rs.getString("moTa");

                LoaiPhong loaiPhong = new LoaiPhong(loaiPhongMa);
                LoaiPhong_Dao loaiPhong_Dao = new LoaiPhong_Dao();
                loaiPhong = loaiPhong_Dao.getLoaiPhongTheoMa(loaiPhongMa);
                phong = new Phong(soPhong, trangThai, loaiPhong, moTa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return phong;
    }

    public ArrayList<Phong> getPhongTheoMaDonDatPhong(String maDonDatPhong) {
        ArrayList<Phong> dsPhong = new ArrayList<>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getConnection();
            String sql = "SELECT p.soPhong, p.trangThai, lp.*, p.moTa " + "FROM Phong p "
                    + "JOIN ChiTietDonDatPhong ctd ON p.soPhong = ctd.soPhong "
                    + "JOIN LoaiPhong lp ON p.loaiPhong = lp.maLoaiPhong "
                    + "WHERE ctd.maDonDatPhong = ? AND p.trangThai = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maDonDatPhong);
            stmt.setString(2, "Đang ở");
            rs = stmt.executeQuery();

            while (rs.next()) {
                String soPhong = rs.getString("soPhong");
                String trangThai = rs.getString("trangThai");
                String mota = rs.getString("moTa");

                String maLoai = rs.getString("maLoaiPhong");
                String tenLoai = rs.getString("tenLoai");
                int soLuong = rs.getInt("soLuong");
                float dienTich = rs.getFloat("dienTich");
                double giaGio = rs.getDouble("giaTheoGio");
                double giaNgay = rs.getDouble("giaTheoNgay");
                double giaDem = rs.getDouble("giaTheoDem");
                double phuThu = rs.getDouble("phuThuQuaGio");

                LoaiPhong lp = new LoaiPhong(maLoai, tenLoai, soLuong, dienTich, giaGio, giaNgay, giaDem, phuThu);
                Phong p = new Phong(soPhong, trangThai, lp, mota);
                dsPhong.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {}
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {}
            try {
                if (con != null) con.close();
            } catch (SQLException e) {}
        }

        return dsPhong;
    }

    public ArrayList<Phong> getPhongTheoMaDonDatPhong1(String maDonDatPhong) {
        ArrayList<Phong> dsPhong = new ArrayList<>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getConnection();
            String sql = "SELECT p.soPhong, p.trangThai, lp.*, p.moTa " + "FROM Phong p "
                    + "JOIN ChiTietSuDungPhong ctsd ON p.soPhong = ctsd.soPhong "
                    + "JOIN LoaiPhong lp ON p.loaiPhong = lp.maLoaiPhong " + "WHERE ctsd.maDonDatPhong = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maDonDatPhong);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String soPhong = rs.getString("soPhong");
                String trangThai = rs.getString("trangThai");
                String mota = rs.getString("moTa");

                String maLoai = rs.getString("maLoaiPhong");
                String tenLoai = rs.getString("tenLoai");
                int soLuong = rs.getInt("soLuong");
                float dienTich = rs.getFloat("dienTich");
                double giaGio = rs.getDouble("giaTheoGio");
                double giaNgay = rs.getDouble("giaTheoNgay");
                double giaDem = rs.getDouble("giaTheoDem");
                double phuThu = rs.getDouble("phuThuQuaGio");

                LoaiPhong lp = new LoaiPhong(maLoai, tenLoai, soLuong, dienTich, giaGio, giaNgay, giaDem, phuThu);
                Phong p = new Phong(soPhong, trangThai, lp, mota);
                dsPhong.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {}
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {}
            try {
                if (con != null) con.close();
            } catch (SQLException e) {}
        }

        return dsPhong;
    }

    public Phong getPhongBySoPhong(String soPhong) {
        Phong phong = null;
        String sql = "SELECT p.soPhong, p.trangThai, p.moTa, lp.maLoaiPhong, lp.tenLoai, lp.soLuong, lp.dienTich, lp.giaTheoGio, lp.giaTheoNgay, lp.giaTheoDem, lp.phuThuQuaGio "
                + "FROM Phong p JOIN LoaiPhong lp ON p.loaiPhong = lp.maLoaiPhong " + "WHERE p.soPhong = ?";

        try (Connection conn = ConnectDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, soPhong);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String trangThai = rs.getString("trangThai");
                String mota = rs.getString("moTa");

                String maLoai = rs.getString("maLoaiPhong");
                String tenLoai = rs.getString("tenLoai");
                int soLuong = rs.getInt("soLuong");
                float dienTich = rs.getFloat("dienTich");
                double giaGio = rs.getDouble("giaTheoGio");
                double giaNgay = rs.getDouble("giaTheoNgay");
                double giaDem = rs.getDouble("giaTheoDem");
                double phuThu = rs.getDouble("phuThuQuaGio");

                LoaiPhong lp = new LoaiPhong(maLoai, tenLoai, soLuong, dienTich, giaGio, giaNgay, giaDem, phuThu);
                phong = new Phong(soPhong, trangThai, lp, mota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phong;
    }

    public ArrayList<Phong> getPhongChuaThanhToan() {
        ArrayList<Phong> danhSach = new ArrayList<>();
        String sql = "SELECT p.soPhong, p.trangThai, lp.*, p.moTa " + "FROM Phong p "
                + "JOIN LoaiPhong lp ON p.loaiPhong = lp.maLoaiPhong "
                + "JOIN ChiTietDonDatPhong ctd ON p.soPhong = ctd.soPhong "
                + "JOIN DonDatPhong ddp ON ctd.maDonDatPhong = ddp.maDonDatPhong "
                + "WHERE ddp.trangThai = ? AND p.trangThai = ?";

        try (Connection conn = ConnectDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "Nhận phòng");
            stmt.setString(2, "Đang ở");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String soPhong = rs.getString("soPhong");
                    String trangThai = rs.getString("trangThai");
                    String mota = rs.getString("moTa");

                    String maLoai = rs.getString("maLoaiPhong");
                    String tenLoai = rs.getString("tenLoai");
                    int soLuong = rs.getInt("soLuong");
                    float dienTich = rs.getFloat("dienTich");
                    double giaGio = rs.getDouble("giaTheoGio");
                    double giaNgay = rs.getDouble("giaTheoNgay");
                    double giaDem = rs.getDouble("giaTheoDem");
                    double phuThu = rs.getDouble("phuThuQuaGio");

                    LoaiPhong lp = new LoaiPhong(maLoai, tenLoai, soLuong, dienTich, giaGio, giaNgay, giaDem, phuThu);
                    Phong p = new Phong(soPhong, trangThai, lp, mota);
                    danhSach.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }

    public boolean setTrangThaiPhong(String soPhong, String trangThai) {
        String sql = "UPDATE Phong SET trangThai = ? WHERE soPhong = ?";

        try (Connection conn = ConnectDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, trangThai);
            stmt.setString(2, soPhong);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<LoaiPhong> getAllLoaiPhong() {
        ArrayList<LoaiPhong> dsLoaiPhong = new ArrayList<>();
        String sql = "SELECT * FROM LoaiPhong";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
                dsLoaiPhong.add(lp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsLoaiPhong;
    }

	public ArrayList<String> getPhongTrongTheoLoaiVaThoiGian(String tenLoai, LocalDateTime thoiDiem) {
	    ArrayList<String> ds = new ArrayList<>();
	    String sql = """
	        SELECT p.soPhong
	        FROM Phong p
	        JOIN LoaiPhong lp ON p.loaiPhong = lp.maLoaiPhong
	        WHERE lp.tenLoai = ?
	          AND p.soPhong NOT IN (
	              SELECT soPhong
	              FROM ChiTietSuDungPhong
	              WHERE ? BETWEEN ngayBatDau AND ngayKetThuc
	          )
	    """;

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, tenLoai);
	        ps.setTimestamp(2, Timestamp.valueOf(thoiDiem));

	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            ds.add(rs.getString("soPhong"));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return ds;
	}


	public String getTenLoaiTheoSoPhong(String soPhong) {
		String sql = "SELECT lp.tenLoai FROM Phong p JOIN LoaiPhong lp ON p.loaiPhong = lp.maLoaiPhong WHERE p.soPhong = ?";
		try (Connection con = ConnectDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, soPhong);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("tenLoai");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

    // New methods to address duplicate data issue
    public ArrayList<Phong> getAllPhongNew() {
        ArrayList<Phong> danhSachPhong = new ArrayList<>();
        String sql = "SELECT p.soPhong, p.trangThai, p.moTa, lp.maLoaiPhong, lp.tenLoai, lp.soLuong, lp.dienTich, lp.giaTheoGio, lp.giaTheoNgay, lp.giaTheoDem, lp.phuThuQuaGio " +
                     "FROM Phong p JOIN LoaiPhong lp ON p.loaiPhong = lp.maLoaiPhong";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String soPhong = rs.getString("soPhong");
                String trangThai = rs.getString("trangThai");
                String moTa = rs.getString("moTa");

                String maLoai = rs.getString("maLoaiPhong");
                String tenLoai = rs.getString("tenLoai");
                int soLuong = rs.getInt("soLuong");
                float dienTich = rs.getFloat("dienTich");
                double giaGio = rs.getDouble("giaTheoGio");
                double giaNgay = rs.getDouble("giaTheoNgay");
                double giaDem = rs.getDouble("giaTheoDem");
                double phuThu = rs.getDouble("phuThuQuaGio");

                LoaiPhong lp = new LoaiPhong(maLoai, tenLoai, soLuong, dienTich, giaGio, giaNgay, giaDem, phuThu);
                Phong p = new Phong(soPhong, trangThai, lp, moTa);
                danhSachPhong.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachPhong;
    }

    public boolean themPhongNew(Phong phong) {
        // Check if room with soPhong already exists
        if (getPhongBySoPhong(phong.getSoPhong()) != null) {
            return false; // Room already exists
        }

        String sql = "INSERT INTO Phong (soPhong, trangThai, loaiPhong, moTa) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, phong.getSoPhong());
            pst.setString(2, phong.getTrangThai());
            pst.setString(3, phong.getLoaiPhong().getMaLoaiPhong());
            pst.setString(4, phong.getMoTa());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatPhongNew(Phong phong) {
        String sql = "UPDATE Phong SET trangThai = ?, loaiPhong = ?, moTa = ? WHERE soPhong = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phong.getTrangThai());
            stmt.setString(2, phong.getLoaiPhong().getMaLoaiPhong());
            stmt.setString(3, phong.getMoTa());
            stmt.setString(4, phong.getSoPhong());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaPhongNew(String soPhong) {
        String sql = "DELETE FROM Phong WHERE soPhong = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, soPhong);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean existsSoPhong(String soPhong, String excludeSoPhong) throws Exception {
        // Implementation: Check if soPhong exists in the database, excluding excludeSoPhong
        // Example SQL: SELECT COUNT(*) FROM Phong WHERE soPhong = ? AND soPhong != ?
       ArrayList<Phong> list = getAllPhongNew();
        return list.stream().anyMatch(p -> p.getSoPhong().equalsIgnoreCase(soPhong) && 
                (excludeSoPhong == null || !p.getSoPhong().equalsIgnoreCase(excludeSoPhong)));
    }

}
   