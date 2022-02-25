package dev.zwazel.autobattler.security.payload.request;

import dev.zwazel.autobattler.classes.enums.UserLoginInfos;

import java.util.Set;

import javax.validation.constraints.*;

public class SignupRequest {
    @NotBlank
    @Size(min = UserLoginInfos.MIN_USERNAME_LENGTH, max = UserLoginInfos.MAX_USERNAME_LENGTH)
    private String username;

    private Set<String> role;

    @NotBlank
    @Size(min = UserLoginInfos.MIN_PASSWORD_LENGTH, max = UserLoginInfos.MAX_PASSWORD_LENGTH)
    private String password;

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

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }
}