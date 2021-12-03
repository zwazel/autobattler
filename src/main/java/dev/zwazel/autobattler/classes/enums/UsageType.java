package dev.zwazel.autobattler.classes.enums;

public enum UsageType {
    MANA,
    STAMINA,
    HEALTH;

    private int amount;
    private final UsageType[] values = UsageType.values();

    UsageType(int amount) {
        this.amount = amount;
    }

    UsageType() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public UsageType[] getValues() {
        return values;
    }
}
