/*
 * @ (#) Runner.java      1.0      1/16/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import model.*;
import model.enums.SizeEnum;
import util.datafaker.DataGenerator;

import javax.management.relation.Role;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Supplier;

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
        roles = generator.getRoleDAL().findAll();
        employees = generator.getEmployeeDAL().findAll();
        promotions = generator.getPromotionDAL().findAll();
        promotionDetails = generator.getPromotionDetailDAL().findAll();

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
                if(name.isEmpty() || name.isBlank()) {
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
                if(name.isEmpty() || name.isBlank()) {
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

                if(name.isEmpty() || name.isBlank()) {
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

                break;
            case 6:
                System.out.println("Nhập tên chức vụ:");
                String roleName = sc.nextLine();
                if(roleName.isEmpty() || roleName.length()==0) {
                    System.out.println("Tên chức vụ không hợp lệ");
                    break;
                }
                try {
                    RoleEntity role = new RoleEntity();
                    role.setRoleName(roleName);
                    String result = generator.getRoleDAL().insert(role)
                            ? "Thêm role thành công: " + role
                            : "Thêm role thất bại!";
                    System.out.println(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                System.out.println("Đang tạo mới PromotionEntity...");
                break;
            case 8:
                System.out.println("Đang tạo mới PromotionDetailEntity...");
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
                System.out.println("Đang đọc danh sách EmployeeEntity...");
                break;
            case 6:
                System.out.println("Đang đọc danh sách RoleEntity...");
                break;
            case 7:
                System.out.println("Đang đọc danh sách PromotionEntity...");
                break;
            case 8:
                System.out.println("Đang đọc danh sách PromotionDetailEntity...");
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
                System.out.println("Đang cập nhật EmployeeEntity...");
                break;
            case 6:
                System.out.println("Đang cập nhật RoleEntity...");
                break;
            case 7:
                System.out.println("Đang cập nhật PromotionEntity...");
                break;
            case 8:
                System.out.println("Đang cập nhật PromotionDetailEntity...");
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
                System.out.println("Đang xóa EmployeeEntity...");
                break;
            case 6:
                System.out.println("Đang xóa RoleEntity...");
                break;
            case 7:
                System.out.println("Đang xóa PromotionEntity...");
                break;
            case 8:
                System.out.println("Đang xóa PromotionDetailEntity...");
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
