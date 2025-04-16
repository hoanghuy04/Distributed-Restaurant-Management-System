/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui.custom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author hoang
 */
public class RoundedComboBox extends JComboBox<String> {

    public RoundedComboBox() {
        setOpaque(false); // Để cho phép vẽ nền trong suốt
    }

    public RoundedComboBox(String[] reservationTimes) {
        super(reservationTimes);
        setOpaque(false); // Để cho phép vẽ nền trong suốt
    }

    public RoundedComboBox(List<String> reservationTimes) {
        super(reservationTimes.toArray(new String[0]));
        setOpaque(false); // Để cho phép vẽ nền trong suốt
    }
    
    public DefaultComboBoxModel<String> getDefaultModel() {
        return (DefaultComboBoxModel<String>) this.getModel();
    }
    
    public void setDefaultModel(String[] model) {
        DefaultComboBoxModel<String> dModel = new DefaultComboBoxModel<>(model);
        this.setModel(dModel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền bo tròn
        Shape roundedRect = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        g2.setColor(getBackground());
        g2.fill(roundedRect);

        // Vẽ nội dung của JTextField
        super.paintComponent(g);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ đường viền bo tròn
        Shape roundedRect = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        g2.setColor(new Color(999999));
        g2.draw(roundedRect);

        g2.dispose();
    }
}
