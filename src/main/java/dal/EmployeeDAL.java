package dal;

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
    public boolean insert(EmployeeEntity t) {
        t.setEmployeeId(IDGeneratorUtility.generateIDWithCreatedDate("Emp", "employees", "employee_id", "created_date", em, LocalDateTime.now()));
        return executeTransaction(() -> em.persist(t));
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
    public Optional<EmployeeEntity> findById(String id) {
        return Optional.ofNullable(em.find(EmployeeEntity.class, id));
    }

    @Override
    public List<EmployeeEntity> findAll() {
        return em.createNamedQuery("EmployeeEntity.findAll", EmployeeEntity.class).getResultList();
    }

    public Optional<EmployeeEntity> findByEmail(String email) {
        try {
            EmployeeEntity result = em.createNamedQuery("EmployeeEntity.findByEmail", EmployeeEntity.class)
                    .setParameter("email", "%" + email + "%")
                    .getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean deleteEmployeesByRole(String roleId) {

        return BaseDAL.executeTransaction(em, () -> {
            // Tìm tất cả các Employee có roleId tương ứng
            List<EmployeeEntity> employees = em.createQuery(
                    "SELECT e FROM EmployeeEntity e WHERE e.role.roleId = :roleId", EmployeeEntity.class)
                    .setParameter("roleId", roleId)
                    .getResultList();

            // Xóa từng Employee tìm thấy
            for (EmployeeEntity employee : employees) {
                em.remove(employee);
            }
        });
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

    public List<EmployeeEntity> getListEmployeeActive(String active) {
        String sql = "select e.* from employees e where e.active = 'True' ";

        Query q = em.createNativeQuery(sql, EmployeeEntity.class);

        return q.getResultList();
    }

    public EmployeeEntity checkLogin(String username, String password) {
        String sql = "select e.* from employees e where e.employee_id = ?1 AND e.password = ?2 ";

        Query q = em.createNativeQuery(sql, EmployeeEntity.class);

        q.setParameter(1, username);
        q.setParameter(2, password);
        try {
            return (EmployeeEntity) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public List<EmployeeEntity> getEmployeesWithKeyword(String name, String phone, String address, String email, String pass, String roleId, boolean active) {
        List<EmployeeEntity> Employees = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM employees WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            queryBuilder.append(" AND fullname LIKE ?");
            parameters.add("%" + name + "%");
        }
        if (phone != null && !phone.trim().isEmpty()) {
            queryBuilder.append(" AND phone_number LIKE ?");
            parameters.add("%" + phone + "%");
        }
        if (address != null && !address.trim().isEmpty()) {
            queryBuilder.append(" AND address LIKE ?");
            parameters.add("%" + address + "%");
        }
        if (email != null && !email.trim().isEmpty()) {
            queryBuilder.append(" AND email LIKE ?");
            parameters.add("%" + email + "%");
        }
        if (pass != null && !pass.trim().isEmpty()) {
            queryBuilder.append(" AND password LIKE ?");
            parameters.add("%" + pass + "%");
        }
        if (roleId != null && !roleId.trim().isEmpty()) {
            queryBuilder.append(" AND role_id = ?");
            parameters.add(roleId);
        }
        queryBuilder.append(" AND active = ?");
        parameters.add(active);

        Query query = em.createNativeQuery(queryBuilder.toString(), EmployeeEntity.class);

        // Thiết lập các tham số cho truy vấn
        for (int i = 0; i < parameters.size(); i++) {
            query.setParameter(i + 1, parameters.get(i));
        }

        Employees = query.getResultList();

        return Employees;
    }

}
