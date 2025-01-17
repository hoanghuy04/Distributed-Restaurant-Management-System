package model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promotions")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NamedQueries({
        @NamedQuery(name = "Promotion.findAll", query = "select p from PromotionEntity p")
})
public class PromotionEntity extends BaseEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "promotion_id", columnDefinition = "nvarchar(50)")
    private String promotionId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "discountPercentage", nullable = false)
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

//    @ManyToOne
//    @MapsId("itemId")
//    @JoinColumn(name = "item_id")
//    private ItemEntity item;
}
