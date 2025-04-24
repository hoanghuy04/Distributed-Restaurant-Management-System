/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.manager;

import bus.*;
import com.formdev.flatlaf.FlatLightLaf;
import common.Constants;
import model.CategoryEntity;
import gui.FormLoad;
import gui.custom.chart.Chart;
import gui.custom.chart.ModelChart;
import gui.custom.piechart.ModelPieChart;
import gui.custom.piechart.PieChart;
import java.awt.Color;
import java.lang.Exception;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import raven.toast.Notifications;
import util.DoubleFormatUlti;

/**
 *
 * @author ADMIN
 */
public class ItemStatsGUI extends javax.swing.JPanel {

    /**
     * Creates new form ItemStatsGUI
     */
    private CategoryBUS categoryBUS;
    private ItemBUS itemBUS;

    public ItemStatsGUI() throws Exception {
        FlatLightLaf.setup();
        categoryBUS = FormLoad.categoryBUS;
        itemBUS = FormLoad.itemBUS;
        List<String> categoryItems = new ArrayList<>();
        categoryItems = categoryBUS.getAllEntities().stream().map(x -> x.getName()).toList();
        initComponents();
        loadComboItemType();
        createChartRevenue(LocalDate.now().atStartOfDay(), LocalDate.now().atTime(23, 59, 59, 999999999), comboItemType.getSelectedItem().toString(), statsRevenue);
        createChartQuantity(LocalDate.now().atStartOfDay(), LocalDate.now().atTime(23, 59, 59, 999999999), comboItemType.getSelectedItem().toString(), statsQuantity);
        createChartAll(LocalDate.now().atStartOfDay(), LocalDate.now().atTime(23, 59, 59, 999999999), categoryItems, statsAll);
        setTextForCategory(LocalDate.now().atStartOfDay(), LocalDate.now().atTime(23, 59, 59, 999999999));
        createChartPieChart(LocalDate.now().atStartOfDay(), LocalDate.now().atTime(23, 59, 59, 999999999), categoryItems, statsCircle);
    }

    private void loadComboItemType() throws Exception {
        comboItemType.removeAllItems();
        List<CategoryEntity> categories = categoryBUS.getAllEntities();
        categories.forEach(x -> comboItemType.addItem(x.getName()));
    }

    private void createChartRevenue(LocalDateTime startDate, LocalDateTime endDate, String nameItem, JPanel panel) throws Exception {
        panel.removeAll();
        Chart chart = new Chart();
        panel.add(chart);
        updateChartRevenue(chart, startDate, endDate, nameItem);
        panel.repaint();
        panel.revalidate();
    }

    private void updateChartRevenue(Chart chart, LocalDateTime startDate, LocalDateTime endDate, String nameItem) throws Exception {
        chart.clear();
        chart.addLegend("Doanh Thu", Constants.COLOR_REVENUE);
        Map<String, Double> map = itemBUS.getTop5ItemHaveBestRevenue(startDate, endDate, nameItem);
        map.entrySet().forEach(x -> chart.addData(new ModelChart(x.getKey(), new double[]{x.getValue()})));
        chart.start();
    }

    private void createChartQuantity(LocalDateTime startDate, LocalDateTime endDate, String nameItem, JPanel panel) throws Exception {
        panel.removeAll();
        Chart chart = new Chart();
        panel.add(chart);
        updateChartQuantity(chart, startDate, endDate, nameItem);
        panel.repaint();
        panel.revalidate();
    }

    private void updateChartQuantity(Chart chart, LocalDateTime startDate, LocalDateTime endDate, String nameItem) throws Exception {
        chart.clear();
        chart.addLegend("Số Lượng", Constants.COLOR_REVENUE);
        Map<String, Integer> map = itemBUS.getTop5ItemHaveBestQuantity(startDate, endDate, nameItem);
        map.entrySet().forEach(x -> chart.addData(new ModelChart(x.getKey(), new double[]{x.getValue()})));
        chart.start();
    }
    private void createChartAll(LocalDateTime startDate, LocalDateTime endDate, List<String> nameItems, JPanel panel) throws Exception {
        panel.removeAll();
        Chart chart = new Chart();
        panel.add(chart);
        updateChartAll(chart, startDate, endDate, nameItems);
        panel.repaint();
        panel.revalidate();
    }

    private void updateChartAll(Chart chart, LocalDateTime startDate, LocalDateTime endDate, List<String> nameItems) throws Exception {
        chart.clear();
        chart.addLegend("Doanh Thu", Constants.COLOR_REVENUE);
        Map<String, Double> map = itemBUS.getRevenueOfAllItems(startDate, endDate, nameItems);
        map.entrySet().forEach(x -> chart.addData(new ModelChart(x.getKey(), new double[]{x.getValue()})));
        chart.start();
    }
    private void createChartPieChart(LocalDateTime startDate, LocalDateTime endDate, List<String> nameItems, JPanel panel) throws Exception {
        panel.removeAll();
        PieChart pieChart = new PieChart();
        panel.add(pieChart);
        updatePieChart(pieChart,startDate, endDate, nameItems);
        panel.repaint();
        panel.revalidate();
    }

    private void updatePieChart(PieChart pieChart, LocalDateTime startDate, LocalDateTime endDate, List<String> nameItems) throws Exception {
        Map<String, Double> map = itemBUS.getRevenueOfAllItems(startDate, endDate, nameItems);
        map.entrySet().forEach((entry) -> {
            String key = entry.getKey();
            double value = entry.getValue();
            if(key.matches(".*Pizza.*")) {
                pieChart.addData(new ModelPieChart(key,value,new Color(248,159,114)));
            }
            if(key.equals("Khai Vị & Salad")) {
                pieChart.addData(new ModelPieChart(key,value,new Color(12,237,159)));
            }
            if(key.equals("Mì Ý")) {
                pieChart.addData(new ModelPieChart(key,value,new Color(253,129,143)));
            }
            if(key.equals("Đồ Uống")) {
                pieChart.addData(new ModelPieChart(key,value,new Color(4,209,219)));
            }
        });
    }
    
    private void setTextForCategory(LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        lblPizzaRevenue.setText("" + DoubleFormatUlti.format(itemBUS.getTotalRevenueByCategory(startDate, endDate, "Pizza")));
        double temp = itemBUS.getTotalRevenueByCategory(startDate, endDate, "Khai Vị") + itemBUS.getTotalRevenueByCategory(startDate, endDate, "Salad");
        lblKhaiViSaladRevenue.setText("" + DoubleFormatUlti.format(temp));
        lblMiyRevenue.setText(""+DoubleFormatUlti.format(itemBUS.getTotalRevenueByCategory(startDate, endDate, "Mì Ý")));
        lblThucUongRevenue.setText(""+DoubleFormatUlti.format(itemBUS.getTotalRevenueByCategory(startDate, endDate, "Đồ Uống")));
        temp = itemBUS.getQtyByCategory(startDate, endDate, "Salad") + itemBUS.getQtyByCategory(startDate, endDate, "Khai Vị");
        lblQtyKhaiviSalad.setText("Số lượng đã bán: " + DoubleFormatUlti.format(temp));
        lblQtyPizza.setText("Số lượng đã bán: "+ itemBUS.getQtyByCategory(startDate, endDate, "Pizza"));
        lblQtyMiy.setText("Số lượng đã bán: "+ itemBUS.getQtyByCategory(startDate, endDate, "Mì Ý"));
        lblQtyThucuong.setText("Số lượng đã bán: "+ itemBUS.getQtyByCategory(startDate, endDate, "Đồ Uống"));
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooser1 = new gui.custom.datechooser.DateChooser();
        dateChooser2 = new gui.custom.datechooser.DateChooser();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        statsByAll = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        pCenter = new javax.swing.JPanel();
        pCenterCenter = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        comboStats = new javax.swing.JComboBox<>();
        statsAll = new javax.swing.JPanel();
        pCenterEast = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        statsCircle = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        pNorth = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        lblPizzaRevenue = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        lblQtyPizza = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        lblKhaiViSaladRevenue = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        lblQtyKhaiviSalad = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        lblMiyRevenue = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        lblQtyMiy = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        lblThucUongRevenue = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        lblQtyThucuong = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        statsByType = new javax.swing.JPanel();
        selectionPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        startedDate = new gui.custom.RoundedTextField();
        endedDate = new gui.custom.RoundedTextField();
        comboItemType = new javax.swing.JComboBox<>();
        roundedButton1 = new gui.custom.RoundedButton();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        statsRevenue = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        statsQuantity = new javax.swing.JPanel();

        dateChooser1.setTextRefernce(startedDate);

        dateChooser2.setTextRefernce(endedDate);

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setBackground(new java.awt.Color(244, 248, 254));
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N

        statsByAll.setBackground(new java.awt.Color(244, 248, 254));
        statsByAll.setLayout(new java.awt.BorderLayout());

        jPanel25.setLayout(new java.awt.BorderLayout());

        pCenter.setBackground(new java.awt.Color(244, 248, 254));
        pCenter.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 0, new java.awt.Color(0, 0, 0)));
        pCenter.setLayout(new java.awt.BorderLayout());

        pCenterCenter.setBackground(new java.awt.Color(255, 255, 255));
        pCenterCenter.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(244, 248, 254));
        jPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 1, new java.awt.Color(0, 0, 0)));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel21.setText("Thống kê sản phẩm theo thời gian");

        comboStats.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        comboStats.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hôm nay", "Hôm trước", "7 ngày trước", "30 ngày trước", "Năm nay", "Năm trước" }));
        comboStats.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                try {
                    comboStatsItemStateChanged(evt);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 451, Short.MAX_VALUE)
                .addComponent(comboStats, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboStats, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel5, java.awt.BorderLayout.NORTH);

        statsAll.setBackground(new java.awt.Color(255, 255, 255));
        statsAll.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        statsAll.setLayout(new java.awt.BorderLayout());
        jPanel4.add(statsAll, java.awt.BorderLayout.CENTER);

        pCenterCenter.add(jPanel4, java.awt.BorderLayout.CENTER);

        pCenter.add(pCenterCenter, java.awt.BorderLayout.CENTER);

        pCenterEast.setBackground(new java.awt.Color(244, 248, 254));
        pCenterEast.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        pCenterEast.setLayout(new java.awt.BorderLayout());

        jPanel22.setLayout(new java.awt.BorderLayout());

        jPanel23.setBackground(new java.awt.Color(244, 248, 254));
        jPanel23.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(0, 0, 0)));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel22.setText("Xu hướng mua hàng của khách hàng");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        jPanel22.add(jPanel23, java.awt.BorderLayout.NORTH);

        statsCircle.setBackground(new java.awt.Color(255, 255, 255));
        statsCircle.setPreferredSize(new java.awt.Dimension(700, 383));
        statsCircle.setLayout(new java.awt.BorderLayout());
        jPanel22.add(statsCircle, java.awt.BorderLayout.CENTER);

        jPanel30.setLayout(new java.awt.BorderLayout());
        jPanel22.add(jPanel30, java.awt.BorderLayout.SOUTH);

        pCenterEast.add(jPanel22, java.awt.BorderLayout.CENTER);

        pCenter.add(pCenterEast, java.awt.BorderLayout.EAST);

        jPanel25.add(pCenter, java.awt.BorderLayout.CENTER);

        pNorth.setBackground(new java.awt.Color(255, 255, 255));
        pNorth.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 40));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(248, 159, 114));

        lblPizzaRevenue.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblPizzaRevenue.setForeground(new java.awt.Color(255, 255, 255));
        lblPizzaRevenue.setText("100000 VND");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Pizza");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon/png/icons8-pizza-100.png"))); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPizzaRevenue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lblPizzaRevenue)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(248, 159, 114));

        lblQtyPizza.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        lblQtyPizza.setForeground(new java.awt.Color(255, 255, 255));
        lblQtyPizza.setText("Số lượng đã bán : 100");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lblQtyPizza)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblQtyPizza)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pNorth.add(jPanel2);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jPanel11.setBackground(new java.awt.Color(12, 237, 159));

        lblKhaiViSaladRevenue.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblKhaiViSaladRevenue.setForeground(new java.awt.Color(255, 255, 255));
        lblKhaiViSaladRevenue.setText("100000 VND");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Khai vị & Salad");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon/png/icons8-salad-100.png"))); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblKhaiViSaladRevenue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(lblKhaiViSaladRevenue)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel12.setBackground(new java.awt.Color(12, 237, 159));

        lblQtyKhaiviSalad.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        lblQtyKhaiviSalad.setForeground(new java.awt.Color(255, 255, 255));
        lblQtyKhaiviSalad.setText("Số lượng đã bán : 100");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lblQtyKhaiviSalad)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblQtyKhaiviSalad)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pNorth.add(jPanel10);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jPanel14.setBackground(new java.awt.Color(253, 129, 143));

        lblMiyRevenue.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblMiyRevenue.setForeground(new java.awt.Color(255, 255, 255));
        lblMiyRevenue.setText("100000 VND");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Mì ý");

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon/png/icons8-spaghetti-100.png"))); // NOI18N

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblMiyRevenue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(lblMiyRevenue)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel15.setBackground(new java.awt.Color(253, 129, 143));

        lblQtyMiy.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        lblQtyMiy.setForeground(new java.awt.Color(255, 255, 255));
        lblQtyMiy.setText("Số lượng đã bán : 100");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lblQtyMiy)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblQtyMiy)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pNorth.add(jPanel13);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jPanel17.setBackground(new java.awt.Color(4, 209, 219));

        lblThucUongRevenue.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblThucUongRevenue.setForeground(new java.awt.Color(255, 255, 255));
        lblThucUongRevenue.setText("100000 VND");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Thức uống");

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon/png/icons8-champagne-100.png"))); // NOI18N

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblThucUongRevenue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(lblThucUongRevenue)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel18.setBackground(new java.awt.Color(4, 209, 219));

        lblQtyThucuong.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        lblQtyThucuong.setForeground(new java.awt.Color(255, 255, 255));
        lblQtyThucuong.setText("Số lượng đã bán : 100");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lblQtyThucuong)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblQtyThucuong)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pNorth.add(jPanel16);

        jPanel25.add(pNorth, java.awt.BorderLayout.NORTH);

        statsByAll.add(jPanel25, java.awt.BorderLayout.CENTER);

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setPreferredSize(new java.awt.Dimension(20, 967));

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 967, Short.MAX_VALUE)
        );

        statsByAll.add(jPanel26, java.awt.BorderLayout.WEST);

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setPreferredSize(new java.awt.Dimension(20, 967));

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 967, Short.MAX_VALUE)
        );

        statsByAll.add(jPanel27, java.awt.BorderLayout.EAST);

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setPreferredSize(new java.awt.Dimension(5493, 20));

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5493, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        statsByAll.add(jPanel28, java.awt.BorderLayout.NORTH);

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setPreferredSize(new java.awt.Dimension(5493, 20));

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5493, Short.MAX_VALUE)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        statsByAll.add(jPanel29, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("Tất cả", statsByAll);

        statsByType.setLayout(new java.awt.BorderLayout());

        selectionPanel.setBackground(new java.awt.Color(244, 248, 254));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel1.setText("Ngày kết thúc");

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel2.setText("Ngày bắt đầu");

        startedDate.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N

        endedDate.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N

        comboItemType.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        comboItemType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pizza", "Khai vị", "Mì ý", "Salad", "Thức uống" }));
        comboItemType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboItemTypeActionPerformed(evt);
            }
        });

        roundedButton1.setText("Thống kê");
        roundedButton1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        roundedButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    roundedButton1ActionPerformed(evt);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        javax.swing.GroupLayout selectionPanelLayout = new javax.swing.GroupLayout(selectionPanel);
        selectionPanel.setLayout(selectionPanelLayout);
        selectionPanelLayout.setHorizontalGroup(
            selectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selectionPanelLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(startedDate, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(endedDate, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(comboItemType, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(roundedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(400, Short.MAX_VALUE))
        );
        selectionPanelLayout.setVerticalGroup(
            selectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selectionPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(selectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, selectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboItemType, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(roundedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(selectionPanelLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(selectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(endedDate, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(startedDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(30, 30, 30))
        );

        statsByType.add(selectionPanel, java.awt.BorderLayout.NORTH);

        jPanel19.setBackground(new java.awt.Color(244, 248, 254));
        jPanel19.setPreferredSize(new java.awt.Dimension(50, 675));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 895, Short.MAX_VALUE)
        );

        statsByType.add(jPanel19, java.awt.BorderLayout.EAST);

        jPanel20.setBackground(new java.awt.Color(244, 248, 254));
        jPanel20.setPreferredSize(new java.awt.Dimension(50, 675));

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 895, Short.MAX_VALUE)
        );

        statsByType.add(jPanel20, java.awt.BorderLayout.WEST);

        jPanel21.setLayout(new javax.swing.BoxLayout(jPanel21, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout(5, 10));

        jPanel3.setBackground(new java.awt.Color(25, 178, 255));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel3.setBackground(Constants.COLOR_PRIMARY
        );
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText(" Top 5 sản phẩm có doanh thu cao nhất");
        jLabel3.setPreferredSize(new java.awt.Dimension(382, 50));
        jPanel3.add(jLabel3, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);

        statsRevenue.setBackground(new java.awt.Color(255, 255, 255));
        statsRevenue.setLayout(new java.awt.BorderLayout());
        jPanel1.add(statsRevenue, java.awt.BorderLayout.CENTER);

        jPanel21.add(jPanel1);

        jPanel24.setBackground(new java.awt.Color(244, 248, 254));
        jPanel24.setPreferredSize(new java.awt.Dimension(1731, 30));

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5393, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel21.add(jPanel24);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new java.awt.BorderLayout(5, 10));

        jPanel8.setBackground(new java.awt.Color(25, 178, 255));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jLabel4.setBackground(Constants.COLOR_PRIMARY);
        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText(" Top 5 sản phẩm có lượt bán cao nhất");
        jLabel4.setPreferredSize(new java.awt.Dimension(364, 50));
        jPanel8.add(jLabel4, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel8, java.awt.BorderLayout.NORTH);

        statsQuantity.setBackground(new java.awt.Color(255, 255, 255));
        statsQuantity.setLayout(new java.awt.BorderLayout());
        jPanel6.add(statsQuantity, java.awt.BorderLayout.CENTER);

        jPanel21.add(jPanel6);

        statsByType.add(jPanel21, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Theo loại sản phẩm", statsByType);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void comboItemTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboItemTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboItemTypeActionPerformed

    private void roundedButton1ActionPerformed(java.awt.event.ActionEvent evt) throws Exception {//GEN-FIRST:event_roundedButton1ActionPerformed
        LocalDate started = LocalDate.parse(startedDate.getText(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate ended = LocalDate.parse(endedDate.getText(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        if (started.isAfter(LocalDate.now())) {
         Notifications.getInstance().show(
                            Notifications.Type.WARNING,
                            Notifications.Location.TOP_RIGHT,
                            10000,
                            "Thông báo: Vui lòng chọn ngày kết thúc sau ngày hiện tại.");
        }
        else if (started.isAfter(LocalDate.now())) {
         Notifications.getInstance().show(
                            Notifications.Type.WARNING,
                            Notifications.Location.TOP_RIGHT,
                            10000,
                            "Thông báo: Vui lòng chọn ngày bắt đầu trước ngày hiện tại.");
        }
        else if (started.isAfter(ended)) {
         Notifications.getInstance().show(
                            Notifications.Type.WARNING,
                            Notifications.Location.TOP_RIGHT,
                            10000,
                            "Thông báo: Vui lòng chọn ngày bắt đầu trước ngày kết thúc.");
        }
        else if (ended.isBefore(started)) {
         Notifications.getInstance().show(
                            Notifications.Type.WARNING,
                            Notifications.Location.TOP_RIGHT,
                            10000,
                            "Thông báo: Vui lòng chọn ngày kết thúc sau ngày bắt đầu.");
        }
        else {
            LocalDateTime start = started.atStartOfDay(); // 00:00:00
            LocalDateTime end = ended.atTime(23, 59, 59, 999999999); // 23:59:59.999999999
            createChartRevenue(start, end, comboItemType.getSelectedItem().toString(), statsRevenue);
            createChartQuantity(start, end, comboItemType.getSelectedItem().toString(), statsQuantity);
        }


    }//GEN-LAST:event_roundedButton1ActionPerformed

    private void comboStatsItemStateChanged(java.awt.event.ItemEvent evt) throws Exception {//GEN-FIRST:event_comboStatsItemStateChanged
        String selectedItem = comboStats.getSelectedItem().toString(); 
        List<String> categoryItems = new ArrayList<>();
        categoryItems = categoryBUS.getAllEntities().stream().map(x -> x.getName()).toList();
        if(selectedItem.equals("Năm nay")) {
            LocalDate localDateStart = LocalDate.of(LocalDate.now().getYear(), 1, 1);
            LocalDate localDateEnd = LocalDate.of(LocalDate.now().getYear(), 12, 31);
            LocalDateTime start = localDateStart.atStartOfDay(); 
            LocalDateTime end = localDateEnd.atTime(23, 59, 59, 999999999);
            createChartAll(start, end, categoryItems, statsAll);
            setTextForCategory(start, end);
            createChartPieChart(start, end, categoryItems, statsCircle);
        } else
        if(selectedItem.equals("Năm trước")) {
            LocalDate localDateStart = LocalDate.of(LocalDate.now().getYear()-1, 1, 1);
            LocalDate localDateEnd = LocalDate.of(LocalDate.now().getYear()-1, 12, 31);
            LocalDateTime start = localDateStart.atStartOfDay(); 
            LocalDateTime end = localDateEnd.atTime(23, 59, 59, 999999999);
            createChartAll(start, end, categoryItems, statsAll);
            setTextForCategory(start, end);
            createChartPieChart(start, end, categoryItems, statsCircle);
             
        } else
        if(selectedItem.equals("Hôm nay")) {
            LocalDate localDateStart = LocalDate.now();
            LocalDate localDateEnd = LocalDate.now();
            LocalDateTime start = localDateStart.atStartOfDay(); 
            LocalDateTime end = localDateEnd.atTime(23, 59, 59, 999999999);
            createChartAll(start, end, categoryItems, statsAll);
            setTextForCategory(start, end);
            createChartPieChart(start, end, categoryItems, statsCircle);
        } else
        if(selectedItem.equals("Hôm trước")) {
            LocalDate localDateStart = LocalDate.now().minusDays(1);
            LocalDate localDateEnd = LocalDate.now().minusDays(1);
            LocalDateTime start = localDateStart.atStartOfDay(); 
            LocalDateTime end = localDateEnd.atTime(23, 59, 59, 999999999);
            createChartAll(start, end, categoryItems, statsAll);
            setTextForCategory(start, end);
            createChartPieChart(start, end, categoryItems, statsCircle);
            
        } else
        if(selectedItem.equals("7 ngày trước")) {
            LocalDate localDateStart = LocalDate.now().minusDays(7);
            LocalDate localDateEnd = LocalDate.now();
            LocalDateTime start = localDateStart.atStartOfDay(); 
            LocalDateTime end = localDateEnd.atTime(23, 59, 59, 999999999);
            createChartAll(start, end, categoryItems, statsAll);
            setTextForCategory(start, end);
            createChartPieChart(start, end, categoryItems, statsCircle);
        } else
        if(selectedItem.equals("30 ngày trước")) {
            LocalDate localDateStart = LocalDate.now().minusDays(30);
            LocalDate localDateEnd = LocalDate.now();
            LocalDateTime start = localDateStart.atStartOfDay(); 
            LocalDateTime end = localDateEnd.atTime(23, 59, 59, 999999999);
            createChartAll(start, end, categoryItems, statsAll);
            setTextForCategory(start, end);
            createChartPieChart(start, end, categoryItems, statsCircle);
        }
        
    }//GEN-LAST:event_comboStatsItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboItemType;
    private javax.swing.JComboBox<String> comboStats;
    private gui.custom.datechooser.DateChooser dateChooser1;
    private gui.custom.datechooser.DateChooser dateChooser2;
    private gui.custom.RoundedTextField endedDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblKhaiViSaladRevenue;
    private javax.swing.JLabel lblMiyRevenue;
    private javax.swing.JLabel lblPizzaRevenue;
    private javax.swing.JLabel lblQtyKhaiviSalad;
    private javax.swing.JLabel lblQtyMiy;
    private javax.swing.JLabel lblQtyPizza;
    private javax.swing.JLabel lblQtyThucuong;
    private javax.swing.JLabel lblThucUongRevenue;
    private javax.swing.JPanel pCenter;
    private javax.swing.JPanel pCenterCenter;
    private javax.swing.JPanel pCenterEast;
    private javax.swing.JPanel pNorth;
    private gui.custom.RoundedButton roundedButton1;
    private javax.swing.JPanel selectionPanel;
    private gui.custom.RoundedTextField startedDate;
    private javax.swing.JPanel statsAll;
    private javax.swing.JPanel statsByAll;
    private javax.swing.JPanel statsByType;
    private javax.swing.JPanel statsCircle;
    private javax.swing.JPanel statsQuantity;
    private javax.swing.JPanel statsRevenue;
    // End of variables declaration//GEN-END:variables
}
