/*
 * @ (#) DataFakerUtil.java      1.0      1/18/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package util.datafaker;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.ItemEntity;
import model.OrderDetailEntity;
import model.OrderEntity;
import model.enums.*;
import net.datafaker.Faker;

import java.time.LocalDateTime;
import java.util.HashSet;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 1/18/2025
 * @version: 1.0
 */
public class DataFakerUtil {
    private final Faker faker = new Faker();
    private final EntityManager entityManager;

    public DataFakerUtil(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public void generateOrders(int numberOfOrders) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            for (int i   = 0; i < numberOfOrders; i++) {
                // Tạo đối tượng OrderEntity
                OrderEntity order = new OrderEntity();
                order.setOrderId(faker.idNumber().valid());
                order.setReservationTime(LocalDateTime.now().plusDays(faker.number().numberBetween(1, 30)));
                order.setExpectedCompletionTime(order.getReservationTime().plusHours(faker.number().numberBetween(1, 3)));
                order.setTotalPrice(faker.number().randomDouble(2, 100, 1000));
                order.setTotalDiscount(faker.number().randomDouble(2, 0, 100));
                order.setTotalPaid(order.getTotalPrice() - order.getTotalDiscount());
                order.setNumberOfCustomer(faker.number().numberBetween(1, 10));
                order.setDeposit(faker.number().randomDouble(2, 0, 100));
                order.setOrderStatus(OrderStatusEnum.SINGLE); // Giá trị giả định
                order.setOrderType(OrderTypeEnum.IMMEDIATE); // Giá trị giả định
                order.setPaymentMethod(PaymentMethodEnum.CASH); // Giá trị giả định
                order.setPaymentStatus(PaymentStatusEnum.PAID); // Giá trị giả định
                order.setReservationStatus(ReservationStatusEnum.RECEIVED); // Giá trị giả định

                // Tạo các OrderDetailEntity liên quan
                HashSet<OrderDetailEntity> orderDetails = new HashSet<>();
                int numberOfItems = faker.number().numberBetween(1, 5);

                for (int j = 0; j < numberOfItems; j++) {
                    OrderDetailEntity detail = new OrderDetailEntity();
                    detail.setOrder(order);
//                    detail.setItem(new ItemEntity(faker.food().dish())); // Giả định ItemEntity có constructor nhận tên
                    detail.setQuantity(faker.number().numberBetween(1, 10));
//                    detail.setPrice(faker.number().randomDouble(2, 10, 100));
                    orderDetails.add(detail);
                }

                order.setOrderDetails(orderDetails);

                // Persist đối tượng OrderEntity và các chi tiết liên quan
                entityManager.persist(order);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }
}
