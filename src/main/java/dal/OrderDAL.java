/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import common.Constants;
import model.OrderEntity;
import model.PromotionDetailEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import model.enums.OrderTypeEnum;
import util.IDGeneratorUtility;

/**
 *
 * @author hoang
 */
public class OrderDAL implements BaseDAL<OrderEntity, String> {

    private EntityManager em;

    public OrderDAL(EntityManager em) {
        this.em = em;
    }

    private EntityTransaction getEntityTransaction() {
        return em.getTransaction();
    }

    @Override
    public boolean insert(OrderEntity orderEntity) {
        orderEntity.setOrderId(IDGeneratorUtility.generateIDWithCreatedDate("O", "orders", "order_id", "reservation_time", em, orderEntity.getReservationTime()));
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
    public Optional<OrderEntity> findById(String orderId) {
        return Optional.ofNullable(em.find(OrderEntity.class, orderId));
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
        String sql = "SELECT * FROM orders WHERE 1 = 1 ";

        // Kiểm tra orderType
        if (orderType != null) {
            if (orderType.equals(OrderTypeEnum.ADVANCE.getOrderType())) {
                sql += " AND order_type = 'ADVANCE' and reservation_status = 'PENDING' ";
            } else if (orderType.equals(OrderTypeEnum.IMMEDIATE.getOrderType())) {
                sql += " AND order_type = 'IMMEDIATE'  and reservation_status = 'RECEIVED' ";
            }
        }

        sql += " AND payment_status = 'UNPAID' ";

        Query q = null;

        if (date != null) {
            if (time == null) {
                sql += " AND YEAR(reservation_time) = YEAR(?1) "
                        + " AND MONTH(reservation_time) = MONTH(?1) "
                        + " AND DAY(reservation_time) = DAY(?1) ";
                q = em.createNativeQuery(sql, OrderEntity.class);
                q.setParameter(1, date);
            } else {
                sql += " AND reservation_time >= ?1 "
                        + " AND reservation_time < ?2";  // Sửa đổi dấu <= thành < để không lấy ngày kế tiếp
                LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
                LocalDateTime theNextDay = LocalDateTime.of(date.plusDays(1), LocalTime.of(0, 0));
                q = em.createNativeQuery(sql, OrderEntity.class);
                q.setParameter(1, reservationDateTime);
                q.setParameter(2, theNextDay);
            }
        } else {
            sql += " AND reservation_time >= ?1 ";
            LocalDateTime dateTime = LocalDateTime.now().minusMinutes(Constants.RESERVATION_TIMEOUT_MINUTES);
            q = em.createNativeQuery(sql, OrderEntity.class);
            q.setParameter(1, dateTime);
        }

        return q.getResultList();
    }

    public List<OrderEntity> getCurrentOrdersAndReservations(LocalDateTime time, int option) {
        String sql = "SELECT * FROM orders WHERE 1 = 1 and payment_status = 'UNPAID' and reservation_status not in ('CANCELED')";

        if (option == -1) {
            sql += " AND reservation_status = 'PENDING' ";
        } else if (option == 1) {
            sql += " AND  reservation_status = 'RECEIVED' ";
        }

        sql += " AND (?1 < DATEADD(HOUR, 1, reservation_time) OR expected_completion_time IS NULL)"
                + " AND ?1 >= DATEADD(HOUR, -1, reservation_time) "
                + " AND order_status = 'SINGLE' ";
        Query q = em.createNativeQuery(sql, OrderEntity.class);
        q.setParameter(1, time);
        return q.getResultList();
    }

        public List<OrderEntity> getReservationsbyOption(String option) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime considerationTime = null;
        Query q = null;

        String sql = "select * from orders\n"
                + "where reservation_status = 'PENDING' and reservation_time between ?1 and ?2";

        switch (option.trim()) {
            case "Sắp tới":
                considerationTime = currentTime.plusMinutes(Constants.RESERVATION_TIMEOUT_MINUTES);
                q = em.createNativeQuery(sql, OrderEntity.class);
                q.setParameter(1, currentTime);
                q.setParameter(2, considerationTime);
                break;
            case "Quá hạn":
                considerationTime = currentTime.minusMinutes(Constants.RESERVATION_TIMEOUT_MINUTES);
                q = em.createNativeQuery(sql, OrderEntity.class);
                q.setParameter(1, considerationTime);
                q.setParameter(2, currentTime);
                break;
            default:
                throw new IllegalArgumentException("Tùy chọn không hợp lệ: " + option);
        }

        return q.getResultList();

    }

    public Optional<OrderEntity> findByTableId(String tableId) {
        String sql = "select o.* from orders o "
                + "where o.table_id = ?1 "
                + "and payment_status = 'UNPAID'";
        Query q = em.createNativeQuery(sql, OrderEntity.class);
        q.setParameter(1, tableId);
        try {
            return Optional.ofNullable((OrderEntity) q.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<OrderEntity> getOrdersbyYear(int year) {
        String sql = "SELECT o.* from orders o where YEAR(o.order_date)=?";
        Query q = em.createNativeQuery(sql, OrderEntity.class);
        q.setParameter(1, year);
        return q.getResultList();
    }

    public List<OrderEntity> getOrdersbyOrderdate(LocalDateTime startedDate, LocalDateTime endedDate) {
        String sql = "SELECT o.* from orders o where o.reservation_time between ? and ? and o.payment_status = 'PAID'";
        Query q = em.createNativeQuery(sql, OrderEntity.class);
        q.setParameter(1, startedDate);
        q.setParameter(2, endedDate);
        return q.getResultList();
    }

    public List<OrderEntity> getListOrder(LocalDateTime startDateTime, LocalDateTime endDateTime, String staff, String rank) {
        String sql = "SELECT o.* "
                + "FROM orders o "
                + "JOIN employees e ON o.employee_id = e.employee_id "
                + "JOIN customers c ON o.customer_id = c.customer_id "
                + "WHERE o.order_date BETWEEN ?1 AND ?2 ";
        if (!staff.trim().equals("") && !staff.equals("Chọn nhân viên")) {
            sql += " AND e.fullname = N'" + staff + "' ";
        }
        if (!rank.trim().equals("") && !rank.equals("Chọn hạng thành viên")) {
            sql += " AND c.level_customer = '" + rank + "' ";
        }
        Query q = em.createNativeQuery(sql, OrderEntity.class);
        q.setParameter(1, startDateTime);
        q.setParameter(2, endDateTime);

        return q.getResultList();
    }

    public List<OrderEntity> getListMergedOrder() {
        String sql = "select o.* from orders o "
                + "where o.payment_status = 'UNPAID' "
                + "and combined_tables is not null ";
        Query q = em.createNativeQuery(sql, OrderEntity.class);
        return q.getResultList();
    }
    
    public List<OrderEntity> getOrdersbyDateAndOption(LocalDateTime startedDate, LocalDateTime endedDate, String option) {
        String sql = "SELECT o.* from orders o where o.reservation_time between ? and ? and o.payment_status = 'PAID' ";
        if (option == "IMMEDIATE") {
            sql += "and o.order_type = 'IMMEDIATE' ";
        }
        if (option == "ADVANCE") {
            sql += "and o.order_type = 'ADVANCE' ";
        }
        Query q = em.createNativeQuery(sql, OrderEntity.class);
        q.setParameter(1, startedDate);
        q.setParameter(2, endedDate);
        return q.getResultList();
    }
}
