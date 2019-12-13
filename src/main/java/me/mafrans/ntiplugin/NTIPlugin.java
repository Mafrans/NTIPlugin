package me.mafrans.ntiplugin;

import me.mafrans.ntiplugin.commands.Command_exempt;
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
    public boolean debug;
    public RoleHandler roleHandler;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Base.plugin = this;
        Base.config = getConfig();
        Base.logger = getLogger();

        if(Base.config.contains("debug")) {
            debug = Base.config.getBoolean("debug");
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getCommand("link").setExecutor(new Command_link());
        getCommand("exempt").setExecutor(new Command_exempt());

        try {
            database = new Database(new File(getDataFolder(), "database.json"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        roleHandler = new RoleHandler();

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
                        if(Schedule.isExempted(player)) continue;

                        Student student = new Student(player);
                        if(student.getSchedule() == null || student.getSchedule().hasLesson(new Date())) {
                            player.kickPlayer(NUtil.colorize(Base.config.getString("messages.lesson")));
                        }
                    }
                    Schedule.updateExempted();
                }
            }
        , 0, 1200);
    }
}
