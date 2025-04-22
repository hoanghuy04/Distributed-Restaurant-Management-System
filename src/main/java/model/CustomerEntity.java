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
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "customers")
@NamedQueries({
        @NamedQuery(name = "CustomerEntity.findAll", query = "select c from CustomerEntity c"),
        @NamedQuery(name = "CustomerEntity.findByPhone", query = "select c from CustomerEntity c where c.phone = :phone"),
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

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Set<OrderEntity> orders;

    public CustomerEntity(String name, String email, String phone, Address address, LocalDateTime dayOfBirth) {
        setName(name);
        setEmail(email);
        setPhone(phone);
//        setAddress(address);
        setDayOfBirth(dayOfBirth);
        setCustomerLevel(CustomerLevelEnum.NEW);
    }
    
    public CustomerEntity(String customerId) {
        setCustomerId(customerId);
    }

    public CustomerEntity(String customerId, String name, String email, String phoneNumber, Address address, LocalDateTime createdDate, LocalDateTime dayOfBirth, Set<OrderEntity> orders, int rewardedPoint, CustomerLevelEnum levelCustomer) {
        setCustomerId(customerId);
        setName(name);
        setEmail(email);
        setPhone(phoneNumber);
        setAddress(address);
        setCreatedDate(createdDate);
        setDayOfBirth(dayOfBirth);
        setOrders(orders);
        this.rewardedPoint = rewardedPoint;
        setCustomerLevel(levelCustomer);
    }

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

    public void setRewardedPoint() {
        this.rewardedPoint = getRewardedPoint();
    }

    public void setCustomerLevel() {
        this.customerLevel = getLevelCustomer();
    }
}
