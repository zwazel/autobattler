package dev.zwazel.autobattler.classes.utils.database;

import java.util.Date;

public interface UserOnly {
    long getId();

    String getUsername();

    Date getAccountCreated();

    Date getLastLogin();
}
