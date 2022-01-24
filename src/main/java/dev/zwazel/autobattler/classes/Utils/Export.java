package dev.zwazel.autobattler.classes.Utils;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Export {
    public void export(History history) throws IOException {
//        var exportsFolder = getExportsFolder();
        new Gson().toJson(history);
//        saveFile(exportsFolder, history);
    }

    private File getExportsFolder() {
        var folder = new File("exports");

        if (folder.exists()) {
            System.out.println("Exports folder already exists.");
            return folder;
        }

        var created = folder.mkdirs();

        if (!created) {
            System.out.println("Could not create Exports folder!");
        }

        return folder;
    }

    private void saveFile(File folder, History history) throws IOException {
        var file = new File(folder.getAbsolutePath() + "/history_" + new SimpleDateFormat("hh_mm_ss").format(new Date()) + ".json");

        var created = file.createNewFile();

        if (!created) {
            System.out.println("Could not create file!");
        }

        var fileWriter = new FileWriter(file);

        fileWriter.append(new Gson().toJson(history));
        fileWriter.flush();

        fileWriter.close();
    }
}
