package bus;

import dal.TableDAL;
import model.OrderEntity;
import model.TableEntity;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import model.enums.PaymentStatusEnum;
import model.enums.TableStatusEnum;

public class TableBUS implements BaseBUS<TableEntity, String> {

    private TableDAL tableDAL;
    private OrderBUS orderBUS;

    public TableBUS(EntityManager entityManager) {
        this.tableDAL = new TableDAL(entityManager);
        this.orderBUS = new OrderBUS(entityManager);
    }

    @Override
    public boolean insertEntity(TableEntity table) {
        return tableDAL.insert(table);
    }

    @Override
    public boolean updateEntity(TableEntity table) {
        return tableDAL.update(table);
    }

    @Override
    public boolean deleteEntity(String id) {
        return tableDAL.deleteById(id);
    }

    @Override
    public TableEntity getEntityById(String id) {
       return tableDAL.findById(id);
    }

    @Override
    public List<TableEntity> getAllEntities() {
        return tableDAL.findAll();
    }

    public List<TableEntity> getListOfAvailableTables(String floorId, LocalDateTime reservationDateTime, int option) {
        List<TableEntity> tables = tableDAL.getListOfAvailableTables(floorId, reservationDateTime);
        List<OrderEntity> orders = orderBUS.getCurrentOrdersAndReservations(reservationDateTime, option);

        //Nếu trong tables có tableEntity nào nằm trong orderEntity.getCombinedTables() thì xóa tableEntity khỏi tables
        for (OrderEntity o : orders) {
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

    public List<TableEntity> getListTablesByStatus(String floorId, String status) {
        List<TableEntity> list = tableDAL.findAll().stream().filter(x -> x.getFloor().getFloorId().equals(floorId)).collect(Collectors.toList());
        List<OrderEntity> orders = orderBUS.getListOfReservations(LocalDate.now(), LocalTime.now(), "ADVANCE");
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

    public TableEntity findByName(String name, String floorId) {
        return tableDAL.findByName(name, floorId);
    }
    
    public List<TableEntity> getTablesWithKeyword(String floorId, Integer capacity, String tableName) {
        return tableDAL.getTablesWithKeyword(floorId, capacity, tableName);
    }    
}
