package quanLy;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import org.jdatepicker.impl.*;

public class ThongKeDoanhThuGUI extends JPanel {
	private JPanel mainPanel;
	private JPanel panelNgay, panelThang, panelNam;

	public ThongKeDoanhThuGUI() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		setBackground(Color.WHITE);

		Font fontTieuDe = new Font("Times New Roman", Font.BOLD, 24);

		// Tiêu đề
		JPanel header = new JPanel(new BorderLayout());
		header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		header.setBackground(Color.WHITE);
		JLabel lbl = new JLabel("Thống kê doanh thu", JLabel.LEFT);
		lbl.setFont(fontTieuDe);
		header.add(lbl, BorderLayout.WEST);
		add(header, BorderLayout.NORTH);

		// Thanh tab ở trên
		TabSwitcher switcher = new TabSwitcher("Thống kê theo ngày", "Thống kê theo tháng", "Thống kê theo năm");
		add(switcher, BorderLayout.NORTH);

		// Nội dung các panel ở dưới
		mainPanel = new JPanel(new CardLayout());
		add(mainPanel, BorderLayout.CENTER);

		panelNgay = new PanelThongKeTheoNgay();
		panelThang = new PanelThongKeTheoThang();
		panelNam = new PanelThongKeTheoNam();

		mainPanel.add(panelNgay, "NGAY");
		mainPanel.add(panelThang, "THANG");
		mainPanel.add(panelNam, "NAM");

		switcher.setTabAction((index) -> {
			CardLayout cl = (CardLayout) mainPanel.getLayout();
			switch (index) {
				case 0 -> cl.show(mainPanel, "NGAY");
				case 1 -> cl.show(mainPanel, "THANG");
				case 2 -> cl.show(mainPanel, "NAM");
			}
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Thống kê doanh thu");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1220, 720);
			frame.setLocationRelativeTo(null);
			frame.setContentPane(new ThongKeDoanhThuGUI());
			frame.setVisible(true);
		});
	}
}
