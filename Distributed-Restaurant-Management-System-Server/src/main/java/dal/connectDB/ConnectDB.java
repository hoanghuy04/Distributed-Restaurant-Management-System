//package dal.connectDB;
//
//import io.github.cdimascio.dotenv.Dotenv;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.Persistence;
//import lombok.Getter;
//
//import java.util.Map;
//
//public class ConnectDB {
//    @Getter
//    private static final ConnectDB instance = new ConnectDB();
//    private static EntityManagerFactory emf = null;
//
//    private ConnectDB() {
//        connect();
//    }
//
//    private void connect() {
//        try {
//            Dotenv dotenv = Dotenv.load();
//            Map<String, Object> props = Map.of(
//                    "jakarta.persistence.jdbc.url", dotenv.get("DB_URL"),
//                    "jakarta.persistence.jdbc.user", dotenv.get("DB_USER"),
//                    "jakarta.persistence.jdbc.password", dotenv.get("DB_PASSWORD")
//            );
//
//            emf = Persistence.createEntityManagerFactory("mariadb", props);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static EntityManager getEntityManager() {
//        if (emf == null) {
//            instance.connect();
//        }
//        return emf.createEntityManager();
//    }
//
//    public static void close() {
//        if (emf != null && emf.isOpen()) {
//            emf.close();
//        }
//    }
//}
/*
 * @ (#) ConnectDB.java      1.0      1/18/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package dal.connectDB;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;

import java.util.Map;

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
            Dotenv dotenv = Dotenv.load();
            Map<String, Object> props = Map.of(
                    "jakarta.persistence.jdbc.url", dotenv.get("DB_URL"),
                    "jakarta.persistence.jdbc.user", dotenv.get("DB_USER"),
                    "jakarta.persistence.jdbc.password", dotenv.get("DB_PASSWORD")
            );

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("mariadb", props);
            em = emf.createEntityManager();
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