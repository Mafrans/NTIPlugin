package me.mafrans.ntiplugin.util;

import me.mafrans.ntiplugin.Base;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Student extends Base {
    private UUID uuid;
    private Player player;
    private OfflinePlayer offlinePlayer;
    private Schedule schedule;

    public Student(Player player) {
        this.uuid = player.getUniqueId();
        this.player = player;
        this.schedule = dbGetSchedule();
    }

    public Student(UUID uuid) {
        this.uuid = uuid;
        this.player = plugin.getServer().getPlayer(uuid);
        this.schedule = dbGetSchedule();

        if(this.player == null) {
            this.offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);
        }
    }

    private Schedule dbGetSchedule() {
        JSONObject json = plugin.database;

        if(!json.has(uuid.toString())) {
            return null;
        }

        JSONObject studentJson = json.getJSONObject(uuid.toString());
        JSONObject schedule = studentJson.getJSONObject("schedule");
        try {
            return new Schedule(schedule.getString("name"), new File(schedule.getString("path")));
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Player getPlayer() {
        return player;
    }

    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }
}
