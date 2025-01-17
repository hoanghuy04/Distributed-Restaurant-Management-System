package model;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NamedQueries({
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

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "active", nullable = false)
    private boolean active;



}
