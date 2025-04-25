package dal;

import model.CategoryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import util.IDGeneratorUtility;

public class CategoryDAL implements BaseDAL<CategoryEntity, String> {

    private EntityManager entityManager;

    public CategoryDAL(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private EntityTransaction getTransaction() {
        return entityManager.getTransaction();
    }

    @Override
    public CategoryEntity insert(CategoryEntity category) {
        category.setCategoryId(IDGeneratorUtility.generateSimpleID("C", "categories", "category_id", entityManager));
        executeTransaction(() -> entityManager.persist(category));
        return category;
    }

    @Override
    public boolean update(CategoryEntity category) {
        return executeTransaction(() -> entityManager.merge(category));
    }

    @Override
    public boolean deleteById(String id) {
//        return executeTransaction(() -> {
//            CategoryEntity category = findById(id)
//                    .orElseThrow(() -> new RuntimeException("Category không tồn tại"));
//            entityManager.remove(category);
//        });
        return false;
    }

    @Override
    public CategoryEntity findById(String id) {
        return entityManager.find(CategoryEntity.class, id);
    }

    @Override
    public List<CategoryEntity> findAll() {
        return entityManager.createNamedQuery("CategoryEntity.findAll", CategoryEntity.class).getResultList();
    }

    public CategoryEntity findByName(String name) {
        try {
            return entityManager.createNamedQuery("CategoryEntity.findByName", CategoryEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private boolean executeTransaction(Runnable action) {
        EntityTransaction transaction = getTransaction();
        try {
            transaction.begin();
            action.run();
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        }
    }

}
