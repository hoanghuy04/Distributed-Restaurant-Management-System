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
    public boolean insert(CategoryEntity category) {
        category.setCategoryId(IDGeneratorUtility.generateSimpleID("C", "categories", "category_id", entityManager));
        return executeTransaction(() -> entityManager.persist(category));
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
    public Optional<CategoryEntity> findById(String id) {
        return Optional.ofNullable(entityManager.find(CategoryEntity.class, id));
    }

    @Override
    public List<CategoryEntity> findAll() {
        return entityManager.createNamedQuery("CategoryEntity.findAll", CategoryEntity.class).getResultList();
    }

    public Optional<CategoryEntity> findByName(String name) {
        try {
            CategoryEntity result = entityManager.createNamedQuery("CategoryEntity.findByName", CategoryEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
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

    public List<CategoryEntity> getTablesWithKeyword(String scrip, String name, boolean active) {
        List<CategoryEntity> tables = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM categories WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (scrip != null && !scrip.trim().isEmpty()) {
            queryBuilder.append(" AND description = ?");
            parameters.add(scrip);
        }

        if (name != null && !name.trim().isEmpty()) {
            queryBuilder.append(" AND name LIKE ?");
            parameters.add("%" + name + "%");
        }
        queryBuilder.append(" AND active = ?");
        parameters.add(active);

        Query query = entityManager.createNativeQuery(queryBuilder.toString(), CategoryEntity.class);

        for (int i = 0; i < parameters.size(); i++) {
            query.setParameter(i + 1, parameters.get(i));
        }

        tables = query.getResultList();

        return tables;
    }

}
