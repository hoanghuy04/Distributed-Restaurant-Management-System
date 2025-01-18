package dal;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.EmployeeEntity;
import model.RoleEntity;
import util.IDGeneratorUtil;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class RoleDAL implements BaseDAL<RoleEntity, String> {
    private EntityManager entityManager;

    @Override
    public boolean insert(RoleEntity roleEntity) {
//        roleEntity.setRoleId(IDGeneratorUtil.generateSimpleID("R","roles","role_id", entityManager));
        return BaseDAL.executeTransaction(entityManager, () -> entityManager.persist(roleEntity));
    }

    @Override
    public boolean update(RoleEntity RoleEntity) {
        return BaseDAL.executeTransaction(entityManager, () -> entityManager.merge(RoleEntity));
    }

    @Override
    public boolean deleteById(String s) {
        RoleEntity roleEntity = entityManager.find(RoleEntity.class, s);
        if (roleEntity != null) {
            return BaseDAL.executeTransaction(entityManager, () -> entityManager.remove(roleEntity));
        }
        return false;
    }

    @Override
    public Optional<RoleEntity> findById(String s) {
        return Optional.ofNullable(entityManager.find(RoleEntity.class, s));
    }

    @Override
    public List<RoleEntity> findAll() {
        return entityManager.createNamedQuery("RoleEntity.findAll", RoleEntity.class).getResultList();
    }
}
