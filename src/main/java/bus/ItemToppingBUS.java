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
        ItemToppingEntity itemTopping = itemToppingDAL.findById(id);
        return itemTopping == null ? null : itemTopping;
    }

    @Override
    public List<ItemToppingEntity> getAllEntities() {
        return itemToppingDAL.findAll();
    }
    
    public ItemToppingEntity findByItemAndToppingId(ItemEntity item, ToppingEntity topping) {
        ItemToppingEntity itemTopping = itemToppingDAL.findByItemAndToppingId(item, topping);
        return itemTopping == null ? null : itemTopping;
    }
}
