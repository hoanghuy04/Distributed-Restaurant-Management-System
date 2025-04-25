/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui.custom;

import common.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedTextField extends JTextField {

    public RoundedTextField() {
        setOpaque(false); // Để cho phép vẽ nền trong suốt
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền bo tròn
        Shape roundedRect = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        g2.setColor(Color.WHITE);
        g2.fill(roundedRect);

        // Vẽ nội dung của JTextField
        super.paintComponent(g);

        g2.dispose();
    }

    @Override
    public Insets getInsets() {
        return new Insets(5, 10, 5, 10); // top, left, bottom, right
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ đường viền bo tròn
        Shape roundedRect = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        g2.setColor(Constants.COLOR_BORDER);
        g2.draw(roundedRect);

        g2.dispose();
    }
}
