package bus;

import model.FloorEntity;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface FloorBUS extends BaseBUS<FloorEntity, String>, Remote {
    @Override
    FloorEntity insertEntity(FloorEntity floor) throws RemoteException;

    @Override
    FloorEntity updateEntity(FloorEntity floor) throws RemoteException;

    @Override
    boolean deleteEntity(String id) throws RemoteException;

    @Override
    FloorEntity getEntityById(String id) throws RemoteException;

    @Override
    List<FloorEntity> getAllEntities() throws RemoteException;

    FloorEntity findByName(String name) throws RemoteException;

    List<FloorEntity> getTablesWithKeyword(Integer capacity, String name) throws RemoteException;

    List<FloorEntity> findByCriteria(FloorEntity criteria) throws RemoteException;
}
