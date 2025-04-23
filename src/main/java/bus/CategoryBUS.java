package bus;

import model.CategoryEntity;

import java.rmi.RemoteException;
import java.util.List;

public interface CategoryBUS extends BaseBUS<CategoryEntity, String> {
    @Override
    boolean insertEntity(CategoryEntity category) throws RemoteException;

    @Override
    boolean updateEntity(CategoryEntity category) throws RemoteException;

    @Override
    boolean deleteEntity(String id) throws RemoteException;

    @Override
    CategoryEntity getEntityById(String id) throws RemoteException;

    @Override
    List<CategoryEntity> getAllEntities() throws RemoteException;

    CategoryEntity findByName(String name) throws RemoteException;
}
