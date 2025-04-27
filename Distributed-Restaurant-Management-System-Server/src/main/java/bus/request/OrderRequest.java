package bus.request;

import model.OrderEntity;
import model.enums.PaymentStatusEnum;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class OrderRequest implements Serializable {
    private OrderEntity orderEntity;
    private PaymentStatusEnum paymentStatus;
    private ClientCallback callback; // Callback để thông báo cho client

    public OrderRequest(OrderEntity orderEntity, PaymentStatusEnum paymentStatus, ClientCallback callback) {
        this.orderEntity = orderEntity;
        this.paymentStatus = paymentStatus;
        this.callback = callback;
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public PaymentStatusEnum getPaymentStatus() {
        return paymentStatus;
    }

    public ClientCallback getCallback() {
        return callback;
    }
}

