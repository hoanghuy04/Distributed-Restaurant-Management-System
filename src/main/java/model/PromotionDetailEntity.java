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

    @EmbeddedId
    private PromotionDetailId promotionDetailId;

    @Enumerated(EnumType.STRING)
    @Column(name = "promotion_type", nullable = false, columnDefinition = "nvarchar(50)")
    private PromotionTypeEnum promotionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_level", nullable = false, columnDefinition = "nvarchar(50)")
    private CustomerLevelEnum customerLevel;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private PromotionEntity promotion;
}
