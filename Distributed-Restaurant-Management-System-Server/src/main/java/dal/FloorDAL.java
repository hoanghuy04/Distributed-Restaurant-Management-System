package dal;

import model.FloorEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import util.IDGeneratorUtility;

public class FloorDAL implements BaseDAL<FloorEntity, String> {
    private EntityManager em;

    public FloorDAL(EntityManager em) {
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
    public FloorEntity insert(FloorEntity t) {
        t.setFloorId(IDGeneratorUtility.generateSimpleID("F", "floors", "floor_id", em));
        executeTransaction(() -> em.persist(t));

        return t;
    }

    @Override
    public FloorEntity update(FloorEntity t) {
        if (t.getFloorId() == null) {
            t.setFloorId(IDGeneratorUtility.generateSimpleID("F", "floors", "floor_id", em));
        }
        return executeTransaction(() -> em.merge(t)) ? t : null;
    }
    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public FloorEntity findById(String id) {
        return em.find(FloorEntity.class, id);
    }

    @Override
    public List<FloorEntity> findAll() {
        return em.createQuery("from FloorEntity", FloorEntity.class).getResultList();
    }
    
    public FloorEntity findByName(String name) {
        return em.createQuery("select f from FloorEntity f " +
                " where f.name = :name", FloorEntity.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<FloorEntity> getTablesWithKeyword(Integer capacity, String name) {
        StringBuilder jpql = new StringBuilder("SELECT f FROM FloorEntity f WHERE 1=1");

        if (capacity != null) {
            jpql.append(" AND f.capacity = :capacity");
        }
        if (name != null && !name.trim().isEmpty()) {
            jpql.append(" AND f.name LIKE :name");
        }

        Query query = em.createQuery(jpql.toString(), FloorEntity.class);

        if (capacity != null) {
            query.setParameter("capacity", capacity);
        }
        if (name != null && !name.trim().isEmpty()) {
            query.setParameter("name", "%" + name + "%");
        }

        return query.getResultList();
    }
    
}
