/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus.impl;

import bus.BaseBUS;
import dal.ItemToppingDAL;
import model.ItemEntity;
import model.ItemToppingEntity;
import model.ItemToppingId;
import model.ToppingEntity;
import jakarta.persistence.EntityManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *
 * @author Trần Ngọc Huyền
 */
public class ItemToppingBUSImpl extends UnicastRemoteObject implements bus.ItemToppingBUS {

    private ItemToppingDAL itemToppingDAL;

    public ItemToppingBUSImpl(EntityManager entityManager)  throws RemoteException {
        this.itemToppingDAL = new ItemToppingDAL(entityManager);
    }

    @Override
    public boolean insertEntity(ItemToppingEntity itemTopping)  throws RemoteException {
        return itemToppingDAL.insert(itemTopping);
    }

    @Override
    public boolean updateEntity(ItemToppingEntity itemTopping)  throws RemoteException {
        return itemToppingDAL.update(itemTopping);
    }

    @Override
    public boolean deleteEntity(ItemToppingId id)  throws RemoteException {
        return itemToppingDAL.deleteById(id);
    }

    @Override
    public ItemToppingEntity getEntityById(ItemToppingId id)  throws RemoteException {
        ItemToppingEntity itemTopping = itemToppingDAL.findById(id);
        return itemTopping == null ? null : itemTopping;
    }

    @Override
    public List<ItemToppingEntity> getAllEntities()  throws RemoteException {
        return itemToppingDAL.findAll();
    }
    
    @Override
    public ItemToppingEntity findByItemAndToppingId(ItemEntity item, ToppingEntity topping)  throws RemoteException {
        ItemToppingEntity itemTopping = itemToppingDAL.findByItemAndToppingId(item, topping);
        return itemTopping == null ? null : itemTopping;
    }
}
