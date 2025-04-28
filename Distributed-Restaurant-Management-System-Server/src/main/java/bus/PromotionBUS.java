package bus;

import model.CustomerEntity;
import model.PromotionEntity;
import model.enums.CustomerLevelEnum;
import model.enums.PromotionTypeEnum;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;

public interface PromotionBUS extends BaseBUS<PromotionEntity, String> {
    @Override
    PromotionEntity insertEntity(PromotionEntity promotion) throws RemoteException;

    @Override
    PromotionEntity updateEntity(PromotionEntity promotion) throws RemoteException;

    @Override
    boolean deleteEntity(String id) throws RemoteException;

    @Override
    PromotionEntity getEntityById(String id) throws RemoteException;

    @Override
    List<PromotionEntity> getAllEntities() throws RemoteException;

    default boolean isFirstOrderOfMonth(CustomerEntity customerEntity)  throws RemoteException{
        return customerEntity.getOrders().stream()
                .filter(o -> o.getReservationTime().getMonth() == LocalDateTime.now().getMonth())
                .count() == 1;

    }

    default boolean isFirstOrder(CustomerEntity customerEntity)  throws RemoteException{
        return customerEntity.getOrders().size() == 1;

    }

    PromotionEntity getBestPromotionByCustomerLevelAndTotalPrice(double totalPaid, CustomerLevelEnum customerLevelEnum) throws RemoteException;

    List<PromotionEntity> getListPromotionActive(String active) throws RemoteException;

    List<PromotionEntity> getPromotionsWithKeywordfit(LocalDateTime startDate, LocalDateTime endDate, String scrip, Double discount, Double minPrice, List<CustomerLevelEnum> rank, PromotionTypeEnum type, boolean active) throws RemoteException;
}
