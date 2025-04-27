package bus.request;

import model.OrderEntity;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallback extends Remote {
    void notifyOrderResult(boolean success, String message, OrderEntity order) throws RemoteException;
}
