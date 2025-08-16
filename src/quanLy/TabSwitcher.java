package quanLy;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;



class TabSwitcher extends JPanel {
	private JButton[] tabs;

	public TabSwitcher(String... titles) {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBackground(Color.WHITE);
		tabs = new JButton[titles.length];
		for (int i = 0; i < titles.length; i++) {
			int index = i;
			tabs[i] = new JButton(titles[i]);
			tabs[i].setFocusPainted(false);
			tabs[i].setBackground(Color.WHITE);
			tabs[i].setFont(new Font("Arial", Font.BOLD, 14));
			tabs[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			tabs[i].addActionListener(e -> {
				setSelected(index);
				if (callback != null) callback.actionPerformed(index);
			});
			add(tabs[i]);
		}
		setSelected(0);
	}

	private void setSelected(int index) {
		for (int i = 0; i < tabs.length; i++) {
			tabs[i].setBackground(i == index ? new Color(0, 200, 130) : Color.WHITE);
			tabs[i].setForeground(i == index ? Color.WHITE : Color.BLACK);
		}
	}

	private TabCallback callback;

	public void setTabAction(TabCallback callback) {
		this.callback = callback;
	}

	public interface TabCallback {
		void actionPerformed(int index);
	}
}
