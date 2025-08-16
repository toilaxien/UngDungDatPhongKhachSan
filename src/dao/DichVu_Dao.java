package dao;

import connectDB.ConnectDB;
import entity.DichVu;
import entity.LoaiDichVu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DichVu_Dao {

    public ArrayList<DichVu> getAllDichVu() {
        ArrayList<DichVu> listDichVu = new ArrayList<>();
        try (Connection con = ConnectDB.getConnection()) {
            String sql = "SELECT * FROM DichVu";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                String maDV = rs.getString(1);
                String tenDV = rs.getString(2);
                String moTa = rs.getString(3);
                double giaDV = rs.getDouble(4);
                String maLoai = rs.getString(5);
                
                LoaiDichVu loaiDichVu = new LoaiDichVu(maLoai);
                DichVu dichVu = new DichVu(maDV, tenDV, moTa, giaDV, loaiDichVu);
                listDichVu.add(dichVu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listDichVu;
    }

    public boolean them(DichVu dichVu) {
        try (Connection con = ConnectDB.getConnection()) {
            String sql = "INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, dichVu.getMaDV());
            stmt.setString(2, dichVu.getTenDV());
            stmt.setString(3, dichVu.getMoTa());
            stmt.setDouble(4, dichVu.getGiaDV());
            stmt.setString(5, dichVu.getLoaiDichVu().getMaLoai());

            int n = stmt.executeUpdate();
            return n > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sua(DichVu dichVu) {
        try (Connection con = ConnectDB.getConnection()) {
            String sql = "UPDATE DichVu SET tenDV = ?, moTa = ?, giaDV = ?, maLoai = ? WHERE maDV = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, dichVu.getTenDV());
            stmt.setString(2, dichVu.getMoTa());
            stmt.setDouble(3, dichVu.getGiaDV());
            stmt.setString(4, dichVu.getLoaiDichVu().getMaLoai());
            stmt.setString(5, dichVu.getMaDV());

            int n = stmt.executeUpdate();
            return n > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoa(String maDV) {
        try (Connection con = ConnectDB.getConnection()) {
            String sql = "DELETE FROM DichVu WHERE maDV = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maDV);

            int n = stmt.executeUpdate();
            return n > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public DichVu timTheoMaDV(String maDV) {
        DichVu dichVu = null;
        try (Connection con = ConnectDB.getConnection()) {
            String sql = "SELECT * FROM DichVu WHERE maDV = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maDV);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String tenDV = rs.getString(2);
                String moTa = rs.getString(3);
                double giaDV = rs.getDouble(4);
                String maLoai = rs.getString(5);
                
                LoaiDichVu loaiDichVu = new LoaiDichVu(maLoai); // Tạo đối tượng LoaiDichVu từ mã loại
                dichVu = new DichVu(maDV, tenDV, moTa, giaDV, loaiDichVu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dichVu;
    }
    
    public ArrayList<DichVu> getDichVuTheoPhieuVaLoai(String maPhieuDichVu, String tenLoaiDichVu) {
        ArrayList<DichVu> ds = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = """
                SELECT dv.maDV, dv.tenDV, dv.moTa, dv.giaDV,
                  ldv.maLoai, ldv.tenLoai
                FROM ChiTietPhieuDichVu ctpdv
                JOIN DichVu dv ON ctpdv.maDichVu = dv.maDV
                JOIN LoaiDichVu ldv ON dv.maLoai = ldv.maLoai
                WHERE ctpdv.maPhieuDichVu = ? AND ldv.tenLoai = ?
            """;

            stmt = con.prepareStatement(sql);
            stmt.setString(1, maPhieuDichVu);
            stmt.setString(2, tenLoaiDichVu);

            rs = stmt.executeQuery();
            while (rs.next()) {
            	String maDV = rs.getString("maDV");
            	String tenDV = rs.getString("tenDV"); 
            	String moTa = rs.getString("moTa");
            	double donGia = rs.getDouble("giaDV"); 


				String maLoai = rs.getString("maLoai"); 
				String tenLoai = rs.getString("tenLoai"); 
                LoaiDichVu loai = new LoaiDichVu(maLoai, tenLoai);

                DichVu dv = new DichVu(maDV, tenDV, moTa, donGia, loai);
                ds.add(dv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); if (rs != null) rs.close(); } catch (SQLException e) {}
        }

        return ds;
    }
    public ArrayList<DichVu> getDichVuTheoLoai(String maLoaiDV) {
        ArrayList<DichVu> ds = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
        	String sql = "SELECT dv.maDV, dv.tenDV, dv.moTa, dv.giaDV, "
        	           + "ldv.maLoai, ldv.tenLoai "
        	           + "FROM DichVu dv "
        	           + "JOIN LoaiDichVu ldv ON dv.maLoai = ldv.maLoai "
        	           + "WHERE ldv.maLoai = ?";


            stmt = con.prepareStatement(sql);
            stmt.setString(1, maLoaiDV);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String maDV = rs.getString("maDV");
                String tenDV = rs.getString("tenDV"); 
                String moTa = rs.getString("moTa");
                double donGia = rs.getDouble("giaDV"); 

                String maLoai = rs.getString("maLoai"); 
                String tenLoai = rs.getString("tenLoai"); 
                LoaiDichVu loai = new LoaiDichVu(maLoai, tenLoai);

                DichVu dv = new DichVu(maDV, tenDV, moTa, donGia, loai);
                ds.add(dv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); if (rs != null) rs.close(); } catch (SQLException e) {}
        }

        return ds;
    }
    public List<DichVu> getAllDichVu1() {
        List<DichVu> listDichVu = new ArrayList<>();
        String sql = """
            SELECT dv.maDV, dv.tenDV, dv.moTa, dv.giaDV, dv.maLoai, ldv.tenLoai
            FROM DichVu dv
            JOIN LoaiDichVu ldv ON dv.maLoai = ldv.maLoai
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String maDV = rs.getString("maDV");
                String tenDV = rs.getString("tenDV");
                String moTa = rs.getString("moTa");
                double giaDV = rs.getDouble("giaDV");
                String maLoai = rs.getString("maLoai");
                String tenLoai = rs.getString("tenLoai");

                LoaiDichVu loaiDichVu = new LoaiDichVu(maLoai, tenLoai);
                DichVu dichVu = new DichVu(maDV, tenDV, moTa, giaDV, loaiDichVu);
                listDichVu.add(dichVu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listDichVu;
    }


}
