package gui.component;

import common.Constants;
import gui.main.LoginGUI;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author RAVEN
 */
public class Header extends javax.swing.JPanel {

    private Timer timer;

    public Header() {
        initComponents();
        setOpaque(false);
        initClock();
//        lblEmpName.setText(LoginGUI.emp.getFullname());
    }

    public JLabel getLblEmpName() {
        return lblEmpName;
    }

    public void setLblEmpName(String empName) {
        this.lblEmpName.setText(empName);
    }

    private void initClock() {
        timer = new Timer(1000, e -> updateClock());
        timer.start();
        updateClock();
    }

    private void updateClock() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(new Date());
        lblClock.setText(currentTime);
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setPaint(Constants.COLOR_PRIMARY);
        g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        g2.dispose();
        super.paintComponent(grphcs);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblEmpName = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblClock = new javax.swing.JLabel();
        lblLogout = new javax.swing.JLabel();
        lblFAQ = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(951, 70));

        lblEmpName.setFont(new java.awt.Font("sansserif", 1, 20)); // NOI18N
        lblEmpName.setForeground(new java.awt.Color(255, 255, 255));
        lblEmpName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEmpName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon/png/icons8-user-48-white.png"))); // NOI18N
        lblEmpName.setIconTextGap(20);

        jLabel2.setFont(new java.awt.Font("sansserif", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(237, 237, 237));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon/png/logo.png"))); // NOI18N
        jLabel2.setText("Zenta Restaurant");

        lblClock.setFont(new java.awt.Font("sansserif", 1, 20)); // NOI18N
        lblClock.setForeground(new java.awt.Color(255, 255, 255));
        lblClock.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblClock.setIconTextGap(20);

        lblLogout.setFont(new java.awt.Font("sansserif", 1, 20)); // NOI18N
        lblLogout.setForeground(new java.awt.Color(237, 237, 237));
        lblLogout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon/png/icons8-move-up-48.png"))); // NOI18N
        lblLogout.setIconTextGap(20);
        lblLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLogoutMouseClicked(evt);
            }
        });

        lblFAQ.setFont(new java.awt.Font("sansserif", 1, 20)); // NOI18N
        lblFAQ.setForeground(new java.awt.Color(237, 237, 237));
        lblFAQ.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblFAQ.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon/png/icons8-faq-48.png"))); // NOI18N
        lblFAQ.setIconTextGap(20);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblEmpName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 234, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 235, Short.MAX_VALUE)
                .addComponent(lblClock)
                .addGap(31, 31, 31)
                .addComponent(lblFAQ)
                .addGap(40, 40, 40)
                .addComponent(lblLogout)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblEmpName, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblClock, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblFAQ, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lblLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLogoutMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lblLogoutMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblClock;
    private javax.swing.JLabel lblEmpName;
    private javax.swing.JLabel lblFAQ;
    private javax.swing.JLabel lblLogout;
    // End of variables declaration//GEN-END:variables
}
