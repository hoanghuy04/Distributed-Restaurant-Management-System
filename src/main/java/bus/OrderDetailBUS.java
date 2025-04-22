package bus;

import model.OrderDetailEntity;
import model.OrderDetailId;

import java.rmi.RemoteException;
import java.util.List;

public interface OrderDetailBUS extends BaseBUS<OrderDetailEntity, OrderDetailId> {
    @Override
    boolean insertEntity(OrderDetailEntity orderDetail) throws RemoteException;

    @Override
    boolean updateEntity(OrderDetailEntity orderDetail) throws RemoteException;

    @Override
    boolean deleteEntity(OrderDetailId id) throws RemoteException;

    @Override
    OrderDetailEntity getEntityById(OrderDetailId id) throws RemoteException;

    @Override
    List<OrderDetailEntity> getAllEntities() throws RemoteException;
}
