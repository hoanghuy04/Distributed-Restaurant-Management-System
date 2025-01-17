/*
 * @ (#) ToppingEntity.java      1.0      1/17/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

/*
 * @description: ToppingEntity
 * @author: Trần Ngọc Huyền
 * @date: 1/17/2025
 * @version: 1.0
 */
@Data
@Entity
@Table(name = "toppings")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ToppingEntity {
    @Id
    @Column(name = "item_id")
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "name", nullable = false, unique = true, columnDefinition = "nvarchar(255)")
    private String name;

    @Column(name = "cost_price", nullable = false)
    private double costPrice;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    private String description;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ToString.Exclude
    @OneToMany(mappedBy = "topping")
    private Set<ItemToppingEntity> itemToppings;

    public ToppingEntity() {

    }

    public ToppingEntity(String id, String name, double costPrice, int stockQuantity,
                         String description, boolean active,
                         Set<ItemToppingEntity> itemToppings) throws Exception {
        this.id = id;
        this.description = description;
        this.active = active;
        this.itemToppings = itemToppings;
        setName(name);
        setCostPrice(costPrice);
        setStockQuantity(stockQuantity);
    }

    public void setName(String name) throws Exception {
        if (name.trim().isEmpty() || name.trim().isBlank()) {
            throw new Exception("Tên topping không được để trống");
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
