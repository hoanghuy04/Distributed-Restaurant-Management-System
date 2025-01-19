package util.datafaker;

import dal.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.*;
import model.enums.TableStatusEnum;
import net.datafaker.Faker;

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
public class DataGenerator {

    Faker faker = new Faker(new java.util.Locale("vi"));
    private final Random rand = new Random();
    EntityManager em = Persistence.createEntityManagerFactory("mariadb").createEntityManager();
    private final CategoryDAL categoryDAL = new CategoryDAL(em);
    private final ItemDAL itemDAL = new ItemDAL(em);
    private final ToppingDAL toppingDAL = new ToppingDAL(em);
    private final ItemToppingDAL itemToppingDAL = new ItemToppingDAL(em);
//    private final EmployeeDAL employeeDAL = new EmployeeDAL(em);
    private final RoleDAL roleDAL = new RoleDAL();
//    private final PromotionDAL promotionDAL = new PromotionDAL(em);
//    private final PromotionDetailDAL promotionDetailDAL = new PromotionDetailDAL(em);
    private final CustomerDAL customerDAL = new CustomerDAL(em);
    private final FloorDAL floorDAL = new FloorDAL(em);
    private final TableDAL tableDAL = new TableDAL(em);
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

    private String generateVietnamesePhoneNumber() {
        String[] prefixes = {"03", "07", "08", "09", "056", "058", "070", "079", "077", "076", "078"};
        String prefix = prefixes[new Random().nextInt(prefixes.length)];
        String suffix = String.format("%07d", new Random().nextInt(10000000)); // 7 chữ số ngẫu nhiên
        return prefix + suffix;
    }
    // CustomerEntity
    private CustomerEntity getCustomerEntity() {
        CustomerEntity customer = new CustomerEntity();
        customer.setName(faker.name().fullName());
        customer.setEmail(faker.internet().emailAddress());
        customer.setPhone(generateVietnamesePhoneNumber());
        LocalDateTime dayOfBirth = LocalDateTime.now().minusYears(18 + rand.nextInt(42));
        customer.setDayOfBirth(dayOfBirth);
        customer.setAddress(generateAddress());
        customer.setRewardedPoint(rand.nextInt(3000));
        customer.setCustomerLevel(customer.getLevelCustomer());
        return customer;
    }

    // FloorEntity
    private FloorEntity generateFloorEntity(int numberFloor, int capacityFloor) {
        FloorEntity floor = new FloorEntity();
        floor.setName("Tầng" + numberFloor);
        floor.setCapacity(capacityFloor);
        Set<TableEntity> tables = new HashSet<>();
        floor.setTables(tables);
        return floor;
    }

    //TableEntity
    private TableEntity getTableEntity(FloorEntity floor) {
        TableEntity table = new TableEntity();
        table.setCapacity(rand.nextInt(6) + 2);
        table.setTableStatus(TableStatusEnum.AVAILABLE);
        table.setFloor(floor);
        return table;
    }

    //OrderEntity
    private OrderEntity generateOrderEntity() {
        return null;
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

        //Floor
        for (int i = 1; i < 4; i++) {
            FloorEntity f = generateFloorEntity(i,15);
            tr.begin();
            floorDAL.insert(f);
            tr.commit();
        }
        //Floor Insert
        floorDAL.findAll().forEach(x -> System.out.println(x));

        //Table
//        for (int i = 1; i < 4; i++) {
//
//        }
    }

    public static void main(String[] args) {
        DataGenerator gen = new DataGenerator();
        gen.generateAndPrintSampleData();
    }


}
