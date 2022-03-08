package dev.zwazel.autobattler.classes.exceptions;

import dev.zwazel.autobattler.classes.model.User;

public class UserNotFound extends Exception {
  public UserNotFound(long id) { super("User (" + id + ") not found"); }
  public UserNotFound(User user) {
    super("User " + user.getUsername() + "(" + user.getId() + ") not found");
  }
  public UserNotFound(String username) {
    super("User " + username + " not found");
  }
}
