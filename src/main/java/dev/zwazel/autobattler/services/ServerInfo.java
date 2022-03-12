package dev.zwazel.autobattler.services;

import dev.zwazel.autobattler.classes.enums.UserLoginInfos;
import dev.zwazel.autobattler.security.payload.request.RememberMeTime;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/api/public/server")
public class ServerInfo {

    @RequestMapping(path = "/info", produces = "application/json")
    public String getServerInfo() {
        HashMap<String, String> infos = getServerInfos();
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

    @RequestMapping(path = "/userLoginInfo", produces = "application/json")
    public String getUserLoginInfo() {
        StringBuilder rememberMeTime = new StringBuilder();
        Iterator<RememberMeTime> rememberMeTimeIterator = List.of(RememberMeTime.values()).iterator();
        while (rememberMeTimeIterator.hasNext()) {
            RememberMeTime next = rememberMeTimeIterator.next();
            String timeText = "{";

            timeText += "\"text\":\"" + next.getText() + "\",\"time\":" + next.getTime() + ",\"ordinal\":" + next.ordinal() + ",\"name\":\"" + next.name() + "\"";

            timeText += "}";
            if (rememberMeTimeIterator.hasNext()) {
                timeText += ",";
            }

            rememberMeTime.append(timeText);
        }

        return "{" +
                "\"usernameMinLength\":" + UserLoginInfos.MIN_USERNAME_LENGTH + "," +
                "\"usernameMaxLength\":" + UserLoginInfos.MAX_USERNAME_LENGTH + "," +
                "\"passwordMinLength\":" + UserLoginInfos.MIN_PASSWORD_LENGTH + "," +
                "\"passwordMaxLength\":" + UserLoginInfos.MAX_PASSWORD_LENGTH + "," +
                "\"rememberMeTimes\":" + "[" + rememberMeTime + "]" +
                "}";
    }

    private HashMap<String, String> getServerInfos() {
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
