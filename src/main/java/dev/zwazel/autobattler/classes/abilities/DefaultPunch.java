package dev.zwazel.autobattler.classes.abilities;

import dev.zwazel.autobattler.classes.abstractClasses.Ability;
import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.enums.AbilityOutputType;
import dev.zwazel.autobattler.classes.enums.UsageType;

public class DefaultPunch extends Ability {
    public DefaultPunch(Unit owner) {
        super("DefaultPunch", "Just a basic, default punch ability", owner, UsageType.NOTHING, 0,
                AbilityOutputType.DAMAGE, 5, 1, 1, owner.getSide().getOpposite());
    }

    @Override
    public String[] getUseMessages() {
        return new String[]{
                this.getOwner().getName() + "(" + this.getOwner().getID() + ")" + " punches $targetName($id) and did " + this.getOutPutAmount() + " damage!"
        };
    }

    @Override
    public String[] getKillMessages() {
        return new String[]{
                "$targetName($id) got punched to death by " + this.getOwner().getName() + "(" + this.getOwner().getID() + ")"
        };
    }

    @Override
    public int scaleOutputAmount(int level, int baseDamage) {
        return (int) (baseDamage + (baseDamage * (level * 0.1)));
    }

    @Override
    public boolean canBeUsed(Unit target) {
//        
        boolean cooldownReady = this.getCurrentCooldown() <= 0;
        boolean inRange = this.isInRange(target);
//        
//        
        return cooldownReady && inRange;
    }

    @Override
    protected boolean use(Unit target) {
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
