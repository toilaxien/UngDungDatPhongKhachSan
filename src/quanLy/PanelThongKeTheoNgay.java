// File: PanelThongKeTheoNgay.java
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
import org.jdatepicker.impl.*;
import connectDB.ConnectDB;

public class PanelThongKeTheoNgay extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTongDoanhThu;
    private JDatePickerImpl datePicker;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    JScrollPane tablePanel;
    private JPanel chartPanel;
    private final DecimalFormat currencyFormat = new DecimalFormat("#,### VND");
    private final DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private LocalDate currentSelectedDate;

    public PanelThongKeTheoNgay() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 20, 15, 20));
        setBackground(Color.WHITE);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);

        LocalDate today = LocalDate.now();
        currentSelectedDate = today;
        datePicker.getModel().setDate(today.getYear(), today.getMonthValue() - 1, today.getDayOfMonth());
        datePicker.getModel().setSelected(true);
        loadData(today);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setOpaque(false);

        JLabel lbl = new JLabel("Chọn ngày:");
        left.add(lbl);

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hôm nay");
        p.put("text.month", "Tháng");
        p.put("text.year", "Năm");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        datePicker.addActionListener(e -> {
            Object selectedObj = datePicker.getModel().getValue();
            if (selectedObj instanceof java.util.Date) {
                java.util.Date utilDate = (java.util.Date) selectedObj;
                LocalDate selected = utilDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                currentSelectedDate = selected;
                loadData(selected);
            }
        });

        left.add(datePicker);
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

    private void loadData(LocalDate date) {
        model.setRowCount(0);
        double total = 0;

        String query = "SELECT ddp.MaDonDatPhong, ddp.NgayTraPhong, " +
                "ISNULL(SUM(ctad.tongThanhToanSauApDung), 0) AS TongThanhToanSauApDung " +
                "FROM DonDatPhong ddp " +
                "LEFT JOIN ChiTietApDung ctad ON ddp.MaDonDatPhong = ctad.MaDonDatPhong " +
                "WHERE CONVERT(date, ddp.NgayTraPhong) = ? " +
                "GROUP BY ddp.MaDonDatPhong, ddp.NgayTraPhong";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, java.sql.Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String ma = rs.getString("MaDonDatPhong");
                String ngay = displayFormat.format(rs.getDate("NgayTraPhong").toLocalDate());
                double tien = rs.getDouble("TongThanhToanSauApDung");
                total += tien;
                model.addRow(new Object[]{ma, ngay, currencyFormat.format(tien)});
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn: " + e.getMessage());
        }

        lblTongDoanhThu.setText("Tổng: " + currencyFormat.format(total));
        chartPanel.repaint();
    }

    private void drawChart(Graphics2D g2) {
        LocalDate selectedDate = currentSelectedDate;

        ArrayList<Double> doanhThuList = new ArrayList<>();
        ArrayList<String> nhan = new ArrayList<>();
        try (Connection conn = ConnectDB.getConnection()) {
            for (int i = -4; i <= 0; i++) {
                LocalDate d = selectedDate.plusDays(i);
                String sql = "SELECT ISNULL(SUM(ctad.tongThanhToanSauApDung), 0) AS Tong FROM DonDatPhong ddp " +
                        "LEFT JOIN ChiTietApDung ctad ON ddp.MaDonDatPhong = ctad.MaDonDatPhong " +
                        "WHERE CONVERT(date, ddp.NgayTraPhong) = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setDate(1, java.sql.Date.valueOf(d));
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        doanhThuList.add(rs.getDouble("Tong"));
                        nhan.add(d.format(DateTimeFormatter.ofPattern("dd/MM")));
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

            String label = nhan.get(i);
            int lw = fm.stringWidth(label);
            g2.drawString(label, x + (barWidth - lw) / 2, height - 10);
        }
    }

    private class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String pattern = "dd/MM/yyyy";
        private final java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(pattern);

        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            return formatter.parse(text);
        }

        @Override
        public String valueToString(Object value) throws java.text.ParseException {
            if (value != null && value instanceof Calendar) {
                Calendar cal = (Calendar) value;
                return formatter.format(cal.getTime());
            }
            return "";
        }
    }
}
