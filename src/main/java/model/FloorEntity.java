package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "floors")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NamedQueries({
    @NamedQuery(name = "FloorEntity.findAll", query = "select f from FloorEntity f")
})
public class FloorEntity implements Serializable {

    @Id
    @Column(name = "floor_id", nullable = false, columnDefinition = "nvarchar(50)")
    @EqualsAndHashCode.Include
    private String floorId;

    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(50)")
    private String name;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @ToString.Exclude
    @OneToMany(mappedBy = "floor", cascade = CascadeType.REMOVE)
    private Set<TableEntity> tables;

    public FloorEntity(String floorId) {
        setFloorId(floorId);
    }

    public FloorEntity(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public FloorEntity(String floorId, String name, int capacity) {
        setFloorId(floorId);
        this.name = name;
        this.capacity = capacity;
    }
}
