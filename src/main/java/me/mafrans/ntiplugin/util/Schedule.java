package me.mafrans.ntiplugin.util;

import me.mafrans.ntiplugin.Base;
import me.mafrans.ntiplugin.NTIPlugin;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Schedule {

    private static Map<String, JSONObject> cache = new HashMap<>();
    private static Map<UUID, Integer> exemptedPlayers = new HashMap<>();

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

    public static boolean isExempted(Player player) {
        return exemptedPlayers.containsKey(player.getUniqueId());
    }

    public static void updateExempted() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int current = getTimeMinutes(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

        HashMap<UUID, Integer> copy = (HashMap<UUID, Integer>) ((HashMap<UUID, Integer>) exemptedPlayers).clone();
        for(UUID uuid : copy.keySet()) {
            int time = copy.get(uuid);

            if(current > time) {
                exemptedPlayers.remove(uuid);
            }
        }
    }

    public static void exempt(UUID player, String time) {
        exemptedPlayers.put(player, getTimeMinutes(time));
    }

    public static Map<UUID, Integer> getExemptedPlayers() {
        return exemptedPlayers;
    }

    public static int getTimeMinutes(String hhmm) {
        String[] split = hhmm.split(":");
        int hours = Integer.parseInt(split[0]);
        int minutes = Integer.parseInt(split[1]);

        return getTimeMinutes(hours, minutes);
    }

    public static int getTimeMinutes(int hours, int minutes) {
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
