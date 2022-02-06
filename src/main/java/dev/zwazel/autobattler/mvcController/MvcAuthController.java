package dev.zwazel.autobattler.mvcController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MvcAuthController {
    @GetMapping("/login")
    public String greeting(Model model) {
        return "public/login";
    }
}
