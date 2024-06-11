package nbdream.accountBook.domain;

public enum AccountBookCategory {
    FARM_PRODUCT_SALES("farm_product_sales"),
    FERTILIZER("fertilizer"),
    SEEDS_AND_SEEDLINGS("seeds_and_seedlings"),
    PESTICIDES("pesticides"),
    FARM_MACHINERY("farm_machinery"),
    DISTRIBUTION_EXPENSE("distribution_expense"),
    RENT("rent"),
    SEED_SALES("seed_sales"),
    FARM_EQUIPMENT_USAGE_FEE("farm_equipment_usage_fee"),
    FARM_EQUIPMENT_RENTAL("farm_equipment_rental"),
    OTHER("other");

    private final String value;

    AccountBookCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    //소문자로 된 string을 enum으로 변환
    public static AccountBookCategory fromValue(String value) {
        for (AccountBookCategory category : AccountBookCategory.values()) {
            if (category.value.equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category value: " + value);
    }
}
