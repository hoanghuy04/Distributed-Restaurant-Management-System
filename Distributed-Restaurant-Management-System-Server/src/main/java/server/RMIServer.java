package server;

import bus.*;
import bus.impl.*;
import dal.connectDB.ConnectDB;

import static util.HostNameUtil.*;

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
    public static void main(String[] args) {
        try {
            System.setProperty("sun.rmi.transport.tcp.readTimeout", "60000");
            System.setProperty("java.rmi.server.hostname", "MSI");
            Context context = new InitialContext();
            LocateRegistry.createRegistry(PORT);

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

            context.bind(getURI(getHostName(), CategoryBUS.class), categoryBUS);
            context.bind(getURI(getHostName(), ItemBUS.class), itemBUS);
            context.bind(getURI(getHostName(), FloorBUS.class), floorBUS);
            context.bind(getURI(getHostName(), TableBUS.class), tableBUS);
            context.bind(getURI(getHostName(), CustomerBUS.class), customerBUS);
            context.bind(getURI(getHostName(), OrderBUS.class), orderBUS);
            context.bind(getURI(getHostName(), OrderDetailBUS.class), orderDetailBUS);
            context.bind(getURI(getHostName(), EmployeeBUS.class), employeeBUS);
            context.bind(getURI(getHostName(), PromotionBUS.class), promotionBUS);
            context.bind(getURI(getHostName(), ToppingBUS.class), toppingBUS);
            context.bind(getURI(getHostName(), ItemToppingBUS.class), itemToppingBUS);
            context.bind(getURI(getHostName(), PromotionDetailBUS.class), promotionDetailBUS);
            context.bind(getURI(getHostName(), RoleBUS.class), roleBUS);


            System.out.println("RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}