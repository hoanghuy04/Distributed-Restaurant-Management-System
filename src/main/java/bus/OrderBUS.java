/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dal.OrderDAL;
import model.OrderEntity;
import model.TableEntity;
import gui.FormLoad;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import util.SortOrderByReservaionTimeUtil;

/**
 *
 * @author hoang
 */
public class OrderBUS implements BaseBUS<OrderEntity, String> {

    private OrderDAL orderDAL;

    public OrderBUS(EntityManager em) {
        orderDAL = new OrderDAL(em);
    }

    @Override
    public boolean insertEntity(OrderEntity orderEntity) {
        return orderDAL.insert(orderEntity);
    }

    @Override
    public boolean updateEntity(OrderEntity orderEntity) {
        return orderDAL.update(orderEntity);
    }

    @Override
    public boolean deleteEntity(String id) {
        return orderDAL.deleteById(id);
    }

    @Override
    public OrderEntity getEntityById(String id) {
        return orderDAL.findById(id);
    }

    @Override
    public List<OrderEntity> getAllEntities() {
        List<OrderEntity> orders = orderDAL.findAll();
        SortOrderByReservaionTimeUtil.sortAscByFloorAndTableId(orders);
        return orders;
    }

    public List<OrderEntity> getListOfReservations(LocalDate date, LocalTime time, String orderType) {
        List<OrderEntity> reservations = orderDAL.getReservationsByDateTime(date, time, orderType);
        SortOrderByReservaionTimeUtil.sortAscByFloorAndTableId(reservations);
        return reservations;
    }

    public List<OrderEntity> getReservationsByOption(String option) {
        List<OrderEntity> reservations = orderDAL.getReservationsByOption(option);
        SortOrderByReservaionTimeUtil.sortAscByFloorAndTableId(reservations);
        return reservations;
    }

    public List<OrderEntity> getCurrentOrdersAndReservations(LocalDateTime time, int option) {
        List<OrderEntity> reservations = orderDAL.getCurrentOrdersAndReservations(time, option);
        SortOrderByReservaionTimeUtil.sortAscByFloorAndTableId(reservations);
        return reservations;
    }

    public OrderEntity findByTableId(String tableId) {
        OrderEntity od = orderDAL.findByTableId(tableId).orElse(null);
        if (od == null) {
            return od;
        }
//        od.setCombinedTables(od.getCombinedTables().stream().map(x -> FormLoad.tableBUS.getEntityById(x.getTableId())).collect(Collectors.toList()));

        return od;
    }

    public double getTotalRevenue(LocalDateTime startedDate, LocalDateTime endedDate) {
        return orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .mapToDouble(x -> x.getTotalPaid() + x.getDeposit())
                .sum();
    }

    public double getTotalCapital(LocalDateTime startedDate, LocalDateTime endedDate) {
        return orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .flatMap(o -> o.getOrderDetails().stream())
                .mapToDouble(od -> (od.getItem().getCostPrice() + od.getTopping().getCostPrice()) * od.getQuantity())
                .sum();
    }
    
    public int getTotalCustomers(LocalDateTime startedDate, LocalDateTime endedDate) {
        return orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .mapToInt(x -> x.getNumberOfCustomer())
                .sum();
    }
    
    public long getTotalOrder(LocalDateTime startedDate, LocalDateTime endedDate) {
        return orderDAL.getOrdersbyOrderdate(startedDate, endedDate)
                .stream()
                .count();
    }
    public double getRevenueByYear(int year) {
        return orderDAL.getOrdersByYear(year).stream()
                .mapToDouble(x -> x.getTotalPaid() + x.getDeposit())
                .sum();
    }

    public double getCapitalByYear(int year) {
        return orderDAL.getOrdersByYear(year).stream()
                .flatMap(o -> o.getOrderDetails().stream())
                .mapToDouble(od -> (od.getItem().getCostPrice() + od.getTopping().getCostPrice())*od.getQuantity())
                .sum();
    }

    public Map<String, Map<Double, Double>> getRevenueStatsByDateOrYear(LocalDateTime startedDate, LocalDateTime endedDate, Integer year) {
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

    public List<OrderEntity> getListOrder(LocalDateTime startDateTime, LocalDateTime endDateTime, String staff, String rank) {
        return orderDAL.getListOrder(startDateTime, endDateTime, staff, rank);
    }

    public OrderEntity getMergedOrderByCombineTable(TableEntity t) {
        List<OrderEntity> mergedOrders = orderDAL.getListMergedOrder();
        return mergedOrders.stream()
                .filter(o -> o.getCombinedTables().contains(t) && o.getReservationTime().toLocalDate().equals(LocalDate.now())).findFirst().orElse(null);
    }

    public Map<String, Integer> getTotalFrequencyByHours(LocalDateTime startDateTime, LocalDateTime endDateTime, String option) {
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
        if (!option.equals("ALL")) {
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
    
    public Map<String, Double> getTotalRevenueByHours(LocalDateTime startDateTime, LocalDateTime endDateTime) {
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
    
    public int getQtyFrequencyByDate(LocalDateTime startDateTime, LocalDateTime endDateTime, String option) {
        if (!option.equals("ALL")) {
            return (int) orderDAL.getOrdersbyDateAndOption(startDateTime, endDateTime, option).stream()
                    .filter(x -> x.getOrderType().toString().equals(option))
                    .count();
        }
        return orderDAL.getOrdersbyDateAndOption(startDateTime, endDateTime, option).size();
    }

    public double getRevenueOfFrequencyByDate(LocalDateTime startDateTime, LocalDateTime endDateTime, String option) {
        if (!option.equals("ALL")) {
            return orderDAL.getOrdersbyDateAndOption(startDateTime, endDateTime, option).stream()
                    .filter(x -> x.getOrderType().toString().equals(option))
                    .mapToDouble(x -> x.getTotalPaid() + x.getDeposit())
                    .sum();
        }
        return orderDAL.getOrdersbyDateAndOption(startDateTime, endDateTime, option).stream()
                .mapToDouble(x -> x.getTotalPaid() + x.getDeposit())
                .sum();
    }

    public Map<String, Map<Integer, Integer>> getFrequencyPromotionStatsbyDatetime(LocalDateTime startedDate, LocalDateTime endedDate) {

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

        for (String date : mapItem.keySet()) {
            if (!result.containsKey(date)) {
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

    public double getTotalDiscount(LocalDateTime startedDate, LocalDateTime endedDate) {

        return orderDAL.getOrdersbyOrderdate(startedDate, endedDate)
                .stream()
                .filter(x -> x.getTotalDiscount() > 0)
                .mapToDouble(o -> o.getTotalDiscount())
                .sum();
    }
}
