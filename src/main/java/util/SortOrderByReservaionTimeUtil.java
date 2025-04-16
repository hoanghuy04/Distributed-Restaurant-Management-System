/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import model.OrderEntity;
import java.util.List;

/**
 *
 * @author hoang
 */
public class SortOrderByReservaionTimeUtil {
        public static void sortAscByFloorAndTableId(List<OrderEntity> reservations) {
        reservations.sort((o1, o2) -> {
            // Kiểm tra null cho Table và Floor
            if (o1.getTable() == null || o2.getTable() == null
                    || o1.getTable().getFloor() == null || o2.getTable().getFloor() == null) {
                return 0;  // Nếu một trong các giá trị là null, bỏ qua so sánh
            }

            // Lấy FloorId từ chuỗi F0001
            String floorId1 = o1.getTable().getFloor().getFloorId();
            String floorId2 = o2.getTable().getFloor().getFloorId();

            // Lấy phần số sau ký tự F
            int floor1 = Integer.parseInt(floorId1.substring(1));  // floorId: F0001 -> 0001
            int floor2 = Integer.parseInt(floorId2.substring(1));

            // Lấy TableId từ chuỗi F0001T0001
            String tableId1 = o1.getTable().getTableId();
            String tableId2 = o2.getTable().getTableId();

            // Lấy phần số sau ký tự T (bỏ qua phần F0001)
            int table1 = Integer.parseInt(tableId1.substring(tableId1.indexOf('T') + 1));  // tableId: F0001T0001 -> 0001
            int table2 = Integer.parseInt(tableId2.substring(tableId2.indexOf('T') + 1));

            // So sánh tầng trước
            int floorComparison = Integer.compare(floor1, floor2);
            if (floorComparison != 0) {
                return floorComparison;
            }

            // Nếu tầng giống nhau, so sánh bàn
            int tableComparison = Integer.compare(table1, table2);
            if (tableComparison != 0) {
                return tableComparison;
            }

            // So sánh thời gian đặt bàn, kiểm tra null
            if (o1.getReservationTime() == null || o2.getReservationTime() == null) {
                return 0; // Bỏ qua nếu thời gian đặt bàn là null
            }

            // Nếu bàn giống nhau, so sánh thời gian đặt bàn (ngược lại)
            return o1.getReservationTime().compareTo(o2.getReservationTime());
        });
    }
}
