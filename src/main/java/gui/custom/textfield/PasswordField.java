package gui.custom.textfield;

import common.Constants;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class PasswordField extends JPasswordField {

    private int borderRadius = 15; // Độ bo tròn
    private boolean hasFocus = false; // Trạng thái focus của TextField

    public boolean isShowAndHide() {
        return showAndHide;
    }

    public void setShowAndHide(boolean showAndHide) {
        this.showAndHide = showAndHide;
        repaint();
    }

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
    private final Image eye;
    private final Image eye_hide;
    private boolean hide = true;
    private boolean showAndHide;

    public PasswordField() {
        setBorder(new EmptyBorder(20, 10, 10, 3));
        setSelectionColor(Constants.COLOR_PRIMARY);

        setCaretColor(Constants.COLOR_TEXT);

        addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent me) {
//                mouseOver = true;
//                repaint();
//            }
//
//            @Override
//            public void mouseExited(MouseEvent me) {
//                mouseOver = false;
//                repaint();
//            }

            @Override
            public void mousePressed(MouseEvent me) {
                if (showAndHide) {
                    int x = getWidth() - 30;
                    if (new Rectangle(x, 0, 30, 30).contains(me.getPoint())) {
                        hide = !hide;
                        if (hide) {
                            setEchoChar('*');
                        } else {
                            setEchoChar((char) 0);
                        }
                        repaint();
                    }
                }
            }
        });
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent fe) {
                showing(false);
                hasFocus = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent fe) {
                showing(true);
                hasFocus = false;
                repaint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                if (showAndHide) {
                    int x = getWidth() - 30;
                    if (new Rectangle(x, 0, 30, 30).contains(me.getPoint())) {
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                    } else {
                        setCursor(new Cursor(Cursor.TEXT_CURSOR));
                    }
                }
            }
        });
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void begin() {
                animateHinText = String.valueOf(getPassword()).equals("");
            }

            @Override
            public void timingEvent(float fraction) {
                location = fraction;
                repaint();
            }

        };
        eye = new ImageIcon(getClass().getResource("/img/icon/png/eye.png")).getImage();
        eye_hide = new ImageIcon(getClass().getResource("/img/icon/png/eye_hide.png")).getImage();
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
//        createLineStyle(g2);
        if (showAndHide) {
            createShowHide(g2);
        }
        g2.dispose();
    }

    private void createShowHide(Graphics2D g2) {
        int x = getWidth() - 30 + 5;
        int y = (getHeight() - 20) / 2;
        g2.drawImage(hide ? eye_hide : eye, x, y, null);
    }

    private void createHintText(Graphics2D g2) {
        Insets in = getInsets();
        g2.setColor(new Color(120, 120, 120));
// Thiết lập font in đậm
        g2.setFont(g2.getFont().deriveFont(Font.BOLD));
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
        g2.drawString(labelText, in.left, (int) (in.top + textY + ft.getAscent() - size));
    }

//    private void createLineStyle(Graphics2D g2) {
//        if (isFocusOwner()) {
//            double width = getWidth() - 4;
//            int height = getHeight();
////            g2.setColor(lineColor);
//            double size;
//            if (show) {
//                size = width * (1 - location);
//            } else {
//                size = width * location;
//            }
//            double x = (width - size) / 2;
//            g2.fillRect((int) (x + 2), height - 2, (int) size, 2);
//        }
//    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền bo tròn
//        g2.setColor(getBackground());
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
        if (!String.valueOf(getPassword()).equals(string)) {
            showing(string.equals(""));
        }
        super.setText(string);
    }
}
