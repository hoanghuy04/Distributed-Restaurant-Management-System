package dto;

import java.util.List;

public class ToppingDTO {
    private String name ;
    private int stockQty ;
    private double costPrice;
    private String desc ;
    private boolean active;
    private List<String > listItemIds;

    public ToppingDTO(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStockQty() {
        return stockQty;
    }

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getListItemIds() {
        return listItemIds;
    }

    public void setListItemIds(List<String> listItemIds) {
        this.listItemIds = listItemIds;
    }
}
