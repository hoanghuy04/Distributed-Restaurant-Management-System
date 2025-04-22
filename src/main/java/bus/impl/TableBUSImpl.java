package bus.impl;

import bus.BaseBUS;
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
    private OrderBUSImpl orderBUSImpl;

    public TableBUSImpl(EntityManager entityManager)  throws RemoteException {
        this.tableDAL = new TableDAL(entityManager);
        this.orderBUSImpl = new OrderBUSImpl(entityManager);
    }

    @Override
    public boolean insertEntity(TableEntity table)  throws RemoteException {
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
    public List<TableEntity> getListOfAvailableTables(String floorId, LocalDateTime reservationDateTime, int option)  throws RemoteException {
        List<TableEntity> tables = tableDAL.getListOfAvailableTables(floorId, reservationDateTime);
        List<OrderEntity> orders = orderBUSImpl.getCurrentOrdersAndReservations(reservationDateTime, option);

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
    public List<TableEntity> getListTablesByStatus(String floorId, String status)  throws RemoteException {
        List<TableEntity> list = tableDAL.findAll().stream().filter(x -> x.getFloor().getFloorId().equals(floorId)).collect(Collectors.toList());
        List<OrderEntity> orders = orderBUSImpl.getListOfReservations(LocalDate.now(), LocalTime.now(), "ADVANCE");
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
}
