/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import common.LevelCustomer;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class CustomerLevelConverter_1 implements AttributeConverter<List<LevelCustomer>, String> {

    // Chuyển từ List<LevelCustomer> sang String khi lưu vào DB
    @Override
    public  String convertToDatabaseColumn(List<LevelCustomer> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        return attribute.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    // Chuyển từ String sang List<LevelCustomer> khi lấy ra từ DB
    @Override
    public List<LevelCustomer> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return Arrays.stream(dbData.split(","))
                .map(LevelCustomer::valueOf)
                .collect(Collectors.toList());
    }
}
