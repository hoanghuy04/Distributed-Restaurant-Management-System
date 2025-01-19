package dal;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import model.ToppingEntity;
import util.IDGeneratorUtil;

import java.util.List;
import java.util.Optional;

/*
 * @description: ToppingDAL
 * @author: Trần Ngọc Huyền
 * @date: 1/18/2025
 * @version: 1.0
 */
@AllArgsConstructor
public class ToppingDAL implements BaseDAL<ToppingEntity, String> {
    private EntityManager em;

    @Override
    public boolean insert(ToppingEntity toppingEntity) {
        toppingEntity.setToppingId(IDGeneratorUtil.generateSimpleID("T", "toppings", "topping_id", em));
        return BaseDAL.executeTransaction(em, () -> em.persist(toppingEntity));
    }

    @Override
    public boolean update(ToppingEntity toppingEntity) {
        return BaseDAL.executeTransaction(em, () -> em.merge(toppingEntity));
    }

    @Override
    public boolean deleteById(String s) {
        return BaseDAL.executeTransaction(em, () -> {
            ToppingEntity toppingEntity = em.find(ToppingEntity.class, s);
            if (toppingEntity != null) {
                em.remove(toppingEntity);
            }
        });
    }

    @Override
    public Optional<ToppingEntity> findById(String s) {
        return Optional.ofNullable(em.find(ToppingEntity.class, s));
    }

    @Override
    public List<ToppingEntity> findAll() {
        return em.createNamedQuery("ToppingEntity.findAll", ToppingEntity.class).getResultList();
    }
}
