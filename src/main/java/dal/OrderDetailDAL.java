/*
 * @ (#) OrderDetailDAL.java      1.0      1/18/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package dal;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.OrderDetailEntity;

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
}
