package dal;

import model.TableEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.enums.TableStatusEnum;
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
        return em.createQuery("from TableEntity", TableEntity.class).getResultList();
    }

    public TableEntity findByName(String name, String floorId) {
        return em.createQuery("select t from TableEntity  t " +
                " where t.name = :name " +
                " and t.floor.id = :floorId",TableEntity.class)
                .setParameter("name", name)
                .setParameter("floorId", floorId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<TableEntity> getListOfAvailableTables(String floorId, LocalDateTime reservationDateTime) {
        String jpql = "SELECT t FROM TableEntity t WHERE 1 = 1 ";
        if (floorId != null) {
            jpql += "AND t.floor.floorId = :floorId ";
        }
        jpql += "AND t.tableId NOT IN ( "
                + "SELECT o.table.tableId FROM OrderEntity o "
                + "WHERE o.reservationTime <= :reservationDateTime "
                + "AND o.expectedCompletionTime >= :reservationDateTime "
                + "AND o.reservationStatus IN ('PENDING', 'RECEIVED'))";

        Query query = em.createQuery(jpql, TableEntity.class);

        if (floorId != null) {
            query.setParameter("floorId", floorId);
        }
        query.setParameter("reservationDateTime", reservationDateTime);

        return query.getResultList();
    }

    public List<TableEntity> getListTablesByStatus(String floorId, String status) {
        String jpql = "SELECT t FROM TableEntity t WHERE t.floor.floorId = :floorId ";
        if (!status.equals("Tất cả")) {
            jpql += "AND t.tableStatus = :status ";
        }

        Query query = em.createQuery(jpql, TableEntity.class);
        query.setParameter("floorId", floorId);

        if (!status.equals("Tất cả")) {
            query.setParameter("status", TableStatusEnum.valueOf(status));
        }

        return query.getResultList();
    }

    public List<TableEntity> getTablesWithKeyword(String floorId, Integer capacity, String tableName) {
        StringBuilder jpql = new StringBuilder("SELECT t FROM TableEntity t WHERE 1 = 1");
        if (floorId != null && !floorId.trim().isEmpty()) {
            jpql.append(" AND t.floor.floorId = :floorId");
        }
        if (capacity != null) {
            jpql.append(" AND t.capacity = :capacity");
        }
        if (tableName != null && !tableName.trim().isEmpty()) {
            jpql.append(" AND t.name LIKE :tableName");
        }

        Query query = em.createQuery(jpql.toString(), TableEntity.class);

        if (floorId != null && !floorId.trim().isEmpty()) {
            query.setParameter("floorId", floorId);
        }
        if (capacity != null) {
            query.setParameter("capacity", capacity);
        }
        if (tableName != null && !tableName.trim().isEmpty()) {
            query.setParameter("tableName", "%" + tableName + "%");
        }

        return query.getResultList();
    }

}
