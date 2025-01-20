/*
 * @ (#) Runner.java      1.0      1/16/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */

import dal.ItemDAL;
import dal.OrderDAL;
import dal.OrderDetailDAL;
import dal.ToppingDAL;
import dal.connectDB.ConnectDB;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import model.CategoryEntity;
import model.*;
import model.enums.*;
import util.datafaker.DataGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 1/16/2025
 * @version: 1.0
 */
public class Runner {
    private static final Scanner sc = new Scanner(System.in);
    private static final DataGenerator generator = new DataGenerator();
    private static final EntityManager em = ConnectDB.getEntityManager();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private static final ItemDAL itemDAL = new ItemDAL(em);
    private static final ToppingDAL toppingDAL = new ToppingDAL(em);
    private static final OrderDAL orderDAL = new OrderDAL(em);
    private static final OrderDetailDAL orderDetailDAL = new OrderDetailDAL(em);

    public static void main(String[] args) {
        generator.generateAndPrintSampleData();

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

                if (generator.getCategoryDAL().findByName(name).orElse(null) != null) {
                    System.out.println("Tên danh mục bị trùng");
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

                if (generator.getToppingDAL().findByName(name).orElse(null) != null) {
                    System.out.println("Tên topping bị trùng");
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

                if (generator.getItemDAL().findByName(name).orElse(null) != null) {
                    System.out.println("Tên sản phẩm bị trùng");
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
                break;
            case 10:
                System.out.println("Đang tạo mới FloorEntity...");
                break;
            case 11:
                System.out.println("Đang tạo mới TableEntity...");
                break;
            case 12: {
                System.out.println("Đang tạo mới OrderEntity...");

                boolean flag = false;

                LocalDateTime reservationTime = null;
                do {
                    try {
                        System.out.println("Nhập thời gian đặt chỗ (dd-MM-yyyy HH:mm):");
                        reservationTime = LocalDateTime.parse(scanner.nextLine(), formatter);
                        flag = true;
                    } catch (Exception e) {
                        flag = false;
                    }
                } while (!flag);

                LocalDateTime expectedCompletionTime = null;
                do {
                    try {
                        System.out.println("Nhập thời gian hoàn thành dự kiến (dd-MM-yyyy HH:mm):");
                        expectedCompletionTime = LocalDateTime.parse(scanner.nextLine(), formatter);
                        flag = true;
                    } catch (Exception e) {
                        flag = false;
                    }
                } while (!flag);


                System.out.println("Nhập số lượng khách:");
                int numberOfCustomer = scanner.nextInt();

                System.out.println("Nhập tiền đặt cọc:");
                double deposit = scanner.nextDouble();

                scanner.nextLine(); // Xóa dòng trống

                OrderStatusEnum orderStatus = null;
                do {
                    try {
                        System.out.println("Nhập trạng thái đơn hàng (SINGLE, MERGE):");
                        orderStatus = OrderStatusEnum.valueOf(scanner.nextLine().toUpperCase());
                        flag = true;
                    } catch (Exception e) {
                        flag = false;
                    }
                } while (!flag);

                OrderTypeEnum orderType = null;
                do {
                    try {
                        System.out.println("Nhập loại đơn hàng (ADVANCE, IMMEDIATE):");
                        orderType = OrderTypeEnum.valueOf(scanner.nextLine().toUpperCase());
                        flag = true;
                    } catch (Exception e) {
                        flag = false;
                    }
                } while (!flag);

                PaymentMethodEnum paymentMethod = null;
                do {
                    try {
                        System.out.println("Nhập phương thức thanh toán (CASH, CREDIT_CARD, E_WALLET):");
                        paymentMethod = PaymentMethodEnum.valueOf(scanner.nextLine().toUpperCase());
                        flag = true;
                    } catch (Exception e) {
                        flag = false;
                    }
                } while (!flag);

                PaymentStatusEnum paymentStatus = null;
                do {
                    try {
                        System.out.println("Nhập trạng thái thanh toán (UNPAID, PAID):");
                        paymentStatus = PaymentStatusEnum.valueOf(scanner.nextLine().toUpperCase());
                        flag = true;
                    } catch (Exception e) {
                        flag = false;
                    }
                } while (!flag);

                ReservationStatusEnum reservationStatus = null;
                do {
                    try {
                        System.out.println("Nhập trạng thái đặt chỗ (PENDING, RECEIVED, CANCELLED):");
                        reservationStatus = ReservationStatusEnum.valueOf(scanner.nextLine().toUpperCase());
                        flag = true;
                    } catch (Exception e) {
                        flag = false;
                    }
                } while (!flag);


                // Khởi tạo OrderEntity
                OrderEntity order = new OrderEntity();
                order.setReservationTime(reservationTime);
                order.setExpectedCompletionTime(expectedCompletionTime);
                order.setTotalPrice();
                order.setTotalDiscount();
                order.setTotalPaid();
                order.setNumberOfCustomer(numberOfCustomer);
                order.setDeposit(deposit);
                order.setOrderStatus(orderStatus);
                order.setOrderType(orderType);
                order.setPaymentMethod(paymentMethod);
                order.setPaymentStatus(paymentStatus);
                order.setReservationStatus(reservationStatus);

                // Nhập danh sách OrderDetailEntity
                System.out.println("Nhập số lượng chi tiết đơn hàng:");
                int detailCount = scanner.nextInt();
                scanner.nextLine(); // Xóa dòng trống
                List<ItemEntity> items = itemDAL.findAll();
                List<ToppingEntity> toppings = toppingDAL.findAll();
                Set<OrderDetailEntity> orderDetails = new HashSet<>();
                for (int i = 0; i < detailCount; i++) {
                    System.out.println("Nhập thông tin chi tiết đơn hàng thứ " + (i + 1));

                    System.out.println("Danh sách items: ");
                    items.forEach(x -> System.out.println(x.getItemId() + " " + x.getName()));
                    System.out.println("Nhập Item ID:");
                    String itemId = scanner.nextLine();

                    System.out.println("Danh sách toppings: ");
                    toppings.forEach(x -> System.out.println(x.getToppingId() + " " + x.getName()));
                    System.out.println("Nhập Topping ID:");
                    String toppingId = scanner.nextLine();

                    System.out.println("Nhập số lượng:");
                    int quantity = scanner.nextInt();

                    System.out.println("Nhập mô tả:");
                    String description = scanner.nextLine();

                    // Khởi tạo OrderDetailEntity
                    OrderDetailEntity detail = new OrderDetailEntity();
                    detail.setOrder(order);

                    detail.setItem(itemDAL.findById(itemId).orElse(null));

                    detail.setTopping(toppingDAL.findById(toppingId).orElse(null));

                    detail.setQuantity(quantity);
                    detail.setLineTotal();
                    detail.setDiscount();
                    detail.setDescription(description);

                    orderDetails.add(detail);
                }

                order.setOrderDetails(orderDetails);
                orderDAL.insert(order);
                orderDetails.forEach(orderDetailDAL::insert);

                // Hiển thị thông tin OrderEntity
                System.out.println("OrderEntity được tạo:");
                System.out.println(order);
                break;
            }
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
                generator.getCategoryDAL().findAll().forEach(System.out::println);
                break;
            case 2:
                generator.getToppingDAL().findAll().forEach(System.out::println);
                break;
            case 3:
                generator.getItemDAL().findAll().forEach(System.out::println);
                break;
            case 4:
                generator.getItemToppingDAL().findAll().forEach(System.out::println);
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
            case 12: {
                List<OrderEntity> orders = orderDAL.findAll();
                System.out.println("Đang đọc danh sách OrderEntity...");
                System.out.println("Danh sách Orders: ");
                try {
                    orders.forEach(order -> {
                        try {
                            System.out.println(order.getOrderId() + " " + order.getCustomer().getName() + " " + order.getCustomer().getPhone());
                        } catch (Exception e) {
                            System.out.println(order.getOrderId());
                        }
                    });
                } catch (Exception e) {
                    orders.forEach(order -> System.out.println(order.getOrderId()));
                }
                System.out.println("Nhập ID order cần xem chi tiết:");
                String orderId = scanner.nextLine();

// Tìm OrderEntity theo ID
                OrderEntity selectedOrder = orders.stream()
                        .filter(order -> order.getOrderId().equals(orderId))
                        .findFirst()
                        .orElse(null);

                if (selectedOrder == null) {
                    System.out.println("Không tìm thấy Order với ID: " + orderId);
                } else {
                    System.out.println("Chi tiết Order:");
                    try {
                        System.out.println("Mã đơn hàng: " + selectedOrder.getOrderId());
                        System.out.println("Khách hàng: " + selectedOrder.getCustomer().getName());
                        System.out.println("Số điện thoại khách hàng: " + selectedOrder.getCustomer().getPhone());
                        System.out.println("Thời gian đặt chỗ: " + selectedOrder.getReservationTime());
                        System.out.println("Thời gian hoàn thành dự kiến: " + selectedOrder.getExpectedCompletionTime());
                        System.out.println("Số lượng khách: " + selectedOrder.getNumberOfCustomer());
                        System.out.println("Tiền đặt cọc: " + selectedOrder.getDeposit());
                        System.out.println("Tổng giá: " + selectedOrder.getTotalPrice());
                        System.out.println("Tổng giảm giá: " + selectedOrder.getTotalDiscount());
                        System.out.println("Tổng tiền thanh toán: " + selectedOrder.getTotalPaid());
                        System.out.println("Trạng thái đơn hàng: " + selectedOrder.getOrderStatus());
                        System.out.println("Loại đơn hàng: " + selectedOrder.getOrderType());
                        System.out.println("Phương thức thanh toán: " + selectedOrder.getPaymentMethod());
                        System.out.println("Trạng thái thanh toán: " + selectedOrder.getPaymentStatus());
                        System.out.println("Trạng thái đặt chỗ: " + selectedOrder.getReservationStatus());

                        // Hiển thị chi tiết các OrderDetailEntity
                        System.out.println("Danh sách chi tiết đơn hàng:");
                        if (selectedOrder.getOrderDetails() != null && !selectedOrder.getOrderDetails().isEmpty()) {
                            selectedOrder.getOrderDetails().forEach(detail -> {
                                try {
                                    System.out.println("  - Item: " + detail.getItem().getName() +
                                            ", Topping: " + detail.getTopping().getName() +
                                            ", Số lượng: " + detail.getQuantity() +
                                            ", Thành tiền: " + detail.getLineTotal() +
                                            ", Giảm giá: " + detail.getDiscount() +
                                            ", Mô tả: " + detail.getDescription());
                                } catch (Exception e) {
                                    System.out.println("  - Chi tiết không đầy đủ hoặc lỗi khi tải dữ liệu.");
                                }
                            });
                        } else {
                            System.out.println("Không có chi tiết đơn hàng nào.");
                        }
                    } catch (Exception e) {
                        System.out.println("Lỗi khi tải thông tin chi tiết đơn hàng: " + e.getMessage());
                    }
                }
                break;
            }
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
            case 1: {
                System.out.print("Nhập ID danh mục cần cập nhật (Gợi ý: C0001): ");
                String categoryId = sc.nextLine().trim();

                CategoryEntity category = generator.getCategoryDAL().findById(categoryId).orElse(null);
                if (category == null) {
                    System.out.println("Không tìm thấy danh mục với ID: " + categoryId);
                    break;
                }

                System.out.println("Thông tin hiện tại:");
                System.out.println("Tên danh mục: " + category.getName());
                System.out.println("Mô tả danh mục: " + category.getDescription());

                System.out.print("Nhập tên danh mục mới (hoặc nhấn Enter để giữ nguyên): ");
                String newName = sc.nextLine().trim();
                if (!newName.isEmpty()) {
                    try {
                        category.setName(newName);
                    } catch (Exception e) {
                        System.out.println("Lỗi: " + e.getMessage());
                        break;
                    }
                }

                System.out.print("Nhập mô tả danh mục mới (hoặc nhấn Enter để giữ nguyên): ");
                String newDescription = sc.nextLine().trim();
                if (!newDescription.isEmpty()) {
                    category.setDescription(newDescription);
                }

                System.out.print("Danh mục còn hoạt động? (true/false, nhấn Enter để giữ nguyên): ");
                String newActive = sc.nextLine().trim();
                if (!newActive.isEmpty()) {
                    category.setActive(Boolean.parseBoolean(newActive));
                }

                String result = generator.getCategoryDAL().update(category) ? "Cập nhật danh mục thành công: " + category : "Cập nhật danh mục thất bại!";
                System.out.println(result);
                break;
            }
            case 2: {
                System.out.print("Nhập ID topping cần cập nhật (gợi ý: T0001): ");
                String toppingId = sc.nextLine().trim();

                ToppingEntity topping = generator.getToppingDAL().findById(toppingId).orElse(null);
                if (topping == null) {
                    System.out.println("Không tìm thấy topping với ID: " + toppingId);
                    break;
                }

                System.out.println("Thông tin hiện tại:");
                System.out.println("Tên topping: " + topping.getName());
                System.out.println("Giá gốc: " + topping.getCostPrice());
                System.out.println("Số lượng tồn kho: " + topping.getStockQuantity());
                System.out.println("Mô tả topping: " + topping.getDescription());

                System.out.print("Nhập tên topping mới (hoặc nhấn Enter để giữ nguyên): ");
                String newName = sc.nextLine().trim();
                if (!newName.isEmpty()) {
                    try {
                        topping.setName(newName);
                    } catch (Exception e) {
                        System.out.println("Lỗi: " + e.getMessage());
                        break;
                    }
                }

                topping.setCostPrice(getDoubleInput("Nhập giá gốc mới", topping.getCostPrice()));
                topping.setStockQuantity(getIntInput("Nhập số lượng tồn kho mới", topping.getStockQuantity()));

                System.out.print("Nhập mô tả mới (hoặc nhấn Enter để giữ nguyên): ");
                String newDescription = sc.nextLine().trim();
                if (!newDescription.isEmpty()) {
                    topping.setDescription(newDescription);
                }

                System.out.print("Topping còn hoạt động? (true/false, nhấn Enter để giữ nguyên): ");
                String newActive = sc.nextLine().trim();
                if (!newActive.isEmpty()) {
                    topping.setActive(Boolean.parseBoolean(newActive));
                }

                String result = generator.getToppingDAL().update(topping) ? "Cập nhật topping thành công: " + topping : "Cập nhật topping thất bại!";
                System.out.println(result);
                break;
            }
            case 3: {
                System.out.print("Nhập ID sản phẩm cần cập nhật (gợi ý: I0001): ");
                String itemId = sc.nextLine().trim();

                ItemEntity item = generator.getItemDAL().findById(itemId).orElse(null);
                if (item == null) {
                    System.out.println("Không tìm thấy sản phẩm với ID: " + itemId);
                    break;
                }

                System.out.println("Thông tin hiện tại:");
                System.out.println("Tên sản phẩm: " + item.getName());
                System.out.println("Giá gốc: " + item.getCostPrice());
                System.out.println("Số lượng tồn kho: " + item.getStockQuantity());
                System.out.println("Mô tả sản phẩm: " + item.getDescription());
                System.out.println("Kích thước: " + (item.getSize() != null ? item.getSize() : "Không có"));

                System.out.print("Nhập tên sản phẩm mới (hoặc nhấn Enter để giữ nguyên): ");
                String newName = sc.nextLine().trim();
                if (!newName.isEmpty()) {
                    try {
                        item.setName(newName);
                    } catch (Exception e) {
                        System.out.println("Lỗi: " + e.getMessage());
                        break;
                    }
                }

                item.setCostPrice(getDoubleInput("Nhập giá gốc mới", item.getCostPrice()));
                item.setStockQuantity(getIntInput("Nhập số lượng tồn kho mới", item.getStockQuantity()));

                System.out.print("Nhập mô tả mới (hoặc nhấn Enter để giữ nguyên): ");
                String newDescription = sc.nextLine().trim();
                if (!newDescription.isEmpty()) {
                    item.setDescription(newDescription);
                }

                System.out.println("Chọn kích thước sản phẩm mới (hoặc nhấn Enter để giữ nguyên):");
                System.out.println("1. SMALL");
                System.out.println("2. MEDIUM");
                System.out.println("3. LARGE");
                System.out.println("4. Không thay đổi kích thước");
                System.out.print("Nhập lựa chọn (1-4): ");
                String sizeChoice = sc.nextLine().trim();

                switch (sizeChoice) {
                    case "1":
                        item.setSize(SizeEnum.SMALL);
                        break;
                    case "2":
                        item.setSize(SizeEnum.MEDIUM);
                        break;
                    case "3":
                        item.setSize(SizeEnum.LARGE);
                        break;
                    case "4":
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ! Kích thước không thay đổi.");
                        break;
                }

                System.out.print("Sản phẩm còn hoạt động? (true/false, nhấn Enter để giữ nguyên): ");
                String newActive = sc.nextLine().trim();
                if (!newActive.isEmpty()) {
                    item.setActive(Boolean.parseBoolean(newActive));
                }

                String result = generator.getItemDAL().update(item) ? "Cập nhật sản phẩm thành công: " + item : "Cập nhật sản phẩm thất bại!";
                System.out.println(result);
                break;
            }
            case 4: {
                System.out.print("Nhập ID sản phẩm (gợi ý: I0001): ");
                String itemId = sc.nextLine().trim();
                ItemEntity item = generator.getItemDAL().findById(itemId).orElse(null);
                if (item == null) {
                    System.out.println("Không tìm thấy sản phẩm với ID: " + itemId);
                    break;
                }

                System.out.print("Nhập ID topping (gợi ý T0001): ");
                String toppingId = sc.nextLine().trim();
                ToppingEntity topping = generator.getToppingDAL().findById(toppingId).orElse(null);
                if (topping == null) {
                    System.out.println("Không tìm thấy topping với ID: " + toppingId);
                    break;
                }

                ItemToppingEntity itemTopping = generator.getItemToppingDAL().findByItemAndTopping(item, topping).orElse(null);
                if (itemTopping == null) {
                    System.out.println("Không tìm thấy ItemToppingEntity với sản phẩm và topping đã nhập.");
                    break;
                }

                System.out.println("Thông tin hiện tại:");
                System.out.println("Giá bán hiện tại: " + itemTopping.getSellingPrice());

                itemTopping.setSellingPrice(getDoubleInput("Nhập giá bán mới: "));

                String result = generator.getItemToppingDAL().update(itemTopping) ? "Cập nhật ItemTopping thành công: " + itemTopping : "Cập nhật ItemTopping thất bại!";
                System.out.println(result);
                break;
            }
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
            case 12: {
                List<OrderEntity> orders = orderDAL.findAll();
                System.out.println("Đang cập nhật OrderEntity...");
                System.out.println("Danh sách Orders: ");
                try {
                    orders.forEach(order -> {
                        try {
                            System.out.println(order.getOrderId() + " " + order.getCustomer().getName() + " " + order.getCustomer().getPhone());
                        } catch (Exception e) {
                            System.out.println(order.getOrderId());
                        }
                    });
                } catch (Exception e) {
                    orders.forEach(order -> System.out.println(order.getOrderId()));
                }

                // Chọn OrderEntity cần cập nhật
                System.out.println("Nhập Order ID cần cập nhật:");
                String orderId = scanner.nextLine();

                // Tìm OrderEntity theo ID
                OrderEntity order = orders.stream()
                        .filter(o -> o.getOrderId().equals(orderId))
                        .findFirst()
                        .orElse(null);

                if (order == null) {
                    System.out.println("Không tìm thấy Order với ID: " + orderId);
                    return;
                }

                System.out.println("Cập nhật thông tin OrderEntity...");

                // Cập nhật thông tin OrderEntity
                boolean flag;
                try {
                    System.out.println("Thời gian cũ " + order.getReservationTime() + " - Nhập thời gian đặt chỗ mới (dd-MM-yyyy HH:mm):");
                    LocalDateTime reservationTime = LocalDateTime.parse(scanner.nextLine(), formatter);
                    order.setReservationTime(reservationTime);
                    flag = true;
                } catch (Exception e) {
                    System.out.println("=> Không chỉnh sửa!");
                    flag = false;
                }

                try {
                    System.out.println("Thời gian cũ " + order.getExpectedCompletionTime() + " - Nhập thời gian hoàn thành dự kiến mới (dd-MM-yyyy HH:mm):");
                    LocalDateTime expectedCompletionTime = LocalDateTime.parse(scanner.nextLine(), formatter);
                    order.setExpectedCompletionTime(expectedCompletionTime);
                    flag = true;
                } catch (Exception e) {
                    System.out.println("=> Không chỉnh sửa!");
                    flag = false;
                }

                System.out.println("Nhập số lượng khách mới:");
                try {
                    order.setNumberOfCustomer(Integer.parseInt(scanner.nextLine()));
                } catch (Exception e) {
                    System.out.println("=> Không chỉnh sửa!");
                }

                System.out.println("Nhập tiền đặt cọc mới:");
                try {
                    order.setDeposit(Double.parseDouble(scanner.nextLine()));
                } catch (Exception e) {
                    System.out.println("=> Không chỉnh sửa!");
                }

                OrderStatusEnum orderStatus;
                try {
                    System.out.println("Nhập trạng thái đơn hàng mới (SINGLE, MERGE):");
                    orderStatus = OrderStatusEnum.valueOf(scanner.nextLine().toUpperCase());
                    order.setOrderStatus(orderStatus);
                } catch (Exception e) {
                    System.out.println("=> Không chỉnh sửa!");
                }

                OrderTypeEnum orderType;
                try {
                    System.out.println("Nhập loại đơn hàng mới (ADVANCE, IMMEDIATE):");
                    orderType = OrderTypeEnum.valueOf(scanner.nextLine().toUpperCase());
                    order.setOrderType(orderType);
                } catch (Exception e) {
                    System.out.println("=> Không chỉnh sửa!");
                }

                PaymentMethodEnum paymentMethod;
                try {
                    System.out.println("Nhập phương thức thanh toán mới (CASH, CREDIT_CARD, E_WALLET):");
                    paymentMethod = PaymentMethodEnum.valueOf(scanner.nextLine().toUpperCase());
                    order.setPaymentMethod(paymentMethod);
                } catch (Exception e) {
                    System.out.println("=> Không chỉnh sửa!");
                }

                PaymentStatusEnum paymentStatus;
                try {
                    System.out.println("Nhập trạng thái thanh toán mới (UNPAID, PAID):");
                    paymentStatus = PaymentStatusEnum.valueOf(scanner.nextLine().toUpperCase());
                    order.setPaymentStatus(paymentStatus);
                } catch (Exception e) {
                    System.out.println("=> Không chỉnh sửa!");
                }

                ReservationStatusEnum reservationStatus;
                try {
                    System.out.println("Nhập trạng thái đặt chỗ mới (PENDING, RECEIVED, CANCELLED):");
                    reservationStatus = ReservationStatusEnum.valueOf(scanner.nextLine().toUpperCase());
                    order.setReservationStatus(reservationStatus);
                } catch (Exception e) {
                    System.out.println("Trạng thái đặt chỗ không hợp lệ, vui lòng thử lại.");
                    flag = false;
                }

                // Ghi thay đổi vào cơ sở dữ liệu
                orderDAL.update(order);

                // Hiển thị thông tin OrderEntity đã cập nhật
                System.out.println("OrderEntity sau khi cập nhật:");
                System.out.println(order);
                break;
            }
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
            case 1: {
                System.out.print("Nhập ID Category cần xóa (Gợi ý: C0001): ");
                String categoryId = sc.nextLine().trim();

                CategoryEntity category = generator.getCategoryDAL().findById(categoryId).orElse(null);
                if (category == null) {
                    System.out.println("Không tìm thấy Category với ID: " + categoryId);
                    break;
                }
                printDeleteOptions();
                int deleteChoice = getChoice();
                switch (deleteChoice) {
                    case 1: {
                        category.setActive(false);
                        generator.getCategoryDAL().update(category);
                        System.out.println("Xoá mềm Category thành công " + category);
                        break;
                    }
                    case 2: {
                        List<ItemEntity> itemsToDelete = generator.getItemDAL().findByCategory(category);
                        itemsToDelete.forEach(item -> {
                            System.out.println("Xóa Item: " + item.getItemId() + " - " + item.getName());

                            generator.getItemToppingDAL().deleteByItemAndTopping(item, null);
                            System.out.println("-> Xóa ItemTopping liên quan đến Item " + item.getItemId());

                            generator.getPromotionDetailDAL().deleteByItemAndPromotion(item, null);
                            System.out.println("-> Xóa PromotionDetail liên quan đến Item " + item.getItemId());

                            generator.getOrderDetailDAL().deleteByItemAndTopping(item, null);
                            System.out.println("-> Xóa OrderDetail liên quan đến Item " + item.getItemId());

                            generator.getItemDAL().deleteById(item.getItemId());
                            System.out.println("-> Xóa Item thành công: " + item.getItemId());
                        });

                        String result = generator.getCategoryDAL().deleteById(categoryId) ? "Xóa Category thành công: " + categoryId + " - " + category.getName() : "Xóa Category thất bại!";
                        System.out.println(result);
                        break;
                    }
                }
                break;
            }
            case 2: {
                System.out.print("Nhập ID Topping cần xóa (Gợi ý: T0001): ");
                String toppingId = sc.nextLine().trim();

                ToppingEntity topping = generator.getToppingDAL().findById(toppingId).orElse(null);
                if (topping == null) {
                    System.out.println("Không tìm thấy Topping với ID: " + toppingId);
                    break;
                }

                printDeleteOptions();
                int deleteChoice = getChoice();

                switch (deleteChoice) {
                    case 1: {
                        topping.setActive(false);
                        generator.getToppingDAL().update(topping);
                        System.out.println("Xóa mềm Topping thành công: " + topping);
                        break;
                    }
                    case 2: {
                        List<ItemToppingEntity> itemToppingsToDelete = generator.getItemToppingDAL().findAll()
                                .stream()
                                .filter(it -> it.getTopping().equals(topping))
                                .collect(Collectors.toList());
                        itemToppingsToDelete.forEach(itemTopping -> {
                            generator.getItemToppingDAL().deleteByItemAndTopping(null, topping);
                            System.out.println("-> Xóa ItemTopping: " + itemTopping);
                        });

                        generator.getOrderDetailDAL().deleteByItemAndTopping(null, topping);
                        System.out.println("-> Xóa OrderDetail liên quan đến Topping: " + toppingId);

                        boolean deleted = generator.getToppingDAL().deleteById(toppingId);
                        System.out.println(deleted ? "Xóa cứng Topping thành công: " + toppingId : "Xóa Topping thất bại!");
                        break;
                    }
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                        break;
                }
                break;
            }
            case 3: {
                System.out.print("Nhập ID Item cần xóa (Gợi ý: I0001): ");
                String itemId = sc.nextLine().trim();

                ItemEntity item = generator.getItemDAL().findById(itemId).orElse(null);
                if (item == null) {
                    System.out.println("Không tìm thấy Item với ID: " + itemId);
                    break;
                }

                printDeleteOptions();
                int deleteChoice = getChoice();
                switch (deleteChoice) {
                    case 1: {
                        item.setActive(false);
                        generator.getItemDAL().update(item);
                        System.out.println("Đã xóa mềm Item: " + item);
                        break;
                    }
                    case 2: {
                        generator.getItemToppingDAL().deleteByItemAndTopping(item, null);
                        System.out.println("-> Xóa các ItemTopping liên quan đến Item: " + item.getItemId());

                        generator.getPromotionDetailDAL().deleteByItemAndPromotion(item, null);
                        System.out.println("-> Xóa các PromotionDetail liên quan đến Item: " + item.getItemId());

                        generator.getOrderDetailDAL().deleteByItemAndTopping(item, null);
                        System.out.println("-> Xóa các OrderDetail liên quan đến Item: " + item.getItemId());

                        String result = generator.getItemDAL().deleteById(itemId)
                                ? "Xóa thành công Item: " + item.getItemId() + " - " + item.getName()
                                : "Xóa Item thất bại!";
                        System.out.println(result);
                        break;
                    }
                    default:
                        System.out.println("Lựa chọn không hợp lệ.");
                        break;
                }
                break;
            }
            case 4: {
                System.out.println("Chọn cách xóa ItemTopping:");
                System.out.println("1. Xóa theo Item");
                System.out.println("2. Xóa theo Topping");
                System.out.println("3. Xóa theo cả Item và Topping");
                System.out.print("Lựa chọn: ");
                int deleteOption = getChoice();

                ItemEntity itemEntity = null;
                ToppingEntity toppingEntity = null;

                switch (deleteOption) {
                    case 1:
                        System.out.print("Nhập ID Item (gợi ý: I0001): ");
                        String itemId = sc.nextLine().trim();
                        itemEntity = generator.getItemDAL().findById(itemId).orElse(null);
                        if (itemEntity == null) {
                            System.out.println("Không tìm thấy Item với ID: " + itemId);
                            return;
                        }
                        break;
                    case 2:
                        System.out.print("Nhập ID Topping (gợi ý: T0001): ");
                        String toppingId = sc.nextLine().trim();
                        toppingEntity = generator.getToppingDAL().findById(toppingId).orElse(null);
                        if (toppingEntity == null) {
                            System.out.println("Không tìm thấy Topping với ID: " + toppingId);
                            return;
                        }
                        break;
                    case 3:
                        System.out.print("Nhập ID Item (gợi ý: I0001): ");
                        itemId = sc.nextLine().trim();
                        itemEntity = generator.getItemDAL().findById(itemId).orElse(null);
                        if (itemEntity == null) {
                            System.out.println("Không tìm thấy Item với ID: " + itemId);
                            return;
                        }

                        System.out.print("Nhập ID Topping (gợi ý: T0001): ");
                        toppingId = sc.nextLine().trim();
                        toppingEntity = generator.getToppingDAL().findById(toppingId).orElse(null);
                        if (toppingEntity == null) {
                            System.out.println("Không tìm thấy Topping với ID: " + toppingId);
                            return;
                        }
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                        return;
                }

                boolean result = generator.getItemToppingDAL().deleteByItemAndTopping(itemEntity, toppingEntity);
                if (result) {
                    System.out.println("Xóa ItemTopping thành công!");
                } else {
                    System.out.println("Xóa ItemTopping thất bại!");
                }
                break;
            }
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
            case 12: {
                List<OrderEntity> orders = orderDAL.findAll();
                System.out.println("Đang xóa OrderEntity...");
                System.out.println("Danh sách Orders: ");
                try {
                    orders.forEach(order -> {
                        try {
                            System.out.println(order.getOrderId() + " " + order.getCustomer().getName() + " " + order.getCustomer().getPhone());
                        } catch (Exception e) {
                            System.out.println(order.getOrderId());
                        }
                    });
                } catch (Exception e) {
                    orders.forEach(order -> System.out.println(order.getOrderId()));
                }
                System.out.println("Nhập ID order cần xem chi tiết và xóa:");
                String orderId = scanner.nextLine();

// Tìm OrderEntity theo ID
                OrderEntity selectedOrder = orders.stream()
                        .filter(order -> order.getOrderId().equals(orderId))
                        .findFirst()
                        .orElse(null);

                if (selectedOrder == null) {
                    System.out.println("Không tìm thấy Order với ID: " + orderId);
                } else {
                    System.out.println("Chi tiết Order:");
                    try {
                        System.out.println("Mã đơn hàng: " + selectedOrder.getOrderId());
                        System.out.println("Khách hàng: " + selectedOrder.getCustomer().getName());
                        System.out.println("Số điện thoại khách hàng: " + selectedOrder.getCustomer().getPhone());
                        System.out.println("Thời gian đặt chỗ: " + selectedOrder.getReservationTime());
                        System.out.println("Thời gian hoàn thành dự kiến: " + selectedOrder.getExpectedCompletionTime());
                        System.out.println("Số lượng khách: " + selectedOrder.getNumberOfCustomer());
                        System.out.println("Tiền đặt cọc: " + selectedOrder.getDeposit());
                        System.out.println("Tổng giá: " + selectedOrder.getTotalPrice());
                        System.out.println("Tổng giảm giá: " + selectedOrder.getTotalDiscount());
                        System.out.println("Tổng tiền thanh toán: " + selectedOrder.getTotalPaid());
                        System.out.println("Trạng thái đơn hàng: " + selectedOrder.getOrderStatus());
                        System.out.println("Loại đơn hàng: " + selectedOrder.getOrderType());
                        System.out.println("Phương thức thanh toán: " + selectedOrder.getPaymentMethod());
                        System.out.println("Trạng thái thanh toán: " + selectedOrder.getPaymentStatus());
                        System.out.println("Trạng thái đặt chỗ: " + selectedOrder.getReservationStatus());

                        // Hiển thị chi tiết các OrderDetailEntity
                        System.out.println("Danh sách chi tiết đơn hàng:");
                        if (selectedOrder.getOrderDetails() != null && !selectedOrder.getOrderDetails().isEmpty()) {
                            selectedOrder.getOrderDetails().forEach(detail -> {
                                try {
                                    System.out.println("  - Item: " + detail.getItem().getName() +
                                            ", Topping: " + detail.getTopping().getName() +
                                            ", Số lượng: " + detail.getQuantity() +
                                            ", Thành tiền: " + detail.getLineTotal() +
                                            ", Giảm giá: " + detail.getDiscount() +
                                            ", Mô tả: " + detail.getDescription());
                                } catch (Exception e) {
                                    System.out.println("  - Chi tiết không đầy đủ hoặc lỗi khi tải dữ liệu.");
                                }
                            });
                        } else {
                            System.out.println("Không có chi tiết đơn hàng nào.");
                        }
                    } catch (Exception e) {
                        System.out.println("Lỗi khi tải thông tin chi tiết đơn hàng: " + e.getMessage());
                    }
                    System.out.println("Cảnh báo: Bạn có thật sự muốn xóa (y/n)?");
                    String choice = scanner.nextLine();
                    switch (choice) {
                        case "y":
                            orderDAL.deleteById(orderId);
                            System.out.println("Đã xóa order có ID: " + orderId);
                            break;
                        case "n":
                            System.out.println("Hủy thao tác.");
                            break;
                    }
                }
                break;
            }
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

    private static void printDeleteOptions() {
        System.out.println("Chọn phương thức xóa:");
        System.out.println("1. Xóa mềm (set active = false)");
        System.out.println("2. Xóa hoàn toàn (bao gồm các liên kết)");
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

    private static double getDoubleInput(String prompt, double currentValue) {
        while (true) {
            try {
                System.out.print(prompt + " (Nhấn Enter để giữ nguyên): ");
                String input = sc.nextLine().trim();
                if (input.isEmpty()) {
                    return currentValue;
                }
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số hợp lệ.");
            }
        }
    }

    private static int getIntInput(String prompt, int currentValue) {
        while (true) {
            try {
                System.out.print(prompt + " (Nhấn Enter để giữ nguyên): ");
                String input = sc.nextLine().trim();
                if (input.isEmpty()) {
                    return currentValue;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số nguyên hợp lệ.");
            }
        }
    }


}
