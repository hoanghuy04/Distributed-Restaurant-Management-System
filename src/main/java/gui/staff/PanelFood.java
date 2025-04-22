/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.staff;

import common.Constants;
import model.ItemEntity;
import gui.manager.PromotionGUI;
import gui.manager.ToppingGUI;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import util.*;
import javax.swing.border.StrokeBorder;

/**
 *
 * @author Trần Ngọc Huyền
 */
public class PanelFood extends javax.swing.JPanel {

    /**
     * Creates new form PanelF
     */
    private ItemEntity item;
    private PanelCategoryDetail panelCategoryDetail;
    private OrderGUI orderGUI;
    private PromotionGUI promotionGUI;
    private DialogItemInformation dialog;
    private ToppingGUI toppingGUI;

    public PanelFood() {
        initComponents();
    }

    public PanelFood(ItemEntity item, Object parentGUI) {
        initComponents();
        this.item = item;
        if (parentGUI instanceof OrderGUI) {
            this.orderGUI = (OrderGUI) parentGUI;
        } else if (parentGUI instanceof PromotionGUI) {
            this.promotionGUI = (PromotionGUI) parentGUI;
        } else if (parentGUI instanceof DialogItemInformation) {
            this.dialog = (DialogItemInformation) parentGUI;
        } else if (parentGUI instanceof ToppingGUI) {
            this.toppingGUI = (ToppingGUI) parentGUI;
        }
        setOpaque(false);
        fillContent();
        setBorder();
    }

    private void fillContent() {
        if (dialog == null) {
            setLblImgItem(item.getImg());
        } else {
            jLayeredPane1.setSize(new Dimension(dialog.getWidth(), 320));
            roundedPanel1.setSize(new Dimension(dialog.getWidth(), 320));
            lblImgItem.setIcon(ResizeImage.resizeImage(new ImageIcon(getClass().getResource("/img/item/" + item.getImg())), dialog.getWidth(), 300));
        }
        setLblNameItem(FormatTextUlti.formatTextWithLineBreaks(item.getName(), 50, "center"));
        System.out.println(item.getTopDiscountPercentage());
        if (item.getTopDiscountPercentage() > 0) {
            lblDiscountItem.setText(DoubleFormatUlti.format(item.getTopDiscountPercentage() * 100) + "%");
            lblPriceItem.setText("<html><body><s style='color: rgba(0, 0, 0, 0.5); font-size: 10px; font-weight:0'>"
                    + DoubleFormatUlti.format(item.getSellingPrice())
                    + "</s> "
                    + DoubleFormatUlti.format(item.getSellingPrice() * (1 - item.getTopDiscountPercentage()))
                    + "</body></html>");
        } else {
            lblDiscountItem.setVisible(false);
            lblPriceItem.setText(DoubleFormatUlti.format(item.getSellingPrice()));
        }
    }

    private void setBorder() {
        if (this.promotionGUI != null && this.promotionGUI.getItems() != null && this.promotionGUI.getItems().contains(this.item)) {
            setBorder(new StrokeBorder(new BasicStroke(3f), Constants.COLOR_PRIMARY));
        } else if (this.toppingGUI != null && this.toppingGUI.getItems() != null && this.toppingGUI.getItems().contains(this.item)) {
            setBorder(new StrokeBorder(new BasicStroke(3f), Constants.COLOR_PRIMARY));
        } else {
            setBorder(null);
        }
    }

    public PanelCategoryDetail getPanelCategoryDetail() {
        return panelCategoryDetail;
    }

    public void setPanelCategoryDetail(PanelCategoryDetail panelCategoryDetail) {
        this.panelCategoryDetail = panelCategoryDetail;
    }

    public void setLblImgItem(String img) {
        this.lblImgItem.setIcon(ResizeImage.resizeImage(new ImageIcon(getClass().getResource("/img/item/" + img)), 200, 160));
    }

    public JLabel getLblImgItem() {
        return lblImgItem;
    }

    public void setLblNameItem(String name) {
        this.lblNameItem.setText(name);
    }

    public void setLblPriceItem(double price) {
        DecimalFormat df = new DecimalFormat("#,###");
        this.lblPriceItem.setText(df.format(price));
    }

    public JLabel getLblDiscountItem() {
        return lblDiscountItem;
    }

    public void setLblDiscountItem(double discount) {
        DecimalFormat df = new DecimalFormat("#,###");
        this.lblPriceItem.setText(df.format(discount));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roundedPanel4 = new gui.custom.RoundedPanel();
        lblPriceItem = new JLabel();
        lblNameItem = new JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        roundedPanel1 = new gui.custom.RoundedPanel();
        lblImgItem = new JLabel();
        lblDiscountItem = new JLabel();

        setPreferredSize(new Dimension(200, 240));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());

        roundedPanel4.setBackground(new Color(241, 241, 241));
        roundedPanel4.setPreferredSize(new Dimension(212, 80));
        roundedPanel4.setLayout(new java.awt.GridLayout(2, 1));

        lblPriceItem.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblPriceItem.setForeground(Constants.COLOR_PRIMARY);
        lblPriceItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPriceItem.setText("jLabel1");
        roundedPanel4.add(lblPriceItem);

        lblNameItem.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNameItem.setForeground(Constants.COLOR_TEXT);
        lblNameItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameItem.setText("jLabel1");
        lblNameItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        roundedPanel4.add(lblNameItem);

        add(roundedPanel4, java.awt.BorderLayout.SOUTH);

        jLayeredPane1.setPreferredSize(new Dimension(200, 300));

        roundedPanel1.setLayout(new java.awt.BorderLayout());
        roundedPanel1.add(lblImgItem, java.awt.BorderLayout.CENTER);

        lblDiscountItem.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblDiscountItem.setForeground(new Color(255, 255, 255));
        lblDiscountItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDiscountItem.setIcon(new ImageIcon(getClass().getResource("/img/icon/png/icons8-discount-60.png"))); // NOI18N
        lblDiscountItem.setText("0.2%");
        lblDiscountItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLayeredPane1.setLayer(roundedPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblDiscountItem, javax.swing.JLayeredPane.POPUP_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundedPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap(134, Short.MAX_VALUE)
                    .addComponent(lblDiscountItem)
                    .addContainerGap()))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addComponent(roundedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(lblDiscountItem)
                    .addGap(0, 240, Short.MAX_VALUE)))
        );

        add(jLayeredPane1, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if (orderGUI != null) {
            DialogItemInformation dialog = new DialogItemInformation(item, null, orderGUI);
            dialog.setVisible(true);
        } else if (promotionGUI != null) {
            if (!this.promotionGUI.getItems().contains(item)) {
                this.promotionGUI.getItems().add(item);
            } else {
                this.promotionGUI.getItems().remove(item);
            }
            List<String> itemsListStr = new ArrayList<>();
            for (ItemEntity i : this.promotionGUI.getItems()) {
                itemsListStr.add(i.getItemId());
            }
            String itemsStr = String.join(",", itemsListStr);
            promotionGUI.getTxtItem().setText(itemsStr);
        } else if (toppingGUI != null) {
            if (!this.toppingGUI.getItems().contains(item)) {
                this.toppingGUI.getItems().add(item);
            } else {
                this.toppingGUI.getItems().remove(item);
            }
            List<String> itemsListStr = new ArrayList<>();
            for (ItemEntity i : this.toppingGUI.getItems()) {
                itemsListStr.add(i.getItemId());
            }
            String itemsStr = String.join(",", itemsListStr);
            toppingGUI.getTxtItem().setText(itemsStr);
        }
        setBorder();
    }//GEN-LAST:event_formMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane jLayeredPane1;
    private JLabel lblDiscountItem;
    private JLabel lblImgItem;
    private JLabel lblNameItem;
    private JLabel lblPriceItem;
    private gui.custom.RoundedPanel roundedPanel1;
    private gui.custom.RoundedPanel roundedPanel4;
    // End of variables declaration//GEN-END:variables
}
