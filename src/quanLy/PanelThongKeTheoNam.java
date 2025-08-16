// File: PanelThongKeTheoNam.java
package quanLy;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import connectDB.ConnectDB;

public class PanelThongKeTheoNam extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTongDoanhThu;
    private JComboBox<Integer> cboNam;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    JScrollPane tablePanel;
    private JPanel chartPanel;
    private final DecimalFormat currencyFormat = new DecimalFormat("#,### VND");
    private int selectedYear;
    private long tongDoanhThuNam;

    public PanelThongKeTheoNam() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 20, 15, 20));
        setBackground(Color.WHITE);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);

        selectedYear = LocalDate.now().getYear();
        cboNam.setSelectedItem(selectedYear);
        loadData(selectedYear);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setOpaque(false);

        left.add(new JLabel("Chọn năm:"));
        cboNam = new JComboBox<>();
        int year = LocalDate.now().getYear();
        for (int i = year - 5; i <= year + 1; i++) cboNam.addItem(i);
        left.add(cboNam);

        cboNam.addActionListener(e -> {
            selectedYear = (int) cboNam.getSelectedItem();
            loadData(selectedYear);
        });

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

        DefaultTableCellRenderer centerAlign = new DefaultTableCellRenderer();
        centerAlign.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);

        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(0).setCellRenderer(centerAlign);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setCellRenderer(centerAlign);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
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

    private void loadData(int year) {
        model.setRowCount(0);
        tongDoanhThuNam = 0;

        String query = "SELECT ddp.MaDonDatPhong, ddp.NgayTraPhong, " +
                "ISNULL(SUM(ctad.tongThanhToanSauApDung), 0) AS TongThanhToanSauApDung " +
                "FROM DonDatPhong ddp " +
                "LEFT JOIN ChiTietApDung ctad ON ddp.MaDonDatPhong = ctad.MaDonDatPhong " +
                "WHERE YEAR(ddp.NgayTraPhong) = ? " +
                "GROUP BY ddp.MaDonDatPhong, ddp.NgayTraPhong";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String ma = rs.getString("MaDonDatPhong");
                String ngay = rs.getDate("NgayTraPhong").toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                long tien = rs.getLong("TongThanhToanSauApDung");
                tongDoanhThuNam += tien;
                model.addRow(new Object[]{ma, ngay, currencyFormat.format(tien)});
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn: " + e.getMessage());
        }

        lblTongDoanhThu.setText("Tổng: " + currencyFormat.format(tongDoanhThuNam));
        chartPanel.repaint();
    }

    private void drawChart(Graphics2D g2) {
        int year = selectedYear;
        ArrayList<Long> doanhThuList = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        try (Connection conn = ConnectDB.getConnection()) {
            for (int i = -4; i <= 0; i++) {
                int y = year + i;
                String sql = "SELECT ISNULL(SUM(ctad.tongThanhToanSauApDung), 0) AS Tong FROM DonDatPhong ddp " +
                        "LEFT JOIN ChiTietApDung ctad ON ddp.MaDonDatPhong = ctad.MaDonDatPhong " +
                        "WHERE YEAR(ddp.NgayTraPhong) = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, y);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        doanhThuList.add(rs.getLong("Tong"));
                        labels.add(String.valueOf(y));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        int width = chartPanel.getWidth();
        int height = chartPanel.getHeight();
        int margin = 40;
        int spacing = 20;
        int totalBars = doanhThuList.size();
        int availableWidth = width - 2 * margin - spacing * (totalBars - 1);
        int barWidth = availableWidth / totalBars;
        long max = doanhThuList.stream().mapToLong(Long::longValue).max().orElse(1);

        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < doanhThuList.size(); i++) {
            int barHeight = (int) ((doanhThuList.get(i) * 1.0 / max) * (height - 60));
            int x = margin + i * (barWidth + spacing);
            int y = height - barHeight - 40;

            g2.setColor(i == doanhThuList.size() - 1 ? new Color(255, 100, 100) : new Color(100, 150, 255));
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
