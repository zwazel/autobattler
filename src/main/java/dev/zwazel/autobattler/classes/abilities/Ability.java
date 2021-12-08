package dev.zwazel.autobattler.classes.abilities;

import dev.zwazel.autobattler.classes.RoundAffected;
import dev.zwazel.autobattler.classes.Utils.Vector;
import dev.zwazel.autobattler.classes.enums.AbilityOutputType;
import dev.zwazel.autobattler.classes.enums.AbilityType;
import dev.zwazel.autobattler.classes.enums.UsageType;

public abstract class Ability extends RoundAffected {
    private final UsageType costType;
    private final AbilityOutputType outputType;
    private final int outPutAmount;
    private final int cooldown;
    private int currentCooldown = 0;
    private AbilityType type;
    private int usageCostAmount;
    private String title;
    private String description;
    private int range;

    public Ability(UsageType costType, int usageCost, AbilityOutputType outputType, int outPutAmount, int cooldown, int range) {
        this.costType = costType;
        this.cooldown = cooldown;
        this.outputType = outputType;
        this.usageCostAmount = usageCost;
        this.range = range;
        this.outPutAmount = outPutAmount;
    }

    public abstract boolean canBeUsed();

    public abstract boolean use();

    // TODO: 07.12.2021
    public boolean isInRange(Vector self, Vector target) {
        return false;
    }

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

    public int getUsageCostAmount() {
        return usageCostAmount;
    }

    public void setUsageCostAmount(int usageCostAmount) {
        this.usageCostAmount = usageCostAmount;
    }

    public AbilityOutputType getOutputType() {
        return outputType;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getCurrentCooldown() {
        return currentCooldown;
    }

    public void setCurrentCooldown(int currentCooldown) {
        this.currentCooldown = currentCooldown;
    }

    public int getOutPutAmount() {
        return outPutAmount;
    }

    @Override
    public String toString() {
        return "Ability{" +
                "costType=" + costType +
                ", outputType=" + outputType +
                ", outPutAmount=" + outPutAmount +
                ", cooldown=" + cooldown +
                ", currentCooldown=" + currentCooldown +
                ", type=" + type +
                ", usageCostAmount=" + usageCostAmount +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", range=" + range +
                '}';
    }
}
