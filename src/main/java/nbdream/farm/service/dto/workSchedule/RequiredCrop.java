package nbdream.farm.service.dto.workSchedule;

import nbdream.farm.exception.CropNotFoundException;

public enum RequiredCrop {
    CHILI("고추(보통재배)"),
    RICE("기계이앙재배"),
    POTATO("감자"),
    SWEETPOTATO("고구마"),
    APPLE("사과"),
    STRAWBERRY("딸기(촉성재배)"),
    GARLIC("마늘"),
    LETTUCE("상추"),
    CABBAGE("배추"),
    TOMATO("토마토");

    private final String value;

    RequiredCrop(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RequiredCrop fromValue(String value) {
        for (RequiredCrop crop : RequiredCrop.values()) {
            if (crop.value.equalsIgnoreCase(value)) {
                return crop;
            }
        }
        throw new CropNotFoundException();
    }

    public static boolean containsValue(String name) {
        for (RequiredCrop crop : RequiredCrop.values()) {
            if (crop.getValue().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
