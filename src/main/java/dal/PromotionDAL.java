package dal;

import model.OrderEntity;
import model.PromotionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.sql.Timestamp;
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
    public PromotionEntity findById(String id) {
        return em.find(PromotionEntity.class, id);
    }

    @Override
    public List<PromotionEntity> findAll() {
        return em.createNamedQuery("PromotionEntity.findAll", PromotionEntity.class).getResultList();
    }

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

    public List<PromotionEntity> getPromotionsWithKeywordfit(LocalDateTime startDate, LocalDateTime endDate, String scrip, Double discount, Double minPrice, String rank, PromotionTypeEnum type, boolean active) {
        List<PromotionEntity> promotions = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM promotions WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();

        if (startDate != null) {
            queryBuilder.append(" AND started_date >= ?");
//            parameters.add(Timestamp.valueOf(startDate.truncatedTo(ChronoUnit.SECONDS)));
            parameters.add(Timestamp.valueOf(startDate));
        }
        if (endDate != null) {
            queryBuilder.append(" AND ended_date <= ?");
            parameters.add(Timestamp.valueOf(endDate));
        }
        if (scrip != null && !scrip.trim().isEmpty()) {
            queryBuilder.append(" AND description LIKE ?");
            parameters.add("%" + scrip + "%");
        }
        if (discount != null) {
            queryBuilder.append(" AND discount_rate = ?");
            parameters.add(discount);
        }
        if (minPrice != null) {
            queryBuilder.append(" AND min_price = ?");
            parameters.add(minPrice);
        }
        if (rank != null && !rank.trim().isEmpty()) {
            queryBuilder.append(" AND applicable_customer_levels = ?");
            parameters.add(rank);
        }
        if (type != null) {
            queryBuilder.append(" AND promotion_type = ?");

            parameters.add(type.name());
        }
        queryBuilder.append(" AND active = ?");
        parameters.add(active);

        Query query = em.createNativeQuery(queryBuilder.toString(), PromotionEntity.class);

        for (int i = 0; i < parameters.size(); i++) {
            query.setParameter(i + 1, parameters.get(i));
        }

        promotions = query.getResultList();

        return promotions;
    }

}
