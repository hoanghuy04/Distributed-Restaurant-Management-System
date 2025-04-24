package dal;

import dal.connectDB.ConnectDB;
import jakarta.persistence.*;
import model.PromotionEntity;
import model.enums.CustomerLevelEnum;
import model.enums.PromotionTypeEnum;
import util.IDGeneratorUtility;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    //NEED FIXING
    public PromotionEntity getPromotionsByCustomerLevelAndTotalPrice(double totalPaid, CustomerLevelEnum customerLevelEnum) {
        String jpql = "SELECT p FROM PromotionEntity p "
                + "WHERE p.active = true "
                + "AND CURRENT_DATE BETWEEN p.startedDate AND p.endedDate "
                + "AND p.minPrice <= :totalPaid "
                + "AND p.promotionType = :promotionType "
//                + "AND p.customerLevels LIKE  :customerLevel "
                + "ORDER BY p.discountPercentage DESC";

        TypedQuery<PromotionEntity> q = em.createQuery(jpql, PromotionEntity.class);
        q.setParameter("totalPaid", totalPaid);
        q.setParameter("promotionType", PromotionTypeEnum.ORDER);  // Assuming PromotionTypeEnum exists
//        q.setParameter("customerLevel", "%"+customerLevelEnum.name()+"%");

        PromotionEntity promotion = (PromotionEntity) q.getResultList()
                            .stream()
                                .filter(p -> {
//                                    StringBuilder  sb = new StringBuilder();
//                                    p.getCustomerLevels().forEach( enumTemp -> sb.append(enumTemp.toString()));
//                                    System.out.println(sb.toString());
//                                    System.out.println(customerLevelEnum.name());
//                                    return sb.toString().contains(customerLevelEnum.name());
                                    return p.getCustomerLevels().contains(customerLevelEnum);
                                })
                                                .sorted(Comparator.comparing(PromotionEntity::getDiscountPercentage).reversed())
                                                .findFirst()
                                                        .orElse(null);

        return promotion;
    }


    public List<PromotionEntity> getPromotionsWithKeywordfit(LocalDateTime startDate, LocalDateTime endDate, String des, Double discount, Double minPrice, List<CustomerLevelEnum> rank, PromotionTypeEnum type, boolean active) {
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
            queryBuilder.append(" AND p.discountPercentage = :discountPercentage");
            parameters.put("discountPercentage", discount);

        }
        if (minPrice != null) {
            queryBuilder.append(" AND p.minPrice = :minPrice");
            parameters.put("minPrice", minPrice);
        }
        if (rank != null && !rank.isEmpty()) {
            queryBuilder.append(" AND p.customerLevels = :applicableCustomerLevels");
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
        PromotionEntity promotions = promotionDAL.getPromotionsByCustomerLevelAndTotalPrice(700000, CustomerLevelEnum.VIP );
        System.out.println(promotions);
    }

}

