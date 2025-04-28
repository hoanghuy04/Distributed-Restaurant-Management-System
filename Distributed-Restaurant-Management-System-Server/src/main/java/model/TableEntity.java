package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import model.enums.TableStatusEnum;

import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(
        name = "tables",
        indexes = {
                @Index(name = "idx_tables_table_id", columnList = "table_id"),
                @Index(name = "idx_tables_name", columnList = "name"),
                @Index(name = "idx_tables_table_status", columnList = "table_status"),
                @Index(name = "idx_tables_floor_id", columnList = "floor_id")
        }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TableEntity implements Serializable {
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

    @OneToMany(mappedBy = "table", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Set<OrderEntity> orders;

    public TableEntity(String tableId) {
        this.tableId = tableId;
    }

    public TableEntity(String tableId, String name) {
        setTableId(tableId);
        setName(name);
    }

    public TableEntity(String name, Integer capacity, TableStatusEnum tableStatus, FloorEntity floor) {
        setName(name);
        this.capacity = capacity;
        this.tableStatus = tableStatus;
        this.floor = floor;
    }

    public TableEntity(String tableId, String name, Integer capacity, TableStatusEnum tableStatus, FloorEntity floor) {
        setName(name);
        this.capacity = capacity;
        this.tableStatus = tableStatus;
        this.floor = floor;
    }
}