package dal;

import dal.connectDB.ConnectDB;
import model.EmployeeEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import util.IDGeneratorUtility;

public class EmployeeDAL implements BaseDAL<EmployeeEntity, String> {

    private EntityManager em;

    public EmployeeDAL(EntityManager em) {
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
    public EmployeeEntity insert(EmployeeEntity t) {
        t.setEmployeeId(IDGeneratorUtility.generateIDWithCreatedDate("Emp", "employees", "employee_id", "created_date", em, LocalDateTime.now()));
        executeTransaction(() -> em.persist(t));

        return t;
    }
    @Override
    public boolean update(EmployeeEntity t) {
        return executeTransaction(() -> em.merge(t));
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public EmployeeEntity findById(String id) {
        return em.find(EmployeeEntity.class, id);
    }

    @Override
    public List<EmployeeEntity> findAll() {
        return em.createNamedQuery("EmployeeEntity.findAll", EmployeeEntity.class).getResultList();
    }

    public EmployeeEntity findByEmail(String email) {
        try {
            EmployeeEntity result = em.createNamedQuery("EmployeeEntity.findByEmail", EmployeeEntity.class)
                    .setParameter("email", "%" + email + "%")
                    .getSingleResult();
            return result != null ? result : null;
        } catch (Exception e) {
            return null;
        }
    }

    public Optional<EmployeeEntity> findByPhoneNumber(String phoneNumber) {
        try {
            EmployeeEntity result = em.createNamedQuery("EmployeeEntity.findByPhoneNumber", EmployeeEntity.class)
                    .setParameter("phoneNumber", "%" + phoneNumber + "%")
                    .getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<EmployeeEntity> getListEmployeeActive() {
        return em.createQuery("select e from EmployeeEntity e where e.active = :active").setParameter("active", true).getResultList();
    }

    public EmployeeEntity checkLogin(String username, String password) {
        String sql = "select e from EmployeeEntity e where e.id = ?1 AND e.password = ?2 ";

        Query q = em.createQuery(sql, EmployeeEntity.class);

        q.setParameter(1, username);
        q.setParameter(2, password);
        try {
            return (EmployeeEntity) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public List<EmployeeEntity> getEmployeesWithKeyword(String name, String phone, String address, String email, String pass, String roleId, boolean active) {

        StringBuilder queryBuilder = new StringBuilder("SELECT e FROM EmployeeEntity e WHERE 1=1");

        if (name != null && !name.trim().isEmpty()) {
            queryBuilder.append(" AND e.fullname LIKE :name");
        }
        if (phone != null && !phone.trim().isEmpty()) {
            queryBuilder.append(" AND e.phoneNumber LIKE :phone");
        }
        if (address != null && !address.trim().isEmpty()) {
            queryBuilder.append(" AND e.address LIKE :address");
        }
        if (email != null && !email.trim().isEmpty()) {
            queryBuilder.append(" AND e.email LIKE :email");
        }
        if (pass != null && !pass.trim().isEmpty()) {
            queryBuilder.append(" AND e.password LIKE :pass");
        }
        if (roleId != null && !roleId.trim().isEmpty()) {
            queryBuilder.append(" AND e.role.roleId = :roleId");
        }
        queryBuilder.append(" AND e.active = :active");

        Query query = em.createQuery(queryBuilder.toString(), EmployeeEntity.class);

        System.out.println(queryBuilder.toString());

        if (name != null && !name.trim().isEmpty()) {
            query.setParameter("name", "%" + name + "%");
        }
        if (phone != null && !phone.trim().isEmpty()) {
            query.setParameter("phone", "%"+ phone + "%");
        }
        if (address != null && !address.trim().isEmpty()) {
            query.setParameter("address", "%" + address + "%");
        }
        if (email != null && !email.trim().isEmpty()) {
            query.setParameter("email", "%" + email + "%");
        }
        if (pass != null && !pass.trim().isEmpty()) {
            query.setParameter("pass", "%" + pass + "%");
        }
        if (roleId != null && !roleId.trim().isEmpty()) {
            query.setParameter("roleId", roleId);
        }
        query.setParameter("active", active);

        System.out.println(query);
        return query.getResultList();
    }

    public static void main(String[] args) {
        EmployeeDAL employeeDAL = new EmployeeDAL(ConnectDB.getEntityManager());
        employeeDAL.getEmployeesWithKeyword(null, "", null, null, null, null, false)
                .forEach(System.out::println);
    }

}

