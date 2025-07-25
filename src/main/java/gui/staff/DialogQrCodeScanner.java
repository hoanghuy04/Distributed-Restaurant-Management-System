/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package gui.staff;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.qrcode.QRCodeReader;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
/**
 *
 * @author pc
 */
public class DialogQrCodeScanner extends javax.swing.JDialog {

    /**
     * Creates new form DialogQrCodeScanner
     */
    private Webcam webcam;
    private TabReservation p;
    private Timer timer;

    /**
     * Creates new form NewJDialog
     */
    public DialogQrCodeScanner(java.awt.Frame parent, TabReservation par) {
        super(parent, true);
        p = par;
        initComponents();
        initWebcam();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout panelWrapperLayout = new javax.swing.GroupLayout(panelWrapper);
        panelWrapper.setLayout(panelWrapperLayout);
        panelWrapperLayout.setHorizontalGroup(
            panelWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        panelWrapperLayout.setVerticalGroup(
            panelWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelWrapper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelWrapper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void initWebcam() {
        webcam = Webcam.getDefault();
        if (webcam == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy webcam!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            webcam.open();
        } catch (WebcamException e) {
            JOptionPane.showMessageDialog(this, "Không thể mở webcam: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setPreferredSize(new Dimension(400, 300));
        panelWrapper.setLayout(new BorderLayout());
        panelWrapper.add(panel, BorderLayout.CENTER);

        timer = new Timer(500, e -> {
            String qrText = scanQRCode();
            System.out.println(qrText);
            if (qrText.matches("^O\\d{10}$")) {
                timer.stop();
                webcam.close();
                this.dispose();
                this.p.setTxtQRContent(qrText);
            }
        });
        timer.start();
    }

    private String scanQRCode() {
        // Lấy hình ảnh từ webcam
        BufferedImage image = webcam.getImage();

        // Kiểm tra nếu không có hình ảnh
        if (image == null) {
            return "Khong Co Hinh Anh";
        }

        try {
            // Giải mã QR code từ hình ảnh
            LuminanceSource source = new BufferedImageLuminanceSource(image);  // Sử dụng BufferedImageLuminanceSource từ ZXing
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

            // Thực hiện giải mã
            QRCodeReader reader = new QRCodeReader();
            Result result = reader.decode(bitmap, hints);

            // Hiển thị kết quả lên label
            String qrContent = result.getText();
            return qrContent;
        } catch (Exception e) {
            // Nếu không tìm thấy mã QR thì không làm gì cả
            return "Exception";
        }
    }
    
    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(DialogQrCodeScanner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(DialogQrCodeScanner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(DialogQrCodeScanner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(DialogQrCodeScanner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                DialogQrCodeScanner dialog = new DialogQrCodeScanner(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panelWrapper;
    // End of variables declaration//GEN-END:variables
}
