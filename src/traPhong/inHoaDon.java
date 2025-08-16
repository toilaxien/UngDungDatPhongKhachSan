package traPhong;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JLabel;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.io.File;


public class inHoaDon {
    private static BaseFont fontChinh;
    
    static {
        try {
            fontChinh = BaseFont.createFont("C:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Định nghĩa các kiểu font với kích thước tối ưu
    private static Font fontTieuDe = new Font(fontChinh, 16, Font.BOLD, new Color(30, 57, 91));
    private static Font fontTieuDeMuc = new Font(fontChinh, 10, Font.BOLD, new Color(30, 57, 91));
    private static Font fontNhan = new Font(fontChinh, 9, Font.BOLD, new Color(70, 70, 70));
    private static Font fontGiaTri = new Font(fontChinh, 9, Font.NORMAL, Color.BLACK);
    private static Font fontTieuDeBang = new Font(fontChinh, 9, Font.BOLD, Color.WHITE);
    private static Font fontNoiDungBang = new Font(fontChinh, 8, Font.NORMAL, Color.BLACK);
    private static Font fontTongCong = new Font(fontChinh, 10, Font.BOLD, new Color(30, 57, 91));
    private static Font fontGiaTriTong = new Font(fontChinh, 10, Font.BOLD, new Color(200, 0, 0));
    private static Font fontChanTrang = new Font(fontChinh, 9, Font.ITALIC, new Color(100, 100, 100));
    private static Font fontGhiChu = new Font(fontChinh, 8, Font.ITALIC, new Color(120, 120, 120));


    public static void taoHoaDon(
            String maDonDatPhong, String ngayLap,
            String tenKhachHang, String soDienThoai,
            String ngayNhanPhong, String ngayTraPhong,
            ArrayList<Object[]> danhSachPhongDuocChon,
            double tongTienPhong,
            double tongTienDichVu,
            double tongChiPhiPhatSinh,
            double tienCoc,
            String maGiamGia,
            double thanhTien,
            String duongDanLuuFile) throws Exception { // Thay đổi thành JLabel
        
        // Khởi tạo tài liệu A4 thẳng đứng với lề nhỏ
        Document taiLieu = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter nguoiViet = PdfWriter.getInstance(taiLieu, new FileOutputStream(duongDanLuuFile));
        taiLieu.open();
        
        // Tạo bảng chính chiếm 95% chiều rộng
        PdfPTable bangChinh = new PdfPTable(1);
        bangChinh.setWidthPercentage(95);
        
        // --- PHẦN TIÊU ĐỀ VỚI LOGO ---
        PdfPTable bangTieuDe = new PdfPTable(2);
        bangTieuDe.setWidthPercentage(100);
        bangTieuDe.setWidths(new float[]{1.5f, 3});
        
        try {
            // Thêm logo vào hóa đơn
            Image logo = Image.getInstance("img/HinhAnhGiaoDienChinh/logohoadon.png");
            logo.scaleToFit(70, 70);
            PdfPCell oLogo = new PdfPCell(logo);
            oLogo.setBorder(Rectangle.NO_BORDER);
            oLogo.setPadding(5f);
            oLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
            bangTieuDe.addCell(oLogo);
        } catch (Exception e) {
            PdfPCell oLogoTrong = new PdfPCell(new Phrase("LOGO", fontTieuDe));
            oLogoTrong.setBorder(Rectangle.NO_BORDER);
            oLogoTrong.setFixedHeight(70f);
            bangTieuDe.addCell(oLogoTrong);
        }
        
        // Ô tiêu đề
        PdfPCell oTieuDe = new PdfPCell(new Phrase("HÓA ĐƠN THANH TOÁN\nKHÁCH SẠN LUX", fontTieuDe));
        oTieuDe.setBorder(Rectangle.NO_BORDER);
        oTieuDe.setHorizontalAlignment(Element.ALIGN_CENTER);
        oTieuDe.setVerticalAlignment(Element.ALIGN_MIDDLE);
        oTieuDe.setPaddingTop(15f);
        bangTieuDe.addCell(oTieuDe);
        
        bangChinh.addCell(bangTieuDe);
        
        // --- THÔNG TIN HÓA ĐƠN ---
        PdfPTable bangThongTin = new PdfPTable(1);
        bangThongTin.setWidthPercentage(100);
        
        Paragraph doanThongTin = new Paragraph();
        doanThongTin.add(new Chunk("Mã đơn: " + maDonDatPhong + "   ", fontNhan));
        doanThongTin.add(new Chunk("Ngày lập: " + ngayLap, fontNhan));
        doanThongTin.setAlignment(Element.ALIGN_RIGHT);
        
        PdfPCell oThongTin = new PdfPCell(doanThongTin);
        oThongTin.setBorder(Rectangle.NO_BORDER);
        bangThongTin.addCell(oThongTin);
        bangChinh.addCell(bangThongTin);
        
        // --- THÔNG TIN KHÁCH HÀNG ---
        PdfPTable bangKhachHang = new PdfPTable(2);
        bangKhachHang.setWidthPercentage(100);
        bangKhachHang.setWidths(new float[]{1, 3});
        
        themDongThongTin(bangKhachHang, "Khách hàng:", tenKhachHang);
        themDongThongTin(bangKhachHang, "Điện thoại:", soDienThoai);
        themDongThongTin(bangKhachHang, "Nhận phòng:", ngayNhanPhong);
        themDongThongTin(bangKhachHang, "Trả phòng:", ngayTraPhong);
        
        bangChinh.addCell(bangKhachHang);
        
        // --- TIỀN PHÒNG ---
        PdfPCell oTieuDePhong = new PdfPCell(new Phrase("I. TIỀN PHÒNG", fontTieuDeMuc));
        oTieuDePhong.setBorder(Rectangle.NO_BORDER);
        oTieuDePhong.setPadding(5f);
        bangChinh.addCell(oTieuDePhong);
        
        // Bảng thông tin phòng
        PdfPTable bangPhong = new PdfPTable(5);
        bangPhong.setWidthPercentage(100);
        bangPhong.setWidths(new float[]{1, 1.5f, 1, 1, 1.5f});
        
        // Tiêu đề bảng
        themTieuDeBang(bangPhong, "Mã phòng");
        themTieuDeBang(bangPhong, "Loại phòng");
        themTieuDeBang(bangPhong, "Thời gian");
        themTieuDeBang(bangPhong, "Đơn giá");
        themTieuDeBang(bangPhong, "Thành tiền");
        
        NumberFormat dinhDangTien = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        // Điền dữ liệu phòng từ List
        if (danhSachPhongDuocChon != null && !danhSachPhongDuocChon.isEmpty()) {
            for (Object[] phong : danhSachPhongDuocChon) {
                if (phong != null && phong.length >= 5) {
                    themDongPhong(bangPhong, 
                        phong[0].toString(), 
                        phong[1].toString(), 
                        phong[2].toString(), 
                        Double.parseDouble(phong[3].toString()), 
                        Double.parseDouble(phong[4].toString()), 
                        dinhDangTien);
                }
            }
        }
        
        bangChinh.addCell(bangPhong);
        
        // Ghi chú thuế
        PdfPCell oGhiChuThue = new PdfPCell(new Phrase("(Đã bao gồm thuế GTGT)", fontGhiChu));
        oGhiChuThue.setBorder(Rectangle.NO_BORDER);
        oGhiChuThue.setPaddingBottom(5f);
        bangChinh.addCell(oGhiChuThue);
        
        // --- CÁC KHOẢN THANH TOÁN ---
        themDongThanhToan(bangChinh, "Tổng tiền phòng:", dinhDangTien.format(tongTienPhong));
        themDongThanhToan(bangChinh, "Tiền dịch vụ:", dinhDangTien.format(tongTienDichVu));
        themDongThanhToan(bangChinh, "Chi phí phát sinh:", dinhDangTien.format(tongChiPhiPhatSinh));
        themDongThanhToan(bangChinh, "Tiền cọc:", dinhDangTien.format(tienCoc));
        themDongThanhToan(bangChinh, "Mã giảm giá:", maGiamGia);
        
        // Thành tiền
        PdfPTable bangTongCong = new PdfPTable(2);
        bangTongCong.setWidthPercentage(100);
        bangTongCong.setWidths(new float[]{1, 1});
        
        PdfPCell oNhanTong = new PdfPCell(new Phrase("THÀNH TIỀN:", fontTongCong));
        oNhanTong.setBorder(Rectangle.NO_BORDER);
        oNhanTong.setPadding(5f);
        bangTongCong.addCell(oNhanTong);
        
        PdfPCell oGiaTriTong = new PdfPCell(new Phrase(dinhDangTien.format(thanhTien), fontGiaTriTong));
        oGiaTriTong.setBorder(Rectangle.NO_BORDER);
        oGiaTriTong.setHorizontalAlignment(Element.ALIGN_RIGHT);
        oGiaTriTong.setPadding(5f);
        bangTongCong.addCell(oGiaTriTong);
        
        bangChinh.addCell(bangTongCong);
        
        
        // --- FOOTER ---
        Paragraph doanChanTrang = new Paragraph();
        doanChanTrang.add(new Chunk("KHÁCH SẠN LUX\n", fontTieuDeMuc));
        doanChanTrang.add(new Chunk("45 Tỉnh lộ 10, Bình Chánh, TP.HCM\n", fontChanTrang));
        doanChanTrang.add(new Chunk("ĐT: 028.1234.5678 - Email: info@luxhotel.com", fontGhiChu));
        doanChanTrang.setAlignment(Element.ALIGN_CENTER);
        
        PdfPCell oChanTrang = new PdfPCell(doanChanTrang);
        oChanTrang.setBorder(Rectangle.NO_BORDER);
        oChanTrang.setPaddingTop(10f);
        bangChinh.addCell(oChanTrang);
        
        taiLieu.add(bangChinh);
        taiLieu.close();
        
      if (Desktop.isDesktopSupported()) {
      try {
          File file = new File(duongDanLuuFile);
          Desktop.getDesktop().browse(file.toURI()); // Gọi lệnh in
      } catch (IOException ex) {
          ex.printStackTrace();
      }
  }
    }
    
    // Các phương thức hỗ trợ
    private static void themDongThongTin(PdfPTable bang, String nhan, String giaTri) {
        PdfPCell oNhan = new PdfPCell(new Phrase(nhan, fontNhan));
        oNhan.setBorder(Rectangle.NO_BORDER);
        oNhan.setPadding(2f);
        bang.addCell(oNhan);
        
        PdfPCell oGiaTri = new PdfPCell(new Phrase(giaTri, fontGiaTri));
        oGiaTri.setBorder(Rectangle.NO_BORDER);
        oGiaTri.setPadding(2f);
        bang.addCell(oGiaTri);
    }
    
    private static void themTieuDeBang(PdfPTable bang, String tieuDe) {
        PdfPCell oTieuDe = new PdfPCell(new Phrase(tieuDe, fontTieuDeBang));
        oTieuDe.setBackgroundColor(new Color(30, 57, 91));
        oTieuDe.setPadding(4f);
        oTieuDe.setHorizontalAlignment(Element.ALIGN_CENTER);
        bang.addCell(oTieuDe);
    }
    
    private static void themDongPhong(PdfPTable bang, String maPhong, String loaiPhong, 
                                    String thoiGian, double donGia, double thanhTien,
                                    NumberFormat dinhDangTien) {
        PdfPCell oMa = new PdfPCell(new Phrase(maPhong, fontNoiDungBang));
        PdfPCell oLoai = new PdfPCell(new Phrase(loaiPhong, fontNoiDungBang));
        PdfPCell oThoiGian = new PdfPCell(new Phrase(thoiGian, fontNoiDungBang));
        PdfPCell oDonGia = new PdfPCell(new Phrase(dinhDangTien.format(donGia), fontNoiDungBang));
        PdfPCell oThanhTien = new PdfPCell(new Phrase(dinhDangTien.format(thanhTien), fontNoiDungBang));
        
        // Căn chỉnh
        oDonGia.setHorizontalAlignment(Element.ALIGN_RIGHT);
        oThanhTien.setHorizontalAlignment(Element.ALIGN_RIGHT);
        
        // Thêm vào bảng
        bang.addCell(oMa);
        bang.addCell(oLoai);
        bang.addCell(oThoiGian);
        bang.addCell(oDonGia);
        bang.addCell(oThanhTien);
    }
    
    private static void themDongThanhToan(PdfPTable bang, String nhan, String giaTri) {
        PdfPTable bangNho = new PdfPTable(2);
        bangNho.setWidthPercentage(100);
        bangNho.setWidths(new float[]{2, 1});
        
        PdfPCell oNhan = new PdfPCell(new Phrase(nhan, fontTongCong));
        oNhan.setBorder(Rectangle.NO_BORDER);
        oNhan.setPadding(2f);
        bangNho.addCell(oNhan);
        
        PdfPCell oGiaTri = new PdfPCell(new Phrase(giaTri, fontGiaTri));
        oGiaTri.setBorder(Rectangle.NO_BORDER);
        oGiaTri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        oGiaTri.setPadding(2f);
        bangNho.addCell(oGiaTri);
        
        PdfPCell oChua = new PdfPCell(bangNho);
        oChua.setBorder(Rectangle.NO_BORDER);
        bang.addCell(oChua);
    }
    

//    public static void main(String[] args) {
//        try {
//            // Tạo dữ liệu mẫu
//            List<Object[]> danhSachPhong = new ArrayList<>();
//            danhSachPhong.add(new Object[]{"P101", "Deluxe", "2 ngày", 1500000, 3000000});
//            danhSachPhong.add(new Object[]{"P102", "Standard", "1 ngày", 1000000, 1000000});
//            danhSachPhong.add(new Object[]{"P102", "Standard", "1 ngày", 1000000, 1000000});
//            danhSachPhong.add(new Object[]{"P102", "Standard", "1 ngày", 1000000, 1000000});
//            danhSachPhong.add(new Object[]{"P102", "Standard", "1 ngày", 1000000, 1000000});
//            danhSachPhong.add(new Object[]{"P102", "Standard", "1 ngày", 1000000, 1000000});
//            danhSachPhong.add(new Object[]{"P102", "Standard", "1 ngày", 1000000, 1000000});
//            JLabel qrCodeLabel = new JLabel();
//            // Gọi hàm tạo hóa đơn
//            
//            
//            
//            taoHoaDon(
//                "HD20230001", 
//                "15/06/2023", 
//                "Nguyễn Văn A", 
//                "0912345678", 
//                "10/06/2023", 
//                "12/06/2023", 
//                danhSachPhong, 
//                4000000, 
//                500000, 
//                200000, 
//                1000000, 
//                "SUMMER10", 
//                3700000, 
//                "C:\\Users\\TOILAXIEN\\OneDrive\\Máy tính\\HoaDon.pdf"
//            );
//            
//            System.out.println("Đã tạo hóa đơn thành công!");
//        } catch (Exception e) {
//            System.err.println("Lỗi khi tạo hóa đơn:");
//            e.printStackTrace();
//        }
//    }
    
    
}