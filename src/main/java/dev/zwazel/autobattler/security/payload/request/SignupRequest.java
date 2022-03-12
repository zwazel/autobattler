package dev.zwazel.autobattler.security.payload.request;

import dev.zwazel.autobattler.classes.enums.UserLoginInfos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class SignupRequest {
    @NotBlank
    @Size(min = UserLoginInfos.MIN_USERNAME_LENGTH, max = UserLoginInfos.MAX_USERNAME_LENGTH)
    private String username;

    private Set<String> role;

    @NotBlank
    @Size(min = UserLoginInfos.MIN_PASSWORD_LENGTH, max = UserLoginInfos.MAX_PASSWORD_LENGTH)
    private String password;

    @NotBlank
    @Size(min = UserLoginInfos.MIN_PASSWORD_LENGTH, max = UserLoginInfos.MAX_PASSWORD_LENGTH)
    private String confirmPassword;

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

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public RememberMeTime getRememberMeTime() {
        return rememberMeTime;
    }

    public void setRememberMeTime(RememberMeTime rememberMeTime) {
        this.rememberMeTime = rememberMeTime;
    }

    @Override
    public String toString() {
        return "SignupRequest{" +
                "username='" + username + '\'' +
                ", role=" + role +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", rememberMeTime=" + rememberMeTime +
                '}';
    }
}