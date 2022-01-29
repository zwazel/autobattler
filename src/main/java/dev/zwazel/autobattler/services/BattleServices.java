package dev.zwazel.autobattler.services;

import dev.zwazel.autobattler.BattlerGen2;
import dev.zwazel.autobattler.classes.Utils.json.History;
import dev.zwazel.autobattler.classes.Utils.json.HistoryToJson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;

@RestController()
public class BattleServices {
    @GetMapping(path = "/getFightHistory")
    public Response getFightHistory() {
        BattlerGen2 battler = new BattlerGen2(false);
        History history = battler.getHistory();
        return Response.ok().entity(HistoryToJson.toJson(history)).build();
    }
}
