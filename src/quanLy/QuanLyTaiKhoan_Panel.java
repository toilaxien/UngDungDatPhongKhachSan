package quanLy;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import entity.NhanVien;
import entity.TaiKhoan;
import dao.TaiKhoan_Dao;
import dao.NhanVien_Dao;

public class QuanLyTaiKhoan_Panel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private TaiKhoan_Dao tkDao = new TaiKhoan_Dao();
    private NhanVien_Dao nvDao = new NhanVien_Dao();
    private ArrayList<NhanVien> dsNhanVien;

    public QuanLyTaiKhoan_Panel() {
        setLayout(new BorderLayout());

        Font fontChuan = new Font("Times New Roman", Font.PLAIN, 18);
        Font fontDam = new Font("Times New Roman", Font.BOLD, 18);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(240, 240, 240));
        add(contentPanel, BorderLayout.CENTER);

        JLabel lblTitle = new JLabel("Quản lý tài khoản");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblTitle.setBounds(450, 0, 300, 30);
        contentPanel.add(lblTitle);

        JPanel tablePanel = new JPanel(null);
        tablePanel.setBounds(30, 60, 1140, 580);
        tablePanel.setBackground(Color.WHITE);
        contentPanel.add(tablePanel);

        JLabel lblDanhSach = new JLabel("Danh sách tài khoản nhân viên");
        lblDanhSach.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblDanhSach.setBounds(400, 10, 400, 30);
        tablePanel.add(lblDanhSach);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 50, 1100, 500);
        tablePanel.add(scrollPane);

        String[] columns = {"Mã NV", "Họ tên", "Chức vụ", "Tài khoản", "Trạng thái", "Chức năng"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setFont(fontChuan);
        table.setRowHeight(30);

        JTableHeader header = table.getTableHeader();
        header.setFont(fontDam);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getColumn("Chức năng").setCellRenderer(new ButtonRenderer());
        scrollPane.setViewportView(table);
        loadTableData();
    }

    public void refreshTable() {
        try {
            model.setRowCount(0);
            dsNhanVien = nvDao.getAllNhanVien();
            for (NhanVien nv : dsNhanVien) {
                TaiKhoan tk = tkDao.getTaiKhoanByMaNV(nv.getMaNV());
                String tenTK = (tk != null) ? tk.getTenDangNhap() : "Chưa có";
                String trangThai = (tk != null) ? tk.getTrangThai() : "";
                model.addRow(new Object[]{
                    nv.getMaNV(), nv.getHoTen(), nv.getChucVu(), tenTK, trangThai, "Quản lý"
                });
            }
            model.fireTableDataChanged();
            System.out.println("refreshTable: Loaded " + dsNhanVien.size() + " employees");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadTableData() {
        refreshTable();
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), dsNhanVien, tkDao));
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Quản lý");
            setFont(new Font("Times New Roman", Font.BOLD, 16));
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private int row;
        private ArrayList<NhanVien> dsNhanVien;
        private TaiKhoan_Dao tkDao;

        public ButtonEditor(JCheckBox checkBox, ArrayList<NhanVien> dsNhanVien, TaiKhoan_Dao tkDao) {
            super(checkBox);
            this.dsNhanVien = dsNhanVien;
            this.tkDao = tkDao;

            button = new JButton("Quản lý");
            button.setFont(new Font("Times New Roman", Font.BOLD, 16));
            button.setBackground(new Color(173, 216, 230));
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorderPainted(false);

            button.addActionListener(e -> {
                fireEditingStopped();
                NhanVien nv = dsNhanVien.get(row);
                TaiKhoan tk = tkDao.getTaiKhoanByMaNV(nv.getMaNV());

                if (tk == null) {
                    taoTaiKhoan(nv, row);
                } else {
                    suaHoacKhoaTaiKhoan(tk, row);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Quản lý";
        }
    }

    private void taoTaiKhoan(NhanVien nv, int row) {
        try {
            String tenDangNhap = JOptionPane.showInputDialog(this, "Tên đăng nhập:");
            String matKhau = JOptionPane.showInputDialog(this, "Mật khẩu:");
            if (tenDangNhap != null && matKhau != null && !tenDangNhap.trim().isEmpty() && !matKhau.trim().isEmpty()) {
                TaiKhoan tk = new TaiKhoan(tenDangNhap, matKhau, "Hoạt động", nv);
                if (tkDao.taoTaiKhoan(tk)) {
                    JOptionPane.showMessageDialog(this, "Tạo tài khoản thành công!");
                    model.setValueAt(tenDangNhap, row, 3);
                    model.setValueAt("Hoạt động", row, 4);
                    model.fireTableDataChanged();
                    System.out.println("taoTaiKhoan: Created account for " + nv.getMaNV());
                } else {
                    JOptionPane.showMessageDialog(this, "Tạo tài khoản thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập và mật khẩu không được để trống!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tạo tài khoản: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void suaHoacKhoaTaiKhoan(TaiKhoan tk, int row) {
        try {
            String[] options = {"Đổi mật khẩu", "Khóa tài khoản"};
            int choice = JOptionPane.showOptionDialog(this, "Chọn thao tác", "Quản lý tài khoản",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == 0) {
                String matKhauMoi = JOptionPane.showInputDialog(this, "Nhập mật khẩu mới:");
                if (matKhauMoi != null && !matKhauMoi.trim().isEmpty()) {
                    tk.setMatKhau(matKhauMoi);
                    if (tkDao.capNhatTaiKhoan(tk)) {
                        JOptionPane.showMessageDialog(this, "Cập nhật mật khẩu thành công!");
                        System.out.println("suaHoacKhoaTaiKhoan: Updated password for " + tk.getTenDangNhap());
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật mật khẩu thất bại!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Mật khẩu mới không được để trống!");
                }
            } else if (choice == 1) {
                if (tkDao.khoaTaiKhoan(tk.getTenDangNhap())) {
                    JOptionPane.showMessageDialog(this, "Tài khoản đã bị khóa!");
                    model.setValueAt("Vô hiệu hóa", row, 4);
                    model.fireTableDataChanged();
                    System.out.println("suaHoacKhoaTaiKhoan: Locked account " + tk.getTenDangNhap());
                } else {
                    JOptionPane.showMessageDialog(this, "Khóa tài khoản thất bại!");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xử lý tài khoản: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
