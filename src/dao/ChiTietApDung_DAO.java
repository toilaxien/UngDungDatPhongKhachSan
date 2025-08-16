package dao;

import java.sql.*;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietApDung;
import entity.DonDatPhong;
import entity.KhuyenMai;

public class ChiTietApDung_DAO {
	public boolean addChiTietApDung(ChiTietApDung cta) {
		String sqlCheck = "SELECT tongThanhToanSauApDung FROM ChiTietApDung WHERE maDonDatPhong = ? AND maKhuyenMai = ?";
		String sqlInsert = "INSERT INTO ChiTietApDung (maDonDatPhong, maKhuyenMai, tongThanhToanSauApDung) VALUES (?, ?, ?)";
		String sqlUpdate = "UPDATE ChiTietApDung SET tongThanhToanSauApDung = ? WHERE maDonDatPhong = ? AND maKhuyenMai = ?";

		try (Connection con = ConnectDB.getConnection()) {

			// 1. Kiểm tra xem bản ghi đã tồn tại
			try (PreparedStatement checkStmt = con.prepareStatement(sqlCheck)) {
				checkStmt.setString(1, cta.getMaDonDatPhong());
				checkStmt.setString(2, cta.getMaKhuyenMai());

				try (ResultSet rs = checkStmt.executeQuery()) {
					if (rs.next()) {
						// Đã tồn tại → cộng dồn
						double tongCu = rs.getDouble("tongThanhToanSauApDung");
						double tongMoi = tongCu + cta.getTongThanhToanSauApDung();

						try (PreparedStatement updateStmt = con.prepareStatement(sqlUpdate)) {
							updateStmt.setDouble(1, tongMoi);
							updateStmt.setString(2, cta.getMaDonDatPhong());
							updateStmt.setString(3, cta.getMaKhuyenMai());
							return updateStmt.executeUpdate() > 0;
						}

					} else {
						// Chưa có → thêm mới
						try (PreparedStatement insertStmt = con.prepareStatement(sqlInsert)) {
							insertStmt.setString(1, cta.getMaDonDatPhong());
							insertStmt.setString(2, cta.getMaKhuyenMai());
							insertStmt.setDouble(3, cta.getTongThanhToanSauApDung());
							return insertStmt.executeUpdate() > 0;
						}
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// Xóa một chi tiết áp dụng
	public boolean deleteChiTietApDung(String maDonDatPhong, String maKhuyenMai) {
		String sql = "DELETE FROM ChiTietApDung WHERE maDonDatPhong = ? AND maKhuyenMai = ?";

		try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

			stmt.setString(1, maDonDatPhong);
			stmt.setString(2, maKhuyenMai);

			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public ArrayList<ChiTietApDung> getDanhSachChiTietApDungTheoMaDon(String maDonDatPhong) {
		ArrayList<ChiTietApDung> danhSach = new ArrayList<>();
		String sql = "SELECT * FROM ChiTietApDung WHERE maDonDatPhong = ?";

		try (Connection conn = ConnectDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, maDonDatPhong);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String maKhuyenMai = rs.getString("maKhuyenMai");
					double tongThanhToanSauApDung = rs.getDouble("tongThanhToanSauApDung");

					ChiTietApDung chiTiet = new ChiTietApDung(maDonDatPhong, maKhuyenMai, tongThanhToanSauApDung);
					danhSach.add(chiTiet);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return danhSach;
	}
}
