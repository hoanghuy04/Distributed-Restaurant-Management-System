/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus.impl;

import bus.BaseBUS;
import dal.CategoryDAL;
import model.CategoryEntity;
import jakarta.persistence.EntityManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *
 * @author pc
 */
public class CategoryBUSImpl extends UnicastRemoteObject implements bus.CategoryBUS  {
    private CategoryDAL categoryDAL;
    public CategoryBUSImpl(EntityManager entityManager) throws RemoteException {
        this.categoryDAL = new CategoryDAL(entityManager);
    }

    @Override
    public CategoryEntity insertEntity(CategoryEntity category)  throws RemoteException {
        return categoryDAL.insert(category);
    }

    @Override
    public boolean updateEntity(CategoryEntity category)  throws RemoteException {
        return categoryDAL.update(category);
    }

    @Override
    public boolean deleteEntity(String id)   throws RemoteException{
        return categoryDAL.deleteById(id);
    }
    
    

    @Override
    public CategoryEntity getEntityById(String id)   throws RemoteException{
        return categoryDAL.findById(id);
    }

    @Override
    public List<CategoryEntity> getAllEntities()   throws RemoteException{
        return categoryDAL.findAll();
    }
    
    @Override
    public CategoryEntity findByName(String name)  throws RemoteException {
        return categoryDAL.findByName(name);
    }

}
