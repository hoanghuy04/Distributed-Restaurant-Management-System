package model.enums;

public enum OrderStatusEnum {
    SINGLE ("Đơn riêng lẻ"),
    MERGED ("Đơn đã gộp");

    private String orderStatus;

    private OrderStatusEnum(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
