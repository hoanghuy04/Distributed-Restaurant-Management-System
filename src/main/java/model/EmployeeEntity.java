package model;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NamedQueries({
    @NamedQuery(name = "EmployeeEntity.findByPhoneNumber", query = "SELECT e FROM EmployeeEntity e WHERE e.phoneNumber like :phoneNumber"),
    @NamedQuery(name = "EmployeeEntity.findByEmail", query = "SELECT e FROM EmployeeEntity e WHERE e.email like :email"),
    @NamedQuery(name = "EmployeeEntity.findAll", query = "select e from EmployeeEntity e")
})
public class EmployeeEntity extends BaseEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "employee_id", columnDefinition = "nvarchar(50)")
    private String employeeId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "fullname", nullable = false)
    private String fullname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Embedded
    @Column(name = "address", nullable = false)
    private Address address;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    public EmployeeEntity(String employeeId) {
        setEmployeeId(employeeId);
    }

    public EmployeeEntity(String password, String fullname, String phoneNumber, String email, String address, RoleEntity role) {
        this.password = password;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.email = email;
//        this.address = address;
        this.role = role;
        this.active = true;
    }

    public EmployeeEntity(String employeeId, String password, String fullname, String phoneNumber, String email, String address, RoleEntity role, boolean active, List<OrderEntity> orders) {
        this.employeeId = employeeId;
        this.password = password;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.email = email;
//        this.address = address;
        this.role = role;
        this.active = active;
    }
}
