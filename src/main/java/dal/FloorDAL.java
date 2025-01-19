package dal;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.FloorEntity;
import util.IDGeneratorUtil;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
public class FloorDAL implements BaseDAL<FloorEntity,String> {
    private EntityManager em;

    @Override
    public boolean insert(FloorEntity floorEntity) {
        floorEntity.setFloorId(IDGeneratorUtil.generateSimpleID("F","floors","floor_id",em));
        return BaseDAL.executeTransaction(em,()->em.persist(floorEntity));
    }

    @Override
    public boolean update(FloorEntity floorEntity) {
        return BaseDAL.executeTransaction(em,()->em.merge(floorEntity));
    }

    @Override
    public boolean deleteById(String s) {
        return false;
    }

    @Override
    public Optional<FloorEntity> findById(String s) {
        return Optional.ofNullable(em.find(FloorEntity.class, s));
    }

    @Override
    public List<FloorEntity> findAll() {
        return em.createNamedQuery("FloorEntity.findAll", FloorEntity.class).getResultList();
    }
}
