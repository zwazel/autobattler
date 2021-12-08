package dev.zwazel.autobattler.classes.abilities;

import dev.zwazel.autobattler.classes.enums.AbilityOutputType;
import dev.zwazel.autobattler.classes.enums.UsageType;
import dev.zwazel.autobattler.classes.units.Unit;

public class DefaultPunch extends Ability {
    public DefaultPunch(Unit owner) {
        super(owner, UsageType.NOTHING, 0, AbilityOutputType.DAMAGE, 5, 10, 1);
    }

    @Override
    public boolean canBeUsed(Unit target) {
        return (this.getCurrentCooldown() == 0) && (this.isInRange(target));
    }

    @Override
    public boolean use(Unit target) {
        if(canBeUsed(target)) {
            this.setCurrentCooldown(this.getCooldown());
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
