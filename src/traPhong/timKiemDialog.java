package traPhong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
public class timKiemDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
	    try {	    	
	    	JFrame aFrame= new JFrame();
	        timKiemDialog dialog = new timKiemDialog(aFrame, true);
	        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	        dialog.setVisible(true);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	/**
	 * Create the dialog.
	 */

	public timKiemDialog(JFrame parent, boolean modal) {
		super(parent, modal);
	    setUndecorated(true); // ✅ Phải là dòng đầu tiên
	    getContentPane().setLayout(null);
	    contentPanel.setLayout(new FlowLayout());
	    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	    BackgroundPanel backgroundPanel = new BackgroundPanel("img/HinhAnhGiaoDienChinh/nenDangNhap.jpg"); 
	    setContentPane(backgroundPanel); 
	    JPanel timKiem = new panel_timKiem(parent);
	    getContentPane().add(timKiem);

	    Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
	    getRootPane().setBorder(border);

	    setSize(929, 629);
	    setLocationRelativeTo(null);
	    setResizable(false);
	}

	class BackgroundPanel extends JPanel {
	    private Image backgroundImage;

	    public BackgroundPanel(String imagePath) {
	        this.backgroundImage = new ImageIcon(imagePath).getImage();
	        setLayout(null); // bạn muốn layout tự định vị trí
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        // Vẽ ảnh full kích thước panel
	        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	    }
	}

}
