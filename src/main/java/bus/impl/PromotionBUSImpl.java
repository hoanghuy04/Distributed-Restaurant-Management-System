package bus.impl;

import bus.BaseBUS;
import dal.PromotionDAL;
import model.CustomerEntity;
import model.PromotionEntity;
import jakarta.persistence.EntityManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import model.enums.CustomerLevelEnum;
import model.enums.PromotionTypeEnum;

public class PromotionBUSImpl extends UnicastRemoteObject implements bus.PromotionBUS {

    private PromotionDAL promotionDAL;

    public PromotionBUSImpl(EntityManager entityManager)  throws RemoteException {
        this.promotionDAL = new PromotionDAL(entityManager);
    }

    @Override
    public boolean insertEntity(PromotionEntity promotion)  throws RemoteException {
        return promotionDAL.insert(promotion);
    }

    @Override
    public boolean updateEntity(PromotionEntity promotion)  throws RemoteException {
        return promotionDAL.update(promotion);
    }

    @Override
    public boolean deleteEntity(String id)  throws RemoteException {
        return promotionDAL.deleteById(id);
    }

    @Override
    public PromotionEntity getEntityById(String id)  throws RemoteException {
        return promotionDAL.findById(id);
    }

    @Override
    public List<PromotionEntity> getAllEntities()  throws RemoteException {
        return promotionDAL.findAll();
    }

//    public double getPromotionByCustomer(OrderEntity orderEntity)  throws RemoteException {
//        LevelCustomer levelCustomer = orderEntity.getCustomer().getLevelCustomer();
//        double totalPrice = orderEntity.getTotalPrice();
//
//        double discountRate = 0.0;
//        switch (levelCustomer)  throws RemoteException {
//            case LevelCustomer.VIP:
//                if (totalPrice >= 500000.0)  throws RemoteException {
//                    discountRate = 0.05;
//                }
//                break;
//            case LevelCustomer.POTENTIAL:
//                if (isFirstOrderOfMonth(orderEntity.getCustomer()))  throws RemoteException {
//                    discountRate = 0.07;
//                }
//                break;
//            case LevelCustomer.NEW:
//                if (isFirstOrder(orderEntity.getCustomer()))  throws RemoteException {
//                    discountRate = 0.1;
//                }
//                break;
//            default:
//                discountRate = 0.0;
//        }
//        return discountRate;
//    }

    @Override
    public PromotionEntity getBestPromotionByCustomerLevelAndTotalPrice(double totalPaid, CustomerLevelEnum customerLevelEnum)  throws RemoteException {
        PromotionEntity promotionEntity = promotionDAL.getPromotionsByCustomerLevelAndTotalPrice(totalPaid, customerLevelEnum);
        return promotionEntity;
    }
    
    @Override
    public List<PromotionEntity> getListPromotionActive(String active)  throws RemoteException {
        return promotionDAL.findAll().stream().filter(x -> x.isActive()).collect(Collectors.toList());
    }
    
    @Override
    public List<PromotionEntity> getPromotionsWithKeywordfit(LocalDateTime startDate, LocalDateTime endDate, String scrip, Double discount, Double minPrice, List<CustomerLevelEnum> rank, PromotionTypeEnum type, boolean active)  throws RemoteException {
        return promotionDAL.getPromotionsWithKeywordfit(startDate, endDate, scrip, discount, minPrice, rank, type, active);
    }
}
