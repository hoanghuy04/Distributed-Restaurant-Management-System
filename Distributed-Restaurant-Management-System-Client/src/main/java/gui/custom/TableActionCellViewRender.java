package gui.custom;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author RAVEN
 */
public class TableActionCellViewRender extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        PanelView action = new PanelView();
        if (value instanceof TableActionEvent) {
            action.initEvent((TableActionEvent) value, row);
        }
        return action;
    }

}
