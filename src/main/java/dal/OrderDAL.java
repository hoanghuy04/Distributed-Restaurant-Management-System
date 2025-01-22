/*
 * @ (#) OrderDAL.java      1.0      1/18/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.OrderEntity;
import model.ToppingEntity;
import util.IDGeneratorUtil;

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
public class OrderDAL implements BaseDAL<OrderEntity, String>{
    private EntityManager entityManager;

    @Override
    public boolean insert(OrderEntity orderEntity) {
        orderEntity.setOrderId(IDGeneratorUtil.generateIDWithCreatedDate("O", "orders", "order_id"
                , "reservation_time", entityManager, orderEntity.getReservationTime()));

        return BaseDAL.executeTransaction(entityManager, () -> entityManager.persist(orderEntity));
    }

    @Override
    public boolean update(OrderEntity orderEntity) {
        return BaseDAL.executeTransaction(entityManager, () -> entityManager.merge(orderEntity));
    }

    @Override
    public boolean deleteById(String s) {
        return BaseDAL.executeTransaction(entityManager, () -> {
            OrderEntity orderEntity = entityManager.find(OrderEntity.class, s);
            if (orderEntity != null) {
                entityManager.remove(orderEntity);
            }
        });
    }

    @Override
    public Optional<OrderEntity> findById(String s) {
        return Optional.ofNullable(entityManager.find(OrderEntity.class, s));
    }

    @Override
    public List<OrderEntity> findAll() {
        return entityManager.createNamedQuery("OrderEntity.findAll", OrderEntity.class).getResultList();
    }


}
