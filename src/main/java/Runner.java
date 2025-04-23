/// *
// * @ (#) Runner.java      1.0      1/16/2025
// *
// * Copyright (c) 2025 IUH. ALL rights reserved.
// */
//
//import dal.ItemDAL;
//import dal.OrderDAL;
//import dal.OrderDetailDAL;
//import dal.ToppingDAL;
//import dal.connectDB.ConnectDB;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityTransaction;
//import jakarta.persistence.Persistence;
//import model.CategoryEntity;
//import model.*;
//import model.enums.*;
//import org.hibernate.Transaction;
//import util.datafaker.DataGenerator;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//import java.util.*;
//import java.util.stream.Collectors;
//
///*
// * @description:
// * @author: Hoang Huy
// * @date: 1/16/2025
// * @version: 1.0
// */
//public class Runner {
//    private static final Scanner sc = new Scanner(System.in);
//    private static final DataGenerator generator = new DataGenerator();
//    private static final EntityManager em = ConnectDB.getEntityManager();
//    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
//
//    private static final ItemDAL itemDAL = new ItemDAL(em);
//    private static final ToppingDAL toppingDAL = new ToppingDAL(em);
//    private static final OrderDAL orderDAL = new OrderDAL(em);
//    private static final OrderDetailDAL orderDetailDAL = new OrderDetailDAL(em);
//
//    public static void main(String[] args) {
//        boolean exit = false;
//        generator.generateAndPrintSampleData();
//        while (!exit) {
//            printMenu();
//            int choice = getChoice();
//
//            switch (choice) {
//                case 1:
//                    createEntity();
//                    break;
//                case 2:
//                    readEntity();
//                    break;
//                case 3:
//                    updateEntity();
//                    break;
//                case 4:
//                    deleteEntity();
//                    break;
//                case 5:
//                    exit = true;
//                    System.out.println("Thoát chương trình.");
//                    break;
//                default:
//                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
//                    break;
//            }
//        }
//    }
//
//    private static void printMenu() {
//        System.out.println("\n----- MENU CRUD ENTITY -----");
//        System.out.println("1. Create (Tạo mới)");
//        System.out.println("2. Read (Xem danh sách)");
//        System.out.println("3. Update (Cập nhật)");
//        System.out.println("4. Delete (Xóa)");
//        System.out.println("5. Exit (Thoát)");
//        System.out.print("Vui lòng chọn: ");
//    }
//
//    private static int getChoice() {
//        try {
//            return Integer.parseInt(sc.nextLine());
//        } catch (NumberFormatException e) {
//            return -1;
//        }
//    }
//
//    private static void createEntity() {
//        System.out.println("\nChọn loại Entity để tạo mới:");
//        printEntityOptions();
//        int entityChoice = getChoice();
//
//        switch (entityChoice) {
//            case 1: {
//                System.out.print("Nhập tên danh mục: ");
//                String name = sc.nextLine().trim();
//                if (name.isEmpty() || name.isBlank()) {
//                    System.out.println("Tên danh mục không được để trống");
//                    break;
//                }
//
//                if (generator.getCategoryDAL().findByName(name) != null) {
//                    System.out.println("Tên danh mục bị trùng");
//                    break;
//                }
//
//                System.out.print("Nhập mô tả danh mục: ");
//                String description = sc.nextLine().trim();
//
//                try {
//                    CategoryEntity category = new CategoryEntity("", name, description, true);
//                    String result = generator.getCategoryDAL().insert(category) ? "Tạo danh mục thành công " + category : "Tạo danh mục thất bại";
//                    System.out.println(result);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//            case 2: {
//                System.out.print("Nhập tên topping: ");
//                String name = sc.nextLine().trim();
//                if (name.isEmpty() || name.isBlank()) {
//                    System.out.println("Tên topping không được để trống");
//                    break;
//                }
//
//                if (generator.getToppingDAL().findByName(name) != null) {
//                    System.out.println("Tên topping bị trùng");
//                    break;
//                }
//
//                double costPrice = getDoubleInput("Nhập giá gốc của topping: ");
//                int stockQuantity = getIntInput("Nhập số lượng tồn kho của topping: ");
//
//                System.out.print("Nhập mô tả topping: ");
//                String description = sc.nextLine().trim();
//                try {
//                    ToppingEntity topping = new ToppingEntity("", name, costPrice, stockQuantity, description, true, new HashSet<>());
//                    String result = generator.getToppingDAL().insert(topping)
//                            ? "Thêm topping thành công: " + topping
//                            : "Thêm topping thất bại!";
//                    System.out.println(result);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//            case 3: {
//                System.out.print("Nhập tên sản phẩm: ");
//                String name = sc.nextLine().trim();
//
//                if (name.isEmpty() || name.isBlank()) {
//                    System.out.println("Tên sản phẩm không được để trống");
//                    break;
//                }
//
//                if (generator.getItemDAL().findByName(name).orElse(null) != null) {
//                    System.out.println("Tên sản phẩm bị trùng");
//                    break;
//                }
//
//                double costPrice = getDoubleInput("Nhập giá gốc của sản phẩm: ");
//                int stockQuantity = getIntInput("Nhập số lượng tồn kho của sản phẩm: ");
//
//                System.out.print("Nhập mô tả sản phẩm: ");
//                String description = sc.nextLine().trim();
//
//                System.out.println("Chọn kích thước sản phẩm:");
//                System.out.println("1. SMALL");
//                System.out.println("2. MEDIUM");
//                System.out.println("3. LARGE");
//                System.out.println("4. Để trống (Không chọn kích thước)");
//
//                System.out.print("Nhập lựa chọn (1-4): ");
//                String choice = sc.nextLine().trim();
//                SizeEnum size = null;
//
//                switch (choice) {
//                    case "1":
//                        size = SizeEnum.SMALL;
//                        break;
//                    case "2":
//                        size = SizeEnum.MEDIUM;
//                        break;
//                    case "3":
//                        size = SizeEnum.LARGE;
//                        break;
//                    case "4":
//                        size = null;
//                        break;
//                    default:
//                        System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập từ 1 đến 4.");
//                        break;
//                }
//
//                System.out.print("Nhập ID danh mục của sản phẩm (gợi ý CO001): ");
//                String categoryId = sc.nextLine().trim().toUpperCase();
//                CategoryEntity category = generator.getCategoryDAL().findById(categoryId);
//                if (category == null) {
//                    System.out.println("Không tìm thấy danh mục với ID: " + categoryId);
//                    break;
//                }
//
//                try {
//                    ItemEntity item = new ItemEntity("", name, costPrice, stockQuantity,
//                            description, "", true, size, category, new HashSet<>());
//
//                    String result = generator.getItemDAL().insert(item)
//                            ? "Thêm sản phẩm thành công: " + item
//                            : "Thêm sản phẩm thất bại!";
//                    System.out.println(result);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//            case 4: {
//                System.out.print("Nhập ID sản phẩm (gợi ý: I0001): ");
//                String itemId = sc.nextLine().trim().toUpperCase();
//                ItemEntity item = generator.getItemDAL().findById(itemId);
//                if (item == null) {
//                    System.out.println("Không tìm thấy sản phẩm với ID: " + itemId);
//                    break;
//                }
//
//                System.out.print("Nhập ID topping (gợi ý T0001): ");
//                String toppingId = sc.nextLine().trim().toUpperCase();
//                ToppingEntity topping = generator.getToppingDAL().findById(toppingId);
//                if (topping == null) {
//                    System.out.println("Không tìm thấy topping với ID: " + toppingId);
//                    break;
//                }
//                ItemToppingEntity existing = generator.getItemToppingDAL().findByItemAndTopping(item, topping);
//                if (existing != null) {
//                    System.out.println("Thực thể đã tồn tại: " + existing);
//                } else {
//                    ItemToppingEntity newEntity = new ItemToppingEntity(item, topping);
//                    generator.getItemToppingDAL().insert(newEntity);
//                    System.out.println("Thêm mới thành công: " + newEntity);
//                }
//
//                break;
//            }
//            case 5: {
//                System.out.println("Nhập mật khẩu:");
//                String password = sc.nextLine();
//
//                System.out.println("Nhập họ tên:");
//                String fullname = sc.nextLine();
//
//                System.out.println("Nhập so điện thoại: ");
//                String phoneNumber = sc.nextLine();
//
//                System.out.println("Nhập email: ");
//                String email = sc.nextLine();
//
//                System.out.println("Nhập địa chỉ: ");
//
//                System.out.println("\tNhập phố: ");
//                String street = sc.nextLine();
//
//                System.out.println("\tNhập phường: ");
//                String ward = sc.nextLine();
//
//                System.out.println("\tNhập đường: ");
//                String district = sc.nextLine();
//
//                System.out.println("\tNhập thành phố: ");
//                String city = sc.nextLine();
//
//                Address address = new Address();
//                address.setCity(city);
//                address.setStreet(street);
//                address.setDistrict(district);
//                address.setWard(ward);
//
//                System.out.println("Nhập trạng thái( 0=False; 1=True ): ");
//                String choice = sc.nextLine().trim();
//                boolean active = true;
//
//                switch (choice) {
//                    case "0":
//                        active = false;
//                        break;
//                    case "1":
//                        active = true;
//                        break;
//                    default:
//                        System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập 0 hoặc 1.");
//                        break;
//                }
//
//                System.out.print("Nhập ID chức vụ(gợi ý: R0001): ");
//                String roleId = sc.nextLine().trim().toUpperCase();
//                RoleEntity role = generator.getRoleDAL().findById(roleId).orElse(null);
//                if (role == null) {
//                    System.out.println("Không tìm thấy chức vu với ID: " + roleId);
//                    break;
//                }
//
//                try {
//                    EmployeeEntity employee = new EmployeeEntity("", password, fullname, phoneNumber, email,
//                            address, active, role);
//
//                    String result = generator.getEmployeeDAL().insert(employee)
//                            ? "Thêm nhân viên thành công: " + employee
//                            : "Thêm nhân viên thất bại!";
//                    System.out.println(result);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//            case 6: {
//                System.out.println("Nhập tên chức vụ:");
//                String roleName = sc.nextLine();
//                if (roleName.isEmpty() || roleName.length() == 0) {
//                    System.out.println("Tên chức vụ không hợp lệ");
//                    break;
//                }
//                try {
//                    RoleEntity role_tmp = new RoleEntity();
//                    role_tmp.setRoleName(roleName);
//                    String result = generator.getRoleDAL().insert(role_tmp)
//                            ? "Thêm chức vụ thành công: " + role_tmp
//                            : "Thêm chức vụ thất bại!";
//                    System.out.println(result);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//            case 7: {
//                {
//                    System.out.println("Nhập mô ta:");
//                    String description = sc.nextLine();
//                    double discountPercentage = getDoubleInput("Nhập phần trăm giảm giá:");
//                    LocalDate startedDate = getDateInput("Nhập ngày bắt đầu: ", LocalDate.now());
//                    LocalDate endedDate = getDateInput("Nhập ngày kết thúc: ", LocalDate.now());
//                    double minPrice = getDoubleInput("Nhập tiền tối thiểu để áp dụng: ");
//                    LocalDate today = LocalDate.now();
//
//                    System.out.println("Chọn loại khuyến mãi:");
//                    System.out.println("1. ITEM");
//                    System.out.println("2. ORDER");
//
//                    System.out.print("Nhập lựa chọn (1 hoặc 2): ");
//                    String promotionTypeChoice = sc.nextLine().trim();
//                    PromotionTypeEnum promotionType = null;
//
//                    switch (promotionTypeChoice) {
//                        case "1":
//                            promotionType = PromotionTypeEnum.ITEM;
//                            break;
//                        case "2":
//                            promotionType = PromotionTypeEnum.ORDER;
//                            break;
//                        default:
//                            System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập từ 1 hoặc 2.");
//                            break;
//                    }
//
//                    List<CustomerLevelEnum> customerLevels = new ArrayList<>();
//                    int numOfCustomerLevel = getIntInput("Nhập số lượng cấp bậc khách hàng: ");
//                    for (int i = 0;
//                         i < numOfCustomerLevel;
//                         i++) {
//                        System.out.println("Chọn cấp độ khách hàng:");
//                        System.out.println("1. NEW");
//                        System.out.println("2. POTENTIAL");
//                        System.out.println("3. VIP");
//
//                        System.out.print("Nhập lựa chọn (1 -> 3): ");
//                        String customerLevelChoice = sc.nextLine().trim();
//                        CustomerLevelEnum customerLevelType = null;
//
//                        switch (customerLevelChoice) {
//                            case "1":
//                                customerLevelType = CustomerLevelEnum.NEW;
//                                break;
//                            case "2":
//                                customerLevelType = CustomerLevelEnum.POTENTIAL;
//                                break;
//                            case "3":
//                                customerLevelType = CustomerLevelEnum.VIP;
//                                break;
//                            default:
//                                System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập từ 1 -> 3.");
//                                break;
//                        }
//                        customerLevels.add(customerLevelType);
//                    }
//
//                    boolean promotionActive = true;
//                    System.out.println("Nhập trạng thái( 0=False; 1=True; Enter = true ): ");
//                    String choice_1 = sc.nextLine().trim();
//
//                    switch (choice_1) {
//                        case "0":
//                            promotionActive = false;
//                            break;
//                        case "1":
//                            promotionActive = true;
//                            break;
//                        default:
//                            System.out.println("Trạng thái: True");
//                            break;
//                    }
//                    try {
//                        PromotionEntity promotion = new PromotionEntity("", promotionType, customerLevels, description, discountPercentage, startedDate, endedDate, promotionActive, minPrice, null, null);
//                        String result = generator.getPromotionDAL().insert(promotion)
//                                ? "Thêm promotion thành công: " + promotion
//                                : "Thêm promotion thất bại!";
//                        System.out.println(result);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
//            }
//            case 8: {
//                {
//                    PromotionEntity promotionSuggestion = generator.getPromotionDAL().findAll().stream().filter(promotionEntity -> promotionEntity.getPromotionType() == PromotionTypeEnum.ITEM).findFirst().orElse(null);
//                    if (promotionSuggestion == null) {
//                        System.out.println("Không tìm thấy khuyến mãi món");
//                        break;
//                    }
//                    System.out.print("Nhập ID khuyến mãi(chỉ có thể tạo với khuyến mãi món) : ");
//                    String promotionId = sc.nextLine().trim().toUpperCase();
//                    PromotionEntity promotion = generator.getPromotionDAL().findById(promotionId).orElse(null);
//                    if (promotion == null) {
//                        System.out.println("Không tìm thấy khuyến mãi với ID: " + promotionId);
//                        break;
//                    }
//
//                    System.out.print("Nhập ID món ăn: ");
//                    String itemId = sc.nextLine().trim().toUpperCase();
//                    ItemEntity item = generator.getItemDAL().findById(itemId);
//                    if (item == null) {
//                        System.out.println("Không tìm thấy món ăn với ID: " + itemId);
//                        break;
//                    }
//
//                    try {
//                        PromotionDetailEntity promotionDetail = new PromotionDetailEntity(promotion, item);
//                        String result = generator.getPromotionDetailDAL().insert(promotionDetail)
//                                ? "Thêm promotion detail thành công: " + promotionDetail
//                                : "Thêm promotion detail thất bại!";
//                        System.out.println(result);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
//            }
//            case 9:
//                System.out.println("Đang tạo mới CustomerEntity...");
//                // Nhập thông tin từ người dùng
//                System.out.print("Nhập tên khách hàng: ");
//                String name = sc.nextLine().trim();
//
//                System.out.print("Nhập email khách hàng: ");
//                String email = sc.nextLine().trim();
//
//                System.out.print("Nhập số điện thoại khách hàng: ");
//                String phone = sc.nextLine().trim();
//
//                System.out.print("Nhập ngày sinh (định dạng yyyy-MM-dd): ");
//                String dobInput = sc.nextLine().trim();
//                LocalDateTime dayOfBirth = null;
//                try {
//                    dayOfBirth = LocalDateTime.parse(dobInput + "T00:00:00");
//                } catch (Exception e) {
//                    System.out.println("Ngày sinh không hợp lệ. Sử dụng giá trị mặc định là null.");
//                }
//
//                System.out.print("Nhập địa chỉ (đường, thành phố, quốc gia): ");
//                System.out.print(" - Đường: ");
//                String street = sc.nextLine().trim();
//                System.out.print(" - Phường/Xã: ");
//                String ward = sc.nextLine().trim();
//                System.out.print(" - Quận/Huyên: ");
//                String district = sc.nextLine().trim();
//                System.out.print(" - Thành phố: ");
//                String city = sc.nextLine().trim();
//
//
//                Address address = new Address();
//                address.setStreet(street);
//                address.setWard(ward);
//                address.setDistrict(district);
//                address.setCity(city);
//
//                // Tạo đối tượng CustomerEntity
//                CustomerEntity newCustomer = new CustomerEntity();
//                newCustomer.setName(name);
//                newCustomer.setEmail(email);
//                newCustomer.setPhone(phone);
//                newCustomer.setDayOfBirth(dayOfBirth);
//                newCustomer.setAddress(address);
//                newCustomer.setRewardedPoint(0); // Điểm thưởng ban đầu là 0
//                newCustomer.setCustomerLevel(CustomerLevelEnum.NEW); // Mức độ khách hàng mặc định là NEW
//
//                if (generator.getCustomerDAL().insert(newCustomer)) {
//                    System.out.println("Thêm khách hàng mới thành công!");
//                    System.out.println(newCustomer);
//                } else {
//                    System.out.println("Thêm khách hàng mới thất bại!");
//                }
//                break;
//            case 10:
//                System.out.println("Đang tạo mới FloorEntity...");
//
//                // Nhập thông tin từ người dùng
//                System.out.print("Nhập tên tầng (Floor Name): ");
//                String floorName = sc.nextLine().trim();
//
//                System.out.print("Nhập sức chứa của tầng (Capacity): ");
//                int capacity = 0;
//                try {
//                    capacity = Integer.parseInt(sc.nextLine().trim());
//                    if (capacity <= 0) {
//                        System.out.println("Sức chứa phải là số nguyên dương. Vui lòng thử lại.");
//                        break;
//                    }
//                } catch (NumberFormatException e) {
//                    System.out.println("Sức chứa không hợp lệ. Vui lòng nhập một số nguyên.");
//                    break;
//                }
//
//                // Tạo đối tượng FloorEntity
//                FloorEntity newFloor = new FloorEntity();
//                newFloor.setName(floorName);
//                newFloor.setCapacity(capacity);
//
//                if (generator.getFloorDAL().insert(newFloor)) {
//                    System.out.println("Thêm tầng mới thành công!");
//                    System.out.println(newFloor);
//                } else {
//                    System.out.println("Thêm tầng mới thất bại!");
//                }
//                break;
//            case 11:
//                System.out.println("Đang tạo mới TableEntity...");
//                System.out.print("Nhập sức chứa của bàn (Capacity): ");
//                int capacityTable = 0;
//                try {
//                    capacityTable = Integer.parseInt(sc.nextLine().trim());
//                    if (capacityTable <= 0) {
//                        System.out.println("Sức chứa phải là số nguyên dương. Vui lòng thử lại.");
//                        break;
//                    }
//                } catch (NumberFormatException e) {
//                    System.out.println("Sức chứa không hợp lệ. Vui lòng nhập một số nguyên.");
//                    break;
//                }
//
//                TableStatusEnum tableStatus = TableStatusEnum.AVAILABLE;
//
//                // Nhập thông tin tầng
//                FloorEntity temp = generator.getFloorDAL().findAll().stream().findFirst().orElse(null);
//                if (temp != null) {
//                    System.out.println("Gợi ý ID: " + temp.getFloorId());
//                }
//
//                System.out.print("Nhập ID tầng (Floor ID) mà bàn sẽ được đặt: ");
//                String floorId = sc.nextLine().trim();
//                FloorEntity floor = generator.getFloorDAL().findById(floorId).orElse(null);
//                if (floor == null) {
//                    System.out.println("Không tìm thấy tầng với ID này.");
//                    break;
//                }
//
//                // Tạo đối tượng TableEntity
//                TableEntity newTable = new TableEntity();
//                newTable.setCapacity(capacityTable);
//                int numberOfTable = generator.getTableDAL().findAll().size() + 1;
//                newTable.setName("Bàn " + numberOfTable);
//                newTable.setTableStatus(tableStatus);
//                newTable.setFloor(floor);
//                floor.getTables().add(newTable);
//
//                if (generator.getTableDAL().insert(newTable)) {
//                    System.out.println("Thêm bàn mới thành công!");
//                    System.out.println(newTable);
//                } else {
//                    System.out.println("Thêm bàn mới thất bại!");
//                }
//                break;
//            case 12: {
//                System.out.println("Đang tạo mới OrderEntity...");
//
//                boolean flag = false;
//
//                LocalDateTime reservationTime = null;
//                do {
//                    try {
//                        System.out.println("Nhập thời gian đặt chỗ (dd-MM-yyyy HH:mm):");
//                        reservationTime = LocalDateTime.parse(sc.nextLine(), formatter);
//                        flag = true;
//                    } catch (Exception e) {
//                        flag = false;
//                    }
//                } while (!flag);
//
//                LocalDateTime expectedCompletionTime = null;
//                do {
//                    try {
//                        System.out.println("Nhập thời gian hoàn thành dự kiến (dd-MM-yyyy HH:mm):");
//                        expectedCompletionTime = LocalDateTime.parse(sc.nextLine(), formatter);
//                        flag = true;
//                    } catch (Exception e) {
//                        flag = false;
//                    }
//                } while (!flag);
//
//
//                System.out.println("Nhập số lượng khách:");
//                int numberOfCustomer = sc.nextInt();
//
//                System.out.println("Nhập tiền đặt cọc:");
//                double deposit = sc.nextDouble();
//
//                sc.nextLine(); // Xóa dòng trống
//
//                OrderStatusEnum orderStatus = null;
//                do {
//                    try {
//                        System.out.println("Nhập trạng thái đơn hàng (SINGLE, MERGE):");
//                        orderStatus = OrderStatusEnum.valueOf(sc.nextLine().toUpperCase());
//                        flag = true;
//                    } catch (Exception e) {
//                        flag = false;
//                    }
//                } while (!flag);
//
//                OrderTypeEnum orderType = null;
//                do {
//                    try {
//                        System.out.println("Nhập loại đơn hàng (ADVANCE, IMMEDIATE):");
//                        orderType = OrderTypeEnum.valueOf(sc.nextLine().toUpperCase());
//                        flag = true;
//                    } catch (Exception e) {
//                        flag = false;
//                    }
//                } while (!flag);
//
//                PaymentMethodEnum paymentMethod = null;
//                do {
//                    try {
//                        System.out.println("Nhập phương thức thanh toán (CASH, CREDIT_CARD, E_WALLET):");
//                        paymentMethod = PaymentMethodEnum.valueOf(sc.nextLine().toUpperCase());
//                        flag = true;
//                    } catch (Exception e) {
//                        flag = false;
//                    }
//                } while (!flag);
//
//                PaymentStatusEnum paymentStatus = null;
//                do {
//                    try {
//                        System.out.println("Nhập trạng thái thanh toán (UNPAID, PAID):");
//                        paymentStatus = PaymentStatusEnum.valueOf(sc.nextLine().toUpperCase());
//                        flag = true;
//                    } catch (Exception e) {
//                        flag = false;
//                    }
//                } while (!flag);
//
//                ReservationStatusEnum reservationStatus = null;
//                do {
//                    try {
//                        System.out.println("Nhập trạng thái đặt chỗ (PENDING, RECEIVED, CANCELLED):");
//                        reservationStatus = ReservationStatusEnum.valueOf(sc.nextLine().toUpperCase());
//                        flag = true;
//                    } catch (Exception e) {
//                        flag = false;
//                    }
//                } while (!flag);
//
//
//                // Khởi tạo OrderEntity
//                OrderEntity order = new OrderEntity();
//                order.setReservationTime(reservationTime);
//                order.setExpectedCompletionTime(expectedCompletionTime);
//                order.setTotalPrice();
//                order.setTotalDiscount();
//                order.setTotalPaid();
//                order.setNumberOfCustomer(numberOfCustomer);
//                order.setDeposit(deposit);
//                order.setOrderStatus(orderStatus);
//                order.setOrderType(orderType);
//                order.setPaymentMethod(paymentMethod);
//                order.setPaymentStatus(paymentStatus);
//                order.setReservationStatus(reservationStatus);
//
//                // Nhập danh sách OrderDetailEntity
//                System.out.println("Nhập số lượng chi tiết đơn hàng:");
//                int detailCount = sc.nextInt();
//                sc.nextLine(); // Xóa dòng trống
//                List<ItemEntity> items = itemDAL.findAll();
//                List<ToppingEntity> toppings = toppingDAL.findAll();
//                Set<OrderDetailEntity> orderDetails = new HashSet<>();
//                for (int i = 0; i < detailCount; i++) {
//                    System.out.println("Nhập thông tin chi tiết đơn hàng thứ " + (i + 1));
//
//                    System.out.println("Danh sách items: ");
//                    items.forEach(x -> System.out.println(x.getItemId() + " " + x.getName()));
//                    System.out.println("Nhập Item ID:");
//                    String itemId = sc.nextLine();
//
//                    System.out.println("Danh sách toppings: ");
//                    toppings.forEach(x -> System.out.println(x.getToppingId() + " " + x.getName()));
//                    System.out.println("Nhập Topping ID:");
//                    String toppingId = sc.nextLine();
//
//                    System.out.println("Nhập số lượng:");
//                    int quantity = sc.nextInt();
//
//                    System.out.println("Nhập mô tả:");
//                    String description = sc.nextLine();
//
//                    // Khởi tạo OrderDetailEntity
//                    OrderDetailEntity detail = new OrderDetailEntity();
//                    detail.setOrder(order);
//
//                    detail.setItem(itemDAL.findById(itemId));
//
//                    detail.setTopping(toppingDAL.findById(toppingId));
//
//                    detail.setQuantity(quantity);
//                    detail.setLineTotal();
//                    detail.setDiscount();
//                    detail.setDescription(description);
//
//                    orderDetails.add(detail);
//                }
//
//                order.setOrderDetails(orderDetails);
//                orderDAL.insert(order);
//                orderDetails.forEach(orderDetailDAL::insert);
//
//                // Hiển thị thông tin OrderEntity
//                System.out.println("OrderEntity được tạo:");
//                System.out.println(order);
//                break;
//            }
//            default:
//                System.out.println("Lựa chọn không hợp lệ.");
//                break;
//        }
//    }
//
//    private static void readEntity() {
//        System.out.println("\nChọn loại Entity để xem danh sách:");
//        printEntityOptions();
//        int entityChoice = getChoice();
//
//        switch (entityChoice) {
//            case 1:
//                generator.getCategoryDAL().findAll().forEach(System.out::println);
//                break;
//            case 2:
//                generator.getToppingDAL().findAll().forEach(System.out::println);
//                break;
//            case 3:
//                generator.getItemDAL().findAll().forEach(System.out::println);
//                break;
//            case 4:
//                generator.getItemToppingDAL().findAll().forEach(System.out::println);
//                break;
//            case 5:
//                generator.getEmployeeDAL().findAll().forEach(System.out::println);
//                break;
//            case 6:
//                generator.getRoleDAL().findAll().forEach(System.out::println);
//                break;
//            case 7:
//                generator.getPromotionDAL().findAll().forEach(System.out::println);
//                break;
//            case 8:
//                generator.getPromotionDetailDAL().findAll().forEach(System.out::println);
//                break;
//            case 9:
//                System.out.println("Đang đọc danh sách CustomerEntity...");
//                break;
//            case 10:
//                System.out.println("Đang đọc danh sách FloorEntity...");
//                break;
//            case 11:
//                System.out.println("Đang đọc danh sách TableEntity...");
//                break;
//            case 12: {
//                List<OrderEntity> orders = orderDAL.findAll();
//                System.out.println("Đang đọc danh sách OrderEntity...");
//                System.out.println("Danh sách Orders: ");
//                try {
//                    orders.forEach(order -> {
//                        try {
//                            System.out.println(order.getOrderId() + " " + order.getCustomer().getName() + " " + order.getCustomer().getPhone());
//                        } catch (Exception e) {
//                            System.out.println(order.getOrderId());
//                        }
//                    });
//                } catch (Exception e) {
//                    orders.forEach(order -> System.out.println(order.getOrderId()));
//                }
//                System.out.println("Nhập ID order cần xem chi tiết:");
//                String orderId = sc.nextLine();
//
//// Tìm OrderEntity theo ID
//                OrderEntity selectedOrder = orders.stream()
//                        .filter(order -> order.getOrderId().equals(orderId))
//                        .findFirst()
//                        .orElse(null);
//
//                if (selectedOrder == null) {
//                    System.out.println("Không tìm thấy Order với ID: " + orderId);
//                } else {
//                    System.out.println("Chi tiết Order:");
//                    try {
//                        System.out.println("Mã đơn hàng: " + selectedOrder.getOrderId());
//                        System.out.println("Khách hàng: " + selectedOrder.getCustomer().getName());
//                        System.out.println("Số điện thoại khách hàng: " + selectedOrder.getCustomer().getPhone());
//                        System.out.println("Thời gian đặt chỗ: " + selectedOrder.getReservationTime());
//                        System.out.println("Thời gian hoàn thành dự kiến: " + selectedOrder.getExpectedCompletionTime());
//                        System.out.println("Số lượng khách: " + selectedOrder.getNumberOfCustomer());
//                        System.out.println("Tiền đặt cọc: " + selectedOrder.getDeposit());
//                        System.out.println("Tổng giá: " + selectedOrder.getTotalPrice());
//                        System.out.println("Tổng giảm giá: " + selectedOrder.getTotalDiscount());
//                        System.out.println("Tổng tiền thanh toán: " + selectedOrder.getTotalPaid());
//                        System.out.println("Trạng thái đơn hàng: " + selectedOrder.getOrderStatus());
//                        System.out.println("Loại đơn hàng: " + selectedOrder.getOrderType());
//                        System.out.println("Phương thức thanh toán: " + selectedOrder.getPaymentMethod());
//                        System.out.println("Trạng thái thanh toán: " + selectedOrder.getPaymentStatus());
//                        System.out.println("Trạng thái đặt chỗ: " + selectedOrder.getReservationStatus());
//
//                        // Hiển thị chi tiết các OrderDetailEntity
//                        System.out.println("Danh sách chi tiết đơn hàng:");
//                        if (selectedOrder.getOrderDetails() != null && !selectedOrder.getOrderDetails().isEmpty()) {
//                            selectedOrder.getOrderDetails().forEach(detail -> {
//                                try {
//                                    System.out.println("  - Item: " + detail.getItem().getName() +
//                                            ", Topping: " + detail.getTopping().getName() +
//                                            ", Số lượng: " + detail.getQuantity() +
//                                            ", Thành tiền: " + detail.getLineTotal() +
//                                            ", Giảm giá: " + detail.getDiscount() +
//                                            ", Mô tả: " + detail.getDescription());
//                                } catch (Exception e) {
//                                    System.out.println("  - Chi tiết không đầy đủ hoặc lỗi khi tải dữ liệu.");
//                                }
//                            });
//                        } else {
//                            System.out.println("Không có chi tiết đơn hàng nào.");
//                        }
//                    } catch (Exception e) {
//                        System.out.println("Lỗi khi tải thông tin chi tiết đơn hàng: " + e.getMessage());
//                    }
//                }
//                break;
//            }
//            default:
//                System.out.println("Lựa chọn không hợp lệ.");
//                break;
//        }
//    }
//
//    private static void updateEntity() {
//        System.out.println("\nChọn loại Entity để cập nhật:");
//        printEntityOptions();
//        int entityChoice = getChoice();
//
//        switch (entityChoice) {
//            case 1: {
//                System.out.print("Nhập ID danh mục cần cập nhật (Gợi ý: C0001): ");
//                String categoryId = sc.nextLine().trim();
//
//                CategoryEntity category = generator.getCategoryDAL().findById(categoryId);
//                if (category == null) {
//                    System.out.println("Không tìm thấy danh mục với ID: " + categoryId);
//                    break;
//                }
//
//                System.out.println("Thông tin hiện tại:");
//                System.out.println("Tên danh mục: " + category.getName());
//                System.out.println("Mô tả danh mục: " + category.getDescription());
//
//                System.out.print("Nhập tên danh mục mới (hoặc nhấn Enter để giữ nguyên): ");
//                String newName = sc.nextLine().trim();
//                if (!newName.isEmpty()) {
//                    try {
//                        category.setName(newName);
//                    } catch (Exception e) {
//                        System.out.println("Lỗi: " + e.getMessage());
//                        break;
//                    }
//                }
//
//                System.out.print("Nhập mô tả danh mục mới (hoặc nhấn Enter để giữ nguyên): ");
//                String newDescription = sc.nextLine().trim();
//                if (!newDescription.isEmpty()) {
//                    category.setDescription(newDescription);
//                }
//
//                System.out.print("Danh mục còn hoạt động? (true/false, nhấn Enter để giữ nguyên): ");
//                String newActive = sc.nextLine().trim();
//                if (!newActive.isEmpty()) {
//                    category.setActive(Boolean.parseBoolean(newActive));
//                }
//
//                String result = generator.getCategoryDAL().update(category) ? "Cập nhật danh mục thành công: " + category : "Cập nhật danh mục thất bại!";
//                System.out.println(result);
//                break;
//            }
//            case 2: {
//                System.out.print("Nhập ID topping cần cập nhật (gợi ý: T0001): ");
//                String toppingId = sc.nextLine().trim();
//
//                ToppingEntity topping = generator.getToppingDAL().findById(toppingId);
//                if (topping == null) {
//                    System.out.println("Không tìm thấy topping với ID: " + toppingId);
//                    break;
//                }
//
//                System.out.println("Thông tin hiện tại:");
//                System.out.println("Tên topping: " + topping.getName());
//                System.out.println("Giá gốc: " + topping.getCostPrice());
//                System.out.println("Số lượng tồn kho: " + topping.getStockQuantity());
//                System.out.println("Mô tả topping: " + topping.getDescription());
//
//                System.out.print("Nhập tên topping mới (hoặc nhấn Enter để giữ nguyên): ");
//                String newName = sc.nextLine().trim();
//                if (!newName.isEmpty()) {
//                    try {
//                        topping.setName(newName);
//                    } catch (Exception e) {
//                        System.out.println("Lỗi: " + e.getMessage());
//                        break;
//                    }
//                }
//
//                topping.setCostPrice(getDoubleInput("Nhập giá gốc mới", topping.getCostPrice()));
//                topping.setStockQuantity(getIntInput("Nhập số lượng tồn kho mới", topping.getStockQuantity()));
//
//                System.out.print("Nhập mô tả mới (hoặc nhấn Enter để giữ nguyên): ");
//                String newDescription = sc.nextLine().trim();
//                if (!newDescription.isEmpty()) {
//                    topping.setDescription(newDescription);
//                }
//
//                System.out.print("Topping còn hoạt động? (true/false, nhấn Enter để giữ nguyên): ");
//                String newActive = sc.nextLine().trim();
//                if (!newActive.isEmpty()) {
//                    topping.setActive(Boolean.parseBoolean(newActive));
//                }
//
//                String result = generator.getToppingDAL().update(topping) ? "Cập nhật topping thành công: " + topping : "Cập nhật topping thất bại!";
//                System.out.println(result);
//                break;
//            }
//            case 3: {
//                System.out.print("Nhập ID sản phẩm cần cập nhật (gợi ý: I0001): ");
//                String itemId = sc.nextLine().trim();
//
//                ItemEntity item = generator.getItemDAL().findById(itemId);
//                if (item == null) {
//                    System.out.println("Không tìm thấy sản phẩm với ID: " + itemId);
//                    break;
//                }
//
//                System.out.println("Thông tin hiện tại:");
//                System.out.println("Tên sản phẩm: " + item.getName());
//                System.out.println("Giá gốc: " + item.getCostPrice());
//                System.out.println("Số lượng tồn kho: " + item.getStockQuantity());
//                System.out.println("Mô tả sản phẩm: " + item.getDescription());
//                System.out.println("Kích thước: " + (item.getSize() != null ? item.getSize() : "Không có"));
//
//                System.out.print("Nhập tên sản phẩm mới (hoặc nhấn Enter để giữ nguyên): ");
//                String newName = sc.nextLine().trim();
//                if (!newName.isEmpty()) {
//                    try {
//                        item.setName(newName);
//                    } catch (Exception e) {
//                        System.out.println("Lỗi: " + e.getMessage());
//                        break;
//                    }
//                }
//
//                item.setCostPrice(getDoubleInput("Nhập giá gốc mới", item.getCostPrice()));
//                item.setStockQuantity(getIntInput("Nhập số lượng tồn kho mới", item.getStockQuantity()));
//
//                System.out.print("Nhập mô tả mới (hoặc nhấn Enter để giữ nguyên): ");
//                String newDescription = sc.nextLine().trim();
//                if (!newDescription.isEmpty()) {
//                    item.setDescription(newDescription);
//                }
//
//                System.out.println("Chọn kích thước sản phẩm mới (hoặc nhấn Enter để giữ nguyên):");
//                System.out.println("1. SMALL");
//                System.out.println("2. MEDIUM");
//                System.out.println("3. LARGE");
//                System.out.println("4. Không thay đổi kích thước");
//                System.out.print("Nhập lựa chọn (1-4): ");
//                String sizeChoice = sc.nextLine().trim();
//
//                switch (sizeChoice) {
//                    case "1":
//                        item.setSize(SizeEnum.SMALL);
//                        break;
//                    case "2":
//                        item.setSize(SizeEnum.MEDIUM);
//                        break;
//                    case "3":
//                        item.setSize(SizeEnum.LARGE);
//                        break;
//                    case "4":
//                        break;
//                    default:
//                        System.out.println("Lựa chọn không hợp lệ! Kích thước không thay đổi.");
//                        break;
//                }
//
//                System.out.print("Sản phẩm còn hoạt động? (true/false, nhấn Enter để giữ nguyên): ");
//                String newActive = sc.nextLine().trim();
//                if (!newActive.isEmpty()) {
//                    item.setActive(Boolean.parseBoolean(newActive));
//                }
//
//                String result = generator.getItemDAL().update(item) ? "Cập nhật sản phẩm thành công: " + item : "Cập nhật sản phẩm thất bại!";
//                System.out.println(result);
//                break;
//            }
//            case 4: {
//                System.out.print("Nhập ID sản phẩm (gợi ý: I0001): ");
//                String itemId = sc.nextLine().trim();
//                ItemEntity item = generator.getItemDAL().findById(itemId);
//                if (item == null) {
//                    System.out.println("Không tìm thấy sản phẩm với ID: " + itemId);
//                    break;
//                }
//
//                System.out.print("Nhập ID topping (gợi ý T0001): ");
//                String toppingId = sc.nextLine().trim();
//                ToppingEntity topping = generator.getToppingDAL().findById(toppingId);
//                if (topping == null) {
//                    System.out.println("Không tìm thấy topping với ID: " + toppingId);
//                    break;
//                }
//
//                ItemToppingEntity itemTopping = generator.getItemToppingDAL().findByItemAndTopping(item, topping);
//                if (itemTopping == null) {
//                    System.out.println("Không tìm thấy ItemToppingEntity với sản phẩm và topping đã nhập.");
//                    break;
//                }
//
//                System.out.println("Thông tin hiện tại:");
//                System.out.println("Giá bán hiện tại: " + itemTopping.getSellingPrice());
//
//                itemTopping.setSellingPrice(getDoubleInput("Nhập giá bán mới: "));
//
//                String result = generator.getItemToppingDAL().update(itemTopping) ? "Cập nhật ItemTopping thành công: " + itemTopping : "Cập nhật ItemTopping thất bại!";
//                System.out.println(result);
//                break;
//            }
//            case 5: {
//                EmployeeEntity employeeSuggestion = generator.getEmployeeDAL().findAll().stream().findFirst().orElse(null);
//                System.out.print("Nhập ID nhân viên cần cập nhật (Gợi ý: " + employeeSuggestion.getEmployeeId() + "): ");
//                String employeeId = sc.nextLine().trim();
//                EmployeeEntity employee = generator.getEmployeeDAL().findById(employeeId).orElse(null);
//                if (employee == null) {
//                    System.out.println("Không tìm thấy nhân viên với ID: " + employeeId);
//                    return;
//                }
//
//                // Hiển thị thông tin hiện tại
//                System.out.println("Thông tin hiện tại:");
//                System.out.println("Họ tên: " + employee.getFullname());
//                System.out.println("Số điện thoại: " + employee.getPhoneNumber());
//                System.out.println("Email: " + employee.getEmail());
//                System.out.println("Địa chỉ: " + employee.getAddress());
//                System.out.println("Trạng thái hoạt động: " + (employee.isActive() ? "Hoạt động" : "Không hoạt động"));
//                System.out.println("Chức vụ (Role): " + employee.getRole().getRoleName());
//
//                System.out.print("Nhập họ tên mới (nhấn Enter để giữ nguyên): ");
//                String newFullname = sc.nextLine().trim();
//                if (!newFullname.isEmpty()) {
//                    employee.setFullname(newFullname);
//                }
//
//                System.out.print("Nhập số điện thoại mới (nhấn Enter để giữ nguyên): ");
//                String newPhoneNumber = sc.nextLine().trim();
//                if (!newPhoneNumber.isEmpty()) {
//                    employee.setPhoneNumber(newPhoneNumber);
//                }
//
//                System.out.print("Nhập email mới (nhấn Enter để giữ nguyên): ");
//                String newEmail = sc.nextLine().trim();
//                if (!newEmail.isEmpty()) {
//                    employee.setEmail(newEmail);
//                }
//
//                System.out.print("Nhập địa chỉ mới (nhấn Enter để giữ nguyên): ");
//                String newAddress = sc.nextLine().trim();
//                if (!newAddress.isEmpty()) {
//                    System.out.println("Nhập địa chỉ: ");
//                    System.out.println("\tNhập phố: ");
//                    String street = sc.nextLine();
//
//                    System.out.println("\tNhập phường: ");
//                    String ward = sc.nextLine();
//
//                    System.out.println("\tNhập đường: ");
//                    String district = sc.nextLine();
//
//                    System.out.println("\tNhập thành phố: ");
//                    String city = sc.nextLine();
//
//                    Address address = new Address();
//
//                    address.setCity(city);
//                    address.setStreet(street);
//                    address.setDistrict(district);
//                    address.setWard(ward);
//                    employee.setAddress(address);
//                }
//
//                // Cập nhật trạng thái hoạt động
//                System.out.print("Nhân viên còn hoạt động? (true/false, nhấn Enter để giữ nguyên): ");
//                String newActive = sc.nextLine().trim();
//                if (!newActive.isEmpty()) {
//                    employee.setActive(Boolean.parseBoolean(newActive));
//                }
//
//                // Cập nhật quyền (Role)
//                System.out.print("Nhập ID chức vụ mới (nhấn Enter để giữ nguyên): ");
//                String newRoleId = sc.nextLine().trim();
//                if (!newRoleId.isEmpty()) {
//                    RoleEntity newRole = generator.getRoleDAL().findById(newRoleId).orElse(null);
//                    if (newRole != null) {
//                        employee.setRole(newRole);
//                    } else {
//                        System.out.println("Không tìm thấy chức vụ với ID: " + newRoleId);
//                    }
//                }
//
//                // Lưu thông tin cập nhật
//                boolean updated = generator.getEmployeeDAL().update(employee);
//                if (updated) {
//                    System.out.println("Cập nhật thông tin nhân viên thành công: " + employee);
//                } else {
//                    System.out.println("Cập nhật thông tin nhân viên thất bại!");
//                }
//                break;
//            }
//            case 6: {
//                RoleEntity roleSuggestion = generator.getRoleDAL().findAll().stream().findFirst().orElse(null);
//                System.out.print("Nhập ID chức vụ cần cập nhật (gợi ý:" + roleSuggestion.getRoleId() + "): ");
//                String roleId = sc.nextLine().trim();
//                RoleEntity role = generator.getRoleDAL().findById(roleId).orElse(null);
//                if (role == null) {
//                    System.out.println("Không tìm thấy chức vụ với ID: " + roleId);
//                    break;
//                }
//                System.out.print("Nhập tên chức vụ mới (nhấn Enter nếu muốn giữ nguyên): ");
//                String roleName = sc.nextLine().trim();
//                if (!roleName.isEmpty()) {
//                    role.setRoleName(roleName);
//                }
//
//                boolean roleUpdated = generator.getRoleDAL().update(role);
//                if (roleUpdated) {
//                    System.out.println("Cập nhật chức vụ thành công: " + role);
//                } else {
//                    System.out.println("Cập nhật chức vụ thất bại!");
//                }
//                break;
//            }
//            case 7: {
//                PromotionEntity promotionSuggestion = generator.getPromotionDAL().findAll().stream().findFirst().orElse(null);
//                System.out.print("Nhập ID khuyến mãi cần cập nhật (Gợi ý:" + promotionSuggestion.getPromotionId() + "): ");
//                String promotionId = sc.nextLine().trim();
//                PromotionEntity promotion = generator.getPromotionDAL().findById(promotionId).orElse(null);
//                if (promotion == null) {
//                    System.out.println("Không tìm thấy khuyến mãi với ID: " + promotionId);
//                    return;
//                }
//
//                // Hiển thị thông tin hiện tại
//                System.out.println("Thông tin hiện tại:");
//                System.out.println("Loại khuyến mãi: " + promotion.getPromotionType());
//                System.out.println("Cấp độ khách hàng: " + promotion.getCustomerLevels());
//                System.out.println("Mô tả: " + promotion.getDescription());
//                System.out.println("Phần trăm giảm giá: " + promotion.getDiscountPercentage());
//                System.out.println("Ngày bắt đầu: " + promotion.getStartedDate());
//                System.out.println("Ngày kết thúc: " + promotion.getEndedDate());
//                System.out.println("Trạng thái hoạt động: " + (promotion.isActive() ? "Hoạt động" : "Không hoạt động"));
//                System.out.println("Giá tối thiểu: " + promotion.getMinPrice());
//
//                // Cập nhật mô tả
//                System.out.print("Nhập mô tả mới (nhấn Enter để giữ nguyên): ");
//                String newDescription = sc.nextLine().trim();
//                if (!newDescription.isEmpty()) {
//                    promotion.setDescription(newDescription);
//                }
//
//                // Cập nhật phần trăm giảm giá
//                System.out.print("Nhập phần trăm giảm giá mới (nhấn Enter để giữ nguyên): ");
//                String newDiscount = sc.nextLine().trim();
//                if (!newDiscount.isEmpty()) {
//                    try {
//                        promotion.setDiscountPercentage(Double.parseDouble(newDiscount));
//                    } catch (NumberFormatException e) {
//                        System.out.println("Phần trăm giảm giá không hợp lệ. Giữ nguyên giá trị cũ.");
//                    }
//                }
//
//                // Cập nhật ngày bắt đầu
//                LocalDate newStartDate = getDateInput("Nhập ngày bắt đầu mới (nhấn Enter để giữ nguyên):", promotion.getStartedDate());
//                if (newStartDate != null) {
//                    promotion.setStartedDate(newStartDate);
//                }
//
//                // Cập nhật ngày kết thúc
//                LocalDate newEndDate = getDateInput("Nhập ngày kết thúc mới (nhấn Enter để giữ nguyên): ", promotion.getEndedDate());
//                if (newEndDate != null) {
//                    promotion.setEndedDate(newEndDate);
//                }
//
//                // Cập nhật trạng thái hoạt động
//                System.out.print("Khuyến mãi còn hoạt động? (true/false, nhấn Enter để giữ nguyên): ");
//                String newPromotionActive = sc.nextLine().trim();
//                if (!newPromotionActive.isEmpty()) {
//                    promotion.setActive(Boolean.parseBoolean(newPromotionActive));
//                }
//
//                // Cập nhật cấp độ khách hàng
//                List<CustomerLevelEnum> customerLevels = new ArrayList<>();
//                int numOfCustomerLevel;
//                System.out.print("Nhập số lượng cấp bậc khách hàng(Nhấn enter để giữ nguyên): ");
//                while (true) {
//                    try {
//                        if(sc.nextLine().trim().isEmpty()) {
//                            break;
//                        }
//                        numOfCustomerLevel =  Integer.parseInt(sc.nextLine().trim());
//                        for (int i = 0;
//                             i < numOfCustomerLevel;
//                             i++) {
//                            System.out.println("Chọn cấp độ khách hàng:");
//                            System.out.println("1. NEW");
//                            System.out.println("2. POTENTIAL");
//                            System.out.println("3. VIP");
//
//                            System.out.print("Nhập lựa chọn (1 -> 3): ");
//                            String customerLevelChoice = sc.nextLine().trim();
//                            CustomerLevelEnum customerLevelType = null;
//
//                            switch (customerLevelChoice) {
//                                case "1":
//                                    customerLevelType = CustomerLevelEnum.NEW;
//                                    break;
//                                case "2":
//                                    customerLevelType = CustomerLevelEnum.POTENTIAL;
//                                    break;
//                                case "3":
//                                    customerLevelType = CustomerLevelEnum.VIP;
//                                    break;
//                                default:
//                                    System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập từ 1 -> 3.");
//                                    break;
//                            }
//                            customerLevels.add(customerLevelType);
//                        }
//                        promotion.setCustomerLevels(customerLevels);
//                    } catch (NumberFormatException e) {
//                        System.out.println("Vui lòng nhập một số nguyên hợp lệ.");
//                    }
//                }
//
//
//                // Cập nhật giá tối thiểu
//                System.out.print("Nhập giá tối thiểu mới (nhấn Enter để giữ nguyên): ");
//                String newMinPrice = sc.nextLine().trim();
//                if (!newMinPrice.isEmpty()) {
//                    try {
//                        promotion.setMinPrice(Double.parseDouble(newMinPrice));
//                    } catch (NumberFormatException e) {
//                        System.out.println("Giá tối thiểu không hợp lệ. Giữ nguyên giá trị cũ.");
//                    }
//                }
//
//                // Lưu thông tin cập nhật
//                boolean promotionUpdated = generator.getPromotionDAL().update(promotion);
//                if (promotionUpdated) {
//                    System.out.println("Cập nhật thông tin khuyến mãi thành công: " + promotion);
//                } else {
//                    System.out.println("Cập nhật thông tin khuyến mãi thất bại!");
//                }
//                break;
//            }
//            case 8: {
//                PromotionDetailEntity promotionDetailEntity = generator.getPromotionDetailDAL().findAll().stream().findFirst().get();
//
//                System.out.print("Nhập ID khuyến mãi của chi tiết khuyến mãi (Gợi ý:" + promotionDetailEntity.getPromotion().getPromotionId() + "):");
//                String pdPromotionId = sc.nextLine().trim();
//
//                System.out.print("Nhập ID món ăn của chi tiết khuyến mãi(Gợi ý:" + promotionDetailEntity.getItem().getItemId() + "):");
//                String pdItemId = sc.nextLine().trim();
//
//                // Tìm PromotionDetailEntity theo ID
//                PromotionDetailEntity promotionDetail = generator.getPromotionDetailDAL().findByPromotionAndItem(pdPromotionId, pdItemId);
//                if (promotionDetail == null) {
//                    System.out.println("Không tìm thấy khuyến mãi chi tiết với ID khuyến mãi: " + pdPromotionId + " ID vật phẩm: " + pdItemId);
//                    return;
//                }
//
//                // Hiển thị thông tin hiện tại
//                System.out.println("Thông tin hiện tại:");
//                System.out.println("Mã khuyến mãi: " + promotionDetail.getPromotion().getPromotionId());
//                System.out.println("Mã món: " + promotionDetail.getItem().getItemId());
//                // Lưu thông tin cập nhật
//                System.out.println("Nhập mã món ăn mới: ");
//                String newItemId = sc.nextLine().trim();
//                ItemEntity newItem = generator.getItemDAL().findById(newItemId);
//                if (newItem != null) {
//                    PromotionDetailEntity promotionDetailEntityNew = new PromotionDetailEntity();
//                    promotionDetailEntityNew.setPromotion(promotionDetail.getPromotion());
//                    promotionDetailEntityNew.setItem(newItem);
//
//                    boolean pdDeleteOld = generator.getPromotionDetailDAL().deleteByItemAndPromotion(promotionDetail.getItem(), promotionDetail.getPromotion());
//                    boolean pdAddNew = generator.getPromotionDetailDAL().insert(promotionDetailEntityNew);
//                    if (pdDeleteOld && pdAddNew) {
//                        System.out.println("Cập nhật thông tin khuyến mãi chi tiết thành công: " + promotionDetail);
//                    } else {
//                        System.out.println("Cập nhật thông tin khuyến mãi chi tiết thất bại!");
//                    }
//                } else {
//                    System.out.println("Id rỗng");
//                }
//
//                break;
//            }
//            case 9:
//                System.out.println("Đang cập nhật CustomerEntity...");
//
//                // Nhập Customer ID để tìm kiếm khách hàng
//                CustomerEntity temp = generator.getCustomerDAL().findAll().stream().findFirst().orElse(null);
//                if (temp != null) {
//                    System.out.println("Gợi ý ID: " + temp.getCustomerId());
//                }
//                System.out.print("Nhập ID khách hàng (Customer ID): ");
//                String customerId = sc.nextLine().trim();
//
//                // Tìm khách hàng bằng Customer ID
//                Optional<CustomerEntity> optionalCustomer = generator.getCustomerDAL().findById(customerId);
//                if (optionalCustomer.isPresent()) {
//                    CustomerEntity customer = optionalCustomer.get();
//
//                    // Nhập thông tin mới
//                    System.out.print("Nhập tên khách hàng mới (nhấn Enter nếu không thay đổi): ");
//                    String name = sc.nextLine().trim();
//                    if (!name.isEmpty()) {
//                        customer.setName(name);
//                    }
//
//                    System.out.print("Nhập email mới (nhấn Enter nếu không thay đổi): ");
//                    String email = sc.nextLine().trim();
//                    if (!email.isEmpty()) {
//                        customer.setEmail(email);
//                    }
//
//                    System.out.print("Nhập số điện thoại mới (nhấn Enter nếu không thay đổi): ");
//                    String phone = sc.nextLine().trim();
//                    if (!phone.isEmpty()) {
//                        customer.setPhone(phone);
//                    }
//
//                    // Nhập ngày sinh mới (nếu có thay đổi)
//                    System.out.print("Nhập ngày sinh mới (yyyy-MM-dd) hoặc nhấn Enter nếu không thay đổi: ");
//                    String dobInput = sc.nextLine().trim();
//                    if (!dobInput.isEmpty()) {
//                        try {
//                            customer.setDayOfBirth(LocalDateTime.parse(dobInput + "T00:00:00"));
//                        } catch (Exception e) {
//                            System.out.println("Ngày sinh không hợp lệ. Sử dụng giá trị mặc định.");
//                        }
//                    }
//
//                    // Nhập thông tin địa chỉ mới
//                    System.out.print("Nhập địa chỉ mới (đường, phường, quận, thành phố) hoặc nhấn Enter nếu không thay đổi: ");
//                    System.out.print(" - Đường (street): ");
//                    String street = sc.nextLine().trim();
//                    if (!street.isEmpty()) {
//                        customer.getAddress().setStreet(street);
//                    }
//
//                    System.out.print(" - Phường (ward): ");
//                    String ward = sc.nextLine().trim();
//                    if (!ward.isEmpty()) {
//                        customer.getAddress().setWard(ward);
//                    }
//
//                    System.out.print(" - Quận (district): ");
//                    String district = sc.nextLine().trim();
//                    if (!district.isEmpty()) {
//                        customer.getAddress().setDistrict(district);
//                    }
//
//                    System.out.print(" - Thành phố (city): ");
//                    String city = sc.nextLine().trim();
//                    if (!city.isEmpty()) {
//                        customer.getAddress().setCity(city);
//                    }
//
//                    if (generator.getCustomerDAL().update(customer)) {
//                        System.out.println("Cập nhật khách hàng thành công!");
//                        System.out.println("Sau khi cập nhật: " + customer);
//                    } else {
//                        System.out.println("Cập nhật khách hàng thất bại!");
//                    }
//                } else {
//                    System.out.println("Không tìm thấy khách hàng với ID này.");
//                }
//                break;
//            case 10:
//                System.out.println("Đang cập nhật FloorEntity...");
//
//                // Nhập Floor ID để tìm kiếm tầng
//                FloorEntity temp1 = generator.getFloorDAL().findAll().stream().findFirst().orElse(null);
//                if (temp1 != null) {
//                    System.out.println("Gợi ý ID: " + temp1.getFloorId());
//                }
//                System.out.print("Nhập ID tầng (Floor ID): ");
//                String floorId = sc.nextLine().trim();
//
//                // Tìm tầng bằng Floor ID
//                Optional<FloorEntity> optionalFloor = generator.getFloorDAL().findById(floorId);
//                if (optionalFloor.isPresent()) {
//                    FloorEntity floor = optionalFloor.get();
//
//                    // Nhập tên tầng mới (hoặc giữ nguyên nếu không thay đổi)
//                    System.out.print("Nhập tên tầng mới (nhấn Enter nếu không thay đổi): ");
//                    String name = sc.nextLine().trim();
//                    if (!name.isEmpty()) {
//                        floor.setName(name);
//                    }
//
//                    // Nhập sức chứa mới (hoặc giữ nguyên nếu không thay đổi)
//                    System.out.print("Nhập sức chứa mới (nhấn Enter nếu không thay đổi): ");
//                    String capacityInput = sc.nextLine().trim();
//                    if (!capacityInput.isEmpty()) {
//                        try {
//                            int capacity = Integer.parseInt(capacityInput);
//                            floor.setCapacity(capacity);
//                        } catch (NumberFormatException e) {
//                            System.out.println("Sức chứa không hợp lệ.");
//                        }
//                    }
//
//                    if (generator.getFloorDAL().update(floor)) {
//                        System.out.println("Cập nhật tầng thành công!");
//                        System.out.println("Sau khi cập nhật: " + floor);
//                    } else {
//                        System.out.println("Cập nhật tầng thất bại!");
//                    }
//                } else {
//                    System.out.println("Không tìm thấy tầng với ID này.");
//                }
//                break;
//
//            case 11:
//                System.out.println("Đang cập nhật TableEntity...");
//
//                // Nhập Table ID để tìm kiếm bàn
//                TableEntity temp2 = generator.getTableDAL().findAll().stream().findFirst().orElse(null);
//                if (temp2 != null) {
//                    System.out.println("Gợi ý ID: " + temp2.getTableId());
//                }
//                System.out.print("Nhập ID bàn (Table ID): ");
//                String tableId = sc.nextLine().trim();
//
//                // Tìm bàn bằng Table ID
//                Optional<TableEntity> optionalTable = generator.getTableDAL().findById(tableId);
//                if (optionalTable.isPresent()) {
//                    TableEntity table = optionalTable.get();
//
//                    // Nhập sức chứa mới (hoặc giữ nguyên nếu không thay đổi)
//                    System.out.print("Nhập sức chứa mới (nhấn Enter nếu không thay đổi): ");
//                    String capacityInput = sc.nextLine().trim();
//                    if (!capacityInput.isEmpty()) {
//                        try {
//                            int capacity = Integer.parseInt(capacityInput);
//                            table.setCapacity(capacity);
//                        } catch (NumberFormatException e) {
//                            System.out.println("Sức chứa không hợp lệ.");
//                        }
//                    }
//
//                    // Nhập ghi chú mới (hoặc giữ nguyên nếu không thay đổi)
//                    System.out.print("Nhập tên bàn mới (nhấn Enter nếu không thay đổi): ");
//                    String name = sc.nextLine().trim();
//                    if (!name.isEmpty()) {
//                        table.setName(name);
//                    }
//
//                    // Nhập trạng thái bàn mới (AVAILABLE, OCCUPIED, RESERVED)
//                    System.out.print("Nhập trạng thái bàn mới    (AVAILABLE:1, OCCUPIED:2): ");
//                    int statusChoice = sc.nextInt();
//                    switch (statusChoice) {
//                        case 1:
//                            table.setTableStatus(TableStatusEnum.AVAILABLE);
//                            break;
//                        case 2:
//                            table.setTableStatus(TableStatusEnum.OCCUPIED);
//                            break;
//                        default:
//                            System.out.println("Lựa chọn không phù hợp! Không thay đổi status");
//                            break;
//                    }
//
//                    // Nhập ID tầng mới (nếu thay đổi)
//                    FloorEntity temp3 = generator.getFloorDAL().findAll().stream().findFirst().orElse(null);
//                    if (temp3 != null) {
//                        System.out.println("Gợi ý ID: " + temp3.getFloorId());
//                    }
//                    System.out.print("Nhập ID tầng mới (Floor ID) hoặc để trống nếu không thay đổi: ");
//                    sc.nextLine();
//                    String newFloorId = sc.nextLine();
//                    if (!newFloorId.isEmpty()) {
//                        FloorEntity newFloor = generator.getFloorDAL().findById(newFloorId).orElse(null);
//                        if (newFloor != null) {
//                            table.setFloor(newFloor);
//                        } else {
//                            System.out.println("Không tìm thấy tầng với ID này.");
//                        }
//                    }
//
//                    // Cập nhật thông tin vào cơ sở dữ liệu
//                    if (generator.getTableDAL().update(table)) {
//                        System.out.println("Cập nhật bàn thành công!");
//                        System.out.println("Sau khi cập nhật: " + table);
//                    } else {
//                        System.out.println("Cập nhật bàn thất bại!");
//                    }
//                } else {
//                    System.out.println("Không tìm thấy bàn với ID này.");
//                }
//                break;
//            case 12: {
//                List<OrderEntity> orders = orderDAL.findAll();
//                System.out.println("Đang cập nhật OrderEntity...");
//                System.out.println("Danh sách Orders: ");
//                try {
//                    orders.forEach(order -> {
//                        try {
//                            System.out.println(order.getOrderId() + " " + order.getCustomer().getName() + " " + order.getCustomer().getPhone());
//                        } catch (Exception e) {
//                            System.out.println(order.getOrderId());
//                        }
//                    });
//                } catch (Exception e) {
//                    orders.forEach(order -> System.out.println(order.getOrderId()));
//                }
//
//                // Chọn OrderEntity cần cập nhật
//                System.out.println("Nhập Order ID cần cập nhật:");
//                String orderId = sc.nextLine();
//
//                // Tìm OrderEntity theo ID
//                OrderEntity order = orders.stream()
//                        .filter(o -> o.getOrderId().equals(orderId))
//                        .findFirst()
//                        .orElse(null);
//
//                if (order == null) {
//                    System.out.println("Không tìm thấy Order với ID: " + orderId);
//                    return;
//                }
//
//                System.out.println("Cập nhật thông tin OrderEntity...");
//
//                // Cập nhật thông tin OrderEntity
//                boolean flag;
//                try {
//                    System.out.println("Thời gian cũ " + order.getReservationTime() + " - Nhập thời gian đặt chỗ mới (dd-MM-yyyy HH:mm):");
//                    LocalDateTime reservationTime = LocalDateTime.parse(sc.nextLine(), formatter);
//                    order.setReservationTime(reservationTime);
//                    flag = true;
//                } catch (Exception e) {
//                    System.out.println("=> Không chỉnh sửa!");
//                    flag = false;
//                }
//
//                try {
//                    System.out.println("Thời gian cũ " + order.getExpectedCompletionTime() + " - Nhập thời gian hoàn thành dự kiến mới (dd-MM-yyyy HH:mm):");
//                    LocalDateTime expectedCompletionTime = LocalDateTime.parse(sc.nextLine(), formatter);
//                    order.setExpectedCompletionTime(expectedCompletionTime);
//                    flag = true;
//                } catch (Exception e) {
//                    System.out.println("=> Không chỉnh sửa!");
//                    flag = false;
//                }
//
//                System.out.println("Nhập số lượng khách mới:");
//                try {
//                    order.setNumberOfCustomer(Integer.parseInt(sc.nextLine()));
//                } catch (Exception e) {
//                    System.out.println("=> Không chỉnh sửa!");
//                }
//
//                System.out.println("Nhập tiền đặt cọc mới:");
//                try {
//                    order.setDeposit(Double.parseDouble(sc.nextLine()));
//                } catch (Exception e) {
//                    System.out.println("=> Không chỉnh sửa!");
//                }
//
//                OrderStatusEnum orderStatus;
//                try {
//                    System.out.println("Nhập trạng thái đơn hàng mới (SINGLE, MERGE):");
//                    orderStatus = OrderStatusEnum.valueOf(sc.nextLine().toUpperCase());
//                    order.setOrderStatus(orderStatus);
//                } catch (Exception e) {
//                    System.out.println("=> Không chỉnh sửa!");
//                }
//
//                OrderTypeEnum orderType;
//                try {
//                    System.out.println("Nhập loại đơn hàng mới (ADVANCE, IMMEDIATE):");
//                    orderType = OrderTypeEnum.valueOf(sc.nextLine().toUpperCase());
//                    order.setOrderType(orderType);
//                } catch (Exception e) {
//                    System.out.println("=> Không chỉnh sửa!");
//                }
//
//                PaymentMethodEnum paymentMethod;
//                try {
//                    System.out.println("Nhập phương thức thanh toán mới (CASH, CREDIT_CARD, E_WALLET):");
//                    paymentMethod = PaymentMethodEnum.valueOf(sc.nextLine().toUpperCase());
//                    order.setPaymentMethod(paymentMethod);
//                } catch (Exception e) {
//                    System.out.println("=> Không chỉnh sửa!");
//                }
//
//                PaymentStatusEnum paymentStatus;
//                try {
//                    System.out.println("Nhập trạng thái thanh toán mới (UNPAID, PAID):");
//                    paymentStatus = PaymentStatusEnum.valueOf(sc.nextLine().toUpperCase());
//                    order.setPaymentStatus(paymentStatus);
//                } catch (Exception e) {
//                    System.out.println("=> Không chỉnh sửa!");
//                }
//
//                ReservationStatusEnum reservationStatus;
//                try {
//                    System.out.println("Nhập trạng thái đặt chỗ mới (PENDING, RECEIVED, CANCELLED):");
//                    reservationStatus = ReservationStatusEnum.valueOf(sc.nextLine().toUpperCase());
//                    order.setReservationStatus(reservationStatus);
//                } catch (Exception e) {
//                    System.out.println("Trạng thái đặt chỗ không hợp lệ, vui lòng thử lại.");
//                    flag = false;
//                }
//
//                // Ghi thay đổi vào cơ sở dữ liệu
//                orderDAL.update(order);
//
//                // Hiển thị thông tin OrderEntity đã cập nhật
//                System.out.println("OrderEntity sau khi cập nhật:");
//                System.out.println(order);
//                break;
//            }
//            default:
//                System.out.println("Lựa chọn không hợp lệ.");
//                break;
//        }
//    }
//
//    private static void deleteEntity() {
//        System.out.println("\nChọn loại Entity để xóa:");
//        printEntityOptions();
//        int entityChoice = getChoice();
//
//        switch (entityChoice) {
//            case 1: {
//                System.out.print("Nhập ID Category cần xóa (Gợi ý: C0001): ");
//                String categoryId = sc.nextLine().trim();
//
//                CategoryEntity category = generator.getCategoryDAL().findById(categoryId);
//                if (category == null) {
//                    System.out.println("Không tìm thấy Category với ID: " + categoryId);
//                    break;
//                }
//                printDeleteOptions();
//                int deleteChoice = getChoice();
//                switch (deleteChoice) {
//                    case 1: {
//                        category.setActive(false);
//                        generator.getCategoryDAL().update(category);
//                        System.out.println("Xoá mềm Category thành công " + category);
//                        break;
//                    }
//                    case 2: {
//                        List<ItemEntity> itemsToDelete = generator.getItemDAL().findByCategory(category);
//                        itemsToDelete.forEach(item -> {
//                            System.out.println("Xóa Item: " + item.getItemId() + " - " + item.getName());
//
//                            generator.getItemToppingDAL().deleteByItemAndTopping(item, null);
//                            System.out.println("-> Xóa ItemTopping liên quan đến Item " + item.getItemId());
//
//                            generator.getPromotionDetailDAL().deleteByItemAndPromotion(item, null);
//                            System.out.println("-> Xóa PromotionDetail liên quan đến Item " + item.getItemId());
//
//                            generator.getOrderDetailDAL().deleteByItemAndTopping(item, null);
//                            System.out.println("-> Xóa OrderDetail liên quan đến Item " + item.getItemId());
//
//                            generator.getItemDAL().deleteById(item.getItemId());
//                            System.out.println("-> Xóa Item thành công: " + item.getItemId());
//                        });
//
//                        String result = generator.getCategoryDAL().deleteById(categoryId) ? "Xóa Category thành công: " + categoryId + " - " + category.getName() : "Xóa Category thất bại!";
//                        System.out.println(result);
//                        break;
//                    }
//                }
//                break;
//            }
//            case 2: {
//                System.out.print("Nhập ID Topping cần xóa (Gợi ý: T0001): ");
//                String toppingId = sc.nextLine().trim();
//
//                ToppingEntity topping = generator.getToppingDAL().findById(toppingId);
//                if (topping == null) {
//                    System.out.println("Không tìm thấy Topping với ID: " + toppingId);
//                    break;
//                }
//
//                printDeleteOptions();
//                int deleteChoice = getChoice();
//
//                switch (deleteChoice) {
//                    case 1: {
//                        topping.setActive(false);
//                        generator.getToppingDAL().update(topping);
//                        System.out.println("Xóa mềm Topping thành công: " + topping);
//                        break;
//                    }
//                    case 2: {
//                        List<ItemToppingEntity> itemToppingsToDelete = generator.getItemToppingDAL().findAll()
//                                .stream()
//                                .filter(it -> it.getTopping().equals(topping))
//                                .collect(Collectors.toList());
//                        itemToppingsToDelete.forEach(itemTopping -> {
//                            generator.getItemToppingDAL().deleteByItemAndTopping(null, topping);
//                            System.out.println("-> Xóa ItemTopping: " + itemTopping);
//                        });
//
//                        generator.getOrderDetailDAL().deleteByItemAndTopping(null, topping);
//                        System.out.println("-> Xóa OrderDetail liên quan đến Topping: " + toppingId);
//
//                        boolean deleted = generator.getToppingDAL().deleteById(toppingId);
//                        System.out.println(deleted ? "Xóa cứng Topping thành công: " + toppingId : "Xóa Topping thất bại!");
//                        break;
//                    }
//                    default:
//                        System.out.println("Lựa chọn không hợp lệ!");
//                        break;
//                }
//                break;
//            }
//            case 3: {
//                System.out.print("Nhập ID Item cần xóa (Gợi ý: I0001): ");
//                String itemId = sc.nextLine().trim();
//
//                ItemEntity item = generator.getItemDAL().findById(itemId);
//                if (item == null) {
//                    System.out.println("Không tìm thấy Item với ID: " + itemId);
//                    break;
//                }
//
//                printDeleteOptions();
//                int deleteChoice = getChoice();
//                switch (deleteChoice) {
//                    case 1: {
//                        item.setActive(false);
//                        generator.getItemDAL().update(item);
//                        System.out.println("Đã xóa mềm Item: " + item);
//                        break;
//                    }
//                    case 2: {
//                        generator.getItemToppingDAL().deleteByItemAndTopping(item, null);
//                        System.out.println("-> Xóa các ItemTopping liên quan đến Item: " + item.getItemId());
//
//                        generator.getPromotionDetailDAL().deleteByItemAndPromotion(item, null);
//                        System.out.println("-> Xóa các PromotionDetail liên quan đến Item: " + item.getItemId());
//
//                        generator.getOrderDetailDAL().deleteByItemAndTopping(item, null);
//                        System.out.println("-> Xóa các OrderDetail liên quan đến Item: " + item.getItemId());
//
//                        String result = generator.getItemDAL().deleteById(itemId)
//                                ? "Xóa thành công Item: " + item.getItemId() + " - " + item.getName()
//                                : "Xóa Item thất bại!";
//                        System.out.println(result);
//                        break;
//                    }
//                    default:
//                        System.out.println("Lựa chọn không hợp lệ.");
//                        break;
//                }
//                break;
//            }
//            case 4: {
//                System.out.println("Chọn cách xóa ItemTopping:");
//                System.out.println("1. Xóa theo Item");
//                System.out.println("2. Xóa theo Topping");
//                System.out.println("3. Xóa theo cả Item và Topping");
//                System.out.print("Lựa chọn: ");
//                int deleteOption = getChoice();
//
//                ItemEntity itemEntity = null;
//                ToppingEntity toppingEntity = null;
//
//                switch (deleteOption) {
//                    case 1:
//                        System.out.print("Nhập ID Item (gợi ý: I0001): ");
//                        String itemId = sc.nextLine().trim();
//                        itemEntity = generator.getItemDAL().findById(itemId);
//                        if (itemEntity == null) {
//                            System.out.println("Không tìm thấy Item với ID: " + itemId);
//                            return;
//                        }
//                        break;
//                    case 2:
//                        System.out.print("Nhập ID Topping (gợi ý: T0001): ");
//                        String toppingId = sc.nextLine().trim();
//                        toppingEntity = generator.getToppingDAL().findById(toppingId);
//                        if (toppingEntity == null) {
//                            System.out.println("Không tìm thấy Topping với ID: " + toppingId);
//                            return;
//                        }
//                        break;
//                    case 3:
//                        System.out.print("Nhập ID Item (gợi ý: I0001): ");
//                        itemId = sc.nextLine().trim();
//                        itemEntity = generator.getItemDAL().findById(itemId);
//                        if (itemEntity == null) {
//                            System.out.println("Không tìm thấy Item với ID: " + itemId);
//                            return;
//                        }
//
//                        System.out.print("Nhập ID Topping (gợi ý: T0001): ");
//                        toppingId = sc.nextLine().trim();
//                        toppingEntity = generator.getToppingDAL().findById(toppingId);
//                        if (toppingEntity == null) {
//                            System.out.println("Không tìm thấy Topping với ID: " + toppingId);
//                            return;
//                        }
//                        break;
//                    default:
//                        System.out.println("Lựa chọn không hợp lệ!");
//                        return;
//                }
//
//                boolean result = generator.getItemToppingDAL().deleteByItemAndTopping(itemEntity, toppingEntity);
//                if (result) {
//                    System.out.println("Xóa ItemTopping thành công!");
//                } else {
//                    System.out.println("Xóa ItemTopping thất bại!");
//                }
//                break;
//            }
//            case 5: {
//                EmployeeEntity empSuggestion = generator.getEmployeeDAL().findAll().stream().findFirst().orElse(null);
//                System.out.print("Nhập ID nhân viên (gợi ý: " + empSuggestion.getEmployeeId() + "): ");
//                String employeeId = sc.nextLine().trim();
//
//                EmployeeEntity employeeToDelete = generator.getEmployeeDAL().findById(employeeId).orElse(null);
//                if (employeeToDelete == null) {
//                    System.out.println("Không tìm thấy nhân viên với ID: " + employeeId);
//                    return;
//                }
//                generator.getOrderDAL().findAll().stream().filter(ord -> ord.getEmployee().getEmployeeId().equals(employeeId))
//                        .forEach(ord -> generator.getOrderDAL().deleteById(ord.getOrderId()));
//
//                employeeToDelete.setRole(null);
//                boolean employeeDeleteResult = generator.getEmployeeDAL().deleteById(employeeId);
//                if (employeeDeleteResult) {
//                    System.out.println("Xóa nhân viên thành công!");
//                } else {
//                    System.out.println("Xóa nhân viên thất bại!");
//                }
//                break;
//            }
//            case 6: {
//                RoleEntity roleSuggestion = generator.getRoleDAL().findAll().stream().findFirst().orElse(null);
//                System.out.print("Nhập ID chức vụ (gợi ý: " + roleSuggestion.getRoleId() + "): ");
//                String roleId = sc.nextLine().trim();
//                RoleEntity roleToDelete = generator.getRoleDAL().findById(roleId).orElse(null);
//                if (roleToDelete == null) {
//                    System.out.println("Không tìm thấy chức vụ với ID: " + roleId);
//                    return;
//                }
//
//
//                List<OrderEntity> orderEntities = generator.getOrderDAL().findAll();
//                List<EmployeeEntity> employees = em.createQuery(
//                                "SELECT e FROM EmployeeEntity e WHERE e.role.roleId = :roleId", EmployeeEntity.class)
//                        .setParameter("roleId", roleId)
//                        .getResultList();
//
//                for (EmployeeEntity employee : employees) {
//                    orderEntities.stream().filter(ord -> ord.getEmployee().getEmployeeId().equals(employee.getEmployeeId()))
//                            .forEach(ord -> generator.getOrderDAL().deleteById(ord.getOrderId()));
//                }
//
//                generator.getEmployeeDAL().deleteEmployeesByRole(roleId);
//                boolean result = generator.getRoleDAL().deleteById(roleId);
//                if (result) {
//                    System.out.println("Xóa chức vụ thành công!");
//                } else {
//                    System.out.println("Xóa chức vụ thất bại!");
//
//                }
//                break;
//            }
//            case 7: {
//                PromotionEntity promotionSuggestion = generator.getPromotionDAL().findAll().stream().findFirst().orElse(null);
//                System.out.print("Nhập ID khuyến mãi cần xoá (Gợi ý:" + promotionSuggestion.getPromotionId() + "): ");
//                String promotionToDeleteId = sc.nextLine().trim();
//                PromotionEntity promotionEntity = generator.getPromotionDAL().findById(promotionToDeleteId).orElse(null);
//                if (promotionEntity == null) {
//                    System.out.println("Không tìm thấy  khuyến mãi với ID: " + promotionEntity);
//                    return;
//                }
//
//                if (promotionEntity.getPromotionDetails() != null) {
//                    promotionEntity.getPromotionDetails().forEach(promotionDetail -> {
//                        ItemEntity itemToDelete = promotionDetail.getItem();
//                        PromotionEntity promotionToDelete = promotionDetail.getPromotion();
//                        boolean promotionDetailDeleteresult = generator.getPromotionDetailDAL().deleteByItemAndPromotion(itemToDelete, promotionToDelete);
//
//                        if (promotionDetailDeleteresult) {
//                        } else {
//                            System.out.println("Xóa chi tiết khuyến mãi thất bại!");
//                        }
//                    });
//                }
//
//                boolean promotionDetailDeleteresult = generator.getPromotionDAL().deleteById(promotionToDeleteId);
//
//                if (promotionDetailDeleteresult) {
//                    System.out.println("Xóa khuyến mãi thành công!");
//                } else {
//                    System.out.println("Xóa khuyến mãi thất bại!");
//                }
//
//                break;
//            }
//            case 8: {
//                PromotionDetailEntity promotionDetailEntity = generator.getPromotionDetailDAL().findAll().stream().findFirst().orElse(null);
//                System.out.print("Nhập ID khuyến mãi của chi tiết khuyến mãi (Gợi ý:" + promotionDetailEntity.getPromotion().getPromotionId() + "):");
//                String pToDeleteId = sc.nextLine().trim();
//                System.out.print("Nhập ID món ăn của chi tiết khuyến mãi(Gợi ý:" + promotionDetailEntity.getItem().getItemId() + "):");
//                String iToDeleteId = sc.nextLine().trim();
//
//                PromotionDetailEntity promotionDetailToDelete = generator.getPromotionDetailDAL().findByPromotionAndItem(pToDeleteId, iToDeleteId);
//                if (promotionDetailToDelete == null) {
//                    System.out.println("Không tìm thấy chi tiết khuyến mãi với ID khuyến mãi: " + pToDeleteId + " ID món ăn: " + iToDeleteId);
//                    return;
//                }
//
//                ItemEntity itemToDelete = promotionDetailToDelete.getItem();
//                PromotionEntity promotionToDelete = promotionDetailToDelete.getPromotion();
//                boolean promotionDetailDeleteResult = generator.getPromotionDetailDAL().deleteByItemAndPromotion(itemToDelete, promotionToDelete);
//
//                if (promotionDetailDeleteResult) {
//                    System.out.println("Xóa chi tiết khuyến mãi thành công!");
//
//                } else {
//                    System.out.println("Xóa chi tiết khuyến mãi thất bại!");
//                }
//                break;
//            }
//            case 9:
//                System.out.println("Đang xóa CustomerEntity...");
//
//                // Nhập Customer ID để tìm và xóa khách hàng
//                CustomerEntity temp = generator.getCustomerDAL().findAll().stream().findFirst().orElse(null);
//                if (temp != null) {
//                    System.out.println("Gợi ý ID: " + temp.getCustomerId());
//                }
//                System.out.print("Nhập ID khách hàng (Customer ID): ");
//                String customerId = sc.nextLine().trim();
//
//                // Tìm khách hàng bằng Customer ID
//                Optional<CustomerEntity> optionalCustomer = generator.getCustomerDAL().findById(customerId);
//                if (optionalCustomer.isPresent()) {
//                    // Xóa khách hàng
//                    if (generator.getCustomerDAL().deleteById(customerId)) {
//                        System.out.println("Xóa khách hàng thành công!");
//                    } else {
//                        System.out.println("Xóa khách hàng thất bại!");
//                    }
//                } else {
//                    System.out.println("Không tìm thấy khách hàng với ID này.");
//                }
//                break;
//            case 10:
//                System.out.println("Đang xóa FloorEntity...");
//
//                // Nhập Floor ID để tìm và xóa tầng
//                FloorEntity temp1 = generator.getFloorDAL().findAll().stream().findFirst().orElse(null);
//                if (temp1 != null) {
//                    System.out.println("Gợi ý ID: " + temp1.getFloorId());
//                }
//                System.out.print("Nhập ID tầng (Floor ID): ");
//                String floorId = sc.nextLine().trim();
//
//                // Tìm tầng bằng Floor ID
//                Optional<FloorEntity> optionalFloor = generator.getFloorDAL().findById(floorId);
//                if (optionalFloor.isPresent()) {
//                    // Xóa tầng
//                    FloorEntity floor = optionalFloor.get();
//                    if (!floor.getTables().isEmpty()) {
//                        floor.getTables().forEach(table -> {
//                            generator.getTableDAL().deleteById(table.getTableId());
//                        });
//                    }
//                    if (generator.getFloorDAL().deleteById(floorId)) {
//                        System.out.println("Xóa tầng thành công!");
//                    } else {
//                        System.out.println("Xóa tầng thất bại!");
//                    }
//                } else {
//                    System.out.println("Không tìm thấy tầng với ID này.");
//                }
//                break;
//
//            case 11:
//                System.out.println("Đang xóa TableEntity...");
//
//                // Nhập Table ID để tìm và xóa bàn
//                TableEntity temp2 = generator.getTableDAL().findAll().stream().findFirst().orElse(null);
//                if (temp2 != null) {
//                    System.out.println("Gợi ý ID: " + temp2.getTableId());
//                }
//                System.out.print("Nhập ID bàn (Table ID): ");
//                String tableId = sc.nextLine().trim();
//
//                // Tìm bàn bằng Table ID
//                Optional<TableEntity> optionalTable = generator.getTableDAL().findById(tableId);
//                if (optionalTable.isPresent()) {
//                    // Xóa bàn
//                    if (generator.getTableDAL().deleteById(tableId)) {
//                        System.out.println("Xóa bàn thành công!");
//                    } else {
//                        System.out.println("Xóa bàn thất bại!");
//                    }
//                } else {
//                    System.out.println("Không tìm thấy bàn với ID này.");
//                }
//                break;
//            case 12: {
//                List<OrderEntity> orders = orderDAL.findAll();
//                System.out.println("Đang xóa OrderEntity...");
//                System.out.println("Danh sách Orders: ");
//                try {
//                    orders.forEach(order -> {
//                        try {
//                            System.out.println(order.getOrderId() + " " + order.getCustomer().getName() + " " + order.getCustomer().getPhone());
//                        } catch (Exception e) {
//                            System.out.println(order.getOrderId());
//                        }
//                    });
//                } catch (Exception e) {
//                    orders.forEach(order -> System.out.println(order.getOrderId()));
//                }
//                System.out.println("Nhập ID order cần xem chi tiết và xóa:");
//                String orderId = sc.nextLine();
//
//// Tìm OrderEntity theo ID
//                OrderEntity selectedOrder = orders.stream()
//                        .filter(order -> order.getOrderId().equals(orderId))
//                        .findFirst()
//                        .orElse(null);
//
//                if (selectedOrder == null) {
//                    System.out.println("Không tìm thấy Order với ID: " + orderId);
//                } else {
//                    System.out.println("Chi tiết Order:");
//                    try {
//                        System.out.println("Mã đơn hàng: " + selectedOrder.getOrderId());
//                        System.out.println("Khách hàng: " + selectedOrder.getCustomer().getName());
//                        System.out.println("Số điện thoại khách hàng: " + selectedOrder.getCustomer().getPhone());
//                        System.out.println("Thời gian đặt chỗ: " + selectedOrder.getReservationTime());
//                        System.out.println("Thời gian hoàn thành dự kiến: " + selectedOrder.getExpectedCompletionTime());
//                        System.out.println("Số lượng khách: " + selectedOrder.getNumberOfCustomer());
//                        System.out.println("Tiền đặt cọc: " + selectedOrder.getDeposit());
//                        System.out.println("Tổng giá: " + selectedOrder.getTotalPrice());
//                        System.out.println("Tổng giảm giá: " + selectedOrder.getTotalDiscount());
//                        System.out.println("Tổng tiền thanh toán: " + selectedOrder.getTotalPaid());
//                        System.out.println("Trạng thái đơn hàng: " + selectedOrder.getOrderStatus());
//                        System.out.println("Loại đơn hàng: " + selectedOrder.getOrderType());
//                        System.out.println("Phương thức thanh toán: " + selectedOrder.getPaymentMethod());
//                        System.out.println("Trạng thái thanh toán: " + selectedOrder.getPaymentStatus());
//                        System.out.println("Trạng thái đặt chỗ: " + selectedOrder.getReservationStatus());
//
//                        // Hiển thị chi tiết các OrderDetailEntity
//                        System.out.println("Danh sách chi tiết đơn hàng:");
//                        if (selectedOrder.getOrderDetails() != null && !selectedOrder.getOrderDetails().isEmpty()) {
//                            selectedOrder.getOrderDetails().forEach(detail -> {
//                                try {
//                                    System.out.println("  - Item: " + detail.getItem().getName() +
//                                            ", Topping: " + detail.getTopping().getName() +
//                                            ", Số lượng: " + detail.getQuantity() +
//                                            ", Thành tiền: " + detail.getLineTotal() +
//                                            ", Giảm giá: " + detail.getDiscount() +
//                                            ", Mô tả: " + detail.getDescription());
//                                } catch (Exception e) {
//                                    System.out.println("  - Chi tiết không đầy đủ hoặc lỗi khi tải dữ liệu.");
//                                }
//                            });
//                        } else {
//                            System.out.println("Không có chi tiết đơn hàng nào.");
//                        }
//                    } catch (Exception e) {
//                        System.out.println("Lỗi khi tải thông tin chi tiết đơn hàng: " + e.getMessage());
//                    }
//                    System.out.println("Cảnh báo: Bạn có thật sự muốn xóa (y/n)?");
//                    String choice = sc.nextLine();
//                    switch (choice) {
//                        case "y":
//                            orderDAL.deleteById(orderId);
//                            System.out.println("Đã xóa order có ID: " + orderId);
//                            break;
//                        case "n":
//                            System.out.println("Hủy thao tác.");
//                            break;
//                    }
//                }
//                break;
//            }
//            default:
//                System.out.println("Lựa chọn không hợp lệ.");
//                break;
//        }
//    }
//
//    private static void printEntityOptions() {
//        System.out.println("1. CategoryEntity");
//        System.out.println("2. ToppingEntity");
//        System.out.println("3. ItemEntity");
//        System.out.println("4. ItemToppingEntity");
//        System.out.println("5. EmployeeEntity");
//        System.out.println("6. RoleEntity");
//        System.out.println("7. PromotionEntity");
//        System.out.println("8. PromotionDetailEntity");
//        System.out.println("9. CustomerEntity");
//        System.out.println("10. FloorEntity");
//        System.out.println("11. TableEntity");
//        System.out.println("12. OrderEntity");
//        System.out.print("Chọn: ");
//    }
//
//    private static void printDeleteOptions() {
//        System.out.println("Chọn phương thức xóa:");
//        System.out.println("1. Xóa mềm (set active = false)");
//        System.out.println("2. Xóa hoàn toàn (bao gồm các liên kết)");
//        System.out.print("Chọn: ");
//    }
//
//    private static double getDoubleInput(String prompt) {
//        while (true) {
//            try {
//                System.out.print(prompt);
//                return Double.parseDouble(sc.nextLine().trim());
//            } catch (NumberFormatException e) {
//                System.out.println("Vui lòng nhập một số hợp lệ.");
//            }
//        }
//    }
//
//    public static LocalDate getDateInput(String prompt, LocalDate defaultDate) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        LocalDate date = defaultDate;
//
//        while (date == null) {
//            System.out.print(prompt + " (định dạng dd/MM/yyyy): ");
//            String input = sc.nextLine();
//            if (input.isEmpty()) return date;
//            try {
//                date = LocalDate.parse(input, formatter);
//            } catch (DateTimeParseException e) {
//                System.out.println("Ngày nhập không hợp lệ. Vui lòng thử lại.");
//            }
//        }
//
//        return date;
//    }
//
//    private static int getIntInput(String prompt) {
//        while (true) {
//            try {
//                System.out.print(prompt);
//                return Integer.parseInt(sc.nextLine().trim());
//            } catch (NumberFormatException e) {
//                System.out.println("Vui lòng nhập một số nguyên hợp lệ.");
//            }
//        }
//    }
//
//    private static double getDoubleInput(String prompt, double currentValue) {
//        while (true) {
//            try {
//                System.out.print(prompt + " (Nhấn Enter để giữ nguyên): ");
//                String input = sc.nextLine().trim();
//                if (input.isEmpty()) {
//                    return currentValue;
//                }
//                return Double.parseDouble(input);
//            } catch (NumberFormatException e) {
//                System.out.println("Vui lòng nhập một số hợp lệ.");
//            }
//        }
//    }
//
//    private static int getIntInput(String prompt, int currentValue) {
//        while (true) {
//            try {
//                System.out.print(prompt + " (Nhấn Enter để giữ nguyên): ");
//                String input = sc.nextLine().trim();
//                if (input.isEmpty()) {
//                    return currentValue;
//                }
//                return Integer.parseInt(input);
//            } catch (NumberFormatException e) {
//                System.out.println("Vui lòng nhập một số nguyên hợp lệ.");
//            }
//        }
//    }
//
//
//}
