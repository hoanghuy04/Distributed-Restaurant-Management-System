package common;

import java.awt.Color;
import org.mindrot.jbcrypt.BCrypt;

public class Constants {

    public static final String REPORT_FILE_PATH = System.getProperty("user.dir") + "/reports/";
    public static final String HOTLINE = "028.3823.6378";
    public static final int RESERVATION_TIMEOUT_MINUTES = 60;
    public static final Color COLOR_PRIMARY = new Color(10, 114, 185);
    public static final Color COLOR_SECONDARY = new Color(25, 178, 255);
    public static final Color COLOR_CLICK = new Color(9, 105, 170);
    public static final Color COLOR_CHOSEN = new Color(8, 96, 155);
    public static final Color COLOR_REVENUE = new Color(153, 221, 255);
    public static final Color COLOR_CAPITAL = new Color(255, 195, 77);
    public static final Color COLOR_PROFIT = new Color(0, 204, 0);
    public static final Color COLOR_GRAY = new Color(102, 102, 102);
    public static final Color COLOR_GREEN = new Color(34, 161, 84);
    public static final Color COLOR_ORANGE = new Color(235, 120, 43);
    public static final Color COLOR_BG = new Color(250, 250, 250);
    public static final Color COLOR_BTN_ICON = new Color(241, 242, 244);
    public static final Color COLOR_BORDER = new Color(218, 220, 221);
    public static final Color COLOR_RED = new Color(232, 76, 34);
    public static final Color COLOR_BROWN = new Color(139, 69, 17);
    public static final Color COLOR_STRONG_BLUE = new Color(0, 0, 139);
    public static final Color COLOR_TEXT = new Color(34, 34, 34);
    public static final String SALT = BCrypt.gensalt();

}
