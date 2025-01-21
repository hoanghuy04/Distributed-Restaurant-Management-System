package dal;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import model.CustomerEntity;
import util.IDGeneratorUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CustomerDAL implements BaseDAL<CustomerEntity,String> {
    private EntityManager em;

    @Override
    public boolean insert(CustomerEntity customerEntity) {
        customerEntity.setCustomerId(IDGeneratorUtil.generateIDWithCreatedDate("C","customers","customer_id","created_date",em, LocalDateTime.now()));
        return BaseDAL.executeTransaction(em,()->em.persist(customerEntity));
    }

    @Override
    public boolean update(CustomerEntity customerEntity) {
        return BaseDAL.executeTransaction(em,()->em.merge(customerEntity));
    }

    @Override
    public boolean deleteById(String s) {
        return false;
    }

    @Override
    public Optional<CustomerEntity> findById(String s) {
        return Optional.ofNullable(em.find(CustomerEntity.class, s));
    }

    @Override
    public List<CustomerEntity> findAll() {
        return em.createNamedQuery("CustomerEntity.findAll", CustomerEntity.class).getResultList();
    }
}
