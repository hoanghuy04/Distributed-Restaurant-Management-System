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

/*
 * @description:
 * @author: Hoang Huy
 * @date: 1/16/2025
 * @version: 1.0
 */
public class Runner {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DataGenerator generator = new DataGenerator();
    private static final EntityManager em = ConnectDB.getEntityManager();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private static final ItemDAL itemDAL = new ItemDAL(em);
    private static final ToppingDAL toppingDAL = new ToppingDAL(em);
    private static final OrderDAL orderDAL = new OrderDAL(em);
    private static final OrderDetailDAL orderDetailDAL = new OrderDetailDAL(em);

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
            return Integer.parseInt(scanner.nextLine());
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
}
