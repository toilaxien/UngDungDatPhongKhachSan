package dao;

import connectDB.ConnectDB;
import entity.KhachHang;

import java.sql.*;
import java.util.ArrayList;

public class KhachHang_Dao {
    private ArrayList<KhachHang> dskh;
    
    public KhachHang_Dao() {
        dskh = new ArrayList<KhachHang>();
    }

    public ArrayList<KhachHang> getAllKhachHang() {
        try (Connection con = ConnectDB.getConnection()) {
            String sql = "SELECT * FROM KhachHang";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                String maKH = rs.getString(1);
                String hoTen = rs.getString(2);
                String sdt = rs.getString(3);
                String soCCCD = rs.getString(4);
                String email = rs.getString(5);
                KhachHang kh = new KhachHang(maKH, hoTen, sdt, soCCCD,email);
                dskh.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dskh;
    }

    public boolean them(KhachHang kh) {
        try (Connection con = ConnectDB.getConnection()) {
            String sql = "INSERT INTO KhachHang (maKH, hoTen, sdt, soCCCD) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, kh.getMaKH());
            stmt.setString(2, kh.getHoTen());
            stmt.setString(3, kh.getSdt());
            stmt.setString(4, kh.getSoCCCD());
            stmt.setString(5, kh.getEmail());
            int n = stmt.executeUpdate();
            return n > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sua(KhachHang kh) {
        try (Connection con = ConnectDB.getConnection()) {
            String sql = "UPDATE KhachHang SET hoTen = ?, sdt = ?, soCCCD = ?, email =? WHERE maKH = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, kh.getHoTen());
            stmt.setString(2, kh.getSdt());
            stmt.setString(3, kh.getSoCCCD());
            stmt.setString(4, kh.getEmail());
            stmt.setString(5, kh.getMaKH());
   
            int n = stmt.executeUpdate();
            return n > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoa(String maKH) {
        try (Connection con = ConnectDB.getConnection()) {
            String sql = "DELETE FROM KhachHang WHERE maKH = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maKH);

            int n = stmt.executeUpdate();
            return n > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public KhachHang timTheoMa(String maKH) {
        KhachHang kh = null;
        try (Connection con = ConnectDB.getConnection()) {
            String sql = "SELECT * FROM KhachHang WHERE maKH = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maKH);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hoTen = rs.getString(2);
                String sdt = rs.getString(3);
                String soCCCD = rs.getString(4);
                String email = rs.getString(5);

                kh = new KhachHang(maKH, hoTen, sdt, soCCCD,email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kh;
    }

    public ArrayList<KhachHang> timTheoSdt(String sdt) {
        ArrayList<KhachHang> dskh = new ArrayList<KhachHang>();
        try (Connection con = ConnectDB.getConnection()) {
            String sql = "SELECT * FROM KhachHang WHERE sdt = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, sdt);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String maKH = rs.getString(1);
                String hoTen = rs.getString(2);
                String soCCCD = rs.getString(4);
                String email = rs.getString(5);
                KhachHang kh = new KhachHang(maKH, hoTen, sdt, soCCCD,email);
                dskh.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dskh;
    }
    
    public boolean themThongTinKhachHang(String hoTen, String sdtMoi, String soCccd, String email, String ngayHienTai) {
        boolean result = false;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Connection connection = ConnectDB.getConnection();
            System.out.println("ngayhientai:"+ngayHienTai);
            // Đếm số khách hàng đăng ký trong ngày để tạo số thứ tự
            String demSql = "SELECT COUNT(*) AS tongSoKhach FROM KhachHang WHERE maKH LIKE ?";
            stmt = connection.prepareStatement(demSql);
            stmt.setString(1, "KH" + ngayHienTai + "%");
            rs = stmt.executeQuery();

            int soLuongKhach = 0;
            if (rs.next()) {
                soLuongKhach = rs.getInt("tongSoKhach");
            }
            rs.close();
            stmt.close();

            // Tăng số thứ tự thêm 1
            soLuongKhach++;

            // Tạo mã khách hàng: KH + DDMMYYYY + 4 chữ số tự động tăng
            String maKH = "KH" + ngayHienTai + String.format("%04d", soLuongKhach);
            System.out.println("mã khách hàng:"+maKH);
            // Thêm khách hàng vào CSDL
            String insertSql = "INSERT INTO KhachHang (maKH, hoTen, sdt, soCCCD, email) VALUES (?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(insertSql);
            stmt.setString(1, maKH);
            stmt.setNString(2, hoTen);
            stmt.setString(3, sdtMoi);
            stmt.setString(4, soCccd);
            stmt.setNString(5, email);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                result = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    
    public KhachHang timKhachHangTheoSoDienThoai(String sdt) {
        KhachHang kh = null;
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT * FROM KhachHang WHERE sdt = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sdt);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                kh = new KhachHang(
                    rs.getString("maKH"),
                    rs.getString("hoTen"),
                    rs.getString("sdt"),
                    rs.getString("soCCCD"),
                    rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("thông tin khách hàng được lấy lại theo sdt:"+kh);
        return kh;
    }
    
	 public KhachHang layKhachHangTheoMaDonDatPhong(String maDonDatPhong) {
	        KhachHang khachHang = null;
	        String sql = "SELECT kh.* FROM KhachHang kh "
	                   + "JOIN DonDatPhong ddp ON kh.maKH = ddp.maKH "
	                   + "WHERE ddp.maDonDatPhong = ?";

	        try (Connection con = ConnectDB.getConnection();
	             PreparedStatement stmt = con.prepareStatement(sql)) {

	            stmt.setString(1, maDonDatPhong);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                khachHang = new KhachHang(
	                    rs.getString("maKH"),
	                    rs.getString("hoTen"),
	                    rs.getString("sdt"),
	                    rs.getString("soCCCD"),
	                    rs.getString("email")
	                );
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return khachHang;
	    }

	 public boolean suaCCCD(KhachHang kh) {
		    try (Connection con = ConnectDB.getConnection()) {
		        String sql = "UPDATE KhachHang SET soCCCD = ? WHERE maKH = ?";
		        PreparedStatement stmt = con.prepareStatement(sql);
		        stmt.setString(1, kh.getSoCCCD());
		        stmt.setString(2, kh.getMaKH());

		        int n = stmt.executeUpdate();
		        return n > 0;
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
		}

}
