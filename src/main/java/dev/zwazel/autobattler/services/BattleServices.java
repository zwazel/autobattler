package dev.zwazel.autobattler.services;

import dev.zwazel.autobattler.BattlerGen2;
import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.utils.json.History;
import dev.zwazel.autobattler.classes.utils.json.HistoryToJson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;

@RestController()
public class BattleServices {
    @GetMapping(path = "/getFightHistory")
    public Response getFightHistory() {
        BattlerGen2 battler = new BattlerGen2(false, false, new Vector(10, 10));
        History history = battler.getHistory();
        if (history == null) {
            // return error
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("History doesn't exist!").build();
        }
        return Response.ok().entity(HistoryToJson.toJson(history)).build();
    }
}
