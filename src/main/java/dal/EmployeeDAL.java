package dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import model.EmployeeEntity;
import model.OrderDetailEntity;
import model.PromotionEntity;

import java.util.List;
import java.util.Optional;

public class EmployeeDAL implements BaseDAL<EmployeeEntity, String>{
    private EntityManager entityManager;
    @Override
    public boolean insert(EmployeeEntity employeeEntity) {
        return BaseDAL.executeTransaction(entityManager, () -> entityManager.persist(employeeEntity));
    }

    @Override
    public boolean update(EmployeeEntity employeeEntity) {
        return BaseDAL.executeTransaction(entityManager, () -> entityManager.merge(employeeEntity));
    }

    @Override
    public boolean deleteById(String s) {
        EmployeeEntity employeeEntity = entityManager.find(EmployeeEntity.class, s);
        if (employeeEntity != null) {
            return BaseDAL.executeTransaction(entityManager, () -> entityManager.remove(employeeEntity));
        }
        return false;
    }

    @Override
    public Optional<EmployeeEntity> findById(String s) {
        return Optional.ofNullable(entityManager.find(EmployeeEntity.class, s));
    }

    @Override
    public List<EmployeeEntity> findAll() {
        return entityManager.createNamedQuery("EmployeeEntity.findAll", EmployeeEntity.class).getResultList();
    }

    public Optional<EmployeeEntity> findByEmail(String email) {
        try {
            EmployeeEntity result = entityManager.createNamedQuery("EmployeeEntity.findByEmail", EmployeeEntity.class)
                    .setParameter("email", "%" + email + "%")
                    .getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<EmployeeEntity> findByPhoneNumber(String phoneNumber) {
        try {
            EmployeeEntity result = entityManager.createNamedQuery("EmployeeEntity.findByPhoneNumber", EmployeeEntity.class)
                    .setParameter("phoneNumber", "%" + phoneNumber + "%")
                    .getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
