package me.mafrans.ntiplugin.util;

import me.mafrans.ntiplugin.Base;
import me.mafrans.ntiplugin.NTIPlugin;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Schedule {

    private static HashMap<String, JSONObject> cache = new HashMap<>();

    private File file;
    private String name;
    private JSONObject json;

    public Schedule(String name, File file) throws IOException {
        this.file = file;
        this.name = name;

        if(!cache.containsKey(name)) {
            cacheSchedule(name, file);
        }
        this.json = cache.get(name);
    }

    public void cacheSchedule(String name, File file) throws IOException {
        this.json = new JSONObject(StringUtils.join(NUtil.readFile(file)));
        cache.put(name, this.json);
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public JSONObject getJson() {
        return json;
    }


    private String[] weekdays = new String[] {
            "sunday",
            "monday",
            "tuesday",
            "wednesday",
            "thursday",
            "friday",
            "saturday"
    };

    public boolean hasLesson(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String weekday = weekdays[calendar.get(Calendar.DAY_OF_WEEK)-1];

        JSONArray lessons = json.getJSONArray(weekday);
        for (int i = 0; i < lessons.length(); i++) {
            JSONObject lesson = lessons.getJSONObject(i);
            int starts = getTimeMinutes(lesson.getString("startsAt"));
            int ends = getTimeMinutes(lesson.getString("endsAt"));

            int current = getTimeMinutes(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

            if(starts <= current && current <= ends) {
                return true;
            }
        }
        return false;
    }

    private int getTimeMinutes(String hhmm) {
        String[] split = hhmm.split(":");
        int hours = Integer.parseInt(split[0]);
        int minutes = Integer.parseInt(split[1]);

        return getTimeMinutes(hours, minutes);
    }

    private int getTimeMinutes(int hours, int minutes) {
        return 60*hours + minutes;
    }

    private static String[] availableSchedules = new String[] {
            "EE17",
            "EE18",
            "EE19",
            "TE17",
            "TE18",
            "TE19",
            "ES17",
            "ES18",
            "ES19",
            "HA17",
            "HA18",
            "HA19",
            "TE4",
    };
    public static void saveDefaults() {
        for(String as : availableSchedules) {
            InputStream stream = NTIPlugin.class.getClassLoader().getResourceAsStream("schedules/" + as + ".json");
            File dest = new File(Base.plugin.getDataFolder(),"schedules/" + as + ".json");

            if(stream == null) {
                continue;
            }
            try {
                FileUtils.copyInputStreamToFile(stream, dest);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
