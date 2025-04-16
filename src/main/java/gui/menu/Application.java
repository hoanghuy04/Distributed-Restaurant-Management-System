package gui.menu;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import gui.main.MainMenu;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import raven.toast.Notifications;

/**
 *
 * @author Raven
 */
public class Application extends javax.swing.JFrame {

    public static Application app;
    public final MainMenu mainMenu;

    public Application(int option) {
        setUndecorated(true);
        initComponents();
        setLocationRelativeTo(null);
        mainMenu = new MainMenu(app, option);
        setContentPane(mainMenu);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        Notifications.getInstance().setJFrame(this);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public static void showForm(Component component) {
        component.applyComponentOrientation(app.getComponentOrientation());
        app.mainMenu.showForm(component);
        app.repaint();
        app.revalidate();
    }

    public static void login() {
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.mainMenu);
        app.mainMenu.applyComponentOrientation(app.getComponentOrientation());
        setSelectedMenu(0, 0);
        app.mainMenu.hideMenu();
        SwingUtilities.updateComponentTreeUI(app.mainMenu);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

//    public static void logout() {
//        FlatAnimatedLafChange.showSnapshot();
//        app.setContentPane(app.mainMenu);
//        app.loginForm.applyComponentOrientation(app.getComponentOrientation());
//        SwingUtilities.updateComponentTreeUI(app.loginForm);
//        FlatAnimatedLafChange.hideSnapshotWithAnimation();
//    }
    public static void setSelectedMenu(int index, int subIndex) {
        app.mainMenu.setSelectedMenu(index, subIndex);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 719, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 521, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
