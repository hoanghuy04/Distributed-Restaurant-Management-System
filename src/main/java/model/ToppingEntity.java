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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NamedQueries({
        @NamedQuery(name = "ToppingEntity.findAll", query = "select t from ToppingEntity t")
})
public class ToppingEntity extends BaseEntity {
    @Id
    @Column(name = "topping_id", columnDefinition = "nvarchar(50)")
    @EqualsAndHashCode.Include
    private String toppingId;

    @Column(name = "name", nullable = false, unique = true, columnDefinition = "nvarchar(255)")
    private String name;

    @Column(name = "cost_price", nullable = false)
    private double costPrice;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ToString.Exclude
    @OneToMany(mappedBy = "topping")
    private Set<ItemToppingEntity> itemToppings;

    public ToppingEntity() {

    }

    public ToppingEntity(String toppingId, String name, double costPrice, int stockQuantity,
                         String description, boolean active,
                         Set<ItemToppingEntity> itemToppings) throws Exception {
        this.toppingId = toppingId;
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
