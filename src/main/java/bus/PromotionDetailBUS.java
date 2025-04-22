/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import common.LevelCustomer;
import dal.PromotionDetailDAL;
import model.ItemEntity;
import model.OrderEntity;
import model.PromotionDetailEntity;
import model.PromotionDetailId;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Trần Ngọc Huyền
 */
public class PromotionDetailBUS implements BaseBUS<PromotionDetailEntity, PromotionDetailId> {

    private PromotionDetailDAL promotionDetailDAL;

    public PromotionDetailBUS(EntityManager entityManager) {
        this.promotionDetailDAL = new PromotionDetailDAL(entityManager);
    }

    @Override
    public boolean insertEntity(PromotionDetailEntity t) {
        return promotionDetailDAL.insert(t);
    }

    @Override
    public boolean updateEntity(PromotionDetailEntity t) {
        return promotionDetailDAL.update(t);
    }

    @Override
    public boolean deleteEntity(PromotionDetailId id) {
        return promotionDetailDAL.deleteById(id);
    }

    @Override
    public PromotionDetailEntity getEntityById(PromotionDetailId id) {
        return promotionDetailDAL.findById(id);
    }

    @Override
    public List<PromotionDetailEntity> getAllEntities() {
        return promotionDetailDAL.findAll();
    }
    
    public PromotionDetailEntity getTopDiscountPercentageOnItem(double totalPrice, ItemEntity it) {
        PromotionDetailEntity promotionDetail = promotionDetailDAL.getTopDiscountPercentageOnItem(totalPrice, it);
        return promotionDetail;
    }
    public boolean deleteEntitiesByPromotionId(String promotionId) {
        return promotionDetailDAL.deleteEntitiesByPromotionId(promotionId);
    }
}
