package dev.zwazel.autobattler.mvcController;

import dev.zwazel.autobattler.classes.utils.User;
import dev.zwazel.autobattler.classes.utils.database.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "public/signup";
    }

    @PostMapping("/signup")
    public String addUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "public/signup";
        }

        userRepository.save(user);
        return "redirect:/secured/home";
    }

    // additional CRUD methods
}