package bus;

import model.ItemEntity;
import model.PromotionDetailEntity;
import model.PromotionDetailId;

import java.rmi.RemoteException;
import java.util.List;

public interface PromotionDetailBUS extends BaseBUS<PromotionDetailEntity, PromotionDetailId> {
    @Override
    PromotionDetailEntity insertEntity(PromotionDetailEntity t) throws RemoteException;

    @Override
    boolean updateEntity(PromotionDetailEntity t) throws RemoteException;

    @Override
    boolean deleteEntity(PromotionDetailId id) throws RemoteException;

    @Override
    PromotionDetailEntity getEntityById(PromotionDetailId id) throws RemoteException;

    @Override
    List<PromotionDetailEntity> getAllEntities() throws RemoteException;

    PromotionDetailEntity getTopDiscountPercentageOnItem(double totalPrice, ItemEntity it) throws RemoteException;

    boolean deleteEntitiesByPromotionId(String promotionId) throws RemoteException;
}
