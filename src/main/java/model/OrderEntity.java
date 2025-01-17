/*
 * @ (#) OrderEntity.java      1.0      1/16/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import model.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 1/16/2025
 * @version: 1.0
 */
@Entity
@Table(name = "orders")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NamedQueries({
        @NamedQuery(name = "OrderEntity.findAll", query = "select o from OrderEntity o")
})
public class OrderEntity {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "reservation_time", nullable = false)
    private LocalDateTime reservationTime;

    @Column(name = "expected_completion_time")
    private LocalDateTime expectedCompletionTime;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Column(name = "total_discount", nullable = false)
    private double totalDiscount;

    @Column(name = "total_paid", nullable = false)
    private double totalPaid;

    @Column(name = "number_of_customer", nullable = false)
    private int numberOfCustomer;

    @Column(name = "deposit", nullable = false)
    private double deposit;

//    @ManyToOne
//    @JoinColumn(name = "customer_id")
//    private CustomerEntity customer;
//
//    @ManyToOne
//    @JoinColumn(name = "employee_id")
//    private EmployeeEntity employee;
//
//    @ManyToOne
//    @JoinColumn(name = "table_id")
//    private TableEntity table;
//
//    @Convert(converter = CombinedTableConverter.class)
//    @Column(name = "combined_tables")
//    private List<TableEntity> combinedTables;
//
//    @ManyToOne
//    @JoinColumn(name = "promotion_id")
//    private PromotionEntity promotion;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, columnDefinition = "nvarchar(50)")
    private OrderStatusEnum orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false, columnDefinition = "nvarchar(50)")
    private OrderTypeEnum orderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, columnDefinition = "nvarchar(50)")
    private PaymentMethodEnum paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, columnDefinition = "nvarchar(50)")
    private PaymentStatusEnum paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status", nullable = false, columnDefinition = "nvarchar(50)")
    private ReservationStatusEnum reservationStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetailEntity> orderDetails;




    public void setTotalPrice() {
//        if (orderDetails == null) {
//            this.totalPrice = 0;
//        } else {
//            this.totalPrice = this.orderDetails.stream()
//                    .mapToDouble(OrderDetailEntity::getLinetotal)
//                    .sum();
//        }
    }

    public void setTotalDiscount() {
//        if (orderDetails == null) {
//            this.totalDiscount = 0;
//        } else {
//            double discountPercentage = 0;
//            if (this.getPromotion() != null) {
//                discountPercentage = this.getPromotion().getDiscountPercentage();
//            }
//            double itemDiscount = this.orderDetails.stream()
//                    .mapToDouble(OrderDetailEntity::getDiscount)
//                    .sum();
//            this.totalDiscount = itemDiscount + (totalPrice - itemDiscount - deposit) * discountPercentage;
//        }
    }

    public boolean insertOrderDetail(OrderDetailEntity orderDetail) {
//        List<OrderDetailEntity> orderDetails = this.getOrderDetails();
//
//        Optional<OrderDetailEntity> existingOrderDetail = orderDetails.stream()
//                .filter(od -> od.equals(orderDetail))
//                .findFirst();
//
//        if (existingOrderDetail.isPresent()) {
//            existingOrderDetail.get().setQuantity(
//                    existingOrderDetail.get().getQuantity() + orderDetail.getQuantity()
//            );
//        } else {
//            orderDetails.add(orderDetail);
//        }
//
//        this.setOrderDetails(orderDetails);
//
        return true;
    }
}
