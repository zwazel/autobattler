package dev.zwazel.autobattler.mvcController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
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
        final Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("project.properties"));

            // get the different properties
            final String version = properties.getProperty("program-version");
            final String desc = properties.getProperty("program-description");
            final String groupID = properties.getProperty("program-group-id");
            final String artifactID = properties.getProperty("program-artifact-id");
            final String name = properties.getProperty("program-name");
            final String javaVersion = properties.getProperty("program-java-version");

            ArrayList<String> infos = new ArrayList<>();
            infos.add("Name: " + name);
            infos.add("Artifact ID: " + artifactID);
            infos.add("Version: " + version);
            infos.add("Description: " + desc);
            infos.add("Java Version: " + javaVersion);
            infos.add("Group ID: " + groupID);

            // return the infos
            return infos;
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> infos = new ArrayList<>();
        infos.add("Error while loading project.properties");
        return infos;
    }
}