package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.BattlerGen2;
import dev.zwazel.autobattler.classes.abilities.DefaultPunch;
import dev.zwazel.autobattler.classes.abstractClasses.Ability;
import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.enums.Action;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.UnitTypes;
import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.utils.json.ActionHistory;

public class MyFirstUnit extends Unit {
    public MyFirstUnit(long id, int priority, int level, Vector position, String name) {
        super(id, priority, level, UnitTypes.MY_FIRST_UNIT, 'u', position, name);
    }

    public MyFirstUnit(long id, int priority, int level, Vector position, Side side, BattlerGen2 battler, String name) {
        super(id, priority, level, UnitTypes.MY_FIRST_UNIT, 'u', position, side, battler, name);
        this.setAbilities(new Ability[]{new DefaultPunch(this)});
    }

    @Override
    public Ability findSuitableAbility() {
        for (Ability ability : getAbilities()) {
            Unit target = findTargetUnit(ability.getTargetSide());
            if (ability.canBeUsed(target)) {
                return ability;
            }
        }
        return null;
    }

    @Override
    public Unit findTargetUnit(Side side) {
        return getBattler().findClosestOther(this, side, true, false);
    }

    @Override
    public void takeDamage(Ability ability) {
        this.setHealth(this.getHealth() - ability.getOutPutAmount());
        if (this.getHealth() <= 0) {
            die(ability);
        }
    }

    @Override
    public ActionHistory run() {
        Action todoAction = null;
        Unit[] targets = new Unit[0];
        Ability suitableAbility = null;
        Vector[] targetPositions = new Vector[0];

        for (Ability ability : getAbilities()) {
            ability.doRound();
        }

        suitableAbility = findSuitableAbility();

        todoAction = (suitableAbility == null) ? Action.CHASE : Action.USE_ABILITY;
        switch (todoAction) {
            case CHASE -> {
                Unit target = findTargetUnit(this.getSide().getOpposite());
                if (target != null) {
                    targets = new Unit[]{target};
                    targetPositions = moveTowards(targets[0]);
                } else {
                    targets = new Unit[0];
                }
            }
            case USE_ABILITY -> {
                targets = new Unit[]{findTargetUnit(suitableAbility.getTargetSide())};
                useAbility(suitableAbility, targets[0]);
            }
            case RETREAT -> {

            }
        }

        return new ActionHistory(todoAction, this, targets, suitableAbility, targetPositions);
    }
}
