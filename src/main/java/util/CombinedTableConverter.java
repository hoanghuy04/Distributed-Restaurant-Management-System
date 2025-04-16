/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import bus.TableBUS;
import model.TableEntity;
import gui.FormLoad;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author pc
 */
@Converter
public class CombinedTableConverter implements AttributeConverter<List<TableEntity>, String> {
    @Override
    public String convertToDatabaseColumn(List<TableEntity> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }

        return attribute.stream().map(TableEntity::getTableId).collect(Collectors.joining(","));
    }

    @Override
    public List<TableEntity> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(dbData.split(","))
                .map(idStr -> {
//                    TableEntity t = Application.tableBUS.getEntityById(idStr);
                    TableEntity t = new TableEntity(idStr);
                    return t;
                }).collect(Collectors.toList());
    }

}
