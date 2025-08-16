package doiPhong;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import dao.ChiTietSuDungPhong_Dao;
import dao.DonDatPhong_Dao;
import entity.ChiTietSuDungPhong;
import entity.DonDatPhong;

public class DialogPhongDangO extends JDialog {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTimKiem;

    public DialogPhongDangO(Frame owner) {
        super(owner, "Danh sách phòng đang ở", true);
        setSize(950, 550);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Font fontTitle = new Font("Times New Roman", Font.BOLD, 24);
        Font fontLabel = new Font("Times New Roman", Font.PLAIN, 18);
        Font fontButton = new Font("Times New Roman", Font.BOLD, 16);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Tìm kiếm phòng đang ở", SwingConstants.CENTER);
        lblTitle.setFont(fontTitle);
        topPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        searchPanel.setBackground(Color.WHITE);

        JLabel lblSoPhong = new JLabel("Số phòng:");
        lblSoPhong.setFont(fontLabel);

        txtTimKiem = new JTextField(15);
        txtTimKiem.setFont(fontLabel);

        JButton btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(fontButton);
        btnTimKiem.setBackground(new Color(0, 123, 255));
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.setOpaque(true);
        btnTimKiem.setContentAreaFilled(true);
        btnTimKiem.setBorderPainted(false);
        btnTimKiem.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        searchPanel.add(lblSoPhong);
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"Mã đơn", "Số phòng", "Ngày nhận", "Ngày trả", "Hành động"};
        model = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        table = new JTable(model);
        table.setFont(fontLabel);
        table.setRowHeight(30);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Times New Roman", Font.BOLD, 18));
        header.setPreferredSize(new Dimension(header.getWidth(), 30));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getColumn("Hành động").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> (Component) value);
        table.getColumn("Hành động").setCellEditor(new ButtonEditor());

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        btnTimKiem.addActionListener(e -> {
            String soPhongCanTim = txtTimKiem.getText().trim();
            taiDuLieuPhongDangO(soPhongCanTim.isEmpty() ? null : soPhongCanTim);
        });

        taiDuLieuPhongDangO(null);
    }

    private void taiDuLieuPhongDangO(String soPhongTim) {
        model.setRowCount(0);
        ChiTietSuDungPhong_Dao ctDAO = new ChiTietSuDungPhong_Dao();
        DonDatPhong_Dao donDao = new DonDatPhong_Dao();
        ArrayList<ChiTietSuDungPhong> danhSach = ctDAO.getDanhSachPhongDangO();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (ChiTietSuDungPhong ct : danhSach) {
            if (soPhongTim != null && !ct.getSoPhong().equalsIgnoreCase(soPhongTim))
                continue;

            DonDatPhong don = donDao.getDonDatPhongTheoMa(ct.getMaDonDatPhong());
            if (don != null && "Nhận phòng".equals(don.getTrangThai())) {
                JButton btnDoiPhong = new JButton("Đổi phòng");
                btnDoiPhong.setFont(new Font("Times New Roman", Font.BOLD, 14));
                btnDoiPhong.setBackground(new Color(255, 153, 0));
                btnDoiPhong.setForeground(Color.WHITE);
                btnDoiPhong.setFocusPainted(false);
                btnDoiPhong.setOpaque(true);
                btnDoiPhong.setContentAreaFilled(true);
                btnDoiPhong.setBorderPainted(false);
                btnDoiPhong.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                btnDoiPhong.addActionListener(e -> {
                    new DoiPhongDialog(this, ct.getMaDonDatPhong(), ct.getSoPhong(), ct.getNgayKetThuc()).setVisible(true);
                    taiDuLieuPhongDangO(null); // reload
                });

                model.addRow(new Object[]{
                        ct.getMaDonDatPhong(),
                        ct.getSoPhong(),
                        ct.getNgayBatDau().format(dtf),
                        ct.getNgayKetThuc().format(dtf),
                        btnDoiPhong
                });
            }
        }
    }

    public static void showDialog(Frame owner) {
        DialogPhongDangO dialog = new DialogPhongDangO(owner);
        dialog.setVisible(true);
    }
}