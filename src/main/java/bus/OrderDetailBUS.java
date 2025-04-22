package bus;

import dal.OrderDetailDAL;
import model.OrderDetailEntity;
import model.OrderDetailId;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class OrderDetailBUS implements BaseBUS<OrderDetailEntity, OrderDetailId> {

    private OrderDetailDAL orderDetailDAL;

    public OrderDetailBUS(EntityManager entityManager) {
        this.orderDetailDAL = new OrderDetailDAL(entityManager);
    }

    @Override
    public boolean insertEntity(OrderDetailEntity orderDetail) {
        return orderDetailDAL.insert(orderDetail);
    }

    @Override
    public boolean updateEntity(OrderDetailEntity orderDetail) {
        return orderDetailDAL.update(orderDetail);
    }

    @Override
    public boolean deleteEntity(OrderDetailId id) {
        return orderDetailDAL.deleteById(id);
    }

    @Override
    public OrderDetailEntity getEntityById(OrderDetailId id) {
       return orderDetailDAL.findById(id);
    }

    @Override
    public List<OrderDetailEntity> getAllEntities() {
        return orderDetailDAL.findAll();
    }
}
