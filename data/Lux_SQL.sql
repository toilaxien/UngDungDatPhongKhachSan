CREATE DATABASE QuanLyKhachSan;
go
USE QuanLyKhachSan;
go
CREATE TABLE LoaiPhong (
    maLoaiPhong VARCHAR(20) PRIMARY KEY,  -- Mã loại phòng (khóa chính)
    tenLoai NVARCHAR(50) NOT NULL,         -- Dùng NVARCHAR để hỗ trợ tiếng Việt
    soLuong INT NOT NULL,                 -- Số lượng phòng thuộc loại này
    dienTich FLOAT,                        -- Diện tích phòng (m2)
    giaTheoGio DECIMAL(10,2) NOT NULL,    -- Giá thuê theo giờ
    giaTheoNgay DECIMAL(10,2) NOT NULL,   -- Giá thuê theo ngày
    giaTheoDem DECIMAL(10,2) NOT NULL,    -- Giá thuê qua đêm
    phuThuQuaGio DECIMAL(10,2) NOT NULL   -- Phụ thu nếu ở quá giờ
);
go
CREATE TABLE Phong (
    soPhong VARCHAR(20) PRIMARY KEY,         -- Số phòng (mã phòng, khóa chính)
    trangThai NVARCHAR(50) NOT NULL,         -- Trạng thái phòng (VD: Trống, Đã đặt, Đang sử dụng)
    loaiPhong VARCHAR(20) NOT NULL,          -- Mã loại phòng (khóa ngoại từ bảng LoaiPhong)
	moTa NVARCHAR(200),
    CONSTRAINT FK_Phong_LoaiPhong FOREIGN KEY (loaiPhong) REFERENCES LoaiPhong(maLoaiPhong)
);
go
CREATE TABLE NhanVien (
    maNV VARCHAR(20) PRIMARY KEY,         -- Mã nhân viên (khóa chính)
    hoTen NVARCHAR(100) NOT NULL,         -- Họ và tên nhân viên
    ngaySinh DATE NOT NULL,               -- Ngày sinh
    sdt VARCHAR(15) NOT NULL,             -- Số điện thoại
    diaChi NVARCHAR(255) NOT NULL,        -- Địa chỉ nhân viên
    soCCCD VARCHAR(20) UNIQUE NOT NULL,   -- Số CCCD (Căn cước công dân)
    chucVu NVARCHAR(50) NOT NULL,         -- Chức vụ nhân viên
    caLamViec NVARCHAR(50) NOT NULL       -- Ca làm việc
);
go
CREATE TABLE TaiKhoan (
    tenDangNhap VARCHAR(50) PRIMARY KEY,  -- Tên đăng nhập (khóa chính)
    matKhau VARCHAR(255) NOT NULL,        -- Mật khẩu (có thể mã hóa sau)
    trangThai NVARCHAR(50) NOT NULL,      -- Trạng thái tài khoản (VD: Hoạt động, Vô hiệu hóa)
    maNV VARCHAR(20) UNIQUE,              -- Mã nhân viên (khóa ngoại, duy nhất)
    CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
        ON DELETE SET NULL                 -- Nếu xóa nhân viên, tài khoản vẫn còn nhưng không liên kết
);
go
CREATE TABLE KhachHang (
    maKH VARCHAR(20) PRIMARY KEY,       -- Mã khách hàng (khóa chính)
    hoTen NVARCHAR(100) NOT NULL,       -- Họ và tên khách hàng
    sdt VARCHAR(15) NOT NULL,           -- Số điện thoại
    soCCCD VARCHAR(20) UNIQUE NULL,  -- Số căn cước công dân (duy nhất)
	email NVARCHAR(50)
);
go
CREATE TABLE LoaiDichVu (
    maLoai VARCHAR(20) PRIMARY KEY,  -- Mã loại dịch vụ (khóa chính)
    tenLoai NVARCHAR(100) NOT NULL   -- Tên loại dịch vụ (hỗ trợ tiếng Việt)
);
go
CREATE TABLE DichVu (
    maDV VARCHAR(20) PRIMARY KEY,      -- Mã dịch vụ (khóa chính)
    tenDV NVARCHAR(100) NOT NULL,      -- Tên dịch vụ
    moTa NVARCHAR(255),                -- Mô tả dịch vụ
    giaDV FLOAT CHECK (giaDV >= 0),    -- Giá dịch vụ, không thể âm
    maLoai VARCHAR(20) NOT NULL,       -- Mã loại dịch vụ (khóa ngoại)
    CONSTRAINT FK_DichVu_LoaiDichVu FOREIGN KEY (maLoai) REFERENCES LoaiDichVu(maLoai)
);
go
CREATE TABLE DonDatPhong (
    maDonDatPhong VARCHAR(20) PRIMARY KEY,  -- Mã đơn đặt phòng (khóa chính)
    maKH VARCHAR(20) NOT NULL,              -- Mã khách hàng (khóa ngoại)
	ngayDatPhong DATETIME NOT NULL,         -- Ngày đặt phòng
    ngayNhanPhong DATETIME NOT NULL,        -- Ngày nhận phòng
    ngayTraPhong DATETIME NOT NULL,         -- Ngày trả phòng
    soKhach INT CHECK (soKhach > 0),        -- Số khách (phải lớn hơn 0)
    tienCoc FLOAT CHECK (tienCoc >= 0),     -- Tiền cọc (không thể âm)
	thoiGianCoc DATETIME NOT NULL,          -- Ngày đặt cọc
    maNV VARCHAR(20) NOT NULL,              -- Mã nhân viên xử lý đơn (khóa ngoại)
    loaiDon NVARCHAR(50) NOT NULL,          -- Loại đơn (hỗ trợ tiếng Việt)
	trangThai NVARCHAR(50) NOT NULL,         -- trạng thái đơn đặt phòng
    CONSTRAINT FK_DonDatPhong_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    CONSTRAINT FK_DonDatPhong_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
);
go
CREATE TABLE ChiTietDonDatPhong (
    maDonDatPhong VARCHAR(20) NOT NULL,  -- Mã đơn đặt phòng (khóa ngoại)
    soPhong VARCHAR(20) NOT NULL,        -- Số phòng (khóa ngoại)
    soLuong INT CHECK (soLuong > 0),     -- Số lượng phòng (phải lớn hơn 0)
    PRIMARY KEY (maDonDatPhong, soPhong),  -- Khóa chính kết hợp
    CONSTRAINT FK_ChiTiet_DonDatPhong FOREIGN KEY (maDonDatPhong) REFERENCES DonDatPhong(maDonDatPhong),
    CONSTRAINT FK_ChiTiet_Phong FOREIGN KEY (soPhong) REFERENCES Phong(soPhong)
);
go
CREATE TABLE ChiPhiPhatSinh (
    maChiPhi VARCHAR(50) PRIMARY KEY,
    chiPhiTBHong FLOAT NOT NULL,
    soGioOQuaGio INT ,
    maDonDatPhong VARCHAR(20) NOT NULL,
	moTa NVARCHAR(255) NULL,
    FOREIGN KEY (maDonDatPhong) REFERENCES DonDatPhong(maDonDatPhong)
);
go
CREATE TABLE PhieuDichVu (
    maPhieuDichVu VARCHAR(50) PRIMARY KEY,
    maDonDatPhong VARCHAR(20) NOT NULL,
	ngayLapPhieu DATETIME NOT NULL,
    trangThai NVARCHAR(50) NOT NULL,
    FOREIGN KEY (maDonDatPhong) REFERENCES DonDatPhong(maDonDatPhong)
);
go
CREATE TABLE ChiTietPhieuDichVu (
    maPhieuDichVu VARCHAR(50),
    maDichVu VARCHAR(20),
    soLuong INT NOT NULL CHECK (soLuong > 0),
    PRIMARY KEY (maPhieuDichVu, maDichVu),
    FOREIGN KEY (maPhieuDichVu) REFERENCES PhieuDichVu(maPhieuDichVu),
    FOREIGN KEY (maDichVu) REFERENCES DichVu(maDV)
);
go
CREATE TABLE KhuyenMai (
    maKhuyenMai VARCHAR(20) PRIMARY KEY,  
    tenKhuyenMai NVARCHAR(50) NOT NULL,         
    loaiKhuyenMai NVARCHAR(50) NOT NULL,                
    giaTriKhuyenMai FLOAT,                        
    ngayBatDau DATETIME NOT NULL,    
    ngayKetThuc DATETIME NOT NULL,   
    dieuKienApDung FLOAT NOT NULL,
	trangThai NVARCHAR(20) NOT NULL
);
go
CREATE TABLE ChiTietApDung (
    maDonDatPhong VARCHAR(20) NOT NULL,   -- Mã đơn đặt phòng (khóa ngoại)
    maKhuyenMai VARCHAR(20) NOT NULL,     -- Mã khuyến mãi (khóa ngoại)
    tongThanhToanSauApDung Float NOT NULL,
    PRIMARY KEY (maDonDatPhong, maKhuyenMai),  -- Khóa chính kết hợp
    CONSTRAINT FK_ChiTietApDung_DonDatPhong FOREIGN KEY (maDonDatPhong) REFERENCES DonDatPhong(maDonDatPhong),
    CONSTRAINT FK_ChiTietApDung_KhuyenMai FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai(maKhuyenMai)
);
GO
CREATE TABLE ChiTietSuDungPhong (
    maDonDatPhong VARCHAR(20) NOT NULL,
    soPhong VARCHAR(20) NOT NULL,
    ngayBatDau DATETIME NOT NULL,
    ngayKetThuc DATETIME NOT NULL,
    ghiChu NVARCHAR(255),
    PRIMARY KEY (maDonDatPhong, soPhong, ngayBatDau),
    FOREIGN KEY (maDonDatPhong) REFERENCES DonDatPhong(maDonDatPhong),
    FOREIGN KEY (soPhong) REFERENCES Phong(soPhong)
);
go
INSERT INTO LoaiPhong (maLoaiPhong, tenLoai, soLuong, dienTich, giaTheoGio, giaTheoNgay, giaTheoDem, phuThuQuaGio)
VALUES 
    ('single', N'Single Room', 8, 30, 150000, 800000, 700000, 100000),
    ('twin', N'Twin Room', 7, 50, 200000, 1200000, 1100000, 150000),
    ('double', N'Double Room', 10, 45, 250000, 1000000, 900000, 200000),
    ('triple', N'Triple Room', 6, 50, 300000, 1400000, 1300000, 250000);
go
INSERT INTO Phong (soPhong, trangThai, loaiPhong, moTa) VALUES
	-- single: 8 phòng
	('P101', N'Trống', 'single', N'Ban công'),
	('P102', N'Trống', 'single', N'View biển'),
	('P103', N'Trống', 'single', N'Phòng hút thuốc'),
	('P104', N'Trống', 'single', N'Ban công, View biển'),
	('P105', N'Trống', 'single', N'Ban công, Phòng hút thuốc'),
	('P106', N'Trống', 'single', N'View biển, Phòng hút thuốc'),
	('P501', N'Trống', 'single', N'Ban công, View biển, Phòng hút thuốc'),
	('P502', N'Trống', 'single', N'Ban công, View biển, Phòng hút thuốc'),
	
	-- double: 10 phòng
	('P201', N'Trống', 'double', N'Ban công'),
	('P202', N'Trống', 'double', N'View biển'),
	('P203', N'Trống', 'double', N'Phòng hút thuốc'),
	('P204', N'Trống', 'double', N'Ban công, View biển'),
	('P205', N'Trống', 'double', N'Ban công, Phòng hút thuốc'),
	('P206', N'Trống', 'double', N'View biển, Phòng hút thuốc'),
	('P503', N'Trống', 'double', N'Ban công, View biển, Phòng hút thuốc'),
	('P504', N'Trống', 'double', N'Ban công, View biển, Phòng hút thuốc'),
	('P505', N'Trống', 'double', N'Ban công, View biển, Phòng hút thuốc'),
	('P508', N'Trống', 'double', N'Ban công, View biển, Phòng hút thuốc'),
	
	-- twin: 7 phòng
	('P301', N'Trống', 'twin', N'Ban công'),
	('P302', N'Trống', 'twin', N'View biển'),
	('P303', N'Trống', 'twin', N'Phòng hút thuốc'),
	('P304', N'Trống', 'twin', N'Ban công, View biển'),
	('P305', N'Trống', 'twin', N'Ban công, Phòng hút thuốc'),
	('P306', N'Trống', 'twin', N'View biển, Phòng hút thuốc'),
	('P509', N'Trống', 'twin', N'Ban công, View biển, Phòng hút thuốc'),
	
	-- triple: 7 phòng
	('P401', N'Trống', 'triple', N'Ban công'),
	('P402', N'Trống', 'triple', N'View biển'),
	('P403', N'Trống', 'triple', N'Phòng hút thuốc'),
	('P404', N'Trống', 'triple', N'Ban công, View biển'),
	('P405', N'Trống', 'triple', N'Ban công, Phòng hút thuốc'),
	('P506', N'Trống', 'triple', N'Ban công, View biển, Phòng hút thuốc'),
	('P507', N'Trống', 'triple', N'Ban công, View biển, Phòng hút thuốc')
go
INSERT INTO NhanVien (maNV, hoTen, ngaySinh, sdt, diaChi, soCCCD, chucVu, caLamViec)
VALUES 
('2025LT001', N'Nguyễn Thị Xuyến', '1990-01-01', '0981234567', N'Hà Nội', '123456789012', N'Lễ tân', N'Sáng'),
('2025LT002', N'Phạm Văn Yến', '1988-05-12', '0972345678', N'Đà Nẵng', '123456789013', N'Lễ tân', N'Chiều'),
('2025QL001', N'Lê Phan Trung', '1988-05-12', '0972345679', N'Hồ Chí Minh', '123456789014', N'Quản lý', N'Chiều');
go
INSERT INTO TaiKhoan (tenDangNhap, matKhau, trangThai, maNV)
VALUES ('nv001', '123456', N'Hoạt động', '2025LT001'),('ql001', '123456', N'Hoạt động', '2025QL001');
go
-- Chèn dữ liệu vào bảng LoaiDichVu
INSERT INTO LoaiDichVu (maLoai, tenLoai)
VALUES 
('Buffet', N'Buffet'), ('GoiMon', N'Gọi món'), ('GiatUi', N'Giặt ủi'), ('DichVuKhac', N'Dịch vụ khác');
go
-- Chèn dữ liệu vào bảng DichVu
INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai)
VALUES
('DVBuffet', N'Buffet sáng', N'Từ 6:00-10:00', 150000, 'Buffet'),

('DVGoiMon1', N'Salad Caesar', N'Salad rau trộn với sốt Caesar, phô mai Parmesan, croutons.', 120000, 'GoiMon'),
('DVGoiMon2', N'Gỏi Cuốn Tôm Thịt', N'Cuốn tươi gồm tôm, thịt, rau sống, bún, kèm nước chấm đặc biệt.', 100000, 'GoiMon'),
('DVGoiMon3', N'Súp Kem Nấm', N'Súp kem mịn, hương nấm thơm ngon, thích hợp cho bữa khởi đầu.', 130000, 'GoiMon'),
('DVGoiMon4', N'Phở Bò', N'Phở bò truyền thống với nước dùng trong, bánh phở mềm, thịt bò thái mỏng.', 150000, 'GoiMon'),
('DVGoiMon5', N'Cơm Gà Xối Mỡ', N'Cơm trắng ăn kèm với gà xối mỡ giòn, nước sốt đặc trưng.', 180000, 'GoiMon'),
('DVGoiMon6', N'Bít Tết Bò Sốt Tiêu Đen', N'Miếng bít tết bò chín tới, sốt tiêu đen đậm đà, ăn kèm rau củ.', 350000, 'GoiMon'),
('DVGoiMon7', N'Pasta Sốt Bolognese', N'Mì Ý sốt bolognese đậm đà với thịt bò, cà chua, và gia vị Ý.', 200000, 'GoiMon'),
('DVGoiMon8', N'Kem Vani', N'Kem mịn màng, hương vị vani truyền thống.', 70000, 'GoiMon'),
('DVGoiMon9', N'Bánh Mousse Socola', N'Bánh mousse béo ngậy với vị socola đậm đà.', 100000, 'GoiMon'),
('DVGoiMon10', N'Trái Cây Tươi', N'Đĩa trái cây theo mùa, cắt sẵn, tươi ngon.', 80000, 'GoiMon'),
('DVGoiMon11', N'Nước Cam Tươi', N'Nước cam ép tươi, không đường.', 50000, 'GoiMon'),
('DVGoiMon12', N'Trà Xanh Nóng', N'Trà xanh thanh mát, hương thơm tự nhiên.', 40000, 'GoiMon'),
('DVGoiMon13', N'Cà Phê Đen', N'Cà phê đen đậm đà, được rang xay ngay.', 45000, 'GoiMon'),
('DVGoiMon14', N'Nước Suối', N'Nước lọc tinh khiết.', 20000, 'GoiMon');
-- STT 1: Áo thun
INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai) VALUES
('DVGiatUiSay1', N'Áo thun', N'Giặt Sấy', 8000, 'GiatUi'),
('DVGiatUiHap1', N'Áo thun', N'Giặt Hấp', 15000, 'GiatUi');

-- STT 2: Áo sơ mi
INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai) VALUES
('DVGiatUiSay2', N'Áo sơ mi', N'Giặt Sấy', 10000, 'GiatUi'),
('DVGiatUiHap2', N'Áo sơ mi', N'Giặt Hấp', 20000, 'GiatUi');

-- STT 3: Áo vest
INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai) VALUES
('DVGiatUiHap3', N'Áo vest', N'Giặt Hấp', 150000, 'GiatUi');

-- STT 4: Quần âu
INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai) VALUES
('DVGiatUiSay4', N'Quần âu', N'Giặt Sấy', 20000, 'GiatUi'),
('DVGiatUiHap4', N'Quần âu', N'Giặt Hấp', 40000, 'GiatUi');

-- STT 5: Quần jeans
INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai) VALUES
('DVGiatUiSay5', N'Quần jeans', N'Giặt Sấy', 20000, 'GiatUi');

-- STT 6: Váy dạ hội
INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai) VALUES
('DVGiatUiHap6', N'Váy dạ hội', N'Giặt Hấp', 200000, 'GiatUi');

-- STT 7: Váy dài
INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai) VALUES
('DVGiatUiHap7', N'Váy dài', N'Giặt Hấp', 250000, 'GiatUi');

-- STT 8: Đầm công sở
INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai) VALUES
('DVGiatUiSay8', N'Đầm công sở', N'Giặt Sấy', 50000, 'GiatUi'),
('DVGiatUiHap8', N'Đầm công sở', N'Giặt Hấp', 100000, 'GiatUi');

-- STT 9: Áo choàng
INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai) VALUES
('DVGiatUiHap9', N'Áo choàng', N'Giặt Hấp', 200000, 'GiatUi');

-- STT 10: Áo khoác da
INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai) VALUES
('DVGiatUiHap10', N'Áo khoác da', N'Giặt Hấp', 300000, 'GiatUi');

-- STT 11: Áo khoác lông vũ
INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai) VALUES
('DVGiatUiHap11', N'Áo khoác lông vũ', N'Giặt Hấp', 180000, 'GiatUi');

-- STT 12: Áo len
INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai) VALUES
('DVGiatUiSay12', N'Áo len', N'Giặt Sấy', 40000, 'GiatUi'),
('DVGiatUiHap12', N'Áo len', N'Giặt Hấp', 80000, 'GiatUi');

-- STT 13: Khăn tắm
INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai) VALUES
('DVGiatUiSay13', N'Khăn tắm', N'Giặt Sấy', 20000, 'GiatUi');

-- STT 14: Đồ lót (1 bộ)
INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai) VALUES
('DVGiatUiSay14', N'Đồ lót (1 bộ)', N'Giặt Sấy', 15000, 'GiatUi');

INSERT INTO KhachHang (maKH, hoTen, sdt, soCCCD, email) VALUES
('KH230520250001', N'Lê Thành Đạo', '0334072027', '075204003367', 'nguyenvandao@gmail.com'),
('KH230520250002', N'Nguyễn Quốc Việt', '0379173132', '064204008238', 'nguyenvanviet@gmail.com'),
('KH230520250003', N'Ngô Bình Xuyên', '0946611645', '091204012876', 'nguyenvanxuyen@gmail.com'),
('KH230520250004', N'Hà Văn Dương', '0383808030', '075204003399', 'nguyenvanduong@gmail.com');

INSERT INTO DonDatPhong
    (maDonDatPhong, maKH, ngayDatPhong, ngayNhanPhong, ngayTraPhong,soKhach, tienCoc, thoiGianCoc, maNV, loaiDon, trangThai)
VALUES
    ('23052025LT001001','KH230520250001','2025-05-23 12:00:00.000','2025-05-23 14:00:00.000','2025-05-24 12:00:00.000',2,0,'2025-05-23 03:00:00.000','2025LT001',N'Theo ngày',N'Nhận phòng');
INSERT INTO ChiTietDonDatPhong (maDonDatPhong, soPhong, soLuong)
VALUES ('23052025LT001001', 'P101', 1);
UPDATE Phong
SET trangThai = N'Đang ở'
WHERE soPhong IN ('P101');
go

INSERT INTO KhuyenMai (
    maKhuyenMai,
    tenKhuyenMai,
    loaiKhuyenMai,
    giaTriKhuyenMai,
    ngayBatDau,
    ngayKetThuc,
    dieuKienApDung,
    trangThai
)
VALUES (
    N'Không',                     -- mã khuyến mãi đặc biệt
    N'Không',                    -- tên hiển thị
    N'Khuyến mãi theo dịp lễ',                    -- loại khuyến mãi
    0,                           -- giá trị khuyến mãi = 0
    '2000-01-01',                -- ngày bắt đầu từ rất xa
    '9999-12-31',                -- ngày kết thúc coi như "vô hạn"
    0,                           -- điều kiện áp dụng = 0
    N'Đang áp dụng'              -- trạng thái: có thể dùng cho mặc định
);


INSERT INTO DichVu (maDV, tenDV, moTa, giaDV, maLoai) VALUES
('DVThueXeDay', N'Thuê xe đẩy em bé', N'Thuê xe đẩy em bé', 50000, 'DichVuKhac'),
('DVBaoMau', N'Phí bảo mẫu', N'Phí bảo mẫu', 40000, 'DichVuKhac'),
('DVNoiEmBe', N'Nôi em bé', N'Nôi em bé', 30000, 'DichVuKhac');
go

INSERT INTO DonDatPhong
    (maDonDatPhong, maKH, ngayDatPhong, ngayNhanPhong, ngayTraPhong, soKhach, tienCoc, thoiGianCoc, maNV, loaiDon, trangThai)
VALUES
    ('25052025LT001001', 'KH230520250001', '2025-05-25 10:00:00', '2025-05-27 14:00:00', '2025-05-31 12:00:00', 1, 0, '2025-05-25 10:00:00', '2025LT001', N'Theo ngày', N'Nhận phòng');

	INSERT INTO ChiTietDonDatPhong (maDonDatPhong, soPhong, soLuong)
VALUES 
    ('25052025LT001001', 'P502', 1); -- single

UPDATE Phong
SET trangThai = N'Đang ở'
WHERE soPhong IN ('P502');

INSERT INTO ChiTietSuDungPhong (maDonDatPhong, soPhong, ngayBatDau, ngayKetThuc, ghiChu)
VALUES 
    ('25052025LT001001', 'P101', '2025-05-27 14:00:00', '2025-05-31 12:00:00', N'Phòng ban công');

INSERT INTO ChiTietApDung (maDonDatPhong, maKhuyenMai, tongThanhToanSauApDung)
VALUES 
    ('25052025LT001001', N'Không', 5000000);