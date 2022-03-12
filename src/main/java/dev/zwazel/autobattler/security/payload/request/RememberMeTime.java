package dev.zwazel.autobattler.security.payload.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RememberMeTime {
    ONE_DAY("1 Day", 86_400_000),
    ONE_WEEK("1 Week", 86_400_000 * 7),
    ONE_MONTH("1 Month", 86_400_000L * 30),
    ONE_YEAR("1 Year", 86_400_000L * 365);

    private final String text;
    private final long time;

    RememberMeTime(String text, long time) {
        this.text = text;
        this.time = time;
    }

    @JsonCreator
    public static RememberMeTime decode(final String name) {
        for (RememberMeTime value : RememberMeTime.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }

    public long getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    @JsonValue
    public String getName() {
        return name();
    }
}
