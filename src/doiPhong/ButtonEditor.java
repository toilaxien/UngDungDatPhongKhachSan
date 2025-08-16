package doiPhong;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JButton button;

    public ButtonEditor() {
        button = new JButton("Đổi phòng");
        button.addActionListener(e -> {
            fireEditingStopped(); // Kết thúc edit trước khi xử lý
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        return (JButton) value;
    }

    @Override
    public Object getCellEditorValue() {
        return button;
    }
}
