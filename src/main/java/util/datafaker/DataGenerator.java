package util.datafaker;

import dal.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import model.*;
import model.enums.CustomerLevelEnum;
import model.enums.PromotionTypeEnum;
import net.datafaker.Faker;

import java.time.LocalDate;
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
    private final RoleDAL roleDAL = new RoleDAL(em);
    private final PromotionDAL promotionDAL = new PromotionDAL(em);
    private final PromotionDetailDAL promotionDetailDAL = new PromotionDetailDAL(em);
//    private final CustomerDAL customerDAL = new CustomerDAL(em);
//    private final FloorDAL floorDAL = new FloorDAL(em);
//    private final TableDAL tableDAL = new TableDAL(em);
//    private final OrderDAL orderDAL = new OrderDAL(em);
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
    private EmployeeEntity generateEmployeeEntity() {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setPassword(faker.internet().password(8, 16));
        employee.setFullname(faker.name().fullName());
        employee.setPhoneNumber(generateVietnamesePhoneNumber());
        employee.setEmail(faker.internet().emailAddress());
        employee.setAddress(generateAddress());
        employee.setActive(faker.bool().bool());
        employee.setRole(generateRoleEntity());

        List<RoleEntity> roles = roleDAL.findAll();
        employee.setRole(roles.isEmpty() ? null : roles.get(rand.nextInt(roles.size())));
        return employee;
    }

    // RoleEntity
    private RoleEntity generateRoleEntity() {
        RoleEntity role = new RoleEntity();
        role.setRoleName(faker.job().title()); // Assign a random job title as the role name
        return role;
    }

    // PromotionEntity
    private PromotionEntity generatePromotionEntity() {
        PromotionEntity promotion = new PromotionEntity();
        promotion.setDescription("Khuyến mãi " + faker.commerce().productName());
        promotion.setDiscountPercentage(faker.number().randomDouble(2, 5, 50));
        LocalDate startDate = LocalDate.now().minusDays(faker.number().numberBetween(1, 30));
        LocalDate endDate = startDate.plusDays(faker.number().numberBetween(7, 30));
        promotion.setStartedDate(startDate);
        promotion.setEndedDate(endDate);
        LocalDate today = LocalDate.now();
        boolean isActive = (today.isAfter(startDate) || today.isEqual(startDate)) && (today.isBefore(endDate) || today.isEqual(endDate));
        promotion.setActive(isActive);
        int minPrice = faker.number().numberBetween(5, 21) * 100_000;
        promotion.setMinPrice(minPrice);

        return promotion;
    }

    // PromotionDetailEntity
    private PromotionDetailEntity generatePromotionDetailEntity() {
        PromotionDetailEntity promotionDetail = new PromotionDetailEntity();

        PromotionEntity promotion = generatePromotionEntity();
        promotionDetail.setPromotion(promotion);

        List<PromotionEntity> promotions = promotionDAL.findAll();
        promotionDetail.setPromotion(promotions.isEmpty() ? null : promotions.get(rand.nextInt(promotions.size())));

        List<ItemEntity> items = itemDAL.findAll();
        promotionDetail.setItem(items.isEmpty() ? null : items.get(rand.nextInt(items.size())));

        PromotionTypeEnum[] promotionTypes = PromotionTypeEnum.values();
        promotionDetail.setPromotionType(promotionTypes[faker.number().numberBetween(0, promotionTypes.length)]);

        CustomerLevelEnum[] customerLevels = CustomerLevelEnum.values();
        promotionDetail.setCustomerLevel(customerLevels[faker.number().numberBetween(0, customerLevels.length)]);

        promotionDetail.setDescription("Áp dụng cho " + faker.commerce().productName());

        return promotionDetail;
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
        return null;
    }

    private String generateVietnamesePhoneNumber() {
        String[] prefixes = {"03", "07", "08", "09", "056", "058", "070", "079", "077", "076", "078"};
        String prefix = prefixes[new Random().nextInt(prefixes.length)];
        String suffix = String.format("%07d", new Random().nextInt(10000000)); // 7 chữ số ngẫu nhiên
        return prefix + suffix;
    }

    public void generateAndPrintSampleData() {
        EntityManager em = Persistence
                .createEntityManagerFactory("mariadb")
                .createEntityManager();

        EntityTransaction tr = em.getTransaction();


        for (int i = 0; i < 10; i++) {
            //new Entity
            tr.begin();
            roleDAL.insert(generateRoleEntity());
            employeeDAL.insert(generateEmployeeEntity());
            promotionDAL.insert(generatePromotionEntity());
            promotionDetailDAL.insert(generatePromotionDetailEntity());
            tr.commit();
        }
    }

    public static void main(String[] args) {

        DataGenerator gen = new DataGenerator();
        gen.generateAndPrintSampleData();
    }


}
