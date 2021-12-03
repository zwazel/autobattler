package dev.zwazel.autobattler.classes.abilities;

import dev.zwazel.autobattler.classes.enums.AbilityOutputType;
import dev.zwazel.autobattler.classes.enums.AbilityType;
import dev.zwazel.autobattler.classes.enums.UsageType;

public abstract class Ability {
    private AbilityType type;
    private final UsageType costType;
    private final AbilityOutputType outputType;
    private String title;
    private String description;
    private final int cooldown;

    public Ability(UsageType costType, int usageCost, AbilityOutputType outputType, int cooldown) {
        costType.setAmount(usageCost);
        this.costType = costType;
        this.cooldown = cooldown;
        this.outputType = outputType;
    }

    public abstract boolean canBeUsed();

    public UsageType getCostType() {
        return costType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AbilityType getType() {
        return type;
    }

    public void setType(AbilityType type) {
        this.type = type;
    }

    public int getCooldown() {
        return cooldown;
    }


    @Override
    public String toString() {
        return "Ability{" +
                "type=" + type +
                ", costType=" + costType +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", cooldown=" + cooldown +
                '}';
    }
}
