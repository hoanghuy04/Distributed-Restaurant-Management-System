/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui.custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Hoang
 */
public class TableDesign {

    private DefaultTableModel modelTable;
    private JTable table;
    private TableColumnModel columnModel;

    public TableDesign(String[] headers, List<Integer> tableWidth) {
        modelTable = new DefaultTableModel(headers, 0) {
            boolean[] canEdit = new boolean[headers.length];

            {
                for (int i = 0; i < headers.length - 1; i++) {
                    canEdit[i] = false;
                }
                canEdit[headers.length - 1] = true;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }

        };
        table = new JTable(modelTable);
        table.setCellSelectionEnabled(false);

        // Thiết lập tiêu đề của bảng
        table.getTableHeader().setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 50));
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private final Color hoverColor = new Color(0xF5F5F5); // Màu xám #F5F5F5

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    label.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 10));
                }

                // Kiểm tra xem hàng có phải là hàng hover hay không
                Point mousePosition = table.getMousePosition();
                if (mousePosition != null) {
                    int rowIndex = table.rowAtPoint(mousePosition);
                    // Kiểm tra nếu chuột ở trên hàng (và không ở header)
                    if (rowIndex != -1 && mousePosition.y > 0) {
                        // Đảm bảo chỉ làm màu cho hàng đang hover
                        if (row == rowIndex) {
                            c.setBackground(hoverColor); // Màu xám cho hàng hover
                        } else {
                            c.setBackground(Color.WHITE); // Màu nền cho hàng không được chọn
                        }
                    } else {
                        c.setBackground(Color.WHITE); // Màu nền cho hàng không được chọn
                    }
                } else {
                    c.setBackground(Color.WHITE); // Màu nền cho hàng không được chọn
                }
                c.setForeground(Color.BLACK); // Màu chữ cho hàng

                setFont(c.getFont().deriveFont(Font.PLAIN, 15));
                return c;
            }
        });

        // Thêm MouseMotionListener để theo dõi chuột
        table.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Cập nhật lại giao diện khi chuột di chuyển
                table.repaint();

                // Thay đổi con trỏ chuột thành HAND_CURSOR khi chuột ở trên hàng
                Point mousePosition = table.getMousePosition();
                if (mousePosition != null) {
                    int rowIndex = table.rowAtPoint(mousePosition);
                    // Kiểm tra nếu vị trí chuột không nằm trong header
                    if (rowIndex != -1 && mousePosition.y > 0) {
                        table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else {
                        table.setCursor(Cursor.getDefaultCursor());
                    }
                } else {
                    table.setCursor(Cursor.getDefaultCursor());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Lấy vị trí chuột
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                int column = table.columnAtPoint(point);
                if (column == table.getColumnCount() - 1) {
                    table.clearSelection();
                }
            }
        });

        columnModel = table.getColumnModel();
        for (int i = 0; i < headers.length; i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth(tableWidth.get(i));
            column.setResizable(false);
        }
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(0xF6F6F6));

        table.setRowHeight(50);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(false); // Không cho phép chọn ô
        table.setFocusable(false); // Không cho phép bàn phím chọn ô

        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(true);
        table.setGridColor(new Color(0xE9E9E9));

    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public DefaultTableModel getModelTable() {
        return modelTable;
    }

    public void setModelTable(DefaultTableModel modelTable) {
        this.modelTable = modelTable;
    }

    public TableColumnModel getColumnModel() {
        return columnModel;
    }

    public void setColumnModel(TableColumnModel columnModel) {
        this.columnModel = columnModel;
    }

}
