package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.DonDatPhong;
import entity.KhachHang;
import entity.NhanVien;

public class DonDatPhong_Dao {
	private KhachHang_Dao khachHang_Dao = new KhachHang_Dao();
	private NhanVien_Dao nhanVien_Dao = new NhanVien_Dao();
	ArrayList<DonDatPhong> danhSach;

	public DonDatPhong_Dao() {
		// TODO Auto-generated constructor stub
		danhSach = new ArrayList<>();
	}

	public DonDatPhong timDonTheoMa(String maDonDatPhong) {
		DonDatPhong don = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = ConnectDB.getConnection();
			String sql = "SELECT * FROM DonDatPhong WHERE maDonDatPhong = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, maDonDatPhong);
			rs = ps.executeQuery();

			if (rs.next()) {
				don = new DonDatPhong();

				don.setMaDonDatPhong(rs.getString("maDonDatPhong"));

				// Ngày nhận, trả -> convert sang LocalDateTime
				Timestamp tsDat = rs.getTimestamp("ngayDatPhong");
				Timestamp tsNhan = rs.getTimestamp("ngayNhanPhong");
				Timestamp tsTra = rs.getTimestamp("ngayTraPhong");
				if (tsDat != null)
					don.setNgayDatPhong(tsDat.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
				if (tsNhan != null)
					don.setNgayNhanPhong(tsNhan.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
				if (tsTra != null)
					don.setNgayTraPhong(tsTra.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

				don.setSoKhach(rs.getInt("soKhach"));
				don.setTienCoc(rs.getDouble("tienCoc"));
				don.setLoaiDon(rs.getString("loaiDon"));
				don.setTrangThai(rs.getString("trangThai"));

				// Gán thông tin khách hàng và nhân viên từ DAO
				String maKH = rs.getString("maKH");
				String maNV = rs.getString("maNV");

				KhachHang kh = khachHang_Dao.timTheoMa(maKH);
				NhanVien nv = nhanVien_Dao.timTheoMa(maNV);

				don.setKhachHang(kh);
				don.setNhanVien(nv);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return don;
	}

	public boolean xoaDonDatPhong(String maDonDatPhong) {
		Connection con = ConnectDB.getConnection();
		String sql = "DELETE FROM DonDatPhong WHERE maDonDatPhong = ?";
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, maDonDatPhong);
			int n = stmt.executeUpdate();
			return n > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<DonDatPhong> getDonDatPhongDaThanhToan() {
		ArrayList<DonDatPhong> danhSach = new ArrayList<>();

		String sql = "SELECT ddp.*, kh.maKH, kh.hoTen AS tenKH, kh.sdt AS sdtKH, kh.soCCCD, kh.email, "
				+ "nv.maNV, nv.hoTen AS tenNV, nv.ngaySinh, nv.sdt AS sdtNV, nv.diaChi, "
				+ "nv.soCCCD AS cccdNV, nv.chucVu, nv.caLamViec " + "FROM DonDatPhong ddp "
				+ "JOIN KhachHang kh ON ddp.maKH = kh.maKH " + "JOIN NhanVien nv ON ddp.maNV = nv.maNV "
				+ "WHERE ddp.trangThai = N'Đã thanh toán'";

		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				KhachHang kh = new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getString("sdtKH"),
						rs.getString("soCCCD"), rs.getString("email"));

				NhanVien nv = new NhanVien(rs.getString("maNV"), rs.getString("tenNV"),
						rs.getDate("ngaySinh").toLocalDate(), rs.getString("sdtNV"), rs.getString("diaChi"),
						rs.getString("cccdNV"), rs.getString("chucVu"), rs.getString("caLamViec"));

				DonDatPhong ddp = new DonDatPhong(rs.getString("maDonDatPhong"), kh,
						rs.getTimestamp("ngayDatPhong").toLocalDateTime(),
						rs.getTimestamp("ngayNhanPhong").toLocalDateTime(),
						rs.getTimestamp("ngayTraPhong").toLocalDateTime(), rs.getInt("soKhach"),
						rs.getDouble("tienCoc"), rs.getTimestamp("thoiGianCoc").toLocalDateTime(), nv,
						rs.getString("loaiDon"), rs.getString("trangThai"));

				danhSach.add(ddp);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return danhSach;
	}

	public ArrayList<DonDatPhong> getDonDatPhongDaThanhToanTheoTenVaSĐT(String ten, String sdt) {
		ArrayList<DonDatPhong> danhSach = new ArrayList<>();

		String sql = "SELECT ddp.*, kh.maKH, kh.hoTen AS tenKH, kh.sdt AS sdtKH, kh.soCCCD, kh.email, "
				+ "nv.maNV, nv.hoTen AS tenNV, nv.ngaySinh, nv.sdt AS sdtNV, nv.diaChi, "
				+ "nv.soCCCD AS cccdNV, nv.chucVu, nv.caLamViec " + "FROM DonDatPhong ddp "
				+ "JOIN KhachHang kh ON ddp.maKH = kh.maKH " + "JOIN NhanVien nv ON ddp.maNV = nv.maNV "
				+ "WHERE ddp.trangThai = N'Đã thanh toán' " + "AND (kh.hoTen LIKE ? OR kh.sdt LIKE ?)";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%" + ten + "%");
			stmt.setString(2, "%" + sdt + "%");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				KhachHang kh = new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getString("sdtKH"),
						rs.getString("soCCCD"), rs.getString("email"));

				NhanVien nv = new NhanVien(rs.getString("maNV"), rs.getString("tenNV"),
						rs.getDate("ngaySinh").toLocalDate(), rs.getString("sdtNV"), rs.getString("diaChi"),
						rs.getString("cccdNV"), rs.getString("chucVu"), rs.getString("caLamViec"));

				DonDatPhong ddp = new DonDatPhong(rs.getString("maDonDatPhong"), kh,
						rs.getTimestamp("ngayDatPhong").toLocalDateTime(),
						rs.getTimestamp("ngayNhanPhong").toLocalDateTime(),
						rs.getTimestamp("ngayTraPhong").toLocalDateTime(), rs.getInt("soKhach"),
						rs.getDouble("tienCoc"), rs.getTimestamp("thoiGianCoc").toLocalDateTime(), nv,
						rs.getString("loaiDon"), rs.getString("trangThai"));

				danhSach.add(ddp);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return danhSach;
	}

	public ArrayList<DonDatPhong> getDonDatPhongTheoMaPhong(String soPhong) {
		String sql = "SELECT ddp.*, kh.maKH, kh.hoTen AS tenKH, kh.sdt AS sdtKH, kh.soCCCD, "
				+ "nv.maNV, nv.hoTen AS tenNV, nv.ngaySinh, nv.sdt AS sdtNV, nv.diaChi, "
				+ "nv.soCCCD AS cccdNV, nv.chucVu, nv.caLamViec " + "FROM DonDatPhong ddp "
				+ "JOIN ChiTietDonDatPhong ct ON ddp.maDonDatPhong = ct.maDonDatPhong "
				+ "JOIN KhachHang kh ON ddp.maKH = kh.maKH " + "JOIN NhanVien nv ON ddp.maNV = nv.maNV "
				+ "WHERE ct.soPhong = ? AND ddp.trangThai = N'Nhận phòng'";

		Connection conn = ConnectDB.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, soPhong);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				KhachHang kh = new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getString("sdtKH"),
						rs.getString("soCCCD"), rs.getString("email"));
				NhanVien nv = new NhanVien(rs.getString("maNV"), rs.getString("tenNV"),
						rs.getDate("ngaySinh").toLocalDate(), rs.getString("sdtNV"), rs.getString("diaChi"),
						rs.getString("cccdNV"), rs.getString("chucVu"), rs.getString("caLamViec"));
				DonDatPhong ddp = new DonDatPhong(rs.getString("maDonDatPhong"), kh,
						rs.getTimestamp("ngayDatPhong").toLocalDateTime(),
						rs.getTimestamp("ngayNhanPhong").toLocalDateTime(),
						rs.getTimestamp("ngayTraPhong").toLocalDateTime(), rs.getInt("soKhach"),
						rs.getDouble("tienCoc"), rs.getTimestamp("thoiGianCoc").toLocalDateTime(), nv,
						rs.getString("loaiDon"), rs.getString("trangThai"));
				danhSach.add(ddp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return danhSach;
	}

	public DonDatPhong getDonDatPhongTheoMaP(String soPhong) {
		String sql = "SELECT ddp.*, kh.maKH, kh.hoTen AS tenKH, kh.sdt AS sdtKH, kh.soCCCD, kh.email, "
				+ "nv.maNV, nv.hoTen AS tenNV, nv.ngaySinh, nv.sdt AS sdtNV, nv.diaChi, "
				+ "nv.soCCCD AS cccdNV, nv.chucVu, nv.caLamViec " + "FROM DonDatPhong ddp "
				+ "JOIN ChiTietDonDatPhong ct ON ddp.maDonDatPhong = ct.maDonDatPhong "
				+ "JOIN KhachHang kh ON ddp.maKH = kh.maKH " + "JOIN NhanVien nv ON ddp.maNV = nv.maNV "
				+ "WHERE ct.soPhong = ? AND ddp.trangThai = N'Nhận phòng'";

		try (Connection conn = ConnectDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, soPhong);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				KhachHang kh = new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getString("sdtKH"),
						rs.getString("soCCCD"), rs.getString("email"));

				NhanVien nv = new NhanVien(rs.getString("maNV"), rs.getString("tenNV"),
						rs.getDate("ngaySinh").toLocalDate(), rs.getString("sdtNV"), rs.getString("diaChi"),
						rs.getString("cccdNV"), rs.getString("chucVu"), rs.getString("caLamViec"));

				return new DonDatPhong(rs.getString("maDonDatPhong"), kh,
						rs.getTimestamp("ngayDatPhong").toLocalDateTime(),
						rs.getTimestamp("ngayNhanPhong").toLocalDateTime(),
						rs.getTimestamp("ngayTraPhong").toLocalDateTime(), rs.getInt("soKhach"),
						rs.getDouble("tienCoc"), rs.getTimestamp("thoiGianCoc").toLocalDateTime(), nv,
						rs.getString("loaiDon"), rs.getString("trangThai"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null; // Nếu không tìm thấy
	}

	public ArrayList<DonDatPhong> getDonDatPhongTheoTenVaSDT(String tenKH, String sdt) {
		String sql = "SELECT ddp.*, kh.maKH, kh.hoTen AS tenKH, kh.sdt AS sdtKH, kh.soCCCD, kh.email,"
				+ "nv.maNV, nv.hoTen AS tenNV, nv.ngaySinh, nv.sdt AS sdtNV, nv.diaChi, "
				+ "nv.soCCCD AS cccdNV, nv.chucVu, nv.caLamViec " + "FROM DonDatPhong ddp "
				+ "JOIN KhachHang kh ON ddp.maKH = kh.maKH " + "JOIN NhanVien nv ON ddp.maNV = nv.maNV "
				+ "WHERE (kh.hoTen LIKE ? OR kh.sdt = ?) AND ddp.trangThai = N'Nhận phòng'";

		try {
			Connection connection = ConnectDB.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, "%" + tenKH + "%");
			stmt.setString(2, sdt);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					KhachHang kh = new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getString("sdtKH"),
							rs.getString("soCCCD"), rs.getString("email"));
					NhanVien nv = new NhanVien(rs.getString("maNV"), rs.getString("tenNV"),
							rs.getDate("ngaySinh").toLocalDate(), rs.getString("sdtNV"), rs.getString("diaChi"),
							rs.getString("cccdNV"), rs.getString("chucVu"), rs.getString("caLamViec"));
					DonDatPhong ddp = new DonDatPhong(rs.getString("maDonDatPhong"), kh,
							rs.getTimestamp("ngayDatPhong").toLocalDateTime(),
							rs.getTimestamp("ngayNhanPhong").toLocalDateTime(),
							rs.getTimestamp("ngayTraPhong").toLocalDateTime(), rs.getInt("soKhach"),
							rs.getDouble("tienCoc"), rs.getTimestamp("thoiGianCoc").toLocalDateTime(), nv,
							rs.getString("loaiDon"), rs.getString("trangThai"));
					danhSach.add(ddp);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return danhSach;
	}

	public DonDatPhong getDonDatPhongTheoMa(String maDon) {
		String sql = "SELECT ddp.*, kh.maKH, kh.hoTen AS tenKH, kh.sdt AS sdtKH, kh.soCCCD, kh.email, "
				+ "nv.maNV, nv.hoTen AS tenNV, nv.ngaySinh, nv.sdt AS sdtNV, nv.diaChi, "
				+ "nv.soCCCD AS cccdNV, nv.chucVu, nv.caLamViec " + "FROM DonDatPhong ddp "
				+ "JOIN KhachHang kh ON ddp.maKH = kh.maKH " + "JOIN NhanVien nv ON ddp.maNV = nv.maNV "
				+ "WHERE ddp.maDonDatPhong = ?";

		try {
			Connection connection = ConnectDB.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, maDon);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					KhachHang kh = new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getString("sdtKH"),
							rs.getString("soCCCD"), rs.getString("email"));
					NhanVien nv = new NhanVien(rs.getString("maNV"), rs.getString("tenNV"),
							rs.getDate("ngaySinh").toLocalDate(), rs.getString("sdtNV"), rs.getString("diaChi"),
							rs.getString("cccdNV"), rs.getString("chucVu"), rs.getString("caLamViec"));
					return new DonDatPhong(rs.getString("maDonDatPhong"), kh,
							rs.getTimestamp("ngayDatPhong").toLocalDateTime(),
							rs.getTimestamp("ngayNhanPhong").toLocalDateTime(),
							rs.getTimestamp("ngayTraPhong").toLocalDateTime(), rs.getInt("soKhach"),
							rs.getDouble("tienCoc"), rs.getTimestamp("thoiGianCoc").toLocalDateTime(), nv,
							rs.getString("loaiDon"), rs.getString("trangThai"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean setTrangThaiDonDatPhong(String maDonDatPhong, String trangThai) {
		String sql = "UPDATE DonDatPhong SET trangThai = ? WHERE maDonDatPhong = ?";

		try (Connection conn = ConnectDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, trangThai);
			stmt.setString(2, maDonDatPhong);

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setTienCocVeKhong(String maDonDatPhong) {
		String sql = "UPDATE DonDatPhong SET tienCoc = 0 WHERE maDonDatPhong = ?";

		try (Connection conn = ConnectDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, maDonDatPhong);

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean coTheCapNhatTrangThai(String maDonDatPhong) {
		String sql = """
				    SELECT COUNT(*) FROM ChiTietDonDatPhong ctd
				    JOIN Phong p ON ctd.soPhong = p.soPhong
				    WHERE ctd.maDonDatPhong = ? AND p.trangThai <> N'Trống'
				""";

		try (Connection con = ConnectDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maDonDatPhong);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				return count == 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public ArrayList<DonDatPhong> getAllDonDatPhong() {
	    ArrayList<DonDatPhong> danhSach = new ArrayList<>();

	    String sql = "SELECT dp.maDonDatPhong, dp.ngayDatPhong, dp.ngayNhanPhong, dp.ngayTraPhong, " +
	                 "dp.soKhach, dp.tienCoc, dp.thoiGianCoc, dp.loaiDon, dp.trangThai, " +
	                 "kh.maKH, kh.hoTen, kh.sdt, kh.soCCCD, kh.email " +
	                 "FROM DonDatPhong dp " +
	                 "JOIN KhachHang kh ON dp.maKH = kh.maKH";

	    try (Connection conn = ConnectDB.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            // Tạo khách hàng
	            KhachHang kh = new KhachHang(
	                rs.getString("maKH"),
	                rs.getString("hoTen"),
	                rs.getString("sdt"),
	                rs.getString("soCCCD"),
	                rs.getString("email")
	            );

	            // Tạo đơn đặt phòng
	            DonDatPhong ddp = new DonDatPhong();
	            ddp.setMaDonDatPhong(rs.getString("maDonDatPhong"));
	            ddp.setNgayDatPhong(rs.getTimestamp("ngayDatPhong").toLocalDateTime());
	            ddp.setNgayNhanPhong(rs.getTimestamp("ngayNhanPhong").toLocalDateTime());
	            ddp.setNgayTraPhong(rs.getTimestamp("ngayTraPhong").toLocalDateTime());
	            ddp.setSoKhach(rs.getInt("soKhach"));
	            ddp.setTienCoc(rs.getDouble("tienCoc"));
	            ddp.setThoiGianCoc(rs.getTimestamp("thoiGianCoc").toLocalDateTime());
	            ddp.setLoaiDon(rs.getString("loaiDon"));
	            ddp.setTrangThai(rs.getString("trangThai"));
	            ddp.setKhachHang(kh);

	            danhSach.add(ddp);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return danhSach;
	}

	public ArrayList<DonDatPhong> getDonDatPhongTheoSDT(String sdt) {
	    ArrayList<DonDatPhong> danhSach = new ArrayList<>();

	    String sql = "SELECT dp.maDonDatPhong, dp.ngayDatPhong, dp.ngayNhanPhong, dp.ngayTraPhong, " +
	                 "dp.soKhach, dp.tienCoc, dp.thoiGianCoc, dp.loaiDon, dp.trangThai, " +
	                 "kh.maKH, kh.hoTen, kh.sdt, kh.soCCCD, kh.email " +
	                 "FROM DonDatPhong dp " +
	                 "JOIN KhachHang kh ON dp.maKH = kh.maKH " +
	                 "WHERE kh.sdt = ?";

	    try (Connection conn = ConnectDB.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, sdt);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            // Tạo khách hàng
	            KhachHang kh = new KhachHang(
	                rs.getString("maKH"),
	                rs.getString("hoTen"),
	                rs.getString("sdt"),
	                rs.getString("soCCCD"),
	                rs.getString("email")
	            );

	            // Tạo đơn đặt phòng
	            DonDatPhong ddp = new DonDatPhong();
	            ddp.setMaDonDatPhong(rs.getString("maDonDatPhong"));
	            ddp.setNgayDatPhong(rs.getTimestamp("ngayDatPhong").toLocalDateTime());
	            ddp.setNgayNhanPhong(rs.getTimestamp("ngayNhanPhong").toLocalDateTime());
	            ddp.setNgayTraPhong(rs.getTimestamp("ngayTraPhong").toLocalDateTime());
	            ddp.setSoKhach(rs.getInt("soKhach"));
	            ddp.setTienCoc(rs.getDouble("tienCoc"));
	            ddp.setThoiGianCoc(rs.getTimestamp("thoiGianCoc").toLocalDateTime());
	            ddp.setLoaiDon(rs.getString("loaiDon"));
	            ddp.setTrangThai(rs.getString("trangThai"));
	            ddp.setKhachHang(kh);

	            danhSach.add(ddp);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return danhSach;
	}
	
	public LocalDateTime layNgayNhanPhong(String maDonDatPhong) {
	    String sql = """
	        SELECT ngayNhanPhong FROM DonDatPhong WHERE maDonDatPhong = ?
	    """;

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, maDonDatPhong);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            Timestamp ts = rs.getTimestamp("ngayNhanPhong");
	            if (ts != null) {
	                return ts.toLocalDateTime(); 
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null; 
	}

	public boolean capNhatThoiGianCoc(String maDonDatPhong, LocalDateTime thoiGianCocMoi) {
	    String sql = """
	        UPDATE DonDatPhong
	        SET thoiGianCoc = ?
	        WHERE maDonDatPhong = ?
	    """;

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setTimestamp(1, Timestamp.valueOf(thoiGianCocMoi));
	        ps.setString(2, maDonDatPhong);

	        int rowsAffected = ps.executeUpdate();
	        return rowsAffected > 0;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public LocalDateTime layThoiGianCoc(String maDonDatPhong) {
	    String sql = """
	        SELECT thoiGianCoc FROM DonDatPhong WHERE maDonDatPhong = ?
	    """;

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, maDonDatPhong);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            Timestamp ts = rs.getTimestamp("thoiGianCoc");
	            if (ts != null) {
	                return ts.toLocalDateTime(); 
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null; 
	}

}
