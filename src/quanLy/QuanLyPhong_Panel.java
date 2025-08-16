package quanLy;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import dao.Phong_Dao;
import dao.LoaiPhong_Dao;
import entity.Phong;
import entity.LoaiPhong;

public class QuanLyPhong_Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    // Phong components
    private JTable tablePhong;
    private DefaultTableModel modelPhong;
    private JTextField tfSoPhong, tfMoTa;
    private JComboBox<LoaiPhong> cbLoaiPhong;
    private JComboBox<String> cbTrangThai;
    private JButton btnThemPhong, btnSuaPhong, btnXoaPhong;

    // LoaiPhong components
    private JTable tableLoaiPhong;
    private DefaultTableModel modelLoaiPhong;
    private JTextField tfMaLoaiPhong, tfTenLoai, tfSoLuong, tfDienTich, tfGiaTheoGio, tfGiaTheoNgay, tfGiaTheoDem, tfPhuThuQuaGio;
    private JButton btnThemLoaiPhong, btnSuaLoaiPhong, btnXoaLoaiPhong;

    private final Phong_Dao phongDao = new Phong_Dao();
    private final LoaiPhong_Dao loaiPhongDao = new LoaiPhong_Dao();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private final Pattern soPhongPattern = Pattern.compile("^P\\d{3}$"); // Regex for P followed by 3 digits

    public QuanLyPhong_Panel() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        add(tabbedPane, BorderLayout.CENTER);

        // Tab 1: Quản lý phòng
        JPanel phongPanel = createPhongPanel();
        tabbedPane.addTab("Quản lý phòng", phongPanel);

        // Tab 2: Quản lý loại phòng
        JPanel loaiPhongPanel = createLoaiPhongPanel();
        tabbedPane.addTab("Quản lý loại phòng", loaiPhongPanel);
    }

    private JPanel createPhongPanel() {
        JPanel contentPanel = new JPanel(null);
        contentPanel.setBackground(new Color(240, 240, 240));

        JLabel lblTitle = new JLabel("Quản lý phòng", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblTitle.setBounds(450, 0, 300, 30);
        contentPanel.add(lblTitle);

        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(30, 40, 1140, 250);
        formPanel.setBackground(Color.WHITE);
        contentPanel.add(formPanel);

        // Form layout
        int labelWidth = 130, fieldWidth = 250, height = 30, spacingY = 40;
        int leftX = 40, rightX = 600;

        Font fontChuan = new Font("Times New Roman", Font.PLAIN, 18);
        Font fontDam = new Font("Times New Roman", Font.BOLD, 18);

        // Left side
        JLabel lbSoPhong = new JLabel("Số phòng:");
        lbSoPhong.setFont(fontDam);
        lbSoPhong.setBounds(leftX, 20, labelWidth, height);

        tfSoPhong = new JTextField();
        tfSoPhong.setFont(fontChuan);
        tfSoPhong.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfSoPhong.setBounds(leftX + labelWidth + 10, 20, fieldWidth, height);

        JLabel lbTrangThai = new JLabel("Trạng thái:");
        lbTrangThai.setFont(fontDam);
        lbTrangThai.setBounds(leftX, 20 + spacingY, labelWidth, height);

        cbTrangThai = new JComboBox<>(new String[]{"Trống", "Không Sử Dụng"});
        cbTrangThai.setFont(fontChuan);
        cbTrangThai.setBounds(leftX + labelWidth + 10, 20 + spacingY, fieldWidth, height);

        // Right side
        JLabel lbLoaiPhong = new JLabel("Loại phòng:");
        lbLoaiPhong.setFont(fontDam);
        lbLoaiPhong.setBounds(rightX, 20, labelWidth, height);

        cbLoaiPhong = new JComboBox<>();
        cbLoaiPhong.setFont(fontChuan);
        cbLoaiPhong.setBounds(rightX + labelWidth + 10, 20, fieldWidth, height);
        cbLoaiPhong.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(fontChuan);
                if (value instanceof LoaiPhong) {
                    setText(((LoaiPhong) value).getTenLoai());
                }
                return this;
            }
        });
        loadLoaiPhong();

        JLabel lbMoTa = new JLabel("Mô tả:");
        lbMoTa.setFont(fontDam);
        lbMoTa.setBounds(rightX, 20 + spacingY, labelWidth, height);

        tfMoTa = new JTextField();
        tfMoTa.setFont(fontChuan);
        tfMoTa.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfMoTa.setBounds(rightX + labelWidth + 10, 20 + spacingY, fieldWidth, height);


        // Buttons (aligned on the same row, adjusted to prevent clipping)
        int buttonWidth = 170, buttonHeight = 35, buttonGap = 10;
        int buttonY = 120 + 2 * spacingY + 5;
        int buttonStartX = rightX-180;

        btnThemPhong = new JButton("Thêm phòng");
        btnThemPhong.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnThemPhong.setBackground(new Color(0, 255, 128));
        btnThemPhong.setOpaque(true);
        btnThemPhong.setContentAreaFilled(true);
        btnThemPhong.setBorderPainted(false);
        btnThemPhong.setBounds(buttonStartX, buttonY, buttonWidth, buttonHeight);
        btnThemPhong.addActionListener(this::handleAddPhong);

        btnSuaPhong = new JButton("Sửa phòng");
        btnSuaPhong.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnSuaPhong.setBackground(new Color(255, 165, 0));
        btnSuaPhong.setOpaque(true);
        btnSuaPhong.setContentAreaFilled(true);
        btnSuaPhong.setBorderPainted(false);
        btnSuaPhong.setBounds(buttonStartX + buttonWidth + buttonGap, buttonY, buttonWidth, buttonHeight);
        btnSuaPhong.addActionListener(this::handleUpdatePhong);

        btnXoaPhong = new JButton("Xóa phòng");
        btnXoaPhong.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnXoaPhong.setBackground(new Color(255, 69, 0));
        btnXoaPhong.setOpaque(true);
        btnXoaPhong.setContentAreaFilled(true);
        btnXoaPhong.setBorderPainted(false);
        btnXoaPhong.setBounds(buttonStartX + 2 * (buttonWidth + buttonGap), buttonY, buttonWidth, buttonHeight);
        btnXoaPhong.addActionListener(this::handleDeletePhong);

        JButton btnXoaTrangPhong = new JButton("Xóa trắng");
        btnXoaTrangPhong.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnXoaTrangPhong.setBackground(new Color(128, 128, 128));
        btnXoaTrangPhong.setOpaque(true);
        btnXoaTrangPhong.setContentAreaFilled(true);
        btnXoaTrangPhong.setBorderPainted(false);
        btnXoaTrangPhong.setBounds(buttonStartX + 3 * (buttonWidth + buttonGap), buttonY, buttonWidth, buttonHeight);
        btnXoaTrangPhong.addActionListener(e -> clearPhongForm());


        Component[] components = {
                lbSoPhong, tfSoPhong, lbTrangThai, cbTrangThai,
                lbLoaiPhong, cbLoaiPhong, lbMoTa, tfMoTa,
                btnThemPhong, btnSuaPhong, btnXoaPhong, btnXoaTrangPhong
        };
        for (Component c : components) formPanel.add(c);

        JPanel tablePanel = new JPanel(null);
        tablePanel.setBounds(30, 300, 1140, 360);
        tablePanel.setBackground(Color.WHITE);
        contentPanel.add(tablePanel);

        JLabel lblDanhSach = new JLabel("Danh sách phòng", SwingConstants.CENTER);
        lblDanhSach.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblDanhSach.setBounds(420, 10, 300, 30);
        tablePanel.add(lblDanhSach);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 50, 1100, 280);
        tablePanel.add(scrollPane);

        String[] columns = {"Số phòng", "Trạng thái", "Loại phòng", "Diện tích", "Giá/giờ", "Giá/ngày", "Giá/đêm", "Phụ thu quá giờ", "Mô tả"};
        modelPhong = new DefaultTableModel(columns, 0);
        tablePhong = new JTable(modelPhong);
        tablePhong.setRowHeight(30);
        tablePhong.setFont(new Font("Times New Roman", Font.PLAIN, 16));

        JTableHeader header = tablePhong.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tablePhong.getColumnCount(); i++) {
            tablePhong.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tablePhong.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tablePhong.getSelectedRow();
                if (selectedRow >= 0) {
                    tfSoPhong.setText(modelPhong.getValueAt(selectedRow, 0).toString());
                    cbTrangThai.setSelectedItem(modelPhong.getValueAt(selectedRow, 1).toString());
                    String tenLoai = modelPhong.getValueAt(selectedRow, 2).toString();
                    for (int i = 0; i < cbLoaiPhong.getItemCount(); i++) {
                        if (cbLoaiPhong.getItemAt(i).getTenLoai().equals(tenLoai)) {
                            cbLoaiPhong.setSelectedIndex(i);
                            break;
                        }
                    }
                    tfMoTa.setText(modelPhong.getValueAt(selectedRow, 8) != null ? modelPhong.getValueAt(selectedRow, 8).toString() : "");
                    tfSoPhong.setEditable(false);
                }
            }
        });

        scrollPane.setViewportView(tablePhong);
        loadDataPhong();
        return contentPanel;
    }

    private JPanel createLoaiPhongPanel() {
        JPanel contentPanel = new JPanel(null);
        contentPanel.setBackground(new Color(240, 240, 240));

        JLabel lblTitle = new JLabel("Quản lý loại phòng", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblTitle.setBounds(450, 0, 300, 30);
        contentPanel.add(lblTitle);

        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(30, 40, 1140, 250);
        formPanel.setBackground(Color.WHITE);
        contentPanel.add(formPanel);

        // Form layout
        int labelWidth = 130, fieldWidth = 250, height = 30, spacingY = 40;
        int leftX = 40, rightX = 600;

        // Left side
        JLabel lbMaLoaiPhong = new JLabel("Mã loại phòng:");
        lbMaLoaiPhong.setBounds(leftX, 20, labelWidth, height);
        tfMaLoaiPhong = new JTextField();
        tfMaLoaiPhong.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfMaLoaiPhong.setBounds(leftX + labelWidth + 10, 20, fieldWidth, height);

        JLabel lbTenLoai = new JLabel("Tên loại:");
        lbTenLoai.setBounds(leftX, 20 + spacingY, labelWidth, height);
        tfTenLoai = new JTextField();
        tfTenLoai.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfTenLoai.setBounds(leftX + labelWidth + 10, 20 + spacingY, fieldWidth, height);

        JLabel lbSoLuong = new JLabel("Số lượng:");
        lbSoLuong.setBounds(leftX, 20 + 2 * spacingY, labelWidth, height);
        tfSoLuong = new JTextField();
        tfSoLuong.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfSoLuong.setBounds(leftX + labelWidth + 10, 20 + 2 * spacingY, fieldWidth, height);

        JLabel lbDienTich = new JLabel("Diện tích:");
        lbDienTich.setBounds(leftX, 20 + 3 * spacingY, labelWidth, height);
        tfDienTich = new JTextField();
        tfDienTich.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfDienTich.setBounds(leftX + labelWidth + 10, 20 + 3 * spacingY, fieldWidth, height);

        // Right side
        JLabel lbGiaTheoGio = new JLabel("Giá theo giờ:");
        lbGiaTheoGio.setBounds(rightX, 20, labelWidth, height);
        tfGiaTheoGio = new JTextField();
        tfGiaTheoGio.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfGiaTheoGio.setBounds(rightX + labelWidth + 10, 20, fieldWidth, height);

        JLabel lbGiaTheoNgay = new JLabel("Giá theo ngày:");
        lbGiaTheoNgay.setBounds(rightX, 20 + spacingY, labelWidth, height);
        tfGiaTheoNgay = new JTextField();
        tfGiaTheoNgay.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfGiaTheoNgay.setBounds(rightX + labelWidth + 10, 20 + spacingY, fieldWidth, height);

        JLabel lbGiaTheoDem = new JLabel("Giá theo đêm:");
        lbGiaTheoDem.setBounds(rightX, 20 + 2 * spacingY, labelWidth, height);
        tfGiaTheoDem = new JTextField();
        tfGiaTheoDem.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfGiaTheoDem.setBounds(rightX + labelWidth + 10, 20 + 2 * spacingY, fieldWidth, height);

        JLabel lbPhuThuQuaGio = new JLabel("Phụ thu quá giờ:");
        lbPhuThuQuaGio.setBounds(rightX, 20 + 3 * spacingY, labelWidth, height);
        tfPhuThuQuaGio = new JTextField();
        tfPhuThuQuaGio.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfPhuThuQuaGio.setBounds(rightX + labelWidth + 10, 20 + 3 * spacingY, fieldWidth, height);

        // Buttons (aligned on the same row, adjusted to prevent clipping)
        int buttonWidth = 170, buttonHeight = 35, buttonGap = 10;
        int buttonY = 120 + 2 * spacingY + 5;
        int buttonStartX = rightX-180;

        btnThemLoaiPhong = new JButton("Thêm loại phòng");
        btnThemLoaiPhong.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnThemLoaiPhong.setBackground(new Color(0, 255, 128));
        btnThemLoaiPhong.setBounds(buttonStartX, buttonY, buttonWidth, buttonHeight);
        btnThemLoaiPhong.addActionListener(this::handleAddLoaiPhong);

        btnSuaLoaiPhong = new JButton("Sửa loại phòng");
        btnSuaLoaiPhong.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnSuaLoaiPhong.setBackground(new Color(255, 165, 0));
        btnSuaLoaiPhong.setBounds(buttonStartX + buttonWidth + buttonGap, buttonY, buttonWidth, buttonHeight);
        btnSuaLoaiPhong.addActionListener(this::handleUpdateLoaiPhong);

        btnXoaLoaiPhong = new JButton("Xóa loại phòng");
        btnXoaLoaiPhong.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnXoaLoaiPhong.setBackground(new Color(255, 69, 0));
        btnXoaLoaiPhong.setBounds(buttonStartX + 2 * (buttonWidth + buttonGap), buttonY, buttonWidth, buttonHeight);
        btnXoaLoaiPhong.addActionListener(this::handleDeleteLoaiPhong);

        JButton btnXoaTrangLoaiPhong = new JButton("Xóa trắng");
        btnXoaTrangLoaiPhong.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnXoaTrangLoaiPhong.setBackground(new Color(128, 128, 128));
        btnXoaTrangLoaiPhong.setBounds(buttonStartX + 3 * (buttonWidth + buttonGap), buttonY, buttonWidth, buttonHeight);
        btnXoaTrangLoaiPhong.addActionListener(e -> clearLoaiPhongForm());

        // Add components to form panel
        Component[] components = {
                lbMaLoaiPhong, tfMaLoaiPhong, lbTenLoai, tfTenLoai, lbSoLuong, tfSoLuong, lbDienTich, tfDienTich,
                lbGiaTheoGio, tfGiaTheoGio, lbGiaTheoNgay, tfGiaTheoNgay, lbGiaTheoDem, tfGiaTheoDem, lbPhuThuQuaGio, tfPhuThuQuaGio,
                btnThemLoaiPhong, btnSuaLoaiPhong, btnXoaLoaiPhong, btnXoaTrangLoaiPhong
        };
        for (Component c : components) {
            formPanel.add(c);
        }

        // Table panel
        JPanel tablePanel = new JPanel(null);
        tablePanel.setBounds(30, 300, 1140, 360);
        tablePanel.setBackground(Color.WHITE);
        contentPanel.add(tablePanel);

        JLabel lblDanhSach = new JLabel("Danh sách loại phòng", SwingConstants.CENTER);
        lblDanhSach.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblDanhSach.setBounds(420, 10, 300, 30);
        tablePanel.add(lblDanhSach);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 50, 1100, 280);
        tablePanel.add(scrollPane);

        String[] columns = {"Mã loại", "Tên loại", "Số lượng", "Diện tích", "Giá/giờ", "Giá/ngày", "Giá/đêm", "Phụ thu quá giờ"};
        modelLoaiPhong = new DefaultTableModel(columns, 0);
        tableLoaiPhong = new JTable(modelLoaiPhong);
        tableLoaiPhong.setRowHeight(30);
        tableLoaiPhong.setFont(new Font("Times New Roman", Font.PLAIN, 16));

        JTableHeader header = tableLoaiPhong.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tableLoaiPhong.getColumnCount(); i++) {
            tableLoaiPhong.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tableLoaiPhong.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableLoaiPhong.getSelectedRow();
                if (selectedRow >= 0) {
                    tfMaLoaiPhong.setText(modelLoaiPhong.getValueAt(selectedRow, 0).toString());
                    tfTenLoai.setText(modelLoaiPhong.getValueAt(selectedRow, 1).toString());
                    tfSoLuong.setText(modelLoaiPhong.getValueAt(selectedRow, 2).toString());
                    tfDienTich.setText(modelLoaiPhong.getValueAt(selectedRow, 3).toString());
                    tfGiaTheoGio.setText(modelLoaiPhong.getValueAt(selectedRow, 4).toString());
                    tfGiaTheoNgay.setText(modelLoaiPhong.getValueAt(selectedRow, 5).toString());
                    tfGiaTheoDem.setText(modelLoaiPhong.getValueAt(selectedRow, 6).toString());
                    tfPhuThuQuaGio.setText(modelLoaiPhong.getValueAt(selectedRow, 7).toString());
                    tfMaLoaiPhong.setEditable(false);
                }
            }
        });

        scrollPane.setViewportView(tableLoaiPhong);
        loadDataLoaiPhong();
        return contentPanel;
    }

    private void loadLoaiPhong() {
        cbLoaiPhong.removeAllItems();
        try {
            List<LoaiPhong> list = loaiPhongDao.getAllLoaiPhongNew();
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có loại phòng nào trong hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            for (LoaiPhong lp : list) {
                cbLoaiPhong.addItem(lp);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách loại phòng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDataPhong() {
        modelPhong.setRowCount(0);
        try {
            List<Phong> list = phongDao.getAllPhongNew();
            for (Phong p : list) {
                LoaiPhong lp = p.getLoaiPhong();
                modelPhong.addRow(new Object[]{
                        p.getSoPhong(),
                        p.getTrangThai(),
                        lp.getTenLoai(),
                        String.format("%.2f", lp.getDienTich()),
                        currencyFormat.format(lp.getGiaTheoGio()),
                        currencyFormat.format(lp.getGiaTheoNgay()),
                        currencyFormat.format(lp.getGiaTheoDem()),
                        currencyFormat.format(lp.getPhuThuQuaGio()),
                        p.getMoTa()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách phòng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDataLoaiPhong() {
        modelLoaiPhong.setRowCount(0);
        try {
            List<LoaiPhong> list = loaiPhongDao.getAllLoaiPhongNew();
            for (LoaiPhong lp : list) {
                modelLoaiPhong.addRow(new Object[]{
                        lp.getMaLoaiPhong(),
                        lp.getTenLoai(),
                        lp.getSoLuong(),
                        String.format("%.2f", lp.getDienTich()),
                        currencyFormat.format(lp.getGiaTheoGio()),
                        currencyFormat.format(lp.getGiaTheoNgay()),
                        currencyFormat.format(lp.getGiaTheoDem()),
                        currencyFormat.format(lp.getPhuThuQuaGio())
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách loại phòng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAddPhong(ActionEvent e) {
        if (!validatePhongInput(true)) return;

        btnThemPhong.setEnabled(false);
        try {
            String soPhong = tfSoPhong.getText().trim();
            String trangThai = cbTrangThai.getSelectedItem().toString();
            LoaiPhong loaiPhong = (LoaiPhong) cbLoaiPhong.getSelectedItem();
            String moTa = tfMoTa.getText().trim();

            if (loaiPhong == null) {
                JOptionPane.showMessageDialog(this, "Loại phòng không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Phong phong = new Phong(soPhong, trangThai, loaiPhong, moTa);

            phongDao.themPhongNew(phong);
            modelPhong.addRow(new Object[]{
                    soPhong, trangThai, loaiPhong.getTenLoai(), String.format("%.2f", loaiPhong.getDienTich()),
                    currencyFormat.format(loaiPhong.getGiaTheoGio()), currencyFormat.format(loaiPhong.getGiaTheoNgay()),
                    currencyFormat.format(loaiPhong.getGiaTheoDem()), currencyFormat.format(loaiPhong.getPhuThuQuaGio()), moTa
            });
            clearPhongForm();
            JOptionPane.showMessageDialog(this, "Thêm phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
            btnThemPhong.setEnabled(true);
        }
    }

    private void handleUpdatePhong(ActionEvent e) {
        int selectedRow = tablePhong.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng để sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validatePhongInput(false)) return;

        btnSuaPhong.setEnabled(false);
        try {
            String soPhong = tfSoPhong.getText().trim();
            String trangThai = cbTrangThai.getSelectedItem().toString();
            LoaiPhong loaiPhong = (LoaiPhong) cbLoaiPhong.getSelectedItem();
            String moTa = tfMoTa.getText().trim();

            if (loaiPhong == null) {
                JOptionPane.showMessageDialog(this, "Loại phòng không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Phong phong = new Phong(soPhong, trangThai, loaiPhong, moTa);

            phongDao.capNhatPhongNew(phong);
            modelPhong.setValueAt(trangThai, selectedRow, 1);
            modelPhong.setValueAt(loaiPhong.getTenLoai(), selectedRow, 2);
            modelPhong.setValueAt(String.format("%.2f", loaiPhong.getDienTich()), selectedRow, 3);
            modelPhong.setValueAt(currencyFormat.format(loaiPhong.getGiaTheoGio()), selectedRow, 4);
            modelPhong.setValueAt(currencyFormat.format(loaiPhong.getGiaTheoNgay()), selectedRow, 5);
            modelPhong.setValueAt(currencyFormat.format(loaiPhong.getGiaTheoDem()), selectedRow, 6);
            modelPhong.setValueAt(currencyFormat.format(loaiPhong.getPhuThuQuaGio()), selectedRow, 7);
            modelPhong.setValueAt(moTa, selectedRow, 8);
            clearPhongForm();
            JOptionPane.showMessageDialog(this, "Sửa phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
            btnSuaPhong.setEnabled(true);
        }
    }

    private void handleDeletePhong(ActionEvent e) {
        int selectedRow = tablePhong.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnXoaPhong.setEnabled(false);
        try {
            String soPhong = modelPhong.getValueAt(selectedRow, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa phòng " + soPhong + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                phongDao.xoaPhongNew(soPhong);
                modelPhong.removeRow(selectedRow);
                clearPhongForm();
                JOptionPane.showMessageDialog(this, "Xóa phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
            btnXoaPhong.setEnabled(true);
        }
    }

    private void handleAddLoaiPhong(ActionEvent e) {
        if (!validateLoaiPhongInput()) return;

        btnThemLoaiPhong.setEnabled(false);
        try {
            LoaiPhong loaiPhong = new LoaiPhong(
                    tfMaLoaiPhong.getText().trim(),
                    tfTenLoai.getText().trim(),
                    Integer.parseInt(tfSoLuong.getText().trim()),
                    Float.parseFloat(tfDienTich.getText().trim()),
                    parseCurrency(tfGiaTheoGio.getText().trim()),
                    parseCurrency(tfGiaTheoNgay.getText().trim()),
                    parseCurrency(tfGiaTheoDem.getText().trim()),
                    parseCurrency(tfPhuThuQuaGio.getText().trim())
            );

            loaiPhongDao.themLoaiPhongNew(loaiPhong);
            modelLoaiPhong.addRow(new Object[]{
                    loaiPhong.getMaLoaiPhong(),
                    loaiPhong.getTenLoai(),
                    loaiPhong.getSoLuong(),
                    String.format("%.2f", loaiPhong.getDienTich()),
                    currencyFormat.format(loaiPhong.getGiaTheoGio()),
                    currencyFormat.format(loaiPhong.getGiaTheoNgay()),
                    currencyFormat.format(loaiPhong.getGiaTheoDem()),
                    currencyFormat.format(loaiPhong.getPhuThuQuaGio())
            });
            clearLoaiPhongForm();
            loadLoaiPhong();
            loadDataPhong();
            JOptionPane.showMessageDialog(this, "Thêm loại phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
            btnThemLoaiPhong.setEnabled(true);
        }
    }

    private void handleUpdateLoaiPhong(ActionEvent e) {
        int selectedRow = tableLoaiPhong.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một loại phòng để sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateLoaiPhongInput()) return;

        btnSuaLoaiPhong.setEnabled(false);
        try {
            LoaiPhong loaiPhong = new LoaiPhong(
                    tfMaLoaiPhong.getText().trim(),
                    tfTenLoai.getText().trim(),
                    Integer.parseInt(tfSoLuong.getText().trim()),
                    Float.parseFloat(tfDienTich.getText().trim()),
                    parseCurrency(tfGiaTheoGio.getText().trim()),
                    parseCurrency(tfGiaTheoNgay.getText().trim()),
                    parseCurrency(tfGiaTheoDem.getText().trim()),
                    parseCurrency(tfPhuThuQuaGio.getText().trim())
            );

            loaiPhongDao.suaLoaiPhongNew(loaiPhong);
            modelLoaiPhong.setValueAt(loaiPhong.getMaLoaiPhong(), selectedRow, 0);
            modelLoaiPhong.setValueAt(loaiPhong.getTenLoai(), selectedRow, 1);
            modelLoaiPhong.setValueAt(loaiPhong.getSoLuong(), selectedRow, 2);
            modelLoaiPhong.setValueAt(String.format("%.2f", loaiPhong.getDienTich()), selectedRow, 3);
            modelLoaiPhong.setValueAt(currencyFormat.format(loaiPhong.getGiaTheoGio()), selectedRow, 4);
            modelLoaiPhong.setValueAt(currencyFormat.format(loaiPhong.getGiaTheoNgay()), selectedRow, 5);
            modelLoaiPhong.setValueAt(currencyFormat.format(loaiPhong.getGiaTheoDem()), selectedRow, 6);
            modelLoaiPhong.setValueAt(currencyFormat.format(loaiPhong.getPhuThuQuaGio()), selectedRow, 7);
            clearLoaiPhongForm();
            loadLoaiPhong();
            loadDataPhong();
            JOptionPane.showMessageDialog(this, "Sửa loại phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
            btnSuaLoaiPhong.setEnabled(true);
        }
    }

    private void handleDeleteLoaiPhong(ActionEvent e) {
        int selectedRow = tableLoaiPhong.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một loại phòng để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnXoaLoaiPhong.setEnabled(false);
        try {
            String maLoaiPhong = modelLoaiPhong.getValueAt(selectedRow, 0).toString();
            if (loaiPhongDao.existsPhongByLoaiPhong(maLoaiPhong)) {
                JOptionPane.showMessageDialog(this, "Không thể xóa loại phòng vì có phòng đang sử dụng! Vui lòng xóa các phòng liên quan trước.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa loại phòng " + maLoaiPhong + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                loaiPhongDao.xoaLoaiPhongNew(maLoaiPhong);
                modelLoaiPhong.removeRow(selectedRow);
                clearLoaiPhongForm();
                loadLoaiPhong();
                loadDataPhong();
                JOptionPane.showMessageDialog(this, "Xóa loại phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
            btnXoaLoaiPhong.setEnabled(true);
        }
    }

    private boolean validatePhongInput(boolean isAdd) {
        String soPhong = tfSoPhong.getText().trim();
        if (soPhong.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số phòng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            tfSoPhong.requestFocus();
            return false;
        }

        // Check room code format (P followed by 3 digits)
        if (!soPhongPattern.matcher(soPhong).matches()) {
            JOptionPane.showMessageDialog(this, "Số phòng phải có định dạng Pxxx (P theo sau là 3 chữ số, ví dụ: P001)!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            tfSoPhong.requestFocus();
            return false;
        }

        // Check for duplicate room code
        try {
            String excludeSoPhong = isAdd ? null : modelPhong.getValueAt(tablePhong.getSelectedRow(), 0).toString();
            if (phongDao.existsSoPhong(soPhong, excludeSoPhong)) {
                JOptionPane.showMessageDialog(this, "Số phòng đã tồn tại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                tfSoPhong.requestFocus();
                return false;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra số phòng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (cbLoaiPhong.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            cbLoaiPhong.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateLoaiPhongInput() {
        if (tfMaLoaiPhong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã loại phòng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            tfMaLoaiPhong.requestFocus();
            return false;
        }
        if (tfTenLoai.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên loại phòng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            tfTenLoai.requestFocus();
            return false;
        }
        try {
            String tenLoai = tfTenLoai.getText().trim();
            String maLoaiPhong = tfMaLoaiPhong.getText().trim();
            if (loaiPhongDao.existsTenLoaiPhong(tenLoai, maLoaiPhong)) {
                JOptionPane.showMessageDialog(this, "Tên loại phòng đã tồn tại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                tfTenLoai.requestFocus();
                return false;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra tên loại phòng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            int soLuong = Integer.parseInt(tfSoLuong.getText().trim());
            if (soLuong < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải không âm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                tfSoLuong.requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            tfSoLuong.requestFocus();
            return false;
        }
        try {
            float dienTich = Float.parseFloat(tfDienTich.getText().trim());
            if (dienTich <= 0) {
                JOptionPane.showMessageDialog(this, "Diện tích phải lớn hơn 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                tfDienTich.requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Diện tích phải là số thực!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            tfDienTich.requestFocus();
            return false;
        }
        try {
            double giaTheoGio = parseCurrency(tfGiaTheoGio.getText().trim());
            if (giaTheoGio < 0) {
                JOptionPane.showMessageDialog(this, "Giá theo giờ phải không âm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                tfGiaTheoGio.requestFocus();
                return false;
            }
            double giaTheoNgay = parseCurrency(tfGiaTheoNgay.getText().trim());
            if (giaTheoNgay < 0) {
                JOptionPane.showMessageDialog(this, "Giá theo ngày phải không âm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                tfGiaTheoNgay.requestFocus();
                return false;
            }
            if (giaTheoNgay < giaTheoGio) {
                JOptionPane.showMessageDialog(this, "Giá theo ngày phải lớn hơn hoặc bằng giá theo giờ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                tfGiaTheoNgay.requestFocus();
                return false;
            }
            double giaTheoDem = parseCurrency(tfGiaTheoDem.getText().trim());
            if (giaTheoDem < 0) {
                JOptionPane.showMessageDialog(this, "Giá theo đêm phải không âm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                tfGiaTheoDem.requestFocus();
                return false;
            }
            double phuThuQuaGio = parseCurrency(tfPhuThuQuaGio.getText().trim());
            if (phuThuQuaGio < 0) {
                JOptionPane.showMessageDialog(this, "Phụ thu quá giờ phải không âm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                tfPhuThuQuaGio.requestFocus();
                return false;
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Giá phải là số thực hợp lệ: " + ex.getMessage(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private double parseCurrency(String input) throws ParseException {
        if (input == null || input.trim().isEmpty()) {
            throw new ParseException("Giá không được để trống", 0);
        }
        String cleanedInput = input.replaceAll("[^0-9,.]", "").replace(",", ".");
        try {
            return NumberFormat.getNumberInstance(Locale.US).parse(cleanedInput).doubleValue();
        } catch (ParseException ex) {
            throw new ParseException("Định dạng giá không hợp lệ: " + input, 0);
        }
    }

    private void clearPhongForm() {
        tfSoPhong.setText("");
        cbTrangThai.setSelectedIndex(0);
        if (cbLoaiPhong.getItemCount() > 0) {
            cbLoaiPhong.setSelectedIndex(0);
        }
        tfMoTa.setText("");
        tfSoPhong.setEditable(true);
        tablePhong.clearSelection();
        tfSoPhong.requestFocus();
    }

    private void clearLoaiPhongForm() {
        tfMaLoaiPhong.setText("");
        tfTenLoai.setText("");
        tfSoLuong.setText("");
        tfDienTich.setText("");
        tfGiaTheoGio.setText("");
        tfGiaTheoNgay.setText("");
        tfGiaTheoDem.setText("");
        tfPhuThuQuaGio.setText("");
        tfMaLoaiPhong.setEditable(true);
        tableLoaiPhong.clearSelection();
        tfMaLoaiPhong.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý phòng");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1220, 720);
            frame.setContentPane(new QuanLyPhong_Panel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}