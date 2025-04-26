package model.enums;

public enum PaymentStatusEnum {
    UNPAID ("Chưa thanh toán"),
    PAID ("Đã thanh toán");

    private String paymentStatus;

    private PaymentStatusEnum(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
