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
    static boolean executeTransaction(EntityManager entityManager, Runnable runnable) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            runnable.run();
            transaction.commit();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }
    }
}