package model;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.util.Objects;

@Embeddable
public class OrderDetailId implements Serializable {

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "topping_id")
    private String toppingId;

    public OrderDetailId() {
    }

    public OrderDetailId(String itemId, String orderId, String toppingId) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.toppingId = toppingId;
    }

    // Getters and setters
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getToppingId() {
        return toppingId;
    }

    public void setToppingId(String toppingId) {
        this.toppingId = toppingId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.itemId);
        hash = 13 * hash + Objects.hashCode(this.orderId);
        hash = 13 * hash + Objects.hashCode(this.toppingId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrderDetailId other = (OrderDetailId) obj;
        if (!Objects.equals(this.itemId, other.itemId)) {
            return false;
        }
        if (!Objects.equals(this.orderId, other.orderId)) {
            return false;
        }
        return Objects.equals(this.toppingId, other.toppingId);
    }

    @Override
    public String toString() {
        return "OrderDetailId{" + "itemId=" + itemId + ", orderId=" + orderId + ", toppingId=" + toppingId + '}';
    }

}
