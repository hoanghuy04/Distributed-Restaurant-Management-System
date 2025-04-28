package bus;

import model.RoleEntity;
import model.TableEntity;
import model.enums.TableStatusEnum;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;

public interface TableBUS  extends BaseBUS<TableEntity, String> {
    boolean lockTable(String tableId) throws RemoteException;

    void unlockTable(String tableId, TableStatusEnum finalStatus) throws RemoteException;

    TableEntity insertEntity(TableEntity table) throws RemoteException;

    TableEntity updateEntity(TableEntity table) throws RemoteException;

    boolean deleteEntity(String id) throws RemoteException;

    TableEntity getEntityById(String id) throws RemoteException;

    List<TableEntity> getAllEntities() throws RemoteException;

    List<TableEntity> getListOfAvailableTables(String floorId, LocalDateTime reservationDateTime, int option) throws Exception;

    List<TableEntity> getListTablesByStatus(String floorId, String status) throws Exception;

    TableEntity findByName(String name, String floorId) throws RemoteException;

    List<TableEntity> getTablesWithKeyword(String floorId, Integer capacity, String tableName) throws RemoteException;

    List<TableEntity> findByCriteria(TableEntity criteria) throws RemoteException;
}
