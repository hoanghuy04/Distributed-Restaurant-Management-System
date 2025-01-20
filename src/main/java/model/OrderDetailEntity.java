/*
 * @ (#) OrderDetailEntity.java      1.0      1/16/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 1/16/2025
 * @version: 1.0
 */
@Data
@Getter
@Entity
@Table(name = "order_details")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NamedQueries({
        @NamedQuery(name = "OrderDetailEntity.findAll", query = "SELECT o FROM OrderDetailEntity o"),
        @NamedQuery(name = "OrderDetailEntity.findByOrderId", query = "SELECT o FROM OrderDetailEntity o WHERE o.order.orderId = :orderId"),
        @NamedQuery(
                name = "OrderDetailEntity.findById",
                query = "SELECT o FROM OrderDetailEntity o WHERE o.order.orderId = :orderId AND o.item.itemId = :itemId AND o.topping.toppingId = :toppingId"
        )
})
public class OrderDetailEntity extends BaseEntity {

    @Id
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    @Id
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topping_id", nullable = false)
    private ToppingEntity topping;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "line_total", nullable = false)
    private double lineTotal;

    @Column
    private double discount;

    @Column(name = "description", columnDefinition = "nvarchar(50)")
    private String description;


    public void setLineTotal() {
        this.lineTotal = (item.getSellingPrice() + topping.getItemToppings()
                .stream()
                .filter(x -> {
                    return x.getItem().equals(item) && x.getTopping().getToppingId().equals(topping.getToppingId());
                })
                .mapToDouble(x -> x.getSellingPrice())
                .sum()) * quantity;
    }


    public void setDiscount() {
//        this.discount = item.getSellingPrice() * item.getTopDiscountPercentage() * quantity;
        this.discount = 0;
    }

}
