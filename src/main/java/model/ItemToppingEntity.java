package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import model.enums.SizeEnum;

/*
 * @description: ItemToppingEntity
 * @author: Trần Ngọc Huyền
 * @date: 1/17/2025
 * @version: 1.0
 */
@Data
@Entity
@Table(name = "items_toppings")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemToppingEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false, columnDefinition = "nvarchar(50)")
    @EqualsAndHashCode.Include
    private ItemEntity item;

    @Id
    @ManyToOne
    @JoinColumn(name = "topping_id", nullable = false, columnDefinition = "nvarchar(50)")
    @EqualsAndHashCode.Include
    private ToppingEntity topping;

    @Column(name = "selling_price", nullable = false)
    private double sellingPrice;

    public ItemToppingEntity() {

    }

    public ItemToppingEntity(ItemEntity item, ToppingEntity topping) {
        this.item = item;
        this.topping = topping;
        setSellingPrice();
    }

    public void setSellingPrice() {
        double sizePrice = 0;
        if(item.getSize() == SizeEnum.SMALL) {
            sizePrice = 0;
        } else if (item.getSize() == SizeEnum.MEDIUM) {
            sizePrice = 20000;
        } else if(item.getSize() == SizeEnum.LARGE) {
            sizePrice = 40000;
        }
        this.sellingPrice = this.topping.getCostPrice() + sizePrice;
    }
}
