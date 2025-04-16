/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;


/**
 *
 * @author Trần Ngọc Huyền
 */
public class NumberUltis {

    public static boolean isInt(String value) {
        try {
            Integer number = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
