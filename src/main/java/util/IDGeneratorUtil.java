/*
 * @ (#) IDGeneratorUtility.java      1.0      1/18/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package util;

import dal.connectDB.ConnectDB;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 1/18/2025
 * @version: 1.0
 */
public class IDGeneratorUtil {
    // Hàm generate ID
    public static String generateIDWithCreatedDate(String prefix, String entityName, String idFieldName, String dateFieldName) {
        // Lấy thời gian hiện tại
        LocalDateTime currentDate = LocalDateTime.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("ddMMyy"));
        String newID = null;
        String formattedSequence = null;
        int sequence = 1; // Bắt đầu sequence từ 1

        // Sử dụng JPA EntityManager để truy vấn ID
        ConnectDB.connect();
        EntityManager em = ConnectDB.getEntityManager();
        try {
            String queryStr = String.format(
                    "SELECT e.%s FROM %s e WHERE YEAR(e.%s) = ?1 "
                            + "AND MONTH(e.%s) = ?2 "
                            + "AND DAY(e.%s) = ?3 ORDER BY e.%s DESC",
                    idFieldName, entityName, dateFieldName, dateFieldName, dateFieldName, idFieldName
            );

            // Thực hiện truy vấn để lấy ID gần nhất
            List<String> result = em.createNativeQuery(queryStr)
                    .setParameter(1, currentDate.getYear())
                    .setParameter(2, currentDate.getMonthValue())
                    .setParameter(3, currentDate.getDayOfMonth())
                    .setMaxResults(1)
                    .getResultList();

            if (!result.isEmpty()) {
                String id = result.get(0);
                String orderDate = id.substring(1, 7); // Lấy phần ngày tháng trong ID
                // So sánh ngày tháng trong ID với ngày hiện tại
                if (orderDate.equals(formattedDate)) {
                    // Nếu cùng ngày, tăng sequence lên
                    sequence = Integer.parseInt(id.substring(7));
                    formattedSequence = String.format("%04d", ++sequence);
                }
            } else {
                // Nếu không có kết quả nào từ truy vấn, bắt đầu từ sequence 1
                formattedSequence = String.format("%04d", sequence++);
            }

            newID = prefix + formattedDate + formattedSequence;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return newID;
    }

    // Hàm sinh ra ID với prefix và số thứ tự (lớn nhất trong database + 1)
    public static String generateSimpleID(String prefix, String tableName, String id) {
        ConnectDB.connect();
        EntityManager em = ConnectDB.getEntityManager();
        int sequence = 1;

        try {
            String sql = String.format("select max(cast(substring(e.%s, len(?1) + 1, 4) as int)) from %s e", id, tableName);

            Query q = em.createNativeQuery(sql);
            q.setParameter(1, prefix);

            Integer maxSequence = (Integer) Optional.ofNullable(q.getSingleResult()).orElse(0);
            sequence = maxSequence + 1;

            return prefix + String.format("%04d", sequence);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
