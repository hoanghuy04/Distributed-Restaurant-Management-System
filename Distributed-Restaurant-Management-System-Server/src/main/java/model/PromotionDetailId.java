/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PromotionDetailId implements Serializable {

    @Column(name = "promotion_id")
    private String promotionId;

    @Column(name = "item_id")
    private String itemId;

    public PromotionDetailId() {
        
    }
    
    public PromotionDetailId(String promotionId, String itemId) {
        setItemId(itemId);
        setPromotionId(promotionId);
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.promotionId);
        hash = 13 * hash + Objects.hashCode(this.itemId);
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
        final PromotionDetailId other = (PromotionDetailId) obj;
        if (!Objects.equals(this.promotionId, other.promotionId)) {
            return false;
        }
        return Objects.equals(this.itemId, other.itemId);
    }

    @Override
    public String toString() {
        return "PromotionDetailId{" + "promotionId=" + promotionId + ", itemId=" + itemId + '}';
    }

    
}