package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/*
 * @description: CategoryEntity
 * @author: Trần Ngọc Huyền
 * @date: 1/17/2025
 * @version: 1.0
 */
@Data
@Entity
@Table(name = "categories")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CategoryEntity {
    @Id
    @Column(name = "category_id")
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(255)")
    private String name;
    private String description;
    private boolean active;

    public CategoryEntity() {

    }

    public CategoryEntity(String id, String name, String description, boolean active) throws Exception {
        this.id = id;
        this.description = description;
        this.active = active;
        setName(name);
    }

    public void setName(String name) throws Exception {
        if (name.trim().isEmpty() || name.trim().isBlank()) {
            throw new Exception("Tên danh mục sản phẩm không được để trống");
        }
        this.name = name;
    }
}
