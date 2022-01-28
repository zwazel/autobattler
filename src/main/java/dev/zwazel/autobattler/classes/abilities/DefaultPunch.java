package dev.zwazel.autobattler.classes.abilities;

import dev.zwazel.autobattler.classes.enums.AbilityOutputType;
import dev.zwazel.autobattler.classes.enums.UsageType;
import dev.zwazel.autobattler.classes.units.Unit;

public class DefaultPunch extends Ability {
    public DefaultPunch(Unit owner) {
        super("DefaultPunch", "Just a basic, default punch ability", owner, UsageType.NOTHING, 0,
                AbilityOutputType.DAMAGE, 5, 1, 1, owner.getSide().getOpposite());
    }

    @Override
    public String[] getUseMessages() {
        return new String[]{
                this.getOwner().getName() + " punches $targetName and did " + this.getOutPutAmount() + " damage!"
        };
    }

    @Override
    public String[] getKillMessages() {
        return new String[]{
                "$targetName got punched to death by " + this.getOwner().getName()
        };
    }

    @Override
    public int scaleOutputAmount(int level, int baseDamage) {
        return (int) (baseDamage + (baseDamage * (level * 0.1)));
    }

    @Override
    public boolean canBeUsed(Unit target) {
//        System.out.println("unit " + this.getOwner().getID() + " checking if ability " + this.getTitle() + " can be used:");
        boolean cooldownReady = this.getCurrentCooldown() <= 0;
        boolean inRange = this.isInRange(target);
//        System.out.println("\tCooldown is reset, ability could be used = " + cooldownReady + ", current cooldown = " + this.getCurrentCooldown());
//        System.out.println("\tis in range of target " + ((target == null) ? "null" : target.getID()) + " = " + inRange);
        return cooldownReady && inRange;
    }

    @Override
    public boolean actuallyUse(Unit target) {
        if (canBeUsed(target)) {
            this.setCurrentCooldown(this.getCooldown());
            doOutput(target);
            return true;
        }
        return false;
    }

    @Override
    public void doRound() {
        if (this.getCurrentCooldown() > 0) {
            this.setCurrentCooldown(this.getCurrentCooldown() - 1);
        }
    }
}
