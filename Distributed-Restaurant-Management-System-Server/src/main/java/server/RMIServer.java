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
            System.setProperty("java.rmi.server.hostname", "huyHoangPc");
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
            FileBUS fileBUS = new FileBUSImpl("/resources");
            bus.MailBUS mailBUS = new MailBUSImpl();

            context.bind(getURI(getHostName(), CategoryBUS.class), categoryBUS);
            System.out.println("CategoryBUS is bound to RMI registry");
            context.bind(getURI(getHostName(), ItemBUS.class), itemBUS);
            System.out.println("ItemBUS is bound to RMI registry");
            context.bind(getURI(getHostName(), FloorBUS.class), floorBUS);
            System.out.println("FloorBUS is bound to RMI registry");
            context.bind(getURI(getHostName(), TableBUS.class), tableBUS);
            System.out.println("TableBUS is bound to RMI registry");
            context.bind(getURI(getHostName(), CustomerBUS.class), customerBUS);
            System.out.println("CustomerBUS is bound to RMI registry");
            context.bind(getURI(getHostName(), OrderBUS.class), orderBUS);
            System.out.println("OrderBUS is bound to RMI registry");
            context.bind(getURI(getHostName(), OrderDetailBUS.class), orderDetailBUS);
            System.out.println("OrderDetailBUS is bound to RMI registry");
            context.bind(getURI(getHostName(), EmployeeBUS.class), employeeBUS);
            System.out.println("EmployeeBUS is bound to RMI registry");
            context.bind(getURI(getHostName(), PromotionBUS.class), promotionBUS);
            System.out.println("PromotionBUS is bound to RMI registry");
            context.bind(getURI(getHostName(), ToppingBUS.class), toppingBUS);
            System.out.println("ToppingBUS is bound to RMI registry");
            context.bind(getURI(getHostName(), ItemToppingBUS.class), itemToppingBUS);
            System.out.println("ItemToppingBUS is bound to RMI registry");
            context.bind(getURI(getHostName(), PromotionDetailBUS.class), promotionDetailBUS);
            System.out.println("PromotionDetailBUS is bound to RMI registry");
            context.bind(getURI(getHostName(), RoleBUS.class), roleBUS);
            System.out.println("FileBUS is bound to RMI registry");
            context.bind(getURI(getHostName(), FileBUS.class), fileBUS);
            System.out.println("MailBUS is bound to RMI registry");
            context.bind(getURI(getHostName(), MailBUS.class), mailBUS);

            System.out.println("RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}