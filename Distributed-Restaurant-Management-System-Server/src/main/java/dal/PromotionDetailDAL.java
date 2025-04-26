/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import jakarta.persistence.*;
import model.ItemEntity;
import model.PromotionDetailEntity;
import model.PromotionDetailId;
import model.PromotionEntity;

import java.util.List;

/**
 *
 * @author Trần Ngọc Huyền
 */
public class PromotionDetailDAL implements BaseDAL<PromotionDetailEntity, PromotionDetailId> {

    private EntityManager em;

    public PromotionDetailDAL(EntityManager em) {
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
    public PromotionDetailEntity insert(PromotionDetailEntity t) {
        executeTransaction(() -> em.persist(t));
        return t;
    }

    @Override
    public boolean update(PromotionDetailEntity t) {
        return executeTransaction(() -> em.merge(t));
    }

    @Override
    public boolean deleteById(PromotionDetailId id) {
        return executeTransaction(() -> {
            PromotionDetailEntity entity = em.find(PromotionDetailEntity.class, id);
            if (entity != null) {
                em.remove(entity);
            }
        });
    }

    @Override
    public PromotionDetailEntity findById(PromotionDetailId id) {
        return em.find(PromotionDetailEntity.class, id);
    }

    @Override
    public List<PromotionDetailEntity> findAll() {
        return em.createNamedQuery("PromotionDetailEntity.findAll", PromotionDetailEntity.class).getResultList();
    }

    public List<PromotionDetailEntity> findByPromotionId(String promotionId) {
        return em.createNamedQuery("PromotionDetailEntity.findByPromotionId", PromotionDetailEntity.class)
                .setParameter("promotionId", promotionId)
                .getResultList();
    }

    public List<PromotionDetailEntity> findByItemId(String itemId) {
        return em.createNamedQuery("PromotionDetailEntity.findByItemId", PromotionDetailEntity.class)
                .setParameter("itemId", itemId)
                .getResultList();
    }

    public PromotionDetailEntity findByPromotionAndItem(String promotionId, String itemId) {
        return em.createQuery(
                "SELECT p FROM PromotionDetailEntity p WHERE p.promotion.promotionId = :promotionId AND p.item.itemId = :itemId",
                PromotionDetailEntity.class
        )
                .setParameter("promotionId", promotionId)
                .setParameter("itemId", itemId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public boolean deleteByItemAndPromotion(ItemEntity itemEntity, PromotionEntity promotionEntity) {
        return BaseDAL.executeTransaction(em, () -> {
            StringBuilder jpql = new StringBuilder("delete from PromotionDetailEntity it where 1=1");
            if (itemEntity != null) {
                jpql.append(" AND it.item.itemId = :itemId");
            }
            if (promotionEntity != null) {
                jpql.append(" AND it.promotion.promotionId = :promotionId");
            }
            Query query = em.createQuery(jpql.toString());
            if (itemEntity != null) {
                query.setParameter("itemId", itemEntity.getItemId());
            }
            if (promotionEntity != null) {
                query.setParameter("promotionId", promotionEntity.getPromotionId());
            }
            query.executeUpdate();
        });
    }

    public PromotionDetailEntity getTopDiscountPercentageOnItem(double totalPrice, ItemEntity it) {
        String jpql = "SELECT pd FROM PromotionDetailEntity pd "
                + "JOIN pd.promotion p "
                + "JOIN pd.item i "
                + "WHERE p.active = true "
                + "AND CURRENT_DATE BETWEEN p.startedDate AND p.endedDate "
                + "AND pd.promotion.minPrice <= :totalPrice "
                + "AND pd.item.itemId = :itemId "
                + "ORDER BY p.discountPercentage DESC";

        TypedQuery<PromotionDetailEntity> q = em.createQuery(jpql, PromotionDetailEntity.class);
        q.setParameter("totalPrice", totalPrice);
        q.setParameter("itemId", it.getItemId());
        q.setMaxResults(1);  // replaces "TOP 1"

        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public boolean deleteEntitiesByPromotionId(String promotionId) {
        return executeTransaction(() -> {
            String sql = "DELETE FROM promotion_details WHERE promotion_id = ?1";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, promotionId);
            q.executeUpdate();
        });
    }

}
