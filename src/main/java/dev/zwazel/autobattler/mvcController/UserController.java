package dev.zwazel.autobattler.mvcController;

import dev.zwazel.autobattler.classes.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "public/signup";
    }

    @GetMapping("/signin")
    public String showSignInForm(User user) {
        return "public/login";
    }

    // additional CRUD methods
}