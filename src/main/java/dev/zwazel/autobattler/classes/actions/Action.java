package dev.zwazel.autobattler.classes.actions;

public enum Action {
    CHASE(() -> {
        System.out.println("Chase");
    }),
    ATTACK(() -> {
        System.out.println("Attack");
    }),
    RETREAT(() -> {
        System.out.println("Retreat");
    });

    private final ActionInterface actionInterface;

    Action(ActionInterface actionInterface) {
        this.actionInterface = actionInterface;
    }

    public ActionInterface getActionInterface() {
        return actionInterface;
    }

    @FunctionalInterface
    public interface ActionInterface {
        void doSomething();
    }
}
