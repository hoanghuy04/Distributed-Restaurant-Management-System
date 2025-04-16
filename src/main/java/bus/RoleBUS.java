/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dal.RoleDAL;
import model.RoleEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class RoleBUS implements BaseBUS<RoleEntity, String>{
    private RoleDAL roleDAL;

    public RoleBUS(EntityManager em) {
        this.roleDAL = new RoleDAL(em);
    }
    
    @Override
    public boolean insertEntity(RoleEntity t) {
        return roleDAL.insert(t);
    }

    @Override
    public boolean updateEntity(RoleEntity t) {
        return roleDAL.update(t);
    }

    @Override
    public boolean deleteEntity(String id) {
        return roleDAL.deleteById(id);
    }

    @Override
    public RoleEntity getEntityById(String id) {
        Optional<RoleEntity> roleOptional = roleDAL.findById(id);
        return roleOptional.orElse(null);
    }

    @Override
    public List<RoleEntity> getAllEntities() {
        return roleDAL.findAll();
    }
    
    public RoleEntity findByName(String name) {
        return roleDAL.findByName(name);
    }
    
}
