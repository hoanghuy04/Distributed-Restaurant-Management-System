package bus.impl;

import bus.BaseBUS;
import dal.ItemDAL;
import dal.OrderDAL;
import model.CategoryEntity;
import model.ItemEntity;
import model.PromotionDetailEntity;
import jakarta.persistence.EntityManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemBUSImpl extends UnicastRemoteObject implements bus.ItemBUS {

    private ItemDAL itemDAL;
    private OrderDAL orderDAL;

    public ItemBUSImpl(EntityManager entityManager)  throws RemoteException {
        this.itemDAL = new ItemDAL(entityManager);
        this.orderDAL = new OrderDAL(entityManager);
    }

    @Override
    public boolean insertEntity(ItemEntity item)  throws RemoteException {
        return itemDAL.insert(item);
    }

    @Override
    public boolean updateEntity(ItemEntity item)  throws RemoteException {
        return itemDAL.update(item);
    }

    @Override
    public boolean deleteEntity(String id)  throws RemoteException {
        return itemDAL.deleteById(id);
    }

    @Override
    public ItemEntity getEntityById(String id)  throws RemoteException {
        ItemEntity item = itemDAL.findById(id);
        return item == null ? null : item;
    }

    @Override
    public List<ItemEntity> getAllEntities()  throws RemoteException {
        return itemDAL.findAll();
    }

    @Override
    public List<ItemEntity> findByCategoryName(String name)  throws RemoteException {
        return itemDAL.findByCategoryName(name);
    }

    @Override
    public List<ItemEntity> findByName(String nameItem, String nameCategory)  throws RemoteException {
        return itemDAL.findByName(nameItem, nameCategory);
    }

    @Override
    public ItemEntity findOneByName(String nameItem, String nameCategory)  throws RemoteException {
        return itemDAL.findOneByName(nameItem, nameCategory);
    }

    @Override
    public double getTopDiscountPercentage(PromotionDetailEntity pd)  throws RemoteException {
        return itemDAL.getTopDiscountPercentage(pd);
    }

    @Override
    public Map<String, Double> getTop5ItemHaveBestRevenue(LocalDateTime startedDate, LocalDateTime endedDate, String nameItem)  throws RemoteException {
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

    @Override
    public Map<String, Double> getRevenueOfAllItems(LocalDateTime startedDate, LocalDateTime endedDate, List<String> nameItems)  throws RemoteException {

        Map<String, Double> mapItemRevenue = orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .flatMap(o -> o.getOrderDetails().stream())
                .filter(od -> nameItems.contains(od.getItem().getCategory().getName()))
                .collect(Collectors.groupingBy(od -> {
                    String categoryName = od.getItem().getCategory().getName();
                    if (categoryName.equals("Salad") || categoryName.equals("Khai vị"))  {
                        return "Khai vị & Salad";
                    }
                    if (categoryName.matches(".*Pizza.*")) {
                        return "Pizza";
                    }
                    return categoryName;
                }, Collectors.summingDouble(od -> (od.getLineTotal()) - od.getDiscount())));
        return mapItemRevenue;
    }

    @Override
    public int getQtyByCategory(LocalDateTime startedDate, LocalDateTime endedDate, String name)  throws RemoteException {
        return orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .flatMap(o -> o.getOrderDetails().stream())
                .filter(x -> x.getItem().getCategory().getName().matches(".*" + name + ".*"))
                .collect(Collectors.summingInt(od -> od.getQuantity()));
    }

    @Override
    public double getTotalRevenueByCategory(LocalDateTime startedDate, LocalDateTime endedDate, String name)  throws RemoteException {
        return orderDAL.getOrdersbyOrderdate(startedDate, endedDate).stream()
                .flatMap(o -> o.getOrderDetails().stream())
                .filter(od -> od.getItem().getCategory().getName().matches(".*" + name + ".*"))
                .collect(Collectors.summingDouble(od -> (od.getLineTotal()) - od.getDiscount()));
    }

    @Override
    public Map<String, Integer> getTop5ItemHaveBestQuantity(LocalDateTime startedDate, LocalDateTime endedDate, String nameItem)  throws RemoteException {
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
    
    @Override
    public Map<String, Integer> getTop5ItemHaveBestQuantityForAllCategories(LocalDateTime startedDate, LocalDateTime endedDate)  throws RemoteException {
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
    
    @Override
    public List<ItemEntity> getItemsWithKeyword(String name, String categoryId, Double costPrice, Integer stockQty, boolean active)  throws RemoteException {
        return itemDAL.getItemsWithKeyword(name, categoryId, costPrice, stockQty, active);
    }

    @Override
    public List<ItemEntity> getFilteredItems(CategoryEntity category, String orderBy)  throws RemoteException {
        List<ItemEntity> items = itemDAL.getFilteredItems();
        return items.stream()
                .filter(x -> x.getCategory().getCategoryId().equals(category.getCategoryId()))
                .sorted((it1, it2) -> {
                    switch (orderBy)   {
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
