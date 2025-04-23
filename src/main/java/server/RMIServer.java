package server;

import bus.*;
import bus.impl.*;
import dal.connectDB.ConnectDB;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.registry.LocateRegistry;

/*
 * @description: RMIServer
 * @author: Trần Ngọc Huyền
 * @date: 4/22/2025
 * @version: 1.0
 */
public class RMIServer {
    public static void main(String[] args) throws Exception {
        Context context = new InitialContext();
        LocateRegistry.createRegistry(9090);

        CategoryBUS categoryBUS = new CategoryBUSImpl(ConnectDB.getEntityManager());
        ItemBUS itemBUS = new ItemBUSImpl(ConnectDB.getEntityManager());
        FloorBUS floorBUS = new FloorBUSImpl(ConnectDB.getEntityManager());
        TableBUS tableBUS = new TableBUSImpl(ConnectDB.getEntityManager());
        CustomerBUS customerBUS = new CustomerBUSImpl(ConnectDB.getEntityManager());
        OrderBUS orderBUS = new OrderBUSImpl(ConnectDB.getEntityManager());
        OrderDetailBUS orderDetailBUS = new OrderDetailBUSImpl(ConnectDB.getEntityManager());
        EmployeeBUS employeeBUS = new EmployeeBUSImpl(ConnectDB.getEntityManager());
        PromotionBUS promotionBUS = new PromotionBUSImpl(ConnectDB.getEntityManager());
        ToppingBUS toppingBUS = new ToppingBUSImpl(ConnectDB.getEntityManager());
        ItemToppingBUS itemToppingBUS = new ItemToppingBUSImpl(ConnectDB.getEntityManager());
        PromotionDetailBUS promotionDetailBUS = new PromotionDetailBUSImpl(ConnectDB.getEntityManager());
        RoleBUS roleBUS = new RoleBUSImpl(ConnectDB.getEntityManager());

        context.bind("rmi://localhost:9090/categoryBUS", categoryBUS);
        context.bind("rmi://localhost:9090/itemBUS", itemBUS);
        context.bind("rmi://localhost:9090/floorBUS", floorBUS);
        context.bind("rmi://localhost:9090/tableBUS", tableBUS);
        context.bind("rmi://localhost:9090/customerBUS", customerBUS);
        context.bind("rmi://localhost:9090/orderBUS", orderBUS);
        context.bind("rmi://localhost:9090/orderDetailBUS", orderDetailBUS);
        context.bind("rmi://localhost:9090/employeeBUS", employeeBUS);
        context.bind("rmi://localhost:9090/promotionBUS", promotionBUS);
        context.bind("rmi://localhost:9090/toppingBUS", toppingBUS);
        context.bind("rmi://localhost:9090/itemToppingBUS", itemToppingBUS);
        context.bind("rmi://localhost:9090/promotionDetailBUS", promotionDetailBUS);
        context.bind("rmi://localhost:9090/roleBUS", roleBUS);


        System.out.println("RMI Server is running...");
    }
}
