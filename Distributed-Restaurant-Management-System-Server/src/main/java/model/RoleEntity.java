package model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NamedQueries({
    @NamedQuery(name = "RoleEntity.findAll", query = "select r from RoleEntity r"),
        @NamedQuery(name = "RoleEntity.findByName", query = "select r from RoleEntity r where r.roleName = :name"),
})
public class RoleEntity implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "role_id", columnDefinition = "nvarchar(50)")
    private String roleId;

    @Column(name = "role_name", nullable = false)
    private String roleName;
    
    public RoleEntity(String roleName) {
        this.roleName = roleName;
    }

    public RoleEntity(String roleId, String roleName, LocalDate createdDate) {
        this.roleId = roleId;
        this.roleName = roleName;
    }
}
