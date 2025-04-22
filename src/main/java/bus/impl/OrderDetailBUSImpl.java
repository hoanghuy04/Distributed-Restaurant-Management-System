package bus.impl;

import bus.BaseBUS;
import dal.OrderDetailDAL;
import model.OrderDetailEntity;
import model.OrderDetailId;
import jakarta.persistence.EntityManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class OrderDetailBUSImpl extends UnicastRemoteObject implements bus.OrderDetailBUS {

    private OrderDetailDAL orderDetailDAL;

    public OrderDetailBUSImpl(EntityManager entityManager)  throws RemoteException {
        this.orderDetailDAL = new OrderDetailDAL(entityManager);
    }

    @Override
    public boolean insertEntity(OrderDetailEntity orderDetail)  throws RemoteException {
        return orderDetailDAL.insert(orderDetail);
    }

    @Override
    public boolean updateEntity(OrderDetailEntity orderDetail)  throws RemoteException {
        return orderDetailDAL.update(orderDetail);
    }

    @Override
    public boolean deleteEntity(OrderDetailId id)  throws RemoteException {
        return orderDetailDAL.deleteById(id);
    }

    @Override
    public OrderDetailEntity getEntityById(OrderDetailId id)  throws RemoteException {
       return orderDetailDAL.findById(id);
    }

    @Override
    public List<OrderDetailEntity> getAllEntities()  throws RemoteException {
        return orderDetailDAL.findAll();
    }
}
