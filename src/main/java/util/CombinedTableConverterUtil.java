/*
 * @ (#) CombinedTableConverterUtil.java      1.0      1/18/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import model.TableEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 1/18/2025
 * @version: 1.0
 */
@Converter(autoApply = true)
public class CombinedTableConverterUtil  implements AttributeConverter<List<TableEntity>, String> {
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
                    TableEntity t = new TableEntity();
                    t.setTableId(idStr);
                    return t;
                }).collect(Collectors.toList());
    }
}
