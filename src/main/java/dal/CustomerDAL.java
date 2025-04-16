/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.CustomerEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;
import util.IDGeneratorUtility;

/**
 *
 * @author hoang
 */
public class CustomerDAL implements BaseDAL<CustomerEntity, String> {

    private EntityManager em;

    public CustomerDAL(EntityManager em) {
        this.em = em;
    }

    private EntityTransaction getEntityTransaction() {
        return em.getTransaction();
    }

    private boolean executeTransaction(Runnable action) {
        EntityTransaction et = getEntityTransaction();

        try {
            et.begin();
            action.run();
            et.commit();
            return true;
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
        }
        return false;
    }

    @Override
    public boolean insert(CustomerEntity t) {
        t.setCustomerId(IDGeneratorUtility.generateIDWithCreatedDate("Cust", "customers", "customer_id", "created_date", em, LocalDateTime.now()));
        return executeTransaction(() -> em.persist(t));
    }

    @Override
    public boolean update(CustomerEntity t) {
        return executeTransaction(() -> em.merge(t));
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public Optional<CustomerEntity> findById(String id) {
        return Optional.ofNullable(em.find(CustomerEntity.class, id));
    }

    @Override
    public List<CustomerEntity> findAll() {
        return em.createNamedQuery("CustomerEntity.findAll", CustomerEntity.class).getResultList();
    }

    public CustomerEntity findByPhone(String phoneNumber) {
        try {
            return em.createNamedQuery("CustomerEntity.findByPhone", CustomerEntity.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CustomerEntity> getCustomersByKeyword(String name, String phoneNumber, String email, LocalDateTime dOB, String address) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM customers WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            queryBuilder.append("AND name LIKE ? ");
            parameters.add("%" + name + "%");
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            queryBuilder.append("AND phone_number LIKE ? ");
            parameters.add("%" + phoneNumber + "%");
        }
        if (email != null && !email.isEmpty()) {
            queryBuilder.append("AND email LIKE ? ");
            parameters.add("%" + email + "%");
        }
        if (dOB != null) {
            queryBuilder.append("AND date_of_birth = ? ");
            parameters.add(dOB);
        }
        if (address != null && !address.isEmpty()) {
            queryBuilder.append("AND address LIKE ? ");
            parameters.add("%" + address + "%");
        }

        Query query = em.createNativeQuery(queryBuilder.toString(), CustomerEntity.class);

        // Thiết lập tham số cho truy vấn
        for (int i = 0; i < parameters.size(); i++) {
            query.setParameter(i + 1, parameters.get(i));
        }

        return query.getResultList();
    }
}
