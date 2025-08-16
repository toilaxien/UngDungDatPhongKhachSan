// File: PanelThongKeTheoThang.java
package quanLy;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import connectDB.ConnectDB;

public class PanelThongKeTheoThang extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTongDoanhThu;
    private JComboBox<Integer> cboThang, cboNam;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    JScrollPane tablePanel;
	private JPanel chartPanel;
    private final DecimalFormat currencyFormat = new DecimalFormat("#,### VND");
    private final DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private YearMonth selectedYearMonth;
    private double tongDoanhThuThang;

    public PanelThongKeTheoThang() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 20, 15, 20));
        setBackground(Color.WHITE);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);

        YearMonth now = YearMonth.now();
        selectedYearMonth = now;
        cboThang.setSelectedIndex(now.getMonthValue() - 1);
        cboNam.setSelectedItem(now.getYear());
        loadData(now);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setOpaque(false);

        left.add(new JLabel("Chọn tháng:"));

        cboThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cboThang.addItem(i);
        left.add(cboThang);

        cboNam = new JComboBox<>();
        int year = LocalDate.now().getYear();
        for (int i = year - 5; i <= year + 1; i++) cboNam.addItem(i);
        left.add(cboNam);

        ActionListener changeListener = e -> {
            int thang = (int) cboThang.getSelectedItem();
            int nam = (int) cboNam.getSelectedItem();
            selectedYearMonth = YearMonth.of(nam, thang);
            loadData(selectedYearMonth);
        };

        cboThang.addActionListener(changeListener);
        cboNam.addActionListener(changeListener);

        panel.add(left, BorderLayout.WEST);

        lblTongDoanhThu = new JLabel("Tổng: 0 VND");
        lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongDoanhThu.setForeground(new Color(0, 128, 0));
        panel.add(lblTongDoanhThu, BorderLayout.EAST);

        return panel;
    }

    private JPanel createContentPanel() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);

        tablePanel = createTablePanel();
        chartPanel = createChartPanel();

        contentPanel.add(tablePanel, "TABLE");
        contentPanel.add(chartPanel, "CHART");

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JPanel tabButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tabButtons.setOpaque(false);

        JButton btnTable = new JButton("Bảng dữ liệu");
        JButton btnChart = new JButton("Biểu đồ");

        btnTable.addActionListener(e -> cardLayout.show(contentPanel, "TABLE"));
        btnChart.addActionListener(e -> cardLayout.show(contentPanel, "CHART"));

        tabButtons.add(btnTable);
        tabButtons.add(btnChart);

        wrapper.add(tabButtons, BorderLayout.NORTH);
        wrapper.add(contentPanel, BorderLayout.CENTER);

        return wrapper;
    }

    private JScrollPane createTablePanel() {
        String[] columns = {"Mã đơn đặt phòng", "Ngày trả phòng", "Tổng thanh toán sau áp dụng"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(2).setCellRenderer(rightAlign);

        return new JScrollPane(table);
    }

    private JPanel createChartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(0, 300));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            new EmptyBorder(10, 10, 10, 10)));

        JPanel chartCanvas = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawChart((Graphics2D) g);
            }
        };
        chartCanvas.setPreferredSize(new Dimension(500, 250));
        chartCanvas.setBackground(Color.WHITE);
        panel.add(chartCanvas, BorderLayout.CENTER);
        return panel;
    }

    private void loadData(YearMonth ym) {
        model.setRowCount(0);
        tongDoanhThuThang = 0;

        String query = "SELECT ddp.MaDonDatPhong, ddp.NgayTraPhong, " +
                "ISNULL(SUM(ctad.tongThanhToanSauApDung), 0) AS TongThanhToanSauApDung " +
                "FROM DonDatPhong ddp " +
                "LEFT JOIN ChiTietApDung ctad ON ddp.MaDonDatPhong = ctad.MaDonDatPhong " +
                "WHERE MONTH(ddp.NgayTraPhong) = ? AND YEAR(ddp.NgayTraPhong) = ? " +
                "GROUP BY ddp.MaDonDatPhong, ddp.NgayTraPhong";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, ym.getMonthValue());
            stmt.setInt(2, ym.getYear());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String ma = rs.getString("MaDonDatPhong");
                String ngay = displayFormat.format(rs.getDate("NgayTraPhong").toLocalDate());
                double tien = rs.getDouble("TongThanhToanSauApDung");
                tongDoanhThuThang += tien;
                model.addRow(new Object[]{ma, ngay, currencyFormat.format(tien)});
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn: " + e.getMessage());
        }

        lblTongDoanhThu.setText("Tổng: " + currencyFormat.format(tongDoanhThuThang));
        chartPanel.repaint();
    }

    private void drawChart(Graphics2D g2) {
        YearMonth ym = selectedYearMonth;

        ArrayList<Double> doanhThuList = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        try (Connection conn = ConnectDB.getConnection()) {
            for (int i = -4; i <= 0; i++) {
                YearMonth ymt = ym.plusMonths(i);
                String sql = "SELECT ISNULL(SUM(ctad.tongThanhToanSauApDung), 0) AS Tong FROM DonDatPhong ddp " +
                        "LEFT JOIN ChiTietApDung ctad ON ddp.MaDonDatPhong = ctad.MaDonDatPhong " +
                        "WHERE MONTH(ddp.NgayTraPhong) = ? AND YEAR(ddp.NgayTraPhong) = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, ymt.getMonthValue());
                    ps.setInt(2, ymt.getYear());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        doanhThuList.add(rs.getDouble("Tong"));
                        labels.add(ymt.getMonthValue() + "/" + ymt.getYear());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        int width = chartPanel.getWidth();
        int height = chartPanel.getHeight();
        int barWidth = width / 8;
        int max = doanhThuList.stream().mapToInt(Double::intValue).max().orElse(1);
        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < doanhThuList.size(); i++) {
            int barHeight = (int) ((doanhThuList.get(i) * 1.0 / max) * (height - 60));
            int x = 40 + i * (barWidth + 20);
            int y = height - barHeight - 30;
            g2.setColor((i == 4) ? new Color(255, 100, 100) : new Color(100, 150, 255));
            g2.fillRect(x, y, barWidth, barHeight);

            g2.setColor(Color.BLACK);
            String text = currencyFormat.format(doanhThuList.get(i));
            int tw = fm.stringWidth(text);
            g2.drawString(text, x + (barWidth - tw) / 2, y - 5);

            String label = labels.get(i);
            int lw = fm.stringWidth(label);
            g2.drawString(label, x + (barWidth - lw) / 2, height - 10);
        }
    }
}
