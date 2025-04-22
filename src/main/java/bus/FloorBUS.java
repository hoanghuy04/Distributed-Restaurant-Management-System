package bus;

import dal.FloorDAL;
import model.FloorEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class FloorBUS implements BaseBUS<FloorEntity, String> {
    private FloorDAL floorDAL;

    public FloorBUS(EntityManager entityManager) {
        this.floorDAL = new FloorDAL(entityManager);
    }

    @Override
    public boolean insertEntity(FloorEntity floor) {
        return floorDAL.insert(floor);
    }

    @Override
    public boolean updateEntity(FloorEntity floor) {
        return floorDAL.update(floor);
    }

    @Override
    public boolean deleteEntity(String id) {
        return floorDAL.deleteById(id);
    }

    @Override
    public FloorEntity getEntityById(String id) {
        return floorDAL.findById(id);
    }
    @Override
    public List<FloorEntity> getAllEntities() {
        return floorDAL.findAll();
    }
     public FloorEntity findByName(String name) {
         return floorDAL.findByName(name);
     }
     
    public List<FloorEntity> getTablesWithKeyword(Integer capacity, String name) {
        return floorDAL.getTablesWithKeyword(capacity, name);
    }        
}
