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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NamedQueries({
        @NamedQuery(name = "ItemEntity.findAll", query = "select i from ItemEntity i")
})
public class ItemEntity extends BaseEntity {
    @Id
    @Column(name = "item_id", columnDefinition = "nvarchar(50)")
    @EqualsAndHashCode.Include
    private String itemId;

    @Column(name = "name", nullable = false, unique = true, columnDefinition = "nvarchar(255)")
    private String name;

    @Column(name = "cost_price", nullable = false)
    private double costPrice;

    @Column(name = "selling_price", nullable = false)
    private double sellingPrice;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;

    private final double VAT = 0.2;

    @Column(name = "img", columnDefinition = "nvarchar(5000)")
    private String img;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "size", nullable = false, columnDefinition = "nvarchar(50)")
    @Enumerated(EnumType.STRING)
    private SizeEnum size;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false, columnDefinition = "nvarchar(50)")
    private CategoryEntity category;

    @ToString.Exclude
    @OneToMany(mappedBy = "item")
    private Set<PromotionDetailEntity> promotionDetailEntities;


    @ToString.Exclude
    @OneToMany(mappedBy = "item")
    private Set<ItemToppingEntity> itemToppings;

//    @ToString.Exclude
//    @OneToMany(mappedBy = "item")
//    private Set<PromotionDetailEntity> promotionDetails;

    public ItemEntity() {

    }

    public ItemEntity(String itemId, String name, double costPrice,
                      int stockQuantity, String description, String img, boolean active,
                      SizeEnum size, CategoryEntity category, Set<ItemToppingEntity> itemToppings) throws Exception {
        this.itemId = itemId;
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
