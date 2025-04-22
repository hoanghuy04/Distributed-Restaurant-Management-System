package dal;

import model.TableEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import util.IDGeneratorUtility;

public class TableDAL implements BaseDAL<TableEntity, String> {

    private EntityManager em;

    public TableDAL(EntityManager em) {
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
    public boolean insert(TableEntity t) {
        t.setTableId(IDGeneratorUtility.generateSimpleID(t.getFloor().getFloorId() + "T", "tables", "table_id", em));
        return executeTransaction(() -> em.persist(t));
    }

    @Override
    public boolean update(TableEntity t) {
        return executeTransaction(() -> em.merge(t));
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public TableEntity findById(String id) {
        return em.find(TableEntity.class, id);
    }

    @Override
    public List<TableEntity> findAll() {
        return em.createNamedQuery("TableEntity.findAll", TableEntity.class).getResultList();
    }

    public TableEntity findByName(String name, String floorId) {
        return em.createNamedQuery("TableEntity.findByName", TableEntity.class).setParameter("name", name).setParameter("floorId", floorId).getSingleResult();
    }

    public List<TableEntity> getListOfAvailableTables(String floorId, LocalDateTime reservationDateTime) {
        String sql = "SELECT t.* "
                + "FROM tables t "
                + "WHERE 1 = 1 ";
        if (floorId != null) {
            sql += "AND t.floor_id = :floorId ";
        }
        sql += "  AND t.table_id NOT IN ( "
                + "        SELECT o.table_id "
                + "        FROM orders o "
                + "        WHERE o.reservation_time <= :reservationDateTime "
                + "        AND o.expected_completion_time >= :reservationDateTime "
                + "        AND o.reservation_status in ('PENDING', 'RECEIVED'))";

        Query q = em.createNativeQuery(sql, TableEntity.class);

        if (floorId != null) {
            q.setParameter("floorId", floorId);
        }
        q.setParameter("reservationDateTime", reservationDateTime);

        return q.getResultList();
    }

    public List<TableEntity> getListTablesByStatus(String floorId, String status) {
        String sql = "select t.* "
                + "from tables t "
                + "where t.floor_id = ?1 ";

        if (!status.equals("Tất cả")) {
            sql += " AND t.table_status = '" + status + "' ";
        }
        Query q = em.createNativeQuery(sql, TableEntity.class);
        q.setParameter(1, floorId);

        return q.getResultList();
    }

    public List<TableEntity> getTablesWithKeyword(String floorId, Integer capacity, String tableName) {
        List<TableEntity> tables = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM tables WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (floorId != null && !floorId.trim().isEmpty()) {
            queryBuilder.append(" AND floor_id = ?");
            parameters.add(floorId);
        }
        if (capacity != null) {
            queryBuilder.append(" AND capacity = ?");
            parameters.add(capacity);
        }
        if (tableName != null && !tableName.trim().isEmpty()) {
            queryBuilder.append(" AND table_name LIKE ?");
            parameters.add("%" + tableName + "%");
        }

        Query query = em.createNativeQuery(queryBuilder.toString(), TableEntity.class);

        for (int i = 0; i < parameters.size(); i++) {
            query.setParameter(i + 1, parameters.get(i));
        }

        tables = query.getResultList();

        return tables;
    }

}