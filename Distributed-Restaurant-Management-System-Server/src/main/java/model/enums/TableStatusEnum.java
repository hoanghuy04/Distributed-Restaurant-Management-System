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


}
