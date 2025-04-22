package dal;

import dal.connectDB.ConnectDB;
import model.OrderDetailId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import model.ItemEntity;
import model.OrderDetailEntity;
import model.ToppingEntity;

import java.util.List;
import java.util.Optional;

public class OrderDetailDAL implements BaseDAL<OrderDetailEntity, OrderDetailId> {

    private EntityManager em;

    //default constructor
    public OrderDetailDAL() {
        this.em = ConnectDB.getEntityManager();
    }

    public OrderDetailDAL(EntityManager em) {
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
    public boolean insert(OrderDetailEntity t) {
        return executeTransaction(() -> em.persist(t));
    }

    @Override
    public boolean update(OrderDetailEntity t) {
        return executeTransaction(() -> em.merge(t));
    }

    @Override
    public boolean deleteById(OrderDetailId id) {
        return executeTransaction(() -> {
            OrderDetailEntity entity = em.find(OrderDetailEntity.class, id);
            if (entity != null) {
                em.remove(entity);
            }
        });
    }

    @Override
    public OrderDetailEntity findById(OrderDetailId id) {
        return em.find(OrderDetailEntity.class, id);
    }

    @Override
    public List<OrderDetailEntity> findAll() {
        return em.createNamedQuery("OrderDetailEntity.findAll", OrderDetailEntity.class).getResultList();
    }

    public OrderDetailEntity findById(String orderId, String itemId, String toppingId) {
        return em.createNamedQuery("OrderDetailEntity.findById", OrderDetailEntity.class)
                    .setParameter("orderId", orderId)
                    .setParameter("itemId", itemId)
                    .setParameter("toppingId", toppingId)
                    .getSingleResult();
    }

    public List<OrderDetailEntity> findByOrderId(String orderId) {
        return em.createNamedQuery("OrderDetailEntity.findByOrderId", OrderDetailEntity.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public boolean deleteByItemAndTopping(ItemEntity itemEntity, ToppingEntity toppingEntity) {
        return BaseDAL.executeTransaction(em, () -> {
            StringBuilder jpql = new StringBuilder("delete from OrderDetailEntity it where 1=1");
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
