/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import bus.impl.*;
import dal.connectDB.ConnectDB;
import model.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.enums.SizeEnum;
import model.enums.TableStatusEnum;

/**
 *
 */
public class MainHuyen {

    public static void main(String[] args) {
        try {
            ConnectDB.connect();
            CategoryBUSImpl categoryBUSImpl = new CategoryBUSImpl(ConnectDB.getEntityManager());
            OrderBUSImpl orderBUSImpl = new OrderBUSImpl(ConnectDB.getEntityManager());
            OrderDetailBUSImpl orderDetailBUSImpl = new OrderDetailBUSImpl(ConnectDB.getEntityManager());
            RoleBUSImpl roleBUSImpl = new RoleBUSImpl(ConnectDB.getEntityManager());
            TableBUSImpl tableBUSImpl = new TableBUSImpl(ConnectDB.getEntityManager());
            CustomerBUSImpl customerBUSImpl = new CustomerBUSImpl(ConnectDB.getEntityManager());
            ItemBUSImpl itemBUSImpl = new ItemBUSImpl(ConnectDB.getEntityManager());
            ToppingBUSImpl toppingBUSImpl = new ToppingBUSImpl(ConnectDB.getEntityManager());
            ItemToppingBUSImpl itemToppingBUSImpl = new ItemToppingBUSImpl(ConnectDB.getEntityManager());
            FloorBUSImpl floorBUSImpl = new FloorBUSImpl(ConnectDB.getEntityManager());
            EmployeeBUSImpl employeeBUS = new EmployeeBUSImpl(ConnectDB.getEntityManager());
            PromotionBUSImpl promotionBUSImpl = new PromotionBUSImpl(ConnectDB.getEntityManager());
            PromotionDetailBUSImpl promotionDetailBUSImpl = new PromotionDetailBUSImpl(ConnectDB.getEntityManager());

            //Category
            String[] categoryNames = {"Pizza hải sản", "Pizza thập phẩm", "Pizza truyền thống", "Pizza chay", "Khai vị", "Mì ý", "Salad", "Thức uống"};
            for (int i = 0; i < categoryNames.length; i++) {
                CategoryEntity c = new CategoryEntity(categoryNames[i], "");
                categoryBUSImpl.insertEntity(c);
            }

            //Item
            String[] sizes = {"SMALL", "MEDIUM", "LARGE"};
            String[] catOneItemNames = {
                "Pizza Hải Sản Pesto Xanh", "Pizza Hải Sản Cocktail", "Pizza Hải Sản Cao Cấp",
                "Pizza Hải Sản Nhiệt Đới", "Pizza Tôm Cocktail"
            };
            for (int i = 0; i < catOneItemNames.length; i++) {
                for (int j = 0; j < sizes.length; j++) {
                    SizeEnum size = SizeEnum.valueOf(sizes[j]);
                    ItemEntity item = new ItemEntity(
                            "(" + size.toString().charAt(0) + ") " + catOneItemNames[i],
                            200000,
                            100,
                            "",
                            true,
                            categoryBUSImpl.getEntityById("C0001"),
                            size,
                            "item_c1_" + i + ".png"
                    );
                    item.setSellingPrice();
                    itemBUSImpl.insertEntity(item);
                }
            }

            String[] catTwoItemNames = {
                "Pizza Aloha", "Pizza Thịt Xông Khói", "Pizza Thịt Nguội Kiểu Canada",
                "Pizza Gà Nướng 3 Vị", "Pizza 5 Loại Thịt Đặc Biệt"
            };
            for (int i = 0; i < catTwoItemNames.length; i++) {
                for (int j = 0; j < sizes.length; j++) {
                    SizeEnum size = SizeEnum.valueOf(sizes[j]);
                    ItemEntity item = new ItemEntity(
                            "(" + size.toString().charAt(0) + ") " + catTwoItemNames[i],
                            180000,
                            100,
                            "",
                            true,
                            categoryBUSImpl.getEntityById("C0002"),
                            size,
                            "item_c2_" + i + ".png"
                    );
                    item.setSellingPrice();
                    itemBUSImpl.insertEntity(item);
                }
            }

            String[] catThreeItemNames = {
                "Pizza Gà Nướng Dứa", "Pizza Xúc Xích Ý", "Pizza Thịt Nguội Nấm", "Pizza Hawaiian"
            };
            for (int i = 0; i < catThreeItemNames.length; i++) {
                for (int j = 0; j < sizes.length; j++) {
                    SizeEnum size = SizeEnum.valueOf(sizes[j]);
                    ItemEntity item = new ItemEntity(
                            "(" + size.toString().charAt(0) + ") " + catThreeItemNames[i],
                            130000,
                            100,
                            "",
                            true,
                            categoryBUSImpl.getEntityById("C0003"),
                            size,
                            "item_c3_" + i + ".png"
                    );
                    item.setSellingPrice();
                    itemBUSImpl.insertEntity(item);
                }
            }

            String[] catFourItemNames = {
                "Pizza Phô Mai", "Pizza Rau Củ"
            };
            for (int i = 0; i < catFourItemNames.length; i++) {
                for (int j = 0; j < sizes.length; j++) {
                    SizeEnum size = SizeEnum.valueOf(sizes[j]);
                    ItemEntity item = new ItemEntity(
                            "(" + size.toString().charAt(0) + ") " + catFourItemNames[i],
                            100000,
                            100,
                            "",
                            true,
                            categoryBUSImpl.getEntityById("C0004"),
                            size,
                            "item_c4_" + i + ".png"
                    );
                    item.setSellingPrice();
                    itemBUSImpl.insertEntity(item);
                }
            }

            String[] catFiveNames = {"Gà Nướng BBQ", "Cánh gà nướng BBQ", "Gà Giòn Xốt Hàn", "Mực Chiên Giòn",
                "Bánh Phô Mai Xoắn", "Gà Popcorn", "Bánh Kẹp Nướng Mexico", "Khoai Tây Chiên", "Bánh Mì Bơ Tỏi", "Bánh Mì Que Nướng", "Sườn siêu sao"};
            for (int i = 0; i < catFiveNames.length; i++) {
                ItemEntity t = new ItemEntity(catFiveNames[i], 100000, 100, "", true, categoryBUSImpl.getEntityById("C0005"), "item_c5_" + i + ".png");
                t.setSellingPrice();
                itemBUSImpl.insertEntity(t);
            }

            String[] catSixNames = {"Mỳ Ý Nghêu Xốt Cay", "Mỳ Ý Nghêu Xốt Húng Quế", "Mì Ý Pesto", "Mỳ Ý Cay Hải Sản",
                "Mỳ Ý Chay Sốt Marinara", "Mỳ Ý Tôm Sốt Kem Cà Chua", "Mỳ Ý Cay Xúc Xích", "Mỳ Ý Giăm Bông Và Nấm Sốt Kem",
                "Mỳ Ý thịt bò bằm", "Mỳ Ý Chay Sốt Kem Tươi"};
            for (int i = 0; i < catSixNames.length; i++) {
                ItemEntity t = new ItemEntity(catSixNames[i], 100000, 100, "", true, categoryBUSImpl.getEntityById("C0006"), "item_c6_" + i + ".png");
                t.setSellingPrice();
                itemBUSImpl.insertEntity(t);
            }

            String[] catSevenNames = {"Salad Trộn Dầu Giấm", "Salad Đặc Sắc", "Salad Gà Giòn Không Xương", "Salad Da Cá Hồi Giòn",
                "Salad Trộn Sốt Caesar"};
            for (int i = 0; i < catSevenNames.length; i++) {
                ItemEntity t = new ItemEntity(catSevenNames[i], 100000, 100, "", true, categoryBUSImpl.getEntityById("C0007"), "item_c7_" + i + ".png");
                t.setSellingPrice();
                itemBUSImpl.insertEntity(t);
            }
            String[] catEightNames = {"Pepsi", "7UP", "Aquafina", "Bia 333",
                "Bia Heniken", "Pepsi Black", "Pepsi Black Lime", "Mirinda Soda Kem", "7UP Soda Chanh"};
            for (int i = 0; i < catEightNames.length; i++) {
                ItemEntity t = new ItemEntity(catEightNames[i], 30000, 100, "", true, categoryBUSImpl.getEntityById("C0008"), "item_c8_" + i + ".png");
                t.setSellingPrice();
                itemBUSImpl.insertEntity(t);
            }

            //Default topping
            ToppingEntity te = new ToppingEntity("", "DEFAULT_TOPPING", 0, 100000, "", true, new HashSet<>());
            toppingBUSImpl.insertEntity(te);

            //Topping
            String[] toppingNames = {"Dày", "Mỏng giòn", "Viền phô mai rộp rộp", "Viền phô mai", "Viền phô mai xúc xích"};
            for (int i = 0; i < toppingNames.length; i++) {
                double price = 0;
                if (i == 0) {
                    price = 0;
                } else {
                    price = 20000;
                }
                ToppingEntity t = new ToppingEntity("", toppingNames[i], price, 100, "", true, new HashSet<>());
                toppingBUSImpl.insertEntity(t);
            }

            //Item topping
            List<ItemEntity> item1 = itemBUSImpl.findByCategoryName("Pizza hải sản");
            List<ItemEntity> item2 = itemBUSImpl.findByCategoryName("Pizza thập cẩm");
            List<ItemEntity> item3 = itemBUSImpl.findByCategoryName("Pizza truyền thống");
            List<ItemEntity> item4 = itemBUSImpl.findByCategoryName("Pizza chay");
            List<String> categoriesToRemove = List.of("Pizza hải sản", "Pizza thập cẩm", "Pizza truyền thống", "Pizza chay");

            List<ToppingEntity> toppings = toppingBUSImpl.getAllEntities();
            toppings.remove(0);
            for (ItemEntity item : item1) {
                for (ToppingEntity topping : toppings) {
                    if (item.getSize() == SizeEnum.SMALL) {
                        ItemToppingEntity itemTopping = new ItemToppingEntity(item, topping);
                        itemToppingBUSImpl.insertEntity(itemTopping);
                        break;
                    }
                    ItemToppingEntity itemTopping = new ItemToppingEntity(item, topping);
                    itemToppingBUSImpl.insertEntity(itemTopping);

                }
            }

            for (ItemEntity item : item2) {
                for (ToppingEntity topping : toppings) {
                    if (item.getSize() == SizeEnum.SMALL) {
                        ItemToppingEntity itemTopping = new ItemToppingEntity(item, topping);
                        itemToppingBUSImpl.insertEntity(itemTopping);
                        break;
                    }
                    ItemToppingEntity itemTopping = new ItemToppingEntity(item, topping);
                    itemToppingBUSImpl.insertEntity(itemTopping);

                }
            }

            for (ItemEntity item : item3) {
                for (ToppingEntity topping : toppings) {
                    if (item.getSize() == SizeEnum.SMALL) {
                        ItemToppingEntity itemTopping = new ItemToppingEntity(item, topping);
                        itemToppingBUSImpl.insertEntity(itemTopping);
                        break;
                    }
                    ItemToppingEntity itemTopping = new ItemToppingEntity(item, topping);
                    itemToppingBUSImpl.insertEntity(itemTopping);

                }
            }

            for (ItemEntity item : item4) {
                for (ToppingEntity topping : toppings) {
                    if (item.getSize() == SizeEnum.SMALL) {
                        ItemToppingEntity itemTopping = new ItemToppingEntity(item, topping);
                        itemToppingBUSImpl.insertEntity(itemTopping);
                        break;
                    }
                    ItemToppingEntity itemTopping = new ItemToppingEntity(item, topping);
                    itemToppingBUSImpl.insertEntity(itemTopping);

                }
            }

            List<ItemEntity> items = itemBUSImpl.getAllEntities();

            items.removeIf(i -> categoriesToRemove.contains(i.getCategory().getName()));
            ToppingEntity dfTopping = toppingBUSImpl.getAllEntities().get(0);
            for (ItemEntity item : items) {
                ItemToppingEntity itemTopping = new ItemToppingEntity(item, dfTopping);
                itemToppingBUSImpl.insertEntity(itemTopping);
            }

            //Floor
            String[] floorNames = {"Lầu 1", "Lầu 2", "Lầu 3"};
            for (int i = 0; i < floorNames.length; i++) {
                FloorEntity f = new FloorEntity(floorNames[i], 20);
                floorBUSImpl.insertEntity(f);
            }
            //Table
            for (int j = 0; j < 15; j++) {
                TableEntity t = new TableEntity("Bàn " + (j + 1), 4, TableStatusEnum.AVAILABLE, floorBUSImpl.getEntityById("F0001"));
                tableBUSImpl.insertEntity(t);
            }
            for (int j = 16; j < 31; j++) {
                TableEntity t = new TableEntity("Bàn " + j, 4, TableStatusEnum.AVAILABLE, floorBUSImpl.getEntityById("F0002"));
                tableBUSImpl.insertEntity(t);
            }
            for (int j = 31; j < 46; j++) {
                TableEntity t = new TableEntity("Bàn " + j, 4, TableStatusEnum.AVAILABLE, floorBUSImpl.getEntityById("F0003"));
                tableBUSImpl.insertEntity(t);
            }

            //Customer
            CustomerEntity c1 = new CustomerEntity("DEFAULT_CUSTOMER", "default@gmail.com", "08386838386", new Address(), LocalDateTime.now().minusYears(20));
            customerBUSImpl.insertEntity(c1);

            CustomerEntity c2 = new CustomerEntity("C Huyền", "tranngochuyenn1909@gmail.com", "0965734559", new Address("", "", "GV", ""), LocalDateTime.now().minusYears(20));
            customerBUSImpl.insertEntity(c2);

            //Role Employee
            RoleEntity rM = new RoleEntity("MANAGER");
            RoleEntity rS = new RoleEntity("STAFF");
            roleBUSImpl.insertEntity(rS);
            roleBUSImpl.insertEntity(rM);

//            //Employee
//            EmployeeEntity e1 = new EmployeeEntity("123", "Trần Ngọc Huyền", "0964424149", "tranngochuyenn1909@gmail.com", "GV", roleBUSImpl.getEntityById("Role0001"));
//            employeeBUS.insertEntity(e1);
//
//            EmployeeEntity e2 = new EmployeeEntity("123", "Nguyễn Huỳnh Minh Hiếu", "0703553341", "koutachan1147@gmail.com", "GV", roleBUSImpl.getEntityById("Role0002"));
//            employeeBUS.insertEntity(e2);
        } catch (Exception ex) {
            Logger.getLogger(MainHuyen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
