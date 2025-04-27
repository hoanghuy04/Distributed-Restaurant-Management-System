package bus.impl;

import bus.OrderBUS;
import bus.request.OrderRequest;
import model.OrderEntity;
import model.enums.PaymentStatusEnum;

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
                } catch (InterruptedException e) {
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
        try {
            OrderEntity order = request.getOrderEntity();
            PaymentStatusEnum paymentStatus = request.getPaymentStatus();
            boolean success;
            String message;
            OrderEntity savedOrder = null;

            try {
                if (paymentStatus == PaymentStatusEnum.PAID) {
                    order.setPaymentStatus(PaymentStatusEnum.PAID);
                    success = orderBUS.updateEntity(order);
                    message = success ? "Thanh toán đơn hàng thành công!" : "Lỗi khi thanh toán đơn hàng.";
                } else {
                    savedOrder = orderBUS.insertEntity(order);
                    success = savedOrder != null;
                    message = success ? "Lưu đơn hàng thành công!" : "Lỗi khi lưu đơn hàng.";
                }
                // Thông báo kết quả cho client
                request.getCallback().notifyOrderResult(success, message, savedOrder);
            } catch (RemoteException e) {
                request.getCallback().notifyOrderResult(false, "Lỗi: " + e.getMessage(), null);
            }
        } catch (RemoteException e) {
            // Xử lý lỗi khi gọi callback
            e.printStackTrace();
        } finally {
            lock.unlock();
            tableLocks.remove(tableId); // Xóa khóa sau khi xử lý xong
        }
    }
}