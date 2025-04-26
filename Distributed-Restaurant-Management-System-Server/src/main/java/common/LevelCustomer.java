/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package common;

/**
 *
 * @author hoang
 */
public enum LevelCustomer {
    NEW("Khách hàng mới"),
    POTENTIAL("Khách hàng tiềm năng"),
    VIP("Khách hàng VIP");

    private String levelCustomer;

    private LevelCustomer(String levelCustomer) {
        this.levelCustomer = levelCustomer;
    }

    public String getLevelCustomer() {
        return levelCustomer;
    }

    public void setLevelCustomer(String levelCustomer) {
        this.levelCustomer = levelCustomer;
    }

    // Phương thức để lấy enum từ chuỗi
    public static LevelCustomer convertToEnum(String text) {
        for (LevelCustomer level : LevelCustomer.values()) {
            if (level.levelCustomer.equalsIgnoreCase(text)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy LevelCustomer cho: " + text);
    }
}
