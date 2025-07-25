/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.manager;

import bus.FloorBUS;
import model.FloorEntity;
import gui.FormLoad;
import gui.custom.TableDesign;
import java.util.Arrays;
import java.util.List;
import javax.management.Notification;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author ADMIN
 */
public class FloorGUI extends javax.swing.JPanel {

    private final String[] headers;
    private final List<Integer> len;
    private final TableDesign tableDesign;
    private final FloorBUS floorBUS;
    private DefaultTableModel modelTable = new DefaultTableModel();

    /**
     * Creates new form FloorGUI
     */
    public FloorGUI() {
        floorBUS = FormLoad.floorBUS;
        headers = new String[]{"Mã lầu", "Tên lầu", "Sức chứa"};
        len = Arrays.asList(100, 200, 50);
        tableDesign = new TableDesign(headers, len);
        initComponents();
        modelTable = tableDesign.getModelTable();
        loadData();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblName = new gui.custom.RoundedTextField();
        jLabel3 = new javax.swing.JLabel();
        lblCapacity = new gui.custom.RoundedTextField();
        jPanel11 = new javax.swing.JPanel();
        btnFind = new gui.custom.RoundedButton();
        btnAdd = new gui.custom.RoundedButton();
        btnUpdate = new gui.custom.RoundedButton();
        btnClear = new gui.custom.RoundedButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel6.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel1.setText("     Thông tin tầng");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel2.setText("Tên tầng");
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 27));

        lblName.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel3.setText("Sức chứa");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(lblCapacity, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(25, 25, 25)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCapacity, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        jPanel6.add(jPanel10, java.awt.BorderLayout.NORTH);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        btnFind.setText("Tìm kiếm");
        btnFind.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });
        jPanel11.add(btnFind);

        btnAdd.setText("Thêm");
        btnAdd.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        btnAdd.setPreferredSize(new java.awt.Dimension(100, 52));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel11.add(btnAdd);

        btnUpdate.setText("Cập nhật");
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        btnUpdate.setPreferredSize(new java.awt.Dimension(102, 50));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel11.add(btnUpdate);

        btnClear.setText("Xóa trắng");
        btnClear.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        jPanel11.add(btnClear);

        jPanel6.add(jPanel11, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel6, java.awt.BorderLayout.NORTH);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel9.setLayout(new java.awt.BorderLayout());

        table = tableDesign.getTable();
        table.setModel(tableDesign.getModelTable());
        table.setColumnModel(tableDesign.getColumnModel());
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        jPanel9.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel8.setPreferredSize(new java.awt.Dimension(1189, 20));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1275, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel8, java.awt.BorderLayout.NORTH);

        jPanel7.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(1269, 40));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1355, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel3.setPreferredSize(new java.awt.Dimension(1269, 40));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1355, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.SOUTH);

        jPanel4.setPreferredSize(new java.awt.Dimension(40, 0));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.WEST);

        jPanel5.setPreferredSize(new java.awt.Dimension(40, 780));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 780, Short.MAX_VALUE)
        );

        add(jPanel5, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearText();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if (validData()) {
            String name = lblName.getText().trim();
            int capacity = 0;
            try {
                capacity = Integer.parseInt(lblCapacity.getText().trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            FloorEntity f = new FloorEntity(null, name, capacity);
            if (floorBUS.insertEntity(f)) {
                JOptionPane.showMessageDialog(null, "Thêm tầng thành công");
                deleteAllTable();
                loadData();
            } else {
                JOptionPane.showMessageDialog(null, "Thêm tầng không thành công");
            }
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        String name = lblName.getText().trim();
        if (name.isBlank() || name.isEmpty()) {
            deleteAllTable();
            loadData();
        } else {
            FloorEntity f = floorBUS.findByName(name);
            if (f != null) {
                deleteAllTable();
                modelTable.addRow(new Object[]{
                    f.getFloorId(), f.getName(), f.getCapacity()
                });
            } else {
                JOptionPane.showMessageDialog(null, "Không tồn tại!");
            }
        }
    }//GEN-LAST:event_btnFindActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn dòng cần sửa");
        } else {
            if (validData()) {
                String name = lblName.getText().trim();
                String id = table.getValueAt(selectedRow, 0).toString();
                int capacity = 0;
                try {
                    capacity = Integer.parseInt(lblCapacity.getText().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                FloorEntity f = new FloorEntity(id, name, capacity);
                if (floorBUS.updateEntity(f)) {
                    JOptionPane.showMessageDialog(null, "Cập nhật tầng thành công");
                    deleteAllTable();
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(null, "Cập nhật tầng không thành công");
                }
            }
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        int row = table.getSelectedRow();
        if (row >= 0) {
            String name = table.getValueAt(row, 1).toString();
            String capacity = table.getValueAt(row, 2).toString();
            lblName.setText(name);
            lblCapacity.setText(capacity);
        }
    }//GEN-LAST:event_tableMouseClicked

    private void clearText() {
        lblCapacity.setText("");
        lblName.setText("");
        
    }

    private void loadData() {
        floorBUS.getAllEntities().stream().forEach(f -> {
            modelTable.addRow(new Object[]{
                f.getFloorId(), f.getName(), f.getCapacity()
            });
        });
    }

    private void deleteAllTable() {
        modelTable.setRowCount(0);
    }

    private boolean validData() {
        String name = lblName.getText().trim();
        if (name.isEmpty() || name.isBlank()) {
            JOptionPane.showMessageDialog(null, "Tên không được rỗng");
            return false;
        }
        int capacity = 0;
        try {
            capacity = Integer.parseInt(lblCapacity.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Sức chứa phải là số!");
            return false;
        }
        return capacity > 0;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gui.custom.RoundedButton btnAdd;
    private gui.custom.RoundedButton btnClear;
    private gui.custom.RoundedButton btnFind;
    private gui.custom.RoundedButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private gui.custom.RoundedTextField lblCapacity;
    private gui.custom.RoundedTextField lblName;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
