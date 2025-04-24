/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dto.ToppingDTO;
import jakarta.persistence.*;
import model.ToppingEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.IDGeneratorUtility;

/**
 *
 * @author Trần Ngọc Huyền
 */
public class ToppingDAL implements BaseDAL<ToppingEntity, String> {

    private EntityManager em;

    public ToppingDAL(EntityManager em) {
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
                System.out.println("ToppingDAL rollback");
                et.rollback();
            }
        }
        return false;
    }

    @Override
    public ToppingEntity insert(ToppingEntity t) {
        t.setToppingId(IDGeneratorUtility.generateIDWithCreatedDate("T", "toppings", "topping_id", "created_date", em, LocalDateTime.now()));
        executeTransaction(() -> em.persist(t));
        return t;
    }

    @Override
    public boolean update(ToppingEntity t) {
        return executeTransaction(() -> em.merge(t));
    }

    @Override
    public boolean deleteById(String id) {
        return executeTransaction(() -> {
            ToppingEntity entity = em.find(ToppingEntity.class, id);
            if (entity != null) {
                em.remove(entity);
            }
        });
    }

    @Override
    public ToppingEntity findById(String id) {
        return em.find(ToppingEntity.class, id);
    }

    @Override
    public List<ToppingEntity> findAll() {
        return em.createNamedQuery("ToppingEntity.findAll", ToppingEntity.class).getResultList();
    }

    public List<ToppingEntity> findByCriteria(ToppingDTO toppingDTO) {
        StringBuilder jpql = new StringBuilder("select t from ToppingEntity t where 1=1");
        Map<String, Object> parameters = new HashMap<>();

        if (toppingDTO.getName() != null && !toppingDTO.getName().trim().isEmpty()) {
            jpql.append(" and t.name like :name");
            parameters.put("name", "%" + toppingDTO.getName() + "%");
        }

        if (toppingDTO.getDesc() != null && !toppingDTO.getDesc().trim().isEmpty()) {
            jpql.append(" and t.description like :description");
            parameters.put("description", "%" + toppingDTO.getDesc() + "%");
        }

        if ( toppingDTO.getCostPrice() >= 0) {
            double costPrice = toppingDTO.getCostPrice();
            if (costPrice == 0) {
                jpql.append(" and t.costPrice is not null");
            } else {
                jpql.append(" and t.costPrice = :costPrice");
                parameters.put("costPrice", costPrice);
            }
        }

        if (toppingDTO.getStockQty() > 0) {
            jpql.append(" and t.stockQuantity = :stockQuantity");
            parameters.put("stockQuantity", toppingDTO.getStockQty());
        }

        TypedQuery<ToppingEntity> query = em.createQuery(jpql.toString(), ToppingEntity.class);

        for (Map.Entry<String, Object> param : parameters.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }

        return query.getResultList();
    }
    public ToppingEntity findByName(String name) {
        try {
            return em.createNamedQuery("ToppingEntity.findByName", ToppingEntity.class)
                    .setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

}
