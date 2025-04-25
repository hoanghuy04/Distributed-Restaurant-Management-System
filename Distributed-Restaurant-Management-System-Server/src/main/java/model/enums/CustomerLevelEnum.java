package model.enums;

public enum CustomerLevelEnum {
    NEW ("Khách hàng mới"),
    POTENTIAL ("Khách hàng tiềm năng"),
    VIP ("Khách hàng thâm thiết");

    private String customerLevel;

    private CustomerLevelEnum(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }
    
    public static CustomerLevelEnum convertToEnum(String text) {
        for (CustomerLevelEnum level : CustomerLevelEnum.values()) {
            if (level.customerLevel.equalsIgnoreCase(text)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy LevelCustomer cho: " + text);
    }
}
