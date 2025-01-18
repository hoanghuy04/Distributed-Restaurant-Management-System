package dal;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import model.CategoryEntity;
import util.IDGeneratorUtil;

import java.util.List;
import java.util.Optional;

/*
 * @description: CategoryDAL
 * @author: Trần Ngọc Huyền
 * @date: 1/18/2025
 * @version: 1.0
 */
@AllArgsConstructor
public class CategoryDAL implements BaseDAL<CategoryEntity, String>{
    private EntityManager em;

    @Override
    public boolean insert(CategoryEntity categoryEntity) {
        categoryEntity.setCategoryId(IDGeneratorUtil.generateSimpleID("C", "categories", "category_id", em));
        return BaseDAL.executeTransaction(em, () -> em.persist(categoryEntity));
    }

    @Override
    public boolean update(CategoryEntity categoryEntity) {
        return BaseDAL.executeTransaction(em, () -> em.merge(categoryEntity));
    }

    @Override
    public boolean deleteById(String s) {
        return BaseDAL.executeTransaction(em, () -> {
            CategoryEntity categoryEntity = em.find(CategoryEntity.class, s);
            if (categoryEntity != null) {
                em.remove(categoryEntity);
            }
        });
    }

    @Override
    public Optional<CategoryEntity> findById(String s) {
        return Optional.ofNullable(em.find(CategoryEntity.class, s));
    }

    @Override
    public List<CategoryEntity> findAll() {
        return em.createNamedQuery("CategoryEntity.findAll", CategoryEntity.class).getResultList();
    }
}
