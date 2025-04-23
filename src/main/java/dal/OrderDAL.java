/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import common.Constants;
import dal.connectDB.ConnectDB;
import model.OrderEntity;
import model.PromotionDetailEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import model.enums.CustomerLevelEnum;
import model.enums.OrderTypeEnum;
import model.enums.ReservationStatusEnum;
import util.IDGeneratorUtility;

/**
 * @author hoang
 */
public class OrderDAL implements BaseDAL<OrderEntity, String> {

    private EntityManager em;

    //default constructor
    public OrderDAL() {
        this.em = ConnectDB.getEntityManager();
    }

    public OrderDAL(EntityManager em) {
        this.em = em;
    }

    private EntityTransaction getEntityTransaction() {
        return em.getTransaction();
    }

    @Override
    public boolean insert(OrderEntity orderEntity) {
        orderEntity.setOrderId(IDGeneratorUtility.generateIDWithCreatedDate("O", "orders", "order_id", "reservation_time", em, orderEntity.getReservationTime()));
        System.out.println("Order ID: " + orderEntity.getOrderId());
        return executeTransaction(() -> em.persist(orderEntity));
    }

    @Override
    public boolean update(OrderEntity orderEntity) {
        if (orderEntity.getOrderDetails() == null) {
            orderEntity.setOrderDetails(new HashSet<>());
        }
        return executeTransaction(() -> em.merge(orderEntity));
    }

    @Override
    public boolean deleteById(String orderId) {
        return false;
    }

    @Override
    public OrderEntity findById(String orderId) {
        return em.find(OrderEntity.class, orderId);
    }

    @Override
    public List<OrderEntity> findAll() {
        return em.createNamedQuery("OrderEntity.findAll", OrderEntity.class).getResultList();
    }

    public boolean executeTransaction(Runnable action) {
        EntityTransaction et = getEntityTransaction();
        try {
            et.begin();
            action.run();
            et.commit();
            return true;
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    //    public List<OrderEntity> getListOfReservations(LocalDate date, LocalTime time) {
//        String sql = "SELECT * \n"
//                                    + "FROM orders\n"
//                                    + "WHERE orderType = 'ADVANCE'\n"
//                                    + "AND CONVERT(DATETIME, reservation_time, 120) = CONVERT(DATETIME, ?1, 120);";
//        
//        Query q = em.createNativeQuery(sql).setParameter(1, date + " " + time);
//        return q.getResultList();
//    }
    public List<OrderEntity> getReservationsByDateTime(LocalDate date, LocalTime time, String orderType) {
        String jpql = "SELECT o FROM OrderEntity o WHERE o.paymentStatus = 'UNPAID'";

        if (orderType != null) {
            if (orderType.equals(OrderTypeEnum.ADVANCE.getOrderType())) {
                jpql += " AND o.orderType = 'ADVANCE' AND o.reservationStatus = 'PENDING'";
            } else if (orderType.equals(OrderTypeEnum.IMMEDIATE.getOrderType())) {
                jpql += " AND o.orderType = 'IMMEDIATE' AND o.reservationStatus = 'RECEIVED'";
            }
        }

        Query query;

        if (date != null) {
            if (time == null) {
                jpql += " AND FUNCTION('YEAR', o.reservationTime) = :year " +
                        "AND FUNCTION('MONTH', o.reservationTime) = :month " +
                        "AND FUNCTION('DAY', o.reservationTime) = :day";
                query = em.createQuery(jpql, OrderEntity.class);
                query.setParameter("year", date.getYear());
                query.setParameter("month", date.getMonthValue());
                query.setParameter("day", date.getDayOfMonth());
            } else {
                jpql += " AND o.reservationTime >= :startDateTime AND o.reservationTime < :endDateTime";
                LocalDateTime startDateTime = LocalDateTime.of(date, time);
                LocalDateTime endDateTime = startDateTime.plusDays(1);
                query = em.createQuery(jpql, OrderEntity.class);
                query.setParameter("startDateTime", startDateTime);
                query.setParameter("endDateTime", endDateTime);
            }
        } else {
            jpql += " AND o.reservationTime >= :dateTime";
            LocalDateTime dateTime = LocalDateTime.now().minusMinutes(Constants.RESERVATION_TIMEOUT_MINUTES);
            query = em.createQuery(jpql, OrderEntity.class);
            query.setParameter("dateTime", dateTime);
        }

        return query.getResultList();
    }

    public List<OrderEntity> getCurrentOrdersAndReservations(LocalDateTime time, int option) {
        LocalDateTime timePlusOneHour = time.plusHours(1);
        LocalDateTime timeMinusOneHour = time.minusHours(1);

        String jpql = "SELECT o FROM OrderEntity o " +
                "WHERE o.paymentStatus = 'UNPAID' " +
                "AND o.reservationStatus NOT IN (:excludedStatuses) " +
                "AND (:timePlusOneHour > o.reservationTime OR o.expectedCompletionTime IS NULL) " +
                "AND :timeMinusOneHour <= o.reservationTime " +
                "AND o.orderStatus = 'SINGLE'";

        if (option == -1) {
            jpql += " AND o.reservationStatus = 'PENDING'";
        } else if (option == 1) {
            jpql += " AND o.reservationStatus = 'RECEIVED'";
        }

        Query query = em.createQuery(jpql, OrderEntity.class);
        query.setParameter("excludedStatuses", List.of(ReservationStatusEnum.CANCELED));
        query.setParameter("timePlusOneHour", timePlusOneHour);
        query.setParameter("timeMinusOneHour", timeMinusOneHour);

        return query.getResultList();
    }

    public List<OrderEntity> getReservationsByOption(String option) {
    LocalDateTime currentTime = LocalDateTime.now();
    LocalDateTime considerationTime;
    String jpql = "SELECT o FROM OrderEntity o WHERE o.reservationStatus = 'PENDING' " +
                  "AND o.reservationTime BETWEEN :startTime AND :endTime";

    switch (option.trim()) {
        case "Sắp tới":
            considerationTime = currentTime.plusMinutes(Constants.RESERVATION_TIMEOUT_MINUTES);
            break;
        case "Quá hạn":
            considerationTime = currentTime.minusMinutes(Constants.RESERVATION_TIMEOUT_MINUTES);
            break;
        default:
            throw new IllegalArgumentException("Tùy chọn không hợp lệ: " + option);
    }

    Query query = em.createQuery(jpql, OrderEntity.class);
    query.setParameter("startTime", option.equals("Sắp tới") ? currentTime : considerationTime);
    query.setParameter("endTime", option.equals("Sắp tới") ? considerationTime : currentTime);

    return query.getResultList();
}

    public OrderEntity findByTableId(String tableId) {
        String jpql = "SELECT o FROM OrderEntity o WHERE o.table.tableId = :tableId AND o.paymentStatus = 'UNPAID'";
        Query query = em.createQuery(jpql, OrderEntity.class);
        query.setParameter("tableId", tableId);
        try {
            return (OrderEntity) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public List<OrderEntity> getOrdersByYear(int year) {
        String sql = "SELECT o from OrderEntity o where YEAR(o.reservationTime)= :year";

        Query q = em.createQuery(sql, OrderEntity.class);
        q.setParameter("year", year);
        return q.getResultList();

    }

    public List<OrderEntity> getOrdersbyOrderdate(LocalDateTime startedDate, LocalDateTime endedDate) {
        String sql = "SELECT o from OrderEntity o where o.reservationTime between :startDate and :endDate and o.paymentStatus = 'PAID'";
        Query q = em.createQuery(sql, OrderEntity.class);
        q.setParameter("startDate", startedDate);
        q.setParameter("endDate", endedDate);
        return q.getResultList();
    }

    public List<OrderEntity> getListOrder(LocalDateTime startDateTime, LocalDateTime endDateTime, String staff, String rank) {
        String jpql = "SELECT o FROM OrderEntity o " +
                      "JOIN o.employee e " +
                      "JOIN o.customer c " +
                      "WHERE o.reservationTime BETWEEN :startDateTime AND :endDateTime";

        if (staff != null && !staff.trim().isEmpty() && !staff.equals("Chọn nhân viên")) {
            jpql += " AND e.fullname = :staff";
        }
        if (rank != null && !rank.trim().isEmpty() && !rank.equals("Chọn hạng thành viên")) {
            jpql += " AND c.customerLevel = :rank";
        }

        Query query = em.createQuery(jpql, OrderEntity.class);
        query.setParameter("startDateTime", startDateTime);
        query.setParameter("endDateTime", endDateTime);

        if (staff != null && !staff.trim().isEmpty() && !staff.equals("Chọn nhân viên")) {
            query.setParameter("staff", staff);
        }
        if (rank != null && !rank.trim().isEmpty() && !rank.equals("Chọn hạng thành viên")) {
            CustomerLevelEnum rankEnum = CustomerLevelEnum.convertToEnum(rank);
            query.setParameter("rank", rankEnum);
        }

        return query.getResultList();
    }

    public List<OrderEntity> getListMergedOrder() {
        String jpql = "SELECT o FROM OrderEntity o " +
                "WHERE o.paymentStatus = 'UNPAID' " +
                "AND o.combinedTables IS NOT NULL";

        Query query = em.createQuery(jpql, OrderEntity.class);
        return query.getResultList();
    }

    public List<OrderEntity> getOrdersbyDateAndOption(LocalDateTime startedDate, LocalDateTime endedDate, String option) {
        String sql = "SELECT o from OrderEntity o " +
                "where o.reservationTime between :startTime and :endTime and o.paymentStatus = 'PAID' ";
        if (Objects.equals(option, "IMMEDIATE")) {
            sql += "and o.orderType = OrderTypeEnum.IMMEDIATE ";
        }
        if (Objects.equals(option, "ADVANCE")) {
            sql += "and o.orderType = OrderTypeEnum.ADVANCE ";
        }
        Query q = em.createQuery(sql, OrderEntity.class);
        q.setParameter("startTime", startedDate);
        q.setParameter("endTime", endedDate);
        return q.getResultList();
    }

    //test getOrdersbyDateAndOption
    public static void main(String[] args) {
        ConnectDB.connect();
        OrderDAL orderDAL = new OrderDAL();
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 5, 31, 23, 59);
        String option = "IMMEDIATE";
        List<OrderEntity> orders = orderDAL.getOrdersbyDateAndOption(startDate, endDate, option);
        for (OrderEntity order : orders) {
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("Reservation Time: " + order.getReservationTime());
            System.out.println("Order Type: " + order.getOrderType());
            System.out.println("Payment Status: " + order.getPaymentStatus());
            System.out.println("Customer Name: " + order.getCustomer().getName());
            System.out.println("-----------------------------");
        }
    }

}
