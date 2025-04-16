/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dto.ToppingDTO;
import model.ToppingEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    public boolean insert(ToppingEntity t) {
        t.setToppingId(IDGeneratorUtility.generateIDWithCreatedDate("T", "toppings", "topping_id", "created_date", em, LocalDateTime.now()));
        return executeTransaction(() -> em.persist(t));
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
    public Optional<ToppingEntity> findById(String id) {
        return Optional.ofNullable(em.find(ToppingEntity.class, id));
    }

    @Override
    public List<ToppingEntity> findAll() {
        return em.createNamedQuery("ToppingEntity.findAll", ToppingEntity.class).getResultList();
    }

    public List<ToppingEntity> findByCriteria(ToppingDTO toppingDTO) {
        StringBuilder sql = new StringBuilder("select t.* from toppings t where 1 = 1 ");

        if (!toppingDTO.getName().trim().equals("")) {
            sql.append(" and t.name like N'" + toppingDTO.getName() + "' ");
        }

        if (!toppingDTO.getDesc().trim().equals("")) {
            sql.append(" and t.description like N'" + toppingDTO.getDesc() + "' ");
        }

        if (toppingDTO.getCostPrice() >= 0) {
            double costPrice = toppingDTO.getCostPrice();
            if (costPrice == 0) {
                sql.append(" and t.cost_price is not null ");
            } else if (costPrice == (long) costPrice) {
                sql.append(" and t.cost_price = " + (long) costPrice + " ");
            }
        }

        if (toppingDTO.getStockQty() > 0) {
            sql.append(" and t.stock_quantity = " + toppingDTO.getStockQty() + " ");
        }

        Query q = em.createNativeQuery(sql.toString(), ToppingEntity.class);
        return q.getResultList();
    }

    public Optional<ToppingEntity> findByName(String name) {
        try {
            ToppingEntity result = em.createNamedQuery("ToppingEntity.findByName", ToppingEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

}
