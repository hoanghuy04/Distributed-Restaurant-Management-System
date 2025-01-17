package model.enums;


/*
 * @description: SizeEnum
 * @author: Trần Ngọc Huyền
 * @date: 1/17/2025
 * @version: 1.0
 */
public enum SizeEnum {
    SMALL("S"),
    MEDIUM("M"),
    LARGE("L");
    private String size;
    private SizeEnum(String size) {
        this.size = size;
    }
    public String getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "SizeEnum{" +
                "size='" + size + '\'' +
                '}';
    }
}
