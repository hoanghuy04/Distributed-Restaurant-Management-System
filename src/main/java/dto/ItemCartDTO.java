/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import model.ItemEntity;
import model.ItemToppingEntity;
import java.util.Objects;

/**
 *
 * @author Trần Ngọc Huyền
 */
public class ItemCartDTO {

    private ItemEntity item;
    private ItemToppingEntity itemTopping;
    private String description;

    public ItemCartDTO() {
    }

    public ItemCartDTO(ItemEntity item, ItemToppingEntity itemTopping, String description) {
        this.item = item;
        this.itemTopping = itemTopping;
        this.description = description;
    }

    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }

    public ItemToppingEntity getItemTopping() {
        return itemTopping;
    }

    public void setItemTopping(ItemToppingEntity itemTopping) {
        this.itemTopping = itemTopping;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.item);
        hash = 79 * hash + Objects.hashCode(this.itemTopping);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ItemCartDTO other = (ItemCartDTO) obj;
        if (!Objects.equals(this.item, other.item)) {
            return false;
        }
        return Objects.equals(this.itemTopping, other.itemTopping);
    }

    @Override
    public String toString() {
        return "ItemCartDTO{" + "item=" + item + ", itemTopping=" + itemTopping + ", description=" + description + '}';
    }
}
