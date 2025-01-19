package util.datafaker;

import dal.*;
import dal.connectDB.ConnectDB;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.Data;
import model.*;
import model.enums.*;
import net.datafaker.Faker;

import java.time.LocalDateTime;
import java.util.*;


/*
 * @description: DataGenerator
 * @author: Trần Ngọc Huyền
 * @date: 1/18/2025
 * @version: 1.0
 */
@Data
public class DataGenerator {
    private Faker faker = new Faker(new Locale("vi"));
    private final Random rand = new Random();
    private final EntityManager em = ConnectDB.getEntityManager();
    private final CategoryDAL categoryDAL = new CategoryDAL(em);
    private final ItemDAL itemDAL = new ItemDAL(em);
    private final ToppingDAL toppingDAL = new ToppingDAL(em);
    private final ItemToppingDAL itemToppingDAL = new ItemToppingDAL(em);
    private final EmployeeDAL employeeDAL = new EmployeeDAL(em);
    private final RoleDAL roleDAL = new RoleDAL();
    private final PromotionDAL promotionDAL = new PromotionDAL(em);
    private final PromotionDetailDAL promotionDetailDAL = new PromotionDetailDAL(em);
    private final CustomerDAL customerDAL = new CustomerDAL(em);
    private final FloorDAL floorDAL = new FloorDAL(em);
    private final TableDAL tableDAL = new TableDAL(em);
    private final OrderDAL orderDAL = new OrderDAL(em);
    private final OrderDetailDAL orderDetailDAL = new OrderDetailDAL(em);

    // CategoryEntity
    public CategoryEntity generateCategoryEntity(String name) {
        String description = "Danh mục " + name + " - " + faker.lorem().sentence();
        try {
            return new CategoryEntity("", name, description, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ItemEntity
    public ItemEntity generateItemEntity(CategoryEntity category) {
        String name = "";
        if (category.getName().trim().equalsIgnoreCase("Pizza")) {
            name = "Pizza " + faker.food().ingredient();
        } else if (category.getName().trim().equalsIgnoreCase("Mì Ý")) {
            name = "Mì Ý " + faker.food().ingredient();
        } else if (category.getName().trim().equalsIgnoreCase("Khai Vị")) {
            name = "Khai Vị " + faker.food().dish();
        } else if (category.getName().trim().equalsIgnoreCase("Đồ uống")) {
            int drinkType = rand.nextInt(3);
            switch (drinkType) {
                case 0:
                    name = "Beer " + faker.beer().name() + " " + faker.number().digits(2);
                    break;
                case 1:
                    name = "Tea " + faker.tea().type() + " " + faker.number().digits(2);
                    break;
                case 2:
                    name = "Coffee " + faker.coffee().blendName() + " " + faker.number().digits(2);
                    break;
            }
        }

        double costPrice = category.getName().trim().equalsIgnoreCase("Pizza") ?
                rand.nextDouble() * 150 + 50 : rand.nextDouble() * 100 + 50;
        int stockQuantity = rand.nextInt(100) + 1;
        String description = faker.lorem().sentence();
        String img = faker.internet().url();
        SizeEnum size = category.getName().trim().equalsIgnoreCase("Pizza") ? SizeEnum.values()[rand.nextInt(SizeEnum.values().length)] : null;

        try {
            return new ItemEntity("", name, costPrice, stockQuantity,
                    description, img, true, size, category, new HashSet<>());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // ToppingEntity
    public ToppingEntity generateToppingEntity(boolean isDefault) {
        String name = isDefault ? "DEFAULT_TOPPING" : "Đế " + faker.food().ingredient();
        double costPrice = rand.nextDouble() * 50 + 10;
        int stockQuantity = rand.nextInt(100) + 1;
        String description = faker.lorem().sentence();
        try {
            return new ToppingEntity("", name, costPrice, stockQuantity, description,
                    true, new HashSet<>());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Address
    public Address generateAddress() {
        return null;
    }

    // EmployeEntity
    public EmployeeEntity generateEmployeeEntity() {
        return null;
    }

    // RoleEntity
    public RoleEntity generateRoleEntity() {
        return null;
    }

    // PromotionEntity
    public PromotionEntity generatePromotionEntity() {
        return null;
    }

    // PromotionDetailEntity
    public PromotionDetailEntity generatePromotionDetailEntity() {
        return null;
    }

    // CustomerEntity
    public CustomerEntity getCustomerEntity() {
        return null;
    }

    // FloorEntity
    public FloorEntity generateFloorEntity() {
        return null;
    }

    //TableEntity
    public TableEntity getTableEntity() {
        return null;
    }

    //OrderEntity
    public OrderEntity generateOrderEntity() {
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
        try {
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

                detail.setItem(item);
                detail.setTopping(topping);

                double itemPrice = 0;
                itemPrice = detail.getItem().getSellingPrice();

                double toppingPrice = 0;
                if (topping != null) {
                    toppingPrice = detail.getTopping().getCostPrice();
                }
                double lineTotal = (itemPrice + toppingPrice) * detail.getQuantity();

                detail.setLineTotal();
                detail.setDiscount();
                detail.setDescription(faker.lorem().sentence());

                if (orderDetails.add(detail)) {
                    orderDetailDAL.insert(detail);
                }
            }

            order.setOrderDetails(orderDetails);

        } catch (Exception e) {
            e.printStackTrace();
        }
        order.setTotalPrice();
        order.setTotalDiscount();
        order.setTotalPaid();

        System.out.println(order);
        return order;
    }

    public void generateAndPrintSampleData() {
        //CategoryEntity
        {
            String[] categoryNames = {"Pizza", "Mì Ý", "Khai Vị", "Đồ uống"};
            for (String name : categoryNames) {
                categoryDAL.insert(generateCategoryEntity(name));
            }
            System.out.println("---------------DANH MỤC SẢN PHẨM---------------");
            categoryDAL.findAll().forEach(x -> System.out.println(x));
        }

        //ToppingEntity
        {
            for (int i = 0; i < 6; i++) {
                toppingDAL.insert(generateToppingEntity(i == 0));
            }
            System.out.println("---------------DANH MỤC TOPPING---------------");
            toppingDAL.findAll().forEach(x -> System.out.println(x));
        }

        //ItemEntity & ItemToppingEntity
        {
            for (CategoryEntity categoryEntity : categoryDAL.findAll()) {
                for (int i = 0; i < 9; i++) {
                    ItemEntity itemEntity = generateItemEntity(categoryEntity);
                    itemDAL.insert(itemEntity);
                    List<ToppingEntity> allToppings = toppingDAL.findAll();

                    if (categoryEntity.getName().equalsIgnoreCase("Pizza")) {
                        if (itemEntity.getSize() == SizeEnum.SMALL) {
                            for (int j = 0; j < 2 && j < allToppings.size(); j++) {
                                ToppingEntity topping = allToppings.get(j);
                                ItemToppingEntity itemTopping = new ItemToppingEntity(itemEntity, topping);
                                itemToppingDAL.insert(itemTopping);
                            }
                        } else {
                            for (ToppingEntity topping : allToppings) {
                                ItemToppingEntity itemTopping = new ItemToppingEntity(itemEntity, topping);
                                itemToppingDAL.insert(itemTopping);
                            }
                        }
                    } else {
                        ToppingEntity defaultTopping = allToppings.get(0);
                        ItemToppingEntity itemTopping = new ItemToppingEntity(itemEntity, defaultTopping);
                        itemToppingDAL.insert(itemTopping);
                    }
                }
                System.out.println("---------------Các sản phẩm trong danh mục " + categoryEntity.getName().toUpperCase() + " ---------------");
                itemDAL.findByCategory(categoryEntity).forEach(x -> System.out.println(x));
            }
        }

        //OrderEntity
        for (int i = 0; i < 10; i++) {
            //new Entity
            orderDAL.insert(generateOrderEntity());
        }
    }

    public static void main(String[] args) {
        DataGenerator gen = new DataGenerator();
        gen.generateAndPrintSampleData();
    }
}
