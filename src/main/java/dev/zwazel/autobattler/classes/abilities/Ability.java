package dev.zwazel.autobattler.classes.abilities;

import dev.zwazel.autobattler.classes.RoundAffected;
import dev.zwazel.autobattler.classes.enums.*;
import dev.zwazel.autobattler.classes.units.Unit;
import dev.zwazel.autobattler.classes.utils.Vector;

import java.util.Random;

public abstract class Ability extends RoundAffected {
    private final UsageType costType;
    private final AbilityOutputType outputType;
    private final int outPutAmount;
    private final int cooldown;
    private final Unit owner;
    private final String[] killMessages;
    private final String[] useMessages;
    private Side targetSide;
    private int currentCooldown = 0;
    private AbilityType type;
    private int usageCostAmount;
    private String title;
    private String description;
    private int range;

    public Ability(String title, String description, Unit owner, UsageType costType, int usageCost, AbilityOutputType outputType, int outPutAmount, int cooldown, int range, Side targetSide) {
        this.costType = costType;
        this.cooldown = cooldown;
        this.outputType = outputType;
        this.usageCostAmount = usageCost;
        this.range = range;
        this.outPutAmount = scaleOutputAmount(owner.getLevel() - 1, outPutAmount);
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.targetSide = targetSide;
        this.killMessages = getKillMessages();
        this.useMessages = getUseMessages();
    }

    public abstract String[] getUseMessages();

    public String getRandomUseMessage(Unit target) {
        Random rand = new Random();
        return finalizeString(this.useMessages[rand.nextInt(this.useMessages.length)], target);
    }

    public abstract String[] getKillMessages();

    public String getRandomKillMessage(Unit target) {
        Random rand = new Random();
        return finalizeString(this.killMessages[rand.nextInt(this.killMessages.length)], target);
    }

    private String finalizeString(String s, Unit target) {
        return s.replace("$targetName", target.getName()).replace("$id", "" + target.getID());
    }

    public abstract int scaleOutputAmount(int level, int baseDamage);

    public abstract boolean canBeUsed(Unit target);

    public boolean use(Unit target) {

        return actuallyUse(target);
    }

    public abstract boolean actuallyUse(Unit target);

    public void doOutput(Unit target) {
        switch (this.getOutputType()) {
            case DAMAGE -> {
                target.takeDamage(this);
            }
            case HEAL -> {

            }
            case STAMINA -> {

            }
        }
    }

    public boolean isInRange(Vector target) {
        Double distance = this.getOwner().getGridPosition().getDistanceFrom(target);
        return distance <= this.range;
    }

    public boolean isInRange(Unit target) {
        return (target != null && target.getMyState() != State.DEAD && isInRange(target.getGridPosition()));
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

    public Unit getOwner() {
        return owner;
    }

    public Side getTargetSide() {
        return targetSide;
    }

    public void setTargetSide(Side targetSide) {
        this.targetSide = targetSide;
    }

    @Override
    public String toString() {
        return "Ability{" +
                "title='" + title + '\'' +
                ", targetSide=" + targetSide +
                ", cooldown=" + cooldown +
                ", currentCooldown=" + currentCooldown +
                ", outputAmount=" + outPutAmount +
                ", outputType=" + outputType +
                '}';
    }
}
