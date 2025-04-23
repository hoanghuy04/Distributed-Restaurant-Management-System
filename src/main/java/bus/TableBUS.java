package bus;

import model.TableEntity;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;

public interface TableBUS extends BaseBUS<TableEntity, String> {
    @Override
    boolean insertEntity(TableEntity table) throws RemoteException;

    @Override
    boolean updateEntity(TableEntity table) throws RemoteException;

    @Override
    boolean deleteEntity(String id) throws RemoteException;

    @Override
    TableEntity getEntityById(String id) throws RemoteException;

    @Override
    List<TableEntity> getAllEntities() throws RemoteException;

    List<TableEntity> getListOfAvailableTables(String floorId, LocalDateTime reservationDateTime, int option) throws RemoteException;

    List<TableEntity> getListTablesByStatus(String floorId, String status) throws RemoteException;

    TableEntity findByName(String name, String floorId) throws RemoteException;

    List<TableEntity> getTablesWithKeyword(String floorId, Integer capacity, String tableName) throws RemoteException;
}
