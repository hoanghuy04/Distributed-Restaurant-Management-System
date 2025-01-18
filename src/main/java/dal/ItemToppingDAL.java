package dal;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import model.ItemToppingEntity;

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
        return List.of();
    }
}
