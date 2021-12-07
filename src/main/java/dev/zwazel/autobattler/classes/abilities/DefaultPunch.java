package dev.zwazel.autobattler.classes.abilities;

import dev.zwazel.autobattler.classes.enums.AbilityOutputType;
import dev.zwazel.autobattler.classes.enums.UsageType;

public class DefaultPunch extends Ability {
    public DefaultPunch() {
        super(UsageType.NOTHING, 0, AbilityOutputType.DAMAGE, 5, 10, 1);
    }

    @Override
    public boolean canBeUsed() {
        return (this.getCurrentCooldown() == 0);
    }

    @Override
    public boolean use() {
        if(canBeUsed()) {
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
