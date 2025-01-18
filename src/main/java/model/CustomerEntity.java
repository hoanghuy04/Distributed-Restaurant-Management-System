package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import model.enums.CustomerLevelEnum;
import org.hibernate.Hibernate;
import org.hibernate.query.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "customers")
@NamedQueries({
        @NamedQuery(name = "CustomerEntity.findAll", query = "select c from CustomerEntity c")
})
public class CustomerEntity extends BaseEntity{
    @Id
    @Column(name = "customer_id", columnDefinition = "nvarchar(50)")
    @EqualsAndHashCode.Include
    private String customerId;

    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(255)")
    private String name;

    @Column(name = "email", nullable = false, columnDefinition = "nvarchar(255)")
    private String email;

    @Column(name = "phone", nullable = false, columnDefinition = "nvarchar(255)")
    private String phone;

    @Embedded
    private Address address;

    @Column(name = "day_of_birth")
    private LocalDateTime dayOfBirth;

    @Column(name = "rewarded_point")
    private int rewardedPoint;

    @Column(name = "customer_level", nullable = false, columnDefinition = "nvarchar(255)")
    @Enumerated(EnumType.STRING)
    private CustomerLevelEnum customerLevel;

    @OneToMany(mappedBy = "customer")
    @ToString.Exclude
    private Set<OrderEntity> orders;

    //Ghet m qua huy oi
//    @PrePersist
//    @PreUpdate
//    public void calculateDerivedFields() {
//        if (orders != null && Hibernate.isInitialized(orders)) {
//            this.rewardedPoint = getRewardedPoint();
//            this.customerLevel = getLevelCustomer();
//        } else {
//            this.rewardedPoint = 0;
//            this.customerLevel = CustomerLevelEnum.NEW;
//        }
//    }

    public int getRewardedPoint() {
        if (orders == null || orders.isEmpty()) {
            return 0;
        }
        return orders.stream()
                .mapToInt(order -> (int) (order.getTotalPrice() / 100000))
                .sum();
    }

    public CustomerLevelEnum getLevelCustomer() {
        int rewardPoint = getRewardedPoint();
        if (rewardPoint <= 500) {
            return CustomerLevelEnum.NEW;
        } else if (rewardPoint <= 2000) {
            return CustomerLevelEnum.POTENTIAL;
        } else {
            return CustomerLevelEnum.VIP;
        }
    }
}
