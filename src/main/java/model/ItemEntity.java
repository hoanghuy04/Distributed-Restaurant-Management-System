/*
 * @ (#) ItemEntity.java      1.0      1/17/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package model;

/*
 * @description: ItemEntity
 * @author: Trần Ngọc Huyền
 * @date: 1/17/2025
 * @version: 1.0
 */

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import model.enums.SizeEnum;

import java.util.Set;

@Data
@Entity
@Table(name = "items")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemEntity {
    @Id
    @Column(name = "item_id")
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "name", nullable = false, unique = true, columnDefinition = "nvarchar(255)")
    private String name;

    @Column(name = "cost_price", nullable = false)
    private double costPrice;

    @Column(name = "selling_price", nullable = false)
    private double sellingPrice;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    private String description;

    private final double VAT = 0.2;

    private String img;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Enumerated(EnumType.STRING)
    private SizeEnum size;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ToString.Exclude
    @OneToMany(mappedBy = "item")
    private Set<ItemToppingEntity> itemToppings;

    public ItemEntity() {

    }

    public ItemEntity(String id, String name, double costPrice,
                      int stockQuantity, String description, String img, boolean active,
                      SizeEnum size, CategoryEntity category, Set<ItemToppingEntity> itemToppings) throws Exception {
        this.id = id;
        this.description = description;
        this.img = img;
        this.active = active;
        this.size = size;
        this.category = category;
        this.itemToppings = itemToppings;
        setCostPrice(costPrice);
        setStockQuantity(stockQuantity);
        setName(name);
        setSellingPrice();
    }

    public void setSellingPrice() {
        double sizePrice = 0;
        if (size == SizeEnum.SMALL) {
            sizePrice = 0;
        } else if (size == SizeEnum.MEDIUM) {
            sizePrice = 40000;
        } else if (size == SizeEnum.LARGE) {
            sizePrice = 100000;
        }
        this.sellingPrice = this.costPrice * (2 + getVAT()) + sizePrice;
    }

    public void setName(String name) throws Exception {
        if (name.trim().isEmpty() || name.trim().isBlank()) {
            throw new Exception("Tên sản phẩm không được để trống");
        }
        this.name = name;
    }

    public void setCostPrice(double costPrice) {
        if (costPrice < 0)
            this.costPrice = 0;
        else
            this.costPrice = costPrice;
    }

    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity < 0)
            this.stockQuantity = 0;
        else
            this.stockQuantity = stockQuantity;
    }
}
