package me.mafrans.ntiplugin.util;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Database extends JSONObject {
    private File file;

    public Database(File file) throws IOException {
        super(read(file));

        this.file = file;
        save();
    }

    public void save() throws IOException {
        file.delete();
        file.createNewFile();

        PrintWriter printWriter = new PrintWriter(file);
        printWriter.println(toString());
        printWriter.close();
    }

    public void addStudent(Student student) {
        JSONObject json = new JSONObject();
        JSONObject scheduleJson = new JSONObject();
        Schedule schedule = student.getSchedule();

        scheduleJson.put("name", schedule.getName());
        scheduleJson.put("path", schedule.getFile().getPath());
        json.put("schedule", scheduleJson);

        put(student.getUUID().toString(), json);

        try {
            save();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String read(File file) throws IOException {
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return "{}";
        }
        return StringUtils.join(NUtil.readFile(file));
    }
}
