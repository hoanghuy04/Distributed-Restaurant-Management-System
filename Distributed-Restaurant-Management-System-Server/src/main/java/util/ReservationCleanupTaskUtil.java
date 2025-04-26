package util;

///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package utillity;
//
///**
// *
// * @author hoang
// */
//import bus.impl.OrderBUS;
//import common.Constants;
//import common.ReservationStatusEnum;
//import connectDB.ConnectDB;
//import entity.OrderEntity;
//import jakarta.mail.MessagingException;
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import raven.toast.Notifications;
//
//public class ReservationCleanupTaskUtil {
//
//    private final OrderBUS orderBUS;
//    boolean checkFirstTime;
//    private StaffGUI parentFrame;
//    boolean check;
//
//    public ReservationCleanupTaskUtil(StaffGUI frame) {
//        this.orderBUS = new OrderBUS(ConnectDB.getEntityManager());
//        this.checkFirstTime = true;
//        this.parentFrame = frame;
//    }
//
//    public void start() {
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//        // Tạo tác vụ để chạy mỗi 15 phút
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                this.check = false;
//                cleanupExpiredReservations();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }, calculateInitialDelay(), 15, TimeUnit.MINUTES);
//    }
//
//    private long calculateInitialDelay() {
//        LocalDateTime now = LocalDateTime.now();
//        int currentMinute = now.getMinute();
//
//        // Tính toán số phút còn lại để đến mốc 00, 15, 30, 45
//        int nextQuarter = ((currentMinute / 15) + 1) * 15 % 60;
//        System.out.println(nextQuarter - currentMinute);
//        return nextQuarter - currentMinute;
//    }
//
//    private void cleanupExpiredReservations() {
//        LocalDateTime now = LocalDateTime.now();
//
//        // Lấy danh sách các đơn đặt trước với điều kiện orderType là ADVANCE
//        List<OrderEntity> advanceOrders = orderBUS.getListOfReservations(LocalDate.now(), null, "ADVANCE");
//        List<OrderEntity> canceledReservations = new ArrayList<>();
//        List<OrderEntity> upComingReservations = new ArrayList<>();
//
//        // Kiểm tra xem đơn nào đã hết thời gian chờ
//        advanceOrders.stream()
//                .filter(order -> order.getReservationStatus().equals(ReservationStatusEnum.PENDING))
//                .forEach(order -> {
//                    if (now.isAfter(order.getReservationTime().plusMinutes(Constants.RESERVATION_TIMEOUT_MINUTES))) {
//                        // Xóa đơn đặt trước đã hết hạn
//                        order.setReservationStatus(ReservationStatusEnum.CANCELED);
//                        orderBUS.updateEntity(order);
//                        this.parentFrame.getMainGUI().getTabReservation().removeFromMapOfAllReservations(order);
//                        Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, 10000, "Thông báo: Đã xóa đơn đặt của khách hàng " + order.getCustomer().getName());
//                        canceledReservations.add(order);
//                        check = true;
//                    } else if (ChronoUnit.MINUTES.between(LocalDateTime.now(), order.getReservationTime()) <= 20) {
//                        upComingReservations.add(order);
//                        check = true;
//                    }
//                });
//
//        try {
//            //Gửi email thông báo
//            MailSenderUtil.sendOrderCancellationEmails(canceledReservations);
//        } catch (MessagingException ex) {
////            Logger.getLogger(ReservationCleanupTaskUtil.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
////            Logger.getLogger(ReservationCleanupTaskUtil.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        try {
//            MailSenderUtil.sendUpcomingReservationReminderEmails(upComingReservations);
//        } catch (MessagingException ex) {
////            Logger.getLogger(ReservationCleanupTaskUtil.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
////            Logger.getLogger(ReservationCleanupTaskUtil.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        if (check) {
//            this.parentFrame.getMainGUI().getTabReservation().setListOfAllReservations();
//        }
//    }
//}

