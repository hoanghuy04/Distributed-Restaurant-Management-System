package model;


import jakarta.persistence.*;
import lombok.*;
import model.enums.CustomerLevelEnum;
import model.enums.PromotionTypeEnum;
import util.CustomerLevelConverter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "promotions",
        indexes = {
                @Index(name = "idx_promotion_id", columnList = "promotion_id"),
                @Index(name = "idx_promotion_active", columnList = "active"),
                @Index(name = "idx_promotion_type", columnList = "promotion_type"),
                @Index(name = "idx_promotion_active_type", columnList = "active, promotion_type"),
                @Index(name = "idx_promotion_date", columnList = "started_date, ended_date"),
                @Index(name = "idx_promotion_min_price", columnList = "min_price")
        }
)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NamedQueries({
        @NamedQuery(name = "PromotionEntity.findAll", query = "select p from PromotionEntity p")
})
public class PromotionEntity extends BaseEntity implements Serializable {

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
    private LocalDateTime startedDate;


    @Column(name = "ended_date", nullable = false)
    private LocalDateTime endedDate;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "min_price", nullable = false)
    private double minPrice;

    @ToString.Exclude
    @OneToMany(mappedBy = "promotion", fetch = FetchType.EAGER)
    private List<OrderEntity> orders;

    @ToString.Exclude
    @OneToMany(mappedBy = "promotion", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<PromotionDetailEntity> promotionDetails;
    
    public PromotionEntity(String description, double discountPercentage, LocalDateTime startedDate, LocalDateTime endedDate,
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

    public PromotionEntity(String promotionId, String description, double discountPercentage, double minPrice, LocalDateTime startedDate,
            LocalDateTime endedDate, boolean active, LocalDateTime creatededDate, List<OrderEntity> orders, Set<PromotionDetailEntity> promotionDetails,
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
