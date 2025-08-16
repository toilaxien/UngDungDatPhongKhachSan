package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import connectDB.ConnectDB;
import entity.NhanVien;

public class NhanVien_Dao {
    private ArrayList<NhanVien> dsnv;

    public NhanVien_Dao() {
        dsnv = new ArrayList<NhanVien>();
    }

    public ArrayList<NhanVien> getAllNhanVien() {
        dsnv.clear(); // Clear the list to avoid stale data
        try (Connection con = ConnectDB.getConnection();
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM NhanVien")) {
            while (rs.next()) {
                String maNV = rs.getString("maNV");
                String hoTen = rs.getString("hoTen");
                LocalDate ngaySinh = rs.getDate("ngaySinh").toLocalDate();
                String sdt = rs.getString("sdt");
                String diaChi = rs.getString("diaChi");
                String soCCCD = rs.getString("soCCCD");
                String chucVu = rs.getString("chucVu");
                String caLamViec = rs.getString("caLamViec");

                NhanVien nv = new NhanVien(maNV, hoTen, ngaySinh, sdt, diaChi, soCCCD, chucVu, caLamViec);
                dsnv.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("getAllNhanVien: Loaded " + dsnv.size() + " employees");
        return new ArrayList<>(dsnv); // Return a copy to prevent external modification
    }

    public ArrayList<NhanVien> getAllLeTan() {
        ArrayList<NhanVien> dsLeTan = new ArrayList<>();
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM NhanVien WHERE chucVu = N'Lễ tân'");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String maNV = rs.getString("maNV");
                String hoTen = rs.getString("hoTen");
                LocalDate ngaySinh = rs.getDate("ngaySinh").toLocalDate();
                String sdt = rs.getString("sdt");
                String diaChi = rs.getString("diaChi");
                String soCCCD = rs.getString("soCCCD");
                String chucVu = rs.getString("chucVu");
                String caLamViec = rs.getString("caLamViec");

                NhanVien nv = new NhanVien(maNV, hoTen, ngaySinh, sdt, diaChi, soCCCD, chucVu, caLamViec);
                dsLeTan.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("getAllLeTan: Loaded " + dsLeTan.size() + " Lễ tân employees");
        return dsLeTan;
    }

    public boolean them(NhanVien nv) {
        if (dsnv.contains(nv)) {
            System.out.println("them: Employee " + nv.getMaNV() + " already exists in list");
            return false;
        }
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement("INSERT INTO NhanVien VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, nv.getMaNV());
            stmt.setString(2, nv.getHoTen());
            stmt.setDate(3, java.sql.Date.valueOf(nv.getNgaySinh()));
            stmt.setString(4, nv.getSdt());
            stmt.setString(5, nv.getDiaChi());
            stmt.setString(6, nv.getSoCCCD());
            stmt.setString(7, nv.getChucVu());
            stmt.setString(8, nv.getCaLamViec());
            int n = stmt.executeUpdate();
            if (n > 0) {
                dsnv.add(nv); // Update local list
                System.out.println("them: Added employee " + nv.getMaNV() + " to database and list");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("them: Failed to add employee " + nv.getMaNV());
        return false;
    }

    public boolean sua(NhanVien nv) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                 "UPDATE NhanVien SET hoTen=?, ngaySinh=?, sdt=?, diaChi=?, soCCCD=?, chucVu=?, caLamViec=? WHERE maNV=?")) {
            stmt.setString(1, nv.getHoTen());
            stmt.setDate(2, java.sql.Date.valueOf(nv.getNgaySinh()));
            stmt.setString(3, nv.getSdt());
            stmt.setString(4, nv.getDiaChi());
            stmt.setString(5, nv.getSoCCCD());
            stmt.setString(6, nv.getChucVu());
            stmt.setString(7, nv.getCaLamViec());
            stmt.setString(8, nv.getMaNV());
            int n = stmt.executeUpdate();
            if (n > 0) {
                // Update local list
                dsnv.removeIf(emp -> emp.getMaNV().equals(nv.getMaNV()));
                dsnv.add(nv);
                System.out.println("sua: Updated employee " + nv.getMaNV());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("sua: Failed to update employee " + nv.getMaNV());
        return false;
    }

    public boolean xoa(String manv) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement("DELETE FROM NhanVien WHERE maNV = ?")) {
            stmt.setString(1, manv);
            int n = stmt.executeUpdate();
            if (n > 0) {
                dsnv.removeIf(emp -> emp.getMaNV().equals(manv));
                System.out.println("xoa: Deleted employee " + manv);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("xoa: Failed to delete employee " + manv);
        return false;
    }

    public NhanVien timTheoMa(String manv) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM NhanVien WHERE maNV = ?")) {
            stmt.setString(1, manv);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    NhanVien nv = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("hoTen"),
                        rs.getDate("ngaySinh").toLocalDate(),
                        rs.getString("sdt"),
                        rs.getString("diaChi"),
                        rs.getString("soCCCD"),
                        rs.getString("chucVu"),
                        rs.getString("caLamViec")
                    );
                    System.out.println("timTheoMa: Found employee " + manv);
                    return nv;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("timTheoMa: Employee " + manv + " not found");
        return null;
    }

    public ArrayList<NhanVien> timTheoChucVu(String chucVu) {
        ArrayList<NhanVien> result = new ArrayList<>();
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM NhanVien WHERE chucVu = ?")) {
            stmt.setString(1, chucVu);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    NhanVien nv = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("hoTen"),
                        rs.getDate("ngaySinh").toLocalDate(),
                        rs.getString("sdt"),
                        rs.getString("diaChi"),
                        rs.getString("soCCCD"),
                        rs.getString("chucVu"),
                        rs.getString("caLamViec")
                    );
                    result.add(nv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("timTheoChucVu: Found " + result.size() + " employees with role " + chucVu);
        return result;
    }

    public NhanVien getNhanVienByMa(String maNV) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM NhanVien WHERE maNV = ?")) {
            stmt.setString(1, maNV);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    NhanVien nv = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("hoTen"),
                        rs.getDate("ngaySinh").toLocalDate(),
                        rs.getString("sdt"),
                        rs.getString("diaChi"),
                        rs.getString("soCCCD"),
                        rs.getString("chucVu"),
                        rs.getString("caLamViec")
                    );
                    System.out.println("getNhanVienByMa: Found employee " + maNV);
                    return nv;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("getNhanVienByMa: Employee " + maNV + " not found");
        return null;
    }

    public NhanVien timNhanVien(String maNV, String soCCCD, String sdt, String tenDangNhap) {
        String sql = "SELECT nv.* FROM NhanVien nv LEFT JOIN TaiKhoan tk ON nv.maNV = tk.maNV " +
                    "WHERE nv.maNV = ? OR nv.soCCCD = ? OR nv.sdt = ? OR tk.tenDangNhap = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maNV);
            stmt.setString(2, soCCCD);
            stmt.setString(3, sdt);
            stmt.setString(4, tenDangNhap);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    NhanVien nv = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("hoTen"),
                        rs.getDate("ngaySinh").toLocalDate(),
                        rs.getString("sdt"),
                        rs.getString("diaChi"),
                        rs.getString("soCCCD"),
                        rs.getString("chucVu"),
                        rs.getString("caLamViec")
                    );
                    System.out.println("timNhanVien: Found employee with criteria");
                    return nv;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("timNhanVien: No employee found with criteria");
        return null;
    }

    public boolean isMaNVExists(String maNV) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM NhanVien WHERE maNV = ?")) {
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boolean exists = rs.getInt(1) > 0;
                    System.out.println("isMaNVExists: maNV " + maNV + " exists: " + exists);
                    return exists;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("isMaNVExists: Error checking maNV " + maNV);
        return false;
    }
}