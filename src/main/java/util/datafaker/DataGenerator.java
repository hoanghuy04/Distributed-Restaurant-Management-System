package util.datafaker;
//Huy
import dal.*;
import dal.connectDB.ConnectDB;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.Data;
import model.*;
import model.enums.*;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
    private final RoleDAL roleDAL = new RoleDAL(em);
    private final PromotionDAL promotionDAL = new PromotionDAL(em);
    private final PromotionDetailDAL promotionDetailDAL = new PromotionDetailDAL(em);
    private final CustomerDAL customerDAL = new CustomerDAL(em);
    private final FloorDAL floorDAL = new FloorDAL(em);
    private final TableDAL tableDAL = new TableDAL(em);
    private final OrderDAL orderDAL = new OrderDAL(em);
    private final OrderDetailDAL orderDetailDAL = new OrderDetailDAL(em);

    // CategoryEntity
    public CategoryEntity generateCategoryEntity(String name) {
        if(categoryDAL.findByName(name).orElse(null) != null) {
            return null;
        }
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
            name = "Pizza " + (rand.nextBoolean()?  faker.food().spice(): faker.food().ingredient());
        } else if (category.getName().trim().equalsIgnoreCase("Mì Ý")) {
            name = "Mì Ý " + (rand.nextBoolean()?  faker.food().spice(): faker.food().ingredient());
        } else if (category.getName().trim().equalsIgnoreCase("Khai Vị")) {
            name = "Khai Vị " + faker.food().dish();
        } else if (category.getName().trim().equalsIgnoreCase("Đồ uống")) {
            int drinkType = rand.nextInt(3);
            switch (drinkType) {
                case 0:
                    name = "Beer " + faker.beer().name();
                    break;
                case 1:
                    name = "Tea " + faker.tea().type();
                    break;
                case 2:
                    name = "Coffee " + faker.coffee().blendName();
                    break;
            }
        }
        if(itemDAL.findByName(name).orElse(null) != null) {
            return null;
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
        String name = isDefault ? "DEFAULT_TOPPING": (rand.nextBoolean()? "Spice " + faker.food().spice(): "Ingredient " + faker.food().ingredient());
        double costPrice = rand.nextDouble() * 50 + 10;
        int stockQuantity = rand.nextInt(100) + 1;
        String description = faker.lorem().sentence();
        if(itemDAL.findByName(name).orElse(null) != null) {
            return null;
        }
        try {
            return new ToppingEntity("", name, costPrice, stockQuantity, description,
                    true, new HashSet<>());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //ItemToppingEntity
    public ItemToppingEntity generateItemToppingEntity(ToppingEntity toppingEntity, ItemEntity itemEntity) {
        if(itemToppingDAL.findByItemAndTopping(itemEntity, toppingEntity).orElse(null) != null) {
            return null;
        }
        return new ItemToppingEntity(itemEntity, toppingEntity);
    }

    //Address
    public Address generateAddress() {
        Address address = new Address();
        address.setStreet(faker.address().streetAddress());
        String[] districts = {"Ba Đình", "Hoàn Kiếm", "Tây Hồ", "Hai Bà Trưng", "Đống Đa", "Cầu Giấy", "Hà Đông", "Long Biên"};
        address.setDistrict(districts[rand.nextInt(districts.length)]);
        String[] wards = {"Phường Trúc Bạch", "Phường Cửa Đông", "Phường Yên Phụ", "Phường Phan Chu Trinh", "Phường Ngọc Hà"};
        address.setWard(wards[rand.nextInt(wards.length)]);
        String[] cities = {
                "Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Bến Tre", "Nha Trang",
                "Cần Thơ", "Hải Phòng", "Đồng Nai", "Bình Dương", "Vũng Tàu",
                "Quy Nhơn", "Phan Thiết", "Huế", "Ninh Bình", "Quảng Ninh",
                "Vĩnh Long", "Long An", "An Giang", "Sóc Trăng", "Tiền Giang",
                "Kiên Giang", "Bắc Giang", "Lào Cai", "Thái Nguyên", "Nam Định",
                "Hạ Long", "Tây Ninh", "Bà Rịa", "Bạc Liêu", "Móng Cái",
                "Lâm Đồng", "Gia Lai", "Kon Tum", "Hòa Bình", "Quảng Nam", "Quảng Ngãi"
        };
        address.setCity(cities[rand.nextInt(cities.length)]);
        return address;
    }

    // EmployeEntity
    public EmployeeEntity generateEmployeeEntity() {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setPassword("123");
        employee.setFullname(faker.name().fullName());
        employee.setPhoneNumber(generateVietnamesePhoneNumber());
        employee.setEmail(faker.internet().emailAddress());
        employee.setAddress(generateAddress());
        employee.setActive(faker.bool().bool());
        System.out.println(employee.getRole());

        List<RoleEntity> roles = roleDAL.findAll();
        employee.setRole(roles.isEmpty() ? null : roles.get(rand.nextInt(roles.size())));
        return employee;
    }

    // RoleEntity
    public RoleEntity generateRoleEntity() {
        RoleEntity roleEntity = new RoleEntity();
        Random random = new Random();

        List<String> roles = new ArrayList<>(Arrays.asList("MANAGER", "STAFF"));
        List<String> currRoles = new ArrayList<>();
        if (roleDAL.findAll().size() > 0) {
            roleDAL.findAll().stream().forEach(role -> currRoles.add(role.getRoleName()));
        }
        roles.removeAll(currRoles);
        String randomRole = roles.get(random.nextInt(roles.size()));
        roleEntity.setRoleName(randomRole);
        return roleEntity;
    }

    // PromotionEntity
    public PromotionEntity generatePromotionEntity() {
        PromotionEntity promotion = new PromotionEntity();
        promotion.setDescription("Khuyến mãi " + faker.commerce().productName());
        promotion.setDiscountPercentage(faker.number().randomDouble(2, 5, 50));

        LocalDate startDate = LocalDate.now().minusDays(faker.number().numberBetween(1, 30));
        LocalDate endDate = startDate.plusDays(faker.number().numberBetween(7, 30));
        promotion.setStartedDate(startDate);
        promotion.setEndedDate(endDate);

        LocalDate today = LocalDate.now();
        boolean isActive = (today.isAfter(startDate) || today.isEqual(startDate)) && (today.isBefore(endDate) || today.isEqual(endDate));


        PromotionTypeEnum[] promotionTypes = PromotionTypeEnum.values();
        promotion.setPromotionType(promotionTypes[faker.number().numberBetween(0, promotionTypes.length)]);

        CustomerLevelEnum[] customerLevels = CustomerLevelEnum.values();
        List<CustomerLevelEnum> shuffledLevels = new ArrayList<>();
        Collections.addAll(shuffledLevels, customerLevels);

        // Shuffle the list to ensure randomness
        Collections.shuffle(shuffledLevels, new Random());

        // Generate a random number between 1 and 3
        int count = new Random().nextInt(3) + 1;

        // Return the first `count` elements of the shuffled list
        List<CustomerLevelEnum> customerLevelList =  shuffledLevels.subList(0, count);

        promotion.setCustomerLevels(customerLevelList);

        promotion.setActive(isActive);
        int minPrice = faker.number().numberBetween(5, 21) * 100_000;
        promotion.setMinPrice(minPrice);
        return promotion;
    }

    public PromotionDetailEntity generatePromotionDetailEntity() {
        PromotionDetailEntity promotionDetail = new PromotionDetailEntity();

        List<PromotionEntity> itemPromotions = promotionDAL.findAll().stream()
                .filter(promo -> promo.getPromotionType() == PromotionTypeEnum.ITEM)
                .toList();

        if (itemPromotions.isEmpty()) {
            return promotionDetail;
        }

        PromotionEntity selectedPromotion = itemPromotions.get(rand.nextInt(itemPromotions.size()));
        promotionDetail.setPromotion(selectedPromotion);

        Set<ItemEntity> existingItems = promotionDetailDAL.findAll().stream()
                .filter(detail -> detail.getPromotion().equals(selectedPromotion))
                .map(PromotionDetailEntity::getItem)
                .collect(Collectors.toSet());

        List<ItemEntity> availableItems = itemDAL.findAll().stream()
                .filter(item -> !existingItems.contains(item))
                .toList();
        if (!availableItems.isEmpty()) {
            promotionDetail.setItem(availableItems.get(rand.nextInt(availableItems.size())));
        }

        return promotionDetail;
    }

    // CustomerEntity
    public CustomerEntity generateCustomerEntity() {
        CustomerEntity customer = new CustomerEntity();
        customer.setName(faker.name().fullName());
        customer.setEmail(faker.internet().emailAddress());
        customer.setPhone(generateVietnamesePhoneNumber());
        LocalDateTime dayOfBirth = LocalDateTime.now().minusYears(18 + rand.nextInt(42));
        customer.setDayOfBirth(dayOfBirth);
        customer.setAddress(generateAddress());
        customer.setRewardedPoint(customer.getRewardedPoint());
        customer.setCustomerLevel(customer.getLevelCustomer());
        return customer;
    }

    // FloorEntity
    public FloorEntity generateFloorEntity( int capacityFloor) {
        FloorEntity floor = new FloorEntity();
        int numberOfFloor = floorDAL.findAll().size()+1;
        floor.setName("Tầng " + numberOfFloor);
        floor.setCapacity(capacityFloor);
        Set<TableEntity> tables = new HashSet<>();
        floor.setTables(tables);
        return floor;
    }

    //TableEntity
    public TableEntity generateTableEntity(FloorEntity floor) {
        TableEntity table = new TableEntity();
        int numberOfTable = tableDAL.findAll().size()+1;
        table.setName("Bàn " + numberOfTable);
        table.setCapacity(rand.nextInt(6) + 2);
        table.setTableStatus(TableStatusEnum.AVAILABLE);
        table.setFloor(floor);
        floor.getTables().add(table);
        return table;
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

            orderDetails.add(detail);
        }

        order.setOrderDetails(orderDetails);
        order.setTotalPrice();
        order.setTotalDiscount();
        order.setTotalPaid();

        return order;
    }
    private String generateVietnamesePhoneNumber() {
        String[] prefixes = {"03", "07", "08", "09", "056", "058", "070", "079", "077", "076", "078"};
        String prefix = prefixes[new Random().nextInt(prefixes.length)];
        String suffix = String.format("%07d", new Random().nextInt(10000000)); // 7 chữ số ngẫu nhiên
        return prefix + suffix;
    }

    public void generateAndPrintSampleData() {
        //CategoryEntity
        String[] categoryNames = {"Pizza", "Mì Ý", "Khai Vị", "Đồ uống"};
        for (String name : categoryNames) {
            CategoryEntity category = generateCategoryEntity(name);
            if (category != null) {
                categoryDAL.insert(category);
            }
        }

        //ToppingEntity
        for (int i = 0; i < 6; i++) {
            ToppingEntity topping = generateToppingEntity(i == 0);
            if (topping != null) {
                toppingDAL.insert(topping);
            }
        }

        //ItemEntity & ItemToppingEntity
        for (CategoryEntity categoryEntity : categoryDAL.findAll()) {
            List<ToppingEntity> allToppings = toppingDAL.findAll();
            for (int i = 0; i < 9; i++) {
                ItemEntity itemEntity = generateItemEntity(categoryEntity);
                if(itemEntity != null) {
                    itemDAL.insert(itemEntity);
                    if (categoryEntity.getName().equalsIgnoreCase("Pizza")) {
                        if (itemEntity.getSize() == SizeEnum.SMALL) {
                            for (int j = 0; j < 2 && j < allToppings.size(); j++) {
                                ToppingEntity toppingEntity = allToppings.get(j);
                                ItemToppingEntity itemTopping = generateItemToppingEntity(toppingEntity, itemEntity);
                                itemToppingDAL.insert(itemTopping);
                            }
                        } else {
                            for (ToppingEntity topping : allToppings) {
                                ItemToppingEntity itemTopping = generateItemToppingEntity(topping, itemEntity);
                                itemToppingDAL.insert(itemTopping);
                            }
                        }
                    } else {
                        ToppingEntity defaultTopping = allToppings.get(0);
                        ItemToppingEntity itemTopping = generateItemToppingEntity(defaultTopping, itemEntity);
                        itemToppingDAL.insert(itemTopping);
                    }
                }
            }
        }
        
        //Role entity
        roleDAL.insert(new RoleEntity("R0001", "MANAGER", LocalDate.now()));
        roleDAL.insert(new RoleEntity("R0002", "MANAGER", LocalDate.now()));

        //Employee entity
        for (int i = 0; i < 10; i++) {
            employeeDAL.insert(generateEmployeeEntity());
        }

        //promotion entity
        for (int i = 0; i < 10; i++) {
            promotionDAL.insert(generatePromotionEntity());
        }

        //promotion detail entity
        for (int i = 0; i < 10; i++) {
            promotionDetailDAL.insert(generatePromotionDetailEntity());
        }

        //Customer entity
        for (int i = 0; i < 10; i++) {
            customerDAL.insert(generateCustomerEntity());
        }

        //Floor
        for (int i = 0; i < 3; i++) {
            floorDAL.insert(generateFloorEntity(15));
        }
        //Table
        floorDAL.findAll().forEach(x -> {
            for(int i = 0; i<10; ++i) {
                tableDAL.insert(generateTableEntity(x));
            }
        });
        //Customer
        for(int i = 0; i<10; ++i) {
            customerDAL.insert(generateCustomerEntity());
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