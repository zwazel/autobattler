package dev.zwazel.autobattler.classes.enums;

public enum UsageType {
    MANA,
    STAMINA,
    HEALTH;

    private final UsageType[] values = UsageType.values();

    UsageType() {
    }

    public UsageType[] getValues() {
        return values;
    }
}
