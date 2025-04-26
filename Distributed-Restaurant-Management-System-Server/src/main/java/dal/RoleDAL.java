/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.RoleEntity;
import util.IDGeneratorUtility;

import java.util.List;


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
    public RoleEntity insert(RoleEntity t) {
        t.setRoleId(IDGeneratorUtility.generateSimpleID("Role", "roles", "role_id", em));
        executeTransaction(() -> em.persist(t));
        return t;
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
    public RoleEntity findById(String id) {
        return em.find(RoleEntity.class, id);
    }

    @Override
    public List<RoleEntity> findAll() {
        return em.createNamedQuery("RoleEntity.findAll", RoleEntity.class).getResultList();
    }

    public RoleEntity findByName(String roleName) {
        return em.createNamedQuery("RoleEntity.findByName", RoleEntity.class).setParameter("name", roleName).getSingleResult();
    }
}
