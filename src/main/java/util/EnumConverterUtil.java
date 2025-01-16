/*
 * @ (#) EnumConverterUtil.java      1.0      1/16/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package util;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 1/16/2025
 * @version: 1.0
 */
public class EnumConverterUtil {
    public static <E extends Enum<E>> E convertToEnum(Class<E> enumClass, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static <E extends Enum<E>> String convertToString(E enumValue) {
        if (enumValue == null) {
            return null;
        }
        return enumValue.name();
    }
}
