package dal;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import model.ItemEntity;
import model.ItemToppingEntity;
import model.ToppingEntity;

import java.util.List;
import java.util.Optional;

/*
 * @description: ItemToppingDAL
 * @author: Trần Ngọc Huyền
 * @date: 1/18/2025
 * @version: 1.0
 */
@AllArgsConstructor
public class ItemToppingDAL implements BaseDAL<ItemToppingEntity, String>{
    private EntityManager em;

    @Override
    public boolean insert(ItemToppingEntity itemToppingEntity) {
        return BaseDAL.executeTransaction(em, () -> em.persist(itemToppingEntity));
    }

    @Override
    public boolean update(ItemToppingEntity itemToppingEntity) {
        return BaseDAL.executeTransaction(em, () -> em.merge(itemToppingEntity));
    }

    @Override
    public boolean deleteById(String s) {
        return false;
    }

    @Override
    public Optional<ItemToppingEntity> findById(String s) {
        return Optional.empty();
    }

    @Override
    public List<ItemToppingEntity> findAll() {
        return em.createNamedQuery("ItemToppingEntity.findAll", ItemToppingEntity.class).getResultList();
    }

    public Optional<ItemToppingEntity> findByItemAndTopping(ItemEntity itemEntity, ToppingEntity toppingEntity) {
        try {
            ItemToppingEntity result = em.createNamedQuery("ItemToppingEntity.findByItemAndTopping", ItemToppingEntity.class)
                    .setParameter("item", itemEntity)
                    .setParameter("topping", toppingEntity)
                    .getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
