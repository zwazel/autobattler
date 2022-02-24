package dev.zwazel.autobattler.services;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

@RestController
@RequestMapping("/api/public/server")
@CrossOrigin(origins = "http://localhost:3000")
public class ServerInfo {

    @RequestMapping(path = "/info", produces = "application/json")
    public String getServerInfo() {
        HashMap<String, String> infos = getInfos();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (String key : infos.keySet()) {
            sb.append("{");
            sb.append("\"name\":\"").append(key).append("\",");
            sb.append("\"value\":\"").append(infos.get(key)).append("\"");
            sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    private HashMap<String, String> getInfos() {
        HashMap<String, String> infos = new HashMap<>();

        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("pom.properties");
            Properties p = new Properties();
            p.load(is);
            String version = p.getProperty("version");
            String javaVersion = p.getProperty("java.version");

            infos.put("Version", version);
            infos.put("Java version", javaVersion);

            // return the infos
            return infos;
        } catch (IOException e) {
            e.printStackTrace();
        }

        infos.put("Error", "Error while loading pom.properties");
        return infos;
    }

}
