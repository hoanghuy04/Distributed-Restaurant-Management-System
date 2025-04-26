/*
 * @ (#) OrderEntity.java      1.0      1/16/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package model;

import common.Constants;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import model.enums.*;
import util.CombinedTableConverterUtil;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 1/16/2025
 * @version: 1.0
 */
@Data
@Entity
@Table(name = "orders")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NamedQueries({
    @NamedQuery(name = "OrderEntity.findAll", query = "select o from OrderEntity o")
})
public class OrderEntity extends BaseEntity implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "order_id", columnDefinition = "nvarchar(50)")
    private String orderId;

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

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "table_id")
    private TableEntity table;

    @ToString.Exclude
    @Convert(converter = CombinedTableConverterUtil.class)
    @Column(name = "combined_tables")
    private List<TableEntity> combinedTables;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private PromotionEntity promotion;

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

    @ToString.Exclude
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<OrderDetailEntity> orderDetails;

    @Override
    @Column(name = "order_date", nullable = false, updatable = false)
    public LocalDateTime getCreatedDate() {
        return super.getCreatedDate();
    }

    public OrderEntity() {
        this.reservationTime = LocalDateTime.now();
        this.expectedCompletionTime = LocalDateTime.now().plusMinutes(Constants.RESERVATION_TIMEOUT_MINUTES);
        this.orderStatus = OrderStatusEnum.SINGLE;
        this.orderType = OrderTypeEnum.IMMEDIATE;
        this.paymentStatus = PaymentStatusEnum.UNPAID;
        this.paymentMethod = PaymentMethodEnum.CASH;
        this.reservationStatus = ReservationStatusEnum.RECEIVED;
        this.orderDetails = new HashSet<>();
        this.combinedTables = new ArrayList<>();
    }

    public OrderEntity(String orderId) {
        setOrderId(orderId);
        this.reservationTime = LocalDateTime.now();
    }

    public OrderEntity(LocalDateTime reservationTime, LocalDateTime expectedCompletionTime, int numberOfCustomer, double deposit,
            CustomerEntity customer, EmployeeEntity employee, TableEntity table, OrderStatusEnum orderStatus, OrderTypeEnum orderType,
            PaymentMethodEnum paymentMethod, PaymentStatusEnum paymentStatus, ReservationStatusEnum reservationStatus, HashSet<OrderDetailEntity> orderDetails, List<TableEntity> listOfCombinedTable) {
        this.reservationTime = reservationTime;
        this.expectedCompletionTime = expectedCompletionTime;
        this.numberOfCustomer = numberOfCustomer;
        this.deposit = deposit;
        this.customer = customer;
        this.employee = employee;
        this.table = table;
        this.orderStatus = orderStatus;
        this.orderType = orderType;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.reservationStatus = reservationStatus;
        this.orderDetails = orderDetails;
        this.combinedTables = listOfCombinedTable;
    }

    public OrderEntity(String orderId, LocalDateTime orderDate, LocalDateTime reservationTime, LocalDateTime expectedCompletionTime, double totalPrice, double totalDiscount,
            double totalPaid, int numberOfCustomer, double deposit, CustomerEntity customer, EmployeeEntity employee, TableEntity table,
            PromotionEntity promotion, OrderStatusEnum orderStatus, OrderTypeEnum orderType, ReservationStatusEnum reservationStatus, PaymentMethodEnum paymentMethod,
            PaymentStatusEnum paymentStatus, HashSet<OrderDetailEntity> orderDetails, List<TableEntity> listOfCombinedTable) {
        setOrderId(orderId);
        this.reservationTime = reservationTime;
        this.expectedCompletionTime = expectedCompletionTime;
        this.totalPrice = totalPrice;
        this.totalDiscount = totalDiscount;
        this.totalPaid = totalPaid;
        this.numberOfCustomer = numberOfCustomer;
        this.deposit = deposit;
        this.customer = customer;
        this.employee = employee;
        this.table = table;
        this.promotion = promotion;
        this.orderStatus = orderStatus;
        this.orderType = orderType;
        this.paymentMethod = paymentMethod;
        this.reservationStatus = reservationStatus;
        this.paymentStatus = paymentStatus;
        this.orderDetails = orderDetails;
        this.combinedTables = listOfCombinedTable;
    }

    public void setTotalPrice() {
        if (orderDetails == null) {
            this.totalPrice = 0;
        } else {
            this.totalPrice = this.orderDetails.stream()
                    .mapToDouble(OrderDetailEntity::getLineTotal)
                    .sum();
        }
    }

    public void setTotalDiscount() {
        if (orderDetails == null) {
            this.totalDiscount = 0;
        } else {
            double discountPercentage = 0;
            if (this.getPromotion() != null) {
                discountPercentage = this.getPromotion().getDiscountPercentage();
            }
            double itemDiscount = this.orderDetails.stream()
                    .mapToDouble(OrderDetailEntity::getDiscount)
                    .sum();
            this.totalDiscount = itemDiscount + (totalPrice - itemDiscount - deposit) * discountPercentage;
        }
    }

    public void setTotalPaid() {
        if (orderDetails == null) {
            this.totalPaid = 0;
        } else {
            this.totalPaid = totalPrice - totalDiscount - deposit;
        }
    }

    public boolean insertOrderDetail(OrderDetailEntity orderDetail) {
        Set<OrderDetailEntity> orderDetails = this.getOrderDetails();

        Optional<OrderDetailEntity> existingOrderDetail = orderDetails.stream()
                .filter(od -> od.equals(orderDetail))
                .findFirst();

        if (existingOrderDetail.isPresent()) {
            existingOrderDetail.get().setQuantity(
                    existingOrderDetail.get().getQuantity() + orderDetail.getQuantity()
            );
        } else {
            orderDetails.add(orderDetail);
        }

        this.setOrderDetails(orderDetails);

        return true;
    }
}
