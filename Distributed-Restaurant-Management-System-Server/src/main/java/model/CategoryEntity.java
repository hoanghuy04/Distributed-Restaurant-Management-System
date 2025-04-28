package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
 * @description: CategoryEntity
 * @author: Trần Ngọc Huyền
 * @date: 1/17/2025
 * @version: 1.0
 */
@Data
@Entity
@Table(name = "categories",   indexes = {
        @Index(name = "idx_category_id", columnList = "category_id"),
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_active", columnList = "active")
})

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NamedQueries({
        @NamedQuery(name = "CategoryEntity.findAll", query = "select c from CategoryEntity c"),
        @NamedQuery(name = "CategoryEntity.findByName", query = "select c from CategoryEntity c where c.name = :name")
})
public class CategoryEntity extends BaseEntity implements Serializable{
    @Id
    @Column(name = "category_id", columnDefinition = "nvarchar(50)")
    @EqualsAndHashCode.Include
    private String categoryId;

    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(255)")
    private String name;

    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active;

    public CategoryEntity() {

    }

    public CategoryEntity(String categoryId, String name, String description, boolean active) throws Exception {
        this.categoryId = categoryId;
        this.description = description;
        this.active = active;
        setName(name);
    }
    
    public CategoryEntity(String categoryId) {
        setCategoryId(categoryId);
    }

    public CategoryEntity(String name, String description) {
        this.name = name;
        this.description = description;
        this.active = true;
    }

    public void setName(String name) throws Exception {
        if (name.trim().isEmpty() || name.trim().isBlank()) {
            throw new Exception("Tên danh mục sản phẩm không được để trống");
        }
        this.name = name;
    }
}
