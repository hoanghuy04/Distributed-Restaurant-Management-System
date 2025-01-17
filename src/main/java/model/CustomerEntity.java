package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import model.enums.CustomerLevelEnum;
import org.hibernate.query.Order;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "customers")
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

    @Column(name = "address", nullable = false)
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
    private List<OrderEntity> orders;

}
