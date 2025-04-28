package dal;

import jakarta.persistence.*;
import model.CategoryEntity;
import model.ItemEntity;
import model.PromotionDetailEntity;

import java.time.LocalDateTime;
import java.util.*;

import util.IDGeneratorUtility;

public class ItemDAL implements BaseDAL<ItemEntity, String> {

    private EntityManager em;

    public ItemDAL(EntityManager em) {
        this.em = em;
    }

    private EntityTransaction getEntityTransaction() {
        return em.getTransaction();
    }

    private boolean executeTransaction(Runnable action) {
        EntityTransaction et = getEntityTransaction();

        try {
            et.begin();
            action.run();
            et.commit();
            return true;
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
        }
        return false;
    }

    @Override
    public ItemEntity insert(ItemEntity t) {
        t.setItemId(IDGeneratorUtility.generateIDWithCreatedDate("I", "items", "item_id", "created_date", em, LocalDateTime.now()));
        executeTransaction(() -> em.persist(t));

        return t;
    }

    @Override
    public boolean update(ItemEntity t) {
        return executeTransaction(() -> em.merge(t));
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public ItemEntity findById(String id) {
        return em.find(ItemEntity.class, id);
    }

    @Override
    public List<ItemEntity> findAll() {
        return em.createNamedQuery("ItemEntity.findAll", ItemEntity.class).getResultList();
    }

    public List<ItemEntity> findByCategory(CategoryEntity categoryEntity) {
        return em.createNamedQuery("ItemEntity.findByCategory", ItemEntity.class).setParameter("categoryId", categoryEntity.getCategoryId()).getResultList();
    }

    public ItemEntity findByName(String name) {
        try {
            return em.createNamedQuery("ItemEntity.findByName", ItemEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ItemEntity> findByCategoryName(String name) {
        String sql = "select i from ItemEntity i "
                + "join CategoryEntity c on i.category.id = c.categoryId "
                + "where lower(c.name) = lower(:name)";
        return  em.createQuery(sql, ItemEntity.class).setParameter("name", name).getResultList();
    }

    public List<ItemEntity> findByName(String nameItem, String nameCategory) {
        String sql = "  select i from ItemEntity i "
                + "  join CategoryEntity c on i.category.categoryId = c.categoryId "
                + "  where i.name like ?1"
                + "  and c.name = ?2 ";
        return em.createQuery(sql, ItemEntity.class)
                .setParameter(1, "%" + nameItem + "%")
                .setParameter(2, nameCategory)
                .getResultList();
    }

    public ItemEntity findOneByName(String nameItem, String nameCategory) {
        String sql = "  select i from ItemEntity i "
                + "  join CategoryEntity c on i.category.categoryId = c.categoryId "
                + "  where i.name = ?1"
                + "  and c.name = ?2 ";
        Query q = em.createQuery(sql, ItemEntity.class);
        q.setParameter(1, nameItem);
        q.setParameter(2, nameCategory);
        try {
            return  (ItemEntity) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public double getTopDiscountPercentage(PromotionDetailEntity pd) {
        return pd.getPromotion().getDiscountPercentage();
    }

    public List<ItemEntity> getItemsWithKeyword(String name, String categoryId, Double costPrice, Integer stockQty, boolean active) {
        StringBuilder jpql = new StringBuilder("select i from ItemEntity i where 1=1");
        Map<String, Object> parameters = new HashMap<>();

        if (name != null && !name.trim().isEmpty()) {
            jpql.append(" and i.name like :name");
            parameters.put("name", "%" + name + "%");
        }
        if (categoryId != null && !categoryId.trim().isEmpty()) {
            jpql.append(" and i.category.categoryId = :categoryId");
            parameters.put("categoryId", categoryId);

        }
        if (costPrice != null) {
            jpql.append(" and i.costPrice = :costPrice");
            parameters.put("costPrice", costPrice);
        }
        if (stockQty != null) {
            jpql.append(" and i.stockQuantity = :stockQuantity");
            parameters.put("stockQuantity", stockQty);
        }
        jpql.append(" and i.active = :active");
        parameters.put("active", active);

        TypedQuery<ItemEntity> query = em.createQuery(jpql.toString(), ItemEntity.class);

        for (Map.Entry<String, Object> param : parameters.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }

        return query.getResultList();
    }

    public List<ItemEntity> getFilteredItems() {
        String sql = ("select i from ItemEntity i "
                + "left join PromotionDetailEntity pd on i.itemId = pd.item.itemId "
                + "left join PromotionEntity p on p.promotionId = pd.promotion.promotionId ");
        return em.createQuery(sql, ItemEntity.class).getResultList();
    }
}
