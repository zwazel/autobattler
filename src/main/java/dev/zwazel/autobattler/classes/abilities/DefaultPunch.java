package dev.zwazel.autobattler.classes.abilities;

import dev.zwazel.autobattler.classes.enums.AbilityOutputType;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.UsageType;
import dev.zwazel.autobattler.classes.units.Unit;

public class DefaultPunch extends Ability {
    public DefaultPunch(Unit owner) {
        super("DefaultPunch", "Just a basic, default punch ability", owner, UsageType.NOTHING, 0,
                AbilityOutputType.DAMAGE, 5, 10, 1, Side.ENEMY);
    }

    @Override
    public boolean canBeUsed(Unit target) {
        System.out.println("unit " + this.getOwner().getID() + " checking if ability " + this.getTitle() + " can be used:");
        boolean cooldownReady = this.getCurrentCooldown() <= 0;
        boolean inRange = this.isInRange(target);
        System.out.println("\tCooldown is reset, ability could be used = " + cooldownReady + ", current cooldown = " + this.getCurrentCooldown());
        System.out.println("\tis in range of target " + ((target == null) ? "null" : target.getID()) + " = " + inRange);
        return cooldownReady && inRange;
    }

    @Override
    public boolean use(Unit target) {
        if (canBeUsed(target)) {
            this.setCurrentCooldown(this.getCooldown());
            System.out.println("unit " + getOwner().getID() + " used " + this.getTitle());
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