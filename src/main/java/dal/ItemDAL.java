package dal;

import model.CategoryEntity;
import model.ItemEntity;
import model.PromotionDetailEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public boolean insert(ItemEntity t) {
        t.setItemId(IDGeneratorUtility.generateIDWithCreatedDate("I", "items", "item_id", "created_date", em, LocalDateTime.now()));
        return executeTransaction(() -> em.persist(t));
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
    public Optional<ItemEntity> findById(String id) {
        return Optional.ofNullable(em.find(ItemEntity.class, id));
    }

    @Override
    public List<ItemEntity> findAll() {
        return em.createNamedQuery("ItemEntity.findAll", ItemEntity.class).getResultList();
    }

    public List<ItemEntity> findByCategory(CategoryEntity categoryEntity) {
        return em.createNamedQuery("ItemEntity.findByCategory", ItemEntity.class).setParameter("categoryId", categoryEntity.getCategoryId()).getResultList();
    }

    public Optional<ItemEntity> findByName(String name) {
        try {
            ItemEntity result = em.createNamedQuery("ItemEntity.findByName", ItemEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<ItemEntity> findByCategoryName(String name) {
        String sql = "select i.* from items i "
                + "join categories c on i.category_id = c.category_id "
                + "where lower(c.name) = lower(N'" + name + "')";
        Query q = em.createNativeQuery(sql, ItemEntity.class);
        return q.getResultList();
    }

    public List<ItemEntity> findByName(String nameItem, String nameCategory) {
        String sql = "  select i.* from items i "
                + "  join categories c on i.category_id = c.category_id "
                + "  where i.name like ?1"
                + "  and c.name = ?2 ";
        Query q = em.createNativeQuery(sql, ItemEntity.class);
        q.setParameter(1, "%" + nameItem + "%");
        q.setParameter(2, nameCategory);
        return q.getResultList();
    }

    public ItemEntity findOneByName(String nameItem, String nameCategory) {
        String sql = "  select i.* from items i "
                + "  join categories c on i.category_id = c.category_id "
                + "  where i.name = ?1"
                + "  and c.name = ?2 ";
        Query q = em.createNativeQuery(sql, ItemEntity.class);
        q.setParameter(1, nameItem);
        q.setParameter(2, nameCategory);
        return (ItemEntity) q.getSingleResult();
    }

    public double getTopDiscountPercentage(PromotionDetailEntity pd) {
        return pd.getPromotion().getDiscountPercentage();
    }

    public List<ItemEntity> getItemsWithKeyword(String name, String categoryId, Double costPrice, Integer stockQty, boolean active) {
        List<ItemEntity> items = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM items WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            queryBuilder.append(" AND name LIKE ?");
            parameters.add("%" + name + "%");
        }
        if (categoryId != null && !categoryId.trim().isEmpty()) {
            queryBuilder.append(" AND category_id = ?");
            parameters.add(categoryId);
        }
        if (costPrice != null) {
            queryBuilder.append(" AND cost_price = ?");
            parameters.add(costPrice);
        }
        if (stockQty != null) {
            queryBuilder.append(" AND stock_quantity = ?");
            parameters.add(stockQty);
        }
        queryBuilder.append(" AND active = ?");
        parameters.add(active);

        Query query = em.createNativeQuery(queryBuilder.toString(), ItemEntity.class);

        // Thiết lập các tham số cho truy vấn
        for (int i = 0; i < parameters.size(); i++) {
            query.setParameter(i + 1, parameters.get(i));
        }

        items = query.getResultList();

        return items;
    }

    public List<ItemEntity> getFilteredItems() {
        String sql = ("select i.* from items i "
                + "left join promotion_details pd on i.item_id = pd.item_id "
                + "left join promotions p on p.promotion_id = pd.promotion_id ");
        Query query = em.createNativeQuery(sql, ItemEntity.class);
        return query.getResultList();
    }
}
