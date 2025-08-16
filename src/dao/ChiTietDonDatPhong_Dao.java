package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.ChiTietDonDatPhong;
import entity.DonDatPhong;
import entity.KhachHang;
import entity.Phong;

public class ChiTietDonDatPhong_Dao {
	private ArrayList<ChiTietDonDatPhong> dsctddp;
	private Connection connection;

	public ChiTietDonDatPhong_Dao() {
		dsctddp = new ArrayList<ChiTietDonDatPhong>();
		connection = ConnectDB.getConnection();
	}

	// Thêm phương thức truy vấn ví dụ:
	public ArrayList<ChiTietDonDatPhong> getAllChiTietDonDatPhong() {
		try {
			String sql = "SELECT * FROM ChiTietDonDatPhong";
			PreparedStatement stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				// Lấy dữ liệu từ ResultSet để tạo đối tượng ChiTietDonDatPhong
				// Giả sử các cột: maDDP, maPhong, ngayNhan, ngayTra, ghiChu
				ChiTietDonDatPhong ct = new ChiTietDonDatPhong(new DonDatPhong(rs.getString("maDonDatPhong")),
						new Phong(rs.getString("soPhong")), rs.getInt("soLuong"));
				dsctddp.add(ct);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dsctddp;
	}

	public ArrayList<ChiTietDonDatPhong> getChiTietDonDatPhongTheoMaDon(String maDonDatPhong) {
	    ArrayList<ChiTietDonDatPhong> dsctddp = new ArrayList<>();  
	    try {
	        String sql = "SELECT * FROM ChiTietDonDatPhong WHERE maDonDatPhong = ?";
	        PreparedStatement stmt = connection.prepareStatement(sql);
	        stmt.setString(1, maDonDatPhong);  
	        ResultSet rs = stmt.executeQuery();  
	        while (rs.next()) {
	            ChiTietDonDatPhong ct = new ChiTietDonDatPhong(
	                new DonDatPhong(rs.getString("maDonDatPhong")),  
	                new Phong(rs.getString("soPhong")),  
	                rs.getInt("soLuong")  
	            );
	            dsctddp.add(ct);  
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();  
	    }
	    return dsctddp; 
	}


	public int countSoPhongTrong(Timestamp tuNgay, Timestamp denNgay, String loaiPhong, String moTa) throws SQLException {
	    int soLuong = 0;

	    // Chọn SQL phù hợp tùy theo moTa
	    String sql;
	    if (moTa == null || moTa.trim().isEmpty()) {
	        sql = """
	            SELECT COUNT(*) AS SoPhongTrong
	            FROM Phong
	            WHERE loaiPhong = ?
	              AND soPhong NOT IN (
	                  SELECT CT.soPhong
	                  FROM ChiTietDonDatPhong CT
	                  JOIN DonDatPhong DDP ON CT.maDonDatPhong = DDP.maDonDatPhong
	                  WHERE
	                      DDP.ngayNhanPhong < ?
	                      AND DDP.ngayTraPhong > ?
	              )
	        """;
	    } else {
	        sql = """
	            SELECT COUNT(*) AS SoPhongTrong
	            FROM Phong
	            WHERE loaiPhong = ?
	              AND moTa = ?
	              AND soPhong NOT IN (
	                  SELECT CT.soPhong
	                  FROM ChiTietDonDatPhong CT
	                  JOIN DonDatPhong DDP ON CT.maDonDatPhong = DDP.maDonDatPhong
	                  WHERE
	                      DDP.ngayNhanPhong < ?
	                      AND DDP.ngayTraPhong > ?
	              )
	        """;
	    }

	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setString(1, loaiPhong);

	        if (moTa == null || moTa.trim().isEmpty()) {
	            stmt.setTimestamp(2, denNgay);
	            stmt.setTimestamp(3, tuNgay);
	        } else {
	            stmt.setString(2, moTa);
	            stmt.setTimestamp(3, denNgay);
	            stmt.setTimestamp(4, tuNgay);
	        }

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                soLuong = rs.getInt("SoPhongTrong");
	            }
	        }
	    }

	    return soLuong;
	}



	public int getGiaTheoKieu(String tenLoaiPhong, String kieuDat) {
	    int gia = 0;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        String sql = "SELECT giaTheoGio, giaTheoNgay, giaTheoDem FROM LoaiPhong WHERE tenLoai = ?";
	        stmt = connection.prepareStatement(sql);
	        stmt.setNString(1, tenLoaiPhong);
	        rs = stmt.executeQuery();

	        if (rs.next()) {
	            switch (kieuDat.toLowerCase()) {
	                case "theo giờ":
	                    gia = rs.getInt("giaTheoGio");
	                    break;
	                case "theo ngày":
	                    gia = rs.getInt("giaTheoNgay");
	                    break;
	                case "theo đêm":
	                    gia = rs.getInt("giaTheoDem");
	                    break;
	                default:
	                    System.err.println("Kiểu đặt phòng không hợp lệ: " + kieuDat);
	                    break;
	            }
	        }
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

	    return gia;
	}


	public List<String> layDanhSachPhongTrong(Timestamp tuNgay, Timestamp denNgay, String loaiPhong, String moTa)
	        throws SQLException {
	    List<String> danhSachPhong = new ArrayList<>();

	    // SQL tùy theo có moTa hay không
	    String sql;
	    if (moTa == null || moTa.trim().isEmpty()) {
	        sql = """
	                SELECT soPhong
	                FROM Phong
	                WHERE loaiPhong = ?
	                  AND soPhong NOT IN (
	                      SELECT CT.soPhong
	                      FROM ChiTietDonDatPhong CT
	                      JOIN DonDatPhong DDP ON CT.maDonDatPhong = DDP.maDonDatPhong
	                      WHERE
	                          DDP.ngayNhanPhong < ?
	                          AND DDP.ngayTraPhong > ?
	                  )
	              """;
	    } else {
	        sql = """
	                SELECT soPhong
	                FROM Phong
	                WHERE loaiPhong = ?
	                  AND moTa = ?
	                  AND soPhong NOT IN (
	                      SELECT CT.soPhong
	                      FROM ChiTietDonDatPhong CT
	                      JOIN DonDatPhong DDP ON CT.maDonDatPhong = DDP.maDonDatPhong
	                      WHERE
	                          DDP.ngayNhanPhong < ?
	                          AND DDP.ngayTraPhong > ?
	                  )
	              """;
	    }

	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setString(1, loaiPhong);

	        if (moTa == null || moTa.trim().isEmpty()) {
	            stmt.setTimestamp(2, denNgay);
	            stmt.setTimestamp(3, tuNgay);
	        } else {
	            stmt.setString(2, moTa);
	            stmt.setTimestamp(3, denNgay);
	            stmt.setTimestamp(4, tuNgay);
	        }

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (!rs.isBeforeFirst()) {
	                System.out.println("Không có phòng trống nào.");
	            }

	            while (rs.next()) {
	                String soPhong = rs.getString("soPhong");
	                danhSachPhong.add(soPhong);

	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Lỗi SQL: " + e.getMessage());
	        throw e;
	    }
        System.out.println("Phòng trống: " + danhSachPhong);
	    return danhSachPhong;
	}


	public int demSoLuongDonTrongNgay() {
		int soLuongDon = 0;
		String sql = "SELECT COUNT(*) AS SoLuongDon FROM DonDatPhong WHERE CONVERT(DATE, ngayDatPhong) = CONVERT(DATE, GETDATE())";
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				soLuongDon = rs.getInt("SoLuongDon");
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
		return soLuongDon;
	}

	public KhachHang timKhachHangTheoSDT(String sdt) {
		KhachHang khachHang = null;
		String sql = "SELECT maKH, hoTen FROM KhachHang WHERE sdt = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, sdt);
			rs = stmt.executeQuery();

			if (rs.next()) {
				String maKH = rs.getString("maKH");
				String hoTen = rs.getNString("hoTen");
				khachHang = new KhachHang(maKH, hoTen);
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

		return khachHang;
	}

	public boolean themDonDatPhong(String maDonDatPhong, String maKH, Timestamp ngayDatPhong, Timestamp ngayNhan,
			Timestamp ngayTra, int soKhach, double tienCoc, Timestamp thoiGianCoc, String maNV, String loaiDon,
			String trangThai) {
		boolean result = false;
		PreparedStatement stmt = null;

		try {
			String sql = "INSERT INTO DonDatPhong (maDonDatPhong, maKH, ngayDatPhong, ngayNhanPhong, ngayTraPhong, "
					+ "soKhach, tienCoc, thoiGianCoc, maNV, loaiDon, trangThai) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			stmt = connection.prepareStatement(sql);
			stmt.setString(1, maDonDatPhong);
			stmt.setString(2, maKH);
			stmt.setTimestamp(3, ngayDatPhong);
			stmt.setTimestamp(4, ngayNhan);
			stmt.setTimestamp(5, ngayTra);
			stmt.setInt(6, soKhach);
			stmt.setDouble(7, tienCoc);
			stmt.setTimestamp(8, thoiGianCoc);
			stmt.setString(9, maNV);
			stmt.setNString(10, loaiDon);
			stmt.setNString(11, trangThai);

			int rowsInserted = stmt.executeUpdate();
			result = rowsInserted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}


	public boolean themChiTietDonDatPhong(String maDonDatPhong, String soPhong) {
		boolean result = false;
		PreparedStatement stmt = null;

		try {
			String sql = "INSERT INTO ChiTietDonDatPhong (maDonDatPhong, soPhong, soLuong) VALUES (?, ?, ?)";
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, maDonDatPhong);
			stmt.setString(2, soPhong);
			stmt.setInt(3, 1); // soLuong luôn là 1

			int rowsInserted = stmt.executeUpdate();
			if (rowsInserted > 0) {
				result = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	public ArrayList<String> getDanhSachSoPhongTheoDon(String maDonDatPhong) {
	    ArrayList<String> danhSach = new ArrayList<>();
	    String sql = "SELECT soPhong FROM ChiTietDonDatPhong WHERE maDonDatPhong = ?";

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, maDonDatPhong);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            danhSach.add(rs.getString("soPhong"));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return danhSach;
	}


}
