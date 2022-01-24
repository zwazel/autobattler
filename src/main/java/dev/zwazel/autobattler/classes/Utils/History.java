package dev.zwazel.autobattler.classes.Utils;

import java.util.ArrayList;

public class History {
    private final Formation left;
    private final Formation right;

    private final ArrayList<ActionHistory> actionHistory;

    public History(Formation left, Formation right) {
        this.left = left;
        this.right = right;
        this.actionHistory = new ArrayList<>();
    }

    public Formation getLeft() {
        return left;
    }

    public Formation getRight() {
        return right;
    }

    public void addActionHistory(ActionHistory history) {
        this.actionHistory.add(history);
    }

    public ArrayList<ActionHistory> getActionHistory() {
        return actionHistory;
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
