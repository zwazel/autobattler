package dev.zwazel.autobattler.classes.Utils.json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Export {
    public void export(History history) throws IOException {
        var exportsFolder = getExportsFolder();

        saveFile(exportsFolder, history);
    }

    private File getExportsFolder() {
        var folder = new File("exports");

        if (folder.exists()) {
            
            return folder;
        }

        var created = folder.mkdirs();

        if (!created) {
            
        }

        return folder;
    }

    private void saveFile(File folder, History history) throws IOException {
        var file = new File(folder.getAbsolutePath() + "/history_" + new SimpleDateFormat("hh_mm_ss").format(new Date()) + ".json");

        var created = file.createNewFile();

        if (!created) {
            
        }

        var fileWriter = new FileWriter(file);

        fileWriter.append(HistoryToJson.toJson(history));
        fileWriter.flush();

        fileWriter.close();
    }
}
