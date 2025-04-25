package model;

import jakarta.persistence.*;
import lombok.*;
import model.enums.CustomerLevelEnum;
import model.enums.OrderStatusEnum;
import model.enums.PromotionTypeEnum;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promotion_details")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NamedQueries({
        @NamedQuery(name = "PromotionDetailEntity.findAll", query = "select p from PromotionDetailEntity p"),
        @NamedQuery(name = "PromotionDetailEntity.findByPromotionId", query = "select p from PromotionDetailEntity p where p.promotion.promotionId = :promotionId"),
        @NamedQuery(name = "PromotionDetailEntity.findByItemId", query = "select p from PromotionDetailEntity p where p.item.itemId = :itemId")
})
public class PromotionDetailEntity implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "promotion_id", nullable = false)
    private PromotionEntity promotion;

    @Id
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;
}