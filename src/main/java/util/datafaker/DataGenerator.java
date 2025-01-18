package util.datafaker;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import model.*;
import net.datafaker.Faker;

import java.util.Random;

/*
 * @description: DataGenerator
 * @author: Trần Ngọc Huyền
 * @date: 1/18/2025
 * @version: 1.0
 */
public class DataGenerator {

    Faker faker = new Faker(new java.util.Locale("vi"));
    private final Random rand = new Random();

    // CategoryEntity
    private CategoryEntity generateCategoryEntity() {
        return null;
    }

    // ItemEntity
    private ItemEntity generateItemEntity() {
        return null;
    }

    // ToppingEntity
    private ToppingEntity generateToppingEntity() {
        return null;
    }

    // ItemToppingEntity
    private ItemToppingEntity generateItemToppingEntity() {
        return null;
    }

    //Address
    private Address generateAddress() {
        return null;
    }

    // EmployeEntity
    private EmployeeEntity generateEmployeeEntity() {
        return null;
    }

    // RoleEntity
    private RoleEntity generateRoleEntity() {
        return null;
    }

    // PromotionEntity
    private PromotionEntity generatePromotionEntity() {
        return null;
    }

    // PromotionDetailEntity
    private PromotionDetailEntity generatePromotionDetailEntity() {
        return null;
    }

    // CustomerEntity
    private CustomerEntity getCustomerEntity() {
        return null;
    }

    // FloorEntity
    private FloorEntity generateFloorEntity() {
        return null;
    }

    //TableEntity
    private TableEntity getTableEntity() {
        return null;
    }

    //OrderEntity
    private OrderEntity generateOrderEntity() {
        return null;
    }

    public void generateAndPrintSampleData() {
        EntityManager em = Persistence
                .createEntityManagerFactory("mariadb")
                .createEntityManager();

        EntityTransaction tr = em.getTransaction();

        for (int i = 0; i < 10; i++) {
            //new Entity
            tr.begin();
            //persit
            tr.commit();
        }
    }

    public static void main(String[] args) {
        DataGenerator gen = new DataGenerator();
        gen.generateAndPrintSampleData();
    }


}
