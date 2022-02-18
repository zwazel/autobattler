package dev.zwazel.autobattler.classes.exceptions;

import dev.zwazel.autobattler.classes.model.User;

public class FormationNotFound extends Exception {
    public FormationNotFound(User user) {
        super("Formation for user " + user.getUsername() +"(" +user.getId()+ ") not found");
    }

    public FormationNotFound(User user, Long formationID) {
        super("Formation ("+formationID+") for user " + user.getUsername() +"(" +user.getId()+ ") not found");
    }
}
