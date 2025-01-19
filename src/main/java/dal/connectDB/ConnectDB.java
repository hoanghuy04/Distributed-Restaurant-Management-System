/*
 * @ (#) ConnectDB.java      1.0      1/18/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package dal.connectDB;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 1/18/2025
 * @version: 1.0
 */
public class ConnectDB {
    @Getter
    private static final ConnectDB instance = new ConnectDB();
    private static EntityManager em = null;

    public static void connect() {
        try {
            em = Persistence.createEntityManagerFactory("mariadb").createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static EntityManager getEntityManager() {
        if (em == null) {
            connect();
        }
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
        return em;
    }
}
