package me.mafrans.ntiplugin;

import me.mafrans.ntiplugin.commands.Command_link;
import me.mafrans.ntiplugin.listeners.PlayerListener;
import me.mafrans.ntiplugin.util.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class NTIPlugin extends JavaPlugin {
    public Database database;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Base.plugin = this;
        Base.config = getConfig();

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getCommand("link").setExecutor(new Command_link());

        try {
            database = new Database(new File(getDataFolder(), "database.json"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Schedule.saveDefaults();
        startKickTimer();
    }

    public void startKickTimer() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
            {
                public void run()
                {
                    for(Player player : Bukkit.getServer().getOnlinePlayers())
                    {
                        Student student = new Student(player);
                        if(student.getSchedule() == null || student.getSchedule().hasLesson(new Date())) {
                            player.kickPlayer(NUtil.colorize(Base.config.getString("messages.lesson")));
                            return;
                        }
                    }
                }
            }
        , 0, 1200);
    }
}