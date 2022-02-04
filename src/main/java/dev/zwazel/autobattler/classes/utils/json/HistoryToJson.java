package dev.zwazel.autobattler.classes.utils.json;

import com.google.gson.Gson;
import dev.zwazel.autobattler.classes.utils.Formation;
import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.units.Unit;

import java.util.Iterator;

public class HistoryToJson {
    // TODO: 29.01.2022 ADD WINNER INFORMATION!!
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

    // TODO: 29.01.2022 ADD OUTPUT AMOUNT
    private static String actionHistoryToJson(ActionHistory actionHistory) {
        Gson gson = new Gson();

        StringBuilder json = new StringBuilder("{");

        json.append("\"user\":{" + "\"id\":").append(actionHistory.user().getID()).append(",").append("\"side\":\"").append(actionHistory.user().getSide()).append("\"").append("}");

//        
//        

        json.append(",\"targets\":[");
        int counter = 0;
        for (Unit unit : actionHistory.targets()) {
            json.append("{\"id\":").append(unit.getID()).append(",").append("\"side\":\"").append(unit.getSide()).append("\"").append("}");
            if (counter < actionHistory.targets().length - 1) {
                counter++;
                json.append(",");
            }
        }
        json.append("]");

        json.append(",\"ability\":{").append((actionHistory.ability() == null) ?
                "\"title\":\"undefined\"" : "\"title\":\"" + actionHistory.ability().getTitle() + "\"" +
                ",\"targetSide\":\"" + actionHistory.ability().getTargetSide() + "\"" +
                ",\"outPutType\":" + "\"" + actionHistory.ability().getOutputType() + "\"").append("}");

        json.append(",\"type\":" + "\"").append(actionHistory.actionType()).append("\"");

        json.append(",\"positions\":[");
        counter = 0;
        for (Vector position : actionHistory.positions()) {
            json.append(gson.toJson(position));
            if (counter < actionHistory.positions().length - 1) {
                counter++;
                json.append(",");
            }
        }
        json.append("]");

        json.append("}");

        return json.toString();
    }

    // TODO: 29.01.2022 ADD HEALTH AND ENERGY 
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
                    .append(",\"position\":").append(gson.toJson(unit.getGridPosition()))
                    .append(",\"side\":\"").append(unit.getSide()).append("\"");
            json.append("}");
            if (iterator.hasNext()) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }
}
