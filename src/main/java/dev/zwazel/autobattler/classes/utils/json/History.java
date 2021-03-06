package dev.zwazel.autobattler.classes.utils.json;

import dev.zwazel.autobattler.BattlerGen2;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.utils.Formation;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
// TODO: 16.02.2022 do a complete overhaul of this class
public class History {
    private final BattlerGen2 battler;
    private final Formation left;
    private final Formation right;
    private final ArrayList<ActionHistory> actionHistory;
    private Side winner;

    public History(Formation left, Formation right, BattlerGen2 battler) {
        this.left = left;
        this.right = right;
        this.actionHistory = new ArrayList<>();
        this.battler = battler;
    }

    public void addActionHistory(ActionHistory history) {
        this.actionHistory.add(history);
    }

    @Override
    public String toString() {
        return "History{" +
                "left=" + left +
                ", right=" + right +
                ", actionHistory=" + actionHistory +
                '}';
    }
}
