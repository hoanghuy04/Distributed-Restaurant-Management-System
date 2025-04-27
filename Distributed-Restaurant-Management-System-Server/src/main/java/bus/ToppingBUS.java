package bus;

import dto.ToppingDTO;
import model.ToppingEntity;

import java.rmi.RemoteException;
import java.util.List;

public interface ToppingBUS extends BaseBUS<ToppingEntity, String> {
    @Override
    ToppingEntity insertEntity(ToppingEntity topping) throws RemoteException;

    @Override
    boolean updateEntity(ToppingEntity topping) throws RemoteException;

    @Override
    boolean deleteEntity(String id) throws RemoteException;

    @Override
    ToppingEntity getEntityById(String id) throws RemoteException;

    @Override
    List<ToppingEntity> getAllEntities() throws RemoteException;

    boolean addTopping(ToppingDTO toppingDTO) throws RemoteException;

    void updateTopping(ToppingDTO toppingDTO, String id) throws RemoteException;

    List<ToppingEntity> findTopping(ToppingDTO toppingDTO) throws RemoteException;

    ToppingEntity findByName(String name ) throws  RemoteException;
}
