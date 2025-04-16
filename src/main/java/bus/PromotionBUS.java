package bus;

import dal.PromotionDAL;
import model.CustomerEntity;
import model.OrderEntity;
import model.PromotionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import model.enums.CustomerLevelEnum;
import model.enums.PromotionTypeEnum;

public class PromotionBUS implements BaseBUS<PromotionEntity, String> {

    private PromotionDAL promotionDAL;

    public PromotionBUS(EntityManager entityManager) {
        this.promotionDAL = new PromotionDAL(entityManager);
    }

    @Override
    public boolean insertEntity(PromotionEntity promotion) {
        return promotionDAL.insert(promotion);
    }

    @Override
    public boolean updateEntity(PromotionEntity promotion) {
        return promotionDAL.update(promotion);
    }

    @Override
    public boolean deleteEntity(String id) {
        return promotionDAL.deleteById(id);
    }

    @Override
    public PromotionEntity getEntityById(String id) {
        return promotionDAL.findById(id);
    }

    @Override
    public List<PromotionEntity> getAllEntities() {
        return promotionDAL.findAll();
    }

//    public double getPromotionByCustomer(OrderEntity orderEntity) {
//        LevelCustomer levelCustomer = orderEntity.getCustomer().getLevelCustomer();
//        double totalPrice = orderEntity.getTotalPrice();
//
//        double discountRate = 0.0;
//        switch (levelCustomer) {
//            case LevelCustomer.VIP:
//                if (totalPrice >= 500000.0) {
//                    discountRate = 0.05;
//                }
//                break;
//            case LevelCustomer.POTENTIAL:
//                if (isFirstOrderOfMonth(orderEntity.getCustomer())) {
//                    discountRate = 0.07;
//                }
//                break;
//            case LevelCustomer.NEW:
//                if (isFirstOrder(orderEntity.getCustomer())) {
//                    discountRate = 0.1;
//                }
//                break;
//            default:
//                discountRate = 0.0;
//        }
//        return discountRate;
//    }

    private boolean isFirstOrderOfMonth(CustomerEntity customerEntity) {
        return customerEntity.getOrders().stream()
                .filter(o -> o.getReservationTime().getMonth() == LocalDateTime.now().getMonth())
                .count() == 1;

    }

    private boolean isFirstOrder(CustomerEntity customerEntity) {
        return customerEntity.getOrders().size() == 1;

    }

    public PromotionEntity getBestPromotionByCustomerLevelAndTotalPrice(double totalPaid, CustomerLevelEnum customerLevelEnum) {
        Optional<PromotionEntity> optionalPromotionEntity = promotionDAL.getPromotionsByCustomerLevelAndTotalPrice(totalPaid, customerLevelEnum);
        return optionalPromotionEntity.orElse(null);
    }
    
    public List<PromotionEntity> getListPromotionActive(String active) {
        return promotionDAL.findAll().stream().filter(x -> x.isActive()).collect(Collectors.toList());
    }
    
    public List<PromotionEntity> getPromotionsWithKeywordfit(LocalDateTime startDate, LocalDateTime endDate, String scrip, Double discount, Double minPrice, String rank, PromotionTypeEnum type, boolean active) {
        return promotionDAL.getPromotionsWithKeywordfit(startDate, endDate, scrip, discount, minPrice, rank, type, active);
    }
}
