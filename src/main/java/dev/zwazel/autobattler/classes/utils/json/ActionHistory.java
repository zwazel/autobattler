package dev.zwazel.autobattler.classes.utils.json;

import dev.zwazel.autobattler.classes.abstractClasses.Ability;
import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.enums.Action;
import dev.zwazel.autobattler.classes.utils.Vector;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Objects;

public final class ActionHistory {
    private final Action actionType;
    private final Unit user;
    private final Unit[] targets;
    private final Ability ability;
    private final Vector @NotNull [] positions;

    public ActionHistory(Action actionType, Unit user, Unit[] targets,
                         Ability ability, Vector[] positions) {
        if (actionType == null) {
            throw new NullPointerException("actionType cannot be null");
        }
        this.actionType = actionType;

        if (user == null) {
            throw new NullPointerException("user cannot be null");
        }
        this.user = user;

        for (var target : targets) {
            if (target == null) {
                throw new NullPointerException("Target cannot be null");
            }
        }
        this.targets = targets;

        this.ability = ability;

        for (var position : positions) {
            if (position == null) {
                throw new NullPointerException("Position cannot be null");
            }
        }
        this.positions = positions;
    }

    @Override
    public String toString() {
        return "ActionHistory{" +
                "actionType=" + actionType +
                ", user=" + user +
                ", target=" + Arrays.toString(targets) +
                ", ability=" + ability +
                ", position=" + Arrays.toString(positions) +
                '}';
    }

    public @NotNull Action actionType() {
        return actionType;
    }

    public @NotNull Unit user() {
        return user;
    }

    public Unit @NotNull [] targets() {
        return targets;
    }

    public @NotNull Ability ability() {
        return ability;
    }

    public Vector @NotNull [] positions() {
        return positions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ActionHistory) obj;
        return Objects.equals(this.actionType, that.actionType) &&
                Objects.equals(this.user, that.user) &&
                Arrays.equals(this.targets, that.targets) &&
                Objects.equals(this.ability, that.ability) &&
                Arrays.equals(this.positions, that.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionType, user, Arrays.hashCode(targets), ability, Arrays.hashCode(positions));
    }

}
