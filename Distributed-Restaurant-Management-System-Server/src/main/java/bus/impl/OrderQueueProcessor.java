package bus.impl;

import bus.OrderBUS;
import bus.request.OrderRequest;
import dal.OrderDAL;
import dal.connectDB.ConnectDB;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import model.OrderEntity;
import model.enums.PaymentStatusEnum;
import model.enums.TableStatusEnum;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OrderQueueProcessor {
    private final BlockingQueue<OrderRequest> orderQueue = new LinkedBlockingQueue<>();
    private final Map<String, Lock> tableLocks = new ConcurrentHashMap<>();
    private final OrderBUS orderBUS;
    private volatile boolean running = true;

    public OrderQueueProcessor(OrderBUS orderBUS) {
        this.orderBUS = orderBUS;
        startProcessing();
    }

    // Thêm yêu cầu vào hàng đợi
    public void addOrderRequest(OrderRequest request) {
        orderQueue.offer(request);
    }

    // Dừng xử lý
    public void stop() {
        running = false;
    }

    // Bắt đầu thread xử lý hàng đợi
    private void startProcessing() {
        new Thread(() -> {
            while (running) {
                try {
                    OrderRequest request = orderQueue.take(); // Lấy yêu cầu từ hàng đợi
                    processRequest(request);
                    Thread.sleep(1000); // Đợi 1 giây sau khi xử lý xong yêu cầu
                } catch (Exception e) {
                    System.out.println("Error processing order request: " + e.getMessage());
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    // Xử lý một yêu cầu
    private void processRequest(OrderRequest request) {
        String tableId = request.getOrderEntity().getTable().getTableId();
        Lock lock = tableLocks.computeIfAbsent(tableId, k -> new ReentrantLock());
        lock.lock();
        EntityManager em = ConnectDB.getEntityManager(); // Tạo EntityManager mới
        try {
            OrderEntity order = request.getOrderEntity();
            PaymentStatusEnum paymentStatus = request.getPaymentStatus();
            boolean success;
            String message;
            OrderEntity savedOrder = null;

            OrderDAL orderDAL = new OrderDAL(em);
            OrderBUSImpl orderBUSImpl = new OrderBUSImpl(em);

            try {
                if (paymentStatus == PaymentStatusEnum.PAID) {
                    order.setPaymentStatus(PaymentStatusEnum.PAID);
                    success = orderBUSImpl.updateEntity(order);
                    message = success ? "Thanh toán đơn hàng thành công!" : "Lỗi khi thanh toán đơn hàng.";
                } else {
                    if (order.getTable().getTableStatus() != TableStatusEnum.OCCUPIED) {
                        savedOrder = orderBUSImpl.insertEntity(order);
                        success = savedOrder != null;
                        message = success ? "Lưu đơn hàng thành công!" : "Lỗi khi lưu đơn hàng.";
                    } else {
                        message = "Bàn đã được đặt trước!";
                        success = false;
                    }
                }
                request.getCallback().notifyOrderResult(success, message, savedOrder);
            } catch (RemoteException e) {
                request.getCallback().notifyOrderResult(false, "Lỗi: " + e.getMessage(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                request.getCallback().notifyOrderResult(false, "Lỗi xử lý yêu cầu: " + e.getMessage(), null);
            } catch (RemoteException re) {
                re.printStackTrace();
            }
        } finally {
            lock.unlock();
            tableLocks.remove(tableId);
        }
    }
}