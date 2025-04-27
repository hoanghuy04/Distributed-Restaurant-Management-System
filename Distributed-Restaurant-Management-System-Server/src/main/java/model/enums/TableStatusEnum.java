package model.enums;

public enum TableStatusEnum {
    AVAILABLE("Bàn trống"),
    OCCUPIED("Đang phục vụ"),
    PROCESSING("Đang xử lý"),;

    private String tableStatus;

    private TableStatusEnum(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    private String getTableStatus() {
        return tableStatus;
    }

    public static TableStatusEnum convertToEnum(String pMethod) {
        for (TableStatusEnum tableStatus : TableStatusEnum.values()) {
            if (tableStatus.getTableStatus().equalsIgnoreCase(pMethod)) {
                return tableStatus;
            }
        }
        throw new IllegalArgumentException("Phương thức " + pMethod + " không tồn tại!");
    }

}
