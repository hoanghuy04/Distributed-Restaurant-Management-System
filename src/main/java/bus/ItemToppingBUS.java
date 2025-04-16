/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dal.ItemToppingDAL;
import model.ItemEntity;
import model.ItemToppingEntity;
import model.ItemToppingId;
import model.ToppingEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Trần Ngọc Huyền
 */
public class ItemToppingBUS implements BaseBUS<ItemToppingEntity, ItemToppingId> {

    private ItemToppingDAL itemToppingDAL;

    public ItemToppingBUS(EntityManager entityManager) {
        this.itemToppingDAL = new ItemToppingDAL(entityManager);
    }

    @Override
    public boolean insertEntity(ItemToppingEntity itemTopping) {
        return itemToppingDAL.insert(itemTopping);
    }

    @Override
    public boolean updateEntity(ItemToppingEntity itemTopping) {
        return itemToppingDAL.update(itemTopping);
    }

    @Override
    public boolean deleteEntity(ItemToppingId id) {
        return itemToppingDAL.deleteById(id);
    }

    @Override
    public ItemToppingEntity getEntityById(ItemToppingId id) {
        Optional<ItemToppingEntity> optionalItemTopping = itemToppingDAL.findById(id);
        return optionalItemTopping.orElse(null); // Trả về null nếu không tìm thấy
    }

    @Override
    public List<ItemToppingEntity> getAllEntities() {
        return itemToppingDAL.findAll();
    }
    
    public ItemToppingEntity findByItemAndToppingId(ItemEntity item, ToppingEntity topping) {
        Optional<ItemToppingEntity> optional = itemToppingDAL.findByItemAndToppingId(item, topping);
        return optional.orElse(null);
    }
}
