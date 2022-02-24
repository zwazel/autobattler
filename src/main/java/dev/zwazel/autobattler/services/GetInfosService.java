package dev.zwazel.autobattler.services;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Properties;

@RestController()
@RequestMapping()
public class GetInfosService {
    @GetMapping(value = "/", produces = "text/plain")
    public String getInfos() {
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

            // return the infos
            return "Version: " + version + "\n" + "Description: " + desc + "\n" + "Group ID: " + groupID + "\n" + "Artifact ID: " + artifactID + "\n" + "Name: " + name + "\n" + "Java Version: " + javaVersion;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "something went wrong";
    }
}
