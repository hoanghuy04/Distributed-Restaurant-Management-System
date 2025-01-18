package util.datafaker;

import dal.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import model.*;
import model.enums.*;
import net.datafaker.Faker;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/*
 * @description: DataGenerator
 * @author: Trần Ngọc Huyền
 * @date: 1/18/2025
 * @version: 1.0
 */
public class DataGenerator {

    Faker faker = new Faker(new java.util.Locale("vi"));
    private final Random rand = new Random();
    EntityManager em = Persistence.createEntityManagerFactory("mariadb").createEntityManager();
    private final CategoryDAL categoryDAL = new CategoryDAL(em);
    private final ItemDAL itemDAL = new ItemDAL(em);
    private final ToppingDAL toppingDAL = new ToppingDAL(em);
    private final ItemToppingDAL itemToppingDAL = new ItemToppingDAL(em);
    private final EmployeeDAL employeeDAL = new EmployeeDAL(em);
    private final RoleDAL roleDAL = new RoleDAL();
    //    private final PromotionDAL promotionDAL = new PromotionDAL(em);
//    private final PromotionDetailDAL promotionDetailDAL = new PromotionDetailDAL(em);
    private final CustomerDAL customerDAL = new CustomerDAL(em);
    //    private final FloorDAL floorDAL = new FloorDAL(em);
    private final TableDAL tableDAL = new TableDAL(em);
    private final OrderDAL orderDAL = new OrderDAL(em);
    private final OrderDetailDAL orderDetailDAL = new OrderDetailDAL(em);


    // CategoryEntity
    private CategoryEntity generateCategoryEntity() {
        return null;
    }

    // ItemEntity
    private ItemEntity generateItemEntity() {
        return null;
    }

    // ToppingEntity
    private ToppingEntity generateToppingEntity() {
        return null;
    }

    // ItemToppingEntity
    private ItemToppingEntity generateItemToppingEntity() {
        return null;
    }

    //Address
    private Address generateAddress() {
        return null;
    }

    // EmployeEntity
    private EmployeeEntity generateEmployeeEntity() {
        return null;
    }

    // RoleEntity
    private RoleEntity generateRoleEntity() {
        return null;
    }

    // PromotionEntity
    private PromotionEntity generatePromotionEntity() {
        return null;
    }

    // PromotionDetailEntity
    private PromotionDetailEntity generatePromotionDetailEntity() {
        return null;
    }

    // CustomerEntity
    private CustomerEntity getCustomerEntity() {
        return null;
    }

    // FloorEntity
    private FloorEntity generateFloorEntity() {
        return null;
    }

    //TableEntity
    private TableEntity getTableEntity() {
        return null;
    }

    //OrderEntity
    private OrderEntity generateOrderEntity() {
        // Tạo đối tượng OrderEntity
        OrderEntity order = new OrderEntity();

        // Gán giá trị bằng Random
        order.setReservationTime(LocalDateTime.now().plusDays(rand.nextInt(30) + 1));
        order.setExpectedCompletionTime(order.getReservationTime().plusHours(rand.nextInt(3) + 1));
        order.setNumberOfCustomer(rand.nextInt(10) + 1);
        order.setDeposit(rand.nextDouble() * 100);
        order.setOrderStatus(OrderStatusEnum.SINGLE);
        order.setOrderType(OrderTypeEnum.IMMEDIATE);
        order.setPaymentMethod(PaymentMethodEnum.CASH);
        order.setPaymentStatus(PaymentStatusEnum.PAID);
        order.setReservationStatus(ReservationStatusEnum.RECEIVED);

        // Lấy customer ngẫu nhiên
        List<CustomerEntity> customers = customerDAL.findAll();
        order.setCustomer(customers.isEmpty() ? null : customers.get(rand.nextInt(customers.size())));

        // Lấy employee ngẫu nhiên
        List<EmployeeEntity> employees = employeeDAL.findAll();
        order.setEmployee(employees.isEmpty() ? null : employees.get(rand.nextInt(employees.size())));

        // Lấy table ngẫu nhiên
        List<TableEntity> tables = tableDAL.findAll();
        order.setTable(tables.isEmpty() ? null : tables.get(rand.nextInt(tables.size())));

        // Tạo danh sách OrderDetailEntity
        HashSet<OrderDetailEntity> orderDetails = new HashSet<>();
        int numberOfItems = faker.number().numberBetween(1, 5);

        List<ItemEntity> items = itemDAL.findAll();
        List<ToppingEntity> toppings = toppingDAL.findAll();

        for (int j = 0; j < numberOfItems; j++) {
            // Tạo OrderDetailEntity
            OrderDetailEntity detail = new OrderDetailEntity();
            detail.setOrder(order);

            // Lấy item và topping ngẫu nhiên
            ItemEntity item = items.isEmpty() ? null : items.get(rand.nextInt(items.size()));
            ToppingEntity topping = toppings.isEmpty() ? null : toppings.get(rand.nextInt(toppings.size()));

            detail.setQuantity(rand.nextInt(5) + 1);

            double itemPrice = 0;
            if (item != null) {
                itemPrice = detail.getItem().getSellingPrice();

            }

            double toppingPrice = 0;
            if (topping != null) {
                toppingPrice = detail.getTopping().getCostPrice();
            }
            double lineTotal = (itemPrice + toppingPrice) * detail.getQuantity();

            detail.setLineTotal();
            detail.setDiscount();
            detail.setDescription(faker.lorem().sentence());

            if(item != null && topping != null) {
                if (orderDetails.add(detail)) {
                    orderDetailDAL.insert(detail);
                }
            }
        }

        order.setOrderDetails(orderDetails);
        order.setTotalPrice();
        order.setTotalDiscount();
        order.setTotalPaid();

        System.out.println(order);
        return order;
    }

    public void generateAndPrintSampleData() {
        EntityManager em = Persistence
                .createEntityManagerFactory("mariadb")
                .createEntityManager();

        EntityTransaction tr = em.getTransaction();

        for (int i = 0; i < 10; i++) {
            //new Entity
            tr.begin();
            //persit
            tr.commit();
        }

        for (int i = 0; i < 10; i++) {
            //new Entity
            try {
                tr.begin();
                orderDAL.insert(generateOrderEntity());
                tr.commit();
            } catch (Exception e) {
                tr.rollback();
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        DataGenerator gen = new DataGenerator();
        gen.generateAndPrintSampleData();
    }


}
