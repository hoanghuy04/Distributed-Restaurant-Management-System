/*
 * @ (#) Runner.java      1.0      1/16/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */

import jakarta.persistence.Persistence;
import model.*;
import model.enums.CustomerLevelEnum;
import model.enums.PromotionTypeEnum;
import model.enums.SizeEnum;
import util.datafaker.DataGenerator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 1/16/2025
 * @version: 1.0
 */

public class Runner {
    private static final Scanner sc = new Scanner(System.in);
    private static final DataGenerator generator = new DataGenerator();
    private static List<CategoryEntity> categories;
    private static List<ToppingEntity> toppings;
    private static List<ItemEntity> items;
    private static List<ItemToppingEntity> itemToppings;
    private static List<RoleEntity> roles;
    private static List<EmployeeEntity> employees;
    private static List<PromotionEntity> promotions;
    private static List<PromotionDetailEntity> promotionDetails;

    public static void main(String[] args) {
        generator.generateAndPrintSampleData();
        categories = generator.getCategoryDAL().findAll();
        toppings = generator.getToppingDAL().findAll();
        items = generator.getItemDAL().findAll();
        itemToppings = generator.getItemToppingDAL().findAll();


        boolean exit = false;
        while (!exit) {
            printMenu();
            int choice = getChoice();

            switch (choice) {
                case 1:
                    createEntity();
                    break;
                case 2:
                    readEntity();
                    break;
                case 3:
                    updateEntity();
                    break;
                case 4:
                    deleteEntity();
                    break;
                case 5:
                    exit = true;
                    System.out.println("Thoát chương trình.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
                    break;
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n----- MENU CRUD ENTITY -----");
        System.out.println("1. Create (Tạo mới)");
        System.out.println("2. Read (Xem danh sách)");
        System.out.println("3. Update (Cập nhật)");
        System.out.println("4. Delete (Xóa)");
        System.out.println("5. Exit (Thoát)");
        System.out.print("Vui lòng chọn: ");
    }

    private static int getChoice() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void createEntity() {
        System.out.println("\nChọn loại Entity để tạo mới:");
        printEntityOptions();
        int entityChoice = getChoice();

        switch (entityChoice) {
            case 1: {
                System.out.print("Nhập tên danh mục: ");
                String name = sc.nextLine().trim();
                if (name.isEmpty() || name.isBlank()) {
                    System.out.println("Tên danh mục không được để trống");
                    break;
                }

                System.out.print("Nhập mô tả danh mục: ");
                String description = sc.nextLine().trim();

                try {
                    CategoryEntity category = new CategoryEntity("", name, description, true);
                    String result = generator.getCategoryDAL().insert(category) ? "Tạo danh mục thành công " + category : "Tạo danh mục thất bại";
                    System.out.println(result);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 2: {
                System.out.print("Nhập tên topping: ");
                String name = sc.nextLine().trim();
                if (name.isEmpty() || name.isBlank()) {
                    System.out.println("Tên topping không được để trống");
                    break;
                }

                double costPrice = getDoubleInput("Nhập giá gốc của topping: ");
                int stockQuantity = getIntInput("Nhập số lượng tồn kho của topping: ");

                System.out.print("Nhập mô tả topping: ");
                String description = sc.nextLine().trim();
                try {
                    ToppingEntity topping = new ToppingEntity("", name, costPrice, stockQuantity, description, true, new HashSet<>());
                    String result = generator.getToppingDAL().insert(topping)
                            ? "Thêm topping thành công: " + topping
                            : "Thêm topping thất bại!";
                    System.out.println(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 3: {
                System.out.print("Nhập tên sản phẩm: ");
                String name = sc.nextLine().trim();

                if (name.isEmpty() || name.isBlank()) {
                    System.out.println("Tên sản phẩm không được để trống");
                    break;
                }

                double costPrice = getDoubleInput("Nhập giá gốc của sản phẩm: ");
                int stockQuantity = getIntInput("Nhập số lượng tồn kho của sản phẩm: ");

                System.out.print("Nhập mô tả sản phẩm: ");
                String description = sc.nextLine().trim();

                System.out.println("Chọn kích thước sản phẩm:");
                System.out.println("1. SMALL");
                System.out.println("2. MEDIUM");
                System.out.println("3. LARGE");
                System.out.println("4. Để trống (Không chọn kích thước)");

                System.out.print("Nhập lựa chọn (1-4): ");
                String choice = sc.nextLine().trim();
                SizeEnum size = null;

                switch (choice) {
                    case "1":
                        size = SizeEnum.SMALL;
                        break;
                    case "2":
                        size = SizeEnum.MEDIUM;
                        break;
                    case "3":
                        size = SizeEnum.LARGE;
                        break;
                    case "4":
                        size = null;
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập từ 1 đến 4.");
                        break;
                }

                System.out.print("Nhập ID danh mục của sản phẩm (gợi ý CO001): ");
                String categoryId = sc.nextLine().trim().toUpperCase();
                CategoryEntity category = generator.getCategoryDAL().findById(categoryId).orElse(null);
                if (category == null) {
                    System.out.println("Không tìm thấy danh mục với ID: " + categoryId);
                    break;
                }
                try {
                    ItemEntity item = new ItemEntity("", name, costPrice, stockQuantity,
                            description, "", true, size, category, new HashSet<>());

                    String result = generator.getItemDAL().insert(item)
                            ? "Thêm sản phẩm thành công: " + item
                            : "Thêm sản phẩm thất bại!";
                    System.out.println(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 4: {
                System.out.print("Nhập ID sản phẩm (gợi ý: I0001): ");
                String itemId = sc.nextLine().trim().toUpperCase();
                ItemEntity item = generator.getItemDAL().findById(itemId).orElse(null);
                if (item == null) {
                    System.out.println("Không tìm thấy sản phẩm với ID: " + itemId);
                    break;
                }

                System.out.print("Nhập ID topping (gợi ý T0001): ");
                String toppingId = sc.nextLine().trim().toUpperCase();
                ToppingEntity topping = generator.getToppingDAL().findById(toppingId).orElse(null);
                if (topping == null) {
                    System.out.println("Không tìm thấy topping với ID: " + toppingId);
                    break;
                }
                ItemToppingEntity existing = generator.getItemToppingDAL().findByItemAndTopping(item, topping).orElse(null);
                if (existing != null) {
                    System.out.println("Thực thể đã tồn tại: " + existing);
                } else {
                    ItemToppingEntity newEntity = new ItemToppingEntity(item, topping);
                    generator.getItemToppingDAL().insert(newEntity);
                    System.out.println("Thêm mới thành công: " + newEntity);
                }

                break;
            }
            case 5:
                System.out.println("Nhập mật khẩu:");
                String password = sc.nextLine();

                System.out.println("Nhập họ tên:");
                String fullname = sc.nextLine();

                System.out.println("Nhập so điện thoại: ");
                String phoneNumber = sc.nextLine();

                System.out.println("Nhập email: ");
                String email = sc.nextLine();

                System.out.println("Nhập địa chỉ: ");

                System.out.println("\tNhập phố: ");
                String street = sc.nextLine();

                System.out.println("\tNhập phường: ");
                String ward = sc.nextLine();

                System.out.println("\tNhập đường: ");
                String district = sc.nextLine();

                System.out.println("\tNhập thành phố: ");
                String city = sc.nextLine();

                Address address = new Address();
                address.setCity(city);
                address.setStreet(street);
                address.setDistrict(district);
                address.setWard(ward);

                System.out.println("Nhập trạng thái( 0=False; 1=True ): ");
                String choice = sc.nextLine().trim();
                boolean active = true;

                switch (choice) {
                    case "0":
                        active = false;
                        break;
                    case "1":
                        active = true;
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập 0 hoặc 1.");
                        break;
                }

                System.out.print("Nhập ID chức vụ(gợi ý: R0001): ");
                String roleId = sc.nextLine().trim().toUpperCase();
                RoleEntity role = generator.getRoleDAL().findById(roleId).orElse(null);
                if (role == null) {
                    System.out.println("Không tìm thấy chức vu với ID: " + roleId);
                    break;
                }

                try {
                    EmployeeEntity employee = new EmployeeEntity("", password, fullname, phoneNumber, email,
                            address, active, role);

                    String result = generator.getEmployeeDAL().insert(employee)
                            ? "Thêm sản phẩm thành công: " + employee
                            : "Thêm sản phẩm thất bại!";
                    System.out.println(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case 6:
                System.out.println("Nhập tên chức vụ:");
                String roleName = sc.nextLine();
                if (roleName.isEmpty() || roleName.length() == 0) {
                    System.out.println("Tên chức vụ không hợp lệ");
                    break;
                }
                try {
                    RoleEntity role_tmp = new RoleEntity();
                    role_tmp.setRoleName(roleName);
                    String result = generator.getRoleDAL().insert(role_tmp)
                            ? "Thêm role thành công: " + role_tmp
                            : "Thêm role thất bại!";
                    System.out.println(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                System.out.println("Nhập mô ta:");
                String description = sc.nextLine();
                double discountPercentage = getDoubleInput("Nhập giá trị giảm giá:");
                LocalDate startedDate = getDateInput("Nhập ngày bắt đầu: ",LocalDate.now());
                LocalDate endedDate = getDateInput("Nhập ngày kết thúc: ", LocalDate.now());
                double minPrice = getDoubleInput("Nhập tiền áp dụng: ");
                LocalDate today = LocalDate.now();

                System.out.println("Chọn loại khuyến mãi:");
                System.out.println("1. ITEM");
                System.out.println("2. ORDER");

                System.out.print("Nhập lựa chọn (1 hoặc 2): ");
                String promotionTypeChoice = sc.nextLine().trim();
                PromotionTypeEnum promotionType = null;

                switch (promotionTypeChoice) {
                    case "1":
                        promotionType = PromotionTypeEnum.ITEM;
                        break;
                    case "2":
                        promotionType = PromotionTypeEnum.ORDER;
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập từ 1 hoặc 2.");
                        break;
                }

                boolean promotionActive = (today.isAfter(startedDate) || today.isEqual(startedDate)) && (today.isBefore(endedDate) || today.isEqual(endedDate));
                System.out.println("Nhập trạng thái( 0=False; 1=True ): ");
                String choice_1 = sc.nextLine().trim();

                switch (choice_1) {
                    case "0":
                        promotionActive = false;
                        break;
                    case "1":
                        promotionActive = true;
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập 0 hoặc 1.");
                        break;
                }
                try {
                    PromotionEntity promotion = new PromotionEntity("", promotionType, description,discountPercentage, startedDate, endedDate, promotionActive, minPrice, null, null);
                    String result = generator.getPromotionDAL().insert(promotion)
                            ? "Thêm promotion thành công: " + promotion
                            : "Thêm promotion thất bại!";
                    System.out.println(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                System.out.print("Nhập ID khuyến mãi: ");
                String promotionId = sc.nextLine().trim().toUpperCase();
                PromotionEntity promotion = generator.getPromotionDAL().findById(promotionId).orElse(null);
                if (promotion == null) {
                    System.out.println("Không tìm thấy khuyến mãi với ID: " + promotionId);
                    break;
                }

                System.out.print("Nhập ID món ăn: ");
                String itemId = sc.nextLine().trim().toUpperCase();
                ItemEntity item = generator.getItemDAL().findById(itemId).orElse(null);
                if (item == null) {
                    System.out.println("Không tìm thấy món ăn với ID: " + itemId);
                    break;
                }


                System.out.println("Chọn cấp độ khách hàng:");
                System.out.println("1. NEW");
                System.out.println("2. POTENTIAL");
                System.out.println("3. VIP");

                System.out.print("Nhập lựa chọn (1 -> 3): ");
                String customerLevelChoice = sc.nextLine().trim();
                CustomerLevelEnum customerLevelType = null;

                switch (customerLevelChoice) {
                    case "1":
                        customerLevelType = CustomerLevelEnum.NEW;
                        break;
                    case "2":
                        customerLevelType = CustomerLevelEnum.POTENTIAL;
                        break;
                    case "3":
                        customerLevelType = CustomerLevelEnum.VIP;
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập từ 1 -> 3.");
                        break;
                }
                System.out.println("Nhập mô ta:");
                String promotionDetailDescription = sc.nextLine();

                try {
                    PromotionDetailEntity promotionDetail = new PromotionDetailEntity(promotion, item, customerLevelType, promotionDetailDescription);
                    String result = generator.getPromotionDetailDAL().insert(promotionDetail)
                            ? "Thêm promotion detail thành công: " + promotionDetail
                            : "Thêm promotion detail thất bại!";
                    System.out.println(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 9:
                System.out.println("Đang tạo mới CustomerEntity...");
                break;
            case 10:
                System.out.println("Đang tạo mới FloorEntity...");
                break;
            case 11:
                System.out.println("Đang tạo mới TableEntity...");
                break;
            case 12:
                System.out.println("Đang tạo mới OrderEntity...");
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ.");
                break;
        }
    }

    private static void readEntity() {
        System.out.println("\nChọn loại Entity để xem danh sách:");
        printEntityOptions();
        int entityChoice = getChoice();

        switch (entityChoice) {
            case 1:
                categories.forEach(System.out::println);
                break;
            case 2:
                toppings.forEach(System.out::println);
                break;
            case 3:
                items.forEach(System.out::println);
                break;
            case 4:
                itemToppings.forEach(System.out::println);
                break;
            case 5:
                generator.getEmployeeDAL().findAll().forEach(System.out::println);
                break;
            case 6:
                generator.getRoleDAL().findAll();
                break;
            case 7:
                generator.getPromotionDAL().findAll();
                break;
            case 8:
                generator.getPromotionDetailDAL().findAll();
                break;
            case 9:
                System.out.println("Đang đọc danh sách CustomerEntity...");
                break;
            case 10:
                System.out.println("Đang đọc danh sách FloorEntity...");
                break;
            case 11:
                System.out.println("Đang đọc danh sách TableEntity...");
                break;
            case 12:
                System.out.println("Đang đọc danh sách OrderEntity...");
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ.");
                break;
        }
    }

    private static void updateEntity() {
        System.out.println("\nChọn loại Entity để cập nhật:");
        printEntityOptions();
        int entityChoice = getChoice();

        switch (entityChoice) {
            case 1:
                System.out.println("Đang cập nhật CategoryEntity...");
                CategoryEntity categoryEntity = categories.get(categories.size() - 1);
//                categoryEntity.setName();
                break;
            case 2:
                System.out.println("Đang cập nhật ToppingEntity...");
                break;
            case 3:
                System.out.println("Đang cập nhật ItemEntity...");
                break;
            case 4:
                System.out.println("Đang cập nhật ItemToppingEntity...");
                break;
            case 5:
                System.out.print("Nhập ID nhân viên cần cập nhật: ");
                String employeeId = sc.nextLine().trim();

                // Tìm EmployeeEntity theo ID
                EmployeeEntity employee = generator.getEmployeeDAL().findById(employeeId).orElse(null);
                if (employee == null) {
                    System.out.println("Không tìm thấy nhân viên với ID: " + employeeId);
                    return;
                }

                // Hiển thị thông tin hiện tại
                System.out.println("Thông tin hiện tại:");
                System.out.println("Họ tên: " + employee.getFullname());
                System.out.println("Số điện thoại: " + employee.getPhoneNumber());
                System.out.println("Email: " + employee.getEmail());
                System.out.println("Địa chỉ: " + employee.getAddress());
                System.out.println("Trạng thái hoạt động: " + (employee.isActive() ? "Hoạt động" : "Không hoạt động"));
                System.out.println("Quyền (Role): " + (employee.getRole() != null ? employee.getRole().getRoleName() : "Chưa được gán"));

                // Cập nhật tên
                System.out.print("Nhập họ tên mới (nhấn Enter để giữ nguyên): ");
                String newFullname = sc.nextLine().trim();
                if (!newFullname.isEmpty()) {
                    employee.setFullname(newFullname);
                }

                // Cập nhật số điện thoại
                System.out.print("Nhập số điện thoại mới (nhấn Enter để giữ nguyên): ");
                String newPhoneNumber = sc.nextLine().trim();
                if (!newPhoneNumber.isEmpty()) {
                    employee.setPhoneNumber(newPhoneNumber);
                }

                // Cập nhật email
                System.out.print("Nhập email mới (nhấn Enter để giữ nguyên): ");
                String newEmail = sc.nextLine().trim();
                if (!newEmail.isEmpty()) {
                    employee.setEmail(newEmail);
                }

                // Cập nhật địa chỉ
                System.out.print("Nhập địa chỉ mới (nhấn Enter để giữ nguyên): ");
                String newAddress = sc.nextLine().trim();
                if (!newAddress.isEmpty()) {
                    System.out.println("Nhập địa chỉ: ");

                    System.out.println("\tNhập phố: ");
                    String street = sc.nextLine();

                    System.out.println("\tNhập phường: ");
                    String ward = sc.nextLine();

                    System.out.println("\tNhập đường: ");
                    String district = sc.nextLine();

                    System.out.println("\tNhập thành phố: ");
                    String city = sc.nextLine();

                    Address address = new Address();

                    address.setCity(city);
                    address.setStreet(street);
                    address.setDistrict(district);
                    address.setWard(ward);
                    employee.setAddress(address);
                }

                // Cập nhật trạng thái hoạt động
                System.out.print("Nhân viên còn hoạt động? (true/false, nhấn Enter để giữ nguyên): ");
                String newActive = sc.nextLine().trim();
                if (!newActive.isEmpty()) {
                    employee.setActive(Boolean.parseBoolean(newActive));
                }

                // Cập nhật quyền (Role)
                System.out.print("Nhập ID quyền mới (nhấn Enter để giữ nguyên): ");
                String newRoleId = sc.nextLine().trim();
                if (!newRoleId.isEmpty()) {
                    RoleEntity newRole = generator.getRoleDAL().findById(newRoleId).orElse(null);
                    if (newRole != null) {
                        employee.setRole(newRole);
                    } else {
                        System.out.println("Không tìm thấy quyền với ID: " + newRoleId);
                    }
                }

                // Lưu thông tin cập nhật
                boolean updated = generator.getEmployeeDAL().update(employee);
                if (updated) {
                    System.out.println("Cập nhật thông tin nhân viên thành công: " + employee);
                } else {
                    System.out.println("Cập nhật thông tin nhân viên thất bại!");
                }
                break;
            case 6:
                System.out.print("Nhập ID chức vụ cần cập nhật (gợi ý: R0001): ");
                String roleId = sc.nextLine().trim();
                RoleEntity role = generator.getRoleDAL().findById(roleId).orElse(null);
                if (role == null) {
                    System.out.println("Không tìm thấy chức vụ với ID: " + roleId);
                    break;
                }

                boolean roleUpdated = generator.getRoleDAL().update(role);
                if (roleUpdated) {
                    System.out.println("Cập nhật chức vụ thành công: " + role);
                } else {
                    System.out.println("Cập nhật chức vụ thất bại!");
                }
                break;
            case 7:
                System.out.println("Đang cập nhật PromotionEntity...");
                System.out.print("Nhập ID khuyến mãi cần cập nhật: ");
                String promotionId = sc.nextLine().trim();

                // Tìm PromotionEntity theo ID
                PromotionEntity promotion = generator.getPromotionDAL().findById(promotionId).orElse(null);
                if (promotion == null) {
                    System.out.println("Không tìm thấy khuyến mãi với ID: " + promotionId);
                    return;
                }

                // Hiển thị thông tin hiện tại
                System.out.println("Thông tin hiện tại:");
                System.out.println("Mô tả: " + promotion.getDescription());
                System.out.println("Phần trăm giảm giá: " + promotion.getDiscountPercentage());
                System.out.println("Ngày bắt đầu: " + promotion.getStartedDate());
                System.out.println("Ngày kết thúc: " + promotion.getEndedDate());
                System.out.println("Loại khuyến mãi: " + promotion.getPromotionType());
                System.out.println("Trạng thái hoạt động: " + (promotion.isActive() ? "Hoạt động" : "Không hoạt động"));
                System.out.println("Giá tối thiểu: " + promotion.getMinPrice());

                // Cập nhật mô tả
                System.out.print("Nhập mô tả mới (nhấn Enter để giữ nguyên): ");
                String newDescription = sc.nextLine().trim();
                if (!newDescription.isEmpty()) {
                    promotion.setDescription(newDescription);
                }

                // Cập nhật phần trăm giảm giá
                System.out.print("Nhập phần trăm giảm giá mới (nhấn Enter để giữ nguyên): ");
                String newDiscount = sc.nextLine().trim();
                if (!newDiscount.isEmpty()) {
                    try {
                        promotion.setDiscountPercentage(Double.parseDouble(newDiscount));
                    } catch (NumberFormatException e) {
                        System.out.println("Phần trăm giảm giá không hợp lệ. Giữ nguyên giá trị cũ.");
                    }
                }

                // Cập nhật ngày bắt đầu
                LocalDate newStartDate = getDateInput("Nhập ngày bắt đầu mới", promotion.getStartedDate());
                if (newStartDate != null) {
                    promotion.setStartedDate(newStartDate);
                }

                // Cập nhật ngày kết thúc
                LocalDate newEndDate = getDateInput("Nhập ngày kết thúc mới", promotion.getEndedDate());
                if (newEndDate != null) {
                    promotion.setEndedDate(newEndDate);
                }

                // Cập nhật trạng thái hoạt động
                System.out.print("Khuyến mãi còn hoạt động? (true/false, nhấn Enter để giữ nguyên): ");
                String newPromotionActive = sc.nextLine().trim();
                if (!newPromotionActive.isEmpty()) {
                    promotion.setActive(Boolean.parseBoolean(newPromotionActive));
                }

                // Cập nhật loại khuyến mãi
                System.out.println("Chọn loại khuyến mãi (0 - Giảm món, 1 - Giảm đơn): ");
                System.out.print("Nhập lựa chọn: ");
                String promotionTypeInput = sc.nextLine().trim();
                if (!promotionTypeInput.isEmpty()) {
                    try {
                        PromotionTypeEnum promotionTypeEnum = PromotionTypeEnum.values()[Integer.parseInt(promotionTypeInput)];
                        promotion.setPromotionType(promotionTypeEnum);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("Lựa chọn loại khuyến mãi không hợp lệ. Giữ nguyên giá trị cũ.");
                    }
                }

                // Cập nhật giá tối thiểu
                System.out.print("Nhập giá tối thiểu mới (nhấn Enter để giữ nguyên): ");
                String newMinPrice = sc.nextLine().trim();
                if (!newMinPrice.isEmpty()) {
                    try {
                        promotion.setMinPrice(Double.parseDouble(newMinPrice));
                    } catch (NumberFormatException e) {
                        System.out.println("Giá tối thiểu không hợp lệ. Giữ nguyên giá trị cũ.");
                    }
                }

                // Lưu thông tin cập nhật
                boolean promotionUpdated = generator.getPromotionDAL().update(promotion);
                if (promotionUpdated) {
                    System.out.println("Cập nhật thông tin khuyến mãi thành công: " + promotion);
                } else {
                    System.out.println("Cập nhật thông tin khuyến mãi thất bại!");
                }


                break;
            case 8:
                System.out.print("Nhập ID khuyến mãi của chi tiết khuến mãi: ");
                String pdPromotionId = sc.nextLine().trim();

                System.out.print("Nhập ID món ăn của chi tiết khuến mãi: ");
                String pdItemId = sc.nextLine().trim();

                // Tìm PromotionDetailEntity theo ID
                PromotionDetailEntity promotionDetail = generator.getPromotionDetailDAL().findByPromotionAndItem(pdPromotionId, pdItemId);
                if (promotionDetail == null) {
                    System.out.println("Không tìm thấy khuyến mãi chi tiết với ID khuyến mãi: " + pdPromotionId + " ID vật phẩm: " + pdItemId);
                    return;
                }

                // Hiển thị thông tin hiện tại
                System.out.println("Thông tin hiện tại:");
                System.out.println("Mã khuyến mãi: " + promotionDetail.getPromotion().getPromotionId());
                System.out.println("Mã món: " + promotionDetail.getItem().getItemId());

                System.out.println("Cấp độ khách hàng: " + promotionDetail.getCustomerLevel().getCustomerLevel());
                System.out.println("Mô tả: " + promotionDetail.getDescription());


                // Cập nhật cấp độ khách hàng
                System.out.println("Chọn cấp độ khách hàng (0 - Khách hàng mới, 1 - Khách hàng tiềm năng, 2 - Khách hàng thân thiết): ");
                System.out.print("Nhập lựa chọn: ");
                String levelInput = sc.nextLine().trim();
                if (!levelInput.isEmpty()) {
                    try {
                        CustomerLevelEnum customerLevelEnum = CustomerLevelEnum.values()[Integer.parseInt(levelInput)];
                        promotionDetail.setCustomerLevel(customerLevelEnum);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("Lựa chọn cấp độ khách hàng không hợp lệ. Giữ nguyên giá trị cũ.");
                    }
                }

                // Cập nhật mô tả
                System.out.print("Nhập mô tả mới (nhấn Enter để giữ nguyên): ");
                String newPDDescription = sc.nextLine().trim();
                if (!newPDDescription.isEmpty()) {
                    promotionDetail.setDescription(newPDDescription);
                }

                // Lưu thông tin cập nhật
                boolean pdupdated = generator.getPromotionDetailDAL().update(promotionDetail);
                if (pdupdated) {
                    System.out.println("Cập nhật thông tin khuyến mãi chi tiết thành công: " + promotionDetail);
                } else {
                    System.out.println("Cập nhật thông tin khuyến mãi chi tiết thất bại!");
                }
                break;
            case 9:
                System.out.println("Đang cập nhật CustomerEntity...");
                break;
            case 10:
                System.out.println("Đang cập nhật FloorEntity...");
                break;
            case 11:
                System.out.println("Đang cập nhật TableEntity...");
                break;
            case 12:
                System.out.println("Đang cập nhật OrderEntity...");
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ.");
                break;
        }
    }

    private static void deleteEntity() {
        System.out.println("\nChọn loại Entity để xóa:");
        printEntityOptions();
        int entityChoice = getChoice();

        switch (entityChoice) {
            case 1:
                System.out.println("Đang xóa CategoryEntity...");
                break;
            case 2:
                System.out.println("Đang xóa ToppingEntity...");
                break;
            case 3:
                System.out.println("Đang xóa ItemEntity...");
                break;
            case 4:
                System.out.println("Đang xóa ItemToppingEntity...");
                break;
            case 5:
                System.out.print("Nhập ID nhân viên (gợi ý: ): ");
                String employeeId = sc.nextLine().trim();

                EmployeeEntity employeeToDelete = generator.getEmployeeDAL().findById(employeeId).orElse(null);
                if (employeeToDelete == null) {
                    System.out.println("Không tìm thấy nhân viên với ID: " + employeeId);
                    return;
                }

                employeeToDelete.setRole(null);
                boolean employeeDeleteResult = generator.getEmployeeDAL().deleteById(employeeId);

                if (employeeDeleteResult) {
                    System.out.println("Xóa nhân viên thành công!");
                } else {
                    System.out.println("Xóa nhân viên thất bại!");
                }
                break;

            case 6:
                System.out.print("Nhập ID chức vụ (gợi ý: Emp1901250001): ");
                String roleId = sc.nextLine().trim();
                RoleEntity roleToDelete = generator.getRoleDAL().findById(roleId).orElse(null);
                if (roleToDelete == null) {
                    System.out.println("Không tìm thấy chức vụ với ID: " + roleId);
                    return;
                }

                boolean result = generator.getRoleDAL().deleteById(roleId);

                if (result) {
                    System.out.println("Xóa chức vụ thành công!");
                } else {
                    System.out.println("Xóa chức vụ thất bại!");
                }
                break;
            case 7:
                System.out.print("Nhập ID khuyến mãi của chi tiết khuyến mãi (gợi ý: ): ");
                String promotionToDeleteId = sc.nextLine().trim();
                PromotionEntity promotionEntity = generator.getPromotionDAL().findById(promotionToDeleteId).orElse(null);
                if (promotionEntity == null) {
                    System.out.println("Không tìm thấy  khuyến mãi với ID: " + promotionEntity);
                    return;
                }

                if( promotionEntity.getPromotionDetails()!=null) {
                    promotionEntity.getPromotionDetails().forEach(promotionDetail -> {
                        ItemEntity itemToDelete = promotionDetail.getItem();
                        PromotionEntity promotionToDelete = promotionDetail.getPromotion();
                        if(itemToDelete!=null) {
                            generator.getItemDAL().deleteById(itemToDelete.getItemId());
                        }

                        if(promotionToDelete!=null) {
                            generator.getPromotionDAL().deleteById(promotionToDelete.getPromotionId());
                        }
                        boolean promotionDetailDeleteresult = generator.getPromotionDetailDAL().deleteByItemAndPromotion(itemToDelete,promotionToDelete);

                        if (promotionDetailDeleteresult) {
//                        System.out.println("Xóa chi tiết khuyến mãi thành công!");
                        } else {
                            System.out.println("Xóa chi tiết khuyến mãi thất bại!");
                        }
                    });
                }

                boolean promotionDetailDeleteresult = generator.getPromotionDAL().deleteById(promotionToDeleteId);

                if (promotionDetailDeleteresult) {
                    System.out.println("Xóa khuyến mãi thành công!");
                } else {
                    System.out.println("Xóa khuyến mãi thất bại!");
                }

                break;
            case 8:
                System.out.print("Nhập ID khuyến mãi của chi tiết khuyến mãi (gợi ý: ): ");
                String pToDeleteId = sc.nextLine().trim();
                System.out.print("Nhập ID món ăn của chi tiết khuến mãi (gợi ý: ): ");
                String iToDeleteId = sc.nextLine().trim();

                PromotionDetailEntity promotionDetailToDelete = generator.getPromotionDetailDAL().findByPromotionAndItem(pToDeleteId, iToDeleteId);
                if (promotionDetailToDelete == null) {
                    System.out.println("Không tìm thấy chi tiết khuyến mãi với ID khuyến mãi: " + pToDeleteId + " ID món ăn: " + iToDeleteId);
                    return;
                }

                ItemEntity itemToDelete = promotionDetailToDelete.getItem();
                PromotionEntity promotionToDelete = promotionDetailToDelete.getPromotion();
                if(itemToDelete!=null) {
                    generator.getItemDAL().deleteById(itemToDelete.getItemId());
                }

                if(promotionToDelete!=null) {
                    generator.getPromotionDAL().deleteById(promotionToDelete.getPromotionId());
                }
                boolean promotionDetailDeleteResult = generator.getPromotionDetailDAL().deleteByItemAndPromotion(itemToDelete,promotionToDelete);

                if (promotionDetailDeleteResult) {
                    System.out.println("Xóa chi tiết khuyến mãi thành công!");
                } else {
                    System.out.println("Xóa chi tiết khuyến mãi thất bại!");
                }
                break;
            case 9:
                System.out.println("Đang xóa CustomerEntity...");
                break;
            case 10:
                System.out.println("Đang xóa FloorEntity...");
                break;
            case 11:
                System.out.println("Đang xóa TableEntity...");
                break;
            case 12:
                System.out.println("Đang xóa OrderEntity...");
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ.");
                break;
        }
    }

    private static void printEntityOptions() {
        System.out.println("1. CategoryEntity");
        System.out.println("2. ToppingEntity");
        System.out.println("3. ItemEntity");
        System.out.println("4. ItemToppingEntity");
        System.out.println("5. EmployeeEntity");
        System.out.println("6. RoleEntity");
        System.out.println("7. PromotionEntity");
        System.out.println("8. PromotionDetailEntity");
        System.out.println("9. CustomerEntity");
        System.out.println("10. FloorEntity");
        System.out.println("11. TableEntity");
        System.out.println("12. OrderEntity");
        System.out.print("Chọn: ");
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số hợp lệ.");
            }
        }
    }

    public static LocalDate getDateInput(String prompt, LocalDate defaultDate) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = defaultDate;

        while (date == null) {
            System.out.print(prompt + " (định dạng dd/MM/yyyy): ");
            String input = scanner.nextLine();
            if(input.isEmpty()) return date;
            try {
                date = LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Ngày nhập không hợp lệ. Vui lòng thử lại.");
            }
        }

        return date;
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số nguyên hợp lệ.");
            }
        }
    }

}
