package model.enums;

public enum PaymentMethodEnum {
    CASH("Tiền mặt"),
    E_WALLET("Ví điện tử"),
    CREDIT_CARD("Ngân hàng");

    private String paymentMethod;

    private PaymentMethodEnum(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
