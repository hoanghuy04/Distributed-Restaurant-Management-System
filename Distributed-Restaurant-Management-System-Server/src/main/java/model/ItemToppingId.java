/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Trần Ngọc Huyền
 */
@Embeddable
public class ItemToppingId implements Serializable {

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "topping_id")
    private String toppingId;

    public ItemToppingId() {
    }

    public ItemToppingId(String itemId, String toppingId) {
        this.itemId = itemId;
        this.toppingId = toppingId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getToppingId() {
        return toppingId;
    }

    public void setToppingId(String toppingId) {
        this.toppingId = toppingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemToppingId that = (ItemToppingId) o;
        return Objects.equals(itemId, that.itemId) &&
                Objects.equals(toppingId, that.toppingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, toppingId);
    }

    @Override
    public String toString() {
        return "ItemToppingId{" + "itemId=" + itemId + ", toppingId=" + toppingId + '}';
    }

}
