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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "order_details")
@NamedQueries({
        @NamedQuery(name = "OrderDetailEntity.findAll", query = "SELECT o FROM OrderDetailEntity o"),
        @NamedQuery(name = "OrderDetailEntity.findByOrderId", query = "SELECT o FROM OrderDetailEntity o WHERE o.orderDetailId.orderId = :orderId"),
        @NamedQuery(name = "OrderDetailEntity.findByItemId", query = "SELECT o FROM OrderDetailEntity o WHERE o.orderDetailId.itemId = :itemId"),
        @NamedQuery(name = "OrderDetailEntity.findByOrderIdAndItemId", query = "SELECT o FROM OrderDetailEntity o WHERE o.orderDetailId.orderId = :orderId AND o.orderDetailId.itemId = :itemId")
})
public class OrderDetailEntity {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private OrderDetailId orderDetailId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "line_total", nullable = false)
    private double lineTotal;

    @Column
    private double discount;

    @Column(name = "description")
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

    public void setOrderDetailId(OrderDetailId orderDetailId) {
        this.orderDetailId = orderDetailId;
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setItem(ItemEntity item) {
//        this.item = item;
    }


    public void setOrder(OrderEntity order) {
        this.order = order;
    }


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

    public void setTopping(ToppingEntity topping) {
//        this.topping = topping;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
