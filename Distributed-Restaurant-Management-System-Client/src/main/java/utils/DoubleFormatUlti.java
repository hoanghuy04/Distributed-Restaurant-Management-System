/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 *
 * @author Trần Ngọc Huyền
 */
public class DoubleFormatUlti {

    private static final DecimalFormat df = new DecimalFormat("#,###");

    public static String format(double value) {
        return df.format(value);
    }

    public static double parse(String formattedString) throws ParseException {
        return df.parse(formattedString).doubleValue();
    }

}