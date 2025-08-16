package quanLy;

import javax.swing.*;
import javax.swing.table.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import dao.NhanVien_Dao;
import entity.NhanVien;

public class QuanLyNhanVien_Panel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfMaNV, tfHoTen, tfSdt, tfCCCD, tfDiaChi;
    private JComboBox<String> cbChucVu, cbCa;
    private JDateChooser dateChooser;
    private NhanVien_Dao nhanVienDao = new NhanVien_Dao();
    private QuanLyTaiKhoan_Panel taiKhoanPanel;

    public QuanLyNhanVien_Panel(QuanLyTaiKhoan_Panel taiKhoanPanel) {
        this.taiKhoanPanel = taiKhoanPanel;
        setLayout(new BorderLayout());

        Font fontChuan = new Font("Times New Roman", Font.PLAIN, 18);
        Font fontDam = new Font("Times New Roman", Font.BOLD, 18);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(240, 240, 240));
        add(contentPanel, BorderLayout.CENTER);

        JLabel lblTitle = new JLabel("Quản lý nhân viên");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblTitle.setBounds(450, 0, 300, 30);
        contentPanel.add(lblTitle);

        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(30, 40, 1140, 250);
        formPanel.setBackground(Color.WHITE);
        contentPanel.add(formPanel);

        int labelWidth = 130, fieldWidth = 250, height = 30, spacingY = 40;
        int leftX = 40, rightX = 600;

        JLabel lbMaNV = new JLabel("Mã nhân viên:");
        lbMaNV.setFont(fontDam);
        lbMaNV.setBounds(leftX, 20, labelWidth, height);
        tfMaNV = new JTextField();
        tfMaNV.setFont(fontChuan);
        tfMaNV.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfMaNV.setBounds(leftX + labelWidth + 10, 20, fieldWidth, height);
        tfMaNV.setEditable(false);

        JLabel lbHoTen = new JLabel("Họ tên:");
        lbHoTen.setFont(fontDam);
        lbHoTen.setBounds(leftX, 20 + spacingY, labelWidth, height);
        tfHoTen = new JTextField();
        tfHoTen.setFont(fontChuan);
        tfHoTen.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfHoTen.setBounds(leftX + labelWidth + 10, 20 + spacingY, fieldWidth, height);

        JLabel lbNgaySinh = new JLabel("Ngày sinh:");
        lbNgaySinh.setFont(fontDam);
        lbNgaySinh.setBounds(leftX, 20 + 2 * spacingY, labelWidth, height);
        dateChooser = new JDateChooser();
        dateChooser.setFont(fontChuan);
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        dateChooser.setBounds(leftX + labelWidth + 10, 20 + 2 * spacingY, fieldWidth, height);

        JLabel lbSdt = new JLabel("SĐT:");
        lbSdt.setFont(fontDam);
        lbSdt.setBounds(leftX, 20 + 3 * spacingY, labelWidth, height);
        tfSdt = new JTextField();
        tfSdt.setFont(fontChuan);
        tfSdt.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfSdt.setBounds(leftX + labelWidth + 10, 20 + 3 * spacingY, fieldWidth, height);

        JLabel lbCCCD = new JLabel("CCCD:");
        lbCCCD.setFont(fontDam);
        lbCCCD.setBounds(rightX, 20, labelWidth, height);
        tfCCCD = new JTextField();
        tfCCCD.setFont(fontChuan);
        tfCCCD.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfCCCD.setBounds(rightX + labelWidth + 10, 20, fieldWidth, height);

        JLabel lbDiaChi = new JLabel("Địa chỉ:");
        lbDiaChi.setFont(fontDam);
        lbDiaChi.setBounds(rightX, 20 + spacingY, labelWidth, height);
        tfDiaChi = new JTextField();
        tfDiaChi.setFont(fontChuan);
        tfDiaChi.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfDiaChi.setBounds(rightX + labelWidth + 10, 20 + spacingY, fieldWidth, height);

        JLabel lbChucVu = new JLabel("Chức vụ:");
        lbChucVu.setFont(fontDam);
        lbChucVu.setBounds(rightX, 20 + 2 * spacingY, labelWidth, height);
        cbChucVu = new JComboBox<>(new String[]{"Kế toán", "Lễ tân", "Bếp", "Buồng phòng"});
        cbChucVu.setFont(fontChuan);
        cbChucVu.setBounds(rightX + labelWidth + 10, 20 + 2 * spacingY, fieldWidth, height);
        cbChucVu.addActionListener(e -> updateMaNVField());

        JLabel lbCa = new JLabel("Ca làm việc:");
        lbCa.setFont(fontDam);
        lbCa.setBounds(rightX, 20 + 3 * spacingY, labelWidth, height);
        cbCa = new JComboBox<>(new String[]{"Ca 1", "Ca 2", "Ca 3"});
        cbCa.setFont(fontChuan);
        cbCa.setBounds(rightX + labelWidth + 10, 20 + 3 * spacingY, fieldWidth, height);

        JButton btnThem = new JButton("Thêm nhân viên");
        btnThem.setFont(fontDam);
        btnThem.setBackground(new Color(0, 255, 128));
        btnThem.setOpaque(true);
        btnThem.setContentAreaFilled(true);
        btnThem.setBorderPainted(false);
        btnThem.setBounds(rightX + labelWidth + 10, 20 + 4 * spacingY + 5, 180, 35);
        btnThem.addActionListener(this::handleAdd);

        Component[] components = {
            lbMaNV, tfMaNV, lbHoTen, tfHoTen, lbNgaySinh, dateChooser, lbSdt, tfSdt,
            lbCCCD, tfCCCD, lbDiaChi, tfDiaChi, lbChucVu, cbChucVu, lbCa, cbCa, btnThem
        };
        for (Component c : components) formPanel.add(c);

        JPanel tablePanel = new JPanel(null);
        tablePanel.setBounds(30, 300, 1140, 360);
        tablePanel.setBackground(Color.WHITE);
        contentPanel.add(tablePanel);

        JLabel lblDanhSach = new JLabel("Danh sách nhân viên");
        lblDanhSach.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblDanhSach.setBounds(420, 10, 300, 30);
        tablePanel.add(lblDanhSach);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 50, 1100, 280);
        tablePanel.add(scrollPane);

        String[] columns = {"Mã NV", "Họ tên", "Ngày sinh", "SĐT", "CCCD", "Địa chỉ", "Chức vụ", "Ca làm"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setFont(fontChuan);
        table.setRowHeight(30);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Times New Roman", Font.BOLD, 18));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scrollPane.setViewportView(table);
        loadData();
        updateMaNVField();
    }

    private void loadData() {
        try {
            model.setRowCount(0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            List<NhanVien> list = nhanVienDao.getAllNhanVien();
            for (NhanVien nv : list) {
                model.addRow(new Object[]{
                    nv.getMaNV(), nv.getHoTen(),
                    sdf.format(java.sql.Date.valueOf(nv.getNgaySinh())),
                    nv.getSdt(), nv.getSoCCCD(), nv.getDiaChi(), nv.getChucVu(), nv.getCaLamViec()
                });
            }
            System.out.println("loadData: Loaded " + list.size() + " employees into table");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String generateMaNV() {
        String role = cbChucVu.getSelectedItem().toString();
        String roleCode;
        switch (role) {
            case "Kế toán": roleCode = "KT"; break;
            case "Lễ tân": roleCode = "LT"; break;
            case "Bếp": roleCode = "B"; break;
            case "Buồng phòng": roleCode = "BP"; break;
            default: roleCode = "NV";
        }

        int year = LocalDate.now().getYear();
        String prefix = year + roleCode;
        int sequence = 1;
        String maNV;

        try {
            do {
                maNV = String.format("%s%03d", prefix, sequence);
                sequence++;
                if (sequence > 999) {
                    JOptionPane.showMessageDialog(this, "Đã hết số thứ tự cho mã nhân viên!");
                    return null;
                }
            } while (nhanVienDao.isMaNVExists(maNV));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra mã nhân viên: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return maNV;
    }

    private void updateMaNVField() {
        String maNV = generateMaNV();
        tfMaNV.setText(maNV != null ? maNV : "Lỗi sinh mã");
    }

    private void handleAdd(ActionEvent e) {
        Date selectedDate = dateChooser.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày sinh hợp lệ!");
            return;
        }
        LocalDate birthDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();
        long age = ChronoUnit.YEARS.between(birthDate, today);
        if (age < 18) {
            JOptionPane.showMessageDialog(this, "Nhân viên phải đủ 18 tuổi!");
            return;
        }

        String hoTen = tfHoTen.getText().trim();
        String sdt = tfSdt.getText().trim();
        String cccd = tfCCCD.getText().trim();
        String diaChi = tfDiaChi.getText().trim();
        String maNV = tfMaNV.getText();

        if (hoTen.isEmpty() || sdt.isEmpty() || cccd.isEmpty() || diaChi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
            return;
        }

        if (maNV.equals("Lỗi sinh mã") || maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không thể sinh mã nhân viên!");
            return;
        }

        if (!hoTen.matches("^[\\p{L} ]+$") || hoTen.length() < 2 || hoTen.length() > 50) {
            JOptionPane.showMessageDialog(this, "Họ tên chỉ chứa chữ cái, dấu cách, từ 2-50 ký tự!");
            return;
        }

        if (!sdt.matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "SĐT phải là 10 số, bắt đầu bằng 0!");
            return;
        }

        if (!cccd.matches("^\\d{12}$")) {
            JOptionPane.showMessageDialog(this, "CCCD phải là 12 số!");
            return;
        }

        if (!diaChi.matches("^[\\p{L}\\d\\s,./-]+$") || diaChi.length() < 5 || diaChi.length() > 100) {
            JOptionPane.showMessageDialog(this, "Địa chỉ từ 5-100 ký tự, chỉ chứa chữ, số, dấu cách, ,./-!");
            return;
        }

        NhanVien nv = new NhanVien(
            maNV, hoTen, birthDate,
            sdt, diaChi, cccd,
            cbChucVu.getSelectedItem().toString(), cbCa.getSelectedItem().toString()
        );

        try {
            if (nhanVienDao.them(nv)) {
                model.addRow(new Object[]{
                    nv.getMaNV(), nv.getHoTen(), new SimpleDateFormat("dd/MM/yyyy").format(selectedDate),
                    nv.getSdt(), nv.getSoCCCD(), nv.getDiaChi(), nv.getChucVu(), nv.getCaLamViec()
                });
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                // Clear input fields
                tfHoTen.setText("");
                tfSdt.setText("");
                tfCCCD.setText("");
                tfDiaChi.setText("");
                dateChooser.setDate(null);
                cbChucVu.setSelectedIndex(0); // Reset to first option
                cbCa.setSelectedIndex(0);     // Reset to first option
                updateMaNVField(); // Generate new MaNV
                if (taiKhoanPanel != null) {
                    System.out.println("handleAdd: Triggering refreshTable for employee " + nv.getMaNV());
                    taiKhoanPanel.refreshTable();
                } else {
                    System.out.println("handleAdd: taiKhoanPanel is null");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại! Mã nhân viên hoặc CCCD có thể đã tồn tại.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhân viên: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}