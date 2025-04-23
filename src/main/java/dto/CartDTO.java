/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import model.ItemEntity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Trần Ngọc Huyền
 */
public class CartDTO implements Serializable {

    private Map<ItemCartDTO, Integer> cart;
    private int qty = 0;

    public CartDTO() {
        cart = new LinkedHashMap<>();
    }

    public CartDTO(Map<ItemCartDTO, Integer> cart) {
        this.cart = cart;

    }

    public Map<ItemCartDTO, Integer> getCart() {
        return cart;
    }

    public void setCart(Map<ItemCartDTO, Integer> cart) {
        this.cart = cart;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.cart);
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
        final CartDTO other = (CartDTO) obj;
        return Objects.equals(this.cart, other.cart);
    }


    @Override
    public String toString() {
        return "CartDTO{"
                + "cart=" + cart
                + ", qty=" + qty
                + '}';
    }

    public boolean insert(ItemCartDTO itemCartDTO) {
        if (cart.containsKey(itemCartDTO)) {
            int curQty = cart.get(itemCartDTO);
            cart.put(itemCartDTO, ++curQty);
        } else {
            cart.put(itemCartDTO, 1);
            ++qty;
        }
        return true;
    }

    public ItemCartDTO findItem(ItemCartDTO itemCartDTO) {
        for (ItemCartDTO it : cart.keySet()) {
            if (it.equals(itemCartDTO)) {
                return it;
            }
        }
        return null;
    }

    public int getItemQty(ItemCartDTO itemCartDTO) {
        ItemCartDTO foundItem = findItem(itemCartDTO);
        if (foundItem != null) {
            return cart.get(foundItem);
        }
        return 0;
    }

    public void updateQty(ItemCartDTO itemCartDTO, int newQty) {
        if (newQty <= 0) {
            cart.remove(itemCartDTO);
            --qty;
        } else {
            cart.put(itemCartDTO, newQty);
        }
    }

    public double getTotalPrice() {
        double price = 0;
        for (Map.Entry<ItemCartDTO, Integer> entry : cart.entrySet()) {
            double itemPrice = entry.getKey().getItem().getSellingPrice();
            double toppingPrice = (entry.getKey().getItemTopping() != null) ? entry.getKey().getItemTopping().getSellingPrice() : 0;
            price += (itemPrice + toppingPrice) * entry.getValue();
        }
        return price;
    }

    public double getItemDiscount() {
        double itemDiscount = 0;
        for (Map.Entry<ItemCartDTO, Integer> entry : cart.entrySet()) {
            ItemEntity item = entry.getKey().getItem();
            if (item.getTopDiscountPercentage() > 0) {
                itemDiscount += item.getSellingPrice() * item.getTopDiscountPercentage() * entry.getValue();
            }
        }
        return itemDiscount;
    }

    public double getTotalPaid(double deposit, double orderDiscount) {
        return getTotalPrice() - getItemDiscount() - orderDiscount - deposit;
    }

    public ItemCartDTO findItemByDesc(String desc) {
        for (ItemCartDTO itemCart : cart.keySet()) {
            if (itemCart.getDescription().equals(desc)) {
                return itemCart;
            }
        }
        return null;
    }

}
