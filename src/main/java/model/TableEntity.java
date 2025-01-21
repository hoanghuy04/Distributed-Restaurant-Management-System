package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import model.enums.TableStatusEnum;

import java.util.Set;

@Data
@Entity
@Table(name = "tables")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NamedQueries({
        @NamedQuery(name = "TableEntity.findAll", query = "select t from TableEntity t")
})
public class TableEntity {
    @Id
    @Column(name = "table_id", columnDefinition = "nvarchar(50)")
    @EqualsAndHashCode.Include
    private String tableId;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name ="name", columnDefinition = "nvarchar(50)")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "table_status", nullable = false, columnDefinition = "nvarchar(50)")
    private TableStatusEnum tableStatus;

    @ManyToOne
    @JoinColumn(name = "floor_id")
    @ToString.Exclude
    private FloorEntity floor;

    @OneToMany(mappedBy = "table", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Set<OrderEntity> orders;
}
