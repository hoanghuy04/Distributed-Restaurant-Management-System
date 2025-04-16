/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.RoleEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import util.IDGeneratorUtility;


public class RoleDAL implements BaseDAL<RoleEntity, String>{
    private EntityManager em;

    public RoleDAL(EntityManager em) {
        this.em = em;
    }

    private EntityTransaction getTransaction() {
        return this.em.getTransaction();
    }
    
    private boolean executeTransaction(Runnable action) {
        EntityTransaction et = this.getTransaction();
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
    public boolean insert(RoleEntity t) {
        t.setRoleId(IDGeneratorUtility.generateSimpleID("Role", "roles", "role_id", em));
        return executeTransaction(() -> em.persist(t));
    }

    @Override
    public boolean update(RoleEntity t) {
        return executeTransaction(() -> em.merge(t));
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public Optional<RoleEntity> findById(String id) {
        return Optional.ofNullable(em.find(RoleEntity.class, id));
    }

    @Override
    public List<RoleEntity> findAll() {
        return em.createNamedQuery("RoleEntity.findAll", RoleEntity.class).getResultList();
    }
    
    public RoleEntity findByName(String roleName) {
        return em.createNamedQuery("RoleEntity.findByName", RoleEntity.class).setParameter("roleName", roleName).getSingleResult();
    }
}
