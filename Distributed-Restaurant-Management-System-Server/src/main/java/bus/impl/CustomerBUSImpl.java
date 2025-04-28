package bus.impl;

import bus.BaseBUS;
import dal.CustomerDAL;
import model.CustomerEntity;
import jakarta.persistence.EntityManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.List;


public class CustomerBUSImpl extends UnicastRemoteObject implements bus.CustomerBUS  {
    private CustomerDAL customerDAL;

    public CustomerBUSImpl(EntityManager entityManager)  throws RemoteException {
        this.customerDAL = new CustomerDAL(entityManager);
    }

    @Override
    public CustomerEntity insertEntity(CustomerEntity customer)  throws RemoteException {
        return customerDAL.insert(customer);
    }

    @Override
    public CustomerEntity updateEntity(CustomerEntity customer)  throws RemoteException {
        return customerDAL.update(customer);
    }
    
    @Override
    public boolean deleteEntity(String id)  throws RemoteException {
        return customerDAL.deleteById(id);
    }
    
    @Override
    public CustomerEntity getEntityById(String id)  throws RemoteException {
        return customerDAL.findById(id);
    }

    @Override
    public List<CustomerEntity> getAllEntities()  throws RemoteException {
        return customerDAL.findAll();
    }
    
    @Override
    public CustomerEntity findByPhone(String phoneNumber)  throws RemoteException {
        return customerDAL.findByPhone(phoneNumber);
    }
    
    @Override
    public List<CustomerEntity> getCustomersByKeyword(String name, String phoneNumber, String email, LocalDateTime dOB)  throws RemoteException {
        return customerDAL.getCustomersByKeyword(name, phoneNumber, email, dOB);
    }

    @Override
    public boolean existsByPhoneOrEmail(String phone, String email) throws RemoteException {
        return customerDAL.existsByPhoneOrEmail(phone, email);
    }

}
