package bus;

import bus.request.ClientCallback;
import model.OrderEntity;
import model.TableEntity;
import model.enums.PaymentStatusEnum;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface OrderBUS extends BaseBUS<OrderEntity, String>, Remote {
    @Override
    OrderEntity insertEntity(OrderEntity orderEntity) throws RemoteException;

    @Override
    boolean updateEntity(OrderEntity orderEntity) throws RemoteException;

    @Override
    boolean deleteEntity(String id) throws RemoteException;

    @Override
    OrderEntity getEntityById(String id) throws RemoteException;

    @Override
    List<OrderEntity> getAllEntities() throws RemoteException;

    List<OrderEntity> getListOfReservations(LocalDate date, LocalTime time, String orderType) throws RemoteException;

    List<OrderEntity> getReservationsByOption(String option) throws RemoteException;

    List<OrderEntity> getCurrentOrdersAndReservations(LocalDateTime time, int option) throws RemoteException;

    OrderEntity findByTableId(String tableId) throws RemoteException;

    double getTotalRevenue(LocalDateTime startedDate, LocalDateTime endedDate) throws RemoteException;

    double getTotalCapital(LocalDateTime startedDate, LocalDateTime endedDate) throws RemoteException;

    int getTotalCustomers(LocalDateTime startedDate, LocalDateTime endedDate) throws RemoteException;

    long getTotalOrder(LocalDateTime startedDate, LocalDateTime endedDate) throws RemoteException;

    double getRevenueByYear(int year) throws RemoteException;

    double getCapitalByYear(int year) throws RemoteException;

    Map<String, Map<Double, Double>> getRevenueStatsByDateOrYear(LocalDateTime startedDate, LocalDateTime endedDate, Integer year) throws RemoteException;

    List<OrderEntity> getListOrder(LocalDateTime startDateTime, LocalDateTime endDateTime, String staff, String rank) throws RemoteException;

    OrderEntity getMergedOrderByCombineTable(TableEntity t) throws RemoteException;

    Map<String, Integer> getTotalFrequencyByHours(LocalDateTime startDateTime, LocalDateTime endDateTime, String option) throws RemoteException;

    Map<String, Double> getTotalRevenueByHours(LocalDateTime startDateTime, LocalDateTime endDateTime) throws RemoteException;

    int getQtyFrequencyByDate(LocalDateTime startDateTime, LocalDateTime endDateTime, String option) throws RemoteException;

    double getRevenueOfFrequencyByDate(LocalDateTime startDateTime, LocalDateTime endDateTime, String option) throws RemoteException;

    Map<String, Map<Integer, Integer>> getFrequencyPromotionStatsbyDatetime(LocalDateTime startedDate, LocalDateTime endedDate) throws RemoteException;

    double getTotalDiscount(LocalDateTime startedDate, LocalDateTime endedDate) throws RemoteException;
    void queueOrderRequest(OrderEntity orderEntity, PaymentStatusEnum paymentStatus, ClientCallback callback) throws Exception;
}
