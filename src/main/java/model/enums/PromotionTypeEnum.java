package model.enums;

public enum PromotionTypeEnum {
    ITEM ("Giảm món"),
    ORDEN ("Giảm đơn");

    private String promotionType;

    private PromotionTypeEnum(String promotionType) {
        this.promotionType = promotionType;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }
}
