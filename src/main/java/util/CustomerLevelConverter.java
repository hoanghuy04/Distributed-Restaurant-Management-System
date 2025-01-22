/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import model.enums.CustomerLevelEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class CustomerLevelConverter implements AttributeConverter<List<CustomerLevelEnum>, String> {
    @Override
    public String convertToDatabaseColumn(List<CustomerLevelEnum> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        return attribute.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<CustomerLevelEnum> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return Arrays.stream(dbData.split(","))
                .map(CustomerLevelEnum::valueOf)
                .collect(Collectors.toList());
    }
}