package dev.zwazel.autobattler.classes.Utils.json;

import com.google.gson.Gson;
import dev.zwazel.autobattler.classes.Utils.Formation;
import dev.zwazel.autobattler.classes.units.Unit;

import java.util.Iterator;

public class HistoryToJson {
    public static String toJson(History history) {
        Gson gson = new Gson();
        String json = "{\"gridSize\":" + gson.toJson(history.getBattler().getGrid().getGridSize()) + ",";

        String jsonLeft = "\"unitsLeft\":";
        jsonLeft += formationToJson(history.getLeft());
        jsonLeft += ",";

        String jsonRight = "\"unitsRight\":";
        jsonRight += formationToJson(history.getRight());
        jsonRight += ",";

        json += jsonLeft;
        json += jsonRight;

        StringBuilder actionHistory = new StringBuilder("\"history\":[");
        Iterator<ActionHistory> iterator = history.getActionHistory().iterator();
        while (iterator.hasNext()) {
            ActionHistory action = iterator.next();
            actionHistory.append(actionHistoryToJson(action));

            if (iterator.hasNext()) {
                actionHistory.append(",");
            }
        }
        actionHistory.append("]");

        json += actionHistory.toString();

        json += "}";

        return json;
    }

    private static String actionHistoryToJson(ActionHistory actionHistory) {
        Gson gson = new Gson();

        String json = "{";

        json += "\"user\":{" +
                "\"id\":" + actionHistory.user().getID() + "," +
                "\"side\":\"" + actionHistory.user().getSide() + "\"" +
                "}";

        json += ",\"target\":{" +
                ((actionHistory.target() == null) ?
                        "\"id\":\"undefined\"" : "\"id\":" + actionHistory.target().getID() + "," +
                        "\"side\":\"" + actionHistory.target().getSide() + "\"") +
                "}";

        json += ",\"ability\":{" +
                ((actionHistory.ability() == null) ?
                        "\"title\":\"undefined\"" : "\"title\":\"" + actionHistory.ability().getTitle() + "\"" +
                        ",\"targetSide\":\"" + actionHistory.ability().getTargetSide() + "\"" +
                        ",\"outPutType\":" + "\"" + actionHistory.ability().getOutputType() + "\""
                ) +
                "}";

        json += ",\"type\":" + "\"" + actionHistory.actionType() + "\"";

        json += ",\"position\":" + gson.toJson(actionHistory.position());

        json += "}";

        return json;
    }

    private static String formationToJson(Formation formation) {
        Gson gson = new Gson();
        StringBuilder json = new StringBuilder("[");

        Iterator<Unit> iterator = formation.getUnits().iterator();
        while (iterator.hasNext()) {
            Unit unit = iterator.next();
            json.append("{");
            json.append("\"id\":").append(unit.getID())
                    .append(",\"type\":").append("\"").append(unit.getType()).append("\"")
                    .append(",\"name\":").append("\"").append(unit.getName()).append("\"")
                    .append(",\"level\":").append(unit.getLevel())
                    .append(",\"priority\":").append(unit.getPriority())
                    .append(",\"position\":").append(gson.toJson(unit.getGridPosition()));
            json.append("}");
            if (iterator.hasNext()) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }
}
