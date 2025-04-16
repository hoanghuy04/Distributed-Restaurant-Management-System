package bus;

import dal.CustomerDAL;
import model.CustomerEntity;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class CustomerBUS implements BaseBUS<CustomerEntity, String> {
    private CustomerDAL customerDAL;

    public CustomerBUS(EntityManager entityManager) {
        this.customerDAL = new CustomerDAL(entityManager);
    }

    @Override
    public boolean insertEntity(CustomerEntity customer) {
        return customerDAL.insert(customer);
    }

    @Override
    public boolean updateEntity(CustomerEntity customer) {
        return customerDAL.update(customer);
    }
    
    @Override
    public boolean deleteEntity(String id) {
        return customerDAL.deleteById(id);
    }
    
    @Override
    public CustomerEntity getEntityById(String id) {
        Optional<CustomerEntity> optionalCustomer = customerDAL.findById(id);
        return optionalCustomer.orElse(null); // Return null if not found
    }

    @Override
    public List<CustomerEntity> getAllEntities() {
        return customerDAL.findAll();
    }
    
    public CustomerEntity findByPhone(String phoneNumber) {
        return customerDAL.findByPhone(phoneNumber);
    }
    
    public List<CustomerEntity> getCustomersByKeyword(String name, String phoneNumber, String email, LocalDateTime dOB, String address) {
        return customerDAL.getCustomersByKeyword(name, phoneNumber, email, dOB, address);
    }
}
