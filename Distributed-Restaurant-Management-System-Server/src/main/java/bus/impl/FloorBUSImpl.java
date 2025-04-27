package bus.impl;

import bus.BaseBUS;
import dal.FloorDAL;
import model.FloorEntity;
import jakarta.persistence.EntityManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class FloorBUSImpl extends UnicastRemoteObject implements bus.FloorBUS {
    private FloorDAL floorDAL;

    public FloorBUSImpl(EntityManager entityManager)  throws RemoteException {
        this.floorDAL = new FloorDAL(entityManager);
    }

    @Override
    public FloorEntity insertEntity(FloorEntity floor)  throws RemoteException {
        return floorDAL.insert(floor);
    }

    @Override
    public boolean updateEntity(FloorEntity floor)  throws RemoteException {
        return floorDAL.update(floor);
    }

    @Override
    public boolean deleteEntity(String id)  throws RemoteException {
        return floorDAL.deleteById(id);
    }

    @Override
    public FloorEntity getEntityById(String id)  throws RemoteException {
        return floorDAL.findById(id);
    }
    @Override
    public List<FloorEntity> getAllEntities()  throws RemoteException {
        return floorDAL.findAll();
    }
     @Override
     public FloorEntity findByName(String name)  throws RemoteException {
         return floorDAL.findByName(name);
     }
     
    @Override
    public List<FloorEntity> getTablesWithKeyword(Integer capacity, String name)  throws RemoteException {
        return floorDAL.getTablesWithKeyword(capacity, name);
    }

    @Override
    public List<FloorEntity> findByCriteria(FloorEntity criteria) throws RemoteException {
        return floorDAL.findByCriteria(criteria);
    }
}
