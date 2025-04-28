package bus;

import model.OrderEntity;

import java.rmi.Remote;

public interface MailBUS extends Remote {
    void sendBookingConfirmation(OrderEntity order) throws Exception;
}
