package dev.zwazel.autobattler.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.zwazel.autobattler.classes.Utils.GetFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URISyntaxException;

@RestController()
public class BattleServices {
    @GetMapping(path = "/hello")
    public Response hello() {
        try {
            File file = new GetFile().getFileFromResource("history.json");
            Reader reader = new FileReader(file);
            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            return Response.ok().entity(jsonObject.toString()).build();
        } catch (URISyntaxException | FileNotFoundException e) {
            e.printStackTrace();
        }

        return Response.serverError().build();
    }
}
