package dal;

import jakarta.persistence.EntityManager;
import model.OrderEntity;
import model.PromotionEntity;

import java.util.List;
import java.util.Optional;

public class PromotionDAL implements BaseDAL<PromotionEntity, String> {
    private EntityManager entityManager;

    @Override
    public boolean insert(PromotionEntity promotionEntity) {
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
