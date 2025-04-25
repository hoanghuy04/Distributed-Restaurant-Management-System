package bus;

import model.RoleEntity;

import java.rmi.RemoteException;
import java.util.List;

public interface RoleBUS extends BaseBUS<RoleEntity, String> {
    @Override
    RoleEntity insertEntity(RoleEntity t) throws RemoteException;

    @Override
    boolean updateEntity(RoleEntity t) throws RemoteException;

    @Override
    boolean deleteEntity(String id) throws RemoteException;

    @Override
    RoleEntity getEntityById(String id) throws RemoteException;

    @Override
    List<RoleEntity> getAllEntities() throws RemoteException;

    RoleEntity findByName(String name) throws RemoteException;
}
