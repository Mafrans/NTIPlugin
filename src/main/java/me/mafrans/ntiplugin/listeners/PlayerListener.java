package me.mafrans.ntiplugin.listeners;

import me.mafrans.ntiplugin.Base;
import me.mafrans.ntiplugin.util.NUtil;
import me.mafrans.ntiplugin.util.Schedule;
import me.mafrans.ntiplugin.util.Student;
import me.mafrans.ntiplugin.util.TablistUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;

public class PlayerListener  extends Base implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Student student = new Student(e.getPlayer());
        TablistUtil.setForPlayer(e.getPlayer(), NUtil.colorize(config.getString("tablist.header")), NUtil.colorize(config.getString("tablist.footer")));

        if(Schedule.isExempted(e.getPlayer())) {
            return;
        }

        if(student.getSchedule() == null) {
            e.getPlayer().sendMessage(NUtil.colorize(config.getString("messages.join_unlinked")));
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            return;
        }

        if(student.getSchedule().hasLesson(new Date())) {
            e.setJoinMessage("");
            e.getPlayer().kickPlayer(NUtil.colorize(config.getString("messages.lesson")));
            return;
        }

        if(config.contains("messages.join") && config.getString("messages.join").isEmpty()) {
            e.getPlayer().sendMessage(NUtil.colorize(config.getString("messages.join")));

            plugin.roleHandler.setRole(
                    e.getPlayer(),
                    student.getSchedule().getName(),
                    NUtil.colorize(ChatColor.DARK_GRAY + "[" + ChatColor.RESET + student.getSchedule().getJson().getString("display") + ChatColor.DARK_GRAY + "] " + ChatColor.RESET),
                    "",
                    ChatColor.RESET
            );
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Student student = new Student(e.getPlayer());

        if(student.getSchedule() == null) {
            return;
        }

        if(student.getSchedule().hasLesson(new Date())) {
            e.setQuitMessage("");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Student student = new Student(e.getPlayer());

        if(student.getSchedule() == null) {
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Student student = new Student(e.getPlayer());

        if(student.getSchedule() == null) {
            e.setCancelled(true);
            return;
        }
    }
}
