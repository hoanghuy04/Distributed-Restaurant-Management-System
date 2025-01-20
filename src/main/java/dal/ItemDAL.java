package dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import model.CategoryEntity;
import model.ItemEntity;
import util.IDGeneratorUtil;

import java.util.List;
import java.util.Optional;

/*
 * @description: ItemDAL
 * @author: Trần Ngọc Huyền
 * @date: 1/18/2025
 * @version: 1.0
 */
@AllArgsConstructor
public class ItemDAL implements  BaseDAL<ItemEntity, String>{
    private EntityManager em;

    @Override
    public boolean insert(ItemEntity itemEntity) {
        itemEntity.setItemId(IDGeneratorUtil.generateSimpleID("I", "items", "item_id", em));
        return BaseDAL.executeTransaction(em, () -> em.persist(itemEntity));
    }

    @Override
    public boolean update(ItemEntity itemEntity) {
        return BaseDAL.executeTransaction(em, () -> em.merge(itemEntity));
    }

    @Override
    public boolean deleteById(String s) {
        return BaseDAL.executeTransaction(em, () -> {
            ItemEntity itemEntity = em.find(ItemEntity.class, s);
           if (itemEntity != null) {
               em.remove(itemEntity);
           }
        });
    }

    @Override
    public Optional<ItemEntity> findById(String s) {
        return Optional.ofNullable(em.find(ItemEntity.class, s));
    }

    @Override
    public List<ItemEntity> findAll() {
        return em.createNamedQuery("ItemEntity.findAll", ItemEntity.class).getResultList();
    }

    public List<ItemEntity> findByCategory(CategoryEntity categoryEntity) {
        return em.createNamedQuery("ItemEntity.findByCategory", ItemEntity.class).setParameter("categoryId", categoryEntity.getCategoryId()).getResultList();
    }

    public Optional<ItemEntity> findByName(String name) {
        try {
            ItemEntity result = em.createNamedQuery("ItemEntity.findByName", ItemEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
