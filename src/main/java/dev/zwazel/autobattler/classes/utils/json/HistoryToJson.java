package dev.zwazel.autobattler.classes.utils.json;

import com.google.gson.Gson;
import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.utils.Formation;
import dev.zwazel.autobattler.classes.utils.Vector;

import java.util.Iterator;

// TODO: 16.02.2022 do a complete overhaul of this class
public class HistoryToJson {
    // TODO: 29.01.2022 ADD WINNER INFORMATION!!
    public static String toJson(History history) {
        Gson gson = new Gson();
        String json = "{\"gridSize\":" + gson.toJson(history.getBattler().getGrid().getGridSize()) + ",";
        json += "\"winner\":\"" + history.getWinner() + "\",";

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

        StringBuilder json = new StringBuilder("{");

        // TODO: 16.02.2022 only write stuff that has changed
        json.append("\"user\":{" + "\"id\":").append(actionHistory.getUser().getID()).append(",")
                .append("\"side\":\"").append(actionHistory.getUser().getSide()).append("\",")
                .append("\"health\":").append(actionHistory.getUser().getHealth()).append(",")
                .append("\"energy\":").append(actionHistory.getUser().getEnergy())
                .append("}");

        json.append(",\"targets\":[");
        int counter = 0;

        // TODO: 16.02.2022 only write stuff that has changed (e.g. health)
        for (Unit unit : actionHistory.getTargets()) {
            json.append("{\"id\":").append(unit.getID()).append(",")
                    .append("\"side\":\"").append(unit.getSide()).append("\",")
                    .append("\"health\":").append(unit.getHealth()).append(",")
                    .append("\"energy\":").append(unit.getEnergy())
                    .append("}");
            if (counter < actionHistory.getTargets().length - 1) {
                counter++;
                json.append(",");
            }
        }
        json.append("]");

        json.append(",\"ability\":{").append((actionHistory.getAbility() == null) ?
                        "\"title\":\"undefined\"" :
                        "\"title\":\"" + actionHistory.getAbility().getTitle() + "\"" +
                                ",\"targetSide\":\"" + actionHistory.getAbility().getTargetSide() + "\"" +
                                ",\"outPutType\":" + "\"" + actionHistory.getAbility().getOutputType() + "\","
                                + "\"outPutAmount\":" + actionHistory.getAbility().getOutPutAmount())
                .append("}");

        json.append(",\"type\":" + "\"").append(actionHistory.getActionType()).append("\"");

        json.append(",\"positions\":[");
        counter = 0;
        for (Vector position : actionHistory.getPositions()) {
            json.append(gson.toJson(position));
            if (counter < actionHistory.getPositions().length - 1) {
                counter++;
                json.append(",");
            }
        }
        json.append("]");

        json.append("}");

        return json.toString();
    }

    public static String formationToJson(Formation formation) {
        StringBuilder json = new StringBuilder("[");

        Iterator<Unit> iterator = formation.getUnits().iterator();
        while (iterator.hasNext()) {
            Unit unit = iterator.next();
            json.append(unitToJson(unit));

            if (iterator.hasNext()) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    public static String unitToJson(Unit unit) {
        Gson gson = new Gson();
        StringBuilder json = new StringBuilder("{");
        json.append("\"id\":").append(unit.getID())
                .append(",\"type\":").append("\"").append(unit.getType()).append("\"")
                .append(",\"name\":").append("\"").append(unit.getName()).append("\"")
                .append(",\"priority\":").append(unit.getPriority())
                .append(",\"position\":").append(gson.toJson(unit.getGridPosition()))
                .append(",\"level\":").append(unit.getLevel());

        if (unit.getSide() != null) {
            json.append(",\"side\":").append("\"").append(unit.getSide()).append("\"");
        }

        json.append("}");
        return json.toString();
    }
}
