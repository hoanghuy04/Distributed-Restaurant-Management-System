package bus;

import model.ItemEntity;
import model.ItemToppingEntity;
import model.ItemToppingId;
import model.ToppingEntity;

import java.rmi.RemoteException;
import java.util.List;

public interface ItemToppingBUS extends BaseBUS<ItemToppingEntity, ItemToppingId> {
    @Override
    ItemToppingEntity insertEntity(ItemToppingEntity itemTopping) throws RemoteException;

    @Override
    boolean updateEntity(ItemToppingEntity itemTopping) throws RemoteException;

    @Override
    boolean deleteEntity(ItemToppingId id) throws RemoteException;

    @Override
    ItemToppingEntity getEntityById(ItemToppingId id) throws RemoteException;

    @Override
    List<ItemToppingEntity> getAllEntities() throws RemoteException;

    ItemToppingEntity findByItemAndToppingId(ItemEntity item, ToppingEntity topping) throws RemoteException;
}
