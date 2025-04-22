/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import jakarta.persistence.*;
import model.ItemEntity;
import model.ItemToppingEntity;
import model.ItemToppingId;
import model.ToppingEntity;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Trần Ngọc Huyền
 */
public class ItemToppingDAL implements BaseDAL<ItemToppingEntity, ItemToppingId> {

    private EntityManager em;

    public ItemToppingDAL(EntityManager em) {
        this.em = em;
    }

    private EntityTransaction getTransaction() {
        return this.em.getTransaction();
    }

    private boolean executeTransaction(Runnable action) {
        EntityTransaction et = this.getTransaction();
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
    public boolean insert(ItemToppingEntity t) {
        return executeTransaction(() -> em.persist(t));
    }

    @Override
    public boolean update(ItemToppingEntity t) {
        return executeTransaction(() -> em.merge(t));
    }

    @Override
    public boolean deleteById(ItemToppingId id) {
        return executeTransaction(() -> {
            ItemToppingEntity entity = em.find(ItemToppingEntity.class, id);
            if (entity != null) {
                em.remove(entity);
            }
        });
    }

    @Override
    public ItemToppingEntity findById(ItemToppingId id) {
        return em.find(ItemToppingEntity.class, id);
    }

    @Override
    public List<ItemToppingEntity> findAll() {
        return em.createNamedQuery("ItemToppingEntity.findAll", ItemToppingEntity.class).getResultList();
    }

    public ItemToppingEntity findByItemAndToppingId(ItemEntity item, ToppingEntity topping) {
        String jpql = "select it from ItemToppingEntity it " +
                "where it.item.itemId = :itemId and it.topping.toppingId = :toppingId";

        TypedQuery<ItemToppingEntity> query = em.createQuery(jpql, ItemToppingEntity.class);
        query.setParameter("itemId", item.getItemId());
        query.setParameter("toppingId", topping.getToppingId());

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public ItemToppingEntity findByItemAndTopping(ItemEntity itemEntity, ToppingEntity toppingEntity) {
        try {
            return em.createNamedQuery("ItemToppingEntity.findByItemAndTopping", ItemToppingEntity.class)
                    .setParameter("item", itemEntity)
                    .setParameter("topping", toppingEntity)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean deleteByItemAndTopping(ItemEntity itemEntity, ToppingEntity toppingEntity) {
        return BaseDAL.executeTransaction(em, () -> {
            StringBuilder jpql = new StringBuilder("delete from ItemToppingEntity it where 1=1");
            if (itemEntity != null) {
                jpql.append(" and it.item.itemId = :itemId");
            }
            if (toppingEntity != null) {
                jpql.append(" and it.topping.toppingId = :toppingId");
            }
            Query query = em.createQuery(jpql.toString());
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
