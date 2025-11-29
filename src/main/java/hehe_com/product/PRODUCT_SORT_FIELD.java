package hehe_com.product;

public enum PRODUCT_SORT_FIELD {
    
    NAME("NAME"),
    PRICE("PRICE"),
    STOCK("STOCK"),
    UPDATED_AT("UPDATED_AT");
    
    private final String value;
    
    PRODUCT_SORT_FIELD(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static PRODUCT_SORT_FIELD fromString(String str) {
        if (str == null) return null;
        for (PRODUCT_SORT_FIELD field : PRODUCT_SORT_FIELD.values()) {
            if (field.value.equalsIgnoreCase(str)) {
                return field;
            }
        }
        throw new IllegalArgumentException("Unknown sort field: " + str);
    }
}
