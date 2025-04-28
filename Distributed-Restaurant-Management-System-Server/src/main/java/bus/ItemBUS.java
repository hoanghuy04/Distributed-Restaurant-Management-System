package bus;

import model.CategoryEntity;
import model.ItemEntity;
import model.PromotionDetailEntity;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ItemBUS extends BaseBUS<ItemEntity, String> {
    @Override
    ItemEntity insertEntity(ItemEntity item) throws RemoteException;

    @Override
    ItemEntity updateEntity(ItemEntity item) throws RemoteException;

    @Override
    boolean deleteEntity(String id) throws RemoteException;

    @Override
    ItemEntity getEntityById(String id) throws RemoteException;

    @Override
    List<ItemEntity> getAllEntities() throws RemoteException;

    List<ItemEntity> findByCategoryName(String name) throws RemoteException;

    List<ItemEntity> findByName(String nameItem, String nameCategory) throws RemoteException;

    ItemEntity findOneByName(String nameItem, String nameCategory) throws RemoteException;

    double getTopDiscountPercentage(PromotionDetailEntity pd) throws RemoteException;

    Map<String, Double> getTop5ItemHaveBestRevenue(LocalDateTime startedDate, LocalDateTime endedDate, String nameItem) throws RemoteException;

    Map<String, Double> getRevenueOfAllItems(LocalDateTime startedDate, LocalDateTime endedDate, List<String> nameItems) throws RemoteException;

    int getQtyByCategory(LocalDateTime startedDate, LocalDateTime endedDate, String name) throws RemoteException;

    double getTotalRevenueByCategory(LocalDateTime startedDate, LocalDateTime endedDate, String name) throws RemoteException;

    Map<String, Integer> getTop5ItemHaveBestQuantity(LocalDateTime startedDate, LocalDateTime endedDate, String nameItem) throws RemoteException;

    Map<String, Integer> getTop5ItemHaveBestQuantityForAllCategories(LocalDateTime startedDate, LocalDateTime endedDate) throws RemoteException;

    List<ItemEntity> getItemsWithKeyword(String name, String categoryId, Double costPrice, Integer stockQty, boolean active)  throws RemoteException;

    List<ItemEntity> getFilteredItems(CategoryEntity category, String orderBy) throws RemoteException;
}
