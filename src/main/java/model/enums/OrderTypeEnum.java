package model.enums;

public enum OrderTypeEnum {
    ADVANCE ("Đơn đặt trước"),
    IMMEDIATE ("Đơn dùng ngay");

    private String orderType;

    private OrderTypeEnum(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
