package gui.custom.textfield;

import common.Constants;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class TextField extends JTextField {

    private int borderRadius = 15; // Độ bo tròn
    private boolean hasFocus = false; // Trạng thái focus của TextField

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

//    public Color getLineColor() {
//        return lineColor;
//    }
//
//    public void setLineColor(Color lineColor) {
//        this.lineColor = lineColor;
//    }
    private final Animator animator;
    private boolean animateHinText = true;
    private float location;
    private boolean show;
//    private boolean mouseOver = false;
    private String labelText = "Label";
//    private Color lineColor = Color.white;

    public TextField() {
        setBorder(new EmptyBorder(20, 10, 10, 3)); // Thay đổi 10 để thêm khoảng cách 2 space bên trái
        setSelectionColor(Constants.COLOR_PRIMARY);

        // Thiết lập màu con trỏ chuột là màu trắng
        setCaretColor(Constants.COLOR_TEXT);

//        addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent me) {
////                mouseOver = true;
//                repaint();
//            }
//
//            @Override
//            public void mouseExited(MouseEvent me) {
////                mouseOver = false;
//                repaint();
//            }
//        });
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent fe) {
                hasFocus = true;
                showing(false);
                repaint();
            }

            @Override
            public void focusLost(FocusEvent fe) {
                hasFocus = false;
                showing(true);
                repaint();
            }
        });
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void begin() {
                animateHinText = getText().equals("");
            }

            @Override
            public void timingEvent(float fraction) {
                location = fraction;
                repaint();
            }

        };
        animator = new Animator(300, target);
        animator.setResolution(0);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
    }

    private void showing(boolean action) {
        if (animator.isRunning()) {
            animator.stop();
        } else {
            location = 1;
        }
        animator.setStartFraction(1f - location);
        show = action;
        location = 1f - location;
        animator.start();
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        int width = getWidth();
        int height = getHeight();
//        if (mouseOver) {
//            g2.setColor(lineColor);
//        } else {
//            g2.setColor(new Color(150, 150, 150));
//        }
        g2.fillRect(2, height - 1, width - 4, 1);
        createHintText(g2);
        g2.dispose();
    }

    private void createHintText(Graphics2D g2) {
        Insets in = getInsets();
        g2.setColor(new Color(120, 120, 120));
// Thiết lập font in đậm
        g2.setFont(g2.getFont().deriveFont(Font.BOLD));
        FontMetrics ft = g2.getFontMetrics();
        Rectangle2D r2 = ft.getStringBounds(labelText, g2);
        double height = getHeight() - in.top - in.bottom;
        double textY = (height - r2.getHeight()) / 2;
        double size;
        if (animateHinText) {
            if (show) {
                size = 18 * (1 - location);
            } else {
                size = 18 * location;
            }
        } else {
            size = 18;
        }
        g2.drawString(labelText, in.right, (int) (in.top + textY + ft.getAscent() - size));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền bo tròn trong khu vực đường viền
//        g2.setColor(Color.red);
        g2.fill(new RoundRectangle2D.Double(2, 1, getWidth() - 4, getHeight() - 3, borderRadius, borderRadius));

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ đường viền bo tròn khi TextField có focus
        if (hasFocus) {
//            g2.setColor(Color.GRAY); // Màu xám cho đường viền
            g2.setStroke(new java.awt.BasicStroke(2)); // Độ dày của đường viền là 3 pixel
            g2.draw(new RoundRectangle2D.Double(1.5, 1.5, getWidth() - 3, getHeight() - 3, borderRadius, borderRadius));
        }

        g2.dispose();
    }

    @Override
    public void setText(String string) {
        if (!getText().equals(string)) {
            showing(string.equals(""));
        }
        super.setText(string);
    }
}
