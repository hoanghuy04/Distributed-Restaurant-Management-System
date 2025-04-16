package bus;

import dal.ItemDAL;
import dal.OrderDAL;
import model.CategoryEntity;
import model.ItemEntity;
import model.PromotionDetailEntity;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemBUS implements BaseBUS<ItemEntity, String> {

    private ItemDAL itemDAL;
    private OrderDAL orderDAL;

    public ItemBUS(EntityManager entityManager) {
        this.itemDAL = new ItemDAL(entityManager);
        this.orderDAL = new OrderDAL(entityManager);
    }

    @Override
    public boolean insertEntity(ItemEntity item) {
        return itemDAL.insert(item);
    }

    @Override
    public boolean updateEntity(ItemEntity item) {
        return itemDAL.update(item);
    }

    @Override
    public boolean deleteEntity(String id) {
        return itemDAL.deleteById(id);
    }

    @Override
    public ItemEntity getEntityById(String id) {
        Optional<ItemEntity> optionalItem = itemDAL.findById(id);
        return optionalItem.orElse(null); // Return null if not found
    }

    @Override
    public List<ItemEntity> getAllEntities() {
        return itemDAL.findAll();
    }

    public List<ItemEntity> findByCategoryName(String name) {
        return itemDAL.findByCategoryName(name);
    }

    public List<ItemEntity> findByName(String nameItem, String nameCategory) {
        return itemDAL.findByName(nameItem, nameCategory);
    }

    public ItemEntity findOneByName(String nameItem, String nameCategory) {
        return itemDAL.findOneByName(nameItem, nameCategory);
    }

    public double getTopDiscountPercentage(PromotionDetailEntity pd) {
        return itemDAL.getTopDiscountPercentage(pd);
    }

    public Map<String, Double> getTop5ItemHaveBestRevenue(LocalDateTime startedDate, LocalDateTime endedDate, String nameItem) {
        Map<String, Double> mapItemRevenue = orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .flatMap(o -> o.getOrderDetails().stream())
                .filter(i -> i.getItem().getCategory().getName().equals(nameItem))
                .collect(Collectors.groupingBy(od -> od.getItem().getName(), Collectors.summingDouble(od -> od.getLineTotal()- od.getDiscount())));

        return mapItemRevenue.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Map<String, Double> getRevenueOfAllItems(LocalDateTime startedDate, LocalDateTime endedDate, List<String> nameItems) {

        Map<String, Double> mapItemRevenue = orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .flatMap(o -> o.getOrderDetails().stream())
                .filter(od -> nameItems.contains(od.getItem().getCategory().getName()))
                .collect(Collectors.groupingBy(od -> {
                    String categoryName = od.getItem().getCategory().getName();
                    if (categoryName.equals("Salad") || categoryName.equals("Khai vị")) {
                        return "Khai vị & Salad";
                    }
                    if (categoryName.matches(".*Pizza.*")) {
                        return "Pizza";
                    }
                    return categoryName;
                }, Collectors.summingDouble(od -> (od.getLineTotal()) - od.getDiscount())));
        return mapItemRevenue;
    }

    public int getQtyByCategory(LocalDateTime startedDate, LocalDateTime endedDate, String name) {
        return orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .flatMap(o -> o.getOrderDetails().stream())
                .filter(x -> x.getItem().getCategory().getName().matches(".*" + name + ".*"))
                .collect(Collectors.summingInt(od -> od.getQuantity()));
    }

    public double getTotalRevenueByCategory(LocalDateTime startedDate, LocalDateTime endedDate, String name) {
        return orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .flatMap(o -> o.getOrderDetails().stream())
                .filter(od -> od.getItem().getCategory().getName().matches(".*" + name + ".*"))
                .collect(Collectors.summingDouble(od -> (od.getLineTotal()) - od.getDiscount()));
    }

    public Map<String, Integer> getTop5ItemHaveBestQuantity(LocalDateTime startedDate, LocalDateTime endedDate, String nameItem) {
        Map<String, Integer> mapItemQuantity = orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .flatMap(o -> o.getOrderDetails().stream())
                .filter(i -> i.getItem().getCategory().getName().equals(nameItem))
                .collect(Collectors.groupingBy(
                        od -> od.getItem().getName(),
                        Collectors.summingInt(od -> od.getQuantity())
                ));

        return mapItemQuantity.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
    
    public Map<String, Integer> getTop5ItemHaveBestQuantityForAllCategories(LocalDateTime startedDate, LocalDateTime endedDate) {
        Map<String, Integer> mapItemQuantity = orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .flatMap(o -> o.getOrderDetails().stream())
                .collect(Collectors.groupingBy(
                        od -> od.getItem().getName(),
                        Collectors.summingInt(od -> od.getQuantity())
                ));

        return mapItemQuantity.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
    
    public List<ItemEntity> getItemsWithKeyword(String name, String categoryId, Double costPrice, Integer stockQty, boolean active) {
        return itemDAL.getItemsWithKeyword(name, categoryId, costPrice, stockQty, active);
    }

    public List<ItemEntity> getFilteredItems(CategoryEntity category, String orderBy) {
        List<ItemEntity> items = itemDAL.getFilteredItems();
        return items.stream()
                .filter(x -> x.getCategory().getCategoryId().equals(category.getCategoryId()))
                .sorted((it1, it2) -> {
                    switch (orderBy) {
                        case "asc":
                            return Double.compare(it1.getSellingPrice() * (1 - it1.getTopDiscountPercentage()), it2.getSellingPrice() * (1 - it2.getTopDiscountPercentage()));
                        case "desc":
                            return Double.compare(it2.getSellingPrice() * (1 - it2.getTopDiscountPercentage()), it1.getSellingPrice() * (1 - it1.getTopDiscountPercentage()));
                        case "promotion":
                            return Double.compare(
                                    it2.getTopDiscountPercentage(),
                                    it1.getTopDiscountPercentage()
                            );
                        default:
                            return 0;
                    }
                })
                .collect(Collectors.toList());
    }
}
