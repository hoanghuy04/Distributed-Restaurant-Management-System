/*
 * @ (#) Runner.java      1.0      1/16/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.Table;
import model.*;
import model.enums.CustomerLevelEnum;
import model.enums.TableStatusEnum;
import util.datafaker.DataGenerator;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Scanner;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 1/16/2025
 * @version: 1.0
 */
public class Runner {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DataGenerator generator = new DataGenerator();

    public static void main(String[] args) {
        boolean exit = false;
        generator.generateAndPrintSampleData();
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
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void createEntity() {
        System.out.println("\nChọn loại Entity để tạo mới:");
        printEntityOptions();
        int entityChoice = getChoice();

        switch (entityChoice) {
            case 1:
                System.out.println("Đang tạo mới CategoryEntity...");
                break;
            case 2:
                System.out.println("Đang tạo mới ToppingEntity...");
                break;
            case 3:
                System.out.println("Đang tạo mới ItemEntity...");
                break;
            case 4:
                System.out.println("Đang tạo mới ItemToppingEntity...");
                break;
            case 5:
                System.out.println("Đang tạo mới EmployeeEntity...");
                break;
            case 6:
                System.out.println("Đang tạo mới RoleEntity...");
                break;
            case 7:
                System.out.println("Đang tạo mới PromotionEntity...");
                break;
            case 8:
                System.out.println("Đang tạo mới PromotionDetailEntity...");
                break;
            case 9:
                System.out.println("Đang tạo mới CustomerEntity...");
                // Nhập thông tin từ người dùng
                System.out.print("Nhập tên khách hàng: ");
                String name = scanner.nextLine().trim();

                System.out.print("Nhập email khách hàng: ");
                String email = scanner.nextLine().trim();

                System.out.print("Nhập số điện thoại khách hàng: ");
                String phone = scanner.nextLine().trim();

                System.out.print("Nhập ngày sinh (định dạng yyyy-MM-dd): ");
                String dobInput = scanner.nextLine().trim();
                LocalDateTime dayOfBirth = null;
                try {
                    dayOfBirth = LocalDateTime.parse(dobInput + "T00:00:00");
                } catch (Exception e) {
                    System.out.println("Ngày sinh không hợp lệ. Sử dụng giá trị mặc định là null.");
                }

                System.out.print("Nhập địa chỉ (đường, thành phố, quốc gia): ");
                System.out.print(" - Đường: ");
                String street = scanner.nextLine().trim();
                System.out.print(" - Phường/Xã: ");
                String ward = scanner.nextLine().trim();
                System.out.print(" - Quận/Huyên: ");
                String district = scanner.nextLine().trim();
                System.out.print(" - Thành phố: ");
                String city = scanner.nextLine().trim();


                Address address = new Address();
                address.setStreet(street);
                address.setWard(ward);
                address.setDistrict(district);
                address.setCity(city);

                // Tạo đối tượng CustomerEntity
                CustomerEntity newCustomer = new CustomerEntity();
                newCustomer.setName(name);
                newCustomer.setEmail(email);
                newCustomer.setPhone(phone);
                newCustomer.setDayOfBirth(dayOfBirth);
                newCustomer.setAddress(address);
                newCustomer.setRewardedPoint(0); // Điểm thưởng ban đầu là 0
                newCustomer.setCustomerLevel(CustomerLevelEnum.NEW); // Mức độ khách hàng mặc định là NEW

                if (generator.getCustomerDAL().insert(newCustomer)) {
                    System.out.println("Thêm khách hàng mới thành công!");
                    System.out.println(newCustomer);
                } else {
                    System.out.println("Thêm khách hàng mới thất bại!");
                }
                break;
            case 10:
                System.out.println("Đang tạo mới FloorEntity...");

                // Nhập thông tin từ người dùng
                System.out.print("Nhập tên tầng (Floor Name): ");
                String floorName = scanner.nextLine().trim();

                System.out.print("Nhập sức chứa của tầng (Capacity): ");
                int capacity = 0;
                try {
                    capacity = Integer.parseInt(scanner.nextLine().trim());
                    if (capacity <= 0) {
                        System.out.println("Sức chứa phải là số nguyên dương. Vui lòng thử lại.");
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Sức chứa không hợp lệ. Vui lòng nhập một số nguyên.");
                    break;
                }

                // Tạo đối tượng FloorEntity
                FloorEntity newFloor = new FloorEntity();
                newFloor.setName(floorName);
                newFloor.setCapacity(capacity);

                if (generator.getFloorDAL().insert(newFloor)) {
                    System.out.println("Thêm tầng mới thành công!");
                    System.out.println(newFloor);
                } else {
                    System.out.println("Thêm tầng mới thất bại!");
                }
                break;
            case 11:
                System.out.println("Đang tạo mới TableEntity...");
                System.out.print("Nhập sức chứa của bàn (Capacity): ");
                int capacityTable = 0;
                try {
                    capacityTable = Integer.parseInt(scanner.nextLine().trim());
                    if (capacityTable <= 0) {
                        System.out.println("Sức chứa phải là số nguyên dương. Vui lòng thử lại.");
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Sức chứa không hợp lệ. Vui lòng nhập một số nguyên.");
                    break;
                }

                TableStatusEnum tableStatus = TableStatusEnum.AVAILABLE;

                // Nhập thông tin tầng
                FloorEntity temp = generator.getFloorDAL().findAll().stream().findFirst().orElse(null);
                if (temp != null) {
                    System.out.println("Gợi ý ID: " + temp.getFloorId());
                }

                System.out.print("Nhập ID tầng (Floor ID) mà bàn sẽ được đặt: ");
                String floorId = scanner.nextLine().trim();
                FloorEntity floor = generator.getFloorDAL().findById(floorId).orElse(null);
                if (floor == null) {
                    System.out.println("Không tìm thấy tầng với ID này.");
                    break;
                }

                // Tạo đối tượng TableEntity
                TableEntity newTable = new TableEntity();
                newTable.setCapacity(capacityTable);
                int numberOfTable = generator.getTableDAL().findAll().size() + 1;
                newTable.setName("Bàn " + numberOfTable);
                newTable.setTableStatus(tableStatus);
                newTable.setFloor(floor);
                floor.getTables().add(newTable);

                if (generator.getTableDAL().insert(newTable)) {
                    System.out.println("Thêm bàn mới thành công!");
                    System.out.println(newTable);
                } else {
                    System.out.println("Thêm bàn mới thất bại!");
                }
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
                System.out.println("Đang đọc danh sách CategoryEntity...");
                break;
            case 2:
                System.out.println("Đang đọc danh sách ToppingEntity...");
                break;
            case 3:
                System.out.println("Đang đọc danh sách ItemEntity...");
                break;
            case 4:
                System.out.println("Đang đọc danh sách ItemToppingEntity...");
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
                generator.getCustomerDAL().findAll().forEach(x -> System.out.println(x));
                break;
            case 10:
                System.out.println("Đang đọc danh sách FloorEntity...");
                generator.getFloorDAL().findAll().forEach(x -> System.out.println(x));
                break;
            case 11:
                System.out.println("Đang đọc danh sách TableEntity...");
                generator.getTableDAL().findAll().forEach(x -> System.out.println(x));
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

                // Nhập Customer ID để tìm kiếm khách hàng
                CustomerEntity temp = generator.getCustomerDAL().findAll().stream().findFirst().orElse(null);
                if (temp != null) {
                    System.out.println("Gợi ý ID: " + temp.getCustomerId());
                }
                System.out.print("Nhập ID khách hàng (Customer ID): ");
                String customerId = scanner.nextLine().trim();

                // Tìm khách hàng bằng Customer ID
                Optional<CustomerEntity> optionalCustomer = generator.getCustomerDAL().findById(customerId);
                if (optionalCustomer.isPresent()) {
                    CustomerEntity customer = optionalCustomer.get();

                    // Nhập thông tin mới
                    System.out.print("Nhập tên khách hàng mới (nhấn Enter nếu không thay đổi): ");
                    String name = scanner.nextLine().trim();
                    if (!name.isEmpty()) {
                        customer.setName(name);
                    }

                    System.out.print("Nhập email mới (nhấn Enter nếu không thay đổi): ");
                    String email = scanner.nextLine().trim();
                    if (!email.isEmpty()) {
                        customer.setEmail(email);
                    }

                    System.out.print("Nhập số điện thoại mới (nhấn Enter nếu không thay đổi): ");
                    String phone = scanner.nextLine().trim();
                    if (!phone.isEmpty()) {
                        customer.setPhone(phone);
                    }

                    // Nhập ngày sinh mới (nếu có thay đổi)
                    System.out.print("Nhập ngày sinh mới (yyyy-MM-dd) hoặc nhấn Enter nếu không thay đổi: ");
                    String dobInput = scanner.nextLine().trim();
                    if (!dobInput.isEmpty()) {
                        try {
                            customer.setDayOfBirth(LocalDateTime.parse(dobInput + "T00:00:00"));
                        } catch (Exception e) {
                            System.out.println("Ngày sinh không hợp lệ. Sử dụng giá trị mặc định.");
                        }
                    }

                    // Nhập thông tin địa chỉ mới
                    System.out.print("Nhập địa chỉ mới (đường, phường, quận, thành phố) hoặc nhấn Enter nếu không thay đổi: ");
                    System.out.print(" - Đường (street): ");
                    String street = scanner.nextLine().trim();
                    if (!street.isEmpty()) {
                        customer.getAddress().setStreet(street);
                    }

                    System.out.print(" - Phường (ward): ");
                    String ward = scanner.nextLine().trim();
                    if (!ward.isEmpty()) {
                        customer.getAddress().setWard(ward);
                    }

                    System.out.print(" - Quận (district): ");
                    String district = scanner.nextLine().trim();
                    if (!district.isEmpty()) {
                        customer.getAddress().setDistrict(district);
                    }

                    System.out.print(" - Thành phố (city): ");
                    String city = scanner.nextLine().trim();
                    if (!city.isEmpty()) {
                        customer.getAddress().setCity(city);
                    }

                    if (generator.getCustomerDAL().update(customer)) {
                        System.out.println("Cập nhật khách hàng thành công!");
                        System.out.println("Sau khi cập nhật: " + customer);
                    } else {
                        System.out.println("Cập nhật khách hàng thất bại!");
                    }
                } else {
                    System.out.println("Không tìm thấy khách hàng với ID này.");
                }
                break;
            case 10:
                System.out.println("Đang cập nhật FloorEntity...");

                // Nhập Floor ID để tìm kiếm tầng
                FloorEntity temp1 = generator.getFloorDAL().findAll().stream().findFirst().orElse(null);
                if (temp1 != null) {
                    System.out.println("Gợi ý ID: " + temp1.getFloorId());
                }
                System.out.print("Nhập ID tầng (Floor ID): ");
                String floorId = scanner.nextLine().trim();

                // Tìm tầng bằng Floor ID
                Optional<FloorEntity> optionalFloor = generator.getFloorDAL().findById(floorId);
                if (optionalFloor.isPresent()) {
                    FloorEntity floor = optionalFloor.get();

                    // Nhập tên tầng mới (hoặc giữ nguyên nếu không thay đổi)
                    System.out.print("Nhập tên tầng mới (nhấn Enter nếu không thay đổi): ");
                    String name = scanner.nextLine().trim();
                    if (!name.isEmpty()) {
                        floor.setName(name);
                    }

                    // Nhập sức chứa mới (hoặc giữ nguyên nếu không thay đổi)
                    System.out.print("Nhập sức chứa mới (nhấn Enter nếu không thay đổi): ");
                    String capacityInput = scanner.nextLine().trim();
                    if (!capacityInput.isEmpty()) {
                        try {
                            int capacity = Integer.parseInt(capacityInput);
                            floor.setCapacity(capacity);
                        } catch (NumberFormatException e) {
                            System.out.println("Sức chứa không hợp lệ.");
                        }
                    }

                    if (generator.getFloorDAL().update(floor)) {
                        System.out.println("Cập nhật tầng thành công!");
                        System.out.println("Sau khi cập nhật: " + floor);
                    } else {
                        System.out.println("Cập nhật tầng thất bại!");
                    }
                } else {
                    System.out.println("Không tìm thấy tầng với ID này.");
                }
                break;

            case 11:
                System.out.println("Đang cập nhật TableEntity...");

                // Nhập Table ID để tìm kiếm bàn
                TableEntity temp2 = generator.getTableDAL().findAll().stream().findFirst().orElse(null);
                if (temp2 != null) {
                    System.out.println("Gợi ý ID: " + temp2.getTableId());
                }
                System.out.print("Nhập ID bàn (Table ID): ");
                String tableId = scanner.nextLine().trim();

                // Tìm bàn bằng Table ID
                Optional<TableEntity> optionalTable = generator.getTableDAL().findById(tableId);
                if (optionalTable.isPresent()) {
                    TableEntity table = optionalTable.get();

                    // Nhập sức chứa mới (hoặc giữ nguyên nếu không thay đổi)
                    System.out.print("Nhập sức chứa mới (nhấn Enter nếu không thay đổi): ");
                    String capacityInput = scanner.nextLine().trim();
                    if (!capacityInput.isEmpty()) {
                        try {
                            int capacity = Integer.parseInt(capacityInput);
                            table.setCapacity(capacity);
                        } catch (NumberFormatException e) {
                            System.out.println("Sức chứa không hợp lệ.");
                        }
                    }

                    // Nhập ghi chú mới (hoặc giữ nguyên nếu không thay đổi)
                    System.out.print("Nhập tên bàn mới (nhấn Enter nếu không thay đổi): ");
                    String name = scanner.nextLine().trim();
                    if (!name.isEmpty()) {
                        table.setName(name);
                    }

                    // Nhập trạng thái bàn mới (AVAILABLE, OCCUPIED, RESERVED)
                    System.out.print("Nhập trạng thái bàn mới    (AVAILABLE:1, OCCUPIED:2): ");
                    int statusChoice = scanner.nextInt();
                    switch (statusChoice) {
                        case 1:
                            table.setTableStatus(TableStatusEnum.AVAILABLE);
                            break;
                        case 2:
                            table.setTableStatus(TableStatusEnum.OCCUPIED);
                            break;
                        default:
                            System.out.println("Lựa chọn không phù hợp! Không thay đổi status");
                            break;
                    }

                    // Nhập ID tầng mới (nếu thay đổi)
                    FloorEntity temp3 = generator.getFloorDAL().findAll().stream().findFirst().orElse(null);
                    if (temp3 != null) {
                        System.out.println("Gợi ý ID: " + temp3.getFloorId());
                    }
                    System.out.print("Nhập ID tầng mới (Floor ID) hoặc để trống nếu không thay đổi: ");
                    scanner.nextLine();
                    String newFloorId = scanner.nextLine();
                    if (!newFloorId.isEmpty()) {
                        FloorEntity newFloor = generator.getFloorDAL().findById(newFloorId).orElse(null);
                        if (newFloor != null) {
                            table.setFloor(newFloor);
                        } else {
                            System.out.println("Không tìm thấy tầng với ID này.");
                        }
                    }

                    // Cập nhật thông tin vào cơ sở dữ liệu
                    if (generator.getTableDAL().update(table)) {
                        System.out.println("Cập nhật bàn thành công!");
                        System.out.println("Sau khi cập nhật: " + table);
                    } else {
                        System.out.println("Cập nhật bàn thất bại!");
                    }
                } else {
                    System.out.println("Không tìm thấy bàn với ID này.");
                }
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

                // Nhập Customer ID để tìm và xóa khách hàng
                CustomerEntity temp = generator.getCustomerDAL().findAll().stream().findFirst().orElse(null);
                if (temp != null) {
                    System.out.println("Gợi ý ID: " + temp.getCustomerId());
                }
                System.out.print("Nhập ID khách hàng (Customer ID): ");
                String customerId = scanner.nextLine().trim();

                // Tìm khách hàng bằng Customer ID
                Optional<CustomerEntity> optionalCustomer = generator.getCustomerDAL().findById(customerId);
                if (optionalCustomer.isPresent()) {
                    // Xóa khách hàng
                    if (generator.getCustomerDAL().deleteById(customerId)) {
                        System.out.println("Xóa khách hàng thành công!");
                    } else {
                        System.out.println("Xóa khách hàng thất bại!");
                    }
                } else {
                    System.out.println("Không tìm thấy khách hàng với ID này.");
                }
                break;
            case 10:
                System.out.println("Đang xóa FloorEntity...");

                // Nhập Floor ID để tìm và xóa tầng
                FloorEntity temp1 = generator.getFloorDAL().findAll().stream().findFirst().orElse(null);
                if (temp1 != null) {
                    System.out.println("Gợi ý ID: " + temp1.getFloorId());
                }
                System.out.print("Nhập ID tầng (Floor ID): ");
                String floorId = scanner.nextLine().trim();

                // Tìm tầng bằng Floor ID
                Optional<FloorEntity> optionalFloor = generator.getFloorDAL().findById(floorId);
                if (optionalFloor.isPresent()) {
                    // Xóa tầng
                    FloorEntity floor = optionalFloor.get();
                    if (!floor.getTables().isEmpty()) {
                        floor.getTables().forEach(table -> {
                            generator.getTableDAL().deleteById(table.getTableId());
                        });
                    }
                    if (generator.getFloorDAL().deleteById(floorId)) {
                        System.out.println("Xóa tầng thành công!");
                    } else {
                        System.out.println("Xóa tầng thất bại!");
                    }
                } else {
                    System.out.println("Không tìm thấy tầng với ID này.");
                }
                break;

            case 11:
                System.out.println("Đang xóa TableEntity...");

                // Nhập Table ID để tìm và xóa bàn
                TableEntity temp2 = generator.getTableDAL().findAll().stream().findFirst().orElse(null);
                if (temp2 != null) {
                    System.out.println("Gợi ý ID: " + temp2.getTableId());
                }
                System.out.print("Nhập ID bàn (Table ID): ");
                String tableId = scanner.nextLine().trim();

                // Tìm bàn bằng Table ID
                Optional<TableEntity> optionalTable = generator.getTableDAL().findById(tableId);
                if (optionalTable.isPresent()) {
                    // Xóa bàn
                    if (generator.getTableDAL().deleteById(tableId)) {
                        System.out.println("Xóa bàn thành công!");
                    } else {
                        System.out.println("Xóa bàn thất bại!");
                    }
                } else {
                    System.out.println("Không tìm thấy bàn với ID này.");
                }
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
}
