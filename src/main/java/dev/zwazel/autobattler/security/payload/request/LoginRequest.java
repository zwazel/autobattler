package dev.zwazel.autobattler.security.payload.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private RememberMeTime rememberMeTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RememberMeTime getRememberMeTime() {
        return rememberMeTime;
    }

    public void setRememberMeTime(RememberMeTime rememberMeTime) {
        this.rememberMeTime = rememberMeTime;
    }
}