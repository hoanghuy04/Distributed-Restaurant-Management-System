package bus;

import model.CustomerEntity;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomerBUS extends BaseBUS<CustomerEntity, String> {
    @Override
    boolean insertEntity(CustomerEntity customer) throws RemoteException;

    @Override
    boolean updateEntity(CustomerEntity customer) throws RemoteException;

    @Override
    boolean deleteEntity(String id) throws RemoteException;

    @Override
    CustomerEntity getEntityById(String id) throws RemoteException;

    @Override
    List<CustomerEntity> getAllEntities() throws RemoteException;

    CustomerEntity findByPhone(String phoneNumber) throws RemoteException;

    List<CustomerEntity> getCustomersByKeyword(String name, String phoneNumber, String email, LocalDateTime dOB, String address) throws RemoteException;
}
