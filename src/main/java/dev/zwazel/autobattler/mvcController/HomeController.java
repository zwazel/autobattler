package dev.zwazel.autobattler.mvcController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

@Controller
public class HomeController {
    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("info", getInfos());
        return "index";
    }

    private ArrayList<String> getInfos() {
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("pom.properties");
            Properties p = new Properties();
            p.load(is);
            String version = p.getProperty("version");
            String javaVersion = p.getProperty("java.version");

            ArrayList<String> infos = new ArrayList<>();
            infos.add("Version: " + version);
            infos.add("Java version: " + javaVersion);

            // return the infos
            return infos;
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> infos = new ArrayList<>();
        infos.add("Error while loading pom.properties");
        return infos;
    }
}