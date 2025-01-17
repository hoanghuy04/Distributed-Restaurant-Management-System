/*
 * @ (#) OrderDetailEntity.java      1.0      1/16/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 1/16/2025
 * @version: 1.0
 */
@Getter
@Entity
@Table(name = "order_details")
@NamedQueries({
        @NamedQuery(name = "OrderDetailEntity.findAll", query = "SELECT o FROM OrderDetailEntity o"),
})
public class OrderDetailEntity extends BaseEntity {
    @Id
    @Column(name = "order_id", columnDefinition = "nvarchar(50)")
    private String orderId;

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "item_id", columnDefinition = "nvarchar(50)")
    private String itemId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "line_total", nullable = false)
    private double lineTotal;

    @Column
    private double discount;

    @Column(name = "description", columnDefinition = "nvarchar(50)")
    private String description;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("itemId")
//    @JoinColumn(name = "item_id", nullable = false)
//    private ItemEntity item;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("toppingId")
//    @JoinColumn(name = "topping_id", nullable = false)
//    private ToppingEntity topping;

    public void setLineTotal() {
//        this.lineTotal = (item.getSellingPrice() + topping.getItemToppings()
//                .stream()
//                .filter(x -> {
//                    return x.getItem().equals(item) && x.getTopping().getToppingId().equals(topping.getToppingId());
//                })
//                .mapToDouble(x -> x.getSellingPrice())
//                .sum()) * quantity;
    }


    public void setDiscount() {
//        this.discount = item.getSellingPrice() * item.getTopDiscountPercentage() * quantity;
    }

}
