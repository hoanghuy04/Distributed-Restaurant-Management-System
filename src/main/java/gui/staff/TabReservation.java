/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.staff;

import bus.OrderBUS;
import common.Constants;
import dal.connectDB.ConnectDB;
import model.OrderEntity;
import gui.FormLoad;
import gui.custom.RoundedPanel;
import jakarta.persistence.EntityManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.enums.OrderTypeEnum;
import model.enums.PaymentStatusEnum;
import model.enums.ReservationStatusEnum;
import util.DatetimeFormatterUtil;

/**
 *
 * @author pc
 */
public class TabReservation extends javax.swing.JPanel {

    private OrderBUS orderBUS;
    private List<OrderEntity> listOfAllReservations;
    private TreeMap<String, List<OrderEntity>> mapOfAllReservations;
    private MainGUI mainGUI;
    private panelDescription description;
    private JTextField txtQrContent;

    /**
     * Creates new form TabReservation
     */
    public TabReservation(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.orderBUS = FormLoad.orderBUS;

        this.listOfAllReservations = orderBUS.getListOfReservations(null, null, OrderTypeEnum.ADVANCE.getOrderType());
        this.mapOfAllReservations = getOrdersGroupByDate(listOfAllReservations);

        this.description = new panelDescription();
        this.txtQrContent = new JTextField();
        // Thêm DocumentListener để lắng nghe thay đổi trên JTextField
        txtQrContent.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                action();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Có thể xử lý sự kiện xóa, nếu cần
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Dùng khi văn bản có định dạng (không cần thiết cho JTextField cơ bản)
            }

            private void action() {
//                JOptionPane.showMessageDialog(null, "Nội dung mới: " + txtQrContent.getText());
                panelAllReservationsMouseClicked(null);
                OrderEntity orderEntity = orderBUS.getEntityById(txtQrContent.getText().trim());
                if (orderEntity != null && orderEntity.getPaymentStatus().equals(PaymentStatusEnum.UNPAID) && orderEntity.getReservationStatus().equals(ReservationStatusEnum.PENDING)) {
                    setListOfReservationsByOption(List.of(orderEntity));
                } else {
                    JOptionPane.showMessageDialog(null, "Không tìm thấy đơn đặt với mã: " + txtQrContent.getText());
                }
            }
        });

        initComponents();
        fillContent();
        setListOfAllReservations();

        // Thêm MouseAdapter vào các panel
        panelAllReservations.addMouseListener(commonMouseAdapter);
        panelUpComingOption.addMouseListener(commonMouseAdapter);
        panelPastOption.addMouseListener(commonMouseAdapter);
    }

    // Khởi tạo MouseAdapter dùng chung
    MouseAdapter commonMouseAdapter = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent evt) {
            JComponent source = (JComponent) evt.getSource();
            // Kiểm tra nếu nền không phải màu xám thì áp dụng màu hover
            if (!source.getBackground().equals(Constants.COLOR_PRIMARY)) {
                source.setBackground(new Color(220, 220, 220)); // Màu nền khi hover
            }
        }

        @Override
        public void mouseExited(MouseEvent evt) {
            JComponent source = (JComponent) evt.getSource();
            // Kiểm tra nếu nền không phải màu xám thì trả về màu nền trắng
            if (!source.getBackground().equals(Constants.COLOR_PRIMARY)) {
                source.setBackground(Color.WHITE); // Màu nền khi không hover
            }
        }

        @Override
        public void mouseClicked(MouseEvent evt) {
            JComponent source = (JComponent) evt.getSource();
            source.setBackground(Constants.COLOR_PRIMARY); // Màu nền khi panel được chọn
            // Thêm logic khi click vào panel ở đây, nếu cần
        }
    };

    public JTextField getTxtQRContent() {
        return txtQrContent;
    }

    public void setTxtQRContent(String txtQrContent) {
        this.txtQrContent.setText(txtQrContent);
    }

    public JLabel getLblAllReservations() {
        return lblAllReservations;
    }

    public void setLblAllReservations(JLabel lblAllReservations) {
        this.lblAllReservations = lblAllReservations;
    }

    public JLabel getLblPastRs() {
        return lblPastRs;
    }

    public void setLblPastRs(JLabel lblPastRs) {
        this.lblPastRs = lblPastRs;
    }

    public JLabel getLblUpComingRs() {
        return lblUpComingRs;
    }

    public void setLblUpComingRs(JLabel lblUpComingRs) {
        this.lblUpComingRs = lblUpComingRs;
    }

    public MainGUI getMainGUI() {
        return mainGUI;
    }

    public void setMainGUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    public JLabel getLblNumberOfAll() {
        return lblNumberOfAll;
    }

    public void setLblNumberOfAll(String s) {
        this.lblNumberOfAll.setText(s);
    }

    public JLabel getLblNumberOfPastRs() {
        return lblNumberOfPastRs;
    }

    public void setLblNumberOfPastRs(String s) {
        this.lblNumberOfPastRs.setText(s);
    }

    public JLabel getLblNumberOfUpComingRs() {
        return lblNumberOfUpComingRs;
    }

    public void setLblNumberOfUpComingRs(String s) {
        this.lblNumberOfUpComingRs.setText(s);
    }

    public List<OrderEntity> getListOfAllReservations() {
        return listOfAllReservations;
    }

    public void setListOfAllReservations(List<OrderEntity> listOfAllReservations) {
        this.listOfAllReservations = listOfAllReservations;
    }

    public TreeMap<String, List<OrderEntity>> getMapOfAllReservations() {
        return mapOfAllReservations;
    }

    public void setMapOfAllReservations(TreeMap<String, List<OrderEntity>> mapOfAllReservations) {
        this.mapOfAllReservations = mapOfAllReservations;
    }

    public void updateNumberOfReservations(LocalDate date, LocalTime time, int changedQty) {
        LocalDateTime currentDate = LocalDateTime.now();

        //update so luong panelDate
        LocalDate sevenDaysLater = currentDate.toLocalDate().plusDays(7);

        // So sánh nếu ngày nhập vào nằm trong khoảng 7 ngày trở lại đây
        if (date.isBefore(sevenDaysLater) && !date.isBefore(currentDate.toLocalDate())) {
            for (Component comp : panel7Dates.getComponents()) {
                if (comp instanceof PanelDate && ((PanelDate) comp).getDate().equals(date)) {
                    PanelDate c = (PanelDate) comp;
                    c.setLblnumber(Integer.parseInt(c.getLblnumber().getText()) + changedQty);
                }
            }
        }

        //update so luong reservations sap toi
        if (!LocalDateTime.of(date, time).isAfter(currentDate.plusHours(2))) {
            setLblNumberOfUpComingRs((Integer.parseInt(lblNumberOfUpComingRs.getText()) + changedQty + ""));
        }

        //update so luong reservations qua han
        if (!LocalDateTime.of(date, time).isAfter(currentDate.minusHours(2))) {
            setLblNumberOfPastRs((Integer.parseInt(lblNumberOfPastRs.getText()) + changedQty + ""));
        }

        setLblNumberOfAll((Integer.parseInt(lblNumberOfAll.getText()) + changedQty + ""));
    }

    private void fillContent() {
        setLblNumberOfAll(listOfAllReservations.size() + "");
        setLblNumberOfUpComingRs(listOfAllReservations.stream()
                .filter(r -> r.getReservationTime()
                .isAfter(LocalDateTime.now())
                && !r.getReservationTime()
                        .isAfter(LocalDateTime.now().plusHours(2)))
                .toList()
                .size() + "");
//        
//        listOfAllReservations.stream()
//                .filter(r -> r.getReservationTime()
//                .isAfter(LocalDateTime.now())
//                && !r.getReservationTime()
//                        .isAfter(r.getReservationTime().plusHours(2)))
//                .toList().stream().forEach(System.out :: println);

        setLblNumberOfPastRs(listOfAllReservations.stream()
                .filter(r -> r.getReservationTime()
                .isBefore(LocalDateTime.now())
                && !r.getReservationTime()
                        .isBefore(LocalDateTime.now().minusHours(2)))
                .toList()
                .size() + "");

        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().plusDays(i);

            String text;
            if (i == 0) {
                text = "Hôm nay " + date.toString();
            } else if (i == 1) {
                text = "Ngày mai " + date.toString();
            } else {
                text = date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("vi", "VN")) + " " + date.toString();
            }

            PanelDate panelDate = new PanelDate(this, date);
            panelDate.setLblDate(text);
            if (text.contains("Hôm nay ")) {
                panelDate.setLblnumber(listOfAllReservations.stream().filter(r -> r.getReservationTime().isAfter(LocalDateTime.now().minusHours(2))
                        && r.getReservationTime().isBefore(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0, 0)))).toList().size());
            } else {
                panelDate.setLblnumber(listOfAllReservations.stream().filter(r -> r.getReservationTime().toLocalDate().equals(date)).toList().size());
            }

            // Thêm PanelDate vào giao diện
            panel7Dates.add(panelDate);
            panel7Dates.add(Box.createRigidArea(new Dimension(10, 3)));
        }

        // Refresh giao diện để cập nhật các panel mới được thêm
        panel7Dates.revalidate();
        panel7Dates.repaint();
//        TabReservation.setLblNumberOfAll(Integer.parseInt(Tab));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelWrapper = new javax.swing.JPanel();
        scrollPaneReservation = new javax.swing.JScrollPane();
        panelReservations = new javax.swing.JPanel();
        panelSideBar = new javax.swing.JPanel();
        panel = new javax.swing.JPanel();
        panelButtonCreate = new gui.custom.RoundedButton();
        panelAllReservations = new gui.custom.RoundedPanel();
        lblAllReservations = new javax.swing.JLabel();
        lblNumberOfAll = new javax.swing.JLabel();
        panelOptions = new gui.custom.RoundedPanel();
        panelUpComingOption = new gui.custom.RoundedPanel();
        lblUpComingRs = new javax.swing.JLabel();
        lblNumberOfUpComingRs = new javax.swing.JLabel();
        panelPastOption = new gui.custom.RoundedPanel();
        lblPastRs = new javax.swing.JLabel();
        lblNumberOfPastRs = new javax.swing.JLabel();
        panel7Dates = new gui.custom.RoundedPanel();
        roundedButton1 = new gui.custom.RoundedButton();

        setLayout(new java.awt.BorderLayout());

        panelWrapper.setLayout(new java.awt.BorderLayout());

        panelReservations.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));
        scrollPaneReservation.setViewportView(panelReservations);

        panelWrapper.add(scrollPaneReservation, java.awt.BorderLayout.CENTER);

        panelSideBar.setLayout(new javax.swing.BoxLayout(panelSideBar, javax.swing.BoxLayout.LINE_AXIS));

        panel.setPreferredSize(new java.awt.Dimension(300, 40));

        panelButtonCreate.setBorder(null);
        panelButtonCreate.setBackground(Constants.COLOR_GREEN);
        panelButtonCreate.setForeground(new java.awt.Color(255, 255, 255));
        panelButtonCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon/png/createReservation.png"))); // NOI18N
        panelButtonCreate.setText("Tạo đơn đặt");
        panelButtonCreate.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        panelButtonCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panelButtonCreateActionPerformed(evt);
            }
        });

        panelAllReservationsMouseClicked(null);
        panelAllReservations.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelAllReservations.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelAllReservationsMouseClicked(evt);
            }
        });

        lblAllReservations.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblAllReservations.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAllReservations.setText("Tất cả đơn đặt");
        lblAllReservations.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAllReservationsMouseClicked(evt);
            }
        });

        lblNumberOfAll.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblNumberOfAll.setText("100");
        lblNumberOfAll.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout panelAllReservationsLayout = new javax.swing.GroupLayout(panelAllReservations);
        panelAllReservations.setLayout(panelAllReservationsLayout);
        panelAllReservationsLayout.setHorizontalGroup(
            panelAllReservationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllReservationsLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblAllReservations, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNumberOfAll)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelAllReservationsLayout.setVerticalGroup(
            panelAllReservationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllReservationsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNumberOfAll, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
            .addGroup(panelAllReservationsLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblAllReservations)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelOptions.setPreferredSize(new java.awt.Dimension(150, 75));

        panelUpComingOption.setBackground(new java.awt.Color(255, 255, 255));
        panelUpComingOption.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelUpComingOption.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelUpComingOptionMouseClicked(evt);
            }
        });

        lblUpComingRs.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblUpComingRs.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUpComingRs.setText("Sắp tới");

        lblNumberOfUpComingRs.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblNumberOfUpComingRs.setText("100");
        lblNumberOfUpComingRs.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout panelUpComingOptionLayout = new javax.swing.GroupLayout(panelUpComingOption);
        panelUpComingOption.setLayout(panelUpComingOptionLayout);
        panelUpComingOptionLayout.setHorizontalGroup(
            panelUpComingOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUpComingOptionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblUpComingRs, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblNumberOfUpComingRs)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        panelUpComingOptionLayout.setVerticalGroup(
            panelUpComingOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUpComingOptionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNumberOfUpComingRs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(lblUpComingRs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelPastOption.setBackground(new java.awt.Color(255, 255, 255));
        panelPastOption.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelPastOption.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelPastOptionMouseClicked(evt);
            }
        });

        lblPastRs.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPastRs.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPastRs.setText("Quá hạn");

        lblNumberOfPastRs.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblNumberOfPastRs.setText("100");
        lblNumberOfPastRs.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout panelPastOptionLayout = new javax.swing.GroupLayout(panelPastOption);
        panelPastOption.setLayout(panelPastOptionLayout);
        panelPastOptionLayout.setHorizontalGroup(
            panelPastOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPastOptionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPastRs, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNumberOfPastRs)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelPastOptionLayout.setVerticalGroup(
            panelPastOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblPastRs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPastOptionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNumberOfPastRs, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelOptionsLayout = new javax.swing.GroupLayout(panelOptions);
        panelOptions.setLayout(panelOptionsLayout);
        panelOptionsLayout.setHorizontalGroup(
            panelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOptionsLayout.createSequentialGroup()
                .addComponent(panelUpComingOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelPastOption, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelOptionsLayout.setVerticalGroup(
            panelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOptionsLayout.createSequentialGroup()
                .addGroup(panelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panelPastOption, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelUpComingOption, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 4, Short.MAX_VALUE))
        );

        panel7Dates.setLayout(new javax.swing.BoxLayout(panel7Dates, javax.swing.BoxLayout.Y_AXIS));

        roundedButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon/png/QrCodeIcon.png"))); // NOI18N
        roundedButton1.setMaximumSize(new java.awt.Dimension(40, 32));
        roundedButton1.setMinimumSize(new java.awt.Dimension(40, 32));
        roundedButton1.setPreferredSize(new java.awt.Dimension(40, 32));
        roundedButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panel7Dates, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAllReservations, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelOptions, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelLayout.createSequentialGroup()
                        .addComponent(panelButtonCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(roundedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelButtonCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roundedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(panelAllReservations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel7Dates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        panelSideBar.add(panel);

        panelWrapper.add(panelSideBar, java.awt.BorderLayout.EAST);

        add(panelWrapper, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void panelButtonCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_panelButtonCreateActionPerformed
        new DialogAddReservation(this.mapOfAllReservations, this).setVisible(true);
    }//GEN-LAST:event_panelButtonCreateActionPerformed

    private void lblAllReservationsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAllReservationsMouseClicked
        panelAllReservationsMouseClicked(evt);
    }//GEN-LAST:event_lblAllReservationsMouseClicked

    private void panelAllReservationsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAllReservationsMouseClicked
        setListOfAllReservations();
        updatePanelBackground();
        this.panelAllReservations.setBackground(Constants.COLOR_PRIMARY);
        getLblNumberOfAll().setForeground(Color.white);
        getLblAllReservations().setForeground(Color.white);
    }//GEN-LAST:event_panelAllReservationsMouseClicked

    private void panelUpComingOptionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelUpComingOptionMouseClicked
        setListOfReservationsByOption(orderBUS.getReservationsByOption(lblUpComingRs.getText()));
        updatePanelBackground();
        this.panelUpComingOption.setBackground(Constants.COLOR_PRIMARY);
        getLblNumberOfUpComingRs().setForeground(Color.white);
        getLblUpComingRs().setForeground(Color.white);
    }//GEN-LAST:event_panelUpComingOptionMouseClicked

    private void panelPastOptionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelPastOptionMouseClicked
        setListOfReservationsByOption(orderBUS.getReservationsByOption(lblPastRs.getText()));
        updatePanelBackground();
        this.panelPastOption.setBackground(Constants.COLOR_PRIMARY);
        getLblNumberOfPastRs().setForeground(Color.white);
        getLblPastRs().setForeground(Color.white);
    }//GEN-LAST:event_panelPastOptionMouseClicked

    private void roundedButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton1ActionPerformed
        new DialogQrCodeScanner(null, this).setVisible(true);
    }//GEN-LAST:event_roundedButton1ActionPerformed

    public static RoundedPanel getRoundedPanel5() {
        return panel7Dates;
    }

    public void setListOfAllReservations() {
        panelReservations.removeAll();

        if (mapOfAllReservations.size() != 0) {
            panelReservations.add(description);
// Biến để tính tổng chiều cao của tất cả các PanelReservationByDate
            int totalHeight = 0;

            for (Map.Entry<String, List<OrderEntity>> entry : mapOfAllReservations.entrySet()) {
                String date = entry.getKey();
                List<OrderEntity> reservations = entry.getValue();

                PanelReservationByDate p = new PanelReservationByDate(this);

                p.setLblDate(date);

                // Thêm các PanelReservation vào PanelReservationByDate
                for (OrderEntity o : reservations) {
//                    System.out.println("1");
                    PanelReservation panelReservation = new PanelReservation(o, p, mainGUI);
                    p.getPanelReservations().add(panelReservation);
                }

                // Tính số hàng và chiều cao cho PanelReservationByDate
                int numOfReservations = reservations.size();
                int rows = (int) Math.ceil(numOfReservations / 5.0);  // Mỗi hàng chứa tối đa 5 panel
                int height = rows * 200 + (rows - 1) * 10 + rows * 30;  // 200 là chiều cao của mỗi panel, 10px là khoảng cách giữa các panel
                p.getPanelReservations().setPreferredSize(new Dimension(p.getPanelReservations().getWidth(), height));

                // Cộng dồn chiều cao cho totalHeight
                totalHeight += height;

                // Thêm PanelReservationByDate vào panelReservations
                panelReservations.add(p);

            }

            // Đặt kích thước cho panelReservations sau khi đã thêm tất cả các PanelReservationByDate
            panelReservations.setPreferredSize(new Dimension(panelReservations.getWidth(), totalHeight + 200)); // 500 là chiều cao bổ sung nếu cần

        } else {
            PanelReservationByDate p = new PanelReservationByDate(this);
            p.getLblDate().setHorizontalAlignment(SwingConstants.CENTER);
            p.getLblDate().setBorder(null);
            p.setLblDate("Hiện không có đơn đặt bàn nào.");
            panelReservations.add(p);
            panelReservations.revalidate();
            panelReservations.repaint();
        }
        panelReservations.revalidate();
        panelReservations.repaint();
    }

    public void setListOfReservationsByOption(List<OrderEntity> res) {
        panelReservations.removeAll();

        if (res.size() != 0) {
            panelReservations.add(description);
            String date = res.get(0).getReservationTime().format(DatetimeFormatterUtil.getDateFormatter());

            PanelReservationByDate p = new PanelReservationByDate(this);
            p.setLblDate(date);

            for (OrderEntity o : res) {
                PanelReservation panelReservation = new PanelReservation(o, p, mainGUI);
                p.getPanelReservations().add(panelReservation);
            }

            int numOfReservations = res.size();
            int rows = (int) Math.ceil(numOfReservations / 5.0);  // Mỗi hàng chứa tối đa 5 panel
            int height = rows * 200 + (rows - 1) * 10 + rows * 30;  // 200 là chiều cao của mỗi panel, 10px là khoảng cách giữa các panel
            p.getPanelReservations().setPreferredSize(new Dimension(p.getPanelReservations().getWidth(), height));

            panelReservations.add(p);

            panelReservations.setPreferredSize(new Dimension(panelReservations.getWidth(), height + 200)); // 500 là chiều cao bổ sung nếu cần

            panelReservations.revalidate();
            panelReservations.repaint();
        } else {
            PanelReservationByDate p = new PanelReservationByDate(this);
            p.getLblDate().setHorizontalAlignment(SwingConstants.CENTER);
            p.getLblDate().setBorder(null);
            p.setLblDate("Hiện không có đơn đặt bàn nào.");
            panelReservations.add(p);
            panelReservations.revalidate();
            panelReservations.repaint();
        }
    }

    public static TreeMap<String, List<OrderEntity>> getOrdersGroupByDate(List<OrderEntity> listOrders) {
        return listOrders.stream()
                .filter(order -> order.getReservationTime() != null)
                .sorted(Comparator.comparing(o -> o.getReservationTime().toLocalDate()))
                .collect(Collectors.groupingBy(
                        order -> order.getReservationTime().format(DatetimeFormatterUtil.getDateFormatter()),
                        TreeMap::new,
                        Collectors.toList()
                ));
    }

    public void addToMapOfAllReservations(OrderEntity orderEntity) {
        String dateKey = orderEntity.getReservationTime().format(DatetimeFormatterUtil.getDateFormatter());

        if (mapOfAllReservations.containsKey(dateKey)) {
            List<OrderEntity> reservationsForDate = mapOfAllReservations.get(dateKey);

            boolean isUpdated = false;
            for (int i = 0; i < reservationsForDate.size(); i++) {
                OrderEntity existingOrder = reservationsForDate.get(i);
                if (existingOrder.getOrderId().equalsIgnoreCase(orderEntity.getOrderId())) {
                    reservationsForDate.set(i, orderEntity);
                    isUpdated = true;
                    break;
                }
            }

            if (!isUpdated) {
                reservationsForDate.add(orderEntity);
            }
        } else {
            List<OrderEntity> newReservationsList = new ArrayList<>();
            newReservationsList.add(orderEntity);

            mapOfAllReservations.put(dateKey, newReservationsList);
        }
        updateNumberOfReservations(orderEntity.getReservationTime().toLocalDate(), orderEntity.getReservationTime().toLocalTime(), 1);
        setListOfAllReservations();
    }

    public void removeFromMapOfAllReservations(OrderEntity orderEntity) {
        String dateKey = orderEntity.getReservationTime().format(DatetimeFormatterUtil.getDateFormatter());

        if (mapOfAllReservations.containsKey(dateKey)) {
            List<OrderEntity> reservationsForDate = mapOfAllReservations.get(dateKey);
            reservationsForDate.remove(orderEntity);

            if (reservationsForDate.size() == 0) {
                mapOfAllReservations.remove(dateKey);
            }
        } else {
            List<OrderEntity> newReservationsList = new ArrayList<>();
            newReservationsList.add(orderEntity);

            mapOfAllReservations.put(dateKey, newReservationsList);
        }

        updateNumberOfReservations(orderEntity.getReservationTime().toLocalDate(), orderEntity.getReservationTime().toLocalTime(), -1);
        setListOfAllReservations();

        // In ra các phần tử trong Map
        System.out.println("Current reservations in the map:");
        for (Map.Entry<String, List<OrderEntity>> entry : mapOfAllReservations.entrySet()) {
            String key = entry.getKey();
            List<OrderEntity> value = entry.getValue();
            System.out.println("Date: " + key + " -> Orders: " + value);
        }
    }

    public void updatePanelBackground() {
        updatePanelBackground(this, Color.WHITE); // Đặt lại toàn bộ panel về màu trắng
    }

    private void updatePanelBackground(JComponent comp, Color color) {
        if (comp instanceof JPanel) {
            comp.setBackground(color);
        }

        for (Component child : comp.getComponents()) {
            // Kiểm tra nếu child là một JLabel trước khi ép kiểu
            if (child instanceof JLabel) {
                ((JLabel) child).setForeground(Constants.COLOR_TEXT);
            }

            // Kiểm tra nếu child là một JPanel trước khi tiếp tục đệ quy
            if (child instanceof JPanel) {
                if (child instanceof PanelDate) {
                    ((PanelDate) child).deselectPanel();
                }
                updatePanelBackground((JComponent) child, color); // Đệ quy cho các thành phần con
            }
        }
    }

//    public void removePanelReservation(PanelReservation panelReservation) {
//        
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblAllReservations;
    private javax.swing.JLabel lblNumberOfAll;
    private javax.swing.JLabel lblNumberOfPastRs;
    private javax.swing.JLabel lblNumberOfUpComingRs;
    private javax.swing.JLabel lblPastRs;
    private javax.swing.JLabel lblUpComingRs;
    private javax.swing.JPanel panel;
    private static gui.custom.RoundedPanel panel7Dates;
    private gui.custom.RoundedPanel panelAllReservations;
    private gui.custom.RoundedButton panelButtonCreate;
    private gui.custom.RoundedPanel panelOptions;
    private gui.custom.RoundedPanel panelPastOption;
    private static javax.swing.JPanel panelReservations;
    private javax.swing.JPanel panelSideBar;
    private gui.custom.RoundedPanel panelUpComingOption;
    private javax.swing.JPanel panelWrapper;
    private gui.custom.RoundedButton roundedButton1;
    private javax.swing.JScrollPane scrollPaneReservation;
    // End of variables declaration//GEN-END:variables
}
