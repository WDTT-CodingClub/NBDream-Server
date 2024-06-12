package nbdream.accountBook.domain;

import nbdream.accountBook.exception.TransactionTypeNotFoundException;

public enum TransactionType {
    EXPENSE("expense"),
    REVENUE("revenue");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // 소문자로 된 string을 enum으로 변환
    public static TransactionType fromValue(String value) {
        for (TransactionType type : TransactionType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new TransactionTypeNotFoundException();
    }
}
