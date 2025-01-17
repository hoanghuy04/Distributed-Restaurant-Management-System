package model;

import jakarta.persistence.*;
import lombok.*;
import model.enums.CustomerLevelEnum;
import model.enums.OrderStatusEnum;
import model.enums.PromotionTypeEnum;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promotion_details")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NamedQueries({
        @NamedQuery(name = "PromotionDetail.findAll", query = "select p from PromotionDetailEntity p")
})
public class PromotionDetailEntity {

    @Id
    @Column(name = "promotion_id", columnDefinition = "nvarchar(50)")
    private String promotionId;

    @Id
    @EqualsAndHashCode.Exclude
    @Column(name = "item_id", columnDefinition = "nvarchar(50)")
    private String itemId;

    @Enumerated(EnumType.STRING)
    @Column(name = "promotion_type", nullable = false, columnDefinition = "nvarchar(50)")
    private PromotionTypeEnum promotionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_level", nullable = false, columnDefinition = "nvarchar(50)")
    private CustomerLevelEnum customerLevel;

    @Column(name = "description")
    private String description;

    @ToString.Exclude
    @ManyToOne
    private PromotionEntity promotion;
}
