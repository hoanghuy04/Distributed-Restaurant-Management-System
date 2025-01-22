package dal;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.OrderEntity;
import model.PromotionEntity;
import util.IDGeneratorUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class PromotionDAL implements BaseDAL<PromotionEntity, String> {
    private EntityManager entityManager;

    @Override
    public boolean insert(PromotionEntity promotionEntity) {
        promotionEntity.setPromotionId(IDGeneratorUtil.generateIDWithCreatedDate("P", "promotions", "promotion_id"
                , "created_date", entityManager, LocalDateTime.now()));
        return BaseDAL.executeTransaction(entityManager, () -> entityManager.persist(promotionEntity));
    }

    @Override
    public boolean update(PromotionEntity promotionEntity) {
        return BaseDAL.executeTransaction(entityManager, () -> entityManager.merge(promotionEntity));
    }

    @Override
    public boolean deleteById(String s) {
        PromotionEntity promotionEntity = entityManager.find(PromotionEntity.class, s);
        if (promotionEntity != null) {
            return BaseDAL.executeTransaction(entityManager, () -> entityManager.remove(promotionEntity));
        }
        return false;
    }

    @Override
    public Optional<PromotionEntity> findById(String s) {
        return Optional.ofNullable(entityManager.find(PromotionEntity.class, s));
    }

    @Override
    public List<PromotionEntity> findAll() {
        return entityManager.createNamedQuery("PromotionEntity.findAll", PromotionEntity.class).getResultList();
    }
}
