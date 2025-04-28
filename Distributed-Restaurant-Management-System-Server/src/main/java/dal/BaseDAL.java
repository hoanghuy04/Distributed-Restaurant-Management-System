/*
 * @ (#) BaseDAL.java      1.0      1/18/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 1/18/2025
 * @version: 1.0
 */
public interface BaseDAL <T, ID>{
    T insert(T t);
    boolean update(T t);
    boolean deleteById(ID id);
    T findById(ID id);
    List<T> findAll();
    static public boolean executeTransaction(EntityManager em, Runnable action) {
        EntityTransaction et = em.getTransaction();
        try {
            if (et.isActive()) {
                throw new IllegalStateException("Giao dịch đang hoạt động, không thể bắt đầu giao dịch mới");
            }
            et.begin();
            action.run();
            if (!et.isActive()) {
                throw new IllegalStateException("Giao dịch không được bắt đầu đúng cách");
            }
            et.commit();
            return true;
        } catch (Exception e) {
            if (et.isActive()) {
                try {
                    et.rollback();
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            System.err.println("Transaction failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (em.isOpen()) {
                em.clear(); // Xóa trạng thái của EntityManager
            }
        }
    }
}