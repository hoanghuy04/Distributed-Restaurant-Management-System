package model;


import jakarta.persistence.*;
import lombok.*;
import model.enums.CustomerLevelEnum;
import model.enums.PromotionTypeEnum;
import util.CustomerLevelConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promotions")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NamedQueries({
        @NamedQuery(name = "PromotionEntity.findAll", query = "select p from PromotionEntity p")
})
public class PromotionEntity extends BaseEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "promotion_id", columnDefinition = "nvarchar(50)")
    private String promotionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "promotion_type", nullable = false, columnDefinition = "nvarchar(50)")
    private PromotionTypeEnum promotionType;

    @Column(name = "customer_levels", nullable = false)
    @Convert(converter = CustomerLevelConverter.class)
    private List<CustomerLevelEnum> customerLevels;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "discount_percentage", nullable = false)
    private double discountPercentage;

    @Column(name = "started_date", nullable = false)
    private LocalDate startedDate;


    @Column(name = "ended_date", nullable = false)
    private LocalDate endedDate;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "min_price", nullable = false)
    private double minPrice;

    @ToString.Exclude
    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    private List<OrderEntity> orders;

    @ToString.Exclude
    @OneToMany(mappedBy = "promotion")
    private Set<PromotionDetailEntity> promotionDetails;
    
    public PromotionEntity(String description, double discountPercentage, LocalDate startedDate, LocalDate endedDate,
            boolean active, Set<PromotionDetailEntity> promotionDetails, List<CustomerLevelEnum> applicableCustomerLevels, PromotionTypeEnum promotionType, double minPrice) {
        this.description = description;
        this.discountPercentage = discountPercentage;
        this.startedDate = startedDate;
        this.endedDate = endedDate;
        this.active = active;
        this.promotionDetails = promotionDetails;
        this.customerLevels = applicableCustomerLevels;
        this.promotionType = promotionType;
        this.minPrice = minPrice;
    }

    public PromotionEntity(String promotionId, String description, double discountPercentage, double minPrice, LocalDate startedDate,
            LocalDate endedDate, boolean active, LocalDateTime creatededDate, List<OrderEntity> orders, Set<PromotionDetailEntity> promotionDetails, 
            List<CustomerLevelEnum> applicableCustomerLevels, PromotionTypeEnum promotionType) {
        this.promotionId = promotionId;
        this.description = description;
        this.discountPercentage = discountPercentage;
        this.minPrice = minPrice;
        this.startedDate = startedDate;
        this.endedDate = endedDate;
        this.active = active;
        this.orders = orders;
        this.promotionDetails = promotionDetails;
        this.customerLevels = applicableCustomerLevels;
        this.promotionType = promotionType;
    }

//    @ManyToOne
//    @MapsId("itemId")
//    @JoinColumn(name = "item_id")
//    private ItemEntity item;
}
