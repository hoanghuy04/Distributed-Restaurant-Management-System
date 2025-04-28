/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus.impl;

import bus.BaseBUS;
import dal.PromotionDetailDAL;
import model.ItemEntity;
import model.PromotionDetailEntity;
import model.PromotionDetailId;
import jakarta.persistence.EntityManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *
 * @author Trần Ngọc Huyền
 */
public class PromotionDetailBUSImpl extends UnicastRemoteObject implements bus.PromotionDetailBUS {

    private PromotionDetailDAL promotionDetailDAL;

    public PromotionDetailBUSImpl(EntityManager entityManager)  throws RemoteException {
        this.promotionDetailDAL = new PromotionDetailDAL(entityManager);
    }

    @Override
    public PromotionDetailEntity insertEntity(PromotionDetailEntity t)  throws RemoteException {
        return promotionDetailDAL.insert(t);
    }

    @Override
    public PromotionDetailEntity updateEntity(PromotionDetailEntity t)  throws RemoteException {
        return promotionDetailDAL.update(t);
    }

    @Override
    public boolean deleteEntity(PromotionDetailId id)  throws RemoteException {
        return promotionDetailDAL.deleteById(id);
    }

    @Override
    public PromotionDetailEntity getEntityById(PromotionDetailId id)  throws RemoteException {
        return promotionDetailDAL.findById(id);
    }

    @Override
    public List<PromotionDetailEntity> getAllEntities()  throws RemoteException {
        return promotionDetailDAL.findAll();
    }
    
    @Override
    public PromotionDetailEntity getTopDiscountPercentageOnItem(double totalPrice, ItemEntity it)  throws RemoteException {
        PromotionDetailEntity promotionDetail = promotionDetailDAL.getTopDiscountPercentageOnItem(totalPrice, it);
        return promotionDetail;
    }
    @Override
    public boolean deleteEntitiesByPromotionId(String promotionId)  throws RemoteException {
        return promotionDetailDAL.deleteEntitiesByPromotionId(promotionId);
    }
}
