package dev.zwazel.autobattler.security.payload.request;

public enum RememberMeTime {
    ONE_DAY(86_400_000),
    ONE_WEEK(86_400_000 * 7),
    ONE_MONTH(86_400_000L * 30),
    ONE_YEAR(86_400_000L * 365);

    private final long time;

    RememberMeTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
