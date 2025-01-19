package dal;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.OrderDetailEntity;
import model.PromotionDetailEntity;
import model.PromotionEntity;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class PromotionDetailDAL implements BaseDAL<PromotionDetailEntity,String> {
    private EntityManager entityManager;

    @Override
    public boolean insert(PromotionDetailEntity promotionDetailEntity) {
        return BaseDAL.executeTransaction(entityManager, () -> entityManager.persist(promotionDetailEntity));
    }

    @Override
    public boolean update(PromotionDetailEntity promotionDetailEntity) {
        return BaseDAL.executeTransaction(entityManager, () -> entityManager.merge(promotionDetailEntity));
    }

    @Override
    public boolean deleteById(String s) {
        PromotionDetailEntity promotionDetailEntity = entityManager.find(PromotionDetailEntity.class, s);
        if (promotionDetailEntity != null) {
            return BaseDAL.executeTransaction(entityManager, () -> entityManager.remove(promotionDetailEntity));
        }
        return false;
    }

    @Override
    public Optional<PromotionDetailEntity> findById(String s) {
       return Optional.ofNullable(entityManager.find(PromotionDetailEntity.class, s));
    }

    @Override
    public List<PromotionDetailEntity> findAll() {
        return entityManager.createNamedQuery("PromotionDetailEntity.findAll", PromotionDetailEntity.class).getResultList();
    }

    public List<PromotionDetailEntity> findByPromotionId(String promotionId) {
        return entityManager.createNamedQuery("PromotionDetailEntity.findByPromotionId", PromotionDetailEntity.class)
                .setParameter("promotionId", promotionId)
                .getResultList();
    }

    public List<PromotionDetailEntity> findByItemId(String itemId) {
        return entityManager.createNamedQuery("PromotionDetailEntity.findByItemId", PromotionDetailEntity.class)
                .setParameter("itemId", itemId)
                .getResultList();
    }

    public PromotionDetailEntity findByPromotionAndItem(String promotionId, String itemId) {
        return entityManager.createQuery(
                        "SELECT p FROM PromotionDetailEntity p WHERE p.promotion.promotionId = :promotionId AND p.item.itemId = :itemId",
                        PromotionDetailEntity.class
                )
                .setParameter("promotionId", promotionId)
                .setParameter("itemId", itemId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public boolean deleteByPromotionAndItem(String promotionId, String itemId) {
        PromotionDetailEntity promotionDetailEntity = entityManager.createQuery(
                        "SELECT p FROM PromotionDetailEntity p WHERE p.promotion.promotionId = :promotionId AND p.item.itemId = :itemId",
                        PromotionDetailEntity.class
                )
                .setParameter("promotionId", promotionId)
                .setParameter("itemId", itemId)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (promotionDetailEntity != null) {
            return BaseDAL.executeTransaction(entityManager, () -> entityManager.remove(promotionDetailEntity));
        }
        return false;
    }

}
