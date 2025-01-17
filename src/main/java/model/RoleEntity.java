package model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NamedQueries({
        @NamedQuery(name = "Role.findAll", query = "select r from RoleEntity r")
})
public class RoleEntity {
    @Id
    @EqualsAndHashCode.Include
    @PrimaryKeyJoinColumn(name = "role_id", columnDefinition = "nvarchar(50)")
    private String roleId;

    @Column(name = "role_name", nullable = false)
    private String roleName;

}
