/*
 * @ (#) OrderDetailDAL.java      1.0      1/18/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.ItemEntity;
import model.OrderDetailEntity;
import model.PromotionEntity;
import model.ToppingEntity;

import java.util.List;
import java.util.Optional;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 1/18/2025
 * @version: 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDAL implements BaseDAL<OrderDetailEntity, String> {
    private EntityManager entityManager;

    @Override
    public boolean insert(OrderDetailEntity orderDetailEntity) {
        return BaseDAL.executeTransaction(entityManager, () -> entityManager.persist(orderDetailEntity));
    }

    @Override
    public boolean update(OrderDetailEntity orderDetailEntity) {
        return BaseDAL.executeTransaction(entityManager, () -> entityManager.persist(orderDetailEntity));
    }

    @Override
    public boolean deleteById(String s) {
        return BaseDAL.executeTransaction(entityManager, () -> {
            OrderDetailEntity entity = entityManager.find(OrderDetailEntity.class, s);
            if (entity != null) {
                entityManager.remove(entity);
            }
        });
    }

    @Override
    public Optional<OrderDetailEntity> findById(String s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<OrderDetailEntity> findAll() {
        return entityManager.createNamedQuery("OrderDetailEntity.findAll", OrderDetailEntity.class).getResultList();
    }

    public Optional<OrderDetailEntity> findById(String orderId, String itemId, String toppingId) {
        try {
            OrderDetailEntity result = entityManager.createNamedQuery("OrderDetailEntity.findById", OrderDetailEntity.class)
                    .setParameter("orderId", orderId)
                    .setParameter("itemId", itemId)
                    .setParameter("toppingId", toppingId)
                    .getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<OrderDetailEntity> findByOrderId(String orderId) {
        return entityManager.createNamedQuery("OrderDetailEntity.findByOrderId", OrderDetailEntity.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public boolean deleteByItemAndTopping(ItemEntity itemEntity, ToppingEntity toppingEntity) {
        return BaseDAL.executeTransaction(entityManager, () -> {
            StringBuilder jpql = new StringBuilder("delete from OrderDetailEntity it where 1=1");
            if (itemEntity != null) {
                jpql.append(" and it.item.itemId = :itemId");
            }
            if (toppingEntity != null) {
                jpql.append(" and it.topping.toppingId = :toppingId");
            }
            Query query = entityManager.createQuery(jpql.toString());
            if (itemEntity != null) {
                query.setParameter("itemId", itemEntity.getItemId());
            }
            if (toppingEntity != null) {
                query.setParameter("toppingId", toppingEntity.getToppingId());
            }
            query.executeUpdate();
        });
    }
}
