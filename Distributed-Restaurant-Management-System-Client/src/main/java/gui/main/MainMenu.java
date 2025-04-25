package gui.main;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.UIScale;
import gui.menu.Application;
import gui.manager.CategoryGUI;
import gui.manager.FrequencyStatsGUI;
import gui.manager.ItemStatsGUI;
import gui.manager.PromotionStatsGUI;
import gui.manager.RevenueStatsGUI;
import gui.manager.EmployeeGUI;
import gui.manager.FloorGUI;
import gui.manager.PromotionGUI;
import gui.manager.SearchGUI;
import gui.manager.TableGUI;
import gui.manager.ToppingGUI;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import javax.swing.*;

import gui.menu.Menu;
import gui.menu.MenuAction;
import gui.staff.CustomerGUI;
import gui.staff.DialogAboutPage;
import gui.staff.ItemGUI;
import gui.staff.MainGUI;
import gui.staff.OverviewGUI;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author Raven
 */
public class MainMenu extends JLayeredPane {

    private Application application;
    private MainGUI mainGUI;
    private int option;

    public MainMenu(Application application, int option) throws Exception {
        this.application = application;
        this.mainGUI = new MainGUI(application);
        this.option = option;
        init(option);
    }

    private void init(int option) throws Exception {
        setLayout(new MainFormLayout());
        menu = new Menu(option);
        panelBody = new JPanel(new BorderLayout());
        initMenuArrowIcon();
        menuButton.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.button.background;"
                + "arc:999;"
                + "focusWidth:0;"
                + "borderWidth:0");
        menuButton.addActionListener((ActionEvent e) -> {
            setMenuFull(!menu.isMenuFull());
        });
        initMenuEvent(option);
        setLayer(menuButton, JLayeredPane.POPUP_LAYER);
        add(menuButton);
        add(menu);
        add(panelBody);
    }

    @Override
    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);
        initMenuArrowIcon();
    }

    private void initMenuArrowIcon() {
        if (menuButton == null) {
            menuButton = new JButton();
        }
        String icon = (getComponentOrientation().isLeftToRight()) ? "menu_left.svg" : "menu_right.svg";
        menuButton.setIcon(new FlatSVGIcon(getClass().getResource("/img/icon/svg/" + icon)));

    }

    private void initMenuEvent(int option) {
        menu.addMenuEvent((int index, int subIndex, MenuAction action) -> {
            if (index == 0) {
                if (option == 1) {
                    Application.showForm(new MainGUI(application));
                } else if (option == 2) {
                    Application.showForm(new OverviewGUI());
                }
            } else if (index == 1) {
                Application.showForm(new CategoryGUI());
            } else if (index == 2) {
                Application.showForm(new ItemGUI());
            } else if (index == 3) {
                Application.showForm(new ToppingGUI());
            } else if (index == 4) {
                Application.showForm(new FloorGUI());
            } else if (index == 5) {
                Application.showForm(new TableGUI());
            } else if (index == 6) {
                Application.showForm(new CustomerGUI());
            } else if (index == 7) {
                if (option == 1) {
                    Application.showForm(new PromotionGUI());
                } else if (option == 2) {
                    Application.showForm(new EmployeeGUI());
                }
            } else if (index == 8) {
                if (option == 1) {
                    Application.showForm(new OverviewGUI());
                } else if (option == 2) {
                    Application.showForm(new PromotionGUI());
                }
            } else if (index == 9) {
                if (option == 1) {
                    if (JOptionPane.showConfirmDialog(null, "Để tiếp tục, vui lòng xác nhận đồng ý với các điều khoản và điều kiện sử dụng. Bạn sẽ được chuyển đến trang tiếp theo sau khi đồng ý.", "Cảnh bào", JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                        try {
                            URI uri = new URI("https://docs.google.com/document/d/1gWEZCmhZCYN6iJE-bSngEy-E-fPjSXTeLJa4NXzQ2QM/edit?tab=t.0#heading=h.3znysh7"); // Liên kết bạn muốn mở
                            Desktop.getDesktop().browse(uri);
                        } catch (URISyntaxException | java.io.IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else if (option == 2) {
                    Application.showForm(new SearchGUI());
                }
            } else if (index == 10) {
                if (option == 1) {
                    new DialogAboutPage(null, true, this).setVisible(true);
                } else if (option == 2) {
                    if (subIndex == 1) {
                        Application.showForm(new RevenueStatsGUI());
                    } else if (subIndex == 2) {
                        Application.showForm(new FrequencyStatsGUI());
                    } else if (subIndex == 3) {
                        Application.showForm(new PromotionStatsGUI());
                    } else if (subIndex == 4) {
                        Application.showForm(new ItemStatsGUI());
                    }
                }

            } else if (index == 11) {
                if (option == 1) {
                    new LoginGUI().setVisible(true);
                    this.setVisible(false);
                } else if (option == 2) {
                    if (JOptionPane.showConfirmDialog(null, "Để tiếp tục, vui lòng xác nhận đồng ý với các điều khoản và điều kiện sử dụng. Bạn sẽ được chuyển đến trang tiếp theo sau khi đồng ý.", "Cảnh bào", JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                        try {
                            URI uri = new URI("https://docs.google.com/document/d/1gWEZCmhZCYN6iJE-bSngEy-E-fPjSXTeLJa4NXzQ2QM/edit?tab=t.0#heading=h.3znysh7"); // Liên kết bạn muốn mở
                            Desktop.getDesktop().browse(uri);
                        } catch (URISyntaxException | java.io.IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } else if (index == 12) {
                new DialogAboutPage(null, true, this).setVisible(true);
            } else if (index == 13) {
                new LoginGUI().setVisible(true);
                this.setVisible(false);
            } else {
                action.cancel();
            }
        });
    }

    private void setMenuFull(boolean full) {
        String icon;
        if (getComponentOrientation().isLeftToRight()) {
            icon = (full) ? "menu_left.svg" : "menu_right.svg";
        } else {
            icon = (full) ? "menu_right.svg" : "menu_left.svg";
        }
        menuButton.setIcon(new FlatSVGIcon(getClass().getResource("/img/icon/svg/" + icon)));
        menu.setMenuFull(full);
        revalidate();
    }

    public void hideMenu() {
        menu.hideMenuItem();
    }

    public void showForm(Component component) {
        panelBody.removeAll();
        panelBody.add(component);
        panelBody.repaint();
        panelBody.revalidate();
    }

    public void setSelectedMenu(int index, int subIndex) throws Exception {
        menu.setSelectedMenu(index, subIndex);
    }

    private Menu menu;
    private JPanel panelBody;
    private JButton menuButton;

    private class MainFormLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(5, 5);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                boolean ltr = parent.getComponentOrientation().isLeftToRight();
                Insets insets = UIScale.scale(parent.getInsets());
                int x = insets.left;
                int y = insets.top;
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int menuWidth = UIScale.scale(menu.isMenuFull() ? menu.getMenuMaxWidth() : menu.getMenuMinWidth());
                int menuX = ltr ? x : x + width - menuWidth;
                menu.setBounds(menuX, y, menuWidth, height);
                int menuButtonWidth = menuButton.getPreferredSize().width;
                int menuButtonHeight = menuButton.getPreferredSize().height;
                int menubX;
                if (ltr) {
                    menubX = (int) (x + menuWidth - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.3f)));
                } else {
                    menubX = (int) (menuX - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.7f)));
                }
                menuButton.setBounds(menubX, UIScale.scale(50), menuButtonWidth, menuButtonHeight);
                int gap = UIScale.scale(5);
                int bodyWidth = width - menuWidth - gap;
                int bodyHeight = height;
                int bodyx = ltr ? (x + menuWidth + gap) : x;
                int bodyy = y;
                panelBody.setBounds(bodyx, bodyy, bodyWidth, bodyHeight);
            }
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public MainGUI getMainGUI() {
        return mainGUI;
    }

    public void setMainGUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

}
