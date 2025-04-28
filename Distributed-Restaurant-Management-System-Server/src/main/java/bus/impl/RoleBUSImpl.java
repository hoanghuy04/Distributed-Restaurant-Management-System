/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus.impl;

import bus.BaseBUS;
import dal.RoleDAL;
import jakarta.persistence.EntityManager;
import model.RoleEntity;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RoleBUSImpl extends UnicastRemoteObject implements bus.RoleBUS {
    private RoleDAL roleDAL;

    public RoleBUSImpl(EntityManager em)  throws RemoteException {
        this.roleDAL = new RoleDAL(em);
    }
    
    @Override
    public RoleEntity insertEntity(RoleEntity t)  throws RemoteException {
        return roleDAL.insert(t);
    }

    @Override
    public RoleEntity updateEntity(RoleEntity t)  throws RemoteException {
        return roleDAL.update(t);
    }

    @Override
    public boolean deleteEntity(String id)  throws RemoteException {
        return roleDAL.deleteById(id);
    }

    @Override
    public RoleEntity getEntityById(String id)  throws RemoteException {
        return roleDAL.findById(id);
    }

    @Override
    public List<RoleEntity> getAllEntities()  throws RemoteException {
        return roleDAL.findAll();
    }
    
    @Override
    public RoleEntity findByName(String name)  throws RemoteException {
        return roleDAL.findByName(name);
    }
    
}
