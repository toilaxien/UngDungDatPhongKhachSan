package quanLy;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import dao.DichVu_Dao;
import dao.LoaiDichVu_Dao;
import entity.DichVu;
import entity.LoaiDichVu;

public class QuanLyDichVu_Panel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfMaDV, tfTenDV, tfMoTa, tfGiaDV;
    private JComboBox<LoaiDichVu> cbLoaiDV;
    private DichVu_Dao dichVuDao = new DichVu_Dao();
    private LoaiDichVu_Dao loaiDichVuDao = new LoaiDichVu_Dao();
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public QuanLyDichVu_Panel() {
        // Ensure no decimal places for whole VND amounts
        currencyFormat.setMaximumFractionDigits(0);
        
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(240, 240, 240));
        add(contentPanel, BorderLayout.CENTER);

        JLabel lblTitle = new JLabel("Quản lý dịch vụ");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblTitle.setBounds(450, 0, 300, 30);
        contentPanel.add(lblTitle);

        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(30, 40, 1140, 250);
        formPanel.setBackground(Color.WHITE);
        contentPanel.add(formPanel);

        // Form fields
        int labelWidth = 170, fieldWidth = 250, height = 30, spacingY = 40;
        int leftX = 40, rightX = 600;

        Font fontChuan = new Font("Times New Roman", Font.PLAIN, 18);
        Font fontDam = new Font("Times New Roman", Font.BOLD, 18);

        JLabel lbMaDV = new JLabel("Mã dịch vụ:");
        lbMaDV.setFont(fontDam);
        lbMaDV.setBounds(leftX, 20, labelWidth, height);

        tfMaDV = new JTextField();
        tfMaDV.setFont(fontChuan);
        tfMaDV.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfMaDV.setBounds(leftX + labelWidth + 10, 20, fieldWidth, height);

        JLabel lbTenDV = new JLabel("Tên dịch vụ:");
        lbTenDV.setFont(fontDam);
        lbTenDV.setBounds(leftX, 20 + spacingY, labelWidth, height);

        tfTenDV = new JTextField();
        tfTenDV.setFont(fontChuan);
        tfTenDV.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfTenDV.setBounds(leftX + labelWidth + 10, 20 + spacingY, fieldWidth, height);

        JLabel lbMoTa = new JLabel("Mô tả:");
        lbMoTa.setFont(fontDam);
        lbMoTa.setBounds(leftX, 20 + 2 * spacingY, labelWidth, height);

        tfMoTa = new JTextField();
        tfMoTa.setFont(fontChuan);
        tfMoTa.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfMoTa.setBounds(leftX + labelWidth + 10, 20 + 2 * spacingY, fieldWidth, height);

        JLabel lbGiaDV = new JLabel("Giá dịch vụ:");
        lbGiaDV.setFont(fontDam);
        lbGiaDV.setBounds(rightX, 20, labelWidth, height);

        tfGiaDV = new JTextField();
        tfGiaDV.setFont(fontChuan);
        tfGiaDV.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        tfGiaDV.setBounds(rightX + labelWidth + 10, 20, fieldWidth, height);

        JLabel lbLoaiDV = new JLabel("Loại dịch vụ:");
        lbLoaiDV.setFont(fontDam);
        lbLoaiDV.setBounds(rightX, 20 + spacingY, labelWidth, height);

        cbLoaiDV = new JComboBox<>();
        cbLoaiDV.setFont(fontChuan);
        cbLoaiDV.setBounds(rightX + labelWidth + 10, 20 + spacingY, fieldWidth, height);
        loadLoaiDichVu();


        // Buttons
        int buttonWidth = 170, buttonHeight = 35, buttonGap = 10; // Adjusted buttonWidth to 130
        int buttonY = 80 + 3 * spacingY;
        int buttonStartX = rightX-180;

        JButton btnThem = new JButton("Thêm dịch vụ");
        btnThem.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnThem.setBackground(new Color(0, 255, 128)); // Màu xanh lá tươi
        btnThem.setOpaque(true);
        btnThem.setContentAreaFilled(true);
        btnThem.setBorderPainted(false);
        btnThem.setBounds(buttonStartX, buttonY, buttonWidth, buttonHeight);
        btnThem.addActionListener(this::handleAdd);

        JButton btnSua = new JButton("Sửa dịch vụ");
        btnSua.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnSua.setBackground(new Color(255, 165, 0)); // Màu cam nhạt
        btnSua.setOpaque(true);
        btnSua.setContentAreaFilled(true);
        btnSua.setBorderPainted(false);
        btnSua.setBounds(buttonStartX + buttonWidth + buttonGap, buttonY, buttonWidth, buttonHeight);
        btnSua.addActionListener(this::handleUpdate);

        JButton btnXoa = new JButton("Xóa dịch vụ");
        btnXoa.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnXoa.setBackground(new Color(255, 69, 0)); // Màu đỏ
        btnXoa.setOpaque(true);
        btnXoa.setContentAreaFilled(true);
        btnXoa.setBorderPainted(false);
        btnXoa.setBounds(buttonStartX + 2 * (buttonWidth + buttonGap), buttonY, buttonWidth, buttonHeight);
        btnXoa.addActionListener(this::handleDelete);

        JButton btnXoaTrang = new JButton("Xóa trắng");
        btnXoaTrang.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnXoaTrang.setBackground(new Color(128, 128, 128)); // Màu xám
        btnXoaTrang.setOpaque(true);
        btnXoaTrang.setContentAreaFilled(true);
        btnXoaTrang.setBorderPainted(false);
        btnXoaTrang.setBounds(buttonStartX + 3 * (buttonWidth + buttonGap), buttonY, buttonWidth, buttonHeight);
        btnXoaTrang.addActionListener(e -> clearForm());


        Component[] components = {
            lbMaDV, tfMaDV, lbTenDV, tfTenDV, lbMoTa, tfMoTa,
            lbGiaDV, tfGiaDV, lbLoaiDV, cbLoaiDV, btnThem, btnSua, btnXoa, btnXoaTrang // Added new button
        };
        for (Component c : components) formPanel.add(c);

        // Table panel
        JPanel tablePanel = new JPanel(null);
        tablePanel.setBounds(30, 300, 1140, 360);
        tablePanel.setBackground(Color.WHITE);
        contentPanel.add(tablePanel);

        JLabel lblDanhSach = new JLabel("Danh sách dịch vụ");
        lblDanhSach.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblDanhSach.setBounds(420, 10, 300, 30);
        tablePanel.add(lblDanhSach);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 50, 1100, 280);
        tablePanel.add(scrollPane);

        String[] columns = {"Mã DV", "Tên DV", "Mô tả", "Giá DV", "Loại DV"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 16));

        // Set column widths for better visibility
        table.getColumnModel().getColumn(0).setPreferredWidth(100); // Mã DV
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Tên DV
        table.getColumnModel().getColumn(2).setPreferredWidth(300); // Mô tả
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Giá DV
        table.getColumnModel().getColumn(4).setPreferredWidth(150); // Loại DV

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    tfMaDV.setText(model.getValueAt(selectedRow, 0).toString());
                    tfTenDV.setText(model.getValueAt(selectedRow, 1).toString());
                    tfMoTa.setText(model.getValueAt(selectedRow, 2) != null ? model.getValueAt(selectedRow, 2).toString() : "");
                    // Format giaDV for display in text field
                    String giaDVStr = model.getValueAt(selectedRow, 3).toString();
                    try {
                        double giaDV = currencyFormat.parse(giaDVStr).doubleValue();
                        tfGiaDV.setText(currencyFormat.format(giaDV));
                    } catch (Exception ex) {
                        tfGiaDV.setText(giaDVStr); // Fallback
                    }
                    String tenLoai = model.getValueAt(selectedRow, 4).toString();
                    for (int i = 0; i < cbLoaiDV.getItemCount(); i++) {
                        if (cbLoaiDV.getItemAt(i).getTenLoai().equals(tenLoai)) {
                            cbLoaiDV.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scrollPane.setViewportView(table);
        loadData();
    }

    private void loadLoaiDichVu() {
        List<LoaiDichVu> list = loaiDichVuDao.getAllLoaiDichVu();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có loại dịch vụ nào trong hệ thống!");
        }
        for (LoaiDichVu loai : list) {
            cbLoaiDV.addItem(loai);
        }
        cbLoaiDV.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof LoaiDichVu) {
                    setText(((LoaiDichVu) value).getTenLoai());
                }
                return this;
            }
        });
    }

    private void loadData() {
        model.setRowCount(0);
        List<DichVu> list = dichVuDao.getAllDichVu1();
        for (DichVu dv : list) {
            model.addRow(new Object[]{
                dv.getMaDV(),
                dv.getTenDV(),
                dv.getMoTa(),
                currencyFormat.format(dv.getGiaDV()),
                dv.getLoaiDichVu().getTenLoai() != null ? dv.getLoaiDichVu().getTenLoai() : "N/A"
            });
        }
    }

    private void handleAdd(ActionEvent e) {
        try {
            String maDV = tfMaDV.getText().trim();
            String tenDV = tfTenDV.getText().trim();
            String moTa = tfMoTa.getText().trim();
            String giaDVText = tfGiaDV.getText().trim();
            LoaiDichVu loaiDV = (LoaiDichVu) cbLoaiDV.getSelectedItem();

            // Input validation
            if (maDV.isEmpty() || tenDV.isEmpty() || giaDVText.isEmpty() || loaiDV == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin bắt buộc!");
                return;
            }

            if (!maDV.matches("^DV.*")) {
                JOptionPane.showMessageDialog(this, "Mã dịch vụ phải bắt đầu bằng 'DV'!");
                return;
            }

            if (maDV.length() > 20) {
                JOptionPane.showMessageDialog(this, "Mã dịch vụ không được vượt quá 20 ký tự!");
                return;
            }

            if (dichVuDao.timTheoMaDV(maDV) != null) {
                JOptionPane.showMessageDialog(this, "Mã dịch vụ đã tồn tại!");
                return;
            }

            double giaDV;
            try {
                // Remove non-numeric characters (e.g., commas, ₫)
                String cleanGiaDV = giaDVText.replaceAll("[^0-9]", "");
                giaDV = Double.parseDouble(cleanGiaDV);
                if (giaDV < 0) {
                    JOptionPane.showMessageDialog(this, "Giá dịch vụ không thể âm!");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá dịch vụ phải là số hợp lệ!");
                return;
            }

            DichVu dv = new DichVu(maDV, tenDV, moTa, giaDV, loaiDV);
            if (dichVuDao.them(dv)) {
                model.addRow(new Object[]{
                    maDV,
                    tenDV,
                    moTa,
                    currencyFormat.format(giaDV),
                    loaiDV.getTenLoai() != null ? loaiDV.getTenLoai() : "N/A"
                });
                JOptionPane.showMessageDialog(this, "Thêm dịch vụ thành công!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại (loại dịch vụ không hợp lệ?)");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm dịch vụ: " + ex.getMessage());
        }
    }

    private void handleUpdate(ActionEvent e) {
        try {
            String maDV = tfMaDV.getText().trim();
            String tenDV = tfTenDV.getText().trim();
            String moTa = tfMoTa.getText().trim();
            String giaDVText = tfGiaDV.getText().trim();
            LoaiDichVu loaiDV = (LoaiDichVu) cbLoaiDV.getSelectedItem();

            // Input validation
            if (maDV.isEmpty() || tenDV.isEmpty() || giaDVText.isEmpty() || loaiDV == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin bắt buộc!");
                return;
            }

            if (!maDV.matches("^DV.*")) {
                JOptionPane.showMessageDialog(this, "Mã dịch vụ phải bắt đầu bằng 'DV'!");
                return;
            }

            if (maDV.length() > 20) {
                JOptionPane.showMessageDialog(this, "Mã dịch vụ không được vượt quá 20 ký tự!");
                return;
            }

            if (dichVuDao.timTheoMaDV(maDV) == null) {
                JOptionPane.showMessageDialog(this, "Mã dịch vụ không tồn tại!");
                return;
            }

            double giaDV;
            try {
                // Remove non-numeric characters (e.g., commas, ₫)
                String cleanGiaDV = giaDVText.replaceAll("[^0-9]", "");
                giaDV = Double.parseDouble(cleanGiaDV);
                if (giaDV < 0) {
                    JOptionPane.showMessageDialog(this, "Giá dịch vụ không thể âm!");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá dịch vụ phải là số hợp lệ!");
                return;
            }

            DichVu dv = new DichVu(maDV, tenDV, moTa, giaDV, loaiDV);
            if (dichVuDao.sua(dv)) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    model.setValueAt(tenDV, selectedRow, 1);
                    model.setValueAt(moTa, selectedRow, 2);
                    model.setValueAt(currencyFormat.format(giaDV), selectedRow, 3);
                    model.setValueAt(loaiDV.getTenLoai() != null ? loaiDV.getTenLoai() : "N/A", selectedRow, 4);
                }
                JOptionPane.showMessageDialog(this, "Sửa dịch vụ thành công!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Sửa thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa dịch vụ: " + ex.getMessage());
        }
    }

    private void handleDelete(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dịch vụ để xóa!");
            return;
        }

        String maDV = model.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa dịch vụ " + maDV + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (dichVuDao.xoa(maDV)) {
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Xóa dịch vụ thành công!");
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại (dịch vụ đang được sử dụng?)");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa dịch vụ: " + ex.getMessage());
            }
        }
    }

    private void clearForm() {
        tfMaDV.setText("");
        tfTenDV.setText("");
        tfMoTa.setText("");
        tfGiaDV.setText("");
        cbLoaiDV.setSelectedIndex(cbLoaiDV.getItemCount() > 0 ? 0 : -1);
        table.clearSelection();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Quản lý dịch vụ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1220, 720);
        frame.setContentPane(new QuanLyDichVu_Panel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}