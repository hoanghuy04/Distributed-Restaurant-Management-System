/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus.impl;

import bus.OrderBUS;
import bus.TableBUS;
import bus.request.ClientCallback;
import bus.request.OrderRequest;
import dal.OrderDAL;
import dal.connectDB.ConnectDB;
import model.OrderEntity;
import model.TableEntity;
import jakarta.persistence.EntityManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import model.enums.PaymentStatusEnum;
import model.enums.TableStatusEnum;
import util.SortOrderByReservaionTimeUtil;

/**
 *
 * @author hoang
 */
public class OrderBUSImpl extends UnicastRemoteObject implements bus.OrderBUS {

    private OrderDAL orderDAL;
    private TableBUS tableBUS;
    private OrderQueueProcessor queueProcessor;
    private final EntityManager em;

    public OrderBUSImpl(EntityManager em) throws Exception{
        this.em = em;
        orderDAL = new OrderDAL(em);
        queueProcessor = new OrderQueueProcessor(this);
    }

    // Lazy initialization của tableBUS
    private TableBUS getTableBUS() throws RemoteException {
        if (tableBUS == null) {
            tableBUS = new TableBUSImpl(em);
        }
        return tableBUS;
    }

    @Override
    public synchronized OrderEntity insertEntity(OrderEntity orderEntity)  throws RemoteException {
        String tableId = orderEntity.getTable().getTableId();
        try {
            // Lưu đơn hàng
            OrderEntity savedOrder = orderDAL.insert(orderEntity);
            // Cập nhật trạng thái bàn thành OCCUPIED
            getTableBUS().unlockTable(tableId, TableStatusEnum.OCCUPIED);
            return savedOrder;
        } catch (Exception e) {
            // Nếu có lỗi, mở khóa bàn
            getTableBUS().unlockTable(tableId, TableStatusEnum.AVAILABLE);
            throw new RemoteException("Lỗi khi tạo đơn hàng: " + e.getMessage());
        }
    }

    @Override
    public OrderEntity updateEntity(OrderEntity orderEntity)  throws RemoteException {
        String tableId = orderEntity.getTable().getTableId();
        try {
            // Cập nhật đơn hàng
            OrderEntity updated = orderDAL.update(orderEntity);
            // Cập nhật trạng thái bàn
            TableStatusEnum finalStatus = (orderEntity.getPaymentStatus() == PaymentStatusEnum.PAID)
                    ? TableStatusEnum.AVAILABLE
                    : TableStatusEnum.OCCUPIED;
            getTableBUS().unlockTable(tableId, finalStatus);
            return updated;
        } catch (Exception e) {
            // Nếu có lỗi, mở khóa bàn
            getTableBUS().unlockTable(tableId, TableStatusEnum.AVAILABLE);
            throw new RemoteException("Lỗi khi cập nhật đơn hàng: " + e.getMessage());
        }
    }

    public synchronized void queueOrderRequest(OrderEntity orderEntity, PaymentStatusEnum paymentStatus, ClientCallback callback) throws Exception {
        OrderRequest request = new OrderRequest(orderEntity, paymentStatus, callback);
        queueProcessor.addOrderRequest(request);
    }

    @Override
    public boolean deleteEntity(String id)  throws RemoteException {
        return orderDAL.deleteById(id);
    }

    @Override
    public OrderEntity getEntityById(String id)  throws RemoteException {
        return orderDAL.findById(id);
    }

    @Override
    public List<OrderEntity> getAllEntities()  throws RemoteException {
        List<OrderEntity> orders = orderDAL.findAll();
        SortOrderByReservaionTimeUtil.sortAscByFloorAndTableId(orders);
        return orders;
    }

    @Override
    public List<OrderEntity> getListOfReservations(LocalDate date, LocalTime time, String orderType)  throws RemoteException {
        List<OrderEntity> reservations = orderDAL.getReservationsByDateTime(date, time, orderType);
        SortOrderByReservaionTimeUtil.sortAscByFloorAndTableId(reservations);
        return reservations;
    }

    @Override
    public List<OrderEntity> getReservationsByOption(String option)  throws RemoteException {
        List<OrderEntity> reservations = orderDAL.getReservationsByOption(option);
        SortOrderByReservaionTimeUtil.sortAscByFloorAndTableId(reservations);
        return reservations;
    }

    @Override
    public List<OrderEntity> getCurrentOrdersAndReservations(LocalDateTime time, int option)  throws RemoteException {
        List<OrderEntity> reservations = orderDAL.getCurrentOrdersAndReservations(time, option);
        SortOrderByReservaionTimeUtil.sortAscByFloorAndTableId(reservations);
        return reservations;
    }

    @Override
    public OrderEntity findByTableId(String tableId)  throws RemoteException {
        OrderEntity od = orderDAL.findByTableId(tableId);
        if (od == null)   {
            return od;
        }
//        od.setCombinedTables(od.getCombinedTables().stream().map(x -> FormLoad.tableBUS.getEntityById(x.getTableId())).collect(Collectors.toList()));

        return od;
    }

    @Override
    public double getTotalRevenue(LocalDateTime startedDate, LocalDateTime endedDate)  throws RemoteException {
        return orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .mapToDouble(x -> x.getTotalPaid() + x.getDeposit())
                .sum();
    }

    @Override
    public double getTotalCapital(LocalDateTime startedDate, LocalDateTime endedDate)  throws RemoteException {
        return orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .flatMap(o -> o.getOrderDetails().stream())
                .mapToDouble(od -> (od.getItem().getCostPrice() + od.getTopping().getCostPrice()) * od.getQuantity())
                .sum();
    }
    
    @Override
    public int getTotalCustomers(LocalDateTime startedDate, LocalDateTime endedDate)  throws RemoteException {
        return orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .mapToInt(x -> x.getNumberOfCustomer())
                .sum();
    }
    
    @Override
    public long getTotalOrder(LocalDateTime startedDate, LocalDateTime endedDate)  throws RemoteException {
        return orderDAL.getOrdersbyOrderdate(startedDate, endedDate)
                .stream()
                .count();
    }
    @Override
    public double getRevenueByYear(int year)  throws RemoteException {
        return orderDAL.getOrdersByYear(year).stream()
                .mapToDouble(x -> x.getTotalPaid() + x.getDeposit())
                .sum();
    }

    @Override
    public double getCapitalByYear(int year)  throws RemoteException {
        return orderDAL.getOrdersByYear(year).stream()
                .flatMap(o -> o.getOrderDetails().stream())
                .mapToDouble(od -> (od.getItem().getCostPrice() + od.getTopping().getCostPrice())*od.getQuantity())
                .sum();
    }

    @Override
    public Map<String, Map<Double, Double>> getRevenueStatsByDateOrYear(LocalDateTime startedDate, LocalDateTime endedDate, Integer year)  throws RemoteException {
        if (year != null) {
            startedDate = LocalDateTime.of(year, 1, 1, 0, 0);
            endedDate = LocalDateTime.of(year, 12, 31, 23, 59);
        }

        Map<String, Double> revenue = orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .collect(Collectors.groupingBy(
                        o -> year != null
                                ? o.getReservationTime().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                                : o.getReservationTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        Collectors.summingDouble(o -> o.getTotalPaid() + o.getDeposit())
                ));

        Map<String, Double> capital = orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .flatMap(o -> o.getOrderDetails().stream())
                .collect(Collectors.groupingBy(
                        od -> year != null
                                ? od.getOrder().getReservationTime().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                                : od.getOrder().getReservationTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        Collectors.summingDouble(od -> (od.getItem().getCostPrice() + od.getTopping().getCostPrice())*od.getQuantity() )
                ));

        Map<String, Map<Double, Double>> result = new LinkedHashMap<>();

        revenue.forEach((dateOrMonth, revenueAmount) -> {
            Double capitalAmount = capital.getOrDefault(dateOrMonth, 0.0);
            Map<Double, Double> revenueAndCapital = new LinkedHashMap<>();
            revenueAndCapital.put(revenueAmount, capitalAmount);
            result.put(dateOrMonth, revenueAndCapital);
        });

        return result.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(
                e2.getValue().keySet().iterator().next(),
                e1.getValue().keySet().iterator().next()))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    @Override
    public List<OrderEntity> getListOrder(LocalDateTime startDateTime, LocalDateTime endDateTime, String staff, String rank)  throws RemoteException {
        return orderDAL.getListOrder(startDateTime, endDateTime, staff, rank);
    }

    @Override
    public OrderEntity getMergedOrderByCombineTable(TableEntity t)  throws RemoteException {
        List<OrderEntity> mergedOrders = orderDAL.getListMergedOrder();
        return mergedOrders.stream()
                .filter(o -> o.getCombinedTables().contains(t) && o.getReservationTime().toLocalDate().equals(LocalDate.now())).findFirst().orElse(null);
    }

    @Override
    public Map<String, Integer> getTotalFrequencyByHours(LocalDateTime startDateTime, LocalDateTime endDateTime, String option)  throws RemoteException {
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:00");
        LocalTime startHour = LocalTime.of(9, 0); // 9:00
        LocalTime endHour = LocalTime.of(21, 0);  // 21:00

        Map<String, Integer> map = orderDAL.getOrdersbyDateAndOption(startDateTime, endDateTime, "ALL")
                .stream()
                .filter(order -> {
                    LocalTime orderTime = order.getReservationTime().toLocalTime();
                    return !orderTime.isBefore(startHour) && !orderTime.isAfter(endHour);
                })
                .collect(Collectors.groupingBy(
                        order -> order.getReservationTime().format(hourFormatter),
                        Collectors.summingInt(order -> 1)
                ));
        if (!option.equals("ALL"))  {
            map = orderDAL.getOrdersbyDateAndOption(startDateTime, endDateTime, "ALL")
                    .stream()
                    .filter(o -> o.getOrderType().toString().equals(option))
                    .filter(order -> {
                        LocalTime orderTime = order.getReservationTime().toLocalTime();
                        return !orderTime.isBefore(startHour) && !orderTime.isAfter(endHour);
                    })
                    .collect(Collectors.groupingBy(
                            order -> order.getReservationTime().format(hourFormatter),
                            Collectors.summingInt(order -> 1)
                    ));
        }
        return new TreeMap<>(map);
    }
    
    @Override
        public Map<String, Double> getTotalRevenueByHours(LocalDateTime startDateTime, LocalDateTime endDateTime)  throws RemoteException {
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:00");
        LocalTime startHour = LocalTime.of(9, 0); 
        LocalTime endHour = LocalTime.of(21, 0);  

        Map<String, Double> map = orderDAL.getOrdersbyOrderdate(startDateTime, endDateTime)
                .stream()
                .filter(order -> {
                    LocalTime orderTime = order.getReservationTime().toLocalTime();
                    return !orderTime.isBefore(startHour) && !orderTime.isAfter(endHour);
                })
                .collect(Collectors.groupingBy(
                        order -> order.getReservationTime().format(hourFormatter),
                        Collectors.summingDouble(o -> o.getTotalPaid() + o.getDeposit())
                ));
        
        return new TreeMap<>(map);
    }
    
    @Override
    public int getQtyFrequencyByDate(LocalDateTime startDateTime, LocalDateTime endDateTime, String option)  throws RemoteException {
        if (!option.equals("ALL"))   {
            return (int) orderDAL.getOrdersbyDateAndOption(startDateTime, endDateTime, option).stream()
                    .filter(x -> x.getOrderType().toString().equals(option))
                    .count();
        }
        return orderDAL.getOrdersbyDateAndOption(startDateTime, endDateTime, option).size();
    }

    @Override
    public double getRevenueOfFrequencyByDate(LocalDateTime startDateTime, LocalDateTime endDateTime, String option)  throws RemoteException {
        if (!option.equals("ALL"))  {
            return orderDAL.getOrdersbyDateAndOption(startDateTime, endDateTime, option).stream()
                    .filter(x -> x.getOrderType().toString().equals(option))
                    .mapToDouble(x -> x.getTotalPaid() + x.getDeposit())
                    .sum();
        }
        return orderDAL.getOrdersbyDateAndOption(startDateTime, endDateTime, option).stream()
                .mapToDouble(x -> x.getTotalPaid() + x.getDeposit())
                .sum();
    }

    @Override
    public Map<String, Map<Integer, Integer>> getFrequencyPromotionStatsbyDatetime(LocalDateTime startedDate, LocalDateTime endedDate)  throws RemoteException {
        Map<String, Integer> mapOrder = orderDAL.getOrdersbyOrderdate(startedDate, endedDate)
                .stream()
                .filter(order -> order.getPromotion() != null)
                .filter(order -> order.getPromotion().getPromotionId() != null)
                .collect(Collectors.groupingBy(
                        o -> o.getReservationTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        Collectors.summingInt(o -> 1)
                ));
        mapOrder = new TreeMap<>(mapOrder);

        Map<String, Integer> mapItem = orderDAL.getOrdersbyOrderdate(startedDate, endedDate)
                .stream()
                .flatMap(o -> o.getOrderDetails().stream()
                .map(od -> new AbstractMap.SimpleEntry<>(o.getReservationTime(), od)))
                .filter(entry -> entry.getValue().getDiscount() > 0)
                .collect(Collectors.groupingBy(
                        entry -> entry.getKey().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        Collectors.summingInt(entry -> entry.getValue().getQuantity())
                ));
        mapItem = new TreeMap<>(mapItem);

        Map<String, Map<Integer, Integer>> result = new TreeMap<>();

        for (String date : mapOrder.keySet()) {
            int orderCount = mapOrder.getOrDefault(date, 0);
            int itemCount = mapItem.getOrDefault(date, 0);

            Map<Integer, Integer> stats = new HashMap<>();
            stats.put(1, orderCount);
            stats.put(2, itemCount);

            result.put(date, stats);
        }

        for (String date : mapItem.keySet())   {
            if (!result.containsKey(date))  {
                int orderCount = mapOrder.getOrDefault(date, 0);
                int itemCount = mapItem.get(date);

                Map<Integer, Integer> stats = new HashMap<>();
                stats.put(1, orderCount);
                stats.put(2, itemCount);

                result.put(date, stats);
            }
        }

        return result;
    }

    @Override
    public List<OrderEntity> findOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate) throws RemoteException   {
        return orderDAL.findOrdersBetweenDates(startDate, endDate);
    }

    @Override
    public double getTotalDiscount(LocalDateTime startedDate, LocalDateTime endedDate)  throws RemoteException {

        return orderDAL.getOrdersbyOrderdate(startedDate, endedDate)
                .stream()
                .filter(x -> x.getTotalDiscount() > 0)
                .mapToDouble(o -> o.getTotalDiscount())
                .sum();
    }

    public static void main(String[] args) throws Exception {
        OrderBUS orderBUS = new OrderBUSImpl(ConnectDB.getEntityManager());
        orderBUS.findOrdersBetweenDates(LocalDate.of(2025, 3, 1).atStartOfDay(), LocalDate.of(2025,6,30).atTime(23,59))
                .forEach(System.out::println);
    }
}
