package dal;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
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
}
