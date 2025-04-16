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
    public boolean insert(FloorEntity t) {
        t.setFloorId(IDGeneratorUtility.generateSimpleID("F", "floors", "floor_id", em));
        return executeTransaction(() -> em.persist(t));
    }

    @Override
    public boolean update(FloorEntity t) {
        return executeTransaction(() -> em.merge(t));
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public Optional<FloorEntity> findById(String id) {
        return Optional.ofNullable(em.find(FloorEntity.class, id));
    }

    @Override
    public List<FloorEntity> findAll() {
        return em.createNamedQuery("FloorEntity.findAll", FloorEntity.class).getResultList();
    }
    
    public FloorEntity findByName(String name) {
        String sql = "select f.* from floors f where f.name = ?1";
        Query q = em.createNativeQuery(sql, FloorEntity.class);
        q.setParameter(1, name);
        try {
            return (FloorEntity) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<FloorEntity> getTablesWithKeyword(Integer capacity, String name) {
        List<FloorEntity> floors = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM floors WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (capacity != null) {
            queryBuilder.append(" AND capacity = ?");
            parameters.add(capacity);
        }
        if (name != null && !name.trim().isEmpty()) {
            queryBuilder.append(" AND name LIKE ?");
            parameters.add("%" + name + "%");
        }

        Query query = em.createNativeQuery(queryBuilder.toString(), FloorEntity.class);

        for (int i = 0; i < parameters.size(); i++) {
            query.setParameter(i + 1, parameters.get(i));
        }

        floors = query.getResultList();

        return floors;
    }    
    
}
