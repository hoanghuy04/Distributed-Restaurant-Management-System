package bus.impl;

import bus.OrderBUS;
import bus.TableBUS;
import dal.TableDAL;
import model.OrderEntity;
import model.TableEntity;
import jakarta.persistence.EntityManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import model.enums.PaymentStatusEnum;
import model.enums.TableStatusEnum;

public class TableBUSImpl extends UnicastRemoteObject implements bus.TableBUS {

    private TableDAL tableDAL;
    private OrderBUS orderBUS;
    private final EntityManager em;

    public TableBUSImpl(EntityManager entityManager)  throws RemoteException {
        this.em = entityManager;
        this.tableDAL = new TableDAL(entityManager);
    }

    // Lazy initialization của orderBUS
    private OrderBUS getOrderBUS() throws Exception {
        if (orderBUS == null) {
            orderBUS = new OrderBUSImpl(em);
        }
        return orderBUS;
    }

    @Override
    public synchronized boolean lockTable(String tableId) throws RemoteException {
        try {
            TableEntity table = getEntityById(tableId); // Giả sử có phương thức này
            if (table == null) {
                throw new RemoteException("Bàn không tồn tại");
            }
            if (table.getTableStatus() == TableStatusEnum.PROCESSING) {
                return false; // Bàn đang được xử lý
            }
            // Đánh dấu bàn là PROCESSING
            table.setTableStatus(TableStatusEnum.PROCESSING);
            updateEntity(table);
            return true;
        } catch (Exception e) {
            throw new RemoteException("Lỗi khi khóa bàn: " + e.getMessage());
        }
    }

    @Override
    public synchronized void unlockTable(String tableId, TableStatusEnum finalStatus) throws RemoteException {
        try {
            TableEntity table = getEntityById(tableId);
            if (table != null) {
                table.setTableStatus(finalStatus);
                updateEntity(table);
            }
        } catch (Exception e) {
            throw new RemoteException("Lỗi khi mở khóa bàn: " + e.getMessage());
        }
    }

    @Override
    public TableEntity insertEntity(TableEntity table)  throws RemoteException {
        return tableDAL.insert(table);
    }

    @Override
    public boolean updateEntity(TableEntity table)  throws RemoteException {
        return tableDAL.update(table);
    }

    @Override
    public boolean deleteEntity(String id)  throws RemoteException {
        return tableDAL.deleteById(id);
    }

    @Override
    public TableEntity getEntityById(String id)  throws RemoteException {
        return tableDAL.findById(id);
    }

    @Override
    public List<TableEntity> getAllEntities()  throws RemoteException {
        return tableDAL.findAll();
    }

    @Override
    public List<TableEntity> getListOfAvailableTables(String floorId, LocalDateTime reservationDateTime, int option) throws Exception {
        List<TableEntity> tables = tableDAL.getListOfAvailableTables(floorId, reservationDateTime);
        List<OrderEntity> orders = getOrderBUS().getCurrentOrdersAndReservations(reservationDateTime, option);

        //Nếu trong tables có tableEntity nào nằm trong orderEntity.getCombinedTables() thì xóa tableEntity khỏi tables
        for (OrderEntity o : orders)   {
            List<TableEntity> combinedTables = o.getCombinedTables();
            tables.removeIf(combinedTables::contains);
        }

        if (LocalDate.now().isEqual(reservationDateTime.toLocalDate())) {
            for (OrderEntity o : orders) {
                if (tables.contains(o.getTable()) && o.getPaymentStatus().equals(PaymentStatusEnum.UNPAID)) {
                    tables.remove(o.getTable());
                }
            }
        }

        return tables;
    }

    @Override
    public List<TableEntity> getListTablesByStatus(String floorId, String status) throws Exception {
        List<TableEntity> list = tableDAL.findAll().stream().filter(x -> x.getFloor().getFloorId().equals(floorId)).collect(Collectors.toList());
        List<OrderEntity> orders = getOrderBUS().getListOfReservations(LocalDate.now(), LocalTime.now(), "ADVANCE");
        List<TableEntity> reservedList = orders.stream().map(x -> x.getTable()).collect(Collectors.toList());
        if (status.equals("Bàn trống")) {
            return list.stream()
                    .filter(x ->x.getTableStatus().equals(TableStatusEnum.AVAILABLE))
                    .collect(Collectors.toList())
                    .stream()
                    .filter(x -> !reservedList.contains(x))
                    .collect(Collectors.toList());
        } else if (status.equals("Phục vụ")) {
            return list.stream()
                    .filter(x -> x.getTableStatus().equals(TableStatusEnum.OCCUPIED))
                    .collect(Collectors.toList());
        } else if (status.equals("Đặt trước")) {
            return list.stream()
                    .filter(x -> reservedList.contains(x))
                    .collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public TableEntity findByName(String name, String floorId)  throws RemoteException {
        return tableDAL.findByName(name, floorId);
    }

    @Override
    public List<TableEntity> getTablesWithKeyword(String floorId, Integer capacity, String tableName)  throws RemoteException {
        return tableDAL.getTablesWithKeyword(floorId, capacity, tableName);
    }

    @Override
    public List<TableEntity> findByCriteria(TableEntity criteria) throws RemoteException {
        return tableDAL.findByCriteria(criteria);
    }
}
