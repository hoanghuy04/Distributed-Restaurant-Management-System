package dal;

import dal.connectDB.ConnectDB;
import model.OrderEntity;
import model.PromotionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import model.enums.CustomerLevelEnum;
import model.enums.PromotionTypeEnum;
import util.IDGeneratorUtility;

public class PromotionDAL implements BaseDAL<PromotionEntity, String> {

    private EntityManager em;

    public PromotionDAL(EntityManager em) {
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
    public boolean insert(PromotionEntity t) {
        t.setPromotionId(IDGeneratorUtility.generateIDWithCreatedDate("P", "promotions", "promotion_id", "created_date", em, LocalDateTime.now()));
        JOptionPane.showMessageDialog(null, t.getPromotionId());
        return executeTransaction(() -> em.persist(t));
    }

    @Override
    public boolean update(PromotionEntity t) {
        return executeTransaction(() -> em.merge(t));
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public Optional<PromotionEntity> findById(String id) {
        return Optional.ofNullable(em.find(PromotionEntity.class, id));
    }

    @Override
    public List<PromotionEntity> findAll() {
        return em.createNamedQuery("PromotionEntity.findAll", PromotionEntity.class).getResultList();
    }


    //NEED FIXING
    public Optional<PromotionEntity> getPromotionsByCustomerLevelAndTotalPrice(double totalPaid, CustomerLevelEnum customerLevelEnum) {
        String sql = "select top 1 p.* "
                + "from promotions p "
                + "where p.active = 'true' "
                + "and GETDATE() between started_date and ended_date "
                + "and min_price <= ?1 "
                + "and promotion_type = 'ORDER' "
                + "and (',' + p.applicable_customer_levels + ',' like '%,' + ?2 + ',%') "
                + "order by p.discount_rate desc";

        Query q = em.createNativeQuery(sql, PromotionEntity.class);
        q.setParameter(1, totalPaid);
        q.setParameter(2, customerLevelEnum.toString());

        try {
            return Optional.ofNullable((PromotionEntity) q.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<PromotionEntity> getPromotionsWithKeywordfit(LocalDateTime startDate, LocalDateTime endDate, String des, Double discount, Double minPrice, String rank, PromotionTypeEnum type, boolean active) {
        StringBuilder queryBuilder = new StringBuilder("SELECT p FROM PromotionEntity p WHERE 1=1 ");
        Map<String, Object> parameters = new HashMap<>();

        if (startDate != null) {
            queryBuilder.append(" AND p.startedDate >= :startDate");
            parameters.put("startDate", startDate);
        }
        if (endDate != null) {
            queryBuilder.append(" AND p.endedDate <= :endDate");
            parameters.put("endDate", endDate);
        }
        if (des != null && !des.trim().isEmpty()) {
            queryBuilder.append(" AND p.description LIKE :description");
            parameters.put("description", "%" + des + "%");
        }
        if (discount != null) {
            queryBuilder.append(" AND p.discountRate = :discountRate");
            parameters.put("discountRate", discount);
        }
        if (minPrice != null) {
            queryBuilder.append(" AND p.minPrice = :minPrice");
            parameters.put("minPrice", minPrice);
        }
        if (rank != null && !rank.trim().isEmpty()) {
            queryBuilder.append(" AND p.applicableCustomerLevels = :applicableCustomerLevels");
            parameters.put("applicableCustomerLevels", rank);
        }
        if (type != null) {
            queryBuilder.append(" AND p.promotionType = :promotionType");
            parameters.put("promotionType", type);
        }
        queryBuilder.append(" AND p.active = :active");
        parameters.put("active", active);

        Query query = em.createQuery(queryBuilder.toString(), PromotionEntity.class);
        
        // Set parameters
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getResultList();
    }

    //test getPromotionsWithKeywordfit
    public static void main(String[] args) {
        ConnectDB.connect();
        EntityManager em = ConnectDB.getEntityManager();
        PromotionDAL promotionDAL = new PromotionDAL(em);
        List<PromotionEntity> promotions = promotionDAL.getPromotionsWithKeywordfit(LocalDate.of(2025,4,11).atStartOfDay(), null, null, null, null, null, null, true);
        for (PromotionEntity promotion : promotions) {
            System.out.println(promotion);
        }
    }

}

